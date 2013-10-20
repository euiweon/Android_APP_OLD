package com.pricecode;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XResultMap;

public class MyMileageInfoActivity extends CMActivity
{
	ListView m_ListView;
	TextView m_tvMyMileage, m_tvAllMileage, m_tvMyLevel;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_mileage_info);

		setTitleBarText("내 마일리지 정보");

		m_ListView = (ListView) findViewById(R.id.listView_my_mileage);
		m_tvMyMileage = (TextView) findViewById(R.id.textView_my_mileage);
		m_tvAllMileage = (TextView) findViewById(R.id.textView_all_mileage);
		m_tvMyLevel = (TextView) findViewById(R.id.textView_my_level);

		String strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
		String strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");
		if( strId.length() == 0 || strPassword.length() == 0 )
		{
			showAlert("로그인 정보가 없습니다.");
			return;
		}

		m_tvAllMileage.setText("총마일리지 : " + m_strAllMileage);
		m_tvMyMileage.setText("현재마일리지 : " + m_strMyMileage);
		m_tvMyLevel.setText("회원님의 등급은 '" + m_strMyLevel + "'입니다.");

		if( CMHttpConn.reqMyMileageInfo(m_Handler, strId, strPassword) == null )
			toastCheckInternet();
		else
			showWait(null);
	}

	@Override
	protected void onDestroy()
	{
		m_Handler = null;
		super.onDestroy();
	}

	XHandler m_Handler = new XHandler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			removeTimeoutMsg();
			if( m_Handler == null )
				return;

			if( msg.what == XHandler.RESULT_TIME_OUT )
			{
				cancelWait();
				toastCheckInternet();
				return;
			}
			else
			{
				cancelWait();
				if( msg.obj != null && msg.obj instanceof CMHttpConn )
				{
					CMHttpConn conn = (CMHttpConn) msg.obj;
					if( conn.m_nWebOpenResult == CMHttpConn.XHTTP_200_OK )
					{
						XResultList resultList = conn.m_XmlParser.getResultList();
						if( resultList != null && resultList.size() > 0 )
						{
							if( updateResultList(resultList) )
								return;
						}

						String strError = null;
						strError = resultList.getOtherValue("errorMsg");
						if( strError != null )
							showAlert(strError);
						else
							toastResultIsEmpty();
						return;
					}
				}

				toastCheckInternet();
				return;
			}
		}
	};

	ArrayAdapter<XResultMap> m_Adapter = null;
	XResultList m_ResultList = null, m_PointList = null;
	String m_strMyMileage = "0", m_strAllMileage = "0", m_strMyLevel = "인턴";

	protected boolean updateResultList( XResultList resultList )
	{
		if( resultList == null || resultList.size() == 0 )
			return false;

		m_ResultList = resultList;

		m_strAllMileage = m_ResultList.getOtherValue("total_point");
		m_strMyMileage = m_ResultList.getOtherValue("remain_point");
		m_strMyLevel = m_ResultList.getOtherValue("member_grade");

		if( m_strAllMileage != null && m_strAllMileage.length() > 0 )
			m_tvAllMileage.setText("총마일리지 : " + m_strAllMileage);
		if( m_strMyMileage != null && m_strMyMileage.length() > 0 )
			m_tvMyMileage.setText("현재마일리지 : " + m_strMyMileage);
		if( m_strMyLevel != null && m_strMyLevel.length() > 0 )
			m_tvMyLevel.setText("회원님의 등급은 '" + m_strMyLevel + "'입니다.");

		m_Adapter = new ArrayAdapter<XResultMap>(this, 0, m_ResultList)
		{
			LayoutInflater m_LayoutInflater = null;

			@Override
			public View getView( int position, View convertView, ViewGroup parent )
			{
				if( convertView == null )
				{
					if( m_LayoutInflater == null )
						m_LayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = m_LayoutInflater.inflate(R.layout.list_item_my_mileage_info, null);
				}
				if( convertView == null || m_ResultList == null )
					return null;

				TextView tvDate = (TextView) convertView.findViewById(R.id.textView_date);
				TextView tvHistory = (TextView) convertView.findViewById(R.id.textView_history);
				TextView tvPoint = (TextView) convertView.findViewById(R.id.textView_point);
				if( tvDate == null || tvHistory == null || tvPoint == null )
					return convertView;

				XResultMap result = m_ResultList.get(position);
				if( result != null )
				{
					String strDate = null, strHistory = null, strPoint = null;
					strDate = result.get("date");
					strHistory = result.get("subject");
					strPoint = result.get("point");

					if( strDate == null )
						strDate = " ";
					else
					{
						int nFound = strDate.indexOf(" ");
						if( nFound > 0 )
							strDate = strDate.substring(0, nFound);
					}
					if( strHistory == null )
						strHistory = " ";
					if( strPoint == null )
						strPoint = " ";

					tvDate.setText(strDate);
					tvHistory.setText(strHistory);
					tvPoint.setText(strPoint);
				}

				return convertView;
			}

		};

		m_ListView.setAdapter(m_Adapter);
		
		setListViewHeightBasedOnChildren();

		return true;
	}

	public void setListViewHeightBasedOnChildren()
	{
		if( m_Adapter == null )
			return;

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(m_ListView.getWidth(), MeasureSpec.AT_MOST);
		for( int i = 0; i < m_Adapter.getCount(); i++ )
		{
			View listItem = m_Adapter.getView(i, null, m_ListView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = m_ListView.getLayoutParams();
		params.height = totalHeight + (m_ListView.getDividerHeight() * (m_Adapter.getCount() - 1));
		m_ListView.setLayoutParams(params);
		m_ListView.requestLayout();
	}
}

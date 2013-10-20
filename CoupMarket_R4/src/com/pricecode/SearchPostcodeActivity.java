package com.pricecode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultAdapter;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XResultMap;

public class SearchPostcodeActivity extends CMActivity implements OnClickListener
{
	LinearLayout m_layoutBefore, m_layoutAfter;
	EditText m_etDong;
	ImageButton m_btnSearch;
	ListView m_ListView;

	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_search_postcode);
	    
	    setTitleBarText("우편번호 찾기");
	    
	    m_layoutBefore = (LinearLayout) findViewById(R.id.linearLayout_before_search);
	    m_layoutAfter = (LinearLayout) findViewById(R.id.linearLayout_after_search);
	    
	    m_etDong = (EditText) findViewById(R.id.editText_dong);
	    m_btnSearch = (ImageButton) findViewById(R.id.imageButton_search);
	    m_ListView = (ListView) findViewById(R.id.listView_postcode);
	    
	    m_btnSearch.setOnClickListener(this);
	    
	    m_layoutAfter.setVisibility(View.GONE);
    }

	@Override
    protected void onDestroy()
    {
		m_Handler = null;
		m_ResultList = null;
	    super.onDestroy();
    }

	@Override
    public void onClick( View v )
    {
	    if(v.getId() == R.id.imageButton_search)
	    {
	    	String strDong = m_etDong.getText().toString();
	    	if(strDong.length() == 0)
	    		return;
	    	if(CMHttpConn.reqPostcodeList(m_Handler, strDong) == null)
	    		toastRequestFail();
	    	else
	    		showWait(null);
	    }
    }

	XHandler m_Handler = new XHandler()
	{
		@Override
        public void handleMessage( Message msg )
        {
			removeTimeoutMsg();
			if(m_Handler == null)
				return;
			
	        if(msg.what == XHandler.RESULT_TIME_OUT)
	        {
	        	cancelWait();
	        	toastCheckInternet();
	        	return;
	        }
	        else if(msg.what == CMHttpConn.TYPE_REQ_POSTCODE_LIST)
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
							updateResultList(resultList);
							return;
						}
						
						toastResultIsEmpty();
						return;
					}
				}

				toastCheckInternet();
				return;
			}
        }
	};

	XResultList m_ResultList = null;
	void updateResultList( XResultList resultList )
    {
		m_ResultList = resultList;
		if( m_ResultList == null || m_ResultList.size() == 0 )
		{
			toastResultIsEmpty();
			return;
		}
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(m_etDong.getWindowToken(), 0);
		
		m_layoutBefore.setVisibility(View.GONE);
		m_layoutAfter.setVisibility(View.VISIBLE);

 
		XResultAdapter adapter = new XResultAdapter(this, R.layout.list_item_postcode,
		                                            R.id.textView_postcode, "zipcode", m_ResultList)
		{
			LayoutInflater m_LayoutInflater = null;

			@Override
            protected View bindView( int position, View convertView, ViewGroup parent, int nLayout,
                                     int nChild )
            {
				if( convertView == null )
				{
					if( m_LayoutInflater == null )
						m_LayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = m_LayoutInflater.inflate(nLayout, null);
				}
				if( convertView == null || m_ResultList == null )
					return null;

				TextView tvPostcode = (TextView) convertView.findViewById(R.id.textView_postcode);
				TextView tvAddress = (TextView) convertView.findViewById(R.id.textView_address);
				if( tvPostcode == null || tvAddress == null )
					return convertView;

				String strPostcode = null, strAddress = "";
				XResultMap result = m_ResultList.get(position);
				if( result != null )
				{
					strPostcode = result.get("zipcode");
					if( strPostcode == null )
						strPostcode = "";
					
					String strSiDo = null, strGuGun = null, strDong = null, strRi = null, strBunji = null;
					strSiDo = result.get("sido");
					strGuGun = result.get("gugun");
					strDong = result.get("dong");
					strRi = result.get("ri");
					strBunji = result.get("bunji");
					
					if(strSiDo != null)
						strAddress += strSiDo;
					if(strGuGun != null)
						strAddress += " " + strGuGun;
					if(strDong != null)
						strAddress += " " + strDong;
					if(strRi != null)
						strAddress += " " + strRi;
					if(strBunji != null)
						strAddress += " " + strBunji;
				}
				
				tvPostcode.setText(strPostcode);
				tvAddress.setText(strAddress);

				return convertView;
            }
			
		};

		m_ListView.setAdapter(adapter);

		m_ListView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick( AdapterView<?> parent, View view, int position, long id )
			{
				String strPostcode = null, strAddress = "";
				XResultMap result = m_ResultList.get(position);
				if( result != null )
				{
					strPostcode = result.get("zipcode");
					if( strPostcode == null )
						strPostcode = "";
					
					String strSiDo = null, strGuGun = null, strDong = null, strRi = null, strBunji = null;
					strSiDo = result.get("sido");
					strGuGun = result.get("gugun");
					strDong = result.get("dong");
					strRi = result.get("ri");
					strBunji = result.get("bunji");
					
					if(strSiDo != null)
						strAddress += strSiDo;
					if(strGuGun != null)
						strAddress += " " + strGuGun;
					if(strDong != null)
						strAddress += " " + strDong;
					if(strRi != null)
						strAddress += " " + strRi;
					if(strBunji != null)
						strAddress += " " + strBunji;
				}
				
				Intent resultData = new Intent();
				resultData.putExtra("postcode", strPostcode);
				resultData.putExtra("address", strAddress);
				setResult(RESULT_OK, resultData);
				finish();
			}
		});
    }
}

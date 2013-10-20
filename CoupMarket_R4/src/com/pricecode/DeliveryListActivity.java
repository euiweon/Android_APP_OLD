package com.pricecode;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pricecode.*;
import com.rangboq.xutil.XFileDownloader;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultAdapter;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XResultMap;

public class DeliveryListActivity extends CMActivity
{
	ListView m_ListView;
	
	String m_strMyArea;
	Spinner m_spArea1, m_spArea2;
	String m_strArea1 = "", m_strArea2 = "";

	String m_strCategoryName = "배달";
	int m_nCategoryNo = 0;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery_list);
		
		Intent curIntent = getIntent();
		if(curIntent != null)
		{
			m_nCategoryNo = curIntent.getIntExtra("category_no", 0);
			m_strCategoryName = curIntent.getStringExtra("category_name");
			if(m_strCategoryName == null)
				m_strCategoryName = "배달";
		}
		
		setTitleBarText(m_strCategoryName);
		
		m_ListView = (ListView) findViewById(R.id.listView_shop);
	    
	    View moreView = getLayoutInflater().inflate(R.layout.list_footer_more, null);
	    m_ListView.addFooterView(moreView);
	    
	    ImageView ivMore = (ImageView) moreView.findViewById(R.id.ImageView_more);
	    ivMore.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
			    requestDeliveryListByArea();
			}
		});
	    
	    m_strMyArea = CMManager.getMyInfo("area");
	    if(m_strMyArea == null)
	    {
	    	showAlert("지역설정정보가 없습니다.");
	    	return;
	    }
	    
		m_spArea1 = (Spinner) findViewById(R.id.spinner_area1);
		m_spArea2 = (Spinner) findViewById(R.id.spinner_area2);
		
		int nFound = m_strMyArea.indexOf("-");
		if(nFound <= 0 || m_strMyArea.length() <= nFound)
		{
			showAlert("지역설정값이 올바르지않습니다.");
			return;
		}
		
		m_strArea1 = m_strMyArea.substring(0, nFound);
		m_strArea2 = m_strMyArea.substring(nFound + 1);
		
		XResultList areaList = CMManager.getAreaList();
		if(areaList == null || areaList.size() == 0)
		{
			showAlert("지역목록정보가 없습니다.");
			return;
		}
			
		updateAreaList(areaList);
	}

	@Override
	protected void onDestroy()
	{
		m_Handler = null;
		super.onDestroy();
	}


	static final int MSG_FOR_NOTIFY_PHOTO_DOWNLOADED = 1;
	XHandler m_Handler = new XHandler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			removeTimeoutMsg();
			if( m_Handler == null )
				return;

			if(msg.what == MSG_FOR_NOTIFY_PHOTO_DOWNLOADED)
			{
				if(m_ListAdapter != null)
					m_ListAdapter.notifyDataSetChanged();
				return;
			}
			else if( msg.what == XHandler.RESULT_TIME_OUT )
			{
				cancelWait();
				toastCheckInternet();
				return;
			}
			else if( msg.what == CMHttpConn.TYPE_REQ_DELIVERY_LIST )
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
	ArrayAdapter<XResultMap> m_ListAdapter = null;
	protected boolean updateResultList( XResultList resultList )
	{
		if(resultList == null || resultList.size() == 0)
			return false;
		
		if(m_nLastNo != -1 && m_ResultList != null && m_ListView != null && m_ListAdapter != null)
		{
			m_ResultList.addAll(resultList);
			updateLastNnumber();
			m_ListAdapter.notifyDataSetChanged();
			return true;
		}
		
		m_ResultList = resultList;
		updateLastNnumber();

		m_ListAdapter = new ArrayAdapter<XResultMap>(this, 0, m_ResultList)
		{
			LayoutInflater m_LayoutInflater = null;

			@Override
			public View getView( int position, View convertView, ViewGroup parent )
			{
				if( convertView == null )
				{
					if( m_LayoutInflater == null )
						m_LayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = m_LayoutInflater.inflate(R.layout.list_item_delivery, null);
				}
				if( convertView == null || m_ResultList == null )
					return null;

				ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.imageView_photo);
				ImageView ivMiniMenu = (ImageView) convertView.findViewById(R.id.imageView_menu);
				ImageView ivMiniCard = (ImageView) convertView.findViewById(R.id.imageView_card);
				TextView tvCoopName = (TextView) convertView.findViewById(R.id.textView_coop_name);
				TextView tvPoint = (TextView) convertView.findViewById(R.id.textView_point);
				TextView tvDeliveryArea = (TextView) convertView.findViewById(R.id.textView_delivery_area);
				TextView tvWorkTime = (TextView) convertView.findViewById(R.id.textView_work_time);
				if( ivPhoto == null || ivMiniMenu == null || ivMiniCard == null || tvCoopName == null || tvDeliveryArea == null || tvPoint == null || tvWorkTime == null )
					return convertView;

				XResultMap result = m_ResultList.get(position);
				if( result != null )
				{
					String strCoopName = null, strPoint = null, strDeliveryArea = null, strWorkTime = null, strUseCard = null;
					strCoopName = result.get("comp_name");
					strPoint = result.get("point");
					strDeliveryArea = result.get("address");
					strWorkTime = result.get("work_hour");
					strUseCard = result.get("use_card");

					if( strCoopName == null )
						strCoopName = "";
					if( strDeliveryArea == null )
						strDeliveryArea = "";
					if( strPoint == null )
						strPoint = "";
					if( strWorkTime == null )
						strWorkTime = "";
					if( strUseCard == null )
						strUseCard = "";
					
					if(strDeliveryArea.length() > 0)
					{
						int nFound = strDeliveryArea.indexOf("||");
						if(nFound > 0)
							strDeliveryArea = strDeliveryArea.substring(0, nFound);
					}

					tvCoopName.setText(strCoopName);
					tvPoint.setText(strPoint + "원");
					tvDeliveryArea.setText(strDeliveryArea);
					tvWorkTime.setText(strWorkTime);
					if(strUseCard == null || strUseCard.equals("Y") == false)
						ivMiniCard.setVisibility(View.INVISIBLE);
					ivMiniMenu.setOnClickListener(m_onClickInList);
					ivMiniMenu.setTag(Integer.valueOf(position));

					Bitmap photoBitmap = null;
					String strThumbUrl = result.get("goods_img");
					if(strThumbUrl != null && strThumbUrl.length() > 0)
					{
    					String strThumbName = XFileDownloader.getLastPart(strThumbUrl);
		    			String strSubDir = CMManager.getImageLocalSubPath(strThumbUrl);
    					String strThumDir = CMManager.ImageThumbPath + strSubDir;
    					String strThumPath = strThumDir + strThumbName;
    					File file = new File(strThumPath);
    					if( file.exists() )
    					{
    						try
    						{
    							photoBitmap = BitmapFactory.decodeFile(strThumPath);
    							if(photoBitmap != null)
    								ivPhoto.setImageBitmap(photoBitmap);
        						photoBitmap = null;
    						}
    						catch( OutOfMemoryError e )
    						{
    							e.printStackTrace();
    						}
    					}
    					else
    					{
    						XFileDownloader.downloadFileFromWeb(getApplicationContext(), strThumbUrl, strThumDir, true, 
    						                                    m_Handler, MSG_FOR_NOTIFY_PHOTO_DOWNLOADED);
    					}
					}
				}

				return convertView;
			}
		};

		m_ListView.setAdapter(m_ListAdapter);

		m_ListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id )
			{
				if(m_ResultList == null || m_ResultList.size() <= position)
					return;
				
				XResultMap selectedItem = m_ResultList.get(position);
				if(selectedItem == null)
					return;
				
				Intent intent = CMManager.getIntent(DeliveryListActivity.this, DeliveryDetailActivity.class);
			    intent.putExtra("category_name", m_strCategoryName);
			    intent.putExtra("category_no", m_nCategoryNo);
				intent.putExtra("item_info", selectedItem);
				startActivity(intent);
			}
		});

		return true;
	}
	
	
	protected void requestDeliveryListByArea()
    {
		String strArea = "";
		if(m_strArea2 != null && m_strArea2.length() > 0)
			strArea = m_strArea1 + "-" + m_strArea2;
		else if(m_strArea1 != null && m_strArea1.length() > 0)
			strArea = m_strArea1;
	    
		if( CMHttpConn.reqDeliveryList(m_Handler, strArea, m_nCategoryNo, m_nLastNo, m_nReqCount) == null )
			toastCheckInternet();
		else
			showWait(null);
    }


	int m_nLastNo = -1, m_nReqCount = 10;
	private void updateLastNnumber()
    {
		m_nLastNo = -1;
	    if(m_ResultList == null || m_ResultList.size() == 0)
	    	return;
	    
	    for(int nIndex=m_ResultList.size()-1; nIndex>=0; nIndex--)
	    {
	    	XResultMap result = m_ResultList.get(nIndex);
	    	if(result == null)
	    		continue;
	    	String strNo = result.get("no");
	    	if(strNo == null || strNo.length() == 0)
	    		continue;
	    	try
	    	{
	    		m_nLastNo = Integer.parseInt(strNo);
	    		if(m_nLastNo > 0)
	    			break;
	    	}
	    	catch(NumberFormatException e)
	    	{
	    		e.printStackTrace();
	    		continue;
	    	}
	    }
    }
	
	OnClickListener m_onClickInList = new OnClickListener()
	{
		@Override
		public void onClick( View v )
		{
			if(v.getId() == R.id.imageView_menu)
			{
				if(m_ResultList == null || m_ResultList.size() == 0)
					return;
				
				Object tag = v.getTag();
				if(tag == null || (tag instanceof Integer) == false)
					return;
				
				int nPosition = ((Integer)tag).intValue();
				XResultMap selectedItem = m_ResultList.get(nPosition);
				if(selectedItem == null)
					return;
				
				Intent intent = CMManager.getIntent(DeliveryListActivity.this, ShowMenuActivity.class);
				intent.putExtra("category_name", m_strCategoryName);
				intent.putExtra("item_info", selectedItem);
				startActivity(intent);
			}
		}
	};
	
	XResultList m_AreaResultList = null, m_AreaList1 = null, m_AreaList2 = null;
	boolean updateAreaList( XResultList resultList )
	{
		m_AreaResultList = resultList;
		if( m_AreaResultList == null )
			m_AreaResultList = new XResultList();
		
		m_AreaList1 = new XResultList();
		XResultMap defaultMap = new XResultMap();
		defaultMap.put("subject", "전체지역");
		m_AreaList1.add(defaultMap);
		for( XResultMap result : m_AreaResultList )
		{
			String strPNo = result.get("pno");
			if( strPNo == null || strPNo.length() == 0 )
				continue;

			if( strPNo.equals("0") )
				m_AreaList1.add(result);
		}
		
		XResultAdapter adapter1 = new XResultAdapter(this, android.R.layout.simple_spinner_item,
		                                             android.R.id.text1, "subject", m_AreaList1);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item,
		                                 android.R.id.text1);
		m_spArea1.setAdapter(adapter1);
		m_spArea1.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int pos, long id )
			{
				if( m_AreaList1 == null )
					return;

				XResultMap result1 = m_AreaList1.get(pos);
				if( result1 == null )
					return;

				String strNo1 = result1.get("no");
				if( strNo1 == null )
					strNo1 = "";

				m_AreaList2 = new XResultList();
				XResultMap defaultMap = new XResultMap();
				defaultMap.put("subject", "전체지역");
				m_AreaList2.add(defaultMap);
				if(strNo1.length() > 0)
				{
					m_spArea2.setEnabled(true);
					
    				for( XResultMap resultOfAll : m_AreaResultList )
    				{
    					String strPNo = resultOfAll.get("pno");
    					if( strPNo == null || strPNo.length() == 0 )
    						continue;
    
    					if( strPNo.equals(strNo1) )
    						m_AreaList2.add(resultOfAll);
    				}
				}
				else
				{
					m_spArea2.setEnabled(false);
				}

				XResultAdapter adapter2 = new XResultAdapter(DeliveryListActivity.this,
				                                             android.R.layout.simple_spinner_item,
				                                             android.R.id.text1, "subject", m_AreaList2);
				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item,
				                                 android.R.id.text1);
				m_spArea2.setAdapter(adapter2);
				m_spArea2.setOnItemSelectedListener(new OnItemSelectedListener()
				{
					@Override
                    public void onItemSelected( AdapterView<?> parent, View view, int pos, long id )
                    {
						applyArea();
                    }

					@Override
                    public void onNothingSelected( AdapterView<?> parent )
                    {
                    }
				});
				
				int nArea2Index = Spinner.NO_ID;
				for( int nIndex=0; nIndex<m_AreaList2.size(); nIndex++ )
				{
					XResultMap result2 = m_AreaList2.get(nIndex);
					String strNo2 = result2.get("no");
					if( strNo2 == null || strNo2.length() == 0 )
						continue;

					if( strNo2.equals(m_strArea2) )
					{
						nArea2Index = nIndex;
						break;
					}
				}
				m_spArea2.setSelection(nArea2Index);
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent )
			{
				m_spArea2.setAdapter(null);
			}
		});

		int nArea1Index = Spinner.NO_ID;
		for( int nIndex=0; nIndex<m_AreaList1.size(); nIndex++ )
		{
			XResultMap result = m_AreaList1.get(nIndex);
			String strNo = result.get("no");
			if( strNo == null || strNo.length() == 0 )
				continue;

			if( strNo.equals(m_strArea1) )
			{
				nArea1Index = nIndex;
				break;
			}
		}
		m_spArea1.setSelection(nArea1Index);

		return true;
	}
	
	void applyArea()
	{
    	if(m_AreaList1 == null || m_AreaList1.size() == 0)
    		return;
    	if(m_AreaList2 == null || m_AreaList2.size() == 0)
    		return;

    	int nArea1 = m_spArea1.getSelectedItemPosition();
    	int nArea2 = m_spArea2.getSelectedItemPosition();
    	if(m_AreaList1.size() <= nArea1 || m_AreaList2.size() <= nArea2)
    		return;
    	
    	XResultMap result1 = m_AreaList1.get(nArea1);
    	XResultMap result2 = m_AreaList2.get(nArea2);
    	if(result1 == null || result2 == null)
    		return;
    	
    	String strArea1 = result1.get("no");
    	String strArea2 = result2.get("no");
    	if(strArea1 == null)
    	{
    		strArea1 = "";
    		strArea2 = "";
    	}
    	
    	if(strArea2 == null)
    		strArea2 = "";
    	
    	m_strArea1 = strArea1;
    	m_strArea2 = strArea2;
    	m_nLastNo = -1;
    	requestDeliveryListByArea();
	}

}

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pricecode.*;
import com.rangboq.xutil.XFileDownloader;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XResultMap;

public class GiftconActivity extends CMActivity implements OnClickListener
{
	TextView m_tvMyMileage;
	ListView m_ListView;
	int m_nReqCategory = 1, m_nShowCategory = 1;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_giftcon);
		
		/*initTopMainBar(R.id.top_imageView_giftcon);
		initBottomTabBar(0);*/
		
		initTopMainBar(0);
		initBottomTabBar(R.id.tab_imageView_gift);
		

		m_tvMyMileage = (TextView) findViewById(R.id.textView_my_mileage);
		m_ListView = (ListView) findViewById(R.id.listView_product);
	    
	    View moreView = getLayoutInflater().inflate(R.layout.list_footer_more, null);
	    m_ListView.addFooterView(moreView);
	    
	    ImageView ivMore = (ImageView) moreView.findViewById(R.id.ImageView_more);
	    ivMore.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
			    if(CMHttpConn.reqGiftconList(m_Handler, m_nReqCategory, m_nLastNo, m_nReqCount) == null)
			    	toastCheckInternet();
			    else
			    	showWait(null);
			}
		});

		int buttonIds[] = { R.id.imageView_cafe, R.id.imageView_food, R.id.imageView_life,
		                   R.id.imageView_gift, };
		for( int nId : buttonIds )
			((ImageView) findViewById(nId)).setOnClickListener(this);
		
		((ImageView) findViewById(R.id.imageView_cafe)).performClick();
	}

	@Override
    protected void onResume()
    {
	    super.onResume();
	    
		String strMyMileage = CMManager.getMyInfo("remain_point");
		if( strMyMileage != null && strMyMileage.length() > 0 )
			m_tvMyMileage.setText("내 마일리지 : " + strMyMileage);
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
			else if( msg.what == CMHttpConn.TYPE_REQ_GIFTCON_LIST )
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
		
		if(m_ResultList != null && m_ListView != null && m_ListAdapter != null)
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
					convertView = m_LayoutInflater.inflate(R.layout.list_item_giftcon, null);
				}
				if( convertView == null || m_ResultList == null )
					return null;

				TextView tvCoopName = (TextView) convertView.findViewById(R.id.textView_coop_name);
				TextView tvProductName = (TextView) convertView.findViewById(R.id.textView_product_name);
				TextView tvPrice = (TextView) convertView.findViewById(R.id.textView_price);
				ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.imageView_photo);
				if( tvCoopName == null || tvProductName == null || tvPrice == null || ivPhoto == null )
					return convertView;

				XResultMap result = m_ResultList.get(position);
				if( result != null )
				{
					String strCoopName = null, strProductName = null, strRealPrice = null;
					strCoopName = result.get("comp_name");
					strProductName = result.get("coupon_name");
					strRealPrice = result.get("real_sel_price");

					if( strCoopName == null )
						strCoopName = "";
					if( strProductName == null )
						strProductName = "";
					if( strRealPrice == null )
						strRealPrice = "";

					tvCoopName.setText(strCoopName);
					tvProductName.setText(strProductName);
					tvPrice.setText(strRealPrice + "원");

					Bitmap photoBitmap = null;
					String strThumbUrl = result.get("thumb_img");
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
				
				Intent intent = CMManager.getIntent(GiftconActivity.this, GiftconDetailActivity.class);
				intent.putExtra("item_info", selectedItem);
				startActivity(intent);
			}
		});

		return true;
	}

	@Override
	public void onClick( View v )
	{
		int nId = v.getId();
		switch( nId )
		{
			case R.id.imageView_cafe:
				m_nShowCategory = 1;
				break;
			case R.id.imageView_food:
				m_nShowCategory = 2;
				break;
			case R.id.imageView_life:
				m_nShowCategory = 3;
				break;
			case R.id.imageView_gift:
				m_nShowCategory = 4;
				break;
		}
		
		int buttonIds[] = { R.id.imageView_cafe, R.id.imageView_food, R.id.imageView_life,
			                   R.id.imageView_gift, };
		for( int nButtonId : buttonIds )
			((ImageView) findViewById(nButtonId)).setSelected(false);
		v.setSelected(true);

		m_nReqCategory = m_nShowCategory;
		m_nLastNo = -1;
		if(m_ResultList != null)
			m_ResultList.clear();
		m_ResultList = null;
		if(m_ListAdapter != null)
			m_ListAdapter.clear();
		m_ListAdapter = null;
		m_ListView.setAdapter(null);
		if( CMHttpConn.reqGiftconList(m_Handler, m_nReqCategory, m_nLastNo, m_nReqCount) == null )
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

}

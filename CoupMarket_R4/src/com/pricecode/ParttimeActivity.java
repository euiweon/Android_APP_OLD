package com.pricecode;

import android.content.Context;
import android.content.Intent;
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
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XResultMap;

public class ParttimeActivity extends CMActivity implements OnItemClickListener
{
	ListView m_ListView;
	int m_nLastNo = -1, m_nReqCount = 10;
	
	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_parttime);
		
		initTopMainBar(0);
		initBottomTabBar(R.id.tab_imageView_parttime);
		
		setTitleBarText("알바정보");
	    
	    m_ListView = (ListView) findViewById(R.id.listView_parttime);
	    
	    View moreView = getLayoutInflater().inflate(R.layout.list_footer_more, null);
	    m_ListView.addFooterView(moreView);
	    
	    ImageView ivMore = (ImageView) moreView.findViewById(R.id.ImageView_more);
	    ivMore.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
			    if(CMHttpConn.reqParttimeList(m_Handler, m_nLastNo, m_nReqCount) == null)
			    	toastCheckInternet();
			    else
			    	showWait(null);
			}
		});
	    
	    m_nLastNo = -1;
	    if(CMHttpConn.reqParttimeList(m_Handler, m_nLastNo, m_nReqCount) == null)
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
			if(m_Handler == null)
				return;
			
			
	        if(msg.what == XHandler.RESULT_TIME_OUT)
	        {
	        	cancelWait();
	        	toastCheckInternet();
	        	return;
	        }
	        else if(msg.what == CMHttpConn.TYPE_REQ_PARTTIME_LIST)
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
					convertView = m_LayoutInflater.inflate(R.layout.list_item_parttime, null);
				}
				if( convertView == null || m_ResultList == null )
					return null;

				TextView tvSubject = (TextView) convertView.findViewById(R.id.textView_subject);
				TextView tvDate = (TextView) convertView.findViewById(R.id.textView_date);
				if( tvSubject == null || tvDate == null )
					return convertView;

				XResultMap result = m_ResultList.get(position);
				if( result != null )
				{
					String strSubject = null, strDate = null;
					
					strSubject = result.get("subject");
					strDate = result.get("rdate");
					
					if(strSubject == null)
						strSubject  = " ";
					if(strDate == null)
						strDate  = "";
					else
					{
						int nFound = strDate.indexOf(" ");
						if(nFound > 0)
							strDate = strDate.substring(0, nFound);
					}
					
					tvSubject.setText(strSubject);
					tvDate.setText(strDate);
				}

				return convertView;
            }
		};

		m_ListView.setAdapter(m_ListAdapter);
		m_ListView.setOnItemClickListener(this);
		
		return true;
	}

	@Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
		if(m_ResultList == null || m_ResultList.size() <= position)
			return;
		
		XResultMap selectedItem = m_ResultList.get(position);
		if(selectedItem == null)
			return;
		
		Intent intent = CMManager.getIntent(ParttimeActivity.this, ParttimeDetailActivity.class);
		intent.putExtra("item_info", selectedItem);
		startActivity(intent);
    }

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

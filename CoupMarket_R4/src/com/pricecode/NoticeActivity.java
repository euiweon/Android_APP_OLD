package com.pricecode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XResultMap;

public class NoticeActivity extends CMActivity
{
	ListView m_ListView;
	int m_nLastNo = -1, m_nReqCount = 10;
	
	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_notice_list);
	    
	    setTitleBarText("공지사항");
	    
	    m_ListView = (ListView) findViewById(R.id.listView_notice);
	    
	    View moreView = getLayoutInflater().inflate(R.layout.list_footer_more, null);
	    m_ListView.addFooterView(moreView);
	    
	    ImageView ivMore = (ImageView) moreView.findViewById(R.id.ImageView_more);
	    ivMore.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
			    if(CMHttpConn.reqNoticeList(m_Handler, m_nLastNo, m_nReqCount) == null)
			    	toastCheckInternet();
			    else
			    	showWait(null);
			}
		});
	    
	    m_nLastNo = -1;
	    if(CMHttpConn.reqNoticeList(m_Handler, m_nLastNo, m_nReqCount) == null)
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
	        else if(msg.what == CMHttpConn.TYPE_REQ_NOTICE_LIST)
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


	SimpleDateFormat m_sdfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
	long m_nCurrentTimeMS = System.currentTimeMillis();
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
					convertView = m_LayoutInflater.inflate(R.layout.list_item_notice, null);
				}
				if( convertView == null || m_ResultList == null )
					return null;

				TextView tvSubject = (TextView) convertView.findViewById(R.id.textView_subject);
				TextView tvDate = (TextView) convertView.findViewById(R.id.textView_date);
				TextView tvContent = (TextView) convertView.findViewById(R.id.textView_content);
				ImageView ivNew = (ImageView) convertView.findViewById(R.id.imageView_new);
				LinearLayout layoutSubject = (LinearLayout) convertView.findViewById(R.id.linearLayout_subject);
				LinearLayout layoutContent = (LinearLayout) convertView.findViewById(R.id.linearLayout_content);
				if( layoutSubject == null || layoutContent == null
					|| tvSubject == null || tvDate == null || tvContent == null || ivNew == null )
					return convertView;

				XResultMap result = m_ResultList.get(position);
				if( result != null )
				{
					String strSubject = null, strDate = null, strContent = null, strOpened = null;
					boolean bIsNew = false;
					
					strSubject = result.get("subject");
					strDate = result.get("rdate");
					strContent = result.get("content");
					strOpened = result.get("opened");
					
					Date date = null;
					if(strDate == null)
						strDate  = "";
					else
					{
						try
                        {
	                        date = m_sdfDate.parse(strDate);
	                        long nDateTimeMS = date.getTime();
	                        if((m_nCurrentTimeMS - nDateTimeMS) < (24 * 60 * 60 * 1000))
	                        	bIsNew = true;
	                        
	                        int nFound = strDate.indexOf(" ");
	                        if(nFound != -1)
	                        	strDate = strDate.substring(0, nFound);
                        }
                        catch( ParseException e )
                        {
	                        e.printStackTrace();
	                        date = null;
                        }
					}
					if(strSubject == null)
						strSubject  = " ";
					if(strContent == null)
						strContent  = " ";
					
					tvSubject.setText(strSubject);
					tvDate.setText(strDate);
					if(bIsNew == false)
						ivNew.setVisibility(View.GONE);
					else
						ivNew.setVisibility(View.VISIBLE);
					if(strOpened == null || strOpened.length() == 0 || strOpened.equals("1") == false)
					{
						layoutContent.setVisibility(View.GONE);
						tvContent.setText("");
					}
					else
					{
						layoutContent.setVisibility(View.VISIBLE);
						tvContent.setText(Html.fromHtml(strContent));
					}
					
					layoutSubject.setTag(Integer.valueOf(position));
					layoutSubject.setOnClickListener(onSubjectBarClickListener);
				}

				return convertView;
            }
			
			OnClickListener onSubjectBarClickListener = new OnClickListener()
			{
				@Override
				public void onClick( View v )
				{
					Integer pos = (Integer) v.getTag();
					if(pos == null)
						return;
					
					int nPos = pos.intValue();
					XResultMap result = m_ResultList.get(nPos);
					if( result == null )
						return;
					
					String strOpened = result.get("opened");
					if(strOpened == null || strOpened.length() == 0 || strOpened.equals("1") == false)
						result.put("opened", "1");
					else
						result.remove("opened");
					
					m_ListAdapter.notifyDataSetChanged();
				}
			};
		};

		m_ListView.setAdapter(m_ListAdapter);
		
		return true;
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

package com.pricecode;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;

public class InquiryActivity extends CMActivity implements OnClickListener
{
	EditText m_etEmail, m_etSubject, m_etContent;
	
	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_inquiry);
	    
	    setTitleBarText("문의하기");
	    
	    m_etEmail = (EditText) findViewById(R.id.editText_email);
	    m_etSubject = (EditText) findViewById(R.id.editText_subject);
	    m_etContent = (EditText) findViewById(R.id.editText_content);
	    
	    findViewById(R.id.imageButton_send).setOnClickListener(this);
	    
	    boolean bDoneFocus = false;
	    if(CMManager.isOnline())
	    {
	    	String strEmail = CMManager.getMyInfo("email");
	    	if(strEmail != null && strEmail.length() > 0)
	    	{
	    		m_etEmail.setText(strEmail);
	    		m_etSubject.requestFocus();
	    		bDoneFocus = true;
	    	}
	    }
	    
	    if(bDoneFocus == false)
	    	m_etEmail.requestFocus();
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
	        else if(msg.what == CMHttpConn.TYPE_REQ_INQUIRY)
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
							String strResult = resultList.get(0).get("result");
							if(strResult != null && strResult.equals("true"))
							{
								showAlertAndFinish("문의사항이 등록되었습니다.");
								return;
							}
							else
							{
								String strError = resultList.get(0).get("errorMsg");
								if(strError != null && strError.length() > 0)
									showToast(strError);
								else
									toastRequestFail();
							}
							return;
						}
						
						toastRequestFail();
						return;
					}
				}

				toastCheckInternet();
				return;
			}
        }
	};

	@Override
    public void onClick( View v )
    {
	    if(v.getId() == R.id.imageButton_send)
	    {
	    	String strId = null, strPassword = null;
	    	if(CMManager.isOnline())
	    	{
				strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
				strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");
	    	}
	    	
	    	String strEmail = m_etEmail.getText().toString().trim();
	    	String strSubject = m_etSubject.getText().toString().trim();
	    	String strContent = m_etContent.getText().toString().trim();
	    	
	    	if(strEmail.length() == 0)
	    	{
	    		showAlert(" 이메일 주소를 입력해주세요.");
	    		return;
	    	}
	    	if(strSubject.length() == 0)
	    	{
	    		showAlert("제목을 입력해주세요.");
	    		return;
	    	}
	    	if(strContent.length() == 0)
	    	{
	    		showAlert("문의내용을 입력해주세요.");
	    		return;
	    	}
	    	
	    	if(CMHttpConn.reqInquiry(m_Handler, strId, strPassword, strEmail, strSubject, strContent) == null)
	    		toastCheckInternet();
	    	else
	    		showWait(null);
	    }
    }
}

package com.pricecode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XResultMap;

public class SearchIdPasswordActivity extends CMActivity implements OnClickListener
{
	EditText m_etEmail1, m_etEmail2, m_etId;
	ImageButton m_btnSearchId, m_btnSearchPassword;
	ScrollView m_svBody;

	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_search_id_password);
	    
	    setTitleBarText("아이디/비밀번호 찾기");
	    
	    m_svBody = (ScrollView) findViewById(R.id.scrollView_body);

	    m_etEmail1 = (EditText) findViewById(R.id.editText_email1);
	    m_etEmail2 = (EditText) findViewById(R.id.editText_email2);
	    m_etId = (EditText) findViewById(R.id.editText_id);
	    
	    m_btnSearchId = (ImageButton) findViewById(R.id.imageButton_search_id);
	    m_btnSearchPassword = (ImageButton) findViewById(R.id.imageButton_search_password);
	    
	    m_btnSearchId.setOnClickListener(this);
	    m_btnSearchPassword.setOnClickListener(this);
	    
	    m_etEmail1.requestFocus();
    }

	@Override
    protected void onDestroy()
    {
		m_Handler = null;
	    super.onDestroy();
    }

	@Override
    public void onClick( View v )
    {
	    if(v.getId() == R.id.imageButton_search_id)
	    {
	    	String strEmail = m_etEmail1.getText().toString().trim();
	    	if(strEmail.length() == 0 || strEmail.contains("@") == false)
	    	{
	    		showAlert("등록한 이메일을 입력해주세요.");
	    		return;
	    	}
	    	
	    	if(CMHttpConn.reqSearchId(m_Handler, strEmail) == null)
	    		toastCheckInternet();
	    	else
	    		showWait(null);
	    }
	    else if(v.getId() == R.id.imageButton_search_password)
	    {
	    	String strEmail = m_etEmail2.getText().toString().trim();
	    	if(strEmail.length() == 0 || strEmail.contains("@") == false)
	    	{
	    		showAlert("등록한 이메일을 입력해주세요.");
	    		return;
	    	}
	    	
	    	String strId = m_etId.getText().toString().trim();
	    	if(strEmail.length() == 0 || strEmail.contains("@") == false)
	    	{
	    		showAlert("등록한 아이디를 입력해주세요.");
	    		return;
	    	}
	    	
	    	if(CMHttpConn.reqSearchPassword(m_Handler, strId, strEmail) == null)
	    		toastCheckInternet();
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
    						String strResult = null;
    						XResultMap result = resultList.get(0);
    						if(result != null)
    							strResult = result.get("result");
    						if( msg.what == CMHttpConn.TYPE_REQ_SEARCH_ID )
    						{
    							if( strResult != null && strResult.equals("true") )
    							{
    								final String strId = result.get("uid");
    								final String strEmail = result.get("email");
    								if(strId != null && strId.length() > 0)
    								{
    									AlertDialog alert = showAlert("아이디는 '" + strId + "'입니다");
    									alert.setOnDismissListener(new OnDismissListener()
										{
											@Override
											public void onDismiss( DialogInterface dialog )
											{
		    									m_etEmail2.setText(strEmail);
		    									m_etId.setText(strId);
		    									m_btnSearchPassword.requestFocus();
		    									
		    									Rect outRect = new Rect();
		    									m_btnSearchPassword.getWindowVisibleDisplayFrame(outRect);
		    									m_svBody.scrollTo(outRect.left, outRect.bottom);

		    									Intent resultData = new Intent();
			    								resultData.putExtra("id", strId);
			    								setResult(RESULT_OK, resultData);
											}
										});
    									return;
    								}
    							}
    						}
    						else if( msg.what == CMHttpConn.TYPE_REQ_SEARCH_PASSWORD )
    						{
    							if( strResult != null && strResult.equals("true") )
    							{
    								final String strId = result.get("uid");
    								final String strEmail = result.get("email");
    								if(strEmail != null && strEmail.length() > 0)
    								{
    									AlertDialog alert = showAlert("임시 비밀번호를  '" + strEmail + "'로 발송하였습니다.");
    									alert.setOnDismissListener(new OnDismissListener()
										{
											@Override
											public void onDismiss( DialogInterface dialog )
											{
			    								Intent resultData = new Intent();
			    								resultData.putExtra("id", strId);
			    								setResult(RESULT_OK, resultData);
			    								finish();
											}
										});
    									return;
    								}
    							}
    						}

							if(result != null)
							{
								String strError = result.get("errorMsg");
								if(strError != null && strError.length() > 0)
								{
									showToast(strError);
									return;
								}
							}
  
    						toastRequestFail();
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

}

package com.pricecode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XUtilFunc;

public class LoginActivity extends CMActivity implements OnClickListener
{
	EditText m_etId, m_etPassword;
	ImageButton m_btnLogin, m_btnSearchIdPW, m_btnJoin;
	CheckBox m_btnAutoLogin;
	ImageView m_imgNoLogin;

	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    super.onCreate(savedInstanceState);
	    closeAllActivity(this);
	    setContentView(R.layout.activity_login);
	    
	    setTitleBarText("로그인");
	    
	    m_etId = (EditText) findViewById(R.id.editText_id);
	    m_etPassword = (EditText) findViewById(R.id.editText_password);
	    m_btnLogin = (ImageButton) findViewById(R.id.imageButton_login);
	    m_btnSearchIdPW = (ImageButton) findViewById(R.id.imageButton_search_id_password);
	    m_btnJoin = (ImageButton) findViewById(R.id.imageButton_join_member);
	    m_btnAutoLogin = (CheckBox) findViewById(R.id.checkBox_auto_login);
	    
	    m_imgNoLogin = (ImageView)findViewById(R.id.no_login);
	    
	    m_btnLogin.setOnClickListener(this);
	    m_btnSearchIdPW.setOnClickListener(this);
	    m_btnJoin.setOnClickListener(this);
	    m_btnAutoLogin.setOnClickListener(this);
	    m_imgNoLogin.setOnClickListener(this);
	    
    	m_btnAutoLogin.setChecked(true);
    	
    	//
    	String strId = null, strPassword = null;
    	strId = getIntent().getStringExtra("id");
    	strPassword = getIntent().getStringExtra("password");
    	if(strId == null)
    		strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
    	if(strPassword == null)
    		strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");
    	
    	m_etId.setText(strId);
    	m_etPassword.setText(strPassword);
    	
	    CMManager.setOnline(false, null, null);
	    CMManager.setMyInfoList(null);
    }

	@Override
    protected void onResume()
    {
	    super.onResume();
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
	    switch(v.getId())
	    {
	    	case R.id.imageButton_login:
	    	{
	    		String strId = m_etId.getText().toString().trim();
	    		String strPassword = m_etPassword.getText().toString().trim();
	    		if(strId.length() == 0)
	    		{
	    			showAlert("아이디를 입력해주세요.");
	    			return;
	    		} 
	    		if(strPassword.length() == 0)
	    		{
	    			showAlert("비밀번호를 입력해주세요.");
	    			return;
	    		} 
	    		
	    		if(CMHttpConn.tryLogin(m_Handler, strId, strPassword) == null)
	    			toastCheckInternet();
	    		else
	    			showWait(null);
	    		break;
	    	}

	    	case R.id.imageButton_search_id_password:
	    	{
	    		Intent intent = CMManager.getIntent(this, SearchIdPasswordActivity.class);
	    		startActivityForResult(intent, REQUEST_FOR_SEARCH_ID_PASSWORD);
	    		break;
	    	}

	    	case R.id.imageButton_join_member:
	    	{
	    		Intent intent = CMManager.getIntent(this, MemberInfoActivity.class);
	    		intent.putExtra("forResult", true);
	    		intent.putExtra("isJoinUI", true);
	    		startActivityForResult(intent, REQUEST_FOR_JOIN);
	    		break;
	    	}
	    	case R.id.no_login:
	    	{
	    		// 
	    		//showAlert("아직 구현되지 않았습니다. ");
	    		
	    		CMManager.setPrefString(CMManager.PREF_USER_ID, "");
	    		CMManager.setPrefStringS(CMManager.PREF_USER_PASSWORD, "");
	    		Intent intent = CMManager.getIntent(LoginActivity.this,
                        CMarketActivity.class);
				startActivity(intent);
				finish();
	    	}
	    		break;
	    }
    }

	static final int REQUEST_FOR_SEARCH_ID_PASSWORD = 1;
	static final int REQUEST_FOR_JOIN = 2;
	@Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
	    // TODO Auto-generated method stub
	    super.onActivityResult(requestCode, resultCode, data);
	    if(resultCode != RESULT_OK || data == null)
	    	return;
	    
	    String strId = data.getStringExtra("id");
	    String strPassword = data.getStringExtra("password");
	    if(strId != null && strId.length() > 0)
	    	m_etId.setText(strId);
	    if(strPassword != null && strPassword.length() > 0)
	    	m_etPassword.setText(strPassword);
	    else
	    {
	    	if(strId != null && strId.length() > 0)
	    		m_etPassword.setText("");
	    }
	    m_btnAutoLogin.setChecked(true);
	    
	    if(strId != null && strId.length() > 0 && strPassword == null || strPassword.length() == 0)
	    	m_etPassword.requestFocus();
	    else
	    {
		    XUtilFunc.hideKeyboard(m_etId);
		    XUtilFunc.hideKeyboard(m_etPassword);
		    
	    	m_btnLogin.requestFocus();
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
	        else if(msg.what == CMHttpConn.TYPE_TRY_LOGIN)
			{
	        	cancelWait();
				if( msg.obj != null && msg.obj instanceof CMHttpConn )
				{
					CMHttpConn conn = (CMHttpConn) msg.obj;
					if( conn.m_nWebOpenResult == CMHttpConn.XHTTP_200_OK )
					{
						String strError = null;

						XResultList resultList = conn.m_XmlParser.getResultList();
						if( resultList != null && resultList.size() > 0 )
						{
							String strResult = resultList.get(0).get("result");
							if( strResult != null && strResult.equals("true") )
							{
					    		String strId = m_etId.getText().toString().trim();
					    		String strPassword = m_etPassword.getText().toString().trim();
								CMManager.setOnline(true, strId, strPassword);
								CMManager.setMyInfoList(resultList);
					    		boolean bAuto = m_btnAutoLogin.isChecked();
					    		CMManager.setPrefBoolean(CMManager.PREF_AUTO_LOGIN, bAuto);

								if( CMHttpConn.reqAreaList(m_Handler) != null )
									return;
							}
							
							String strWebError = resultList.get(0).get("errorMsg");
							if(strWebError != null && strWebError.length() > 0)
								strError = strWebError;
							else
								strError = "로그인요청이 실패하였습니다.";
						}
						
						if(strError != null)
							showAlert(strError);
						else
							toastRequestFail();
						return;
					}
				}

				showAlert("인터넷 연결 및 로그인 정보를 확인하여 주십시오.");
				return;
			}
			else if( msg.what == CMHttpConn.TYPE_REQ_AREA_LIST )
			{
				if( msg.obj != null && msg.obj instanceof CMHttpConn )
				{
					CMHttpConn conn = (CMHttpConn) msg.obj;
					if( conn.m_nWebOpenResult == CMHttpConn.XHTTP_200_OK )
					{
						XResultList resultList = conn.m_XmlParser.getResultList();
						if( resultList != null && resultList.size() > 0 )
						{
							String strResult = resultList.getOtherValue("result");
							if( strResult != null && strResult.equals("true") )
							{
								CMManager.setAreaList(resultList);

								showToast("로그인 되었습니다.");
								
								Intent intent = CMManager.getIntent(LoginActivity.this,
								                                    CMarketActivity.class);
								startActivity(intent);
								finish();
								return;
							}
						}
					}
				}

				showToast("지역목록정보 수신이 실패하였습니다.");
				return;
			}
       }
	};
}

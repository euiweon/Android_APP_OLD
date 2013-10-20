package com.pricecode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;

public class IntroActivity extends CMActivity
{
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		closeAllActivity(this);
		setContentView(R.layout.activity_intro);

		m_Handler.sendEmptyMessageDelayed(MSG_FOR_INTRO_DISPLAY, 1000);
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_intro, menu);
		return true;
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		m_Handler = null;
		super.onDestroy();
	}

	public static final int MSG_FOR_INTRO_DISPLAY = 1;
	XHandler m_Handler = new XHandler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			removeTimeoutMsg();
			if( m_Handler == null )
				return;

			if( msg.what == MSG_FOR_INTRO_DISPLAY )
			{
				if( CMManager.isOnline() )
				{
					Intent intent = CMManager.getIntent(IntroActivity.this, CMarketActivity.class);
					startActivity(intent);
					finish();
					return;
				}
				else
				{
					boolean bAutoLogin = CMManager.getPrefBoolean(CMManager.PREF_AUTO_LOGIN, false);
					String strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
					String strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");
					if( bAutoLogin == false || strId.length() == 0 || strPassword.length() == 0 )
					{
						Intent intent = CMManager.getIntent(IntroActivity.this, CMarketActivity.class);
						startActivity(intent);
						finish();
						return;
					}
					else if( CMHttpConn.tryLogin(m_Handler, strId, strPassword) == null )
					{
						toastCheckInternet();

						Intent intent = CMManager.getIntent(IntroActivity.this, CMarketActivity.class);
						startActivity(intent);
						finish();
						return;
					}
				}
			}
			else if( msg.what == XHandler.RESULT_TIME_OUT )
			{
				toastCheckInternet();
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
								
								String strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
								String strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD,
								                                              "");
								CMManager.setOnline(true, strId, strPassword);

								showToast("로그인 되었습니다.");
								
								Intent intent = CMManager.getIntent(IntroActivity.this,
								                                    CMarketActivity.class);
								startActivity(intent);
								finish();
								return;
							}
						}
					}
				}

				showToast("지역목록정보 수신이 실패하였습니다.");
				
				Intent intent = CMManager.getIntent(IntroActivity.this, CMarketActivity.class);
				startActivity(intent);
				finish();
				return;
			}
			else if( msg.what == CMHttpConn.TYPE_TRY_LOGIN )
			{
				String strStatus = "로그인 정보를 확인하여 주십시오.";
				if( msg.obj != null && msg.obj instanceof CMHttpConn )
				{
					CMHttpConn conn = (CMHttpConn) msg.obj;
					if( conn.m_nWebOpenResult == CMHttpConn.XHTTP_200_OK )
					{
						XResultList resultList = conn.m_XmlParser.getResultList();
						if( resultList != null && resultList.size() > 0 )
						{
							String strResult = resultList.get(0).get("result");
							if( strResult != null && strResult.equals("true") )
							{
								CMManager.setMyInfoList(resultList);
								
								if( CMHttpConn.reqAreaList(m_Handler) != null )
									return;
								
								strStatus = "지역목록정보 요청이 실패하였습니다.";
							}
						}
					}
				}

				showToast(strStatus);

				Intent intent = CMManager.getIntent(IntroActivity.this, CMarketActivity.class);
				startActivity(intent);
				finish();
				return;
			}
		}
	};

}

package com.etoos.test;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NetPopup  extends Activity implements OnClickListener 
{
	Thread ConnectThread;
	boolean bFinish = false;
	int APINumber= -1;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.netpopup);

		{

			
			APINumber =  getIntent().getIntExtra("API", -1);
			ConnectAPI( APINumber );
		}
	}
	


	

	private void ConnectAPI( int index )
	{
		
		if ( index == -1 )
			return;
		bFinish = false;
		final AppManagement _AppManager = (AppManagement) getApplication();
		_AppManager.ParseString = "";
		String URL = "";
		switch ( index )
		{
//		0. 이벤트
		case 0:
		{
			URL += "http://etoos.hubweb.net:8080/SMART_OMR/api/event_api.jsp";
		}
			break;
//			1. 로그인 
		case 1:
			URL += "http://etoos.hubweb.net:8080/SMART_OMR/api/Login_api.jsp";
			break;
			
//			2.omr정보 전달 api
		case 2:
			URL += "http://etoos.hubweb.net:8080/SMART_OMR/api/omrGroup.jsp";
			break;
//			3. omr 제출 
		case 3:
			URL += "http://etoos.hubweb.net:8080/SMART_OMR/api/omrSubmit.jsp";
			break;
			
//			5 .학교 검색
		case 5:
			URL += "http://etoos.hubweb.net:8080/SMART_OMR/api/omrSubmit.jsp";
			break;
//			4. omr 제출 ( 히스토리 )
		case 4:
			URL += "http://etoos.hubweb.net:8080/SMART_OMR/api/omrHistory.jsp";
			break;
			
//			6. 빠른응시 OMR
		case 6:
			URL += "http://etoos.hubweb.net:8080/SMART_OMR/api/omrSubmit.jsp";
			break;
			
			
		
		}
		
		if (index ==3 )
		{
			final String finalURL = URL;
			ConnectThread = new Thread(new Runnable()
			{

				public void run() 
				{
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(finalURL, _AppManager.ParamData);
					_AppManager.ParseString = strJSON;
					handler.sendEmptyMessage(0);
				}
			});
			ConnectThread.start();
		}
		else
		{
			final String finalURL = URL;
			ConnectThread = new Thread(new Runnable()
			{

				public void run() 
				{
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(finalURL, _AppManager.ParamData);
					_AppManager.ParseString = strJSON;
					handler.sendEmptyMessage(0);
				}
			});
			ConnectThread.start();
		}


	}
	
	@Override
	public void onBackPressed() 
	{
		// 스레드가 살아 있음에도 종료를 하고자 할때 들어옴. 
		if ( bFinish == false )
		{
			bFinish = true;
	    	this.setResult(-1);
	    	finish();			
		}

	}
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{

			switch(msg.what)
			{
				case 0:
				{
					// 무슨 메세지를 받았던간에 화면을 종료 시키고 다음 화면으로 이동 시킨다. 
					callFinish();
				}
				break;
			}
		}
	};
    	
    private void callFinish()
    {
    	// 스레드가 살아있는데 여기 들어왔다는건 전송 도중에 back 키를 눌렀다는 것 
    	// 팝업이 종료되어도 스레드는 죽지 않기에, 메세지를 보내지 않도록 해야 한다. 
    	if ( bFinish == false )
		{
    		bFinish = true;
        	Intent i =  new Intent();
        	i.putExtra("return", APINumber);
        	setResult(10, i);
        	
        	finish();
		}
    }





	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	

}

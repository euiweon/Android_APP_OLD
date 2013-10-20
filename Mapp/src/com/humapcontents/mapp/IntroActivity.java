package com.humapcontents.mapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;



import com.euiweonjeong.base.BaseActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

public class IntroActivity extends BaseActivity {

	IntroActivity self;
	private Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);  // 인트로 레이아웃 출력      
        
        
        self = this;
        GetToken();
    }
    
    
    // 현재 상태바의 사이즈를 구한다. 
    @Override
    public void onWindowFocusChanged(boolean hasFocus) 
    {
           
                        
            super.onWindowFocusChanged(hasFocus);
    }
    
    public void GetToken()
    {
    	final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				handler2.sendMessage(handler2.obtainMessage(20,getAuthToken()  ));
				
			}
		});
		thread.start();
		
    }
    
    public String getAuthToken() 
	{

        
        String authToken = "DQAAALsAAACNH97Asx798UT-ruZPwzLiLJut4jeAaA2SkxQBOwCKhT7tjN5ETeAZjh6jNCO3qGZe2oB8kOIijLcVJOTXFiUHyuI6wy1l3s2yT7EYqogL5kYFZWYrwY6nASMijJfWNJcU3rMW95S7AZkx7NRRtJcV9vdJR324gVfGYuoOg9BJ2LXXJBSWVj_V1OsCt-iF22srlenEUz-6xEigu-7vQ0LBXtCwva9HfDfl3Vqazr6joDIUtRsitqfqwCUS0WGofGw";
 
        /*Log.v("C2DM", "AuthToken : " + authToken);
 
       // if (authToken == null)
        {
            StringBuffer postDataBuilder = new StringBuffer();
             postDataBuilder.append("&Email=mapp.contents@gmail.com");
            postDataBuilder.append("&Passwd=mapptown8"); 
            postDataBuilder.append("&service=youtube");
            postDataBuilder.append("&source=MappContents");
 
            byte[] postData = null;
			try {
				postData = postDataBuilder.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            URL url = null;
			try {
				url = new URL("https://www.google.com/accounts/ClientLogin");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            try {
				conn.setRequestMethod("POST");
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",
                    Integer.toString(postData.length));
 
            // 출력스트림을 생성하여 서버로 송신
            OutputStream out = null;
			try {
				out = conn.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
            	if ( out != null)
            		out.write(postData);
            	else
            	{
            		return "";

            	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            // 서버로부터 수신받은 스트림 객체를 버퍼에 넣어 읽는다.
            BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(
				        conn.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            String sIdLine = null;
			try {
				sIdLine = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String lsIdLine = null;
			try {
				lsIdLine = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String authLine = null;
			try {
				authLine = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            authToken = authLine.substring(5, authLine.length());
        }*/

        return authToken;
    }
    
    
    final Handler handler2 = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			
			switch(msg.what)
			{
			case 0:
			
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 2:

				break;
				
			case 3:
	
				break;
			case 20:
			{
				if ( msg.obj.equals(""))
					
				{
            		Toast.makeText(self, "인터넷 접속을 할수 없어 종료합니다.", Toast.LENGTH_SHORT);
            		handler.postDelayed(TimerRunner2, 2000); 
            		
				}
				else
				{
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.m_YoutubeToken = (String) msg.obj;
					Log.v("Youtube", "AuthToken : " + _AppManager.m_YoutubeToken);
					handler.postDelayed(TimerRunner, 2000);   		// 2초 뒤에 호출	
				}

				
			}
				break;
			default:
				break;
			}

		}
    	
	};

    
    // **********************************************************************
    // 타이머 처리
    // **********************************************************************    
    private Runnable TimerRunner = new IntroRunnAble(this);
    public class IntroRunnAble implements Runnable 
    { 
    	 
    	Object parentActivity;
    	public IntroRunnAble(Object parameter)
    	{
    		   
    	    // store parameter for later user 
    	   parentActivity = parameter;
    	} 
    	 
    	public void run() 
    	{ 
    		
    		{
    			ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE); 
    			Integer memoryClass = am.getLargeMemoryClass();
    			
    			 // calculate status bar height
                Rect rect = new Rect();
                self.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                Log.d(TAG, "status bar height = " + rect.top);
                Log.d(TAG, "title bar height = " + self.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop());
                Log.d("Memory", memoryClass.toString(), null);
                
                
                AppManagement _AppManager = (AppManagement) getApplication();
                _AppManager.CreateUISizeConverter(rect.top);
    		}
           handler.removeCallbacks(TimerRunner);   // 재실행 제거
           
           
	      Intent intent;
          // Create an Intent to launch an Activity for the tab (to be reused)
          intent = new Intent().setClass((IntroActivity)parentActivity, HomeActivity.class);
          
          
          startActivity( intent ); 

     
               
    	} 
    } 
    
    private Runnable TimerRunner2 = new IntroRunnAble2(this);
    public class IntroRunnAble2 implements Runnable 
    { 
    	 
    	Object parentActivity;
    	public IntroRunnAble2(Object parameter)
    	{
    		   
    	    // store parameter for later user 
    	   parentActivity = parameter;
    	} 
    	 
    	public void run() 
    	{ 
    		
    		
           handler.removeCallbacks(TimerRunner2);   // 재실행 제거
           
           
           moveTaskToBack(true);
		    android.os.Process.killProcess(android.os.Process.myPid()); 

     
               
    	} 
    }
}

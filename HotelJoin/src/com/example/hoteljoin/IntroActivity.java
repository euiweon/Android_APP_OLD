package com.example.hoteljoin;

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

public class IntroActivity extends Activity {

	IntroActivity self;
	private Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);  // 인트로 레이아웃 출력      
        
        
        handler.postDelayed(TimerRunner, 2000);
        
        self = this;
    }
    
    
    // 현재 상태바의 사이즈를 구한다. 
    @Override
    public void onWindowFocusChanged(boolean hasFocus) 
    {
           
                        
            super.onWindowFocusChanged(hasFocus);
    }
    


    
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
                Log.d("TAG", "status bar height = " + rect.top);
                Log.d("TAG", "title bar height = " + self.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop());
                Log.d("Memory", memoryClass.toString(), null);

                AppManagement _AppManager = (AppManagement) getApplication();
                _AppManager.CreateUISizeConverter(rect.top);
    		}
           handler.removeCallbacks(TimerRunner);   // 재실행 제거
           
           
	      Intent intent;
          // Create an Intent to launch an Activity for the tab (to be reused)
          intent = new Intent().setClass((IntroActivity)parentActivity, MainActivity.class);
          
          
          startActivity( intent ); 

     
               
    	} 
    } 
    
    
}

package com.goodmate.tayotayo;

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
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class OutroActivity extends Activity {

	OutroActivity self;
	private Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outro);  // 인트로 레이아웃 출력      
        
        
        handler.postDelayed(TimerRunner, 2000);
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	_AppManager.activityList1.add(this);
        }
        //((ImageView)findViewById(R.id.back)).setImageResource(R.drawable.thanks1);
        
        
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
    		
           handler.removeCallbacks(TimerRunner);   // 재실행 제거
           
           

           // 종료 
           {
        	   AppManagement _AppManager = (AppManagement) getApplication();
              
        	   moveTaskToBack(true);
        	   for ( int i = 0; i < _AppManager.activityList1.size() ; i++ )
		    		 _AppManager.activityList1.get(i).finish();
		    	 
		    	 android.os.Process.killProcess(android.os.Process.myPid());
           }

     
               
    	} 
    } 
    
    
}

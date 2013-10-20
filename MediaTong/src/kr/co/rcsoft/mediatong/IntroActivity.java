package kr.co.rcsoft.mediatong;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.UISizeConverter;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class IntroActivity extends BaseActivity {

	IntroActivity self;
	private Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);  // 인트로 레이아웃 출력      
        handler.postDelayed(TimerRunner, 2000);   		// 2초 뒤에 호출
        
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
    			 // calculate status bar height
                Rect rect = new Rect();
                self.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                Log.d(TAG, "status bar height = " + rect.top);
                Log.d(TAG, "title bar height = " + self.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop());
                
                AppManagement _AppManager = (AppManagement) getApplication();
                _AppManager.CreateUISizeConverter(rect.top);
    		}
           handler.removeCallbacks(TimerRunner);   // 재실행 제거
           
           
           Date today = new Date(); 
           
           SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd"); 
            
           

   			SharedPreferences prefdefault = getSharedPreferences("Event", MODE_PRIVATE); 
   			String text = prefdefault.getString("CAL", "");
   			
   			if ( text.equals(date.format(today)) )
   			{
   				Intent intent;
                // Create an Intent to launch an Activity for the tab (to be reused)
               intent = new Intent().setClass((IntroActivity)parentActivity, HomeActivity.class);
               //intent = new Intent().setClass((IntroActivity)parentActivity, NaverMapActivity.class);
               
               startActivity( intent );   
   			}
   			else
   			{
   	           Intent intent;
               // Create an Intent to launch an Activity for the tab (to be reused)
              intent = new Intent().setClass((IntroActivity)parentActivity, IntroEventActivity.class);
              //intent = new Intent().setClass((IntroActivity)parentActivity, NaverMapActivity.class);
              
              startActivity( intent );   
   			}

     
               
    	} 
    } 

}

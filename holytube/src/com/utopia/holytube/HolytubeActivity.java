package com.utopia.holytube;

import java.io.IOException;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.utopia.holytube.mainSingleton;
import com.utopia.holytube.mainSingleton.TodayData;





enum CURR_XML_STATE
{
	CURR_XML_VERSION,
	CURR_XML_RESULTCODE,
	CURR_XML_MANNIMG,
	CURR_XML_MID,
	CURR_XML_PREACHER,
	CURR_XML_PLAYTIME,
	CURR_XML_TITLE,
	CURR_XML_IMG,
	CURR_XML_MOVIE,
	CURR_XML_DATALENG,
	CURR_XML_YOUTUBE,
	CURR_XML_UNKNOWN,
}

public class HolytubeActivity extends HolyTubeBaseActivity implements OnClickListener  {


	/** Called when the activity is first created. */
	
	HolytubeActivity  self;
	

	
	private ArrayList<String> tempstring;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        self = this;
        
        tempstring = new ArrayList<String>();
        
       
        (findViewById(R.id.main_1_btn)).setOnClickListener( this);
        (findViewById(R.id.main_2_btn)).setOnClickListener( this);
        (findViewById(R.id.main_3_btn)).setOnClickListener( this);
        (findViewById(R.id.main_4_btn)).setOnClickListener( this);
        


        
       mainSingleton myApp = (mainSingleton) getApplication();
        
       RefreshUI();
       AfterCreate();
       


		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주세요.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
        
    }
    
    public void RefreshUI()
    {
 


    }

	@Override
	protected void onPause() {
		super.onPause();
	}


    public void GetDataInfo()
    {
		final mainSingleton myApp = (mainSingleton) getApplication();

		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();
				myApp.TodayXMLString = myApp.GetHTTPGetData(myApp.DEF_HOME_URL + "/today_movies.asp?", data);
		        handler.sendEmptyMessage(0);
			}
		});
		thread.start();
    }
    
    
    
    
    final Handler handler = new Handler( )
	{
    			
		public void handleMessage(Message msg)
		{
			String message = null;
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
			{
				Intent intent;
	            intent = new Intent().setClass(self, TodayMovieActivity.class);
	            startActivity( intent ); 
			}
			break;
			case 1:
			{
				
			}
			break;

			case 2:

				break;
			default:

				break;
			}

		}
	};
	
	
	
    public void onClick(View v )
    {
    	switch(v.getId())
    	{
    	case R.id.main_1_btn:
    	{
            
    		// 데이터 가져온다.
    		
    		

    		GetDataInfo();
    	}
    		break;
    	case R.id.main_2_btn:
    	{
		
           Intent intent;
           intent = new Intent().setClass(this, BookStoreActivity.class);
           startActivity( intent );   
           
    	}
    		break;
    		
    	case R.id.main_3_btn:
    	{
    		Intent intent;
            intent = new Intent().setClass(this, KAMActivity.class);
            startActivity( intent );   
    	}
    	break;
    	
    	case R.id.main_4_btn:
    	{
    		Intent intent;
            intent = new Intent().setClass(this, DonateActivity.class);
            startActivity( intent );   
    	}
    	break;
    	

    	default:
    		break;
    	}
    }

    
    @Override
    public void onBackPressed() 
    {

    	new AlertDialog.Builder(this)
		 .setTitle("종료 확인")
		 .setMessage("정말 종료 하겠습니까?") //줄였음
		 .setPositiveButton("예", new DialogInterface.OnClickListener() 
		 {
		     public void onClick(DialogInterface dialog, int whichButton)
		     {   
		    	 ExitApp();
		     }
		 })
		 .setNegativeButton("아니요", new DialogInterface.OnClickListener() 
		 {
		     public void onClick(DialogInterface dialog, int whichButton) 
		     {
		         //...할일
		     }
		 })
		 .show();
    	
    }

}
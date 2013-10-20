package com.utopia.holytube;

import com.euiweonjeong.base.BaseActivity;
import com.utopia.holytube.TodayMovieActivity.Today_ClickListen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TodayMovieDetailActivity extends HolyTubeBaseActivity {
    /** Called when the activity is first created. */
	TodayMovieDetailActivity self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todaymovie);
        
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주세요.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        // Tab
        {
            Button TabBTN2 = (Button)findViewById(R.id.today_tab_btn_2);
            TabBTN2.setOnClickListener(new Today_ClickListen(this));
            Button TabBTN3 = (Button)findViewById(R.id.today_tab_btn_3);
            TabBTN3.setOnClickListener(new Today_ClickListen(this));
            Button TabBTN4 = (Button)findViewById(R.id.today_tab_btn_4);
            TabBTN4.setOnClickListener(new Today_ClickListen(this));
            Button TabBTN5 = (Button)findViewById(R.id.today_tab_btn_5);
            TabBTN5.setOnClickListener(new Today_ClickListen(this));
        }
        
        {
       	 Button TabBTN2 = (Button)findViewById(R.id.homebutton);
            TabBTN2.setOnClickListener(new Today_ClickListen(this));
            
       	 Button TabBTN3 = (Button)findViewById(R.id.backbutton);
       	 TabBTN3.setOnClickListener(new Today_ClickListen(this));
       }
       AfterCreate();
        
    }
    
    public  class Today_ClickListen implements OnClickListener
    {

    	TodayMovieDetailActivity Parentactivity;
    	public Today_ClickListen( TodayMovieDetailActivity activity)
    	{
    		Parentactivity = activity;
    	}
    	
    	public void onClick(View v )
        {
        	switch(v.getId())
        	{
	        	case R.id.today_tab_btn_2:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, KingWayActivity.class);
			        startActivity( intent );  
	        	}
	        		break;
	        		
	        	case R.id.today_tab_btn_3:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, PazzleDetailActivity.class);
			        startActivity( intent );   
	        	}
	        		break;
	        		
	        	case R.id.today_tab_btn_4:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, ContryActivity.class);
			        startActivity( intent );  
	        	}
	        		break;
	        		
	        	case R.id.today_tab_btn_5:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, MovieCollectActivity.class);
			        startActivity( intent );  
	        	}
	        		break;
	        		
	        	case R.id.homebutton:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, HolytubeActivity.class);
			        startActivity( intent ); 
	        	}
	        	break;
	        	case R.id.backbutton:
	        	{
	        		onBackPressed();
	        	}
	        	break;

        	}
        }
    	
    }
}


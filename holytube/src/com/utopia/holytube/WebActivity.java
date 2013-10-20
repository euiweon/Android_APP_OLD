package com.utopia.holytube;

import com.euiweonjeong.base.BaseActivity;
import com.utopia.holytube.TodayMovieActivity.Today_ClickListen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WebActivity extends HolyTubeBaseActivity {
    /** Called when the activity is first created. */
	WebActivity self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        
        final mainSingleton myApp = (mainSingleton) getApplication();
        WebView mWebView;
        
        self = this;
        mWebView = (WebView) findViewById(R.id.webzineView); 
       
        mWebView.loadUrl(myApp.DEF_HOME_URL + "/mobile/index.html"); 
        mWebView.setWebViewClient(new DraptWebViewClient());
        
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

    	WebActivity Parentactivity;
    	public Today_ClickListen( WebActivity activity)
    	{
    		Parentactivity = activity;
    	}
    	
    	public void onClick(View v )
        {
        	switch(v.getId())
        	{

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
    

	private class DraptWebViewClient extends WebViewClient 
	{
	   @Override
	   public boolean shouldOverrideUrlLoading(WebView view, String url) 
	   {
	     view.loadUrl(url);
	     return true;
	   }
	}

 
}
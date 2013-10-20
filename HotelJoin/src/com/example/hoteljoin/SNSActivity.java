package com.example.hoteljoin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.KakaoLink;
import com.euiweonjeong.base.StoryLink;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.slidingmenu.lib.SlidingMenu;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SNSActivity extends HotelJoinBaseActivity implements OnClickListener{
	SNSActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 
	 Integer m_Tab = -1;
	 
	private SharedPreferences mPrefs;
				
	 private String encoding = "UTF-8";

	 
	 private WebView mWebView;
	 
	 String URL = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sns);
		
		

		// 비하인드 뷰 설정
		setBehindContentView(R.layout.main_menu_1);
		
		self = this;
		

		
		// 슬라이딩 뷰
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.RIGHT);
		
		{
			Display defaultDisplay = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    	
			int width;
			@SuppressWarnings("deprecation")
			int windowswidth = defaultDisplay.getWidth();
			@SuppressWarnings("deprecation")
			int windowsheight= defaultDisplay.getHeight();
			int originwidth = 480;
			

			width = (int) ( (float) 340 * ( (float)(windowswidth)/ (float)(originwidth) )  );

			
			menu.setBehindOffset(windowswidth - width );
		}
		
		menu.setFadeDegree(0.35f);
		
		AfterCreate(7);
		

		mWebView = (WebView)findViewById(R.id.pay_web);

		BtnEvent( R.id.sns_1_btn);
		BtnEvent( R.id.sns_2_btn);
		BtnEvent( R.id.sns_3_btn);
		

		
		ChangeTab(0);


		
	}
	

	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	public void BtnEvent( int id )
    {
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
    }

	
	public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	public void ImageBtnResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	public void RefreshUI()
	{
		mWebView.setVisibility(View.VISIBLE);
		((ImageView)findViewById(R.id.kakao_image)).setVisibility(View.GONE);
		final AppManagement _AppManager = (AppManagement) getApplication();

		// 웹뷰에서 자바스크립트실행가능        
		mWebView.getSettings().setJavaScriptEnabled(true); 
		// 구글홈페이지 지정        
		mWebView.loadUrl(URL);
		// WebViewClient 지정        
		mWebView.setWebViewClient(new WebViewClientClass());  


	}
	
	private class WebViewClientClass extends WebViewClient 
	{         
		@Override        
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{             
			view.loadUrl(url);             
			return true;        
		}     
	}
	

	public void ChangeTab(int index)
	{
		if ( m_Tab == index)
			return;
		
		m_Tab = index;
		((ImageView)findViewById(R.id.sns_1_btn)).setBackgroundResource(R.drawable.sns_blog_bt);
		((ImageView)findViewById(R.id.sns_2_btn)).setBackgroundResource(R.drawable.sns_facebook_bt);
		((ImageView)findViewById(R.id.sns_3_btn)).setBackgroundResource(R.drawable.sns_kakao_bt);
		
		switch(m_Tab)
		{
		case 0:
			((ImageView)findViewById(R.id.sns_1_btn)).setBackgroundResource(R.drawable.sns_blog_bt2);
			URL = "http://m.blog.naver.com";
			RefreshUI();
			break;
		case 1:
			((ImageView)findViewById(R.id.sns_2_btn)).setBackgroundResource(R.drawable.sns_facebook_bt2);
			URL = "http://m.facebook.com/#!/hoteljoinkr";
			RefreshUI();
			break;
		case 2:
			((ImageView)findViewById(R.id.sns_3_btn)).setBackgroundResource(R.drawable.sns_kakao_bt2);
			URL = "http://m.hoteljoin.com/mweb/tory.html";
			RefreshUI();
			break; 
		}
		
		
		
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{

		case R.id.sns_1_btn:
		{
			ChangeTab(0);
		}
			break;
		case R.id.sns_2_btn:
		{
			ChangeTab(1);

		}
			break;
		case R.id.sns_3_btn:
		{
			ChangeTab(2);

		}
			break;

		}
		
	}
	




	
	

	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
			{
				
			}
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 21:
			{

				onBackPressed();
			}
				break;
			case 23:
			{

			}
			case 30:
			{
				Toast.makeText(self
	                    .getApplicationContext(), 
	                    "담벼락에 글을 남겼습니다.",
	                    Toast.LENGTH_LONG).show();
				onBackPressed();
			}
				break;
			default:
				break;
			}

		}
    	
	};	
	
	
	private void alert(String message) {
		new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(R.string.app_name)
			.setMessage(message)
			.setPositiveButton(android.R.string.ok, null)
			.create().show();
	}

	
	

    

}

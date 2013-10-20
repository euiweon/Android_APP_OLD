package com.humapcontents.mapp;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;
import com.facebook.*;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.*;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.humapcontents.mapp.data.TwitterConstant;


public class SettingMainActivity extends MappBaseActivity implements OnClickListener {

	private SettingMainActivity self;
	

	private SharedPreferences mPrefs;

	Facebook facebook = new Facebook("463598960369393");
	

	private Switch facebookswitch ;
	private Switch twiterswitch;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);  // 인트로 레이아웃 출력     

        self = this;
        
        
        
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        
        /*
        
         * Get existing access_token if any
 
         */
        
        {
        	mPrefs = getSharedPreferences( "facebooks" ,MODE_PRIVATE);
            
            String access_token = mPrefs.getString("access_token", null);
     
            long expires = mPrefs.getLong("access_expires", 0);
     
            if(access_token != null) {
     
                facebook.setAccessToken(access_token);
     
            }
     
            if(expires != 0) {
     
                facebook.setAccessExpires(expires);
     
            }
        }
 

		

        
        ImageResize(R.id.main_layout);
        ImageResize(R.id.main_tab_1);
        ImageResize(R.id.main_tab_2);
        ImageResize(R.id.main_tab_3);
        ImageBtnResize(R.id.main_tab_4);
        ImageBtnResize(R.id.main_tab_5);
        ImageResize(R.id.main_tab_1_name);
        ImageResize(R.id.main_tab_2_name);
        ImageResize(R.id.main_tab_3_name);
        ImageResize(R.id.main_tab_4_name);
        ImageResize(R.id.main_tab_5_name);
        ImageResize(R.id.main_tab_1_line);
        ImageResize(R.id.main_tab_2_line);
        ImageResize(R.id.main_tab_3_line);
        ImageResize(R.id.main_tab_4_line);
        ImageResize(R.id.main_tab_5_line);
        ImageResize(R.id.main_tab_6_line);
        ImageBtnResize(R.id.main_tab_6);
        ImageResize(R.id.main_tab_6_name);
        
        
        ImageResize(R.id.main_tab_1_arrow);
        ImageResize(R.id.main_tab_4_arrow);
        ImageResize(R.id.main_tab_5_arrow);
        ImageResize(R.id.main_tab_6_arrow);
        
        ImageResize(R.id.logout_btn);
        ImageResize(R.id.facebook_switch);
        ImageResize(R.id.twitter_switch);
        
        String token ="";

        BottomMenuFullUp();
        AfterCreate( 7 );
        
        
        ImageBtnEvent(R.id.title_icon , this);
        
        
        twiterswitch = (Switch) findViewById(R.id.twitter_switch);
        
        
        if (twiterswitch != null) 
        {            
        	twiterswitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
				{
					// TODO Auto-generated method stub
					
					if(arg1)
					{
						Twitterlogin();
					}
					else
					{
						Twitterlogout();
					}
					
					
				}});
        }
        

        
        facebookswitch = (Switch) findViewById(R.id.facebook_switch);
        if (facebookswitch != null) 
        {            
        	facebookswitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@SuppressWarnings("deprecation")
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
				{
					// TODO Auto-generated method stub
					
					if(arg1)
					{
						// 일단 개발자 계정에 문제가 있기에 막아둔다. 
					     
						/* facebook.authorize(self, new String[] {}, new DialogListener() {
							 
				                public void onComplete(Bundle values) {
				 
				                    SharedPreferences.Editor editor = mPrefs.edit();
				                    editor.putString("access_token", facebook.getAccessToken());
				                    editor.putLong("access_expires", facebook.getAccessExpires());
				                    editor.commit();
				                    InitFacebook();
				 
				                }
				                public void onFacebookError(FacebookError error) {
				                	
				                }
				                public void onError(DialogError e) {
				                	
				                }
				                public void onCancel() {
				                	
				                }
						 });*/
						 

							self.ShowAlertDialLog( self ,"에러" , "추후에 구현 예정입니다. " );
		
							InitFacebook();
						 
					    

					    
					}
					else
					{
					     //tvStateofToggleButton.setText("OFF");
					}
					
					
				}});
        }
        

        
        
    }
    
    
    private void facebooklogout()
    {
      try
      {
    	  facebook.logout(this);      
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    
    
    
    public void InitFacebook()
    {
    	Switch s = (Switch) findViewById(R.id.facebook_switch);
    	
    	if(!facebook.isSessionValid())
    	{
    		s.setChecked(false);
    	}
    	else
    	{
    		s.setChecked(true);
    	}
    	
        
        
        Switch s2 = (Switch) findViewById(R.id.twitter_switch);
        String accessToken = TwitUtil.getAppPreferences(this, TwitterConstant.TWITTER_ACCESS_TOKEN);
        
        if ( accessToken == null || accessToken.equals("") || accessToken.equals("STATE_IS_LOGOUT")  )
        {
        	// 토큰이 없음
        	s2.setChecked(false);
        }
        else
        {
        	s2.setChecked(true);
        }
        
        
        
       /* Session session = Session.getActiveSession();
        if (session == null) 
        {
        	// 세션이 생성되지 않았다. 
        	s.setChecked(false);
        }
        else if (session.isOpened())
        {
        	// 세션의 생성 
        	s.setChecked(true);
        }
        else
        {
        	// 세션이 닫혔다.
        	s.setChecked(false);
        }*/
        
        
    }
    
   
    
    
   
    
    
    public void Twitterlogin()
	{
		final  AppManagement _AppManager = (AppManagement) getApplication();
		//mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				try {
  	              String accessToken = TwitUtil.getAppPreferences(self, TwitterConstant.TWITTER_ACCESS_TOKEN);
  	              String accessTokenSecret = TwitUtil.getAppPreferences(self, TwitterConstant.TWITTER_ACCESS_TOKEN_SECRET);
  	              Log.d("TAG", "accessToken : " + accessToken + "// accessTokenSecret : " + accessTokenSecret);
  	  
  	              if (accessToken != null && !"".equals(accessToken) && accessTokenSecret != null && !"".equals(accessTokenSecret)
  	                      && !accessToken.equals("STATE_IS_LOGOUT") && !accessTokenSecret.equals("STATE_IS_LOGOUT")) {
  	                  // 로그인 되어 있다면!!!
  	                  Log.d("TAG", " 로그인 되어 있다!!!");
  	                  _AppManager.mAccessToken = new twitter4j.auth.AccessToken(accessToken, accessTokenSecret);
  	                  Log.v(TwitterConstant.LOG_TAG, "accessToken : " + _AppManager.mAccessToken.getToken());
  	                  Log.v(TwitterConstant.LOG_TAG, "accessTokenSecret : " + _AppManager.mAccessToken.getTokenSecret());
  	              } else if ((accessToken.equals("STATE_IS_LOGOUT") && accessTokenSecret.equals("STATE_IS_LOGOUT")) || true) {
  	                  // 로그인이 안되어 있다거나, 로그아웃을 했을 경우!!
  	                 Log.d("TAG", "로그인 하자!!");
  	                  ConfigurationBuilder cb = new ConfigurationBuilder();
  	                  cb.setDebugEnabled(true);
  	                  cb.setOAuthConsumerKey(TwitterConstant.TWITTER_CONSUMER_KEY);
  	                  cb.setOAuthConsumerSecret(TwitterConstant.TWITTER_CONSUMER_SECRET);
  	                  TwitterFactory factory = new TwitterFactory(cb.build());
  	                  _AppManager.mTwitter = factory.getInstance();
  	                  _AppManager.mRqToken = _AppManager.mTwitter.getOAuthRequestToken(TwitterConstant.TWITTER_CALLBACK_URL);
  	                  Log.v(TwitterConstant.LOG_TAG, "AuthorizationURL >>>>>>>>>>>>>>> " + _AppManager.mRqToken.getAuthorizationURL());
  	                  
  	                  
  	                  handler.sendEmptyMessage(20);
  	  
  	                  
  	              }
  	          } catch (Exception e) {
  	              Log.d("TAG", e.getMessage());
  	              e.printStackTrace();
  	            handler.sendEmptyMessage(1);
  	          }
  	      }

		});
		thread.start();
	}

    
    
    
    
    private void Twitterlogout() 
    {
    	 
    	TwitUtil.setAppPreferences(this, TwitterConstant.TWITTER_ACCESS_TOKEN, "STATE_IS_LOGOUT");
    	TwitUtil.setAppPreferences(this, TwitterConstant.TWITTER_ACCESS_TOKEN_SECRET, "STATE_IS_LOGOUT");
    	 
    	         
    }

    
    

    	          
    
    
    
    

    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
     
            AppManagement _AppManager = (AppManagement) getApplication();
             // 액티비티가 정상적으로 종료되었을 경우
             if (resultCode == RESULT_OK) {
                 if (requestCode == TwitterConstant.TWITTER_LOGIN_CODE) {
                     try {
                    	 _AppManager.mAccessToken =  _AppManager.mTwitter.getOAuthAccessToken( _AppManager.mRqToken, data.getStringExtra("oauth_verifier"));
     
                         Log.v(TwitterConstant.LOG_TAG, "Twitter Access Token : " +  _AppManager.mAccessToken.getToken());
                         Log.v(TwitterConstant.LOG_TAG, "Twitter Access Token Secret : " +  _AppManager.mAccessToken.getTokenSecret());
     
                         TwitUtil.setAppPreferences(this, TwitterConstant.TWITTER_ACCESS_TOKEN,  _AppManager.mAccessToken.getToken());
                         TwitUtil.setAppPreferences(this, TwitterConstant.TWITTER_ACCESS_TOKEN_SECRET,  _AppManager.mAccessToken.getTokenSecret());
     
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 else
                 {
                	 // 페이스북 처리
                	 
                	 facebook.authorizeCallback(requestCode, resultCode, data);
                	 InitFacebook();
                	 
                 }
             }
         }

    
    
    

    private boolean isSubsetOf(List<String> permissions2,
			List<String> permissions3) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
    public void  onResume()
    {
    	super.onResume();
    	InitFacebook();
    	RefreshUI();
    }
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
    
    


	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		
		AppManagement _AppManager = (AppManagement) getApplication();
		switch(arg0.getId() )
		{
		case R.id.main_tab_1:
		{
			Intent intent;
            intent = new Intent().setClass(self, SettingLoginActivity.class);
            startActivity( intent ); 
		}
	
			break;
		case R.id.logout_btn:
		{
			_AppManager.m_MappToken= "";
			_AppManager.m_bLogin = false;
			_AppManager.m_LoginID = "";
			
       	 
	        SharedPreferences preferences = getSharedPreferences( "login" ,MODE_PRIVATE);
	        SharedPreferences.Editor editor = preferences.edit();
	        editor.putString("id", _AppManager.m_LoginID ); //키값, 저장값
	        editor.putString("skey",_AppManager.m_MappToken );
	        editor.commit();
			
			RefreshUI();
		}
			break;
		case R.id.main_tab_4:
		{
			if ( _AppManager.m_bLogin == false )
			{
				self.ShowAlertDialLog( self ,"에러" , "로그인 되어 있지 않습니다." );
			}
			else
			{
				Intent intent;
	            intent = new Intent().setClass(self, SettingPasswordActivity .class);
	            startActivity( intent ); 
			}
			break;
		}
		case R.id.main_tab_5:
		{
			Intent intent;
            intent = new Intent().setClass(self, SettingNoticeActivity .class);
            startActivity( intent ); 
		}
		
		
			break;
			
		case R.id.main_tab_6:
			
		{
			Intent intent;
            intent = new Intent().setClass(self, SettingAppInfoActivity.class);
            startActivity( intent ); 
		}
			break;
			
		
		}
		
	}
	
	public void RefreshUI()
	{
		
		AppManagement  _AppManager= (AppManagement) getApplication();
		
		TextView textview = (TextView)findViewById(R.id.main_tab_1_name);
		
		
		if (_AppManager.m_bLogin == false)
		{
			ImageView logout = (ImageView)findViewById(R.id.logout_btn);
			logout.setVisibility(View.GONE);
			logout.setOnClickListener(null);
			
			ImageView imageview = (ImageView)findViewById(R.id.main_tab_1);
			imageview.setOnClickListener(this);
			
			ImageView arrow = (ImageView)findViewById(R.id.main_tab_1_arrow);
			arrow.setVisibility(View.VISIBLE);
			
			textview.setText("로그인");
		}
		else
		{
			ImageView arrow = (ImageView)findViewById(R.id.main_tab_1_arrow);
			arrow.setVisibility(View.GONE);
			
			ImageView logout = (ImageView)findViewById(R.id.logout_btn);
			logout.setVisibility(View.VISIBLE);
			logout.setOnClickListener(this);
			
			ImageView imageview = (ImageView)findViewById(R.id.main_tab_1);
			imageview.setOnClickListener(null);
			
			textview.setText(_AppManager.m_LoginID );
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
				RefreshUI();
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
				AppManagement  _AppManager= (AppManagement) getApplication();
				Intent intent = new Intent(self, TwitterLogin.class);
                  intent.putExtra("auth_url", _AppManager.mRqToken.getAuthorizationURL());
                  startActivityForResult(intent, TwitterConstant.TWITTER_LOGIN_CODE);
			}
				break;
			default:
				break;
			}

		}
    	
	};
	
	

	
}

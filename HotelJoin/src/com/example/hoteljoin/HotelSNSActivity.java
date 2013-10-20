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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HotelSNSActivity extends HotelJoinBaseActivity implements OnClickListener{
	HotelSNSActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 
	 
	 
	private SharedPreferences mPrefs;
				
	 Facebook facebook = new Facebook("489115824472243");
	 private String encoding = "UTF-8";

	 
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotelsns);
		
		
		// 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
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
		
		AfterCreate(1);
		


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
		

		BtnEvent(R.id.sub_bar);
		
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
	
	
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{

		case R.id.sub_bar:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			
			switch(_AppManager.m_SNSSelect)
			{
			case  0:
				//카카오톡
			{
				try {
					sendUrlLink();
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				break;
			case 1:
				// 카카오 스토리
			{
				try {
					sendPostingLink();
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				break;
			case 2:
				// 페이스북
			{
				publishFacebook();
			}
				break;
			}
		}
			break;


		}
		
	}
	
	public void FaceBookLogin()
	{
		facebook.authorize(self, new String[] {"publish_actions"},  new DialogListener() {
			 
            public void onComplete(Bundle values) {

                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString("access_token", facebook.getAccessToken());
                editor.putLong("access_expires", facebook.getAccessExpires());
                editor.commit();
                publishFacebook();
            }
            public void onFacebookError(FacebookError error) {
            	self.ShowAlertDialLog( self ,"페이스북 에러" , error.getMessage() );
            }
            public void onError(DialogError e) {
            	self.ShowAlertDialLog( self ,"에러" , e.getMessage() );
            }
            public void onCancel() {
            	
            }
	 });
	}
	
	private void publishFacebook() {

		if(!facebook.isSessionValid())
    	{

			FaceBookLogin();

    	}
    	else
    	{
    		
    		mProgress.show();
    		Thread thread = new Thread(new Runnable()
    		{
    			public void run() 
    			{
    				EditText id = (EditText)findViewById(R.id.write_text);
    				AppManagement _AppManager = (AppManagement) getApplication();
    	    		Bundle postParams = new Bundle();
    	            postParams.putString("name", "Hotel Join");
    	            postParams.putString("caption","test");
    	            postParams.putString("description", id.getText().toString());
    	            postParams.putString("picture", _AppManager.m_MainimageURL);
    	            postParams.putString("message",  id.getText().toString());
    	            postParams.putString("link", "");
    	          
    	            
    	            
    	            try {
    					facebook.request("me/feed", postParams, "POST");
    					handler.sendEmptyMessage(30);
    					
    				} catch (FileNotFoundException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (MalformedURLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    		});
    		thread.start();

    	}

    }
	
	/**
	 * Story Link
	 * Send posting (text or url)
	 * @throws NameNotFoundException 
	 */
	public void sendPostingLink() throws NameNotFoundException {
		

		
		EditText id = (EditText)findViewById(R.id.write_text);
		AppManagement _AppManager = (AppManagement) getApplication();
		Map<String, Object> urlInfoAndroid = new Hashtable<String, Object>(1);
		urlInfoAndroid.put("title", _AppManager.m_HotelName);
		urlInfoAndroid.put("desc",id.getText().toString());
		urlInfoAndroid.put("imageurl", new String[] {_AppManager.m_MainimageURL});
		urlInfoAndroid.put("type", "article");
		

		// Recommended: Use application context for parameter.
		StoryLink storyLink = StoryLink.getLink(getApplicationContext());

		// check, intent is available.
		if (!storyLink.isAvailableIntent()) {
			alert("Not installed KakaoStory.");			
			return;
		}

		/**
		 * @param activity
		 * @param post (message or url)
		 * @param appId
		 * @param appVer
		 * @param appName
		 * @param encoding
		 * @param urlInfoArray
		 */
		storyLink.openKakaoLink(this, 
				"http://hoteljoin.com",
				getPackageName(), 
				getPackageManager().getPackageInfo(getPackageName(), 0).versionName, 
				"Hotel Join",
				encoding, 
				urlInfoAndroid);
	}
	
	/**
	 * Send URL
	 * @throws NameNotFoundException 
	 */
	public void sendUrlLink() throws NameNotFoundException {
		// Recommended: Use application context for parameter.
		KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());

		// check, intent is available.
		if (!kakaoLink.isAvailableIntent()) {
			alert("Not installed KakaoTalk.");			
			return;
		}

		/**
		 * @param activity
		 * @param url
		 * @param message
		 * @param appId
		 * @param appVer
		 * @param appName
		 * @param encoding
		 */
		EditText id = (EditText)findViewById(R.id.write_text);
		AppManagement _AppManager = (AppManagement) getApplication();

		kakaoLink.openKakaoLink(this, 
				"http://hoteljoin.com", 
				_AppManager.m_HotelName, 
				getPackageName(), 
				getPackageManager().getPackageInfo(getPackageName(), 0).versionName, 
				id.getText().toString(), 
				encoding);
	}

	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) 
	{
		if (resultCode != RESULT_OK) {
			return;
		}

		Cursor c = getContentResolver().query(intent.getData(), null, null, null, null);
		c.moveToNext();
		String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
		c.close();
		
		StoryLink storyLink = StoryLink.getLink(getApplicationContext());

		// check, intent is available.
		if (!storyLink.isAvailableIntent()) 
		{
			alert("Not installed KakaoStory.");
			return;
		}
		
		storyLink.openStoryLinkImageApp(this, path);
		
		facebook.authorizeCallback(requestCode, resultCode, intent);

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

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;
import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;


public class SettingLoginActivity extends MappBaseActivity implements OnClickListener {

	private SettingLoginActivity self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_login_layout);  // 인트로 레이아웃 출력     

        self = this;
        
        
        
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
		

        
        ImageResize(R.id.main_layout);
        ImageResize(R.id.login_input_image);
        ImageResize(R.id.login_id_data);
        ImageResize(R.id.login_pass_data);
        ImageBtnResize(R.id.login_btn);
        ImageBtnResize(R.id.login_lost_pass);
        ImageBtnResize(R.id.login_join_btn);
        

        
        String token ="";

        BottomMenuFullUp();
        AfterCreate( 7 );
        
        ImageBtnEvent(R.id.title_icon , this);

        
        
    }
    
    

    @Override
    public void  onResume()
    {
    	super.onResume();
    	RefreshUI();
    }
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
    
    


	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		
		AppManagement _AppManager = (AppManagement) getApplication();
		switch(arg0.getId() )
		{
		case R.id.login_btn:
			Login();
			break;
		case R.id.login_lost_pass:
			
		{
			Intent intent;
            intent = new Intent().setClass(self, SettingLostPasswordActivity.class);
            startActivity( intent ); 
			
		}
			break;
		case R.id.login_join_btn:
			
		{
			Intent intent;
            intent = new Intent().setClass(self, SettingJoinActivity.class);
            startActivity( intent ); 
			
		}
			break;
			
		case R.id.title_icon:
		{
			onBackPressed();
		}
		break;
		}
		
		
		
	}
	
	public void RefreshUI()
	{
		
		AppManagement _AppManager = (AppManagement) getApplication();
	}
	
	
	public void Login()
	{
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				// ID와 패스워드를 가져온다 
				

				EditText id = (EditText)findViewById(R.id.login_id_data);
				EditText pass = (EditText)findViewById(R.id.login_pass_data);
				Map<String, String> data = new HashMap  <String, String>();

				data.put("id", id.getText().toString());
				data.put("password", getMD5Hash(id.getText().toString()+ pass.getText().toString()));
				
				/*data.put("id", "ds1924@naver.com");
				data.put("password", getMD5Hash("ds1924@naver.comjem124"));*/
				
				
				String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Login", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.optString("errcode").equals("0"))
					{

						_AppManager.m_MappToken=  json.optString("skey");
						_AppManager.m_bLogin = true;
						_AppManager.m_LoginID = id.getText().toString();
						_AppManager.m_Password = pass.getText().toString();
						handler.sendEmptyMessage(0);
					}
					else 
					{
						// 에러 메세지를 전송한다. 
						handler.sendMessage(handler.obtainMessage(1,_AppManager.GetError(json.optInt("errcode")) ));
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler.sendMessage(handler.obtainMessage(1,"Error" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}


	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				AppManagement _AppManager = (AppManagement) getApplication();
	        	 
		        SharedPreferences preferences = getSharedPreferences( "login" ,MODE_PRIVATE);
		        SharedPreferences.Editor editor = preferences.edit();
		        editor.putString("id", _AppManager.m_LoginID ); //키값, 저장값
		        editor.putString("skey",_AppManager.m_MappToken );
		        editor.commit();



				onBackPressed();
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
				break;
			default:
				break;
			}

		}
    	
	};
	
	

	
}

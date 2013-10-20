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



import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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


public class SettingLostPasswordActivity extends MappBaseActivity implements OnClickListener {

	private SettingLostPasswordActivity self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_password_lost_layout);  // 인트로 레이아웃 출력     

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
        ImageResize(R.id.old_pass_input_image);
        ImageResize(R.id.oldpass_data);
        


        
        ImageBtnResize(R.id.title_icon2);
        

        
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
		case R.id.title_icon:
		{
			onBackPressed();
		}
			break;
		case R.id.login_lost_pass:
			
		{
			
		}
			break;
		case R.id.title_icon2:
		{
			ChangePassword();
		}
			break;
		}
		
	}
	
	public void RefreshUI()
	{
		
		AppManagement _AppManager = (AppManagement) getApplication();
	}
	
	
	public void ChangePassword()
	{
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				// ID와 패스워드를 가져온다 
				

				
				
				EditText oldpass = (EditText)findViewById(R.id.oldpass_data);

				Map<String, String> data = new HashMap  <String, String>();

				data.put("id", oldpass.getText().toString());

				String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/RecoveryPassword", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.optString("errcode").equals("0"))
					{
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
				Toast.makeText(self
	                    .getApplicationContext(), 
	                    "메일이 발송되었습니다. ",
	                    Toast.LENGTH_LONG).show();
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
			case 4:
				self.ShowAlertDialLog( self ,"에러" , "이전 비밀번호가 틀립니다." );
				break;
			case 5:
				self.ShowAlertDialLog( self ,"에러" , "새로운 비밀번호가 같지 않습니다." );
				break;
			case 20:
				break;
			default:
				break;
			}

		}
    	
	};
	
	

	
}

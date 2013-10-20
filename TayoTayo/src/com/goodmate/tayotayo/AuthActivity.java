package com.goodmate.tayotayo;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.euiweonjeong.base.BaseActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AuthActivity extends BaseTayoActivity {

	AuthActivity self;
	
	String code = ""; 
	private Handler handler = new Handler();
	
	String PhoneNumber= "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_confirm);  // 인트로 레이아웃 출력      
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        

        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	_AppManager.activityList1.add(this);
        }
      
        self = this;
        
        AfterCreate();
        
        BtnEvent(R.id.phone_request);
        BtnEvent(R.id.phone_request2);
        
        
        TelephonyManager telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
        String phoneNum = telManager.getLine1Number();
        
        ((EditText)findViewById(R.id.login_id_data)).setText(phoneNum);
        
        
        {
        	
        	Log.e(TAG, android.os.Build.SERIAL );
        	
        }
    }

    
    @Override
    public void  onResume()
    {
    	super.onResume();
    	RefreshUI();
    }
    
	@Override
    public void onBackPressed() 
    {
    	
		// 이전 화면으로 못 돌아가게 막는다.( 아예 종료가 되도록한다 )
    	// 일단은 돌아갈수 있게 테스트용으로 열어둔다. 
    	new AlertDialog.Builder(this)
		 .setTitle("종료")
		 .setMessage("종료하시겠습니까?") //줄였음
		 .setPositiveButton("예", new DialogInterface.OnClickListener() 
		 {
		     public void onClick(DialogInterface dialog, int whichButton)
		     {   
		    	 Intent intent;
		          // Create an Intent to launch an Activity for the tab (to be reused)
		          intent = new Intent().setClass(self, OutroActivity.class);
		          
		          
		          startActivity( intent ); 
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
	
	
	public void BtnEvent( int id )
    {
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(new OnClickListener()
        {
            public void onClick(View arg0)
            {
            	switch(arg0.getId())
            	{
            	case R.id.phone_request:
            	{
            		GetRequestNumberData();
            	}
            		break;
            	case R.id.phone_request2:
            	{
            		GetAuthenticaePhoneData();
            	}
            		break;
            		
            		
            	}
            	
            }
        });

    }
	
	public void GetRequestNumberData()
    {
    	{
    		mProgress.setMessage("인증번호 발송을 진행중입니다. ");
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{


					EditText oldpass = (EditText)findViewById(R.id.login_id_data);
					Map<String, String> data = new HashMap  <String, String>();
					

					data.put("phoneNumber", oldpass.getText().toString());

					
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/RequestPhoneAuthenticationCode", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("IsSuccess").equals("true"))
						{

							JSONObject usageList = (JSONObject) json.opt("Contents");
							
							{
								code = usageList.optString("Code", "");
							}
							
							PhoneNumber = oldpass.getText().toString();
							
							handler2.sendEmptyMessage(0);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler2.sendMessage(handler2.obtainMessage(1,(json.optString("errcode")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler2.sendMessage(handler2.obtainMessage(1,"ReqeustCode" ));
						e.printStackTrace();
					} 
				}
			});
			thread.start();
		}
    }
	
	
	public void GetAuthenticaePhoneData()
    {
    	{
    		mProgress.setMessage("인증번호를 비교중입니다.  ");
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{


					EditText oldpass = (EditText)findViewById(R.id.login_id_data);
					EditText oldpass1 = (EditText)findViewById(R.id.login_pass_data);
					Map<String, String> data = new HashMap  <String, String>();
					

					data.put("phoneNumber", oldpass.getText().toString());
					data.put("code", oldpass1.getText().toString());

					if(code.equals(oldpass1.getText().toString()))
					{
						handler2.sendEmptyMessage(3);
					}
					else
					{
						handler2.sendEmptyMessage(1);
					}
					
					/*String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/AuthenticaePhone", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("IsSuccess").equals("true"))
						{

							JSONObject usageList = (JSONObject) json.opt("Contents");
							
							if ( usageList.optString("AuthenticationResult").equals("true"))
							{
								handler2.sendEmptyMessage(3);
							}
							else
							{
								handler2.sendEmptyMessage(1);
							}
							
							
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler2.sendMessage(handler2.obtainMessage(1,(json.optString("IsSuccess")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler2.sendMessage(handler2.obtainMessage(1,strJSON ));
						e.printStackTrace();
					} */
				}
			});
			thread.start();
		}
    }
	
	final Handler handler2 = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				self.ShowAlertDialLog( self ,"인증번호발송" , "인증번호를 발송하였습니다." );
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			case 2:
				break;
				
			case 3:
			{
				TelephonyManager telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
     	       String phoneNum = telManager.getLine1Number();

     	      phoneNum = PhoneNumber;
				// 고유 식별 번호를 여기서 입력해준다.
				AppManagement _AppManager = (AppManagement) getApplication();
				SharedPreferences preferences = getSharedPreferences( "userinfo" ,MODE_PRIVATE);
				SharedPreferences.Editor edit = preferences.edit();
				edit.putString("uuid", _AppManager.DeviceID);
				edit.putString("phone", phoneNum);
				edit.commit();
				
				// 다음화면으로...
				Intent intent;
	       		intent = new Intent().setClass(self, MainActivity.class);
	    	    startActivity( intent ); 
			}
				break;
			case 20:
				break;
			default:
				break;
			}

		}
    	
	};
	
	
    
}

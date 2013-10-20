package com.hoteljoin;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends HotelJoinBaseActivity implements OnClickListener{

	LoginActivity self;
	 int MenuSize;
	 
	 Boolean viewLoginMenu = false; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);
		
		self = this;
		
	     // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        

        


		AfterCreate();
		
		
		BtnEvent( R.id.no_member_tab);
		BtnEvent( R.id.member_tab_2);	
		BtnEvent( R.id.login_btn_3);	
		
		
		BtnEvent( R.id.login_btn_2);	
		BtnEvent( R.id.join_btn);
		BtnEvent( R.id.join_btn_2);
		
		BtnEvent( R.id.id_clear);
		BtnEvent( R.id.id_clear2);
		BtnEvent( R.id.pass_clear);
		BtnEvent( R.id.pass_clear2);
		BtnEvent( R.id.auto_login);
		
		{
			SharedPreferences preferences = getSharedPreferences( "login" ,MODE_PRIVATE);
			if (preferences.getBoolean("auto", false))
			{
				((ImageView)findViewById(R.id.auto_login)).setBackgroundResource(R.drawable.check_on);
			}
			else
			{
				((ImageView)findViewById(R.id.auto_login)).setBackgroundResource(R.drawable.check_off);
			}
		}
		
		
		RefreshUI();

	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		viewLoginMenu = false;
		RefreshUI();
	}
	
	public void RefreshUI()
	{
		if (viewLoginMenu == false )
		{
			((View)findViewById(R.id.main_layout_1)).setVisibility(View.VISIBLE);
			((View)findViewById(R.id.main_layout_2)).setVisibility(View.GONE);
			
		}
		else
		{
			((View)findViewById(R.id.main_layout_1)).setVisibility(View.GONE);
			((View)findViewById(R.id.main_layout_2)).setVisibility(View.VISIBLE);
		}
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
		case R.id.auto_login:
		{
			SharedPreferences preferences = getSharedPreferences( "login" ,MODE_PRIVATE);
			
			if (!preferences.getBoolean("auto", false))
			{
				((ImageView)findViewById(R.id.auto_login)).setBackgroundResource(R.drawable.check_on);
			}
			else
			{
				((ImageView)findViewById(R.id.auto_login)).setBackgroundResource(R.drawable.check_off);
			}
			
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("auto", !preferences.getBoolean("auto", false) );
	        editor.commit();
	        
			
		}
			break;
		case R.id.id_clear:
		{
			((EditText)findViewById(R.id.login_id_data_4)).setText("");
		}
			break;
		case R.id.id_clear2:
		{
			((EditText)findViewById(R.id.login_id_data_2)).setText("");
		}
			break;
		case R.id.pass_clear2:
		{
			((EditText)findViewById(R.id.login_rese_number)).setText("");
		}
			break;
			
		case R.id.pass_clear:
		{
			((EditText)findViewById(R.id.login_pass_data_4)).setText("");
		}
			break;
		case R.id.no_member_tab:
			viewLoginMenu = true;
			 RefreshUI();
			break;
		case R.id.member_tab_2:
			viewLoginMenu = false;
			 RefreshUI();
			break;
		case R.id.login_btn_3:
			Login( ((EditText)findViewById(R.id.login_id_data_4)).getText().toString(), ((EditText)findViewById(R.id.login_pass_data_4)).getText().toString());
			break;
		case  R.id.login_btn_2:
			//  예약번호로 로그인
          
			{
/*				AppManagement _AppManager = (AppManagement) getApplication();
				EditText id = (EditText)findViewById(R.id.login_id_data_2);
				EditText pass = (EditText)findViewById(R.id.login_rese_number);
				_AppManager.m_ReservationName = id.getText().toString();
				_AppManager.m_ReservationNumber = pass.getText().toString();
				Intent intent;
	            intent = new Intent().setClass(baseself, ReservationActivity.class);
	            startActivity( intent ); */
			}
          
			break;
		case R.id.join_btn:
		case R.id.join_btn_2:
			Intent intent;
            intent = new Intent().setClass(baseself, JoinActivity.class);
            startActivity( intent );
			break;
		}
		
	}
	
	

	
	 @Override
	protected void onActivityResult(int requestCode, int resultcode, Intent data)
	{
		super.onActivityResult(requestCode, resultcode, data);
		AppManagement _AppManager = (AppManagement) getApplication();
		if ( requestCode == 1 )
		{
			if ( resultcode == 10 )
			{
					
				int callnum  = data.getIntExtra("return", -1);
				// 각자 타입에 따라서 parsing 하기 
					
				switch ( callnum )
				{
				case 38:
					Log.d("API Call Success", _AppManager.ParseString);
					_AppManager.PParsingClass.LoginParsing(_AppManager.ParseString);
					
					if ( _AppManager.PErrorCode == 0 )
					{
						
						{
							SharedPreferences preferences = getSharedPreferences( "login" ,MODE_PRIVATE);
					        SharedPreferences.Editor editor = preferences.edit();
					        editor.putString("id", ((EditText)findViewById(R.id.login_id_data_4)).getText().toString() ); //키값, 저장값
					        editor.putString("name",_AppManager.PParsingData.login.name );
					        editor.putString("email",_AppManager.PParsingData.login.email );
					        editor.putString("nickname",_AppManager.PParsingData.login.nickname );
					        editor.putString("mobile",_AppManager.PParsingData.login.mobile );
					        editor.putString("pass",((EditText)findViewById(R.id.login_pass_data_4)).getText().toString() );
					        editor.commit();
						}
						
						// 마일리지 정보를 받아와야함. 
						_AppManager.ParamData.clear();
				    	_AppManager.ParamData.put("memberId", ((EditText)findViewById(R.id.login_id_data_4)).getText().toString());

				    	Intent intent = new Intent(baseself, NetPopup.class);
				    	intent.putExtra("API", 46);
						startActivityForResult(intent , 1);
					}
					else
					{
						self.ShowAlertDialLog(self,"에러", _AppManager.PErrorMsg);
					}
					
					break;
				case 46:
					if ( _AppManager.PErrorCode == 0 )
					{
						// 마일리지 정보 
						Log.d("API Call Success", _AppManager.ParseString);
						_AppManager.PParsingClass.MyBenefitsInfo(_AppManager.ParseString);
						onBackPressed();
					}
					break;
				}
			}
			else
			{
				Log.d("API Call failed", "강제로 네트워크 API 종료함");
			}
			
		}
		else
		{
			Log.w("error", "여긴 그냥 잘못 들어온것, 네트워크 문제이거나 그냥 코드 문제임으로 그냥 종료 시켜야함");
		}
		
		
	}


}

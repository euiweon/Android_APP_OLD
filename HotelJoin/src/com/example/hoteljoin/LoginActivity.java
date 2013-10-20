package com.example.hoteljoin;

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
	 SlidingMenu menu ;
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
        

        
		// 비하인드 뷰 설정
		setBehindContentView(R.layout.main_menu_1);
		

		
		// 슬라이딩 뷰
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.RIGHT);
		
		{
			Display defaultDisplay = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    	
			int width;
			@SuppressWarnings("deprecation")
			int windowswidth = defaultDisplay.getWidth();
			@SuppressWarnings("deprecation")
			int windowsheight= defaultDisplay.getHeight();
			int originwidth = 480;
			

			width = (int) ( (float) 359 * ( (float)(windowswidth)/ (float)(originwidth) )  );

			
			sm.setBehindOffset(windowswidth - width );
		}
		
		sm.setFadeDegree(0.35f);
		
		BottomMenuDown(true);
		AfterCreate(0);
		
		
		BtnEvent( R.id.no_member_tab);
		BtnEvent( R.id.member_tab_2);	
		BtnEvent( R.id.login_btn);	
		
		
		BtnEvent( R.id.login_btn_2);	
		BtnEvent( R.id.join_btn);
		BtnEvent( R.id.join_btn_2);
		
		
		RefreshUI();
		
		
		EditText et =(EditText)findViewById(R.id.login_id_data);
		et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Log.w("onTextChanged", s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				Log.w("onTextChanged", s.toString());				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.w("onTextChanged", s.toString());				
			}


		});
		
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
		case R.id.no_member_tab:
			viewLoginMenu = true;
			 RefreshUI();
			break;
		case R.id.member_tab_2:
			viewLoginMenu = false;
			 RefreshUI();
			break;
		case R.id.login_btn:
			Login();
			break;
		case  R.id.login_btn_2:
			//  예약번호로 로그인
          
			{
				AppManagement _AppManager = (AppManagement) getApplication();
				EditText id = (EditText)findViewById(R.id.login_id_data_2);
				EditText pass = (EditText)findViewById(R.id.login_rese_number);
				_AppManager.m_ReservationName = id.getText().toString();
				_AppManager.m_ReservationNumber = pass.getText().toString();
				Intent intent;
	            intent = new Intent().setClass(baseself, ReservationActivity.class);
	            startActivity( intent ); 
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
	
	
	public void Login()
	{
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				// ID와 패스워드를 가져온다 
				

				EditText id = (EditText)findViewById(R.id.login_id_data_4);
				EditText id2 = (EditText)findViewById(R.id.login_id_data_2);
				EditText pass = (EditText)findViewById(R.id.login_pass_data_4);
				Map<String, String> data = new HashMap  <String, String>();

				data.put("memberId", id.getText().toString());
				data.put("password", pass.getText().toString());
				
				String iddata =  id.getText().toString();
				
				if (id.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(2);
					return;
				}
				
				if (pass.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(3);
					return;
				}
				
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/member/login.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						

						_AppManager.m_LoginCheck = true;
						_AppManager.m_LoginID = id.getText().toString();
						_AppManager.m_Password = pass.getText().toString();
						
						_AppManager.m_NickName = _AppManager.GetHttpManager().DecodeString(json.getString("nickname"));
						_AppManager.m_Name = _AppManager.GetHttpManager().DecodeString(json.getString("name"));
						_AppManager.m_Email = _AppManager.GetHttpManager().DecodeString(json.getString("email"));
						_AppManager.m_Mobile =_AppManager.GetHttpManager().DecodeString(json.getString("mobile"));
						handler.sendEmptyMessage(0);
					}
					else 
					{
						// 에러 메세지를 전송한다. 
						handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.getString("errorMsg")) ));
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler.sendMessage(handler.obtainMessage(1,"json Data Error \n" + e.getMessage() ));
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
		        editor.putString("name",_AppManager.m_Name );
		        editor.putString("email",_AppManager.m_Email );
		        editor.putString("nickname",_AppManager.m_NickName );
		        editor.putString("mobile",_AppManager.m_Mobile );
		        editor.putString("pass",_AppManager.m_Password );
		        editor.commit();
		        _AppManager.m_RefreshUI  = true;
		 
				

				onBackPressed();
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			case 2:
				self.ShowAlertDialLog( self ,"에러" , "아이디가 입력되지 않았습니다.");
				break;	
			case 3:
				self.ShowAlertDialLog( self ,"에러" , "패스워드가 입력되지 않았습니다.");
				break;
			case 20:
				break;
			default:
				break;
			}

		}
    	
	};


    

}

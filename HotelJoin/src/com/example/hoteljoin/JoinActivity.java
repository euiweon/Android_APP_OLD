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
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class JoinActivity extends HotelJoinBaseActivity implements OnClickListener{

	 SlidingMenu menu ;
	 int MenuSize;
	 
	 Boolean checkbox1 = false;
	 Boolean checkbox2 = false;
	 Boolean checkbox3 = false;
	 
	 
	 Boolean viewLoginMenu = false; 
	 JoinActivity self;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_main);
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
		
		
		BtnEvent( R.id.checkbox_1);
		BtnEvent( R.id.checkbox_2);	
		BtnEvent( R.id.checkbox_3);	
		
		
		BtnEvent( R.id.id_check);
		BtnEvent( R.id.detail_btn_1);
		BtnEvent( R.id.detail_btn_2);	
		BtnEvent( R.id.detail_btn_3);	
		
		BtnEvent( R.id.next_btn);
		RefreshUI();
	}
	
	
	public void RefreshUI()
	{
		if (checkbox1 == false )
		{
			((ImageView)findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.checkbox_1);
		}
		else
		{
			((ImageView)findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.checkbox_2);
		}
		
		if (checkbox2 == false )
		{
			((ImageView)findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.checkbox_1);
		}
		else
		{
			((ImageView)findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.checkbox_2);
		}
		if (checkbox3 == false )
		{
			((ImageView)findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.checkbox_1);
		}
		else
		{
			((ImageView)findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.checkbox_2);
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
		AppManagement _AppManager = (AppManagement) getApplication();
		switch(v.getId())
		{
		case R.id.checkbox_1:
			checkbox1 = !checkbox1;
			RefreshUI();
			break;
		case R.id.checkbox_2:
			checkbox2 = !checkbox2;
			RefreshUI();
			break;
		case R.id.checkbox_3:
			checkbox3 = !checkbox3;
			RefreshUI();
			break;
		case R.id.id_check:
			CheckID();
			break;
		case  R.id.detail_btn_1:
		{
			_AppManager.m_URL = "http://m.hoteljoin.com/mweb/agreement.html";
			_AppManager.m_URLTitle = "이용약관";
			Intent intent;
            intent = new Intent().setClass(baseself, WebActivity.class);
            startActivity( intent ); 
		}

			break;
		case  R.id.detail_btn_2:
		{
			_AppManager.m_URL = "http://m.hoteljoin.com/mweb/policy.html";
			_AppManager.m_URLTitle = "개인정보 취급방침";
			Intent intent;
            intent = new Intent().setClass(baseself, WebActivity.class);
            startActivity( intent ); 
		}
			break;
		case  R.id.detail_btn_3:
		{
			_AppManager.m_URL = "http://m.hoteljoin.com/mweb/thirdPolicy.html";
			_AppManager.m_URLTitle = "제3자 정보제공동의";
			Intent intent;
            intent = new Intent().setClass(baseself, WebActivity.class);
            startActivity( intent ); 
		}
			break;
		case R.id.next_btn:
			MakeID();
			break;
		}
		
	}
    
	
	public void CheckID()
	{
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				// ID와 패스워드를 가져온다 
				

				EditText id = (EditText)findViewById(R.id.login_id_data_3);
				Map<String, String> data = new HashMap  <String, String>();

				data.put("memberId", id.getText().toString());

				
				if (id.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(7);
				}
				
				
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/member/validateId.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{

						handler.sendEmptyMessage(6);
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
					handler.sendMessage(handler.obtainMessage(1,"Error" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	

	
	public void MakeID()
	{
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				// ID와 패스워드를 가져온다 
				

				EditText id = (EditText)findViewById(R.id.login_id_data_3);
				EditText pass = (EditText)findViewById(R.id.login_pass_data_3);
				EditText pass1 = (EditText)findViewById(R.id.login_pass_data_1);
				EditText mail = (EditText)findViewById(R.id.login_email_data);
				EditText phone = (EditText)findViewById(R.id.login_phone_number_data);
				EditText name = (EditText)findViewById(R.id.login_name_data);
				EditText nick = (EditText)findViewById(R.id.login_nick_data);
				
				
				Map<String, String> data = new HashMap  <String, String>();

				data.put("memberId", id.getText().toString());
				data.put("nickname", nick.getText().toString());
				data.put("name", name.getText().toString());
				data.put("password", pass.getText().toString());
				data.put("email", mail.getText().toString());
				data.put("mobile", phone.getText().toString());
				
				if (id.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(7);
					return;
				}
				if (pass1.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(8);
					return;
				}
				if (pass.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(8);
					return;
				}
				
				if (!pass.getText().toString().equals(pass1.getText().toString()))
				{
					handler.sendEmptyMessage(8);
					return;
				}
				
				if (phone.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(10);
					return;
				}
				
				if (name.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(11);
					return;
				}
				
				if (nick.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(12);
					return;
				}
				

				if (mail.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(9);
					return;
				}
				
				else if (!isEmailPattern(mail.getText().toString()) )
				{
					handler.sendEmptyMessage(27);
					return;
					
				}
				
				if ( checkbox1== false )
				{
					handler.sendEmptyMessage(2);
					return;
				}
				if ( checkbox2== false )
				{
					handler.sendEmptyMessage(3);
					return;
				}
				
				if ( checkbox3== false )
				{
					handler.sendEmptyMessage(4);
					return;
				}
				
				
				String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mweb/member/register.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{

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
				onBackPressed();				
				Toast.makeText(getApplicationContext(),"회원가입이 되셨습니다.",Toast.LENGTH_SHORT).show();
				
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			case 2:
				self.ShowAlertDialLog( self ,"에러" , "이용 약관에 동의 하지 않았습니다.");
				break;	
			case 3:
				self.ShowAlertDialLog( self ,"에러" , "개인정보 제공에 동의 하지 않았습니다.");
				break;
			case 4:
				self.ShowAlertDialLog( self ,"에러" , "제 3자 정보 제공에 동의 하지 않았습니다.");
				break;
			case 5:
				self.ShowAlertDialLog( self ,"에러" , "중복된 아이디입니다. ");
				break;
			case 6:
				self.ShowAlertDialLog( self ,"사용가능" , "사용가능한 아이디입니다. ");
				break;
			case 7:
				self.ShowAlertDialLog( self ,"에러" , "아이디를 입력해주세요.  ");
				break;
			case 8:
				self.ShowAlertDialLog( self ,"에러" , "비밀번호를 다시 확인해주세요. ");
				break;
			case 9:
				self.ShowAlertDialLog( self ,"에러" , "E-Mail 주소를 제대로 입력해주세요. ");
				break;
			case 10:
				self.ShowAlertDialLog( self ,"에러" , "전화번호를 제대로 입력해주세요.");
				break;
			case 11:
				self.ShowAlertDialLog( self ,"에러" , "이름을 제대로 입력해주세요.");
				break;
			case 12:
				self.ShowAlertDialLog( self ,"에러" , "닉네임을 제대로 입력해주세요.");
				break;
				
			case 20:
				break;
			case 27:
				self.ShowAlertDialLog( self ,"에러" , "올바르지 않은 메일주소입니다. ");
				break;
			default:
				break;
			}

		}
    	
	};
	

}

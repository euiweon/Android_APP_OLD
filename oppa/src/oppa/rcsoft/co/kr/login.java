package oppa.rcsoft.co.kr;

import java.util.HashMap;
import java.util.Map;

import oppa.rcsoft.co.kr.MyInfo.MyAccountData;

import org.json.JSONException;
import org.json.JSONObject;




import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


// 로그인 화면 
public class login extends BaseActivity implements OnClickListener {
    /** Called when the activity is first created. */
	   /** Called when the activity is first created. */
		private login  self = null;

	    @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        self = this;
	        setContentView(R.layout.home_login_layout);
	        
			mProgress = new ProgressDialog(this);
			mProgress.setMessage("잠시만 기다려 주세요.");
			mProgress.setIndeterminate(true);
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgress.setCancelable(false);
			
	        
	        Button LoginBtn = (Button)findViewById(R.id.home_login_login);
	        LoginBtn.setOnClickListener(this);
	        Button FindBtn = (Button)findViewById(R.id.home_login_account_find_btn);
	        FindBtn.setOnClickListener(this);
	        Button CreateBtn = (Button)findViewById(R.id.home_login_account_create_btn);
	        CreateBtn.setOnClickListener(this);
	        
	        
	        {
	        	MyInfo myApp = (MyInfo) getApplication();
	        	
	        	MyAccountData Mydata = myApp.GetAccountData();
	        	
	        	CheckBox IDSave = (CheckBox)findViewById(R.id.home_login_id_save_check);
				CheckBox AutoLogin = (CheckBox)findViewById(R.id.home_login_auto_save_check);
	        	
	        	if (Mydata.AutoLogin == true )
	        	{
					EditText IDBOX = (EditText)findViewById(R.id.home_login_id_textbox);
					IDBOX.setText(Mydata.ID);
					
					
					EditText Password = (EditText)findViewById(R.id.home_login_password_textbox);
					Password.setText(Mydata.Password);
					IDSave.setChecked(true);
					AutoLogin.setChecked(true);
					
					Login();

	        	}
	        	else if ( Mydata.IDSave == true)
	        	{
					EditText IDBOX = (EditText)findViewById(R.id.home_login_id_textbox);
					IDBOX.setText(Mydata.ID);
					IDSave.setChecked(true);
	        	}
	        	
	        }
	        
	        

	        
	        

	    }
	    public void onResume()
	    {
	    	// 로그인 된 상태의 경우인데 여기로 들어왔을 경우 강제로 이동한다. 
	    	 super.onResume();
	    }
	    
	    
	    @Override
		public boolean onKeyDown(int keyCode, KeyEvent event)
		{
	    	if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
			{
				return true;
			}
			Log.e(TAG, "onKeyDown");
			return super.onKeyDown(keyCode, event);

		}
	    
	    public void onClick(View v )
	    {
	    	switch(v.getId())
	    	{
	    	case R.id.home_login_login:
	    	{
	    		HideKeyBoard();
	    		Login();
				
	    	}
	    		break;
	    	case R.id.home_login_account_create_btn:
	    	{
    		
	           Intent intent;
	           intent = new Intent().setClass(this, login_adult.class);
	           startActivity( intent );   
	           
	    	}
	    		break;
	      	case R.id.home_login_account_find_btn:
	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, login_finder.class);
		        startActivity( intent );   
		           
	    	}
	    		break;
	    	default:
	    		break;
	    	}
	    }
	    
	    @Override
	    public void onBackPressed() 
	    {
	    	// 이전 화면으로 못 돌아가게 막는다.( 아예 종료가 되도록한다 )
	    	// 일단은 돌아갈수 있게 테스트용으로 열어둔다. 
	    	/*HomeActivity parent = (HomeActivity) getParent();
			parent.onBackPressed();*/
	    	
	    	new AlertDialog.Builder(self)
			 .setTitle("종료 확인")
			 .setMessage("정말 종료 하겠습니까?") //줄였음
			 .setPositiveButton("예", new DialogInterface.OnClickListener() 
			 {
			     public void onClick(DialogInterface dialog, int whichButton)
			     {   
						moveTaskToBack(true);
						finish();
					    android.os.Process.killProcess(android.os.Process.myPid());
					    
					    ActivityManager am  = (ActivityManager)self.getSystemService(Activity.ACTIVITY_SERVICE);
			    	    am.restartPackage(self.getPackageName());
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
	    
	    public void Login()
	    {
	    	
			{
				final MyInfo myApp = (MyInfo) getApplication();
				HideKeyBoard();
				mProgress.show();
				Thread thread = new Thread(new Runnable()
				{

					public void run() 
					{
						// ID와 패스워드를 가져온다 
						EditText IDBOX = (EditText)findViewById(R.id.home_login_id_textbox);
						String ID = IDBOX.getText().toString();
						
						EditText Password = (EditText)findViewById(R.id.home_login_password_textbox);
						String pass = Password.getText().toString();

						Map<String, String> data = new HashMap  <String, String>();
						data.put("mb_password", pass );
						data.put("mb_id", ID);
						data.put("return_type", "json");
						
						String strJSON = myApp.PostCookieHTTPData("http://oppa.rcsoft.co.kr/api/gnu_login.php", data);
						
						try 
						{
							JSONObject json = new JSONObject(strJSON);
							if(json.getString("result").equals("ok"))
							{
								MyAccountData Mydata = myApp.GetAccountData();
								
								Mydata.ID = ID;
								Mydata.Password = pass;
								Mydata.Name = json.getString("mb_name");
								Mydata.Level = json.getInt("mb_level");
								// 자동 로그인도....
								CheckBox IDSave = (CheckBox)findViewById(R.id.home_login_id_save_check);
								CheckBox AutoLogin = (CheckBox)findViewById(R.id.home_login_auto_save_check);
								Mydata.IDSave = IDSave.isChecked();
								Mydata.AutoLogin = AutoLogin.isChecked();
								
								
								
					
								// 데이터 저장하기.
								myApp.SetAccountData(Mydata);
								myApp.SaveInfo();
								handler.sendEmptyMessage(2);
							}
							else 
							{
								// 에러 메세지를 전송한다. 
								handler.sendMessage(handler.obtainMessage(1,myApp.DecodeString(json.getString("result_text")) ));
								return ;
							}
						} catch (JSONException e) 
						{
							// TODO Auto-generated catch block
							handler.sendMessage(handler.obtainMessage(1,"로그인에 실패했습니다." ));
							e.printStackTrace();
						}
					}
				});
				thread.start();
			}
	    }
	    
	    public void HideKeyBoard()
	    {
	    	
			EditText IDBOX = (EditText)findViewById(R.id.home_login_id_textbox);
			EditText Password = (EditText)findViewById(R.id.home_login_password_textbox);

			
	    	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
	    	imm.hideSoftInputFromWindow(IDBOX.getWindowToken(),0);   // 키보드 내리기 
	    	imm.hideSoftInputFromWindow(Password.getWindowToken(),0);
	    }
	    
	    public void GetUserInfo()
	    {
			final MyInfo myApp = (MyInfo) getApplication();
			HideKeyBoard();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{


					Map<String, String> data = new HashMap  <String, String>();

					data.put("return_type", "json");
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getUserInfo.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							MyAccountData Mydata = myApp.GetAccountData();

							Mydata.ID = json.getString("mb_id");
							Mydata.NickName = json.getString("mb_nick");
							Mydata.Level = json.getInt("mb_level");

							Mydata.Point = json.getInt("mb_point");
							Mydata.Grade = json.getString("mb_grade");
							Mydata.Status = json.getString("mb_status");
							
							
				
							// 데이터 저장하기.
							myApp.SetAccountData(Mydata);
							myApp.SaveInfo();
							handler.sendEmptyMessage(0);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(1,myApp.DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler.sendMessage(handler.obtainMessage(1,"로그인에 실패했습니다." ));
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
					{
					
			           Intent intent;
			           intent = new Intent().setClass(self, Home.class);
			           startActivity( intent );  
					}   
					break;
				case 1:
					// 오류처리 
					self.ShowAlertDialLog( self ,"로그인 에러" , (String) msg.obj );
					break;
				case 2:
					GetUserInfo();
					break;
				default:

					break;
				}

			}
		};
}
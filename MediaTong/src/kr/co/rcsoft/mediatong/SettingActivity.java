package kr.co.rcsoft.mediatong;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.UISizeConverter;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingActivity extends BaseActivity implements OnClickListener{

	SettingActivity self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);  
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
        self = this;
        
        // 전체 레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.setting_main);
            _AppManager.GetUISizeConverter().ParentRelativeConvertLinearLayout(layout);
        }
        // 헤더레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.setting_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	
        	{
        		LinearLayout layout = (LinearLayout)findViewById(R.id.setting_row_1);
        		_AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
        		layout.setOnClickListener(this);
        	}
        	{
        		LinearLayout layout = (LinearLayout)findViewById(R.id.setting_row_2);
        		_AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
        		layout.setOnClickListener(this);
        	}
        	{
        		LinearLayout layout = (LinearLayout)findViewById(R.id.setting_row_3);
        		_AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
        		layout.setOnClickListener(this);
        	}
        	{
        		LinearLayout layout = (LinearLayout)findViewById(R.id.setting_row_4);
        		_AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
        		layout.setOnClickListener(this);
        	}
        	{
        		LinearLayout layout = (LinearLayout)findViewById(R.id.setting_row_5);
        		_AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
        		layout.setOnClickListener(this);
        	}
        	{
        		LinearLayout layout = (LinearLayout)findViewById(R.id.setting_row_6);
        		_AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
        		layout.setOnClickListener(this);
        	}
        	{
        		LinearLayout layout = (LinearLayout)findViewById(R.id.setting_row_7);
        		_AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
        		layout.setOnClickListener(this);
        	}
        	
        	{
        		LinearLayout layout = (LinearLayout)findViewById(R.id.setting_row_8);
        		_AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
        		layout.setOnClickListener(this);
        	}
        	
        	
        	{
        		ToggleButton btn1 = (ToggleButton) findViewById(R.id.setting_toggle1);
        		ToggleButton btn2 = (ToggleButton) findViewById(R.id.setting_toggle2);
        		btn1.setOnClickListener(this);
        		btn2.setOnClickListener(this);
        	}
        	
        	if ( _AppManager.m_LoginCheck == true )
    		{
        		GetMyjobAlarm();
        		//GetScrapAlarm();
    		}
    		else
    		{

    		}
        	
        }
        
        
        
        ImageBtnResize(R.id.setting_back );
        
    }
    
    public void GetMyjobAlarm()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getMyjobAlarm.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(1,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{

							 if( json.getInt("mb_myjob_alarm")== 0 )
							 {
								 handler.sendEmptyMessage(10);
							 }
							 else
							 {
								 handler.sendEmptyMessage(11);
							 }
							
							
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    }
    
    
    public void GetScrapAlarm()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getScrapAlarm.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(1,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{

							 if( json.getInt("mb_scrap_alarm")== 0 )
							 {
								 handler.sendEmptyMessage(20);
							 }
							 else
							 {
								 handler.sendEmptyMessage(21);
							 }
							
							
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    }
    
    
    public void SaveMyjobAlarm()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					ToggleButton btn1 = (ToggleButton) findViewById(R.id.setting_toggle1);
					
					if (btn1.isChecked() == false) 
					{
						data.put("mb_myjob_alarm", "0");
					}
					else
					{
						data.put("mb_myjob_alarm", "1");
					}
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_saveMyjobAlarm.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(1,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{

							handler.sendEmptyMessage(0);							
							
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    }
    
    
    public void SaveScrapAlarm()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					ToggleButton btn1 = (ToggleButton) findViewById(R.id.setting_toggle2);
					
					if (btn1.isChecked() == false) 
					{
						data.put("mb_scrap_alarm", "0");
					}
					else
					{
						data.put("mb_scrap_alarm", "1");
					}
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_saveScrapAlarm.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(1,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{

							handler.sendEmptyMessage(0);							
							
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    }
    
    
	final Handler handler = new Handler()
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
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			case 2:
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			case 10:
			{
				ToggleButton btn1 = (ToggleButton) findViewById(R.id.setting_toggle1);
    			btn1.setChecked(false);
    			GetScrapAlarm();
			}
				break;
			case 11:
			{
				ToggleButton btn1 = (ToggleButton) findViewById(R.id.setting_toggle1);
    			btn1.setChecked(true);
    			GetScrapAlarm();
			}
			
			case 20:
			{
				ToggleButton btn1 = (ToggleButton) findViewById(R.id.setting_toggle2);
    			btn1.setChecked(false);
			}
				break;
			case 21:
			{
				ToggleButton btn1 = (ToggleButton) findViewById(R.id.setting_toggle2);
    			btn1.setChecked(true);
			}
				break;
				
			}
		}
	};
	
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		
		AppManagement _AppManager = (AppManagement) getApplication();
		
		switch( arg0.getId() )
		{
		
		case R.id.setting_back:
			onBackPressed();
			
			break;
		case R.id.setting_toggle1:
		{
    		if ( _AppManager.m_LoginCheck == true )
    		{
    			SaveMyjobAlarm();
    		}
    		else
    		{
    			self.ShowAlertDialLog( self ,"로그인 에러" , "로그인시 사용할수 있습니다. " );
    			ToggleButton btn1 = (ToggleButton) findViewById(R.id.setting_toggle1);
    			btn1.setChecked(false);
    		}
		}
			break;
		case R.id.setting_toggle2:
		{
    		if ( _AppManager.m_LoginCheck == true )
    		{
    			SaveScrapAlarm();
    		}
    		else
    		{
    			self.ShowAlertDialLog( self ,"로그인 에러" , "로그인시 사용할수 있습니다. " );
    			ToggleButton btn1 = (ToggleButton) findViewById(R.id.setting_toggle2);
    			btn1.setChecked(false);
    		}
		}
			break;
		case R.id.setting_row_1:
		{
    		
    		if ( _AppManager.m_LoginCheck == true )
    		{
    			self.ShowAlertDialLog( self ,"로그인 중" , "현재 로그인 중입니다. " );
    		}
    		else
    		{
	    		Intent intent;
	            intent = new Intent().setClass(this, LoginActivity.class);
	            startActivity( intent );
    		}
		}
			break;
			
		case R.id.setting_row_2:
		{
    		// 공지사항
			Intent intent;
            intent = new Intent().setClass(this, NoticeActivity.class);
            startActivity( intent );
		}
			break;
		case R.id.setting_row_3:
		{
    		// PC버전 웹사이트
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(_AppManager.m_PCVerAddress)));
		}
			break;
		case R.id.setting_row_4:
		{
    		// 업데이트 사항
			Intent intent;
            intent = new Intent().setClass(this, UpdateActivity.class);
            startActivity( intent );
		}
			break;
		case R.id.setting_row_5:
		{
    		// 문의사항
			Intent intent;
            intent = new Intent().setClass(this, QNAActivity.class);
            startActivity( intent );
			
		}
			break;
		case R.id.setting_row_6:
		{
    		// FAQ
			Intent intent;
            intent = new Intent().setClass(this, FAQActivity.class);
            startActivity( intent );
		}
			break;
			
		}
		
	}

}

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;
import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;


public class SettingJoinActivity extends MappBaseActivity implements OnClickListener {

	private SettingJoinActivity self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_join_layout);  // 인트로 레이아웃 출력     

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
        ImageResize(R.id.join_desc_1);
        ImageResize(R.id.login_input_image);
        ImageResize(R.id.newpass_data);
        ImageResize(R.id.newpass_data2);
        
        ImageResize(R.id.nick_input_image);
        ImageResize(R.id.nick_data);
        ImageBtnResize(R.id.join_desc_2);
        ImageBtnResize(R.id.join_desc_3);
        ImageBtnResize(R.id.join_desc_4);
        
        ImageResize3(R.id.popup_1);
        ImageResize(R.id.popup_bar_1);
        ImageBtnResize(R.id.popup_exit_1);
        ImageResize(R.id.popup_title_1);
        ImageResize(R.id.popup_scrollview_1);
        ImageResize(R.id.popup_text_1);
        
        
        ImageResize3(R.id.popup_2);
        ImageResize(R.id.popup_bar_2);
        ImageBtnResize(R.id.popup_exit_2);
        ImageResize(R.id.popup_title_2);
        ImageResize(R.id.popup_scrollview_2);
        ImageResize(R.id.popup_text_2);
        
        
        ImageResize3(R.id.popup_3);
        ImageResize(R.id.popup_bar_3);
        ImageBtnResize(R.id.popup_exit_3);
        ImageResize(R.id.popup_title_3);
        ImageResize(R.id.popup_scrollview_3);
        ImageResize(R.id.popup_text_3);
        
        
        ImageResize(R.id.checkBox1);
        ImageResize(R.id.checkBox2);
        

        

        ImageBtnResize(R.id.login_join_btn);
        
        String token ="";

        BottomMenuDown(true);
        AfterCreate( 7 );
        
        ImageBtnEvent(R.id.title_icon , this);
        PopupClose();
    }
    
    

    @Override
    public void  onResume()
    {
    	super.onResume();
    	RefreshUI();
    }
    
    
    public void PopupClose()
    {
    	FrameLayout detailBar1 = (FrameLayout)findViewById(R.id.popup_1);
    	FrameLayout detailBar2 = (FrameLayout)findViewById(R.id.popup_2);
    	FrameLayout detailBar3 = (FrameLayout)findViewById(R.id.popup_3);
    	detailBar1.setVisibility(View.GONE);
    	detailBar2.setVisibility(View.GONE);
    	detailBar3.setVisibility(View.GONE);
    	
    }
    
    public void PopupOpen(int index)
    {
    	PopupClose();
    	FrameLayout detailBar1 = (FrameLayout)findViewById(R.id.popup_1);
    	FrameLayout detailBar2 = (FrameLayout)findViewById(R.id.popup_2);
    	FrameLayout detailBar3 = (FrameLayout)findViewById(R.id.popup_3);
    	switch( index )
    	{
    	case 0:
    		detailBar1.setVisibility(View.VISIBLE);
    		break;
    	case 1:
    		detailBar2.setVisibility(View.VISIBLE);
    		break;
    	case 2:
    		detailBar3.setVisibility(View.VISIBLE);
    		break;
    		
    	}
    	
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
		case R.id.join_desc_2:
			PopupOpen(0);
			break;
		case R.id.join_desc_3:
			PopupOpen(1);
			break;
		case R.id.join_desc_4:
			PopupOpen(2);
			break;
		case R.id.popup_exit_1:
		case R.id.popup_exit_2:
		case R.id.popup_exit_3:
			PopupClose();
			break;
			
		case R.id.login_join_btn:
		{
			if ( ((CheckBox)findViewById(R.id.checkBox1)).isChecked() == false||  ((CheckBox)findViewById(R.id.checkBox2)).isChecked() == false  )
			{
				self.ShowAlertDialLog( self ,"에러" , "약관에 동의를 해주세요." );
				return;
			}
			
			if ( ((EditText)findViewById(R.id.oldpass_data)).getText().toString().length() < 5  )
			{
				self.ShowAlertDialLog( self ,"에러" , " 이메일은  최소 5글자 이상 입력해야 됩니다. " );
				return;
			}
			
			if (false == ((EditText)findViewById(R.id.newpass_data)).getText().toString().equals(((EditText)findViewById(R.id.newpass_data2)).getText().toString()) )
			{
				self.ShowAlertDialLog( self ,"에러" , " 입력된 패스워드가 틀립니다. 확인해주세요.  " );
				return;
			}
			
			if ( ((EditText)findViewById(R.id.nick_data)).getText().toString().length() < 4  )
			{
				self.ShowAlertDialLog( self ,"에러" , " 닉네임은  최소 4글자 이상 입력해야 됩니다. " );
				return;
			}
			
			Join();
			
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
	
	
	public void Join()
	{
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				// ID와 패스워드를 가져온다 
				

				EditText id = (EditText)findViewById(R.id.oldpass_data);
				EditText pass = (EditText)findViewById(R.id.newpass_data);
				EditText nick = (EditText)findViewById(R.id.nick_data);
				Map<String, String> data = new HashMap  <String, String>();

				data.put("id", id.getText().toString());
				data.put("password", getMD5Hash(id.getText().toString()+ pass.getText().toString()));
				data.put("nick", nick.getText().toString());
				

				
				
				String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Signup", data);

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

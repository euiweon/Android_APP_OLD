package kr.co.rcsoft.mediatong;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;

public class UpdateActivity extends BaseActivity implements OnClickListener{


	String m_CurrVer = "";
	String m_NewVer = "";
	UpdateActivity self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_layout);  
        
        
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		{
			PackageInfo info = null;
			try {
				info = getPackageManager().getPackageInfo(getPackageName(), 0);
				m_CurrVer =info.versionName;
			} catch (NameNotFoundException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			}

			
		}
		
		{
	        {
	        	AppManagement _AppManager = (AppManagement) getApplication();
	        	FrameLayout layout = (FrameLayout)findViewById(R.id.update_head00);
	            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
	        }
	        {
	        	AppManagement _AppManager = (AppManagement) getApplication();
	        	FrameLayout layout = (FrameLayout)findViewById(R.id.update_head0);
	            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
	        }
	        {
	        	AppManagement _AppManager = (AppManagement) getApplication();
	        	FrameLayout layout = (FrameLayout)findViewById(R.id.update_head1);
	            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
	        }
	        {
	        	AppManagement _AppManager = (AppManagement) getApplication();
	        	FrameLayout layout = (FrameLayout)findViewById(R.id.update_head2);
	            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
	        }
	        
	        {
	        	AppManagement _AppManager = (AppManagement) getApplication();
	        	FrameLayout layout = (FrameLayout)findViewById(R.id.update_btn);
	            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
	            layout.setOnClickListener(this);
	        }
		}
		
		ImageBtnResize(R.id.update_back );
		
		GetVersion();
    }
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    
    public void GetVersion()
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
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getNewVersion.php", data);
					
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

							m_NewVer = _AppManager.GetHttpManager().DecodeString(json.getString("version2"));
							
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
			FrameLayout layout = (FrameLayout)findViewById(R.id.update_btn);
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:

			{
				((TextView)findViewById(R.id.update_now)).setText("현재버전 : " + m_CurrVer);
				((TextView)findViewById(R.id.update_new)).setText("최신버전 : " + m_NewVer);
				// 버전이 같을때
				if ( m_CurrVer.equals(m_NewVer) )
				{
					layout.setVisibility(View.GONE);
				}
				else
				{
					layout.setVisibility(View.VISIBLE);
				}
			}
				break;
			case 1:
				break;
			case 2:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				((TextView)findViewById(R.id.update_now)).setText("현재버전 : " + m_CurrVer);
				((TextView)findViewById(R.id.update_new)).setText("버전을 알수 없습니다.");
				layout.setVisibility(View.GONE);
				break;
				
			case 8 :
				break;	
			default:
  				mProgress.dismiss();
  				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
  				((TextView)findViewById(R.id.update_now)).setText("현재버전 : " + m_CurrVer);
  				((TextView)findViewById(R.id.update_new)).setText("버전을 알수 없습니다.");
  				layout.setVisibility(View.GONE);
				break;
			}
		}
	};
	public void onClick(View v) {
		// TODO 자동 생성된 메소드 스텁
		
		switch( v.getId() )
		{
		case R.id.update_btn:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://market.android.com/details?id=kr.co.rcsoft.mediatong")));
			break; 
		case R.id.update_back:
			onBackPressed();
			
			break;
		}
		
	}
		
}
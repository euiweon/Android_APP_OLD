package kr.co.rcsoft.mediatong;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.euiweonjeong.base.BaseActivity;


public class LoginActivity extends BaseActivity implements OnClickListener {

	LoginActivity self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);  // 인트로 레이아웃 출력  
        
        self = this;
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        // 텍스트뷰 하이퍼링크 
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	TextView  link = (TextView)findViewById(R.id.login_link);
        	link.setText(Html.fromHtml("회원가입과 아이디/비밀번호 찾기는 미디어통 웹사이트 (<a href=\"http://www.mediatong.com\" >www.mediatong.com</a> )에 접속하여 확인해 주시기 바랍니다. "));
        	link.setMovementMethod(LinkMovementMethod.getInstance());
        	_AppManager.GetUISizeConverter().ConvertFrameLayoutTextView(link);
        }
        
        // 레이아웃 리사이징
        // 헤더만 리사이징 하면 됨. 
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.login_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout =  (LinearLayout)findViewById(R.id.login_btn);
        	_AppManager.GetUISizeConverter().ParentFrameConvertLinearLayout(layout);
            layout.setOnClickListener(this);
        }
        
        
        ImageBtnResize(R.id.login_back );
        ImageBtnResize(R.id.login_recruit );
        
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText IDBOX = (EditText)findViewById(R.id.login_id);
        	_AppManager.GetUISizeConverter().ConvertFrameLayoutEditText(IDBOX);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText Password = (EditText)findViewById(R.id.login_pass);
        	_AppManager.GetUISizeConverter().ConvertFrameLayoutEditText(Password);
        }
        
        
        
       
        
    }
    
    
    
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    
    public void Login()
    {
    	{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					// ID와 패스워드를 가져온다 
					EditText IDBOX = (EditText)findViewById(R.id.login_id);
					String ID = IDBOX.getText().toString();
					
					EditText Password = (EditText)findViewById(R.id.login_pass);
					String pass = Password.getText().toString();

					
					Map<String, String> data = new HashMap  <String, String>();
					data.put("mb_pass", pass );
					data.put("mb_id", ID);
					
					data.put("return_type", "json");
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_memberLogin.php", data);
					
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
						handler.sendMessage(handler.obtainMessage(1,"로그인에 실패했습니다." ));
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
    }
    
    final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				// 홈 화면으로 간다. 
				Intent intent;
	            intent = new Intent().setClass(self, HomeActivity.class);
	            startActivity( intent ); 
	            
	            // 로그인 체크를 한다. 
	            AppManagement _AppManager = (AppManagement) getApplication();
	            _AppManager.m_LoginCheck = true;
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 2:

				break;
				
			case 3:
	
				break;
			default:
				break;
			}

		}
    	
	};
	
	

	
    
	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		switch(arg0.getId())
    	{
		case R.id.login_btn:
		{
			Login();
		}
			break;
		case R.id.login_back:
		{
			onBackPressed();
		}
			break;
	    	case R.id.login_recruit:
	    	{
	    		Intent intent;
	            intent = new Intent().setClass(this, Recruit2Activity.class);
	            startActivity( intent );
	    	}
			break;
			
    	}
	}
    



}
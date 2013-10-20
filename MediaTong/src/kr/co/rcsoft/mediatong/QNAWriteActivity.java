package kr.co.rcsoft.mediatong;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.euiweonjeong.base.BaseActivity;

public class QNAWriteActivity extends BaseActivity implements OnClickListener{

	QNAWriteActivity self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qna_write_layout);  // 인트로 레이아웃 출력      
        
        self = this;
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.qna_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText box = (EditText)findViewById(R.id.qna_write_title);
        	_AppManager.GetUISizeConverter().ConvertFrameLayoutEditText(box);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText box = (EditText)findViewById(R.id.qna_write_content);
        	_AppManager.GetUISizeConverter().ConvertFrameLayoutEditText(box);
        }
        
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	TextView  link = (TextView)findViewById(R.id.qna_text_title1);
        	_AppManager.GetUISizeConverter().ConvertFrameLayoutTextView(link);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	TextView  link = (TextView)findViewById(R.id.qna_text_title2);
        	_AppManager.GetUISizeConverter().ConvertFrameLayoutTextView(link);
        }
        
        
        
        ImageBtnResize(R.id.qna_back );
        ImageBtnResize(R.id.qna_write_btn );
        
       
        
    }
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }

	public void onClick(View v) {
		// TODO 자동 생성된 메소드 스텁
		
		switch(v.getId())
		{
		case R.id.qna_write_btn:
		{
			 QnaWrite();
		}
			break;
		case R.id.qna_back:
		{
			onBackPressed();
		}
			break;
		}
		
	}
	
	
	public void QnaWrite()
    {
    	{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					// ID와 패스워드를 가져온다 
					EditText IDBOX = (EditText)findViewById(R.id.qna_write_title);
					String ID = IDBOX.getText().toString();
					
					EditText Password = (EditText)findViewById(R.id.qna_write_content);
					String pass = Password.getText().toString();

					
					Map<String, String> data = new HashMap  <String, String>();
					data.put("bd_contents", pass );
					data.put("bd_title", ID);
					
					data.put("return_type", "json");
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_saveQna.php", data);
					
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
			default:
				break;
			}

		}
    	
	};
	
}
package oppa.rcsoft.co.kr;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

// Home 성인 인증 
public class login_adult extends BaseActivity  implements OnClickListener
{

	private login_adult  self = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_login_adult);
        self = this;
        
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("작업중입니다.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
        
        Button BackBtn = (Button)findViewById(R.id.home_login_adult_ok);
        BackBtn.setOnClickListener(this);
        Button ConFirmBtn = (Button)findViewById(R.id.home_login_adult_cancel);
        ConFirmBtn.setOnClickListener(this);
    }
    
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Log.e(TAG, "onKeyDown");
		return super.onKeyDown(keyCode, event);
	}
    

	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		switch(v.getId())
    	{
    	case R.id.home_login_adult_ok:
    	{
    		AdultCheck();
    	}
    		break;
    	case R.id.home_login_adult_cancel:
    	{
    		this.onBackPressed();
    	}
    		break;
    		
    	}
		
	}
	
    
    private void AdultCheck()
    {
		{
			final MyInfo myApp = (MyInfo) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					// ID와 패스워드를 가져온다 
					EditText Name = (EditText)findViewById(R.id.home_adult_name);
					String strname = Name.getText().toString();
					
					EditText jumin1 = (EditText)findViewById(R.id.home_adult_jumin1);
					String strjumin1 = jumin1.getText().toString();
					
					EditText jumin2 = (EditText)findViewById(R.id.home_adult_jumin2);
					String strjumin2 = jumin2.getText().toString();

					Map<String, String> data = new HashMap  <String, String>();
					// 테스트용...
					data.put("mb_name", strname );
					data.put("jumin1", strjumin1);
					data.put("jumin2", strjumin2);
					data.put("return_type", "json");
					
					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/oppa_adultCheck.php", data);
					
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
							handler.sendMessage(handler.obtainMessage(1,myApp.DecodeString(json.getString("result_text")) ));
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
    
    final Handler handler = new Handler( )
	{
    			
		public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				/*HomeActivity parent = (HomeActivity) self.getParent();
				 * 
				parent.setContentView(BaseActivityGroup.CHILD_FIVE);*/
			{
		           Intent intent;
		           intent = new Intent().setClass(self, login_account.class);
		           startActivity( intent ); 				
			}
  
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self.getParent() ,"인증 에러" , (String) msg.obj );
				break;
			default:

				break;
			}

		}
	};

}

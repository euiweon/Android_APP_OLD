package oppa.rcsoft.co.kr;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// 쪽지 보내기 
public class My_Memo_Send extends  BaseActivity  implements OnClickListener
{
    /** Called when the activity is first created. */
	
	public static My_Memo_Send  self;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.memo_send_write);          // 인트로 레이아웃 출력     
        self = this;
        
        Button WriteBtn = (Button)findViewById(R.id.memo_send_write_btn);
    	WriteBtn.setBackgroundResource(R.drawable.shop_detail_qna_write_btn);
    	WriteBtn.setOnClickListener(this);
    	
    	
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// Tab
		{
    		Button TabBTN2 = (Button)findViewById(R.id.mypage1_main_tab_btn_1);
    		TabBTN2.setOnClickListener(this);
    		Button TabBTN3 = (Button)findViewById(R.id.mypage1_main_tab_btn_2);
    		TabBTN3.setOnClickListener(this);
    		Button TabBTN4 = (Button)findViewById(R.id.mypage1_main_tab_btn_3);
    		TabBTN4.setOnClickListener(this);
    		Button TabBTN5 = (Button)findViewById(R.id.mypage1_main_tab_btn_4);
    		TabBTN5.setOnClickListener(this);
		}
		
		 RefreshUI();

    }

    
    
    public void RefreshUI()
    {
    	setContentView(R.layout.memo_send_write);        
         
           
        Button WriteBtn = (Button)findViewById(R.id.memo_send_write_btn);
       	WriteBtn.setBackgroundResource(R.drawable.shop_detail_qna_write_btn);
       	WriteBtn.setOnClickListener(this);
       	
       	
        mProgress = new ProgressDialog(this);
   		mProgress.setMessage("잠시만 기다려 주십시오.");
   		mProgress.setIndeterminate(true);
   		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
   		mProgress.setCancelable(false);
    }

    public void onClick(View v )
    {
    	switch(v.getId())
    	{
	    		
	    	case R.id.memo_send_write_btn:
	    	{
	    		SendWrite( );
	    	}
	    		break;
	    		
	    	case R.id.mypage1_main_tab_btn_2:

	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, Shop_MainList.class);
		        startActivity( intent );  
	    	}
	    		break;
	    		
	    	case R.id.mypage1_main_tab_btn_3:
	 
	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, ToyMainList.class);
		        startActivity( intent );   
	    	}
	    		break;
	    		
	    	case R.id.mypage1_main_tab_btn_4:
	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, Community_Main.class);
		        startActivity( intent );  
	    	}
	    		break;
	    		
	    	case R.id.mypage1_main_tab_btn_1:
	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, Home.class);
		        startActivity( intent );  
	    	}
	    		break;
	    		
    	}
    }
    final Handler handler = new Handler()
  	{
  		public void handleMessage(Message msg)
  		{
  			String message = null;
  			mProgress.dismiss();
  			switch(msg.what)
  			{
  			case 0:
  			{
  				{
  	  				EditText content = (EditText)findViewById(R.id.memo_send_text123);
  	  				content.setText("");
  	  				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
  					imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
  				}
  				MyInfo myApp = (MyInfo) getApplication();
  				// 정상적으로 메세지가 전송 되었을 경우 
  				onBackPressed();
  				message = "쪽지를 잘 보냈습니다.";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				
  				
  				// 키보드를 숨기고 텍스트를 초기화 한다. 

  				

  			}
  				break;
  			case 1:
  				message = "No data";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				break;
  			case 2:
  				self.ShowAlertDialLog( self,"메모전송실패" , (String) msg.obj );
  				break;
  			case 3:
  				message = "데이터가 없습니다";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				break;
  			default:
  				//	message = "실패하였습니다.";

  				break;
  			}

  		}
  	};
  	
    
    void SendWrite( )
    {
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();


					
					EditText content = (EditText)findViewById(R.id.memo_send_text123);
					String strcon = content.getText().toString();
					

					data.put("me_recv_mb_id", "admin");
					data.put("me_memo", strcon);
					data.put("return_type", "json");

					
					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/opps_memoSend.php", data);	
					if ( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"서버에 문제가 있으므로 나중에 다시 시도하세요." ));
						return;
					}
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{

							handler.sendEmptyMessage(0);
							
						}
						else if(json.getString("result").equals("error"))
						{
							handler.sendMessage(handler.obtainMessage(2,myApp.DecodeString(json.getString("result_text")) ));
						}
					}
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			});
			thread.start();
    	}
    		
    }
    
}

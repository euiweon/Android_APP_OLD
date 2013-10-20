package oppa.rcsoft.co.kr;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

// 리플 달기
public class Toy_Reply extends  BaseActivity implements OnClickListener
{
	static public Toy_Reply self;
	public boolean m_bReply;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        self = this;
        
        RefreshUI();
    }
    
    public void RefreshUI()
    {
        setContentView(R.layout.toy_detail_reply);
        Button WriteBtn = (Button)findViewById(R.id.bbs_detail_after_write_reply_btn);
        WriteBtn.setOnClickListener(this);
            
        mProgress = new ProgressDialog(this);
    	mProgress.setMessage("잠시만 기다려 주십시오.");
    	mProgress.setIndeterminate(true);
    	mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	mProgress.setCancelable(false);
    	
    	
    	MyInfo myApp = (MyInfo) getApplication();
    	
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// Tab
		{
			Button TabBTN2 = (Button)findViewById(R.id.commu_main_tab_btn_1);
			TabBTN2.setOnClickListener(this);
			Button TabBTN3 = (Button)findViewById(R.id.commu_main_tab_btn_2);
			TabBTN3.setOnClickListener(this);
			Button TabBTN4 = (Button)findViewById(R.id.commu_main_tab_btn_4);
			TabBTN4.setOnClickListener(this);
			Button TabBTN5 = (Button)findViewById(R.id.commu_main_tab_btn_5);
			TabBTN5.setOnClickListener(this);
		}
		

    		ImageView title = (ImageView)findViewById(R.id.bbs_detail_reply_title);
    		title.setImageResource(R.drawable.n_3_title3);
    		title.setMaxWidth(150);
    		
    		WriteBtn.setBackgroundResource(R.drawable.n_3_25_btn);
    		

    	
    	
    	


    }
    
  

    public void onClick(View v )
    {
    	switch(v.getId())
    	{

	    	case R.id.bbs_detail_after_write_reply_btn:
	    	{
	    		SendReply();
	    	}
	    		break;
	    	case R.id.commu_main_tab_btn_1:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, Home.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.commu_main_tab_btn_2:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.commu_main_tab_btn_4:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, Community_Main.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.commu_main_tab_btn_5:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, MyPage_Main.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	default:
	    		break;
    	}
    }
    final Handler handler = new Handler()
  	{
  		public void handleMessage(Message msg)
  		{
  			String message = null;
  			
  			MyInfo myApp = (MyInfo) getApplication();
  			mProgress.dismiss();
  			switch(msg.what)
  			{
  			case 0:
  			{
  				// 정상적으로 메세지가 전송 되었을 경우 
  				onBackPressed();
  				m_bReply = false;
  				message = "댓글이 잘 써졌습니다.";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				Toy_Detail_BBS.self.RefreshUI();
  				myApp.m_BBSReplyContent.isWriteType = false;
  			}
  				break;
  			case 1:
  				message = "No data";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				break;
  			case 2:
  				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
  				break;
  			case 3:
  				message = "데이터가 없습니다";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				break;
  			default:

  				break;
  			}

  		}
  	};
  	
    

    
    void SendReply()
    {
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

				
					EditText content = (EditText)findViewById(R.id.bbs_detail_epil_reply_content);
					String strcon = content.getText().toString();
					
					data.put("bo_table", "play");
					data.put("w","c");
					data.put("wr_content", strcon);
					data.put("return_type", "json");
					data.put("wr_id",myApp.m_BBSReplyContent.wr_id);
					
					if ( myApp.m_BBSReplyContent.isReply )
					{	
						data.put("comment_id",myApp.m_BBSReplyContent.re_id);
					}

					
					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/gnu_saveBoardComment.php" , data);	
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

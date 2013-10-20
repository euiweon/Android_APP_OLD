package oppa.rcsoft.co.kr;


import java.util.HashMap;
import java.util.Map;

import oppa.rcsoft.co.kr.Shop_MainFinderList.MoreBTNClick;

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
import android.widget.Toast;



// 후기 글쓰기 클래스 
public class Shop_Detail_After_Write extends BaseActivity implements OnClickListener
{
	static public Shop_Detail_After_Write self;
	public String m_Title;
	public String m_Content;
	
	public boolean m_bReply;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.shop_detail_epilogue);
    
        Button WriteBtn = (Button)findViewById(R.id.shop_detail_after_write_btn);
        WriteBtn.setOnClickListener(this);
        
        
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		m_bReply = false;
	
		{
			Button MainBtn1 = (Button)findViewById(R.id.shop_main_tab_btn_11);
	        MainBtn1.setOnClickListener(new MoreBTNClick());
	        Button MainBtn2 = (Button)findViewById(R.id.shop_main_tab_btn_31);
	        MainBtn2.setOnClickListener(new MoreBTNClick());
	        Button MainBtn3 = (Button)findViewById(R.id.shop_main_tab_btn_41);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
	        Button MainBtn4 = (Button)findViewById(R.id.shop_main_tab_btn_51);
	        MainBtn4.setOnClickListener(new MoreBTNClick());
	    }
		
		MyInfo myApp = (MyInfo) getApplication();
		m_bReply = myApp.m_BBSReplyContent.isWriteType; 
		RefreshUI();
	
    }
    
    public  class MoreBTNClick implements OnClickListener
    {

    	MyInfo myApp = (MyInfo) getApplication();
		public void onClick(View v )
        {
        	switch(v.getId())
        	{
        	case R.id.shop_main_tab_btn_11:
        	{
        		Intent intent;
    	        intent = new Intent().setClass(self, Home.class);
    	        startActivity( intent );  
        	}
        		break;
        		
        	case R.id.shop_main_tab_btn_31:
        	{
        		Intent intent;
    	        intent = new Intent().setClass(self, ToyMainList.class);
    	        startActivity( intent );   
        	}
        		break;
        		
        	case R.id.shop_main_tab_btn_41:
        	{
        		Intent intent;
    	        intent = new Intent().setClass(self, Community_Main.class);
    	        startActivity( intent );  
        	}
        		break;
        		
        	case R.id.shop_main_tab_btn_51:
        	{
        		Intent intent;
    	        intent = new Intent().setClass(self, MyPage_Main.class);
    	        startActivity( intent );  
        	}
        		break;
	
        	}
    
        }
    }
    
    
    
    public void RefreshUI()
    {
    	if ( m_bReply )
    	{
            setContentView(R.layout.shop_detail_epilogue_reply);
            
            Button WriteBtn = (Button)findViewById(R.id.shop_detail_after_write_reply_btn);
            WriteBtn.setOnClickListener(this);
            
            
            
            mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주십시오.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
    	}
    	else
    	{
            setContentView(R.layout.shop_detail_epilogue);
            
            Button WriteBtn = (Button)findViewById(R.id.shop_detail_after_write_btn);
            WriteBtn.setOnClickListener(this);
            
            
            
            mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주십시오.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
    	}
    }
    
    

    public void onClick(View v )
    {
    	switch(v.getId())
    	{

	    	case R.id.shop_detail_after_write_btn:
	    	{
	    		// 제목과 내용을 입력했는지 확인...
	    		
	    		// 데이터 전송후 뒤로 가기 누른다. 
	    		//onBackPressed();
	        	final CharSequence[] PhoneModels = {"최악이에요 ", "별로에요", "그냥 그래요", "좋아요", "아주 좋아요"};
    	        AlertDialog.Builder alt_bld = new AlertDialog.Builder(self);
    	        alt_bld.setTitle("평점주기");
    	        alt_bld.setSingleChoiceItems(PhoneModels, -1, new DialogInterface.OnClickListener() 
    	        {
    	            public void onClick(DialogInterface dialog, int item)
    	            {
    	            	dialog.dismiss();
    	            	// 메세지 전송 
    	            	SendWrite(item  );
    	            }
    	        });
    	        AlertDialog alert = alt_bld.create();
    	        alert.show();
	    		
	    	}
	    		break;
	    		
	    	case R.id.shop_detail_after_write_reply_btn:
	    	{
	    		SendReply();
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
  			
  			
  			mProgress.dismiss();
  			switch(msg.what)
  			{
  			case 0:
  			{
  				// 정상적으로 메세지가 전송 되었을 경우 
  				m_bReply = false;
  				message = "글이 잘 써졌습니다.";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				
  				Shop_Detail_After_BBS.self.RefreshUI();
  				
  				onBackPressed();
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
  				
  			case 7:
  			{
  				// 정상적으로 메세지가 전송 되었을 경우 
  				m_bReply = false;
  				message = "글이 잘 써졌습니다.";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				
  				Shop_Detail_After_ViewFlipper.self.RefreshUI();
  				
  				onBackPressed();
  			}
  			default:

  				break;
  			}

  		}
  	};
  	
    
    void SendWrite( int item )
    {
    	mProgress.show();
    	final Integer score = item+ 1;
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

									
					EditText content = (EditText)findViewById(R.id.shop_detail_epil_content);
					String strcon = content.getText().toString();
					
					// 후기니까 후기로 고정한다. 
					data.put("bo_table","hugi");
					data.put("wr_content", strcon);
					data.put("return_type", "json");
					data.put("sh_id", myApp.m_CurrShopInfo.ShopID);
					data.put("eval_point", score.toString() );
					

					
					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/gnu_saveBoardArticle.php", data);	
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

							handler.sendEmptyMessage(7);
							
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

				
					EditText content = (EditText)findViewById(R.id.shop_detail_epil_reply_content);
					String strcon = content.getText().toString();
					
					// 후기니까 후기로 고정한다. 
					data.put("bo_table","hugi");
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




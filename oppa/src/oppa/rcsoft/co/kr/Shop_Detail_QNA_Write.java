package oppa.rcsoft.co.kr;


import java.util.HashMap;
import java.util.Map;

import oppa.rcsoft.co.kr.Shop_MainFinderList.MoreBTNClick;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


// Shop 업소 QNA 글쓰기 

public class Shop_Detail_QNA_Write extends BaseActivity implements OnClickListener
{
	
	
	static public Shop_Detail_QNA_Write self;
	public String m_Title;
	public String m_Content;
	
	public boolean m_bReply;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {

        self = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_detail_qna_write);
    
        Button WriteBtn = (Button)findViewById(R.id.shop_detail_qna_write_btn);
        WriteBtn.setOnClickListener(this);
        

        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		m_bReply = false;
		
		RefreshUI();
		
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
    	MyInfo myApp = (MyInfo) getApplication();
    	if (myApp.m_BBSReplyContent.isReply )
    	{
    		ImageView image = (ImageView)findViewById(R.id.shop_qna_title2);
    		image.setImageResource(R.drawable.n_4_title6);
    		Button WriteBtn = (Button)findViewById(R.id.shop_detail_qna_write_btn);
    	   	WriteBtn.setBackgroundResource(R.drawable.n_4_17_btn);
    		WriteBtn.setOnClickListener(this);
    	}
    	else
    	{
    		ImageView image = (ImageView)findViewById(R.id.shop_qna_title2);
    		image.setImageResource(R.drawable.shop_detail_qna_title);
    		
    	   	Button WriteBtn = (Button)findViewById(R.id.shop_detail_qna_write_btn);
    	   	WriteBtn.setBackgroundResource(R.drawable.shop_detail_qna_write_btn);
    	   	WriteBtn.setOnClickListener(this);
    	}
    }
    
    


    public void onClick(View v )
    {
    	MyInfo myApp = (MyInfo) getApplication();
    	switch(v.getId())
    	{

	    	case R.id.shop_detail_qna_write_btn:
	    	{
	    		if (myApp.m_BBSReplyContent.isReply )
	    		{
	    			SendReply(  );
	    		}
	    		else
	    		{
	    			SendWrite(  );
	    		}
	    		
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
  				onBackPressed();
  				m_bReply = false;
  				message = "글이 잘 써졌습니다.";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				
  				Shop_Detail_QNA.self.RefreshUI();
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
  			default:

  				break;
  			}

  		}
  	};
  	
    
    void SendWrite(  )
    {
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					EditText content = (EditText)findViewById(R.id.shop_detail_qna_text123);
					String strcon = content.getText().toString();
 
					data.put("bo_table","ask");
					data.put("wr_subject", "androidAPP");
					data.put("wr_content", strcon);
					data.put("return_type", "json");
					data.put("sh_id", myApp.m_CurrShopInfo.ShopID);


					
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

				
					EditText content = (EditText)findViewById(R.id.shop_detail_qna_text123);
					String strcon = content.getText().toString();
					

					data.put("bo_table", "ask");
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


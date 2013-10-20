package oppa.rcsoft.co.kr;

import java.util.HashMap;
import java.util.Map;

import oppa.rcsoft.co.kr.Shop_MainFinderList.MoreBTNClick;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


// 업소 정보 관련 게시물 신고 하기 
public class Shop_Detail_Singo  extends BaseActivity implements OnClickListener
{
	static public Shop_Detail_Singo self;
	public String m_Title;
	public String m_Content;
	
	public boolean m_bReply;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.shop_detail_report);
    
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		m_bReply = false;
		
		
		Button itemAddSingo = (Button)findViewById(R.id.sendreportbtn);
		itemAddSingo.setVisibility(View.VISIBLE);
		
		// 클릭이벤트 추가     					
		itemAddSingo.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v )
	        {
	        	switch(v.getId())
	        	{
	        	case R.id.sendreportbtn:
	        	{
	        		SendReport();
					
	        	}
	        		break;
	        	}
	        }
		});
		
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
        setContentView(R.layout.shop_detail_report);
    
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		m_bReply = false;
		
		
		Button itemAddSingo = (Button)findViewById(R.id.sendreportbtn);
		itemAddSingo.setVisibility(View.VISIBLE);
		
		// 클릭이벤트 추가     					
		itemAddSingo.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v )
	        {
	        	switch(v.getId())
	        	{
	        	case R.id.sendreportbtn:
	        	{
	        		SendReport();
					
	        	}
	        		break;
	        	}
	        }
		});
    }
    
    
    public void onClick(View v )
    {
    	switch(v.getId())
    	{

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
  				// 여기는 하나만 있으니 수정 
  				m_bReply = false;
  				message = "신고 되었습니다.";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

  				//Shop_Detail_After_ViewFlipper.self.RefreshUI();
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
  				break;
  			default:

  				break;
  			}

  		}
  	};
  	
    
    void SendReport(  )
    {
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					data.put("bo_table", myApp.m_BBSSingo.bo_table);
					data.put("wr_id",  myApp.m_BBSSingo.wr_id);
					
					data.put("return_type", "json");
					
					String Reason = "";
					
					CheckBox box1 = (CheckBox)findViewById(R.id.report_check1);
					CheckBox box2 = (CheckBox)findViewById(R.id.report_check2);
					CheckBox box3 = (CheckBox)findViewById(R.id.report_check3);
					CheckBox box4 = (CheckBox)findViewById(R.id.report_check4);
					
					if ( box1.isChecked() )
					{
						Reason = " " + "불법 홍보 게시물" ;
					}
					

					if ( box2.isChecked() )
					{
						Reason += " " + "동일내용의 게시글 도배" ;
					}
					

					if ( box3.isChecked() )
					{
						Reason += " " + "음란성 게시물" ;
					}
					

					if ( box4.isChecked() )
					{
						EditText text1 = (EditText)findViewById(R.id.report_content);
						Reason += " " + text1.getText();
					}

					data.put("rp_reason", Reason);
					
					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/oppa_report.php", data);	
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

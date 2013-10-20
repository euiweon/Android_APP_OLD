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
import android.widget.Toast;



// 게시물 쓰기 ( 후기, 자유게시판 , 공지사항)
public class BBS_Writer  extends  BaseActivity implements OnClickListener
{
    /** Called when the activity is first created. */
	
	static public BBS_Writer self;
	public String m_Title;
	public String m_Content;
	
	public boolean m_BBSEpil;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        
        self = this;

		m_BBSEpil = false;
		

		
		RefreshUI();
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// Tab
		{
			Button TabBTN2 = (Button)findViewById(R.id.commu_main_tab_btn_1);
			TabBTN2.setOnClickListener(this);
			Button TabBTN3 = (Button)findViewById(R.id.commu_main_tab_btn_2);
			TabBTN3.setOnClickListener(this);
			Button TabBTN4 = (Button)findViewById(R.id.commu_main_tab_btn_3);
			TabBTN4.setOnClickListener(this);
			Button TabBTN5 = (Button)findViewById(R.id.commu_main_tab_btn_5);
			TabBTN5.setOnClickListener(this);
		}
		
    }
    
    public void RefreshUI()
    {
    	MyInfo myApp = (MyInfo) getApplication();
    	if  ( myApp.m_BBSID.bo_table == "qna" )
    	{
    		setContentView(R.layout.bbs_detail_qna_write);
            Button WriteBtn = (Button)findViewById(R.id.bbs_detail_qna_write_btn);
            WriteBtn.setOnClickListener(this);
    	   	WriteBtn.setBackgroundResource(R.drawable.shop_detail_qna_write_btn);
    	}
    	else
    	{
    		setContentView(R.layout.bbs_write);
    		if ( m_BBSEpil )
        	{
                setContentView(R.layout.bbs_write);
                
                Button WriteBtn = (Button)findViewById(R.id.bbs_wr_score);
                WriteBtn.setOnClickListener(this);
        	}
        	else
        	{
                setContentView(R.layout.bbs_de_write);
                Button WriteBtn = (Button)findViewById(R.id.bbs_de_save);
                WriteBtn.setOnClickListener(this);

        	}
    	}
    	
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

	    	case R.id.bbs_wr_score:
	    	{
	    		// 제목과 내용을 입력했는지 확인...
	    		
	    		// 데이터 전송후 뒤로 가기 누른다. 
	    		//onBackPressed();
	        	final CharSequence[] PhoneModels = {"평점 1.0 ", "평점 2.0", "평점 3.0", "평점 4.0", "평점 5.0"};
    	        AlertDialog.Builder alt_bld = new AlertDialog.Builder(self.getParent());
    	        alt_bld.setTitle("점수를 주세요");
    	        alt_bld.setSingleChoiceItems(PhoneModels, -1, new DialogInterface.OnClickListener() 
    	        {
    	            public void onClick(DialogInterface dialog, int item)
    	            {
    	            	dialog.dismiss();
    	            	// 메세지 전송 
    	            	SendHugi( item );
    	            	
    	            }
    	        });
    	        AlertDialog alert = alt_bld.create();
    	        alert.show();
	    		
	    	}
	    		break;
	    		
	    	case R.id.bbs_de_save:
	    	{
	    		SendWrite( );
	    	}
	    		break;
	    		
	    	case R.id.bbs_detail_qna_write_btn:
	    	{
	    		SendWrite2();
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
	    	case R.id.commu_main_tab_btn_3:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, ToyMainList.class);
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
  			mProgress.dismiss();
  			switch(msg.what)
  			{
  			case 0:
  			{
  				MyInfo myApp = (MyInfo) getApplication();
  				// 정상적으로 메세지가 전송 되었을 경우 
  				onBackPressed();
  				message = "글이 잘 써졌습니다.";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				
  				if  ( myApp.m_BBSID.bo_table == "qna" )
  				{
  					BBS_QNA_List.self.RefreshUI();
  				}
  				else
  				{
  					BBS_Main_List.self.RefreshUI();
  				}
  			}
  				break;
  			case 1:
  				message = "No data";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				break;
  			case 2:
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


					
					EditText content = (EditText)findViewById(R.id.bbs_de_content);
					String strcon = content.getText().toString();
					

					data.put("bo_table", myApp.m_BBSID.bo_table);
					
					data.put("wr_content", strcon);
					data.put("return_type", "json");

					
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
    
    
    void SendHugi( int item)
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


					
					EditText content = (EditText)findViewById(R.id.bbs_wr_content);
					String strcon = content.getText().toString();
					
					
					// 후기니까 후기로 고정한다. 
					data.put("bo_table","hugi");
					data.put("eval_point", score.toString() );
					data.put("wr_content", strcon);
					data.put("return_type", "json");
					
					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/gnu_saveBoardArticle.php" , data);	
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
    
    
    void SendWrite2(  )
    {
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					EditText content = (EditText)findViewById(R.id.bbs_detail_qna_text123);
					String strcon = content.getText().toString();
 
					data.put("bo_table","qna");
					data.put("wr_subject", "androidAPP");
					data.put("wr_content", strcon);
					data.put("return_type", "json");


					
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
}

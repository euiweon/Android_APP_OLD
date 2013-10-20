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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

// 커뮤니티 메인페이지 
public class Community_Main extends BaseActivity  implements OnClickListener
{    
	Community_Main self ;
	
	boolean 	noticeNew = false;
	boolean 	freeNew = false;
	boolean 	hugiNew = false;
	boolean 	qnaNew = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comm_main);          // 인트로 레이아웃 출력      
        
        self = this;
        
        
        Button BackBtn = (Button)findViewById(R.id.commu_main_back);
        BackBtn.setOnClickListener(this);
        Button NoticeBtn = (Button)findViewById(R.id.bbs_main_notice);
        NoticeBtn.setOnClickListener(this);
        Button EpilBtn = (Button)findViewById(R.id.bbs_main_epil);
        EpilBtn.setOnClickListener(this);
        Button FreeBtn = (Button)findViewById(R.id.bbs_main_free);
        FreeBtn.setOnClickListener(this);
        Button QNABtn = (Button)findViewById(R.id.bbs_main_qna);
        QNABtn.setOnClickListener(this);
        Button ContactBtn = (Button)findViewById(R.id.bbs_report);
        ContactBtn.setOnClickListener(this);
        
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
		
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		RefreshUI();
		

    }
    
    public void RefreshUI()
    {
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					data.put("bo_table", "notice" );
					data.put("return_type",  "json");

					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/gnu_checkBoardNew.php", data);	
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
							 if (json.getInt("new_cnt") > 0 )
							 {
								 noticeNew  = true;
							 }
							 else 
							 {
								 noticeNew = false;
							 }
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
    
    public void RefreshUI1()
    {
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					data.put("bo_table", "hugi" );
					data.put("return_type",  "json");

					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/gnu_checkBoardNew.php", data);	
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
							 if (json.getInt("new_cnt") > 0 )
							 {
								 hugiNew  = true;
							 }
							 else 
							 {
								 hugiNew = false;
							 }
							handler.sendEmptyMessage(1);
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
    
    
    public void RefreshUI2()
    {
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					data.put("bo_table", "free" );
					data.put("return_type",  "json");

					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/gnu_checkBoardNew.php", data);	
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
							 if (json.getInt("new_cnt") > 0 )
							 {
								 freeNew  = true;
							 }
							 else 
							 {
								 freeNew = false;
							 }
							handler.sendEmptyMessage(3);
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
    
    public void RefreshUI3()
    {
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					data.put("bo_table", "qna" );
					data.put("return_type",  "json");

					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/gnu_checkBoardNew.php", data);	
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
							 if (json.getInt("new_cnt") > 0 )
							 {
								 qnaNew  = true;
							 }
							 else 
							 {
								 qnaNew = false;
							 }
							handler.sendEmptyMessage(4);
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

    
    final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			String message = null;
			
			switch(msg.what)
			{
			case 0:
			{
				RefreshUI1();
			}
				
				break;
			case 1:
				RefreshUI2();
				break;
			case 2:
				mProgress.dismiss();
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			case 3:
				RefreshUI3();
				break;
			case 4 :
				mProgress.dismiss();
				
				{
					
					

					
					{
						TextView noticeText = (TextView) findViewById(R.id.commu_new1);
						if (noticeNew  )
						{
							noticeText.setVisibility(View.VISIBLE);
						}
						else
						{
							noticeText.setVisibility(View.GONE);
						}						
					}
					
					{
						TextView hugiText = (TextView) findViewById(R.id.commu_new2);
						if (hugiNew  )
						{
							hugiText.setVisibility(View.VISIBLE);
						}
						else
						{
							hugiText.setVisibility(View.GONE);
						}						
					}

					
					
					{
						TextView freeText = (TextView) findViewById(R.id.commu_new3);
						if (freeNew  )
						{
							freeText.setVisibility(View.VISIBLE);
						}
						else
						{
							freeText.setVisibility(View.GONE);
						}						
					}

					
					{
						TextView qnaText = (TextView) findViewById(R.id.commu_new4);
						if (qnaNew  )
						{
							qnaText.setVisibility(View.VISIBLE);
						}
						else
						{
							qnaText.setVisibility(View.GONE);
						}						
					}



				}
				break;

			default:
				//	message = "실패하였습니다.";
				mProgress.dismiss();
				break;
			}

		}
	};
    
    
    public void onBackPressed() 
    {
		
        Intent intent;
        intent = new Intent().setClass(self, Home.class);
        startActivity( intent );  
    }
    
    public void onClick(View v )
    {
		
    	MyInfo myApp = (MyInfo) getApplication();
    	switch(v.getId())
    	{
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
    		
    	case R.id.bbs_main_notice:
    	{
    		myApp.m_BBSID.bo_table= "notice";
    		
    		Intent intent;
	        intent = new Intent().setClass(self, BBS_Main_List.class);
	        startActivity( intent ); 
    		
    	}
		
			
    		break;
    	case R.id.bbs_main_epil:
    	{
    		myApp.m_BBSID.bo_table= "hugi";
    		Intent intent;
	        intent = new Intent().setClass(self, BBS_Main_List.class);
	        startActivity( intent ); 
    		
    	}
    		break;
    	case R.id.bbs_main_free:
    	{
    		myApp.m_BBSID.bo_table= "free";
    		Intent intent;
	        intent = new Intent().setClass(self, BBS_Main_List.class);
	        startActivity( intent ); 
			
    	}

    		break;
    	case R.id.bbs_main_qna:
    	{
    		myApp.m_BBSID.bo_table= "qna"; 		
    		Intent intent;
	        intent = new Intent().setClass(self, BBS_QNA_List.class);
	        startActivity( intent ); 
    	}

    		break;
    	case R.id.bbs_report:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, BBS_Company.class);
	        startActivity( intent ); 
    	}
    		break;

    	}
    }
     
}
package oppa.rcsoft.co.kr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;



// 커뮤니티 제휴 문의 .

public class BBS_Company extends  BaseActivity   implements OnClickListener
{
	String[] items2 = {"키스방",
			"마싸지", 
			"나이트", 
			"룸싸롱", 
			"기타업소", 
			};
	int CategoryCount = 0;
	
	ArrayList<String >	m_CategoryList2 = new ArrayList<String >();
	
	public static BBS_Company self;
	
	int m_Category;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        self = this;
        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.bbs_company, null); 
        this.setContentView(viewToLoad);   
        
        Button CreateBTN = (Button)findViewById(R.id.bbs_company_wr_score);
        CreateBTN.setOnClickListener(this);
        
        
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
		
		
		// 카테고리 리스트 가져오기 
		GetCateList();

        
    }
    
    void InputSpinnerItem2()
    {
        // 스피너 처리 
        Spinner spin = (Spinner)findViewById(R.id.bbs_company_shoptype);
        
        spin.setPrompt("업종을 선택하세요.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_CategoryList2);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{                
        		m_Category = position + 1;            
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}       
        });
    }
    

    public void onClick(View v )
    {
    	switch(v.getId())
    	{
	    	case R.id.bbs_company_wr_score:
	    	{
	    		SendWrite( );
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
    
	public void GetCateList()
	{
		// 카테고리 리스트를 1번부터 차례대로 가져온다. 
		m_CategoryList2.clear();
		CategoryCount  = 1;
		GetCateList2( CategoryCount );
    }
    
	public void GetCateList2( int kind)
    {
    	
		{
			final int kind1 = kind;
			final MyInfo myApp = (MyInfo) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					data.put("c1_idx", Integer.toString(kind1) );
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getCate2List.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							items2 = new String[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								items2[i] = myApp.DecodeString(Qlist.getJSONObject(i).getString("c2_name"));
							}
							handler.sendEmptyMessage(100);
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
						handler.sendMessage(handler.obtainMessage(1,"로그인에 실패했습니다." ));
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
    }
	
	
    // 제휴문의 버튼 클릭후 메세지 전송
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

					
					EditText content = (EditText)findViewById(R.id.bbs_company_wr_content);
					String strcon = content.getText().toString();
					
					EditText addr = (EditText)findViewById(R.id.bbs_company_addr);
					String straddr = addr.getText().toString();
					EditText tel = (EditText)findViewById(R.id.bbs_company_tel);
					String strtel = tel.getText().toString();
					
			 		Spinner spin = (Spinner)findViewById(R.id.bbs_company_shoptype);
					String loca = (String) spin.getSelectedItem();
					
					if ( straddr.equals(""))
					{
						self.ShowAlertDialLog( self ,"에러" , "주소가 기입되지 않았습니다" );
						return;
					}
					if ( strtel.equals(""))
					{
						self.ShowAlertDialLog( self ,"에러" , "전화번호가 기입되지 않았습니다");
						
						return;
					}
					if ( strcon.equals(""))
					{
						self.ShowAlertDialLog( self ,"에러" ,  "문의 내역이 기입되지 않았습니다"  );
						return;
					}
					
					
					data.put("wr_content", strcon);
					data.put("return_type", "json");
					data.put("wr_4", straddr);
					data.put("wr_3", strtel );
					data.put("wr_2", loca);
					data.put("bo_table", "ally");
					
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
    
    
    
    
    final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{

			String message = null;
			
			switch(msg.what)
			{
			case 0:
				// 전송 성공
				mProgress.dismiss();
				Toast.makeText(getApplicationContext(), "내역을 전송하였습니다.", Toast.LENGTH_LONG).show();
				
				{
					EditText content = (EditText)findViewById(R.id.bbs_company_wr_content);
					content.setText("");
					
					EditText addr = (EditText)findViewById(R.id.bbs_company_addr);
					addr.setText("");
					EditText tel = (EditText)findViewById(R.id.bbs_company_tel);
					tel.setText("");
				}
				onBackPressed();
				break;
			case 1:
				// 데이터가 없음 
				mProgress.dismiss();
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				// 에러 메세지 출력
				mProgress.dismiss();
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
				
			case 100:
				// 카테고리 리스트에 모두다 들어 갔는지 확인 하고 아니라면 다시한번 더 가져옴
			{
				CategoryCount++;
				
				for( int i = 0 ; i < items2.length; i++ )
				{
					m_CategoryList2.add(items2[i]);
				}
				
				if ( CategoryCount > 5  )
				{
					mProgress.dismiss();
					InputSpinnerItem2();
				}
				else
				{
					GetCateList2( CategoryCount );
				}
			}
			break;
			default:
				//	message = "실패하였습니다.";

				break;
			}

		}
	};
	
    
}

package oppa.rcsoft.co.kr;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

// 계정 생성화면 
public class login_account extends BaseActivity implements OnClickListener
{

	
	String[] items = {"내맘대로 설정",
			"생년월일(예) 81.01.14",
			"좋아하는 음식은?",
			"첫사랑의 이름은?",
			"어머니 성함은?",
			"현재 거주하는 동네이름은?"};
	
	static login_account  self = null;
	
	Boolean	m_bTerms;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.home_login_account_create_layout, null); 
        this.setContentView(viewToLoad);   
        
        self = this;
        
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("작업중입니다.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
        
        Button TermsBtn = (Button)findViewById(R.id.home_login_account_terms);
        TermsBtn.setOnClickListener(this);
        
        Button Overlap1Btn = (Button)findViewById(R.id.home_login_account_overlap1);
        Overlap1Btn.setOnClickListener(this); 
        Button Overlap2Btn = (Button)findViewById(R.id.home_login_account_overlap2);
        Overlap2Btn.setOnClickListener(this); 
        
        Button CreateBtn = (Button)findViewById(R.id.home_login_account_create);
        CreateBtn.setOnClickListener(this); 

        
        m_bTerms = false;
        
       // GetQuestionItem();
        
        InputSpinnerItem();
        

    }
    
    // 비밀번호 관련 질문을 가져온다... 
    
    public void GetQuestionItem()
    {
    	
		{
			final MyInfo myApp = (MyInfo) getApplication();
			
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					//data.put("return_type", "json");
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_passwordQuestion.php&return_type=json", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(1,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							items = new String[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								items[i] = myApp.DecodeString(Qlist.getJSONObject(i).getString("question"));
							}
							// 리스트를 갱신하라는 메세지를 넘겨준다. 
							handler.sendEmptyMessage(2);
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
    public void InputSpinnerItem()
    {
        // 스피너 처리 
        Spinner spin = (Spinner)findViewById(R.id.home_account_spinner);
        
        spin.setPrompt("질문을 선택하세요.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{                
        		            
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}       
        });
    }
    
	public Boolean isTerms()
	{
		return m_bTerms;
	}

	
    
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart()
	{
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onUserInteraction()
	{
		// TODO Auto-generated method stub
		super.onUserInteraction();
	}

	@Override
	protected void onUserLeaveHint()
	{
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
	}

	@Override
	protected String getClassName(Class<?> cls)
	{
		// TODO Auto-generated method stub
		return super.getClassName(cls);
	}


	

	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
    	{
    	case R.id.home_login_account_terms:
    	{
    		setContentView(R.layout.home_login_terms);
            Button BackBtn = (Button)findViewById(R.id.home_login_terms_back);
            BackBtn.setOnClickListener(this);
            m_bTerms = true;
            // 약관 페이지 가져오기 
            GetTermsData();
    	}
    		break;
    	case R.id.home_login_account_overlap1:
    		// 아이디 중복
    		CheckIDOverlap();
    		break;
    	case  R.id.home_login_account_overlap2:
    		// 닉네임 중복
    		CheckNickOverlap();
    		break;
    	case R.id.home_login_account_create:
    		// 회원 가입
    		CreateAccount();
    		break;
    	case R.id.home_login_terms_back:
    	{
    		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.home_login_account_create_layout, null); 
            this.setContentView(viewToLoad);   
            Button TermsBtn = (Button)findViewById(R.id.home_login_account_terms);
            TermsBtn.setOnClickListener(this);
            
            m_bTerms = false; 

    	}
    		break;
    		
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
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			case 2:
				// 비밀번호 질문 갱신
				InputSpinnerItem();
				break;
			case 3:
				// 약관 갱신
				RefrechTermsData( (String) msg.obj );
				
				break;
			case 4:
				self.ShowAlertDialLog( self ,"사용가능" , "사용할수 있습니다." );
				break;
			case 5:
				self.ShowAlertDialLog( self ,"가입완료" , "가입이 완료 되었습니다." );
	    		/*HomeActivity parent = (HomeActivity) getParent();
				parent.setContentView(BaseActivityGroup.CHILD_ONE);*/
				break;
			default:

				break;
			}

		}
	};
	
    @Override
    public void onBackPressed() 
    {
    	
    	// 약관 페이지 때문에 문제가 생김 
    	if ( !m_bTerms )
    	{
        	//HomeActivity parent = (HomeActivity) getParent();
    		super.onBackPressed();	
    	}
    	else
    	{
    		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.home_login_account_create_layout, null); 
            this.setContentView(viewToLoad);   
            Button TermsBtn = (Button)findViewById(R.id.home_login_account_terms);
            TermsBtn.setOnClickListener(this);
            
            Button Overlap1Btn = (Button)findViewById(R.id.home_login_account_overlap1);
            Overlap1Btn.setOnClickListener(this); 
            Button Overlap2Btn = (Button)findViewById(R.id.home_login_account_overlap2);
            Overlap2Btn.setOnClickListener(this); 
            Button CreateBtn = (Button)findViewById(R.id.home_login_account_create);
            CreateBtn.setOnClickListener(this); 
            
            
            InputSpinnerItem();
            
            
            m_bTerms = false; 
    	}
    }
    
    
    public void GetTermsData()
    {
    	{
			final MyInfo myApp = (MyInfo) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_getMemberTerms.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							handler.sendMessage(handler.obtainMessage(3,myApp.DecodeString(json.getString("terms")) ));
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(1,"접속 에러" ));
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
    
    public void RefrechTermsData(String data)
    {
    	TextView TermsText = (TextView)findViewById(R.id.home_terms_text);
    	TermsText.setText(data);
    	
    }
    
    public void CheckIDOverlap()
    {
    	{
			final MyInfo myApp = (MyInfo) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					EditText ID = (EditText)findViewById(R.id.home_account_id_text);
					String strID = ID.getText().toString();
					
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					data.put("mb_id", strID);
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_registIdCheck.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							handler.sendEmptyMessage(4);
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
    
    public void CheckNickOverlap()
    {
    	{
			final MyInfo myApp = (MyInfo) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					EditText Nick = (EditText)findViewById(R.id.home_account_nickname);
					String strNick = Nick.getText().toString();
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					data.put("mb_nick", strNick);
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_registNickCheck.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							handler.sendEmptyMessage(4);
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
    
    public void CreateAccount()
    {
    	{
			final MyInfo myApp = (MyInfo) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					EditText Nick = (EditText)findViewById(R.id.home_account_nickname);
					String strNick = Nick.getText().toString();
					EditText ID = (EditText)findViewById(R.id.home_account_id_text);
					String strID = ID.getText().toString();
					
					EditText Pass = (EditText)findViewById(R.id.home_account_pass1);
					String strPass = Pass.getText().toString();
					
					EditText Pass2 = (EditText)findViewById(R.id.home_account_pass2);
					String strPass2 = Pass2.getText().toString();
					
					if ( strPass.equals(""))
					{
						handler.sendMessage(handler.obtainMessage(1, "비밀번호를 확인해주세요." ));
						return;
					}
					if ( strPass.equals(strPass2) == false )
					{
						handler.sendMessage(handler.obtainMessage(1, "비밀번호를 확인해주세요." ));
						return;
					}
					CheckBox TermsCheck = (CheckBox)findViewById(R.id.home_login_account_terms_check);
					if( TermsCheck.isChecked() == false )
					{
						handler.sendMessage(handler.obtainMessage(1, "약관을 확인해주세요" ));
						return;
					}

					
					Spinner spin = (Spinner)findViewById(R.id.home_account_spinner);
					String strQ = (String) spin.getSelectedItem();
					
					EditText Pass3 = (EditText)findViewById(R.id.home_account_pass3);
					String strPass3 = Pass3.getText().toString();
					
					
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					data.put("mb_nick", strNick);
					data.put("mb_id", strID);
					data.put("mb_password", strPass);
					data.put("mb_password_q", strQ);
					data.put("mb_password_a", strPass3);
					

					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/gnu_registMember.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							handler.sendEmptyMessage(5);
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
}

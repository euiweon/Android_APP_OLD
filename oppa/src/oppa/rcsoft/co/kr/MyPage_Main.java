package oppa.rcsoft.co.kr;



import oppa.rcsoft.co.kr.MyInfo.MyAccountData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;



// Mypage 메인화면 
public class MyPage_Main extends BaseActivity implements OnClickListener
{    
	MyPage_Main self;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.introlayout);          // 인트로 레이아웃 출력      
        
        RefreshUI();
        self = this;
    }
     
    
    public void onBackPressed() 
    {
		
        Intent intent;
        intent = new Intent().setClass(self, Home.class);
        startActivity( intent );  
    }
    
    
    public void RefreshUI()
    {
    	MyInfo myApp = (MyInfo) getApplication();
        MyAccountData Mydata = myApp.GetAccountData();
        
        if ( Mydata.Level == 3 )
        {
        	setContentView(R.layout.mypage_main_2);
        	
     		///////////////////////////////////////////////////////////////////////////////////////////////////
    		// Tab
    		{
	    		Button TabBTN2 = (Button)findViewById(R.id.mypage2_main_tab_btn_1);
	    		TabBTN2.setOnClickListener(this);
	    		Button TabBTN3 = (Button)findViewById(R.id.mypage2_main_tab_btn_2);
	    		TabBTN3.setOnClickListener(this);
	    		Button TabBTN4 = (Button)findViewById(R.id.mypage2_main_tab_btn_3);
	    		TabBTN4.setOnClickListener(this);
	    		Button TabBTN5 = (Button)findViewById(R.id.mypage2_main_tab_btn_4);
	    		TabBTN5.setOnClickListener(this);
    		}
        	
            {
                Button MemoBtn = (Button)findViewById(R.id.mypage_main_memo_btn2);
                MemoBtn.setOnClickListener(this);
            }
            
            
            // 정보수정
            {
                Button infoBtn = (Button)findViewById(R.id.mypage_main_info_btn2);
                infoBtn.setOnClickListener(this);
            }
            
            // 프로필수정
            {
                Button proBtn = (Button)findViewById(R.id.mypage_main_profile_btn2);
                proBtn.setOnClickListener(this);
                proBtn.setVisibility(View.VISIBLE);
            }
            
            // 운영자에게 쪽지 보내기 
            {
                Button infoBtn = (Button)findViewById(R.id.mypage_main_admin_btn2);
                infoBtn.setOnClickListener(this);
            	
            }
            
            
           // 자동로그인
            
            {
            	final RadioGroup Autologin = (RadioGroup)findViewById(R.id.my_autologin_2);
            	Autologin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() 
            	{
            		public void onCheckedChanged(RadioGroup group, int checkedId) 
            		{
            	    // TODO Auto-generated method stub
            			if(checkedId != -1)
            		    {
            				RadioButton rb = (RadioButton)findViewById(checkedId);
            				if(rb != null)
            				{
            		        	MyInfo myApp = (MyInfo) getApplication();
            		        	MyAccountData Mydata = myApp.GetAccountData();
            		        	int temp =  Autologin.getCheckedRadioButtonId();
            		        	
            		        	if (  Autologin.getCheckedRadioButtonId() == R.id.my_autologin_21 )
            		        	{
            		        		Mydata.AutoLogin = true;
            		        	}
            		        	else
            		        	{
            		        		Mydata.AutoLogin = false;
            		        	}
            		        	
    							myApp.SetAccountData(Mydata);
    							myApp.SaveInfo();
            				}
            		    }
            		}
            	});

            	
            	if ( Mydata.AutoLogin  )
            	{
            		Autologin.check(R.id.my_autologin_21);
            	}
            	else
            	{
            		Autologin.check(R.id.my_autologin_22);
            	}
            }
        }
        else if ( Mydata.Level == 5 || Mydata.Level == 10 )
        {
        	setContentView(R.layout.mypage_main_2);
        	
     		///////////////////////////////////////////////////////////////////////////////////////////////////
    		// Tab
    		{
	    		Button TabBTN2 = (Button)findViewById(R.id.mypage2_main_tab_btn_1);
	    		TabBTN2.setOnClickListener(this);
	    		Button TabBTN3 = (Button)findViewById(R.id.mypage2_main_tab_btn_2);
	    		TabBTN3.setOnClickListener(this);
	    		Button TabBTN4 = (Button)findViewById(R.id.mypage2_main_tab_btn_3);
	    		TabBTN4.setOnClickListener(this);
	    		Button TabBTN5 = (Button)findViewById(R.id.mypage2_main_tab_btn_4);
	    		TabBTN5.setOnClickListener(this);
    		}
        	
            {
                Button MemoBtn = (Button)findViewById(R.id.mypage_main_memo_btn2);
                MemoBtn.setOnClickListener(this);
            }
            
            
            // 정보수정
            {
                Button infoBtn = (Button)findViewById(R.id.mypage_main_info_btn2);
                infoBtn.setOnClickListener(this);
            }
            
            // 프로필수정
            {
                Button proBtn = (Button)findViewById(R.id.mypage_main_profile_btn2);
                proBtn.setOnClickListener(this);
                proBtn.setVisibility(View.GONE);
            }
            
            // 운영자에게 쪽지 보내기 
            {
                Button infoBtn = (Button)findViewById(R.id.mypage_main_admin_btn2);
                infoBtn.setOnClickListener(this);
            	
            }
            
            
           // 자동로그인
            
            {
            	final RadioGroup Autologin = (RadioGroup)findViewById(R.id.my_autologin_2);
            	Autologin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() 
            	{
            		public void onCheckedChanged(RadioGroup group, int checkedId) 
            		{
            	    // TODO Auto-generated method stub
            			if(checkedId != -1)
            		    {
            				RadioButton rb = (RadioButton)findViewById(checkedId);
            				if(rb != null)
            				{
            		        	MyInfo myApp = (MyInfo) getApplication();
            		        	MyAccountData Mydata = myApp.GetAccountData();
            		        	int temp =  Autologin.getCheckedRadioButtonId();
            		        	
            		        	if (  Autologin.getCheckedRadioButtonId() == R.id.my_autologin_21 )
            		        	{
            		        		Mydata.AutoLogin = true;
            		        	}
            		        	else
            		        	{
            		        		Mydata.AutoLogin = false;
            		        	}
            		        	
    							myApp.SetAccountData(Mydata);
    							myApp.SaveInfo();
            				}
            		    }
            		}
            	});

            	
            	if ( Mydata.AutoLogin  )
            	{
            		Autologin.check(R.id.my_autologin_21);
            	}
            	else
            	{
            		Autologin.check(R.id.my_autologin_22);
            	}
            }
        }
        else
        {
            setContentView(R.layout.mypage_main_1);
   
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

            // 쪽지함
            {
                Button MemoBtn = (Button)findViewById(R.id.mypage_main_memo_btn);
                MemoBtn.setOnClickListener(this);
            }
            
            // 찜 업소
            // 찜 인물
            {
                Button ZZimShopBtn = (Button)findViewById(R.id.myinfo_shop_1);
                ZZimShopBtn.setOnClickListener(this);
            }
            {
                Button ZZimSisterBtn = (Button)findViewById(R.id.myinfo_sister_1);
                ZZimSisterBtn.setOnClickListener(this);
            }
            
            // 등급안내
            
            {
                Button GredeBtn = (Button)findViewById(R.id.my_main_grade);
                GredeBtn.setOnClickListener(this);

            }
            
            
            // 정보수정
            {
                Button infoBtn = (Button)findViewById(R.id.my_myinfo_rewrite123);
                infoBtn.setOnClickListener(this);
            }
            
            // 자동로그인
            
            {
            	final RadioGroup Autologin = (RadioGroup)findViewById(R.id.my_autologin);
            	Autologin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() 
            	{
            		public void onCheckedChanged(RadioGroup group, int checkedId) 
            		{
            	    // TODO Auto-generated method stub
            			if(checkedId != -1)
            		    {
            				RadioButton rb = (RadioButton)findViewById(checkedId);
            				if(rb != null)
            				{
            		        	MyInfo myApp = (MyInfo) getApplication();
            		        	MyAccountData Mydata = myApp.GetAccountData();
            		        	int temp =  Autologin.getCheckedRadioButtonId();
            		        	
            		        	if (  Autologin.getCheckedRadioButtonId() == R.id.autologin1 )
            		        	{
            		        		Mydata.AutoLogin = true;
            		        	}
            		        	else
            		        	{
            		        		Mydata.AutoLogin = false;
            		        	}
            		        	
    							myApp.SetAccountData(Mydata);
    							myApp.SaveInfo();
            				}
            		    }
            		}
            	});

            	
            	if ( Mydata.AutoLogin  )
            	{
            		Autologin.check(R.id.autologin1);
            	}
            	else
            	{
            		Autologin.check(R.id.autologin);
            	}
            }
            
            // 운영자에게 쪽지 보내기 
            {
                Button infoBtn = (Button)findViewById(R.id.my_memo_send_btn);
                infoBtn.setOnClickListener(this);
            	
            }
        }
    }

	public void onClick(View v )
    {
		
    	switch(v.getId())
    	{
    	case R.id.mypage_main_memo_btn:
    	case R.id.mypage_main_memo_btn2:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, My_Memo_Collect.class);
	        startActivity( intent );  
    		
    		
    	}
    		break;
    	case R.id.my_main_grade:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, My_Grade.class);
	        startActivity( intent );  
    		
    	}
    		break;
    		
    	case R.id.my_myinfo_rewrite123:
    	case R.id.mypage_main_info_btn2:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, My_Info_ReWrite.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.myinfo_shop_1:
    	{

    		Intent intent;
	        intent = new Intent().setClass(self, My_ZZimShop.class);
	        startActivity( intent );  
	        
    		 MyInfo myApp = (MyInfo) getApplication();
    		 myApp.m_ZZime.Type = "ZZIM";
    		 break;
    	}
    	
    	case R.id.myinfo_sister_1:
    	{

    		Intent intent;
	        intent = new Intent().setClass(self, My_ZZimSister.class);
	        startActivity( intent );  
	        
    		 MyInfo myApp = (MyInfo) getApplication();
    		 myApp.m_ZZime.Type = "ZZIM";
    	}
    		break;
    		
    	case R.id.mypage_main_profile_btn2:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, My_Profile.class);
	        startActivity( intent );  
    		
    	}
    		break;
    		
    	case R.id.my_memo_send_btn:    
    	case R.id.mypage_main_admin_btn2:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, My_Memo_Send.class);
	        startActivity( intent );  
    	}
    		break;
    		
    		
    	case R.id.mypage1_main_tab_btn_2:
    	case R.id.mypage2_main_tab_btn_2:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Shop_MainList.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.mypage1_main_tab_btn_3:
    	case R.id.mypage2_main_tab_btn_3:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, ToyMainList.class);
	        startActivity( intent );   
    	}
    		break;
    		
    	case R.id.mypage1_main_tab_btn_4:
    	case R.id.mypage2_main_tab_btn_4:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Community_Main.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.mypage1_main_tab_btn_1:
    	case R.id.mypage2_main_tab_btn_1:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Home.class);
	        startActivity( intent );  
    	}
    		break;

    	}
    }
}
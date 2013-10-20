package kr.co.rcsoft.mediatong;



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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;


public class Recruit2Activity extends BaseActivity implements OnClickListener{

	
	ArrayList< LinearLayout> ButtonList;
	ArrayList< TextView> TextList;
	Recruit2Activity  self;
	
	RecruitDataArea [] listItem;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruit_layout2);  
        
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        self = this;
        
        ButtonList = new ArrayList< LinearLayout>();
        TextList = new ArrayList< TextView>();
        // 전체 레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_main);
            _AppManager.GetUISizeConverter().ParentRelativeConvertLinearLayout(layout);
        }
        // 헤더레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.recruit_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }

        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_btn_1);
            _AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
            
            layout.setVisibility(View.GONE);
            ButtonList.add(layout);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_btn_2);
            _AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
            
            layout.setVisibility(View.GONE);
            ButtonList.add(layout);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_btn_3);
            _AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
            layout.setVisibility(View.GONE);
            ButtonList.add(layout);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_btn_4);
            _AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
            layout.setVisibility(View.GONE);
            ButtonList.add(layout);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_btn_5);
            _AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
            layout.setVisibility(View.GONE);
            ButtonList.add(layout);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_btn_6);
            _AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
            layout.setVisibility(View.GONE);
            ButtonList.add(layout);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_btn_7);
            _AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
            layout.setVisibility(View.GONE);
            ButtonList.add(layout);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_btn_8);
            _AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
            layout.setVisibility(View.GONE);
            ButtonList.add(layout);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_btn_9);
            _AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
            layout.setVisibility(View.GONE);
            ButtonList.add(layout);
        }

        TextList.add((TextView)findViewById(R.id.recruit_btn_1_text));
        TextList.add((TextView)findViewById(R.id.recruit_btn_2_text));
        TextList.add((TextView)findViewById(R.id.recruit_btn_3_text));
        TextList.add((TextView)findViewById(R.id.recruit_btn_4_text));
        TextList.add((TextView)findViewById(R.id.recruit_btn_5_text));
        TextList.add((TextView)findViewById(R.id.recruit_btn_6_text));
        TextList.add((TextView)findViewById(R.id.recruit_btn_7_text));
        TextList.add((TextView)findViewById(R.id.recruit_btn_8_text));
        TextList.add((TextView)findViewById(R.id.recruit_btn_9_text));
        
        
        
        ImageBtnResize(R.id.recruit_back );
        ImageBtnResize(R.id.recruit_search );
        
        ImageBtnResize2( R.id.recruit_btn_1_1);
        ImageBtnResize2( R.id.recruit_btn_2_1);
        ImageBtnResize2( R.id.recruit_btn_3_1);
        ImageBtnResize2( R.id.recruit_btn_4_1);
        ImageBtnResize2( R.id.recruit_btn_5_1);
        ImageBtnResize2( R.id.recruit_btn_6_1);
        ImageBtnResize2( R.id.recruit_btn_7_1);
        ImageBtnResize2( R.id.recruit_btn_8_1);
        ImageBtnResize2( R.id.recruit_btn_9_1);
        
        SetBtnEvent(R.id.recruit_bottom_home);
        SetBtnEvent(R.id.recruit_bottom_myjob);
        SetBtnEvent(R.id.recruit_bottom_scrap);
        SetBtnEvent(R.id.recruit_bottom_setting);
        
        
        GetList();
   
    }
    
	public void SetBtnEvent( int id )
	{
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
	}
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    public void ImageBtnResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    
    public void GetList(  )
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getUpjong1Data.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							listItem = new RecruitDataArea[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								listItem[i] = new RecruitDataArea();
								listItem[i].Area = _AppManager.GetHttpManager().DecodeString(Qlist.getJSONObject(i).getString("u1_name"));
								listItem[i].Index = Integer.parseInt(Qlist.getJSONObject(i).getString("u1_idx"));
							}
							// 스피너를 갱신하라는 메세지를 넘겨준다. 
							handler.sendEmptyMessage(0);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
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
    
    
    final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			String message = null;
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				
			// 리스트 처리
			// 추후 리스트뷰 형태로 수정해야함. 
			// 리스트 뷰 형태로 수정하지 못한것은 가변사이즈에 대한 고려 때문...
			{
				if ( listItem != null )
				{
					for ( int i = 0 ; i < 9 ; i ++ )
					{
						ButtonList.get(i).setVisibility(View.GONE);
					}
					for (int i = 0 ; i < listItem.length ; i++ )
					{
						ButtonList.get(i).setVisibility(View.VISIBLE);
						TextList.get(i).setText(listItem[i].Area );
					}
				}
				
				
			}
				
				
				break;
			case 1:
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			}
		}
	};
    
    


	public void onClick(View arg0) 
	{
		// TODO 자동 생성된 메소드 스텁
		
		AppManagement _AppManager = (AppManagement) getApplication();
		switch(arg0.getId())
    	{
			case R.id.recruit_back:
			{
				onBackPressed();
			}
				break;
			case R.id.recruit_search:
			{
				Intent intent;
	            intent = new Intent().setClass(this, RecruitFindActivity.class);
	            startActivity( intent );
			}
				break;
				
			case R.id.recruit_btn_1_1:
			{
				
				_AppManager.m_UpjongIndex = listItem[0].Index;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_btn_2_1:
			{
				_AppManager.m_UpjongIndex = listItem[1].Index;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_btn_3_1:
			{
				_AppManager.m_UpjongIndex = listItem[2].Index;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_btn_4_1:
			{
				_AppManager.m_UpjongIndex = listItem[3].Index;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_btn_5_1:
			{
				_AppManager.m_UpjongIndex = listItem[4].Index;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_btn_6_1:
			{
				_AppManager.m_UpjongIndex = listItem[5].Index;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_btn_7_1:
			{
				_AppManager.m_UpjongIndex = listItem[6].Index;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_btn_8_1:
			{
				_AppManager.m_UpjongIndex = listItem[7].Index;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_btn_9_1:
			{
				_AppManager.m_UpjongIndex = listItem[8].Index;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;

				

				
			case R.id.recruit_bottom_home:
			{
				Intent intent;
	            intent = new Intent().setClass(self, HomeActivity.class);
	            startActivity( intent ); 
			}
				break;
			case R.id.recruit_bottom_myjob:
			{
				
	    		if ( _AppManager.m_LoginCheck == true )
	    		{
	   			
	    			Intent intent;
	                intent = new Intent().setClass(self, MyJobListActivity.class);
	                startActivity( intent );

	    		}
	    		else
	    		{
	    			self.ShowAlertDialLog( self ,"로그인 에러" , "로그인을 해야 이 메뉴를 사용할수 있습니다." );
	    		}
			}
				break;
			case R.id.recruit_bottom_scrap:
			{
	    		
	    		if ( _AppManager.m_LoginCheck == true )
	    		{
	   			
	    			Intent intent;
	                intent = new Intent().setClass(self, ScrapMainActivity.class);
	                startActivity( intent );

	    		}
	    		else
	    		{
	    			self.ShowAlertDialLog( self ,"로그인 에러" , "로그인을 해야 이 메뉴를 사용할수 있습니다." );
	    		}
			}
				break;
			case R.id.recruit_bottom_setting:
			{
				Intent intent;
	            intent = new Intent().setClass(self, SettingActivity.class);
	            startActivity( intent ); 
			}
				break;
				
    	}
	}
}
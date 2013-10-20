package kr.co.rcsoft.mediatong;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.euiweonjeong.base.BaseActivity;


public class RecruitActivity extends BaseActivity implements OnClickListener{

	private Handler handler = new Handler();
	
	RecruitActivity  self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruit_layout);  
        
        self = this;
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
        ImageBtnResize(R.id.recruit_back );
        ImageBtnResize(R.id.recruit_search );
        
        ImageBtnResize(R.id.recruit_broad );
        ImageBtnResize(R.id.recruit_pro );
        ImageBtnResize(R.id.recruit_paper );
        ImageBtnResize(R.id.recruit_movie );
        ImageBtnResize(R.id.recruit_ad );
        ImageBtnResize(R.id.recruit_enter );
        ImageBtnResize(R.id.recruit_create );
        ImageBtnResize(R.id.recruit_it );
        ImageBtnResize(R.id.recruit_game );
        ImageBtnResize(R.id.recruit_edu );
        SetBtnEvent(R.id.recruit_bottom_home);
        SetBtnEvent(R.id.recruit_bottom_myjob);
        SetBtnEvent(R.id.recruit_bottom_scrap);
        SetBtnEvent(R.id.recruit_bottom_setting);
        
        
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
			case R.id.recruit_broad:
			{
				
				_AppManager.m_UpjongIndex = 1;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_pro:
			{
				_AppManager.m_UpjongIndex = 3;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_paper:
			{
			}
				break;
			case R.id.recruit_movie:
			{
				_AppManager.m_UpjongIndex = 4;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_ad:
			{
			}
				break;
			case R.id.recruit_enter:
			{
				_AppManager.m_UpjongIndex = 2;
				Intent intent;
	            intent = new Intent().setClass(this, RecruitDetailActivity.class);
	            startActivity( intent );
			}
				break;
			case R.id.recruit_create:
			{
			}
				break;
			case R.id.recruit_it:
			{
			}
				break;
			case R.id.recruit_game:
			{
			}
				break;
			case R.id.recruit_edu:
			{
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
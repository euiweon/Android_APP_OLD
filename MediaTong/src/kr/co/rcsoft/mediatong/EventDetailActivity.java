package kr.co.rcsoft.mediatong;

import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.ImageViewDoubleTab;
import com.euiweonjeong.base.ImageViewDoubleTabListener;

public class EventDetailActivity extends BaseActivity  implements OnClickListener , ImageViewDoubleTabListener{


	EventDetailActivity  self;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_layout);  
       
        self = this;
        // 전체 레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.event_main);
            _AppManager.GetUISizeConverter().ParentRelativeConvertLinearLayout(layout);
        }
        // 헤더레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.event_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        {
        	ImageViewDoubleTab view = (ImageViewDoubleTab)findViewById(R.id.event_now);
        	
        	view.SetDoubleTabListener(this);
        }
        
        
        ImageBtnResize(R.id.event_back );
        ImageBtnResize(R.id.event_now );
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	ImageView imageview = (ImageView)findViewById(R.id.event_now);
        	
        	if ( _AppManager.m_Eventdata.bd_file != null )
        	{
        		imageview.setImageBitmap(_AppManager.m_Eventdata.bd_file);
        	}
        	
        }
        
        SetBtnEvent(R.id.event_bottom_home);
        SetBtnEvent(R.id.event_bottom_myjob);
        SetBtnEvent(R.id.event_bottom_scrap);
        SetBtnEvent(R.id.event_bottom_setting);
        
        
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


	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		switch( arg0.getId())
		{
		case R.id.event_back:
			onBackPressed();
			break;
		case R.id.event_bottom_home:
		{
			Intent intent;
            intent = new Intent().setClass(self, HomeActivity.class);
            startActivity( intent ); 
		}
			break;
		case R.id.event_bottom_myjob:
		{
			final AppManagement _AppManager = (AppManagement) getApplication();
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
		case R.id.event_bottom_scrap:
		{
    		final AppManagement _AppManager = (AppManagement) getApplication();
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
		case R.id.event_bottom_setting:
		{
			Intent intent;
            intent = new Intent().setClass(self, SettingActivity.class);
            startActivity( intent ); 
		}
			break;
			
		}
		
	}

	public void ImageViewDoubleTabEvent(View v) {
		// TODO 자동 생성된 메소드 스텁
		
		switch ( v.getId() )
		{
		case R.id.event_now:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediatong.rcsoft.co.kr/company/board_list.php?bd_name=event_result")));
			Log.d("doubleTab", "doubleTab");
			break;
		}
		
	}

    


    
}
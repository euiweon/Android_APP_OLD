package com.example.hoteljoin;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.euiweonjeong.base.RecycleUtils;
import com.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HotelImageActivity extends HotelJoinBaseActivity implements OnClickListener{
	HotelImageActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;

	 

	Integer m_GalleryWidth;
	Integer m_GalleryHeight;
	
	Integer Index = 1;
	
	
	ViewPager vp_main = null;	//ViewPager
	CustomPagerAdapter cpa = null;	//커스텀 어댑터
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallayimage);
		
		
		{
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
		// 비하인드 뷰 설정
		setBehindContentView(R.layout.main_menu_1);
		
		self = this;
		

		
		// 슬라이딩 뷰
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.RIGHT);
		
		{
			Display defaultDisplay = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    	
			int width;
			@SuppressWarnings("deprecation")
			int windowswidth = defaultDisplay.getWidth();
			@SuppressWarnings("deprecation")
			int windowsheight= defaultDisplay.getHeight();
			int originwidth = 480;
			

			width = (int) ( (float) 340 * ( (float)(windowswidth)/ (float)(originwidth) )  );

			
			menu.setBehindOffset(windowswidth - width );
		}
		
		menu.setFadeDegree(0.35f);
		
		
		{
			AppManagement _AppManager = (AppManagement) getApplication();

			
			Index = 1;
			((TextView)findViewById(R.id.image_text)).setText(Index + "/"+ _AppManager.m_GallayList.size());
			
			
		}

		
	        
		BtnEvent( R.id.left);
		BtnEvent( R.id.right);
		
		AfterCreate(1);
		
		
		{
			vp_main = (ViewPager) findViewById(R.id.detail_gallery);
			cpa = new CustomPagerAdapter();
			
			vp_main.setAdapter(cpa);
			 //ViewPage 페이지 변경 리스너
	        vp_main.setOnPageChangeListener(new OnPageChangeListener()
	        {

				@Override
				public void onPageScrollStateChanged(int state) {}

				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

				@Override
				public void onPageSelected(int position) 
				{
					//페이지가 변경될때 변경되는 페이지 포지션에 대한 체크(버튼을 활성화/비활성화 시켜줌)
					//pageCheck(position);
					
					AppManagement _AppManager = (AppManagement) getApplication();
					Index = position + 1;
					
					if ( Index > _AppManager.m_GallayList.size())
					{
						Index  =_AppManager.m_GallayList.size();
					}
					else if ( Index < 1)
					{
						Index  =1;
					}
					
					
					SetPage();
				}
	        	
	        });
	        
	        vp_main.setCurrentItem(0);	        
		}
		
		
		


	}
	
	
	@Override
    protected void onDestroy() {
         //Adapter가 있으면 어댑터에서 생성한 recycle메소드를 실행 
        if (cpa != null)
        	cpa.recycle();
         RecycleUtils.recursiveRecycle(getWindow().getDecorView());
          System.gc();
 
         super.onDestroy();
     }
	


	@Override
	public void onResume()
	{
		super.onResume();

	}
	
	public void BtnEvent( int id )
    {
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
    }

	
	public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	public void ImageBtnResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{

		case R.id.left:
		{
			
			Index--;
			
			if ( Index < 1)
			{
				Index  =1;
			}
			else
			{
				SetPage();
				vp_main.setCurrentItem(Index - 1);	
			}

		}
			break;
		case R.id.right:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			Index++;
			
			if ( Index > _AppManager.m_GallayList.size())
			{
				Index  =_AppManager.m_GallayList.size();
			}
			else
			{
				
				SetPage();
				vp_main.setCurrentItem(Index - 1);	
			}
		}
			break;
			

		}
		
	}
	
	public void SetPage()
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		((TextView)findViewById(R.id.image_text)).setText(Index + "/"+ _AppManager.m_GallayList.size());
		 
	}
	
	
	private class CustomPagerAdapter extends PagerAdapter{

		private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();
        @Override
        public int getCount() {
        	AppManagement _AppManager = (AppManagement) getApplication();
                return _AppManager.m_GallayList.size();
        }

        public void recycle() {

    		for (WeakReference<View> ref : mRecycleList) {

    	             RecycleUtils.recursiveRecycle(ref.get());
    		}

    	}

        
        /**
         * 각 페이지 정의
         */
        @Override
        public Object instantiateItem(View collection, int position)
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	ImageView img = new ImageView(getApplicationContext()); //this is a variable that stores the context of the activity
            //set properties for the image like width, height, gravity etc...



            img.setTag(_AppManager.m_GallayList.get(position));;
			BitmapManager.INSTANCE.loadBitmap_3(_AppManager.m_GallayList.get(position), img);
			mRecycleList.add(new WeakReference<View>(img) );
            //img.setImageResource(resId); //setting the source of the image
            
            
            ((ViewPager) collection).addView(img,0);  

            return img;

        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
                ((ViewPager) collection).removeView((View) view);
        }

        
        
        @Override
        public boolean isViewFromObject(View view, Object object) {
                return view==((View)object);
        }

        
        @Override
        public void finishUpdate(View v) {
        }
        

        @Override
        public void restoreState(Parcelable pc, ClassLoader cl) {
        }

        @Override
        public Parcelable saveState() {
                return null;
        }

        @Override
        public void startUpdate(View v) {
        }

    }
	


}

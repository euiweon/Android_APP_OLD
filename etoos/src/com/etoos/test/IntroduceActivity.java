package com.etoos.test;

import android.os.Bundle;






import java.util.ArrayList;

import com.euiweonjeong.base.BitmapManager;

import com.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroduceActivity extends EtoosBaseActivity implements OnClickListener {

	IntroduceActivity self;
	ViewPager m_VP = null;	//ViewPager
	CustomPagerAdapter m_CPA = null;	//커스텀 어댑터



	// 
	private boolean m_CallBottomBanner = false;		// 하단 배너 호출 할타이밍인가? 아니면 상단배너 호출 타이밍인가
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introduce);

		AfterCreate();
		self = this;
		m_CallBottomBanner = false;
		m_CurrIndex =  0;
		m_VP = (ViewPager) findViewById(R.id.market_img);
		m_CPA = new CustomPagerAdapter();
		m_VP.setAdapter(m_CPA);

		//ViewPage 페이지 변경 리스너
		m_VP.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageScrollStateChanged(int state) {}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

			@Override
			public void onPageSelected(int position) 
			{
				selectViewPaper( position );

			}

		});

		BtnEvent(R.id.skip);
		initViewPaperDot(4);
		selectViewPaper(0);

	}

	@Override
	public void onBackPressed() 
	{

		new AlertDialog.Builder(this)
		.setTitle("종료 확인")
		.setMessage("정말 종료 하겠습니까?") //줄였음
		.setPositiveButton("예", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{   
				ExitApp();
			}
		})
		.setNegativeButton("아니요", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				//...할일
			}
		})
		.show();

	}

	public void BtnEvent( int id )
	{
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
	}

	@Override
	public void onResume()
	{
		super.onResume();

	}






	///////////////////////////////////////////////////////////////////////////////////////////////////////
	/// 상단 뷰 페이퍼 

	protected void selectViewPaper(int position)
	{
		int itemIds[] = {
				R.id.imageView_dot_1,
				R.id.imageView_dot_2,
				R.id.imageView_dot_3,
				R.id.imageView_dot_4,
				R.id.imageView_dot_5,
				R.id.imageView_dot_6,
				R.id.imageView_dot_7,
				R.id.imageView_dot_8,
				R.id.imageView_dot_9,
				R.id.imageView_dot_10,
		};
		int nMyId = itemIds[position];
		ImageView ivButton = null;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);
			if(nId == nMyId)
			{
				ivButton.setSelected(true);
			}
			else
			{
				ivButton.setSelected(false);
			}
		}
	}

	private void initViewPaperDot( int count)
	{
		int itemIds[] = {
				R.id.imageView_dot_1,
				R.id.imageView_dot_2,
				R.id.imageView_dot_3,
				R.id.imageView_dot_4,
				R.id.imageView_dot_5,
				R.id.imageView_dot_6,
				R.id.imageView_dot_7,
				R.id.imageView_dot_8,
				R.id.imageView_dot_9,
				R.id.imageView_dot_10,

		};

		ImageView ivButton = null;
		int i = 0;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);

			if (i < count)
			{
				ivButton.setVisibility(View.VISIBLE);
			}
			else
			{
				ivButton.setVisibility(View.GONE);
			}
			i++;
		}
	}



	private class CustomPagerAdapter extends PagerAdapter{


		@Override
		public int getCount() {
			return 4 ;
		}

		/**
		 * 각 페이지 정의
		 */
		 @Override
		 public Object instantiateItem(View collection, int position)
		{



			ImageView img = new ImageView(self); 
			final int pos = position;
			int resId = 0;


			OnClickListener ol  = new OnClickListener()
			{

				@Override
				public void onClick(View v) {

					Intent intent;
					intent = new Intent().setClass(baseself, MainActivity.class);
					startActivity( intent ); 

				}            
			};

			img.setOnClickListener(ol);

			switch ( position )
			{
			case  0 :
				img.setTag( "http://www.apple.com/home/images/billboard_iphone_hero.jpg");
				BitmapManager.INSTANCE.loadBitmap_2("http://www.apple.com/home/images/billboard_iphone_hero.jpg", img);
				break;
			case 1:

				img.setTag( "http://ecx.images-amazon.com/images/I/41XbCqh%2BHlL._SY300_.jpg");
				BitmapManager.INSTANCE.loadBitmap_2("http://ecx.images-amazon.com/images/I/41XbCqh%2BHlL._SY300_.jpg", img);

				break;
			case 2:
				img.setTag( "http://ecx.images-amazon.com/images/I/71p7Mh8mZRL._SX450_.jpg");
				BitmapManager.INSTANCE.loadBitmap_2("http://ecx.images-amazon.com/images/I/71p7Mh8mZRL._SX450_.jpg", img);
				break;
			case 3:

				img.setTag( "http://ecx.images-amazon.com/images/I/81BLd9yh%2B9L._SL1500_.jpg");
				BitmapManager.INSTANCE.loadBitmap_2("http://ecx.images-amazon.com/images/I/81BLd9yh%2B9L._SL1500_.jpg", img);
				break;
			default:
				img.setTag( "http://g-ecx.images-amazon.com/images/G/01/kindle/dp/2012/KT/KT-slate-02-lg._V389394532_.jpg");
				BitmapManager.INSTANCE.loadBitmap_2("http://g-ecx.images-amazon.com/images/G/01/kindle/dp/2012/KT/KT-slate-02-lg._V389394532_.jpg", img);
				break;
			}



			// View를 직접 Add 하고자 할때... 
			/* 
	             v = mInflater.inflate(R.layout.inflate_three, null);
                v.findViewById(R.id.iv_three); 
                v.findViewById(R.id.btn_click_3).setOnClickListener(mPagerListener);

			 */

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
	///////////////////////////////////////////////////////////////////////////////////////////////////////



	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch( arg0.getId())
		{
		case R.id.skip:
		{
			Intent intent;
			intent = new Intent().setClass(baseself, MainActivity.class);
			startActivity( intent ); 
		}
		break;
		}

	}

}

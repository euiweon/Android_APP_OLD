package com.hoteljoin.view;

import com.euiweonjeong.base.BitmapManager;
import com.hoteljoin.AppManagement;
import com.hoteljoin.MainActivity;
import com.hoteljoin.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class MainBottomBanner extends LinearLayout {

	Context mContext = null;
	MainActivity activity;
	ImageView Photo = null;

	String linkTypeCode;
	String linkCode;
	AppManagement _AppManager;

	public MainBottomBanner(MainActivity context, AppManagement _AppManager1) 
	{
		super((Context)context);
		_AppManager = _AppManager1;
		initView(context);
	}
	public MainBottomBanner(MainActivity context,  AttributeSet attrs, AppManagement _AppManager1) {

		super((Context)context, attrs);
		_AppManager = _AppManager1;
		initView(context);
	}
	

	void initView(MainActivity context)
	{
		activity = context;
		mContext = context;
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.main_banner_row, this, false);
		addView(v);   
		

		v.setOnClickListener(new View.OnClickListener() 
		{

			public void onClick(View v) 
			{
				// 클릭시에 이벤트 상세 페이지로 넘겨준다. 

			}
		});
		Photo = (ImageView) findViewById(R.id.banner); 
		
		ImageResize2(R.id.banner);
		ImageResize2(R.id.title_line);
		
		

	}
	
	 public void ImageResize2(int id )
	 {
	    View box = (View)findViewById(id);
	    _AppManager.GetUISizeConverter().ConvertLinearLayoutView(box);
	 }
	
	public void SetData(String linktypecode , String linkcode, String ImageURL )
	{

		linkTypeCode = linktypecode;
		linkCode = linkcode;
		Photo.setTag(ImageURL);
		BitmapManager.INSTANCE.loadBitmap_2(ImageURL, Photo);

	}
}
	
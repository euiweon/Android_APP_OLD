package com.pricecode;

import com.pricecode.*;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MarketItem extends LinearLayout {

	Context mContext = null;
	CMarketActivity activity;
	ImageView Photo = null;

	TextView Title = null;
	TextView Desc = null;
	TextView NewValue = null;
	TextView OldValue = null;
	String LinkURL = null;
	
	int Number =0 ;
	public MarketItem(CMarketActivity context) 
	{
		super((Context)context);
		initView(context);
	}
	public MarketItem(CMarketActivity context,  AttributeSet attrs) {

		super((Context)context, attrs);
		initView(context);

	}
	

	void initView(CMarketActivity context)
	{
		activity = context;
		mContext = context;
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.list_main_item, this, false);
		addView(v);   
		

		v.setOnClickListener(new View.OnClickListener() 
		{

			public void onClick(View v) 
			{
				activity.ShowWebview();
				activity.openWebView(Number);

			}
		});
		
		
		
		Photo = (ImageView) findViewById(R.id.imageView_photo); 

		Title = (TextView) findViewById(R.id.textView_title); 
		Desc = (TextView) findViewById(R.id.textView_desc); 
		NewValue = (TextView) findViewById(R.id.textView_new); 
		OldValue = (TextView) findViewById(R.id.textView_old); 
	}
	
	public void SetData(String title, String ImageURL, String Desc1, String newvalue, String oldvalue, String URL , int number)
	{
		Title.setText(title);
		Desc.setText(Desc1);
		NewValue.setText(newvalue);
		OldValue.setText(oldvalue);
		
		OldValue.setPaintFlags(OldValue.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		 

		Number = number;
		LinkURL =URL; 
		Photo.setTag(ImageURL);
		Photo.setScaleType(ScaleType.FIT_XY);
		BitmapManager.INSTANCE.loadBitmap_2(ImageURL, Photo);
		

		
	}
	
}

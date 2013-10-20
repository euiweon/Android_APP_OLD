package com.etoos.view;


import com.etoos.test.R;
import com.etoos.test.R.layout;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class FastResultItem_dummy  extends LinearLayout {

	Context mContext = null;
	TextView TilteView = null;
	TextView TilteView2 = null;
	TextView TilteView3 = null;
	

	public FastResultItem_dummy(Context context) 
	{
		super(context);
		initView(context);
	}
	public FastResultItem_dummy(Context context, AttributeSet attrs) {

		super(context, attrs);
		initView(context);
	}
	

	void initView(Context context)
	{
		mContext = context;
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.dummy_row, this, false);
		addView(v);   

	}
	
	public void SetData(String title,  String Index , String Name )
	{


	}
}
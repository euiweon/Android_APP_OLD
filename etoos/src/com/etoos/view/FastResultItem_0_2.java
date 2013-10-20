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


public class FastResultItem_0_2  extends LinearLayout {

	Context mContext = null;
	TextView TilteView = null;
	TextView TilteView2 = null;
	TextView TilteView3 = null;
	

	public FastResultItem_0_2(Context context) 
	{
		super(context);
		initView(context);
	}
	public FastResultItem_0_2(Context context, AttributeSet attrs) {

		super(context, attrs);
		initView(context);
	}
	

	void initView(Context context)
	{
		mContext = context;
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.result_data_0_2, this, false);
		addView(v);  

		TilteView = (TextView) findViewById(R.id.name); 
		TilteView2 = (TextView) findViewById(R.id.data); 

	}
	
	public void SetData(String title,  String Data )
	{
		TilteView.setText(title);
		TilteView2.setText(Data);
	}
}
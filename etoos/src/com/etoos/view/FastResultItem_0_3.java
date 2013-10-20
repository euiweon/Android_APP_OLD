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


public class FastResultItem_0_3  extends LinearLayout {

	Context mContext = null;
	
	public boolean m_Hide = false;
	
	View mainView;


	public FastResultItem_0_3(Context context) 
	{
		super(context);
		initView(context);
	}
	public FastResultItem_0_3(Context context, AttributeSet attrs) {

		super(context, attrs);
		initView(context);
	}
	

	void initView(Context context)
	{
		mContext = context;
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v= li.inflate(R.layout.result_data_0_3, this, false);
		addView(v);   
		
		mainView = findViewById(R.id.result_data_0_3); 

	}
	
	public void HideView()
	{
		m_Hide = !m_Hide;
		
		if ( m_Hide )
		{
			mainView.setBackgroundResource(R.drawable.all_pools_top);
			

			
		}
		else
		{
			mainView.setBackgroundResource(R.drawable.all_pools_low);
		}
		
		
		
	}

}
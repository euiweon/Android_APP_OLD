package com.pricecode;

import com.pricecode.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeliveryDetailReviewItemBtn extends LinearLayout {

	Context mContext = null;

	public DeliveryDetailReviewItemBtn(Context context) 
	{
		super(context);
		initView(context);
	}
	public DeliveryDetailReviewItemBtn(Context context, AttributeSet attrs) {

		super(context, attrs);
		initView(context);

	}
	

	void initView(Context context)
	{
		mContext = context;
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.list_item_devery_review2, this, false);
		addView(v);   

	}

	
}

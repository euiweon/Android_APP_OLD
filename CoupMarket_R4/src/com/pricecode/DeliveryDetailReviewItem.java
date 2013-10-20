package com.pricecode;

import com.pricecode.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeliveryDetailReviewItem extends LinearLayout {

	Context mContext = null;
	TextView TilteView = null;
	ImageView StarView1 = null;
	ImageView StarView2 = null;
	ImageView StarView3 = null;
	ImageView StarView4 = null;
	ImageView StarView5 = null;
	TextView ReviewerView = null;
	TextView DayView = null;
	TextView ContentsView = null;
	public DeliveryDetailReviewItem(Context context) 
	{
		super(context);
		initView(context);
	}
	public DeliveryDetailReviewItem(Context context, AttributeSet attrs) {

		super(context, attrs);
		initView(context);

	}
	

	void initView(Context context)
	{
		mContext = context;
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.list_item_devery_review, this, false);
		addView(v);   
		
		TilteView = (TextView) findViewById(R.id.review_title); 
		StarView1 = (ImageView) findViewById(R.id.review_star1); 
		StarView2 = (ImageView) findViewById(R.id.review_star2); 
		StarView3 = (ImageView) findViewById(R.id.review_star3); 
		StarView4 = (ImageView) findViewById(R.id.review_star4); 
		StarView5 = (ImageView) findViewById(R.id.review_star5); 
		ReviewerView = (TextView) findViewById(R.id.reviewer); 
		DayView = (TextView) findViewById(R.id.review_day); 
		ContentsView = (TextView) findViewById(R.id.review_main); 
	}
	
	public void SetData(String title, int starcount, String reviewer, String day, String Content )
	{
		TilteView.setText(title);
		ReviewerView.setText(reviewer);
		DayView.setText(day);
		ContentsView.setText(Content);
		
		StarView1.setImageResource(R.drawable.star_on);
		StarView2.setImageResource(R.drawable.star_on);
		StarView3.setImageResource(R.drawable.star_on);
		StarView4.setImageResource(R.drawable.star_on);
		StarView5.setImageResource(R.drawable.star_on);
		
		switch( starcount )
		{
		case 5:
			StarView5.setImageResource(R.drawable.star_off);
		case 4:
			StarView4.setImageResource(R.drawable.star_off);
		case 3:
			StarView3.setImageResource(R.drawable.star_off);
		case 2:
			StarView2.setImageResource(R.drawable.star_off);
		case 1:
			StarView1.setImageResource(R.drawable.star_off);
		case 0:
			break;
	
		}
		
	}
	
}

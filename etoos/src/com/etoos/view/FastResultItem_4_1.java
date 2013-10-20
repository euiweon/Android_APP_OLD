package com.etoos.view;


import java.util.ArrayList;

import com.etoos.data.OMRHistoryContent;
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


public class FastResultItem_4_1  extends LinearLayout {

	Context mContext = null;


	public FastResultItem_4_1(Context context) 
	{
		super(context);
		initView(context);
	}
	public FastResultItem_4_1(Context context, AttributeSet attrs) {

		super(context, attrs);
		initView(context);
	}


	void initView(Context context)
	{
		mContext = context;

		String infService = Context.LAYOUT_INFLATER_SERVICE;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.result_data_4_1, this, false);
		addView(v);   

	}

	public void SetData(ArrayList< OMRHistoryContent > list)
	{

		findViewById(R.id.sol_time_1).setVisibility(View.GONE);
		findViewById(R.id.sol_time_2).setVisibility(View.GONE);
		findViewById(R.id.sol_time_3).setVisibility(View.GONE);
		findViewById(R.id.sol_time_4).setVisibility(View.GONE);
		findViewById(R.id.sol_1).setVisibility(View.GONE);
		findViewById(R.id.sol_2).setVisibility(View.GONE);
		findViewById(R.id.sol_3).setVisibility(View.GONE);
		findViewById(R.id.sol_4).setVisibility(View.GONE);
		for ( int i = 0 ; i  < list.size() ; i++ )
		{
			TextView textView = null; 
			ImageView imageView = null; 
			switch(i)
			{
			case 0: 	
				textView = (TextView) findViewById(R.id.sol_time_1); 
				imageView = (ImageView) findViewById(R.id.sol_1); 

				break; 
			case 1: 	
				textView = (TextView) findViewById(R.id.sol_time_2); 
				imageView = (ImageView) findViewById(R.id.sol_2); 

				break; 
			case 2: 	
				textView = (TextView) findViewById(R.id.sol_time_3); 
				imageView = (ImageView) findViewById(R.id.sol_3); 

				break; 
			case 3: 	
				textView = (TextView) findViewById(R.id.sol_time_4); 
				imageView = (ImageView) findViewById(R.id.sol_4); 
				break; 
			}
			textView.setText(list.get(i).Timer + "ÃÊ");

			textView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.VISIBLE);

			switch( list.get(i).QuestionType)
			{
			case 0:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : imageView.setBackgroundResource(R.drawable.a_white_1 );						break;
					case 1 : imageView.setBackgroundResource(R.drawable.a_white_2 );						break;
					case 2 : imageView.setBackgroundResource(R.drawable.a_white_3 );						break;
					case 3 : imageView.setBackgroundResource(R.drawable.a_white_4 );						break;
					case 4 : imageView.setBackgroundResource(R.drawable.a_white_5 );						break;
					}
				}
			break;
			case 1:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : imageView.setBackgroundResource(R.drawable.mark_show_1 );						break;
					case 1 : imageView.setBackgroundResource(R.drawable.mark_show_2 );						break;
					case 2 : imageView.setBackgroundResource(R.drawable.mark_show_3 );						break;
					case 3 : imageView.setBackgroundResource(R.drawable.mark_show_4 );						break;
					case 4 : imageView.setBackgroundResource(R.drawable.mark_show_5 );						break;
					}
				}
			break;
			case 2:
			case 3:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : imageView.setBackgroundResource(R.drawable.a_black_1 );						break;
					case 1 : imageView.setBackgroundResource(R.drawable.a_black_2 );						break;
					case 2 : imageView.setBackgroundResource(R.drawable.a_black_3 );						break;
					case 3 : imageView.setBackgroundResource(R.drawable.a_black_4 );						break;
					case 4 : imageView.setBackgroundResource(R.drawable.a_black_5 );						break;
					}
				}
			break;
			}

		}
	}
}
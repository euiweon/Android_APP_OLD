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


public class FastResultItem_1  extends LinearLayout {

	Context mContext = null;
	TextView TilteView = null;
	TextView TilteView2 = null;
	TextView TilteView3 = null;


	public FastResultItem_1(Context context) 
	{
		super(context);
		initView(context);
	}
	public FastResultItem_1(Context context, AttributeSet attrs) {

		super(context, attrs);
		initView(context);
	}


	void initView(Context context)
	{
		mContext = context;

		String infService = Context.LAYOUT_INFLATER_SERVICE;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.result_data_1, this, false);
		addView(v);   

	}

	public void SetData(ArrayList< OMRHistoryContent > list )
	{

		// 일단 모든 데이터 Hide 시킴
		((TextView)findViewById(R.id.number1)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.number2)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.number3)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.number4)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.number5)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.number6)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.number7)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.number8)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.number9)).setVisibility(View.GONE);


		// 해당하는 데이터만 Visible 시키고 데이터를 입력해준다. 


		for ( int  i = 0 ; i < list.size() ; i++  )
		{
			switch( i)
			{
			case 0 :
			{
				((TextView)findViewById(R.id.number1)).setText(list.get(i).Number.toString());
				((TextView)findViewById(R.id.number1)).setVisibility(View.VISIBLE);
			}
			break;
			case 1 :
			{
				((TextView)findViewById(R.id.number2)).setText(list.get(i).Number.toString());
				((TextView)findViewById(R.id.number2)).setVisibility(View.VISIBLE);
			}
			break;
			case 2 :
			{
				((TextView)findViewById(R.id.number3)).setText(list.get(i).Number.toString());
				((TextView)findViewById(R.id.number3)).setVisibility(View.VISIBLE);
			}
			break;
			case 3 :
			{
				((TextView)findViewById(R.id.number4)).setText(list.get(i).Number.toString());
				((TextView)findViewById(R.id.number4)).setVisibility(View.VISIBLE);
			}
			break;
			case 4 :
			{
				((TextView)findViewById(R.id.number5)).setText(list.get(i).Number.toString());
				((TextView)findViewById(R.id.number5)).setVisibility(View.VISIBLE);
			}
			break;
			case 5 :
			{
				((TextView)findViewById(R.id.number6)).setText(list.get(i).Number.toString());
				((TextView)findViewById(R.id.number6)).setVisibility(View.VISIBLE);
			}
			break;
			case 6 :
			{
				((TextView)findViewById(R.id.number7)).setText(list.get(i).Number.toString());
				((TextView)findViewById(R.id.number7)).setVisibility(View.VISIBLE);
			}
			break;

			case 7 :
			{
				((TextView)findViewById(R.id.number8)).setText(list.get(i).Number.toString());
				((TextView)findViewById(R.id.number8)).setVisibility(View.VISIBLE);
			}
			break;
			case 8 :
			{
				((TextView)findViewById(R.id.number9)).setText(list.get(i).Number.toString());
				((TextView)findViewById(R.id.number9)).setVisibility(View.VISIBLE);
			}
			break;

			}
		}


	}
}
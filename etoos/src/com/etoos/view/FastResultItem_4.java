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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class FastResultItem_4  extends LinearLayout {

	Context mContext = null;
	TextView TilteView = null;
	TextView TilteView2 = null;
	TextView TilteView3 = null;
	boolean m_Hide = true;
	
	ViewGroup managerView = null;
	
	
	ArrayList<View> m_ViewList = new ArrayList<View>();

	public FastResultItem_4(Context context) 
	{
		super(context);
		initView(context);
	}
	public FastResultItem_4(Context context, AttributeSet attrs) {

		super(context, attrs);
		initView(context);
	}
	

	void initView(Context context)
	{
		
		m_ViewList.clear();
		mContext = context;
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View managerView = li.inflate(R.layout.result_data_4, this, false);
		

		addView(managerView);   
		
		
		TilteView = (TextView) findViewById(R.id.number); 
		TilteView2 = (TextView) findViewById(R.id.number2); 
		TilteView3 = (TextView) findViewById(R.id.number3); 

	}
	
	public void SetData(Integer Number , Integer Answer [],  ArrayList<View> viewList, ArrayList< OMRHistoryContent > list  )
	{
		m_ViewList = viewList;
		
		TilteView.setText(Number.toString());
		
		// 정답 입력 
		String tempString = "";
		for ( int i = 0 ; i < Answer.length ; i++)
		{
			if (Answer[i] > 1)
			{
				tempString += i+1+ " ";
			}
			
		}
		if ( tempString.equals(""))
		{
			TilteView2.setText("정답을 체크하지 않았습니다.");
		}
		else
		{
			TilteView2.setText(tempString.toString());
		}
		
		
		Integer Time = 0;
		Integer Min = 0;
		Integer Sec = 0 ; 
		for ( int i = 0 ; i < list.size() ; i++ )
		{
			Time += list.get(i).Timer;
		}
		
		Min = Time / 60 ; 
		Sec = Time % 60;
		TilteView3.setText(String.format("%02d",Min) + " : " + String.format("%02d",Sec));
		
		
	}	
	
	public void ShowDetail()
	{
		if ( m_ViewList == null )
			return;
		m_Hide = !m_Hide;
		for ( int i = 0 ; i < m_ViewList.size() ; i ++ )
		{
			if ( m_Hide )
			{
				m_ViewList.get(i).setVisibility(View.GONE);
			}
			else
			{
				m_ViewList.get(i).setVisibility(View.VISIBLE);
			}
		}
	}
}
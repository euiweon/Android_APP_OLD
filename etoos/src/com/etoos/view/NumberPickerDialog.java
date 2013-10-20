package com.etoos.view;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.etoos.test.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class NumberPickerDialog extends Dialog{

	Context maincontext ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setAttributes(lpWindow);

		setContentView(R.layout.popupnumberpick);

		setLayout();

		setClickListener(mLeftClickListener , null ); 
	}

	public NumberPickerDialog(Context context) {
		// Dialog 배경을 투명 처리 해준다.
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
	}

	public NumberPickerDialog(Context context , String title , 
			View.OnClickListener singleListener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
		this.mLeftClickListener = singleListener;
	}

	public NumberPickerDialog(Context context , String title , String content , 
			View.OnClickListener leftListener ) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
		maincontext = context;
		this.mLeftClickListener = leftListener;

	}



	private void setClickListener(View.OnClickListener left , View.OnClickListener right){
		if(left!=null && right!=null){
			mLeftButton.setOnClickListener(left);
		}else if(left!=null && right==null){
			mLeftButton.setOnClickListener(left);
		}else {

		}
	}


	private Button mLeftButton;


	private View.OnClickListener mLeftClickListener;

	/*
	 * Layout
	 */
	 private void setLayout(){
//		mTitleView = (TextView) findViewById(R.id.tv_title);
//		mContentView = (TextView) findViewById(R.id.tv_content);
		mLeftButton = (Button) findViewById(R.id.bt_left);
		
		final WheelView hours = (WheelView) findViewById(R.id.hun);
		hours.setViewAdapter(new kankan.wheel.widget.adapters.NumericWheelAdapter(maincontext, 0, 9));
	
		final WheelView mins = (WheelView) findViewById(R.id.ten);
		mins.setViewAdapter(new kankan.wheel.widget.adapters.NumericWheelAdapter(maincontext, 0, 9));
		
		final WheelView sec = (WheelView) findViewById(R.id.one);
		sec.setViewAdapter(new kankan.wheel.widget.adapters.NumericWheelAdapter(maincontext, 0, 9));
		
	 }
	 
	 public String GetNumber()
	 {
		 Integer returnvalue =0;
		 final WheelView hours = (WheelView) findViewById(R.id.hun);
		 returnvalue = ((Integer)((hours.getCurrentItem() )* 100)) ;
		 final WheelView mins = (WheelView) findViewById(R.id.ten);
		 returnvalue += ((Integer)(mins.getCurrentItem()* 10)) ;
		 final WheelView sec = (WheelView) findViewById(R.id.one);
		 returnvalue += ((Integer)(sec.getCurrentItem())) ;
		 
		 //returnvalue = ((Integer)(hours.getCurrentItem())).toString() +((Integer)(hours.getCurrentItem())).toString()+((Integer)(hours.getCurrentItem())).toString();
		 return ((Integer)(hours.getCurrentItem())).toString() +((Integer)(mins.getCurrentItem())).toString()+((Integer)(sec.getCurrentItem())).toString();
	 }
	 
	 

}
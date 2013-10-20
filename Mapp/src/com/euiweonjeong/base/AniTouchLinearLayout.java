package com.euiweonjeong.base;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;



public class AniTouchLinearLayout extends LinearLayout {
	
	private static final String TAG = "AniTouchLinearLayout";

	private float x_offset = 0;
    private float y_offset = 0;

    
	 public AniTouchLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	 
	 protected boolean isTransformedTouchPointInView(float x, float y, View child, PointF outLocalPoint) {
		 //Log.d(TAG, "isTranformedTouchPointInView()");
		 final Rect frame = new Rect();
		 child.getHitRect(frame);
		 if(frame.contains((int)x, (int)y)) {
			 return true;
		 }
		 return false;
	 }
	 
	 

}

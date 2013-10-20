package com.euiweonjeong.base;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;




public class ImageViewDoubleTab extends ImageView  {

	ImageViewDoubleTabListener listener = null;
	private long lastTouchTime = -1;
	public ImageViewDoubleTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 磊悼 积己等 积己磊 胶庞
		
		
	}
	
	public void SetDoubleTabListener( ImageViewDoubleTabListener l )
	{
		listener = l;
	}


	public boolean onTouchEvent(MotionEvent ev) 
	{
	 


		if (ev.getAction() == MotionEvent.ACTION_DOWN) 
		{
		 
	
	
			long thisTime = System.currentTimeMillis();
			 
			if (thisTime - lastTouchTime < 250) 
			{
				// Double tap
				 
				lastTouchTime = -1;	
				if ( listener!=null )
				{
					listener.ImageViewDoubleTabEvent((View) (this) );
				}
			} 
			else
			{
			 
				// Too slow :)
				 
				lastTouchTime = thisTime;
	 
			}
			
		}
		return false;
	}
}

package com.euiweonjeong.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class GalleryForOneFling extends Gallery {
	 public GalleryForOneFling(Context context, AttributeSet attrs) {
		 super(context, attrs);
	 }


	 @Override
	 public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	 	 int position = getSelectedItemPosition();
	 	 if(e2.getX() > e1.getX()){
	 		 if(position > 0) setSelection(position - 1, true);
	 	 }
	 	 else{
	 		 if(position < getCount() - 1) setSelection(position + 1, true);
	 	 }
	 	 return true;
	 }

}

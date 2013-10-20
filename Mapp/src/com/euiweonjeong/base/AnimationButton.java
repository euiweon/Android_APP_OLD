package com.euiweonjeong.base;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;



public class AnimationButton extends Button {
    private float x_offset = 0;
    private float y_offset = 0;
    
    public final static int DELEGATE = 0;
	public final static int MY_DELEGATE = 1;
	private int mMode = MY_DELEGATE;

    public AnimationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationButton(Context context) {
        super(context);
    }

    public AnimationButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDelegate(int delegate) {
		mMode = delegate;
	}
    
    @Override
    public void getHitRect(Rect outRect) {
        Rect curr = new Rect();
        super.getHitRect(curr);

        outRect.bottom = (int) (curr.bottom + y_offset);
        outRect.top = (int) (curr.top + y_offset);
        outRect.left = (int) (curr.left + x_offset);
        outRect.right = (int) (curr.right + x_offset);
    }

    public void setOffset(float endX, float endY) {
        x_offset = endX;
        y_offset = endY;
    }

    public float getXOffset() {
        return x_offset;
    }

    public float getYOffset() {
        return y_offset;
    }
    
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mMode == DELEGATE) {
			return super.onTouchEvent(event);
		}
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int action = event.getAction();

		if (isEnabled()) {
			switch (action & 0xFF) {
			case MotionEvent.ACTION_DOWN:
				setPressed(true);
				return true;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				setPressed(false);
				return false;
			}
		}
		if (action == MotionEvent.ACTION_MOVE) {
			if (!contains(x, y)) {
				setPressed(false);
				return false;
			}
			return true;
		}
		return false;
	}

	/*
	 * Returns if child view's expandable touch area contains
	 * the point.
	 */
	private boolean contains(int x, int y) {
		final Rect frame = new Rect();
		getHitRect(frame);
		if(frame.contains((int)x, (int)y)) {
			 return true;
		}
		return false;
	}
	
}



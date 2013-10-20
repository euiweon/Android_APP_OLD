package com.goodmate.tayotayo;

import com.euiweonjeong.base.BaseActivity;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

// 기본 Activity
public class BaseTayoActivity extends BaseActivity
{
	BaseTayoActivity baseself;
	public ProgressDialog mProgress;
	protected String TAG = "BaseTayoActivity";

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		TAG = getClassName(getClass());
		
		Log.e(TAG, "onCreate");
		
		baseself = this;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		Log.e(TAG, "onNewIntent");
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.e(TAG, "onPause");
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.e(TAG, "onRestart");
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.e(TAG, "onResume");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.e(TAG, "onSaveInstanceState");
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Log.e(TAG, "onStart");
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		Log.e(TAG, "onStop");
	}

	@Override
	public void onUserInteraction()
	{
		super.onUserInteraction();
		Log.e(TAG, "onUserInteraction");
	}

	@Override
	protected void onUserLeaveHint()
	{
		super.onUserLeaveHint();
		Log.e(TAG, "onUserLeaveHint");
	}
	


	protected String getClassName(Class<?> cls)
	{
		String FQClassName = cls.getName();
		int firstChar = FQClassName.lastIndexOf('.') + 1;
			
		if(firstChar > 0)
		{
			FQClassName = FQClassName.substring(firstChar);
		}
			
		return FQClassName;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Log.e(TAG, "onKeyDown");
		return super.onKeyDown(keyCode, event);
	}
	
	public void RefreshUI()
	{
		
	}
	
	
    public void ImageBtnRefresh( int id , int bitmapID )
    {
    	
    	ImageView imageview = (ImageView)findViewById(id);
    	imageview.setBackgroundResource(bitmapID);
    }
	
	public void ShowAlertDialLog( Activity activity , String message )
	{
		// 일단 찜이 제대로 되었는지 확인한다. 
		// 세가지의 경우에 따라 처리한다.
		// 1. 정상적으로 등록 되었을 경우
		// 2. 이미 등록이 되어 있었을 경우
		// 3. 원인 모를 문제로 등록이 안될 경우 
		
		// 다이얼로그를 생성 
		new AlertDialog.Builder(activity)

	
		.setTitle("에러")
		.setMessage(message)
		.setPositiveButton("확인", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		})
		.show();
	}
	public void ShowAlertDialLog( Activity activity , String titleMessage , String message )
	{
		// 일단 찜이 제대로 되었는지 확인한다. 
		// 세가지의 경우에 따라 처리한다.
		// 1. 정상적으로 등록 되었을 경우
		// 2. 이미 등록이 되어 있었을 경우
		// 3. 원인 모를 문제로 등록이 안될 경우 
		
		// 다이얼로그를 생성 
		new AlertDialog.Builder(activity)

	
		.setTitle(titleMessage)
		.setMessage(message)
		.setPositiveButton("확인", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		})
		.show();
	}
	
	

	
	public static Typeface mTypeface;
	public static Typeface mTypefaceBold;
	
	public void AfterCreate()
    {
    	if ( mTypeface == null)
    		mTypeface = Typeface.createFromAsset(getAssets(), "nanumgothic.ttf");
    	
    	if ( mTypefaceBold == null)
    		mTypefaceBold = Typeface.createFromAsset(getAssets(), "nanumgothicbold.ttf");
    	
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);
        setResize( 2, root, true);
    	
    	{

    	}
    }
	
	
	public void ImageResize(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	
    	View box = (View)findViewById(id);
    	Log.v("Type", box.getClass().getName() );
    	_AppManager.GetUISizeConverter().ConvertFrameLayoutView(box);
    }
    public void ImageResize2(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View box = (View)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutView(box);
    }
    
    public void ImageResize3(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View box = (View)findViewById(id);
    	if ( box != null )
    		_AppManager.GetUISizeConverter().ConvertRelativeLayoutView(box);
    }
    
    public void TextResize(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertFrameLayoutTextView(box);
    }
    public void TextResize2(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutTextView(box);
    }
    
    public void TextResize3(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertRelativeLayoutTextView(box);
    }
    
    


    
    
    void setGlobalFont(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView)
            {
                ((TextView)child).setTypeface(mTypeface);
                ((TextView)child).setTypeface(mTypefaceBold, Typeface.BOLD);
            }
           /* else if  (child instanceof EditText) 
            {
            	((EditText)child).setTypeface(mTypeface);
                ((EditText)child).setTypeface(mTypefaceBold, Typeface.BOLD);
            }*/
            else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup)child);
            
            
            
        }
    }

    void setTextFont( int resId)
    { 
    	TextView box = (TextView)findViewById(resId);
    	
    	((TextView)box).setTypeface(mTypeface);
        ((TextView)box).setTypeface(mTypefaceBold, Typeface.BOLD);
    }
    
    void setResize( int index , ViewGroup root , Boolean first )
    {
    	for (int i = 0; i < root.getChildCount(); i++) 
    	{
            View child = root.getChildAt(i);
            if ( first == false)
            {
            	if ( index == 0 )
                {
            		 if (child instanceof TextView)
            		 {
            			 TextResize3(child.getId());
            			 
            		 }
            		 else
            		 {
            			 ImageResize3(child.getId());
            		 }
                	
                }
                else if ( index == 1 )
                {
                	if (child instanceof TextView)
	           		{
                		TextResize2(child.getId());
                		
	           		}
	           		else
	           		{
	           			ImageResize2(child.getId());
	           		}

                }
                else if ( index == 2 )
                {
                	if (child instanceof TextView)
	           		{
                		TextResize(child.getId());
                		
	           		}
                	else if ( child instanceof CalendarView)
                	{
                		ImageResize(child.getId());
                	}
	           		else
	           		{
	           			ImageResize(child.getId());
	           		}
                }
            }
            
            if (child instanceof LinearLayout)
            {
            	setResize( 1 , (ViewGroup)child, false );
            }
                
            else if (child instanceof FrameLayout)
            {
            	if ( child instanceof CalendarView)
            	{
            		
            	}
            	else
            	{
            		setResize( 2 , (ViewGroup)child, false );
            	}
            	
            }
            else if (child instanceof RelativeLayout)
            {
            	setResize( 0 , (ViewGroup)child, false );
            } 
        }
    }
    
	
}

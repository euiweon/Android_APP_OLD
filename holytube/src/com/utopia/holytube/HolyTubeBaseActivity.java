package com.utopia.holytube;

import java.io.StringReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.RecycleUtils;
import com.utopia.holytube.mainSingleton.TodayData;

public class HolyTubeBaseActivity extends BaseActivity 
{
	HolyTubeBaseActivity baseself;

	int m_CurrIndex;

	public static Typeface mTypeface;
	public static Typeface mTypefaceBold;
	
	public ProgressDialog mProgress;
	protected String TAG = "HotelJoinBaseActivity";
	
	
	@Override
	protected void onDestroy()
	{

		RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		System.gc();

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
	
	public static boolean isEmailPattern(String email){
	     Pattern pattern=Pattern.compile("\\w+[@]\\w+\\.\\w+");
	     Matcher match=pattern.matcher(email);
	     return match.find();
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
	
	

	
	/** Get Bitmap's Width **/
	 public static int getBitmapOfWidth( String fileName ){
	    try {
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(fileName, options);
	        return options.outWidth;
	    } catch(Exception e) {
	    return 0;
	    }
	 }
	 
	 /** Get Bitmap's height **/
	 public static int getBitmapOfHeight( String fileName ){
	  
	    try {
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(fileName, options);
	  
	        return options.outHeight;
	    } catch(Exception e) {
	        return 0;
	   }
	 }
	 
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseself = this;
        TAG = getClassName(getClass());
		
		Log.e(TAG, "onCreate");
		
		{
        	mainSingleton _AppManager = (mainSingleton) getApplication();
        	_AppManager.activityList1.add(this);
        }
		
    }
    
    public void ImageBtnEvent( int id , OnClickListener listener  )
    {
    	View imageview = (View)findViewById(id);
        imageview.setOnClickListener(listener);
    }
    
    
    public static String getMD5Hash(String s) {
        MessageDigest m = null;
        String hash = null;
   	
        try {
       	 m = MessageDigest.getInstance("MD5");
       	 m.update(s.getBytes(),0,s.length());
   	     hash = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
       	 e.printStackTrace();
        }
   	
        return hash;
   }

    
    public void ExitApp()
    {
    	mainSingleton _AppManager = (mainSingleton) getApplication();
        
 	   moveTaskToBack(true);
 	   for ( int i = 0; i < _AppManager.activityList1.size() ; i++ )
	    		 _AppManager.activityList1.get(i).finish();
 	   
 	  _AppManager.activityList1.clear();
	    	 
	   android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    
    public void ImageResize(int id )
    {
    	mainSingleton _AppManager = (mainSingleton) getApplication();
    	
    	View box = (View)findViewById(id);
    	Log.v("Type", box.getClass().getName() );
    	_AppManager.GetUISizeConverter().ConvertFrameLayoutView(box);
    }
    public void ImageResize2(int id )
    {
    	mainSingleton _AppManager = (mainSingleton) getApplication();
    	View box = (View)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutView(box);
    }
    
    public void ImageResize3(int id )
    {
    	mainSingleton _AppManager = (mainSingleton) getApplication();
    	View box = (View)findViewById(id);
    	if ( box != null )
    		_AppManager.GetUISizeConverter().ConvertRelativeLayoutView(box);
    }
    
    public void TextResize(int id )
    {
    	mainSingleton _AppManager = (mainSingleton) getApplication();
    	TextView box = (TextView)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertFrameLayoutTextView(box);
    }
    public void TextResize2(int id )
    {
    	mainSingleton _AppManager = (mainSingleton) getApplication();
    	TextView box = (TextView)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutTextView(box);
    }
    
    public void TextResize3(int id )
    {
    	mainSingleton _AppManager = (mainSingleton) getApplication();
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
            	setResize( 2 , (ViewGroup)child, false );
            	
            }
            else if (child instanceof RelativeLayout)
            {
            	setResize( 0 , (ViewGroup)child, false );
            } 
        }
    }
    

    
    public void AfterCreate( )
    {
    	if ( mTypeface == null)
    		mTypeface = Typeface.createFromAsset(getAssets(), "nanumgothic.ttf");
    	
    	if ( mTypefaceBold == null)
    		mTypefaceBold = Typeface.createFromAsset(getAssets(), "nanumgothicbold.ttf");
    	
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);
        setResize( 2, root, true);
    }
    
    
    public String getPath(Uri uri)
    {    
    	
    	String[] projection = { MediaStore.Images.Media.DATA };
    	Cursor cursor =getContentResolver().query(uri, projection, null, null, null);
        //Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    } 
    
    public String getRealImagePath (Uri uriPath)
	{
		String []proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = managedQuery (uriPath, proj, null, null, null);
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		String path = cursor.getString(index);
		path = path.substring(5);

		return path;
	}
    
    public void Like( final String mid , final Handler handle)
    {
    	if ( mProgress.isShowing() == true)
	   		return;
		mProgress.show();
		
		final mainSingleton myApp = (mainSingleton) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				handle.sendEmptyMessage(200);
				
				Map<String, String> data = new HashMap  <String, String>();
				
				data.put("mid", mid );
				data.put("uuid", myApp.DeviceID );
				


				//String strJSON = myApp.GetHTTPGetData(mainSingleton.DEF_HOME_URL + "/movie_group_detail_s.asp", data);
				String strJSON = myApp.PostHTTPData(mainSingleton.DEF_HOME_URL + "/fav_s.asp", data);
				
				if ( strJSON.equals("OK"))
				{
					handle.sendEmptyMessage(201);
				}
				else if ( strJSON.equals("DUPLICATE"))
				{
					handle.sendEmptyMessage(202);
				}
				else
				{
					handle.sendEmptyMessage(203);
				}
					

				
			}
		});
		
		thread.start(); 
    }
    


}

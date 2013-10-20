package com.example.hoteljoin;



import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.BitmapManager;
import com.euiweonjeong.base.RecycleUtils;
import com.slidingmenu.lib.app.SlidingActivity;
import com.slidingmenu.lib.app.SlidingFragmentActivity;


public class HotelJoinBaseActivity2 extends BaseActivity  {

	
	HotelJoinBaseActivity2 baseself;

	
	int m_CurrIndex;
	

	
	public static Typeface mTypeface;
	public static Typeface mTypefaceBold;
	 LinearLayout 		m_BottomMenuBar;
	 LinearLayout 		m_BottomMenuBar_1;
	
	OnClickListener m_ClickListner = new OnClickListener()
	{  //클릭 이벤트 객체
		public void onClick(View v) 
		{

		   
			switch (v.getId()) 
			{
				case R.id.bottom_1:
				{
					if ( m_CurrIndex != 0 )
					{
		            	Intent intent;
		                intent = new Intent().setClass(baseself, MainActivity.class);
		                startActivity( intent ); 					
					}
				}
					break;
				case R.id.bottom_2:
				{
					if ( m_CurrIndex != 1 )
					{
		            	Intent intent;
		                intent = new Intent().setClass(baseself, HotelSearchActivity.class);
		                startActivity( intent ); 
					}
				}
					break;
				case R.id.bottom_3:
				{
					if ( m_CurrIndex != 2 )
					{
						
						Intent intent;
		                intent = new Intent().setClass(baseself, MyLocationActivity.class);
		                startActivity( intent ); 
					}
				}
					break;
				case R.id.bottom_4:
				{
					if ( m_CurrIndex != 3 )
					{
						
						/*Intent intent;
		                intent = new Intent().setClass(baseself, TravelActivity.class);
		                startActivity( intent ); */
						
					}
				}
					break;
				case R.id.bottom_5:
				{
					if ( m_CurrIndex != 4 )
					{
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.m_UsbHotelcode= false; 
						Intent intent;
		                intent = new Intent().setClass(baseself, TravelActivity.class);
		                startActivity( intent ); 
						
					}
				}
					break;

					
			}
		}
	};
		 


	
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

    
    
    
    public void ImageResize(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View box = (View)findViewById(id);
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
    
    
    public void AfterCreate( int Index , Boolean resize )
    {
    	if ( mTypeface == null)
    		mTypeface = Typeface.createFromAsset(getAssets(), "nanumgothic.ttf");
    	
    	if ( mTypefaceBold == null)
    		mTypefaceBold = Typeface.createFromAsset(getAssets(), "nanumgothicbold.ttf");
    	
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);
        if ( resize == true)
        	setResize( 2, root, true);
        else
    	{
           ImageResize(R.id.title_bar);
           TextResize(R.id.title_logo);
            ImageResize(R.id.sub_bar);
            ImageResize(R.id.main_layout);

            
            
            ImageResize3(R.id.bottom_menu);
            ImageResize2(R.id.bottom_menu_1);
            ImageResize2(R.id.bottom_1);
            ImageResize2(R.id.bottom_2);
            ImageResize2(R.id.bottom_3);
            ImageResize2(R.id.bottom_4);
            ImageResize2(R.id.bottom_5);

            
            
            


    	}
    	

    	
    	{
    		ImageView imageview = (ImageView)findViewById(R.id.bottom_1);
    		imageview.setOnClickListener(m_ClickListner);
    	}
    	{
    		ImageView imageview = (ImageView)findViewById(R.id.bottom_2);
    		imageview.setOnClickListener(m_ClickListner);
    	}
    	{
    		ImageView imageview = (ImageView)findViewById(R.id.bottom_3);
    		imageview.setOnClickListener(m_ClickListner);
    	}
    	{
    		ImageView imageview = (ImageView)findViewById(R.id.bottom_4);
    		imageview.setOnClickListener(m_ClickListner);
    	}
    	{
    		ImageView imageview = (ImageView)findViewById(R.id.bottom_5);
    		imageview.setOnClickListener(m_ClickListner);
    	}

    	
		
    	m_CurrIndex = Index;
    	
    	switch ( m_CurrIndex )
    	{
	    	case 0:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_1);	
		    	image.setBackgroundResource(R.drawable.bt_1_2_bt);
		    }
		    	break;
	    	case 1:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_2);	
		    	image.setBackgroundResource(R.drawable.bt_2_2_bt);	
		    }
		    	break;
		    	
	    	case 2:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_3);	
		    	image.setBackgroundResource(R.drawable.bt_3_2_bt);	
		    }
		    	break;
		    	
	    	case 3:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_4);	
		    	image.setBackgroundResource(R.drawable.bt_4_2_bt);	
		    }
		    	break;
	    	case 4:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_5);	
		    	image.setBackgroundResource(R.drawable.bt_5_2_bt);	
		    }
		    	break;

    	}
    }
    
  
    
    public static boolean isEmailPattern(String email){
        Pattern pattern=Pattern.compile("\\w+[@]\\w+\\.\\w+");
        Matcher match=pattern.matcher(email);
        return match.find();
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
    
    
    public void BottomMenuUp()
    {
    	((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.VISIBLE);
    }
    
    public void BottomMenuDown( Boolean bFullDown )
    {
    	 ((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.GONE);
    }
    
    public void BottomMenuDefault()
    {
    	((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.VISIBLE);

    }
    


    
    


    
    
    protected boolean isSubsetOf1(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }
    

	public class ImageAdapter extends BaseAdapter {

		private Bitmap [] ImageList;
		private Context mContext;
		
		ImageAdapter(Context c, Bitmap [] list){
			mContext = c;
			ImageList = list; 
		}
		
		
		
		public int getCount() {
			return ImageList.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {	
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			
			BitmapFactory.Options bo = new BitmapFactory.Options();
			bo.inSampleSize = 8;
			
			imageView.setImageBitmap(ImageList[position]);
			
			return imageView;
			
		}
	}
	
	
	public class ImageAdapter2 extends BaseAdapter {

		private String [] ImageList;
		private Context mContext;
		
		private Integer m_Width;
		private Integer m_Height;
		
		
		ImageAdapter2(Context c, String [] list , int width, int height){
			mContext = c;
			ImageList = list; 
			m_Width= width;
			m_Height = height; 
			
		}
		
		
		
		public int getCount() {
			return ImageList.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {	
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ImageView imageView = new ImageView(mContext);
			
			imageView.setLayoutParams(new Gallery.LayoutParams(m_Width, m_Height));



			imageView.setTag(ImageList[position]);
			BitmapManager.INSTANCE.loadBitmap(ImageList[position], imageView, m_Width, m_Height);
			return imageView;
			
		}
	}
	
	
	public class ImageAdapter3 extends BaseAdapter {

		private ArrayList<String> ImageList;
		private Context mContext;
		
		private Integer m_Width;
		private Integer m_Height;
		
		
		ImageAdapter3(Context c, ArrayList<String> list , int width, int height){
			mContext = c;
			ImageList = list; 
			m_Width= width;
			m_Height = height; 
			
		}
		
		
		
		public int getCount() {
			return ImageList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {	
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ImageView imageView = new ImageView(mContext);

			imageView.setLayoutParams(new Gallery.LayoutParams(m_Width , m_Height));
			
			imageView.setTag(ImageList.get(position));
			BitmapManager.INSTANCE.loadBitmap(ImageList.get(position), imageView, m_Width, m_Height);
			return imageView;
			
		}
	}
	
	
	public class ImageAdapter4 extends BaseAdapter {

		private Bitmap [] ImageList;
		private Context mContext;
		
		private Integer m_Width;
		private Integer m_Height;
		
		ImageAdapter4(Context c, Bitmap [] list, int width, int height ){
			mContext = c;
			ImageList = list; 
			m_Width = width;
			m_Height= height;
		}
		
		
		
		public int getCount() {
			return ImageList.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {	
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			
			imageView.setLayoutParams(new Gallery.LayoutParams(m_Width , m_Height));
			

			
			imageView.setImageBitmap(ImageList[position]);
			
			return imageView;
			
		}
	}
	
	
	
}

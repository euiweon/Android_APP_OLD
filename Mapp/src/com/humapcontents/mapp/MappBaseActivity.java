package com.humapcontents.mapp;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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

public class MappBaseActivity extends BaseActivity  {

	
	MappBaseActivity baseself;
	public int m_BottomBarStat = 2;
	
	int m_CurrIndex;
	
	private Animation	m_Init_Menu_Animation;
	private Animation	m_Bottom_Menu_Down_Animation;
	private Animation	m_Bottom_Menu_FullDown_Animation;
	private Animation	m_Bottom_Menu_Up_Animation;
	
	
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
		                intent = new Intent().setClass(baseself, HomeActivity.class);
		                startActivity( intent ); 					
					}
				}
					break;
				case R.id.bottom_2:
				{
					if ( m_CurrIndex != 1 )
					{
		            	Intent intent;
		                intent = new Intent().setClass(baseself, AuditionActivity.class);
		                startActivity( intent ); 
					}
				}
					break;
				case R.id.bottom_3:
				{
					if ( m_CurrIndex != 2 )
					{
						
						Intent intent;
		                intent = new Intent().setClass(baseself, AlbumActivty.class);
		                startActivity( intent ); 
					}
				}
					break;
				case R.id.bottom_4:
				{
					if ( m_CurrIndex != 3 )
					{
						
						Intent intent;
		                intent = new Intent().setClass(baseself, SquareMagazineActivty.class);
		                startActivity( intent ); 
						
					}
				}
					break;
				case R.id.bottom_5:
				{
					if ( m_BottomBarStat == 1 )
					{
						BottomMenuUp();
					}
					else if ( m_BottomBarStat == 2 )
					{
						BottomMenuDown(false);
					}
				}
					break;
				case R.id.bottom_6:
				{
					if ( m_CurrIndex != 4 )
					{
						Intent intent;
		                intent = new Intent().setClass(baseself, SearchActivity.class);
		                startActivity( intent ); 
						
					}
				}
					break;
				case R.id.bottom_7:
				{
					if ( m_CurrIndex != 5 )
					{
						AppManagement _AppManager = (AppManagement) getApplication();
						if (_AppManager.m_bLogin == false )
						{
							Toast.makeText(baseself
				                    .getApplicationContext(), 
				                    "로그인을 하지 않았습니다. 설정화면으로 이동합니다.",
				                    Toast.LENGTH_LONG).show();
				        	
				        	Intent intent;
				            intent = new Intent().setClass(baseself, SettingMainActivity.class);
				            startActivity( intent ); 
						}
						else
						{
							Intent intent;
			                intent = new Intent().setClass(baseself, MypageMainActivity.class);
			                startActivity( intent ); 
						}

					}
				}
					break;
					
				case R.id.bottom_8:
				{
					if ( m_CurrIndex != 6 )
					{
						
						Intent intent;
		                intent = new Intent().setClass(baseself, FavoriteActivity.class);
		                startActivity( intent ); 
						
					}
				}
					break;
				case R.id.bottom_9:
				{
					if ( m_CurrIndex != 7 )
					{
						Intent intent;
		                intent = new Intent().setClass(baseself, SettingMainActivity.class);
		                startActivity( intent ); 
					}
				}
					break;
				case R.id.bottom_10:
				{
					if ( m_CurrIndex != 9 )
					{
						Intent intent;
		                intent = new Intent().setClass(baseself, MappActivity.class);
		                startActivity( intent ); 
					}
					
				}
					break;
					
			}
		}
	};
		 


	
	

	
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
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
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
    
    
    public void ImageResizeListView( View parent , int id  ) 
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View box = (View)parent.findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutView(box);
    }
    
    public void AfterCreate( int Index)
    {
    	
    	{
            ImageResize(R.id.title_bar);
            ImageResize(R.id.title_name);
            ImageResize(R.id.title_icon);
            ImageResize(R.id.title_desc);
            
            
           // ImageResize3(R.id.bottom_menu);
            ImageResize2(R.id.bottom_menu_1);
            ImageResize2(R.id.bottom_menu_2);
            ImageResize2(R.id.bottom_1);
            ImageResize2(R.id.bottom_2);
            ImageResize2(R.id.bottom_3);
            ImageResize2(R.id.bottom_4);
            ImageResize2(R.id.bottom_5);
            ImageResize2(R.id.bottom_6);
            ImageResize2(R.id.bottom_7);
            ImageResize2(R.id.bottom_8);
            ImageResize2(R.id.bottom_9);
            ImageResize2(R.id.bottom_10);
            
            
            
          /* TextView textView = (TextView)findViewById(R.id.title_name);
            textView.setText("");
            final SpannableStringBuilder sp = new SpannableStringBuilder("Mapp");
            sp.setSpan(new ForegroundColorSpan(0xff0000ff), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.append(sp); */

    	}
    	
    	/*{
    		 m_BottomMenuBar = (LinearLayout)findViewById(R.id.bottom_menu);
    		 m_BottomMenuBar_1 = (LinearLayout)findViewById(R.id.bottom_menu_1);
    		 m_Init_Menu_Animation	= AnimationUtils.loadAnimation(getBaseContext(), R.anim.init_menu_down);
    		

    		 m_Init_Menu_Animation.setAnimationListener(new AnimationListener() {

    			    public void onAnimationStart(Animation animation) {

    			    }

    			    public void onAnimationRepeat(Animation animation) {
    			    }

    			    public void onAnimationEnd(Animation animation)
    			    {
    			    	((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.VISIBLE);
    			    	((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.GONE);
    					
    			    }
    			});
    		 
    		 m_Init_Menu_Animation.setFillAfter(false);
    		 m_BottomMenuBar.startAnimation(m_Init_Menu_Animation);
    		 
    		 
    		 m_Bottom_Menu_Down_Animation	= AnimationUtils.loadAnimation(getBaseContext(), R.anim.bottom_menu_down);
    		 m_Bottom_Menu_FullDown_Animation	= AnimationUtils.loadAnimation(getBaseContext(), R.anim.bottom_menu_full_down);
    		 m_Bottom_Menu_Up_Animation	= AnimationUtils.loadAnimation(getBaseContext(), R.anim.bottom_menu_up);
    		 
    		 
    		 m_Bottom_Menu_Down_Animation.setFillAfter(false);
    		 m_Bottom_Menu_FullDown_Animation.setFillAfter(false);
    		 m_Bottom_Menu_Up_Animation.setFillAfter(false);

    		 
    		 m_Bottom_Menu_Down_Animation.setAnimationListener(new AnimationListener() {

 			    public void onAnimationStart(Animation animation) {

 			    }

 			    public void onAnimationRepeat(Animation animation) {
 			    }

 			    public void onAnimationEnd(Animation animation) {
 			       
 			    	if ( m_BottomBarStat == 0 )
   			    	{
   			    		((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.GONE);
   				    	((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.GONE);
   			    	}
   			    	else if ( m_BottomBarStat == 1 )
   			    	{
   			    		((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.VISIBLE);
   				    	((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.GONE);
   			    	}
 			    	
 			    }
 			});
    		 
    		 m_Bottom_Menu_FullDown_Animation.setAnimationListener(new AnimationListener() {

  			    public void onAnimationStart(Animation animation) {

  			    }

  			    public void onAnimationRepeat(Animation animation) {
  			    }

  			    public void onAnimationEnd(Animation animation) {  
  			    	
  			    	((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.GONE);
			    	((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.GONE);

  			    }
  			});
    		 
    		 
    		 m_Bottom_Menu_Up_Animation.setAnimationListener(new AnimationListener() {

   			    public void onAnimationStart(Animation animation) {
   			    	
   			    	if ( m_BottomBarStat == 1 )
   			    	{
   			    		((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.VISIBLE);
   				    	((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.GONE);
   			    	}
   			    	else if ( m_BottomBarStat == 2 )
   			    	{
   			    		((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.VISIBLE);
   				    	((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.VISIBLE);
   			    	}
   			    	

   			    }

   			    public void onAnimationRepeat(Animation animation) {
   			    }

   			    public void onAnimationEnd(Animation animation) {
   			    	
   			    	
   			    	
   			    }
   			});
    		 

    		 
    	}*/
    	
    	
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
    	{
    		ImageView imageview = (ImageView)findViewById(R.id.bottom_6);
    		imageview.setOnClickListener(m_ClickListner);
    	}
    	{
    		ImageView imageview = (ImageView)findViewById(R.id.bottom_7);
    		imageview.setOnClickListener(m_ClickListner);
    	}
    	{
    		ImageView imageview = (ImageView)findViewById(R.id.bottom_8);
    		imageview.setOnClickListener(m_ClickListner);
    	}
    	{
    		ImageView imageview = (ImageView)findViewById(R.id.bottom_9);
    		imageview.setOnClickListener(m_ClickListner);
    	}
    	{
    		ImageView imageview = (ImageView)findViewById(R.id.bottom_10);
    		imageview.setOnClickListener(m_ClickListner);
    	}
    	
    	
		
    	m_CurrIndex = Index;
    	
    	switch ( m_CurrIndex )
    	{
	    	case 0:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_1);	
		    	image.setBackgroundResource(R.drawable.selcted_home_k_icon);
		    }
		    	break;
	    	case 1:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_2);	
		    	image.setBackgroundResource(R.drawable.selected_stage_k_icon);	
		    }
		    	break;
		    	
	    	case 2:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_3);	
		    	image.setBackgroundResource(R.drawable.selcted_album_k_icon);	
		    }
		    	break;
		    	
	    	case 3:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_4);	
		    	image.setBackgroundResource(R.drawable.selcted_squre_k_icon);	
		    }
		    	break;
	    	case 4:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_6);	
		    	image.setBackgroundResource(R.drawable.selcted_search_k_icon);	
		    }
		    	break;
	    	case 5:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_7);	
		    	image.setBackgroundResource(R.drawable.selcted_home_k_icon);	
		    }
		    	break;
	    	case 6:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_8);	
		    	image.setBackgroundResource(R.drawable.selcted_favorites_k_icon);	
		    }
		    	break;
	    	case 7:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_9);	
		    	image.setBackgroundResource(R.drawable.selcted_settings_k_icon);	
		    }
		    	break;
	    	case 9:
		    {
		    	ImageView image = (ImageView)findViewById(R.id.bottom_10);	
		    	image.setBackgroundResource(R.drawable.selected_logo_icon);	
		    }
		    	break;
    	}
    }
    
    public void BottomMenuUp()
    {
    	switch( m_BottomBarStat) 
    	{
    	case 0:
    	{
    		BottomMenuUp1();
    		m_BottomBarStat = 1;
    	}
    		break;
    	case 1:
    	{
    		BottomMenuUp2();
    		m_BottomBarStat = 2;
    	}
    		break; 
    	}
    }
    
    public void BottomMenuFullUp()
    {
    	BottomMenuUp2();
		m_BottomBarStat = 2;
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
    
    
    public void BottomMenuDown( Boolean bFullDown )
    {
    	switch( m_BottomBarStat) 
    	{
    	case 1:
    	{
    		BottomMenuDown1();
    		m_BottomBarStat = 0;
    	}
    		break;
    	case 2:
    	{
    		if ( bFullDown == false )
    		{
    			BottomMenuDown2();
    			m_BottomBarStat = 1;
    		}
    		else
    		{
    			BottomMenuDown3();
    			m_BottomBarStat = 0;
    		}
    	}
    		break; 
    	}
    }
    
    public void BottomMenuDefault()
    {
    	m_BottomBarStat = 1;
    	((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.VISIBLE);
	    ((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.GONE);
    }
    
    private void BottomMenuUp1()
    {
    	//m_BottomMenuBar.startAnimation(m_Bottom_Menu_Up_Animation);
    	((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.VISIBLE);
	    ((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.GONE);
    }
    
    private void BottomMenuUp2()
    {
    	//m_BottomMenuBar.startAnimation(m_Bottom_Menu_Up_Animation);
    	((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.VISIBLE);
	    ((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.VISIBLE);
    }
    
    
    
    // 하단 메뉴 한줄 떠있을 경우 없애기 
    private void BottomMenuDown1()
    {
		
		 //m_BottomMenuBar.startAnimation(m_Bottom_Menu_Down_Animation);
		 
		 ((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.GONE);
		  ((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.GONE);
    }
    
    // 하단 메뉴 2줄이 떠있을 경우 한줄 없애기 
    private void BottomMenuDown2()
    {
    	 //m_BottomMenuBar.startAnimation(m_Bottom_Menu_Down_Animation);
    	 
    	((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.VISIBLE);
 	    ((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.GONE);
    }
    
    // 하단 메뉴 2줄이 떠있을 경우 완전 없애기 
    private void BottomMenuDown3()
    {
    	 //m_BottomMenuBar.startAnimation(m_Bottom_Menu_FullDown_Animation);
    	 
    	 ((LinearLayout)findViewById(R.id.bottom_menu_1)).setVisibility(View.GONE);
		 ((LinearLayout)findViewById(R.id.bottom_menu_2)).setVisibility(View.GONE);
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
	
	
	public class ImageAdapter5 extends BaseAdapter {

		private String [] ImageList;
		private Context mContext;
		
		private Integer m_Width;
		private Integer m_Height;
		
		
		ImageAdapter5(Context c, String [] list , int width, int height){
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
			BitmapManager.INSTANCE.loadBitmap2(ImageList[position], imageView, m_Width, m_Height);
			return imageView;
			
		}
	}
	
}

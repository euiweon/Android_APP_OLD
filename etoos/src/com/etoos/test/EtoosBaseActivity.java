package com.etoos.test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.cookie.Cookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etoos.data.EventData;
import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.RecycleUtils;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;




public class EtoosBaseActivity extends BaseActivity 
{
	EtoosBaseActivity baseself;
	

	public int m_CurrIndex = 0;

	public static Typeface mTypeface;
	public static Typeface mTypefaceBold;
	
	public ProgressDialog mProgress;
	protected String TAG = "HotelJoinBaseActivity";
	
	private SharedPreferences sharedPref;
	
	BroadcastReceiver receiver;
	
	public boolean dataConnect = true;
	
	View ConnectView = null;
	@Override
	protected void onDestroy()
	{

		RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		System.gc();

		unregisterReceiver(receiver);
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
		
		CookieSyncManager.getInstance().stopSync();
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
		CookieSyncManager.getInstance().startSync();
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

	
	// 네트워크 상태 체크 
	public boolean checkNetwork() 
	{
		boolean result = true;
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		// boolean isWifiAvail = ni.isAvailable();
		boolean isWifiConn = ni.isConnected();
		ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// boolean isMobileAvail = ni.isAvailable();
		boolean isMobileConn = ni.isConnected();

		if (isWifiConn == false && isMobileConn == false) {
			result = false;
			Log.d("LOG", "네트워크가 연결되어있지 않습니다.");
		}
		else {
			Log.d("LOG", "네트워크가 정상적으로 연결되어있습니다.");

		}
		Log.i("LOG", "네트워크 상태 검사가 끝났습니다.");

		return result;
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
	
    public void DataConnect()
    {
    	
    	{
    		AppManagement _AppManager = (AppManagement) getApplication();
    		_AppManager.m_Connect = true;; 
    		
    		ConnectView.setVisibility(View.GONE);
    	}
    	
    }
    
    public void DataDisconnect()
    {
    	
    	{
    		AppManagement _AppManager = (AppManagement) getApplication();
    		_AppManager.m_Connect = false; 
    		ConnectView.setVisibility(View.VISIBLE);
    	}

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
        
        // 연결 상태 체크...
        {
        	IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        	receiver = new ConnReceiver(this);
        	registerReceiver(receiver, filter);
        }
        
        sharedPref = getSharedPreferences("cookie", Activity.MODE_PRIVATE);
        
		
		Log.e(TAG, "onCreate");
		
		{
        	AppManagement _AppManager = (AppManagement) getApplication();
        	_AppManager.activityList1.add(this);
        }
		
		 CookieSyncManager.createInstance(this);

		
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
    	AppManagement _AppManager = (AppManagement) getApplication();
        
 	   moveTaskToBack(true);
 	   for ( int i = 0; i < _AppManager.activityList1.size() ; i++ )
	    		 _AppManager.activityList1.get(i).finish();
 	   
 	  _AppManager.activityList1.clear();
	    	 
	   android.os.Process.killProcess(android.os.Process.myPid());
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
    
    
    
    public void ImageListResize(int id , ViewGroup view)
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	
    	View box = (View)view.findViewById(id);
    	Log.v("Type", box.getClass().getName() );
    	_AppManager.GetUISizeConverter().ConvertFrameLayoutView(box);
    }
    public void ImageListResize2(int id , ViewGroup view)
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View box = (View)view.findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutView(box);
    }
    
    public void ImageListResize3(int id , ViewGroup view)
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View box = (View)view.findViewById(id);
    	if ( box != null )
    		_AppManager.GetUISizeConverter().ConvertRelativeLayoutView(box);
    }
    
    public void TextListResize(int id , ViewGroup view)
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)view.findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertFrameLayoutTextView(box);
    }
    public void TextListResize2(int id, ViewGroup view )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)view.findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutTextView(box);
    }
    
    public void TextListResize3(int id , ViewGroup view)
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)view.findViewById(id);
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
            		if ( child.getId()  > 0 )
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
            		 
                	
                }
                else if ( index == 1 )
                {
                	if ( child.getId()  > 0 )
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
                	

                }
                else if ( index == 2 )
                {
                	if ( child.getId()  > 0 )
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
    
    
    void setListResize( int index , ViewGroup firstroot ,ViewGroup root , Boolean first )
    {
    	for (int i = 0; i < root.getChildCount(); i++) 
    	{
            View child = root.getChildAt(i);
           
            if ( first == false)
            {
            	if ( index == 0 )
                {
            		if ( child.getId()  > 0 )
            		{
            			if (child instanceof TextView)
	               		{
            				TextListResize3(child.getId() , firstroot);
	               			 
	               		}
	               		else
	               		{
	               			ImageListResize3(child.getId(), firstroot);
	               		}
            		}
            		 
                	
                }
                else if ( index == 1 )
                {
                	if ( child.getId()  > 0 )
                	{
                		if (child instanceof TextView)
    	           		{
                    		TextListResize2(child.getId(), firstroot);
                    		
    	           		}
    	           		else
    	           		{
    	           			ImageListResize2(child.getId(), firstroot);
    	           		}
                	}
                	

                }
                else if ( index == 2 )
                {
                	if ( child.getId()  > 0 )
                	{
                    	if (child instanceof TextView)
    	           		{
                    		TextListResize(child.getId(), firstroot);
                    		
    	           		}
                    	else if ( child instanceof CalendarView)
                    	{
                    		ImageListResize(child.getId(), firstroot);
                    	}
                    	
    	           		else
    	           		{
    	           			ImageListResize(child.getId(), firstroot);
    	           		}
                	}

                }
            }
            
            if (child instanceof LinearLayout)
            {
            	setListResize( 1 ,  firstroot ,(ViewGroup)child, false );
            }
                
            else if (child instanceof FrameLayout)
            {
            	if ( child instanceof CalendarView)
            	{
            		
            	}
            	else
            	{
            		setListResize( 2 ,  firstroot ,(ViewGroup)child, false );
            	}
            	
            }
            else if (child instanceof RelativeLayout)
            {
            	setListResize( 0 ,  firstroot ,(ViewGroup)child, false );
            } 
        }
    }
    
    public void ListViewColSize( ViewGroup view)
    {
    	AppManagement _AppManager = (AppManagement) getApplication();

    	// 최상위 뷰를 리사이즈 한다. 
    	/*if ( view != null )
    		_AppManager.GetUISizeConverter().ConvertListViewSize(view);
    	else
    		return;
    	*/
    	for (int i = 0; i < view.getChildCount(); i++) 
    	{
            View child = view.getChildAt(i);
            if (child instanceof LinearLayout)
            {
            	setListResize( 1 , view ,(ViewGroup)child, false );
            }
                
            else if (child instanceof FrameLayout)
            {
            	if ( child instanceof CalendarView)
            	{
            		
            	}
            	else
            	{
            		setListResize( 2 , view,(ViewGroup)child, false );
            	}
            	
            }
            else if (child instanceof RelativeLayout)
            {
            	setListResize( 0 , view,(ViewGroup)child, false );
            } 
        }
    }
    
    
    public void AfterCreate( )
    {
    	SetFont();
       
    	ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setResize( 2, root, true);
        
        {
        	ConnectView = (View) getLayoutInflater(). inflate(R.layout.connectfailed, null);
        	
        	root.addView(ConnectView);
        }
		
    }
    
    public void SetFont()
    {
    	if ( mTypeface == null)
    		mTypeface = Typeface.createFromAsset(getAssets(), "nanumgothic.ttf");
    	
    	if ( mTypefaceBold == null)
    		mTypefaceBold = Typeface.createFromAsset(getAssets(), "nanumgothicbold.ttf");
    	
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);
       
    }
    
    public void SetFont(View view)
    {
    	if ( mTypeface == null)
    		mTypeface = Typeface.createFromAsset(getAssets(), "nanumgothic.ttf");
    	
    	if ( mTypefaceBold == null)
    		mTypefaceBold = Typeface.createFromAsset(getAssets(), "nanumgothicbold.ttf");
    	
        
        setGlobalFont((ViewGroup)view);
       
    }
    
    public void SetWebViewCookie( String url)
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	CookieManager cookieManager = CookieManager.getInstance();

    	
    	List<Cookie> cookies  = _AppManager.GetHttpManager().GetCookies();
    	if (!cookies.isEmpty()) 
    	{
    		for (int i = 0; i < cookies.size(); i++) 
    		{
    			String cookieString = cookies.get(i).getName() + "=" + cookies.get(i).getValue();
    			cookieManager.setCookie(url, cookieString);
    		}
    	}
    }
   
    // 로그인이 성공적이였을경우 
    // 쿠키를 저장 
    // 
    
    public void SaveCookie( String url  )
    {
    
    	CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		
		// 쿠키 데이터를 가져온다. 
		String cookie = cookieManager.getCookie(url);
		String domain = "";
		String path = "";
	    Log.d("Cookie", url +  " cookie ------>"+cookie);
	    
	    // 문자열로 이루어진 데이터이기에 분할을 해준다. 
	    
	    cookie = cookie.replace(" " , "");		// 띄어쓰기 부터 제거
	    
	    String [] data = cookie.split(";");
	    
	    Map<String , String > cookieList = new HashMap<String , String>();
	    
	    
	    // 문자열 파싱 
	    // domain과 path는 따로 관리 ...
	    
	    for ( int i = 0; i < data.length ; i ++ )
	    {
	    	String [] key_value = data[i].split("=");
	    	if ( key_value[0].equals("domain") )
	    	{
	    		domain = key_value[1];
	    	}
	    	else if ( key_value[0].equals("path") )
	    	{
	    		path = key_value[1];
	    	}
	    	else
	    	{
	    		cookieList.put(key_value[0], key_value[1]);
	    	}
	    }
	    
	    // 쿠키 데이터 출력... 
	    for( String key : cookieList.keySet() )
	    {
	    	Log.d("Cookie",  "Key: "+key + "  Value : " + cookieList.get(key));
	    }
	    
	    // Cookie Save
	    SharedPreferences.Editor sharedEditor = sharedPref.edit();
	    for( String key : cookieList.keySet() )
	    {
	    	sharedEditor.putString(key, cookieList.get(key));
	    }
	    sharedEditor.putString("domain", domain);
	    sharedEditor.putString("path", path);
	    sharedEditor.commit();
    }
    
    // 이미 이전에 저장된 쿠키가 있을 경우 웹뷰에 입력해준다. 
    public void InitCookie()
    {
    	
    	// 웹뷰에 넣어 준다. 
    	CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		
    	Map<String, String> cookieList = (Map<String, String>)sharedPref.getAll();
    	
    	if ( cookieList != null )
    	{
    		// 데이터 출력 
        	for( String key : cookieList.keySet() )
    	    {
    	    	Log.d("Cookie11",  "Key: "+key + "  Value : " + cookieList.get(key));
    	    }
        	
        	try
        	{
        		
    			Map<String, String> cookieList2 = new HashMap<String, String >();
    			
    			String domain = null;
    			String path = null;
    			if (!cookieList.isEmpty()) 
    			{
    				for( String key : cookieList.keySet() )
    	        	{
    	        		if ( key.equals("domain") )
    	        		{
    	        			domain= cookieList.get(key);
    	        			if ( domain.equals(null)|| domain.equals(""))
    	        			{
    	        				
    	        			}
    	        			else
    	        			{
    	        				cookieList2.put(key, cookieList.get(key));
    	        			}
    	        			
    	        		}
    	        		else if ( key.equals("path")  )
    	        		{
    	        			path= cookieList.get(key);
    	        			if ( path.equals(null)|| path.equals(""))
    	        			{
    	        				
    	        			}
    	        			else
    	        			{
    	        				cookieList2.put(key, cookieList.get(key));
    	        			}
    	        			
    	        		}
    	        		else
    	        		{
    	        			cookieList2.put(key, cookieList.get(key));
    	        		}
    	        	}
    			}
    			
    			
            	if (!cookieList2.isEmpty()) 
            	{
            		for( String key : cookieList2.keySet() )
            	    {
            	        // cookie = cookies.get(i);
            	        String cookieString = key + "="
            	                    + cookieList2.get(key);
            	        Log.e("test", cookieString);
            	        cookieManager.setCookie("http://xx.xxx.xxx.com", cookieString);
            	    }
            	}
            	
            	CookieSyncManager.getInstance().sync();
        		
    			Thread.sleep(500);
    		} 
        	catch (InterruptedException e) 
    		{
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	
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
    
    

    @Override
	protected void onActivityResult(int requestCode, int resultcode, Intent data)
	{
    	AppManagement _AppManager = (AppManagement) getApplication();
		if ( requestCode == 1 )
		{
			if ( resultcode == 10 )
			{

				int callnum  = data.getIntExtra("return", -1);
				// 각자 타입에 따라서 parsing 하기 

				switch ( callnum )

				{
				case 1:
				{
					_AppManager.LogInDataParsing();
				}

				break;
				case 2:
				case 38:
					break;
				}

				Log.d("API Call Success", _AppManager.ParseString);
				//_AppManager.PParsingClass.LoginParsing(_AppManager.ParseString);

				// 에러메세지 받아서 성공여부 출력해서 그에 관한 처리를 해준다. 

			}

			else
			{
				Log.d("API Call failed", "강제로 네트워크 API 종료함");
			}

		}
		else
		{
			Log.w("error", "여긴 그냥 잘못 들어온것, 네트워크 문제이거나 그냥 코드 문제임으로 그냥 종료 시켜야함");
		}
	}


}

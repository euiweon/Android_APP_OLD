package com.example.hoteljoin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.euiweonjeong.base.KakaoLink;
import com.euiweonjeong.base.StoryLink;
import com.euiweonjeong.base.view.ObservableScrollView;
import com.euiweonjeong.base.view.ScrollViewListener;
import com.example.hoteljoin.HotelLocationActivity.MainMapFragement;
import com.example.hoteljoin.HotelSearchActivity.Destination;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;




import com.slidingmenu.lib.SlidingMenu;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class HotelDetail2Activity extends HotelJoinBaseActivity2 implements OnClickListener, ScrollViewListener{




	HotelDetail2Activity self;
	 SlidingMenu menu ;
	 int MenuSize;

	 
	public class DetailListData
	{
		DetailListData()
		{
			
		}

			
		String hotelCode;
		String hotelName;
		String hotelNameEn;
		String nationCode;
		String nationName;
		String nationNameEn;
		String cityCode;
		String cityName;
		String cityNameEn;
		String starRating;
		String checkinTime;
		String checkoutTime;
		String roomCount;
		String latitude;
		String longitude;
		String address;
		String price;
		String numOfDiary;
		String numOfReview;
		String rating;
		String description;
		String traffic;
		String event;
	}
	
	public class ImageData
	{
		String primaryYn;
		String smallImageUrl;
		String middleImageUrl;
	}
	
	public class FacilityData
	{
		String typeCode;
		String facilityCode;
		String facilityName;

	}

	 private HashMap<Marker, EventInfo> eventMarkerMap; 
	 private MainMapFragement mapFragment; 
	 
	 public class MainMapFragement extends MapFragment
	 {   
		 public String hotelCode;
		 public String hotelName;
		 public String starRating;
		 public String latitude;
		 public String longitude;
		 public String price;


		 public Marker placeMarker(EventInfo eventInfo) 
		 {    
			 Marker m  = mMap.addMarker(new MarkerOptions()
			 	.position(eventInfo.pos)      
			 	.title(eventInfo.title)
			 	.snippet(eventInfo.price)
			 	.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot)));
			 return m;    
		 }   
	 }
		
	public Boolean m_bFooter = true;

	
	private GoogleMap mMap;

	
	String m_MainimageURL;
	
	 
	 Boolean locationcheck = false;;
	 LocationManager locationmanager;
	 
	 DetailListData m_HotelInfo = new DetailListData();
	 
	 Facebook facebook = new Facebook("489115824472243");
	 
	 ArrayList <ImageData > m_ImageData = new  ArrayList <ImageData >();
	 ArrayList <FacilityData > m_FacilityData = new  ArrayList <FacilityData >();
	 
	 private SharedPreferences mPrefs;
	 
	ViewPager vp_main = null;	//ViewPager
	CustomPagerAdapter cpa = null;	//커스텀 어댑터
	 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hoteldetailview_main);
		
		self = this;

		{
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }


		mapFragment = new MainMapFragement(); 
		
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.hotel_map)).getMap();
		//mMap = ((M)findViewById(R.id.title_logo)
		
		
		eventMarkerMap = new HashMap<Marker, EventInfo>(); 
		
		{
			CameraPosition sydney = new CameraPosition.Builder().target(new LatLng(37.5666091, 126.978371)).zoom(15.5f)
						.bearing(0).tilt(0).build();
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(sydney));
			
			
			
			// Adding and showing marker while touching the GoogleMap        
			mMap.setOnMapClickListener(new OnMapClickListener() 
			{             
				public void onMapClick(LatLng arg0) 
				{                
					// 다음
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.m_HotelLng =  m_HotelInfo.longitude;
					_AppManager.m_HotelLat =  m_HotelInfo.latitude;
					_AppManager.m_HotelName = m_HotelInfo.hotelName;
					
					Intent intent;
		            intent = new Intent().setClass(baseself, HotelLocationActivity.class);
		            startActivity( intent ); 
					
				}
			});
		}
		{
			UiSettings setting = mMap.getUiSettings();
			setting.setScrollGesturesEnabled(false);
			setting.setAllGesturesEnabled(false);
			setting.setZoomControlsEnabled(false);

		}
		
		
		
		m_ImageData.clear();
		m_FacilityData.clear();
		
		
		ObservableScrollView sv = (ObservableScrollView) findViewById(R.id.scrollview_1);
		sv.setScrollViewListener( this);
		


		setTextFont( R.id.title_logo);
		setTextFont(R.id.hotel_name);
		setTextFont(R.id.tab_1_text);
		setTextFont(R.id.tab_2_text);
		
		
		ImageResize(R.id.scrollview_1);
		ImageResize(R.id.hotel_bar);
		TextResize(R.id.hotel_name);
		TextResize(R.id.tab_1_text);
		TextResize(R.id.tab_2_text);
		ImageResize(R.id.hotel_image);
		ImageBtnResize(R.id.hotel_reser);
		
		ImageBtnResize(R.id.tab_1);
		ImageBtnResize(R.id.tab_2);
		ImageBtnResize(R.id.tab_3);
		ImageBtnResize(R.id.tab_4);
		ImageResize(R.id.hotel_map);
		ImageBtnResize(R.id.hotel_etc1);
		ImageBtnResize(R.id.hotel_etc2);
		ImageBtnResize(R.id.hotel_etc3);
		ImageBtnResize(R.id.hotel_etc4);
		ImageResize(R.id.hotel_addr);
		
		
		ImageResize(R.id.price_bar);
		TextResize(R.id.price);
		TextResize(R.id.hotel_addr_text);
		
		
		
		
		
		
		
		AfterCreate(1, false);
		
		{
        	mPrefs = getSharedPreferences( "facebooks" ,MODE_PRIVATE);
            
            String access_token = mPrefs.getString("access_token", null);
     
            long expires = mPrefs.getLong("access_expires", 0);
     
            if(access_token != null) {
     
                facebook.setAccessToken(access_token);
     
            }
     
            if(expires != 0) {
     
                facebook.setAccessExpires(expires);
     
            }
        }
		
		
		{
			vp_main = (ViewPager) findViewById(R.id.hotel_image);
			cpa = new CustomPagerAdapter();
			
			vp_main.setAdapter(cpa);
			 //ViewPage 페이지 변경 리스너
	        vp_main.setOnPageChangeListener(new OnPageChangeListener()
	        {

				@Override
				public void onPageScrollStateChanged(int state) {}

				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

				@Override
				public void onPageSelected(int position) 
				{
					//페이지가 변경될때 변경되는 페이지 포지션에 대한 체크(버튼을 활성화/비활성화 시켜줌)
					//pageCheck(position);				
				}
	        	
	        });
	        
	        
	        vp_main.setOnTouchListener(new OnTouchListener()
	        {

	        	public boolean onTouch(View v, MotionEvent event) {
	        	    switch (event.getAction()) 
	        	    {
	        	    case MotionEvent.ACTION_MOVE: 
	        	    	vp_main.requestDisallowInterceptTouchEvent(true);
	        	        break;
	        	    case MotionEvent.ACTION_UP:
	        	    case MotionEvent.ACTION_CANCEL:
	        	    	vp_main.requestDisallowInterceptTouchEvent(false);
	        	        break;
	        	    }
	        	    return false;
	        	}
	
	        }
	        );
	        
	        vp_main.setCurrentItem(0);	        
		}
		

		GetData();
		
	}
	
	public void onScrollChanged(ObservableScrollView sv, int x, int y, int oldx, int oldy)
	{
	    sv.setVisibility(View.GONE);
	    sv.setVisibility(View.VISIBLE);
	}
	
	public void BtnEvent( int id )
    {
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
    }

	
	public void RefreshUI()
	{
		/*{
			ImageView Image = (ImageView)findViewById(R.id.hotel_image);
			
			Image.setTag( m_MainimageURL);;
			BitmapManager.INSTANCE.loadBitmap(m_MainimageURL, Image, 480, 249);
		}*/
		
		for ( int i = 0; i < m_ImageData.size() ; i++)
		{
			if ( m_ImageData.get(i).primaryYn.equals("1"))
				vp_main.setCurrentItem(i);	
		}
		
		((TextView)findViewById(R.id.title_logo)).setText(m_HotelInfo.hotelName);
		((TextView)findViewById(R.id.tab_1_text)).setText("("+ m_HotelInfo.numOfReview+ ")");
		((TextView)findViewById(R.id.tab_2_text)).setText("("+ m_HotelInfo.numOfDiary+ ")");
		((TextView)findViewById(R.id.hotel_addr_text)).setText( m_HotelInfo.address );
		
		
		
		
		
		{
			DecimalFormat df = new DecimalFormat("#,##0");

			
			((TextView)findViewById(R.id.price)).setText("￦ " + df.format(Double.parseDouble(m_HotelInfo.price)) );
		}
		
		CameraPosition sydney = new CameraPosition.Builder().target(new LatLng(Double.parseDouble(m_HotelInfo.latitude), Double.parseDouble(m_HotelInfo.longitude)))
				.zoom(15.5f).bearing(0).tilt(0).build();
		
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(sydney));
		
		
		{
			AppManagement _AppManager = (AppManagement) getApplication();


			
			 EventInfo item = new EventInfo();


			item.pos = new LatLng(Double.parseDouble(m_HotelInfo.latitude), Double.parseDouble(m_HotelInfo.longitude));
			item.title = m_HotelInfo.hotelName;
			
			//((TextView)findViewById(R.id.title_logo)).setText(item.hotelName);
				
			Marker firstMarker = mapFragment.placeMarker(item); 
			eventMarkerMap.put(firstMarker, item); 
		}
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_GallayList.clear();
			
			for ( int i = 0 ; i < m_ImageData.size() ; i++ )
			{
				_AppManager.m_GallayList.add(m_ImageData.get(i).middleImageUrl);
			}
		}

	}

	
	
	public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	public void ImageBtnResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	public void AddMarker( EventInfo info)
	{
		Marker firstMarker = mapFragment.placeMarker(info);
		eventMarkerMap.put(firstMarker, info);     
	}
	public void ClearMarker()
	{
		eventMarkerMap.clear();
	}
	
	
	
	public void GetData()
	{

		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				Map<String, String> data = new HashMap  <String, String>();
				
				data.put("hotelCode", _AppManager.m_HotelCode);

				
				
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/search/searchHotelDetail.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						JSONObject object = (JSONObject)json.get("hotelInfo");
						
						{
							
							
							_AppManager.m_CheckInDay = object.optString("checkinDay");
							_AppManager.m_NumRoom = object.optString("numOfRooms");
							_AppManager.m_Duration = object.optString("duration");
							_AppManager.m_NumPer = object.optString("numPerRoom");
							m_HotelInfo.hotelCode =  object.optString("hotelCode");
							m_HotelInfo.hotelName =  object.optString("hotelName");
							
							// 
							m_HotelInfo.longitude =  object.optString("longitude");
							m_HotelInfo.latitude =  object.optString("latitude");
							
							
							double  longitude = Double.parseDouble(m_HotelInfo.longitude );
							double  latitude = Double.parseDouble(m_HotelInfo.latitude );
							
							_AppManager.m_SearchWorld  = true;
							if (latitude > 32.0 && latitude < 40.0 )
							{
								if (longitude > 124.0 && longitude < 131.0 )
								{
									_AppManager.m_SearchWorld = false;
								}
							}
							
							if ( _AppManager.m_SearchWorld == false)
							{
								m_HotelInfo.hotelNameEn =  object.optString("hotelNameEn");

							}
							else
							{
								m_HotelInfo.hotelNameEn = m_HotelInfo.hotelName; 
							}
							
							m_HotelInfo.nationCode =  object.optString("nationCode");
							m_HotelInfo.nationName =  object.optString("nationName");
							m_HotelInfo.nationNameEn =  object.optString("nationNameEn");
							m_HotelInfo.cityCode =  object.optString("cityCode");
							m_HotelInfo.cityName =  object.optString("cityName");
							m_HotelInfo.cityNameEn =  object.optString("cityNameEn");
							m_HotelInfo.starRating =  object.optString("starRating");
							m_HotelInfo.checkinTime =  object.optString("checkinTime");
							m_HotelInfo.checkoutTime =  object.optString("checkoutTime");
							m_HotelInfo.roomCount =  object.optString("roomCount");
							
							m_HotelInfo.numOfReview =  object.optString("numOfReview");
							
							
							m_HotelInfo.address =  object.optString("address");
							m_HotelInfo.price =  object.optString("price");
							m_HotelInfo.numOfDiary =  object.optString("numOfDiary");
							
							m_HotelInfo.rating =  object.optString("rating");
							m_HotelInfo.traffic =  object.optString("traffic");
							m_HotelInfo.description =  object.optString("description");
							m_HotelInfo.event =  (object.optString("event"));
							
							
							_AppManager.m_writeNationCode = m_HotelInfo.nationCode ;
							_AppManager.m_writeCityCode = m_HotelInfo.cityCode;

							
							
						}
						
						JSONArray usageList = (JSONArray)json.get("imageList");
						for(int i = 0; i < usageList.length(); i++)
						{
							
							{
								ImageData item = new ImageData();
								JSONObject list = (JSONObject)usageList.get(i);
								item.primaryYn =  list.optString("primaryYn");
								item.smallImageUrl =  list.optString("smallImageUrl");
								item.middleImageUrl =  list.optString("middleImageUrl");

								if ( item.primaryYn.equals("1"))
								{
									m_MainimageURL = item.middleImageUrl;
								}
								//if ( m_ImageData.size() < 5 )
									m_ImageData.add(item);
							}

						}
						
						//if ( json.get("facilityList") != null)
						{
							JSONArray usageList2 = (JSONArray)json.optJSONArray("facilityList");
							if ( usageList2 != null)
							{
								for(int i = 0; i < usageList2.length(); i++)
								{
									FacilityData item = new FacilityData();
									JSONObject list = (JSONObject)usageList2.get(i);
									item.typeCode =  list.optString("typeCode");
									item.facilityCode =  list.optString("facilityCode");
									item.facilityName =  list.optString("facilityName");

									m_FacilityData.add(item);
								}
							}
							
							
						}

						
						handler.sendEmptyMessage(0);

					}
					else 
					{
						// 에러 메세지를 전송한다. 
						handler.sendMessage(handler.obtainMessage(1,json.getString("errorMsg")) );
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler.sendMessage(handler.obtainMessage(1,"json Data Error \n" + e.getMessage() ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	public void GetZZim()
	{

		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				EditText searchText = (EditText)findViewById(R.id.search_text_1);
				Map<String, String> data = new HashMap  <String, String>();



				data.put("memberId", _AppManager.m_LoginID);
				data.put("hotelCode", m_HotelInfo.hotelCode);
				data.put("hotelName",m_HotelInfo.hotelName);
				data.put("nationCode", m_HotelInfo.nationCode);

					

		
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/bookmark/bookmarkAdd.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
	
						
						handler.sendEmptyMessage(30);

					}
					else 
					{
						// 에러 메세지를 전송한다. 
						handler.sendMessage(handler.obtainMessage(1,json.getString("errorMsg")) );
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler.sendMessage(handler.obtainMessage(1,"Error" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
			{

				RefreshUI();
				
			}
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 2:

				break;
				
			case 3:
	
				break;
			case 10:
			{

			}
				break;
			case 11:
			{

			}
				break;
			case 20:
				break;
			case 30:
				self.ShowAlertDialLog( self ,"찜하기" , "찜이 되었습니다." );
				break;
			default:
				break;
			}

		}
    	
	};
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		

		switch(v.getId())
		{
		case R.id.hotel_image:
		{
			Intent intent;
            intent = new Intent().setClass(baseself, HotelImageActivity.class);
            startActivity( intent );
		}
			break;
		case R.id.hotel_reser:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			if (_AppManager.m_SearchWorld)
			{
				Intent intent;
	            intent = new Intent().setClass(baseself, Reservation1Activity.class);
	            startActivity( intent ); 
			}
			else
			{
				Intent intent;
	            intent = new Intent().setClass(baseself, Reservation1Activity.class);
	            startActivity( intent ); 
			}
			//self.ShowAlertDialLog( self ,"예약" , m_HotelInfo.hotelName + "  예약하기 " );
		}
			break;
		case R.id.tab_1:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelName = m_HotelInfo.hotelName;
			Intent intent;
            intent = new Intent().setClass(baseself, ReviewActivity.class);
            startActivity( intent ); 
			
		}
			break;
		case R.id.tab_2:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_UsbHotelcode= false; 
			_AppManager.m_CityCode = m_HotelInfo.cityCode;
			_AppManager.m_NationCode = m_HotelInfo.nationCode;
			Intent intent;
            intent = new Intent().setClass(baseself, TravelActivity2.class);
            startActivity( intent ); 
			//self.ShowAlertDialLog( self ,"여행일지" , m_HotelInfo.hotelName + "  관련여행일지 보기 " );
		}
			break;
		case R.id.tab_3:
		{
			String []	NationList = {"카카오톡으로 보내기", "카카오 스토리로 보내기", "페이스북으로 보내기"};
			
			
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	        alt_bld.setTitle("전송방법 선택");
	        alt_bld.setSingleChoiceItems(NationList, -1, new DialogInterface.OnClickListener() 
	        {
	            public void onClick(DialogInterface dialog, int item) 
	            {
	            	if (item == 2 )
	            	{
	            		facebook.dialog(
	            			    self,
	            			    "stream.publish", 
	            			    new MyDialogListener());   


	            	}
	            	else if (item == 0)
	            	{
	            		KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());

	            		// check, intent is available.
	            		if (!kakaoLink.isAvailableIntent()) {
	            			self.ShowAlertDialLog(self, "에러", "카카오톡이 설치 되어 있지 않습니다.");			
	            		}
	            		else
	            		{
		            		// Text 전송할 때
		            		Intent intent = new Intent(Intent.ACTION_SEND);
		            		intent.setType("text/plain");
		            		intent.putExtra(Intent.EXTRA_SUBJECT, m_HotelInfo.hotelName);
		            		intent.putExtra(Intent.EXTRA_TEXT, m_HotelInfo.description);

		            		// KakaoTalk으로 바로 보내시려면 아래 코드를 추가합니다.
		            		intent.setPackage("com.kakao.talk");
		            		startActivity(intent);
	            		}
	            		
	            	}
	            	else if ( item == 1)
	            	{
	            		AppManagement _AppManager = (AppManagement) getApplication();
	            		Map<String, Object> urlInfoAndroid = new Hashtable<String, Object>(1);
	            		urlInfoAndroid.put("title", _AppManager.m_HotelName);
	            		urlInfoAndroid.put("desc",m_HotelInfo.description);
	            		urlInfoAndroid.put("imageurl", new String[] {m_MainimageURL});
	            		urlInfoAndroid.put("type", "article");
	            		

	            		// Recommended: Use application context for parameter.
	            		StoryLink storyLink = StoryLink.getLink(getApplicationContext());

	            		// check, intent is available.
	            		if (!storyLink.isAvailableIntent()) {
	            			self.ShowAlertDialLog(self, "에러", "카카오스토리가 설치 되어 있지 않습니다.");			
	            		
	            		}
	            		else
	            		{
	            			/**
		            		 * @param activity
		            		 * @param post (message or url)
		            		 * @param appId
		            		 * @param appVer
		            		 * @param appName
		            		 * @param encoding
		            		 * @param urlInfoArray
		            		 */
		            		try {
								storyLink.openKakaoLink(self, 
										"http://hoteljoin.com",
										getPackageName(), 
										getPackageManager().getPackageInfo(getPackageName(), 0).versionName, 
										"Hotel Join",
										"UTF-8", 
										urlInfoAndroid);
							} catch (NameNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	            		}

	            		
	            	}
	            	else
	            	{
	            		AppManagement _AppManager = (AppManagement) getApplication();
		            	_AppManager.m_SNSSelect= item; 
		            	_AppManager.m_MainimageURL = m_MainimageURL;
		            	_AppManager.m_HotelName = m_HotelInfo.hotelName;
		            	Intent intent;
			            intent = new Intent().setClass(baseself, HotelSNSActivity.class);
			            startActivity( intent );
	            	}
	            	
	            	dialog.cancel();
	            }
	        });
	        AlertDialog alert = alt_bld.create();
	        alert.show();
			
		}
			break;
		case R.id.tab_4:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			if ( _AppManager.m_LoginCheck == true)
			{
				GetZZim();
				//self.ShowAlertDialLog( self ,"찜하기" , m_HotelInfo.hotelName + " 찜하기메뉴로 " );
			}
			else
			{
				Intent intent;
	            intent = new Intent().setClass(baseself, LoginActivity.class);
	            startActivity( intent );
				//self.ShowAlertDialLog( self ,"찜하기" , m_HotelInfo.hotelName + "로그인이 안되었으니 로그인 메뉴로" );
			}
				
		}
			break;
		case R.id.hotel_etc1:
		{
			self.ShowAlertDialLog( self ,"호텔특전사항" , m_HotelInfo.hotelName + "호텔 특전사항" );
		}
			break;
		case R.id.hotel_etc2:
		{
			self.ShowAlertDialLog( self ,"호텔소개" , m_HotelInfo.hotelName + "\n"+ m_HotelInfo.description );
		}
			break;
		case R.id.hotel_etc3:
		{
			String sisul ="";
			
			for ( int i = 0 ; i < m_FacilityData.size() ; i++ )
			{
				sisul += m_FacilityData.get(i).facilityName;
				sisul += "\n";

			}
			self.ShowAlertDialLog( self ,"부대시설" , sisul );
			
		}
			break;
		case R.id.hotel_etc4:
		{
			self.ShowAlertDialLog( self ,"교통사항" , m_HotelInfo.traffic );
		}
			break;
					
		}
		
	}
	
	public class MyDialogListener implements DialogListener {

	    public void onComplete(Bundle values) {

	        final String postId = values.getString("post_id");

	        if (postId != null) {

	            // "Wall post made..."

	        } else {
	            // "No wall post made..."
	        }

	    }

	    public void onFacebookError(FacebookError e) {}
	    public void onError(DialogError e) {}
	    public void onCancel() {}

	}
	
	
	
	private class CustomPagerAdapter extends PagerAdapter{

        
        @Override
        public int getCount() {
                return self.m_ImageData.size();
        }

        /**
         * 각 페이지 정의
         */
        @Override
        public Object instantiateItem(View collection, int position)
        {
        	ImageView img = new ImageView(self); //this is a variable that stores the context of the activity
            //set properties for the image like width, height, gravity etc...



            img.setTag( self.m_ImageData.get(position).middleImageUrl);;
			BitmapManager.INSTANCE.loadBitmap_2(self.m_ImageData.get(position).middleImageUrl, img);
            //img.setImageResource(resId); //setting the source of the image
            
            OnClickListener ol  = new OnClickListener()
            {

				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					Intent intent;
		            intent = new Intent().setClass(baseself, HotelImageActivity.class);
		            startActivity( intent );

                }
					           
            };
            img.setOnClickListener(ol);
            
            ((ViewPager) collection).addView(img,0);  

            return img;

        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
                ((ViewPager) collection).removeView((View) view);
        }

        
        
        @Override
        public boolean isViewFromObject(View view, Object object) {
                return view==((View)object);
        }

        
        @Override
        public void finishUpdate(View v) {
        }
        

        @Override
        public void restoreState(Parcelable pc, ClassLoader cl) {
        }

        @Override
        public Parcelable saveState() {
                return null;
        }

        @Override
        public void startUpdate(View v) {
        }

    }
    


	
}

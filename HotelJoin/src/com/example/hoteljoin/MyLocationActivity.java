package com.example.hoteljoin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyLocationActivity extends HotelJoinBaseActivity2 implements OnClickListener{
	MyLocationActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 
	 
	 Double m_lat;
	 Double m_lng;
	 
	 private GoogleMap mMap;
	 private Criteria criteria;
	 
	 Boolean locationcheck = false;;
	 LocationManager locationmanager;
	 
	 String CityName;
	 String CityCode;
	 
	 private HashMap<Marker, EventInfo> eventMarkerMap; 
	 private MainMapFragement mapFragment; 
	 
	 
	 public ArrayList<EventInfo>  m_ArrayList = new ArrayList<EventInfo>();
	 
	 Calendar calendar = Calendar.getInstance();
	 
	 
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
	 
	 
	 Integer day;
	 Integer month;
	 Integer year;
	 
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylocate);
		
		m_ArrayList.clear();

		{
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
		
        

		mapFragment = new MainMapFragement(); 
		AfterCreate(2 , false);
		
		
		setTextFont( R.id.title_logo);
		
		ImageBtnResize(R.id.locate_btn);
		

		
		
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_view)).getMap();
		
		
		
		{
			CameraPosition sydney = new CameraPosition.Builder().target(new LatLng(37.5666091, 126.978371)).zoom(15.5f)
						.bearing(0).tilt(0).build();
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(sydney));
		}
		
		
		
        String context = Context.LOCATION_SERVICE;
        locationmanager=(LocationManager)getSystemService(context);
        
        if(!locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) 
        {
            alertCheckGPS();
        }

        
        eventMarkerMap = new HashMap<Marker, EventInfo>(); 
       
        ClearMarker();
		criteria=new Criteria();
		GetLocation();
		 
		 
		 
		 mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
		 {             
			 @Override     
			 public void onInfoWindowClick(Marker marker) 
			 {       
				 EventInfo eventInfo = eventMarkerMap.get(marker);
				 
				 // 호텔 상세정보 페이지 가기
				 Toast.makeText(getBaseContext(),
						  eventInfo.title + " ID " + eventInfo.hotelCode, 
						  Toast.LENGTH_LONG).show(); 
				 
				 
					{
						AppManagement _AppManager = (AppManagement) getApplication();
						

						
						_AppManager.m_SearchWorld = false;
						
						_AppManager.m_HotelCode = eventInfo.hotelCode;
						Intent intent;
			            intent = new Intent().setClass(baseself, HotelDetail2Activity.class);
			            startActivity( intent ); 

					}
					
					
			 }    
		 });   
		 UiSettings settings = mMap.getUiSettings();
		 mMap.setMyLocationEnabled(true);
		 settings.setCompassEnabled(true);
		 
		 GetMyLocationInfo();

	}
	
	
	private void GetMyLocationInfo()
	{
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mMap.clear();
		eventMarkerMap.clear();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				


				Map<String, String> data = new HashMap  <String, String>();

				data.put("latitude", m_lat.toString());
				data.put("longitude", m_lng.toString());
				data.put("distance", "15");

				
				// 내주변 호텔 목록 ( 아직 API 없음 )
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/search/searchHotelNearbyList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{

						
						CityCode = _AppManager.GetHttpManager().DecodeString(json.optString("cityCode"));
						CityName = _AppManager.GetHttpManager().DecodeString(json.optString("cityName"));
						
						JSONArray usageList = (JSONArray)json.get("hotelList");
						
						// 주변에 호텔 데이터를 얻는다. 
						for(int i = 0; i < usageList.length(); i++)
						{
							EventInfo item = new EventInfo();
							JSONObject list = (JSONObject)usageList.get(i);
							
							item.hotelCode = _AppManager.GetHttpManager().DecodeString(list.optString("hotelCode"));
							item.hotelName =  _AppManager.GetHttpManager().DecodeString(list.optString("hotelName"));
							item.starRating = _AppManager.GetHttpManager().DecodeString(list.optString("starRating"));
							item.latitude =  _AppManager.GetHttpManager().DecodeString(list.optString("latitude"));
							item.longitude =  _AppManager.GetHttpManager().DecodeString(list.optString("longitude"));
							item.price =  _AppManager.GetHttpManager().DecodeString(list.optString("price"));
							
							{
								DecimalFormat df = new DecimalFormat("#,##0");

								item.price = "￦ " + df.format(Double.parseDouble(item.price)) ;
							}
							
							item.pos = new LatLng(Double.parseDouble(item.latitude), Double.parseDouble(item.longitude));
							item.title = item.hotelName;
							m_ArrayList.add(item);
							
						}

						handler.sendEmptyMessage(0);
					}
					else 
					{
						// 에러 메세지를 전송한다. 
						handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.getString("errorMsg")) ));
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
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				
			{
				for ( int i = 0 ; i < m_ArrayList.size() ; i++ )
				{
					Marker firstMarker = mapFragment.placeMarker(m_ArrayList.get(i)); 
					eventMarkerMap.put(firstMarker, m_ArrayList.get(i)); 
				}
				
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
			case 20:
				break;
			default:
				break;
			}

		}
    	
	};
	
	
	
	private void updateWithNewLocation(Location location) {

        String latlng = "";
        if(location!=null && locationcheck == false)

        {
        	locationcheck = true;
        	m_lat = location.getLatitude();
                m_lng = location.getLongitude();
                latlng = "위도 : "+m_lat+" \n경도 : "+m_lng;
                
                CameraPosition sydney = new CameraPosition.Builder().target(new LatLng(m_lat, m_lng)).zoom(15.5f)
						.bearing(0).tilt(0).build();
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(sydney));

			mMap.clear();
        }
        else if(location!=null && locationcheck == true)
        {
        	m_lat = location.getLatitude();
            m_lng = location.getLongitude();
            latlng = "위도 : "+m_lat+" \n경도 : "+m_lng;
        }

        else

        {
                latlng="위치를 찾을수 없습니다.";
        }


	}
	
	
	
	private void alertCheckGPS() 
	{
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS가 꺼져있습니다. GPS가 꺼져있을 경우 위치 탐색이 되질 않습니다. \n GPS를 켜시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveConfigGPS();
                            }
                    })
                .setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                    });
        AlertDialog alert = builder.create();
        alert.show();
    }

   // GPS 설정화면으로 이동
    private void moveConfigGPS() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }
    
	

    public void GetLocation()
    {
    	locationcheck = false;
    	
    	criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        
        String provider = locationmanager.getBestProvider(criteria, true);

        Location location = locationmanager.getLastKnownLocation(provider);


        ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        // GPS 
        if(!locationmanager.isProviderEnabled(provider)&&locationmanager.getLastKnownLocation(provider)!=null)
        {
        	updateWithNewLocation(location);
        	locationmanager.requestLocationUpdates(provider, 1000, 10, locationListener);     
        	//Toast.makeText(getApplicationContext(),"gps 이용가능",Toast.LENGTH_SHORT).show();
           
        } 
        // GPS 연결 정보 얻어오는게 늦었고, Wifi가 연결되어 있을 경우 
        else if ( isWifi )
        {
        	criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        	provider = locationmanager.getBestProvider(criteria, true);
        	location = locationmanager.getLastKnownLocation(provider);
        	updateWithNewLocation(location);
        	locationmanager.requestLocationUpdates(provider, 1000, 10, locationListener);
        	//Toast.makeText(getApplicationContext(),"gps이용불가, 네트워크로 추적",Toast.LENGTH_SHORT).show();
        }
        // GPS도 안되고 Wifi도 안 될경우 위치 추적 실패 떠야함. 
        else
        {
        	//Toast.makeText(getApplicationContext(),"gps이용불가 Wifi 이용 불가 ",Toast.LENGTH_SHORT).show();
        }


    }

	

	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	public void BtnEvent( int id )
    {
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
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
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.locate_btn:
			GetLocation();
			break;

		}
		
	}
	
	
	
	
	
	private final LocationListener locationListener = new LocationListener() {

        

        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

                //위치 프로바이더의 하드웨어 상태가 변경된 경우 어플리케이션을 업데이트한다.

                

        }

        

        public void onProviderEnabled(String arg0) {

                //위치 프로바이더가 활성화된 경우 애플리케이션을 업데이트 한다.

                

        }

        

        public void onProviderDisabled(String arg0) {

                //위치 프로바이더가 비활성화된 경우 애플리케이션을 업데이트 한다. 

                updateWithNewLocation(null);

                

        }

        

        public void onLocationChanged(Location location) {

                //새로운 위치를 기반으로 애플리케이션을 업데이트 한다.

                updateWithNewLocation(location);

                

        }

	};
    

}

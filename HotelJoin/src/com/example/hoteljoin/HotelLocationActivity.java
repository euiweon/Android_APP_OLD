package com.example.hoteljoin;

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

public class HotelLocationActivity extends HotelJoinBaseActivity2 implements OnClickListener{
	HotelLocationActivity  self;
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
	 
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylocate);
		
		

		{
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
		
        

		mapFragment = new MainMapFragement(); 
		AfterCreate(1 , false);
		
		
		setTextFont( R.id.title_logo);
		
		ImageBtnResize(R.id.locate_btn);
		
		
		
		

		
		((View)findViewById(R.id.sub_bar)).setVisibility(View.GONE);
		//mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_view)).getMap();
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_view)).getMap();
		
		AppManagement _AppManager = (AppManagement) getApplication();
		//_AppManager.m_HotelSearchListData =0;
		eventMarkerMap = new HashMap<Marker, EventInfo>(); 
		for ( int i = 0;  i < _AppManager.m_HotelSearchListData.size() ; i++ )
		{
			if ( i == 0 )
			{
				CameraPosition sydney = new CameraPosition.Builder().target(new LatLng
						(Double.parseDouble(_AppManager.m_HotelSearchListData.get(i).latitude),
						Double.parseDouble(_AppManager.m_HotelSearchListData.get(i).longitude)))
						.zoom(15.5f).bearing(0).tilt(0).build();
				
				
				mMap.animateCamera(CameraUpdateFactory.newCameraPosition(sydney));
			}
			
			EventInfo item = new EventInfo();

			item.hotelName = _AppManager.m_HotelSearchListData.get(i).hotelName;
			item.latitude =  _AppManager.m_HotelSearchListData.get(i).latitude;
			item.longitude =  _AppManager.m_HotelSearchListData.get(i).longitude;	
			item.pos = new LatLng(Double.parseDouble(item.latitude), Double.parseDouble(item.longitude));
			item.title = item.hotelName;
			item.hotelCode = _AppManager.m_HotelSearchListData.get(i).hotelCode;
			((TextView)findViewById(R.id.title_logo)).setText("검색결과");
				
			Marker firstMarker = mapFragment.placeMarker(item); 
			eventMarkerMap.put(firstMarker, item); 
		}
		

		 mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
		 {             
			 @Override     
			 public void onInfoWindowClick(Marker marker) 
			 {       
				 EventInfo eventInfo = eventMarkerMap.get(marker);
				 
				 AppManagement _AppManager = (AppManagement) getApplication();
				 // 호텔 상세정보 페이지 가기
				 Toast.makeText(getBaseContext(),
						  eventInfo.title + " ID " + eventInfo.hotelCode, 
						  Toast.LENGTH_LONG).show();
				 
				 _AppManager.m_HotelCode = eventInfo.hotelCode;
					Intent intent;
		            intent = new Intent().setClass(baseself, HotelDetailActivity.class);
		            startActivity( intent ); 
			 }    
		 });
		 


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
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			CameraPosition sydney = new CameraPosition.Builder().target(new LatLng(Double.parseDouble(_AppManager.m_HotelLat), Double.parseDouble(_AppManager.m_HotelLng)))
					.zoom(15.5f).bearing(0).tilt(0).build();
					mMap.animateCamera(CameraUpdateFactory.newCameraPosition(sydney));
		}
			break;

		}
		
	}
	
	
    

}

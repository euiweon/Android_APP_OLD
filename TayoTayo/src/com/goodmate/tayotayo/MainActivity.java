package com.goodmate.tayotayo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapView.OpenAPIKeyAuthenticationResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BaseActivity;
import com.goodmate.tayotayo.ContactActivity.ContactAdapter;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import net.daum.mf.map.api.MapReverseGeoCoder;
public class MainActivity extends BaseTayoActivity implements
	OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, MapView.CurrentLocationEventListener, 
	MapView.POIItemEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
	

	MainActivity self;
	protected SharedPreferences mPreferences;
	
	
	Boolean is_Popup = false;
	
	public String DestName;
	public String DestLat;
	public String DestLng;
	
	
	public boolean MyLocation = false;
	
	public String Lng;
	public String Lat;
	
	
	public String localName_3;
	public String lng;
	public String lat;
	public String title;
	
	int PopupIndex = -1;
	
	
	AroundTaxiData CurrentTaxi = new AroundTaxiData();
	
	int TaxiState = 0;
	
	private ListView m_ListView;
	
	

	Boolean locationcheck = false;;
	LocationManager locationmanager;
	Criteria criteria;
	 
	Boolean CurrentPosition = true;		// 현재 위치인지?
	Boolean UseNetwork = false;			// 현재 네트워크가 사용중인지 체크 ( 사용중이라면 사용이 끝난후 데이터 전송해야함 )
	Boolean MoreGetData = false;			// 한번더 얻어와야할 정보가 있는지 확인한다. 
	 
	double m_lat;
	double m_lng;

	int BallType  = 0;
	private ContactAdapter m_Adapter;
	
	private MapView mapView;
	
	private TimerTask mTask; 
    private Timer mTimer; 
    
    Integer TimerType = 0;
    
    MapPOIItem balltype1;
    MapPOIItem balltype2;
    
    
    String TelNumber;
    String TelCompany;
    
    boolean CheckCurrPosition = false;
    
    String FinalLocationData = "";
    
    
    boolean m_bLocation = false;
    String LocationLat = "";
    String LocationLng = "";
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		{
        	AppManagement _AppManager = (AppManagement) getApplication();
        	_AppManager.activityList1.add(this);
        }
		
		// 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
		self = this;

        mapView = (MapView)findViewById(R.id.daumMapView);
        mapView.setDaumMapApiKey("ccfcbca3779b9992c6602df34e72cacddef6dfff");
        mapView.setOpenAPIKeyAuthenticationResultListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setPOIItemEventListener(this);

       // mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        
		
		mPreferences = getPreferences(MODE_PRIVATE);
		
		
		 {
			 AppManagement _AppManager = (AppManagement) getApplication();
	        m_ListView = ((ListView)findViewById(R.id.select_list_2));	    
	    	m_Adapter = new ContactAdapter(this, R.layout.destnatio_row, _AppManager.FavoriteArray);
	    	
	    	
	    	m_ListView.setAdapter(m_Adapter);
			m_ListView.setDivider(null);
			
	    		
	    	m_Adapter.notifyDataSetChanged();
	    }
		 
		
		
		BtnEvent(R.id.top_1);
		BtnEvent(R.id.top_2);
		BtnEvent(R.id.top_3);
		BtnEvent(R.id.top_4);
		BtnEvent(R.id.bottom_1);
		
		BtnEvent(R.id.dest_data);
		BtnEvent(R.id.select_bt1);
		BtnEvent(R.id.select_bt2);
		
		BtnEvent(R.id.popup_bottom_2);
		BtnEvent(R.id.popup_bottom_2_2);
		
		BtnEvent(R.id.popup_bottom_4_bt_1);
		BtnEvent(R.id.popup_bottom_4_bt_2);
		
		BtnEvent(R.id.yes_bt);
		BtnEvent(R.id.no_bt);
		BtnEvent(R.id.popup_bottom_6);
		BtnEvent(R.id.popup_bottom_6_6);
		
		
		BtnEvent(R.id.yes_bt_7);
		BtnEvent(R.id.no_bt_7);
		
		
		BtnEvent(R.id.checkbox_1);
		BtnEvent(R.id.checkbox_2);
		BtnEvent(R.id.checkbox_3);
		BtnEvent(R.id.checkbox_4);
		BtnEvent(R.id.select_bt8);
		
		BtnEvent(R.id.select_bt9_1);
		BtnEvent(R.id.select_bt9_2);
		
		
		
		AfterCreate();
		ClosePopup();
		
		
		GetLogin();
		
		
		String context = Context.LOCATION_SERVICE;
        locationmanager=(LocationManager)getSystemService(context);
        
        if(!locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) 
        {
            alertCheckGPS();
        }
        
		
		criteria=new Criteria();
		GetLocation();
		
        
        mapView.setCurrentLocationEventListener(this);
		
		TimerOn();

	}
	
	
	protected void TimerOn()
	{
		if ( mTimer != null )
			return;
		
		mTask = new TimerTask() { 

            @Override

            public void run() 
            { 
            	if ( TimerType == 0 )
            		GetSearchTaxi();
            	else if ( TimerType == 1)
            	{
            		GetCallState2();
            	}
            } 

        };
        
        mTimer = new Timer(); 
        mTimer.schedule(mTask, 12000, 10000); 
	}
	protected void TimerOff()
	{
		if ( mTimer != null)
			mTimer.cancel();
		mTimer = null;
	}
	
	
	
	

	@Override
	protected void onDestroy() {

		// save map view state such as map center position and zoom level.

		super.onDestroy();
	}


	// 팝업의 오픈 
	public void OpenPopup( int index )
	{
		TimerOff();
		PopupIndex = index;

		is_Popup = true;
		
		((View)findViewById(R.id.bottom_menu)).setVisibility(View.GONE);
		((View)findViewById(R.id.top_menu)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_1)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_2)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_3)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_4)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_5)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_6)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_7)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_8)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_9)).setVisibility(View.GONE);
		
		ChangeBalltype(-1);
		switch ( index)
		{
		case 0:
			((View)findViewById(R.id.pop_menu_1)).setVisibility(View.VISIBLE);
			break;
		case 1:
			
			
			((View)findViewById(R.id.pop_menu_2)).setVisibility(View.VISIBLE);
			
			{
				lat= Double.toString(m_lat);
				lng =Double.toString(m_lng);
				ChangeBalltype(1);
			}
			break;
		case 2:
			((View)findViewById(R.id.pop_menu_3)).setVisibility(View.VISIBLE);
			break;
		case 3:
			((View)findViewById(R.id.pop_menu_4)).setVisibility(View.VISIBLE);
			TimerType = 1;
			TimerOn();
			break;
		case 4:
			((View)findViewById(R.id.pop_menu_5)).setVisibility(View.VISIBLE);

			break;
		case 5:
			((View)findViewById(R.id.pop_menu_6)).setVisibility(View.VISIBLE);
			{


				lat= Double.toString(m_lat);
				lng =Double.toString(m_lng);
				ChangeBalltype(1);
			}
			break;
		case 6: 
			
			((View)findViewById(R.id.pop_menu_7)).setVisibility(View.VISIBLE);

			
			break;
		case 7:
			((View)findViewById(R.id.pop_menu_8)).setVisibility(View.VISIBLE);
			{
				{
					boolean checkbox1 = mPreferences.getBoolean("CALL_OP_ALL", false);
					if ( !checkbox1 )
            			((ImageView)findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.box);
            		else
            			((ImageView)findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.check_bt);
				}
				
				{
					boolean checkbox1 = mPreferences.getBoolean("CALL_OP_NOR", false);
					if ( !checkbox1 )
            			((ImageView)findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.box);
            		else
            			((ImageView)findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.check_bt);
				}
				
				{
					boolean checkbox1 = mPreferences.getBoolean("CALL_OP_MOD", false);
					if ( !checkbox1 )
            			((ImageView)findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.box);
            		else
            			((ImageView)findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.check_bt);
				}
				
				{
					boolean checkbox1 = mPreferences.getBoolean("CALL_OP_PET", false);
					if ( !checkbox1 )
            			((ImageView)findViewById(R.id.checkbox_4)).setBackgroundResource(R.drawable.box);
            		else
            			((ImageView)findViewById(R.id.checkbox_4)).setBackgroundResource(R.drawable.check_bt);
				}
			}
			break;
		case 8: 
			((View)findViewById(R.id.pop_menu_9)).setVisibility(View.VISIBLE);
			break;
			
		}
	}
	
	
	// 팝업 닫기
	public void ClosePopup()
	{
		CheckCurrPosition = false;
		TimerType = 0;
		TimerOn();
		PopupIndex = -1; 
		BallType = 0; 
		ChangeBalltype(BallType);
		is_Popup = false; 
		
		((View)findViewById(R.id.bottom_menu)).setVisibility(View.VISIBLE);
		((View)findViewById(R.id.top_menu)).setVisibility(View.VISIBLE);
		
		((View)findViewById(R.id.pop_menu_1)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_2)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_3)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_4)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_5)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_6)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_7)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_8)).setVisibility(View.GONE);
		((View)findViewById(R.id.pop_menu_9)).setVisibility(View.GONE);
		
	}
	

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.e(TAG, "onResume");
		
		CheckCurrPosition = false;
		 AppManagement _AppManager = (AppManagement) getApplication();
		if ( _AppManager.FavoriteResi) 
		{
			OpenPopup(5);
			 _AppManager.FavoriteResi = false;
			 CheckCurrPosition = true;
		}
	}
	
	// 버튼 이벤트

	public void BtnEvent( int id )
    {
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(new OnClickListener()
        {
            public void onClick(View arg0)
            {
            	switch(arg0.getId())
            	{
            	case R.id.top_1:
            	{
            		 Intent intent;
			          // Create an Intent to launch an Activity for the tab (to be reused)
			         intent = new Intent().setClass(self, ContactActivity.class);
			          
			          
			         startActivity( intent ); 
			         

            		
            	}
            		break;
            	case R.id.top_2:
            	{
            		// 즐겨찾기 목적지
            		Intent intent;
			        intent = new Intent().setClass(self, FavoriteActivity.class);
			        startActivity( intent );
            		
            	}
            		break;
            	case R.id.top_3:
            	{
            		// 콜 옵션 설정
            		OpenPopup(7);
            	}
            		break;
            	case R.id.top_4:
            	{
            		// 택시 사용 이력 설정
            		Intent intent;
			        intent = new Intent().setClass(self, Boarding_Log_Activity.class);
			        startActivity( intent );
            		
            		
            	}
            		break;
            	case R.id.bottom_1:
            	{
            		OpenPopup(0);
            	}
            		break;
            	case R.id.dest_data:
            	{
            		// 목적지 새로 입력
            		
            		OpenPopup(1);
            	}
            		break;
            	case R.id.select_bt1:
            	{
            		// 선택
            		
            		AppManagement _AppManager = (AppManagement) getApplication();

            		int count  = 0;
            		String tempLat = "";
            		String tempLng = "";
					for ( int i = 0 ; i <_AppManager.FavoriteArray.size() ; i++ )
					{
						if(_AppManager.FavoriteArray.get(i).isselect == true )
						{
							count ++ ;
							tempLat = _AppManager.FavoriteArray.get(i).Lat;
							tempLng = _AppManager.FavoriteArray.get(i).Lng;
						}
					}
					
					if ( count == 0 )
					{
						self.ShowAlertDialLog( self ,"에러" , "아무것도 선택이 되지 않았습니다" );
					}
					else if ( count > 1)
					{
						self.ShowAlertDialLog( self ,"에러" , "중복선택입니다." );
					}
					else
					{
						DestLat = tempLat;
						DestLng = tempLng;
	            		SetDestnation2();
					}
            	}
            		break;
            	case R.id.select_bt2:
            	{
            		GetCallTaxi();
    		    	/*new AlertDialog.Builder(self)
    				 .setTitle("검색지설정")
    				 .setMessage(" 현재 보고 있는 곳을 목적지로 설정하시겠습니까?") //줄였음
    				 .setPositiveButton("예", new DialogInterface.OnClickListener() 
    				 {
    				     public void onClick(DialogInterface dialog, int whichButton)
    				     {   
    				    	// 건너 뛰기 
    		            	GetCallTaxi();
    				     }
    				 })
    				 .setNegativeButton("아니요", new DialogInterface.OnClickListener() 
    				 {
    				     public void onClick(DialogInterface dialog, int whichButton) 
    				     {
    				         //...할일
    				     }
    				 })
    				 .show();*/
            		
            	}
            		break;
            	case R.id.popup_bottom_2:
            	{
            		// 목적지 검색
            		FindLocation();
            		
            	}
            		break;
            	case R.id.popup_bottom_2_2:
            	{

    		    	new AlertDialog.Builder(self)
    				 .setTitle("검색지설정")
    				 .setMessage(" 현재 보고 있는 곳을 목적지로 설정하시겠습니까?") //줄였음
    				 .setPositiveButton("예", new DialogInterface.OnClickListener() 
    				 {
    				     public void onClick(DialogInterface dialog, int whichButton)
    				     {   
    				    	 SetDestnation2();
    		            		// 목적지 검색 확인 
    				     }
    				 })
    				 .setNegativeButton("아니요", new DialogInterface.OnClickListener() 
    				 {
    				     public void onClick(DialogInterface dialog, int whichButton) 
    				     {
    				         //...할일
    				    	 
    				    	 m_bLocation = false;
    				     }
    				 })
    				 .show();
    		    	
            	}
            		break;
            	case R.id.popup_bottom_4_bt_1:
            	{
            		AppManagement _AppManager = (AppManagement) getApplication();
            		
            		long now = System.currentTimeMillis();

            		// 현재 시간을 저장 한다.
            		Date date = new Date(now);
            		SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            		String Time = sdfNow.format(date);
            		
            		_AppManager.AddBoard(_AppManager.CallIdx, Time, CurrentTaxi.CarNumber , CurrentTaxi.PhoneNumber);
            		
            		FindLocation3();
            		
            	}
            		break;
            	case R.id.popup_bottom_4_bt_2:
            	{
            		// 취소
            		OpenPopup(4);
            		((TextView)findViewById(R.id.cancel_data)).setText("예약된 "+ CurrentTaxi.CarNumber + "의 택시를 취소하시겠습니까?");
            		
            	}
            		break;
            	case R.id.yes_bt:
            	{
            		// 승객 콜 취소
            		CancelCall();
            		// 맵뷰에 있는 마커를 모두 지워 준다. 
            		mapView.removeAllPOIItems();
            	}
            		break;
            	case R.id.no_bt:
            	{
            		// 승객 콜 취소 취소
            		onBackPressed();
            	}
            		break;
            	case R.id.popup_bottom_6:
            	{
            		FindLocation2();
            	}
            		break;
            	case R.id.popup_bottom_6_6:
            	{
            		new AlertDialog.Builder(self)
   				 .setTitle("즐겨찾기")
   				 .setMessage(" 현재 보고 있는 곳을 즐겨찾기에 등록하시겠습니까?") //줄였음
   				 .setPositiveButton("예", new DialogInterface.OnClickListener() 
   				 {
   				     public void onClick(DialogInterface dialog, int whichButton)
   				     {   
   				    	OpenPopup(6);
   				     }
   				 })
   				 .setNegativeButton("아니요", new DialogInterface.OnClickListener() 
   				 {
   				     public void onClick(DialogInterface dialog, int whichButton) 
   				     {
   				         //...할일
   				     }
   				 })
   				 .show();
            		
            		
            	}
            		break;
            	case R.id.yes_bt_7:
            	{
            		AppManagement _AppManager = (AppManagement) getApplication();
            		EditText oldpass = (EditText)findViewById(R.id.loca_data);
            		
            		// 핀의 위치로 목적지를 등록 한다. 
            		if ( balltype2 != null )
            		{
                		DestLat =  Double.toString(balltype2.getMapPoint().getMapPointGeoCoord().latitude);
                		DestLng = Double.toString(balltype2.getMapPoint().getMapPointGeoCoord().longitude);
                		if ( oldpass.getText().toString().equals(""))
                		{
                			_AppManager.AddFavorite(DestName, DestLat, DestLng);
                		}
                		else
                		{
                			_AppManager.AddFavorite(oldpass.getText().toString(), DestLat, DestLng);
                		}
                		ClosePopup();
                		 Intent intent;
    			          // Create an Intent to launch an Activity for the tab (to be reused)
    			         intent = new Intent().setClass(self, FavoriteActivity.class);
    			          
    			          
    			         startActivity( intent );          			
            		}

			         
            		
            		
            	}
            		break;
            	case R.id.no_bt_7:
            	{
            		OpenPopup(5);
            	}
            		break;
            	case R.id.checkbox_1:
            	{
            		boolean checkbox1 = mPreferences.getBoolean("CALL_OP_ALL", false);
            		checkbox1 = !checkbox1;
            		SharedPreferences.Editor edit = mPreferences.edit();

            		edit.putBoolean("CALL_OP_ALL", checkbox1);
            		edit.commit();
            		
            		if ( !checkbox1 )
            			((ImageView)findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.box);
            		else
            			((ImageView)findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.check_bt);

            	}
            		break;
            	case R.id.checkbox_2:
            	{
            		boolean checkbox1 = mPreferences.getBoolean("CALL_OP_NOR", false);
            		checkbox1 = !checkbox1;
            		SharedPreferences.Editor edit = mPreferences.edit();

            		edit.putBoolean("CALL_OP_NOR", checkbox1);
            		edit.commit();
            		if ( !checkbox1 )
            			((ImageView)findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.box);
            		else
            			((ImageView)findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.check_bt);
            		
            	}
            		break;
            	case R.id.checkbox_3:
            	{
            		boolean checkbox1 = mPreferences.getBoolean("CALL_OP_MOD", false);
            		checkbox1 = !checkbox1;
            		SharedPreferences.Editor edit = mPreferences.edit();

            		edit.putBoolean("CALL_OP_MOD", checkbox1);
            		edit.commit();
            		if ( !checkbox1 )
            			((ImageView)findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.box);
            		else
            			((ImageView)findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.check_bt);
            		
            	}
            		break;
            	case R.id.checkbox_4:
            	{
            		boolean checkbox1 = mPreferences.getBoolean("CALL_OP_PET", false);
            		checkbox1 = !checkbox1;
            		SharedPreferences.Editor edit = mPreferences.edit();

            		edit.putBoolean("CALL_OP_PET", checkbox1);
            		edit.commit();
            		
            		if ( !checkbox1 )
            			((ImageView)findViewById(R.id.checkbox_4)).setBackgroundResource(R.drawable.box);
            		else
            			((ImageView)findViewById(R.id.checkbox_4)).setBackgroundResource(R.drawable.check_bt);
            		
            	}
            		break;
            	case R.id.select_bt8:
            	{
            		ClosePopup();
            	}
            		break;
            	case R.id.select_bt9_1:
            	{
            		
            	}
            		break;
            	case R.id.select_bt9_2:
            	{
            		
            	}
            		break;
            		
            		
            	}
            	
            }
        });

    }
	// 뒤로가기
	@Override
    public void onBackPressed() 
    {
    	
		if ( is_Popup)
		{
			if ( PopupIndex == 4)
			{
				OpenPopup(3);
			}
			else
			{
				ClosePopup();
			}
			
		}
		else
		{
			// 이전 화면으로 못 돌아가게 막는다.( 아예 종료가 되도록한다 )
	    	// 일단은 돌아갈수 있게 테스트용으로 열어둔다. 
	    	new AlertDialog.Builder(this)
			 .setTitle("종료")
			 .setMessage("종료하시겠습니까?") //줄였음
			 .setPositiveButton("예", new DialogInterface.OnClickListener() 
			 {
			     public void onClick(DialogInterface dialog, int whichButton)
			     {   
			    	 Intent intent;
			          // Create an Intent to launch an Activity for the tab (to be reused)
			          intent = new Intent().setClass(self, OutroActivity.class);
			          
			          
			          startActivity( intent ); 
			     }
			 })
			 .setNegativeButton("아니요", new DialogInterface.OnClickListener() 
			 {
			     public void onClick(DialogInterface dialog, int whichButton) 
			     {
			         //...할일
			     }
			 })
			 .show();
		}
    	
    	
    }
	
	// 로그인
	public void GetLogin()
    {
    	{
    		mProgress.setMessage("로그인 중입니다. ");
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{


					EditText oldpass = (EditText)findViewById(R.id.login_id_data);
					Map<String, String> data = new HashMap  <String, String>();
					
					SharedPreferences preferences = getSharedPreferences( "userinfo" ,MODE_PRIVATE);
					
					data.put("phoneNumber", preferences.getString("phone", ""));
					data.put("uuid",_AppManager.DeviceID);
					data.put("osId","1");

					
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/Login", data);

					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("IsSuccess").equals("true"))
						{

							JSONObject usageList = (JSONObject) json.opt("Contents");
							
							{
								_AppManager.UserIdx = usageList.optString("UserIdx", "");
								
								JSONArray Coupons = (JSONArray) usageList.optJSONArray("Coupons");
								_AppManager.CouponArray.clear();
								
								if ( Coupons !=null )
								{
									for ( int i = 0 ; i < Coupons.length() ; i++ )
									{
										JSONObject list = (JSONObject)Coupons.get(i);
										
										MainCouponData object = new MainCouponData() ;
										
										object.Idx = list.getString("Idx");
										object.Name = list.getString("Name");
										object.EffectDescription = list.getString("EffectDescription");
										object.Url = list.getString("Url");
										object.IconUrl = list.getString("IconUrl");
										object.RemainTime = list.getString("RemainTime");
										
										_AppManager.CouponArray.add(object );
									}
								}
								
							}
							
							handler2.sendEmptyMessage(0);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler2.sendMessage(handler2.obtainMessage(1,(((JSONObject) json.opt("Contents")).optString("Error")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler2.sendMessage(handler2.obtainMessage(1,"ReqeustCode" ));
						e.printStackTrace();
					} 
				}
			});
			thread.start();
		}
    }

	// 주변 택시 찾기 
	public void GetSearchTaxi()
    {
		
		//if ( MyLocation )
    	{
    		
    		/*mProgress.setMessage("택시 찾는 중입니다. ");
			final  AppManagement _AppManager = (AppManagement) getApplication();
			//mProgress.show();
			
			for ( int i = 0 ; i < _AppManager.AroundTaxiArray.size(); i++ )
			{
				mapView.removePOIItem(_AppManager.AroundTaxiArray.get(i).poi );
			}
			
			
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{


					
				}
			});
			thread.start();*/
    		
    		final  AppManagement _AppManager = (AppManagement) getApplication();
    		Map<String, String> data = new HashMap  <String, String>();
			

			
			_AppManager.AroundTaxiArray.clear();
			data.put("UserIdx", _AppManager.UserIdx);
			data.put("Lat",Double.toString(m_lat));
			data.put("Lng",Double.toString(m_lng));
			data.put("SearchDistance ","6000");
			
			String strJSON = _AppManager.GetHttpManager().GetHTTPData2(_AppManager.DEF_URL +  "/GetAroundTaxiList", data);

			try 
			{
				JSONObject json = new JSONObject(strJSON);
				if(json.optString("IsSuccess").equals("true"))
				{

					if ((json.optString("Contents")) == null || (json.optString("Contents")).equals("null") )
					{
						//handler2.sendMessage(handler2.obtainMessage(1,"데이터 오류"));
						_AppManager.AroundTaxiArray.clear();
						//return ;
					}
					JSONObject usageList = (JSONObject) json.opt("Contents");
					
					{
						JSONArray Data = (JSONArray) usageList.optJSONArray("Data");
						_AppManager.AroundTaxiArray.clear();
						for ( int i = 0 ; i < Data.length() ; i++ )
						{
							JSONObject list = (JSONObject)Data.get(i);
							
							AroundTaxiData object = new AroundTaxiData() ;


							object.DriverIdx = list.getString("DriverIdx");
							object.CarNumber = list.getString("CarNumber");
							object.DriverName = list.getString("DriverName");
							object.IsEmpty = list.getString("IsEmpty");
							object.PhoneNumber = list.getString("PhoneNumber");
							
							object.Lat = list.getString("Lat");
							object.Lng = list.getString("Lng");
							
							_AppManager.AroundTaxiArray.add(object );
						}
					}
					
					
					handler2.sendEmptyMessage(24);
				}
				else 
				{
					// 에러 메세지를 전송한다. 
					handler2.sendMessage(handler2.obtainMessage(1,(json.optString("Data null")) ));
					return ;
				}
			} catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				handler2.sendMessage(handler2.obtainMessage(1, "Json Parsing Error \n " + strJSON ));
				e.printStackTrace();
			} 
			
		}
		/*else
		{
			self.ShowAlertDialLog( self ,"에러" , "내 위치를 검색할수 없어서 주변의 택시를 찾을수 없습니다. " );
		}*/
    }
	
	// 콜 택시 호출
	public void GetCallTaxi()
    {
		
		//if ( MyLocation )
    	{
    		OpenPopup(2);
			final  AppManagement _AppManager = (AppManagement) getApplication();
			
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{


					Map<String, String> data = new HashMap  <String, String>();
					

					
					data.put("UserIdx", _AppManager.UserIdx);
					
					// 마커에 지정된 위치로...
					data.put("Lat",Double.toString(m_lat));
					data.put("Lng",Double.toString(m_lng));
					data.put("SearchLevel ","0");
					
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/CallTaxi", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("IsSuccess").equals("true"))
						{
							if ((json.optString("Contents")) == null || (json.optString("Contents")).equals("null") )
							{
								handler2.sendEmptyMessage(231);
								return ;
							}

							JSONObject usageList = (JSONObject) json.opt("Contents");
							_AppManager.CallIdx = usageList.optString("CallIdx", "");
							
							handler2.sendEmptyMessage(31);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							//handler2.sendMessage(handler2.obtainMessage(1,(json.optString("ReqeustCode")) ));
							handler2.sendEmptyMessage(2302);
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler2.sendMessage(handler2.obtainMessage(1,"Json Parsing Error /n" + strJSON ));
						e.printStackTrace();
					} 
				}
			});
			thread.start();
		}
		/*else
		{
			self.ShowAlertDialLog( self ,"에러" , "내 위치를 검색할수 없어서 택시를 찾을수 없습니다. " );
		}*/
    }
	
	public void SetDestnation2()
	{
	    m_bLocation = true;
	    LocationLat = DestLat;
	    LocationLng = DestLng;

	}
	
	// 목적지 지정
	public void SetDestnation()
	{
		mProgress.setMessage("목적지를 설정 중입니다. ");
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{


				Map<String, String> data = new HashMap  <String, String>();
			
				data.put("UserIdx", _AppManager.UserIdx);
				data.put("CallIdx ", _AppManager.CallIdx);
				data.put("Lat",LocationLat);
				data.put("Lng",LocationLng);
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/SetDestnation", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.optString("IsSuccess").equals("true"))
					{
						
						if ((json.optString("Contents")) == null || (json.optString("Contents")).equals("null") )
						{
							handler2.sendMessage(handler2.obtainMessage(1,"목적지를 설정할수 없습니다."));
							return ;
						}

						JSONObject usageList = (JSONObject) json.opt("Contents");
						
						
						handler2.sendEmptyMessage(40);
					}
					else 
					{
						// 에러 메세지를 전송한다. 
						handler2.sendMessage(handler2.obtainMessage(1,(json.optString("Data null")) ));
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler2.sendMessage(handler2.obtainMessage(1,"Json Parsing Error" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	// 호출한 택시 정보 가져오기 
	public void GetCallState()
	{
		mProgress.setMessage("호출한 택시의 정보를 가져오는 중입니다.");
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{


				Map<String, String> data = new HashMap  <String, String>();
				

				
				data.put("UserIdx", _AppManager.UserIdx);
				data.put("CallIdx",_AppManager.CallIdx);
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/GetCallState", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.optString("IsSuccess").equals("true"))
					{

						JSONObject usageList = (JSONObject) json.opt("Contents");
						TaxiState = usageList.getInt("State");
						{
							JSONObject usageList2 = (JSONObject) usageList.opt("CurrentTaxi");
							CurrentTaxi.DriverIdx = usageList2.getString("DriverIdx");
							CurrentTaxi.CarNumber = usageList2.getString("CarNumber");
							CurrentTaxi.DriverName = usageList2.getString("DriverName");
							CurrentTaxi.IsEmpty = usageList2.getString("IsEmpty");
							CurrentTaxi.PhoneNumber = usageList2.getString("PhoneNumber");
							CurrentTaxi.Lat = usageList2.getString("Lat");
							CurrentTaxi.Lng = usageList2.getString("Lng");
						}
						
						
						
						
						handler2.sendEmptyMessage(50);
					}
					else 
					{
						// 에러 메세지를 전송한다.( 일단 에러 메세지 무시하자 ) 
						//handler2.sendMessage(handler2.obtainMessage(1,(json.optString("ReqeustCode")) ));
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler2.sendMessage(handler2.obtainMessage(1,"Json Parsing Error" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	// 택시의 상태를 가져온다 No 쓰레드 
	public void GetCallState2()
	{
		mProgress.setMessage("호출한 택시의 정보를 가져오는 중입니다.");
		final  AppManagement _AppManager = (AppManagement) getApplication();
		Map<String, String> data = new HashMap  <String, String>();
		

		
		data.put("UserIdx", _AppManager.UserIdx);
		data.put("CallIdx",_AppManager.CallIdx);
		
		String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/GetCallState", data);

		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.optString("IsSuccess").equals("true"))
			{

				JSONObject usageList = (JSONObject) json.opt("Contents");
				TaxiState = usageList.getInt("State");
				{
					JSONObject usageList2 = (JSONObject) usageList.opt("CurrentTaxi");
					CurrentTaxi.DriverIdx = usageList2.getString("DriverIdx");
					CurrentTaxi.CarNumber = usageList2.getString("CarNumber");
					CurrentTaxi.DriverName = usageList2.getString("DriverName");
					CurrentTaxi.IsEmpty = usageList2.getString("IsEmpty");
					CurrentTaxi.PhoneNumber = usageList2.getString("PhoneNumber");
					CurrentTaxi.Lat = usageList2.getString("Lat");
					CurrentTaxi.Lng = usageList2.getString("Lng");
				}
				
				
				
				
				handler2.sendEmptyMessage(50);
			}
			else 
			{
				// 에러 메세지를 전송한다. 
				handler2.sendMessage(handler2.obtainMessage(1,(json.optString("Data null")) ));
				return ;
			}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			handler2.sendMessage(handler2.obtainMessage(1,"Json Parsing Error" ));
			e.printStackTrace();
			return ;
		} 
	}
	
	// 호출한 택시의 취소
	public void CancelCall()
	{
		mProgress.setMessage("호출한 택시를 취소하는 중입니다.");
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{


				Map<String, String> data = new HashMap  <String, String>();
				

				
				data.put("UserIdx", _AppManager.UserIdx);
				data.put("CallIdx",_AppManager.CallIdx);
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/CancelCall", data);
					
				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.optString("IsSuccess").equals("true"))
					{

						JSONObject usageList = (JSONObject) json.opt("Contents");
						
						
						handler2.sendEmptyMessage(60);
					}
					else 
					{
						// 에러 메세지를 전송한다. 
						handler2.sendMessage(handler2.obtainMessage(1,(json.optString("Data null ")) ));
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler2.sendMessage(handler2.obtainMessage(1,"Json Parsing Error" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	// 쿠폰 사용하기 
	public void UseCoupon()
	{
		mProgress.setMessage("쿠폰 사용을 시도합니다. ");
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{


				Map<String, String> data = new HashMap  <String, String>();
				

				
				data.put("UserIdx", _AppManager.UserIdx);
				data.put("DriverIdx",_AppManager.DriverIdx);
				data.put("CouponIdx",_AppManager.CouponIdx);
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/UseCoupon", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.optString("IsSuccess").equals("true"))
					{

						JSONObject usageList = (JSONObject) json.opt("Contents");
						
						
						handler2.sendEmptyMessage(70);
					}
					else 
					{
						// 에러 메세지를 전송한다. 
						handler2.sendMessage(handler2.obtainMessage(1,(json.optString("ReqeustCode")) ));
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler2.sendMessage(handler2.obtainMessage(1,"ReqeustCode" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	// 위치 검색( 위경도 )
	public void FindLocation()
	{
		mProgress.setMessage("검색중 입니다. ");
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{


				Map<String, String> data = new HashMap  <String, String>();
				
				EditText oldpass = (EditText)findViewById(R.id.popup_title_text_2);
				
				
				
				data.put("q", oldpass.getText().toString());
				data.put("output","json");
				data.put("apikey","8e3f8ba676d65658619b76e6e768218c6c53001b");
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData( "http://apis.daum.net/local/geo/addr2coord", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					
					JSONObject channel = json.optJSONObject("channel");
					{
						if (channel.getString("result").equals("0") )
						{
							handler2.sendMessage(handler2.obtainMessage(1,"검색결과가 없습니다." ));
						}
						else
						{
							JSONArray Data = (JSONArray) channel.optJSONArray("item");
							_AppManager.AroundTaxiArray.clear();
							for ( int i = 0 ; i < 1 ; i++ )
							{
								JSONObject list = (JSONObject)Data.get(i);
								
								
								localName_3 = list.optString("localName_3");
								lng = list.optString("lng");
								lat = list.optString("lat");

								
							}
							handler2.sendEmptyMessage(10230);
						}
					}
						
						
					
					
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler2.sendMessage(handler2.obtainMessage(1,"ReqeustCode" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	// 즐겨찾기용 위치검색
	public void FindLocation2()
	{
		mProgress.setMessage("검색중 입니다. ");
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{


				Map<String, String> data = new HashMap  <String, String>();
				
				EditText oldpass = (EditText)findViewById(R.id.popup_title_text_6_6);
				
				
				
				data.put("q", oldpass.getText().toString());
				data.put("output","json");
				data.put("apikey","8e3f8ba676d65658619b76e6e768218c6c53001b");
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData( "http://apis.daum.net/local/geo/addr2coord", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					
					JSONObject channel = json.optJSONObject("channel");
					{
						if (channel.getString("result").equals("0") )
						{
							handler2.sendMessage(handler2.obtainMessage(1,"검색결과가 없습니다." ));
						}
						else
						{
							JSONArray Data = (JSONArray) channel.optJSONArray("item");
							_AppManager.AroundTaxiArray.clear();
							for ( int i = 0 ; i < 1 ; i++ )
							{
								JSONObject list = (JSONObject)Data.get(i);
								
								
								localName_3 = list.optString("localName_3");
								lng = list.optString("lng");
								lat = list.optString("lat");

								
							}
							handler2.sendEmptyMessage(10230);
						}
					}
						
						
					
					
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler2.sendMessage(handler2.obtainMessage(1,"ReqeustCode" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	
	}	
	
	// 탑승시 주소검색
	public void FindLocation3()
	{
		mProgress.setMessage("검색중 입니다. ");
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{


				Map<String, String> data = new HashMap  <String, String>();
				
				EditText oldpass = (EditText)findViewById(R.id.popup_title_text_6_6);
				
				
				
				data.put("longitude",CurrentTaxi.Lng);
				data.put("latitude",CurrentTaxi.Lat);
				data.put("inputCoordSystem","WGS84");
				data.put("output","json");
				data.put("apikey","8e3f8ba676d65658619b76e6e768218c6c53001b");
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData( "http://apis.daum.net/local/geo/coord2addr", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					
					JSONObject channel = json.optJSONObject("channel");
					{
						if (channel.getString("result").equals("0") )
						{
							handler2.sendMessage(handler2.obtainMessage(1,"검색결과가 없습니다." ));
						}
						else
						{
							
							JSONArray Data = (JSONArray) channel.optJSONArray("item");

							
							FinalLocationData = json.optString("name1","");
							FinalLocationData += " ";
							FinalLocationData += json.optString("name2","");
							FinalLocationData += " ";
							FinalLocationData += json.optString("name3","");


							handler2.sendEmptyMessage(10231);
						}
					}
						
						
					
					
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler2.sendMessage(handler2.obtainMessage(1,"ReqeustCode" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	
	}
	
	final Handler handler2 = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			final  AppManagement _AppManager = (AppManagement) getApplication();
			
			switch(msg.what)
			{
			case 0:
				
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			case 2:
				break;
				
			case 3:
			{
				
			}
				break;
			case 20:
				break;
			case 24:
			{
			
			}
				break;
			case 31:
				// 배차 끝... 팝업 닫고, 택시 정보 표기 
				if ( m_bLocation == true )
				{
					SetDestnation();
				}
				ClosePopup();
				OpenPopup(3);
				GetCallState();
				ChangeBalltype(0);
				break;
			case 40:
				// 목적지 설정 
			{
				GetCallTaxi(); 
				

			}
				
				break;
			case 50:
				// 콜에 관련된 정보 설정 
			{
				((TextView)findViewById(R.id.popup_title_text_number)).setText(CurrentTaxi.CarNumber + " 배차완료");	
				
				// 위치정보 갱신 
				
				// 모든 POI 삭제
				mapView.removeAllPOIItems();

				            // 콜한 위치 표기 
				MapPOIItem balltype100 = new MapPOIItem();
				balltype100.setItemName("현재 내위치");
				balltype100.setMapPoint(MapPoint.mapPointWithGeoCoord(m_lat,m_lng));
				balltype100.setMarkerType(MapPOIItem.MarkerType.CustomImage);
				balltype100.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
				balltype100.setCustomImageResourceId(R.drawable.call_here);
				balltype100.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(122,10));
				balltype100.setShowCalloutBalloonOnTouch(false);
		        mapView.addPOIItem(balltype100);
		      ;

		        
		      	MapPOIItem balltype101 = new MapPOIItem();
		      	balltype101.setItemName("목적지");
		      	balltype101.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(CurrentTaxi.Lat),Double.parseDouble(CurrentTaxi.Lng)));
		      	balltype101.setMarkerType(MapPOIItem.MarkerType.BluePin);
		      	balltype101.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
		      	balltype101.setCustomImageResourceId(R.drawable.destination_button);
		      	balltype101.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(93,10));
		      	balltype101.setShowCalloutBalloonOnTouch(false);
		      	balltype101.setDraggable(false );
		      	balltype101.setTag(20032);
		        mapView.addPOIItem(balltype101);
		        
		        
		        mapView.fitMapViewAreaToShowAllPOIItems();
		
				
			}
				break;
			case 60:
				//  콜 취소 
				ClosePopup();
				break;
			case 70:
				// 쿠폰 사용
				
				break;
				
			case 10230:
			{
				// 검색후 맵을 센터로 옮긴다. 
				ChangeBalltype(1);
	            //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(lat), Double.parseDouble(lng)), true);
				mapView.fitMapViewAreaToShowAllPOIItems();
	
			}
				
				break;
			case 10231:
			{
				SharedPreferences preferences = getSharedPreferences( "contacts" ,MODE_PRIVATE);
	        	if(preferences.getBoolean("check", false) )
	        	{
	        		_AppManager.SendSMS(CurrentTaxi.CarNumber, FinalLocationData);
	        	}
				
        		// 탑승완료
        		 Intent intent;
		         intent = new Intent().setClass(self, OutroActivity.class);
		         startActivity( intent ); 
			}
				break;
				
			case 231:
			{
        		mProgress.setMessage("콜택시 업체를 조회중입니다. ");
        		mProgress.show();
        		_AppManager.GetTaxiCompanyInfo("서울", handler2);
			}
				break;
			case 2302:
				self.ShowAlertDialLog( self ,"에러" , "현재 배치할수 있는 차량이 없습니다. " );
				break;
			case 20001:
			{
				if ( _AppManager.TaxiInfoArray.size() == 0 )
				{
					self.ShowAlertDialLog( self ,"에러" , "현재 배치할수 있는 차량이 없습니다. " );
				}
				else
				{
					TelCompany = _AppManager.TaxiInfoArray.get(0).TaxiCompanyName;
	            	TelNumber= _AppManager.TaxiInfoArray.get(0).Telephone;
	            	
					String [] Company = new String[_AppManager.TaxiInfoArray.size()];
					for ( int i = 0 ; i < _AppManager.TaxiInfoArray.size(); i++  )
					{
						Company[i] = (Html.fromHtml(_AppManager.TaxiInfoArray.get(i).TaxiCompanyName)).toString();
					}
					AlertDialog.Builder alt_bld = new AlertDialog.Builder(self);
			        
			        alt_bld.setTitle("주변에 배차가능한 택시가 없습니다. 원하시는 콜택시 회사를 선택해주십시오.");
			        
			        alt_bld.setCancelable(false);
	                alt_bld.setPositiveButton("전화걸기",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int id) {
	                            	startActivity(new Intent(Intent.ACTION_DIAL , Uri.parse("tel:" + TelNumber)));
	                            	dialog.cancel();
	                            	ClosePopup();
	                            }
	                    });
	                alt_bld.setNegativeButton("취소",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int id) {
	                            	dialog.cancel();
	                            	ClosePopup();
	                            }
	                    });
			        
			        alt_bld.setSingleChoiceItems(Company, 0, new DialogInterface.OnClickListener() 
			        {
			            public void onClick(DialogInterface dialog, int item) 
			            {
			            	TelCompany = _AppManager.TaxiInfoArray.get(item).TaxiCompanyName;
			            	TelNumber= _AppManager.TaxiInfoArray.get(item).Telephone;
			            }
			        });
			        AlertDialog alert = alt_bld.create();
			        alert.show();
			        
				}
				
			}
				break;
			case 20002:
				break;
			default:
				break;
			}

		}
    	
	};
	
	
	public void RefreshUI()
	{
		m_Adapter.notifyDataSetChanged();
	}
	
	public class ContactAdapter extends ArrayAdapter<FavoriteData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<FavoriteData> mList;
		private LayoutInflater mInflater;
		
    	public ContactAdapter(Context context, int layoutResource, ArrayList<FavoriteData> mTweetList)
		{
			super(context, layoutResource, mTweetList);
			this.mContext = context;
			this.mResource = layoutResource;
			this.mList = mTweetList;
			this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
    	
    	@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
    		final FavoriteData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				((TextView)convertView.findViewById(R.id.audition_deteil_row_reply)).setText(mBar.Name);
			
				
				LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.audition_deteil_row_3);
				if ( mBar.isselect )
				{
					detailBar1.setBackgroundResource(R.drawable.destination_list2);
				}
				else
				{
					detailBar1.setBackgroundResource(R.drawable.destination_list);
				}
				detailBar1.setOnClickListener(new View.OnClickListener() 
				{

					public void onClick(View v) 
					{
						AppManagement _AppManager = (AppManagement) getApplication();

						for ( int i = 0 ; i <_AppManager.FavoriteArray.size() ; i++ )
						{
							_AppManager.FavoriteArray.get(i).isselect = false;
							
							mBar.isselect = true;
							m_Adapter.notifyDataSetChanged();
						}
					}
				});
				
				
			}
			return convertView;
		}
    }

	public void ChangeBalltype ( int index )
	{
		switch( BallType)
		{
		case 0:
		{
			if ( balltype1 != null )
			{
				mapView.removePOIItem(balltype1);
			}
		}
			break; 
		case 1:
		{
			if ( balltype2 != null )
			{
				mapView.removePOIItem(balltype2);
			}
		}
			break; 
		case 2:
		{
			if ( balltype1 != null )
			{
				mapView.removePOIItem(balltype1);
			}
		}
			break; 
		}
		
		
		switch( index)
		{
		case 0:
		{
			balltype1 = new MapPOIItem();
			balltype1.setItemName("현재 내위치");
			balltype1.setMapPoint(MapPoint.mapPointWithGeoCoord(m_lat,m_lng));
			balltype1.setMarkerType(MapPOIItem.MarkerType.CustomImage);
			balltype1.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
			balltype1.setCustomImageResourceId(R.drawable.call_here);
			balltype1.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(122,10));
			balltype1.setShowCalloutBalloonOnTouch(false);
			balltype1.setDraggable(true );
	        balltype1.setTag(20031);
	        mapView.addPOIItem(balltype1);
	        
	        
	        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(m_lat,m_lng), true);
		}
			break; 
		case 1:
		{
			balltype2 = new MapPOIItem();
			balltype2.setItemName("목적지");
			balltype2.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(lat),Double.parseDouble(lng)));
			balltype2.setMarkerType(MapPOIItem.MarkerType.CustomImage);
			balltype2.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
			balltype2.setCustomImageResourceId(R.drawable.destination_button);
			balltype2.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(93,10));
			balltype2.setShowCalloutBalloonOnTouch(false);
			balltype2.setDraggable(true );
			balltype2.setTag(20032);
	        mapView.addPOIItem(balltype2);
	        
	       
		}
			break; 
			
		case 2:
		{
			balltype1 = new MapPOIItem();
			balltype1.setItemName("현재 내위치");
			balltype1.setMapPoint(MapPoint.mapPointWithGeoCoord(m_lat,m_lng));
			balltype1.setMarkerType(MapPOIItem.MarkerType.CustomImage);
			balltype1.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
			balltype1.setCustomImageResourceId(R.drawable.call_here);
			balltype1.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(122,10));
			balltype1.setShowCalloutBalloonOnTouch(false);
			balltype1.setTag(20031);
	        mapView.addPOIItem(balltype1);
	        
	        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(m_lat,m_lng), true);
		}
			break; 

		
		}
		BallType = index;
		
		

	}

	
	
	//-- GPS 관련
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
	        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
	        
	        String provider = locationmanager.getBestProvider(criteria, true);

	        if ( provider == null )
	        {
	        	Location location ;
	        	ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

	            boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
	            
	            if ( isWifi )
	            {
	            	criteria.setAccuracy(Criteria.ACCURACY_FINE);
	            	provider = LocationManager.GPS_PROVIDER;
	            	provider = locationmanager.getBestProvider(criteria, true);
	            	location = locationmanager.getLastKnownLocation(provider);
	            	updateWithNewLocation(location);
	            	locationmanager.requestLocationUpdates(provider, 1000, 10, locationListener);
	            	//Toast.makeText(getApplicationContext(),"gps이용불가, 네트워크로 추적",Toast.LENGTH_SHORT).show();
	            }
	        }
	        else
	        {
	            Location location = locationmanager.getLastKnownLocation(provider);


	            ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

	            boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

	            // GPS 정보보다 Wifi 정보를 우선시 함. 
	            if ( isWifi )
	            {
	            	criteria.setAccuracy(Criteria.ACCURACY_FINE);
	            	provider = locationmanager.getBestProvider(criteria, true);
	            	location = locationmanager.getLastKnownLocation(provider);
	            	updateWithNewLocation(location);
	            	//locationmanager.requestLocationUpdates(provider, 1000, 10, locationListener);
	            	//Toast.makeText(getApplicationContext(),"gps이용불가, 네트워크로 추적",Toast.LENGTH_SHORT).show();
	            }
	            
	            // GPS 
	            // 
	            else if(locationmanager.isProviderEnabled(provider)&&locationmanager.getLastKnownLocation(provider)!=null)
	            {
	            	updateWithNewLocation(location);
	            	//locationmanager.requestLocationUpdates(provider, 1000, 10, locationListener);     
	            	//Toast.makeText(getApplicationContext(),"gps 이용가능",Toast.LENGTH_SHORT).show();
	               
	            } 
	            
	            // GPS도 안되고 Wifi도 안 될경우 위치 추적 실패 떠야함. 
	            else
	            {
	            	//Toast.makeText(getApplicationContext(),"gps이용불가 Wifi 이용 불가 ",Toast.LENGTH_SHORT).show();
	            }
	        }
	 


	    }
		
		private final LocationListener locationListener = new LocationListener() 
		{
		    public void onLocationChanged(Location location) {
		     updateWithNewLocation(location);
		    }
		    public void onProviderDisabled(String provider){
		     updateWithNewLocation(null);
		    }
		    public void onProviderEnabled(String provider){ }
		    public void onStatusChanged(String provider, int status,
		      Bundle extras){ }
		 };
		   

		private void updateWithNewLocation(Location location) {

			AppManagement _AppManager = (AppManagement) getApplication();
	        String latlng = "";
	        if(location!=null && locationcheck == false)

	        {
	        	
	        	locationcheck = true;
	        	m_lat = location.getLatitude();
	            m_lng = location.getLongitude();
	            latlng = "위도 : "+m_lat+" \n경도 : "+m_lng;
	            
	            
	           
	            ChangeBalltype(0);
	           
	        }
	        else if(location!=null && locationcheck == true)
	        {
	        	m_lat = location.getLatitude();
	            m_lng = location.getLongitude();
	            latlng = "위도 : "+m_lat+" \n경도 : "+m_lng;
	            
	            ChangeBalltype(0);


	        }

	        else
	        {
	        	m_lat =37.5666091;
	        	m_lng = 126.978371;
	        	latlng="위치를 찾을수 없습니다. 기본 위치로 설정합니다. ";
	        	self.ShowAlertDialLog( self ,"에러" , latlng);
	            
	        	//ChangeBalltype(0);
	        }


		}


		@Override
		public void onCalloutBalloonOfPOIItemTouched(MapView arg0,
				MapPOIItem arg1) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onDraggablePOIItemMoved(MapView arg0, MapPOIItem arg1,
				MapPoint arg2) {
			// TODO Auto-generated method stub
			// 콜할 장소
			if ( arg1.getTag() == 20031)
			{
				m_lat =  balltype1.getMapPoint().getMapPointGeoCoord().latitude;
				m_lng =  balltype1.getMapPoint().getMapPointGeoCoord().longitude;
			}
			// 목적지 장소
			else if ( arg1.getTag() == 20032)
			{
				DestLat =  Double.toString(balltype2.getMapPoint().getMapPointGeoCoord().latitude);
        		DestLng = Double.toString(balltype2.getMapPoint().getMapPointGeoCoord().longitude);
			}
			
		}


		@Override
		public void onPOIItemSelected(MapView arg0, MapPOIItem arg1) {
			// TODO Auto-generated method stub
			
			Toast toast = Toast.makeText(getApplicationContext(), "핀을 길게 누르시면 핀을 이동 시킬 수 있습니다.", Toast.LENGTH_SHORT);
			toast.setGravity (Gravity.TOP | Gravity.CENTER, 0, 0);
			toast.show();
			
			
		}


		@Override
		public void onCurrentLocationDeviceHeadingUpdate(MapView arg0,
				float arg1) {
			// TODO Auto-generated method stub
			
			
		}


		@Override
		public void onCurrentLocationUpdate(MapView arg0, MapPoint arg1,
				float arg2) {

		}


		@Override
		public void onCurrentLocationUpdateCancelled(MapView arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onCurrentLocationUpdateFailed(MapView arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onMapViewCenterPointMoved(MapView arg0, MapPoint arg1) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onMapViewDoubleTapped(MapView arg0, MapPoint arg1) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onMapViewInitialized(MapView arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onMapViewLongPressed(MapView arg0, MapPoint arg1) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onMapViewSingleTapped(MapView arg0, MapPoint arg1) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onMapViewZoomLevelChanged(MapView arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onDaumMapOpenAPIKeyAuthenticationResult(MapView arg0,
				int arg1, String arg2) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder arg0,
				String arg1) {
			// TODO Auto-generated method stub
			
		}
		

}

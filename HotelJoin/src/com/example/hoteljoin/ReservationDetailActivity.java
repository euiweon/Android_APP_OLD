package com.example.hoteljoin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.euiweonjeong.base.KakaoLink;
import com.euiweonjeong.base.StoryLink;
import com.example.hoteljoin.HotelDetail2Activity.MyDialogListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.slidingmenu.lib.SlidingMenu;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReservationDetailActivity extends HotelJoinBaseActivity implements OnClickListener{
	ReservationDetailActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 


	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	
	private LayoutInflater mInflater;
	
	private ListView m_ListView;

	
	String hotelCode="";
	String hotelName="";
	String checkinDay="";
	String checkoutDay="";
	String duration="";
	String roomName="";
	String roomCount="";
	String adultCount="";
	String childCount="";
	String resvStatusCode="";
	String resvStatusName="";
	String payStatusCode="";
	String payStatusName="";
	String breakfastYn="";
	String addInfo="";
	String cityCode="";
	String cityName="";
	String nationCode="";
	String nationName="";
	
	String chargeOnCancelYn;


	Facebook facebook = new Facebook("489115824472243");
	
	 private SharedPreferences mPrefs;
	
	Integer day;
	 Integer month;
	 Integer year;
	 Calendar calendar = Calendar.getInstance();
	 
	 public ArrayList<CancelPolicyList>	m_RoomCancel = new ArrayList<CancelPolicyList>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation_detail);
		
		

		// 비하인드 뷰 설정
		setBehindContentView(R.layout.main_menu_1);
		
		self = this;
		

	    // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
		
		// 슬라이딩 뷰
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.RIGHT);
		
		{
			Display defaultDisplay = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    	
			int width;
			@SuppressWarnings("deprecation")
			int windowswidth = defaultDisplay.getWidth();
			@SuppressWarnings("deprecation")
			int windowsheight= defaultDisplay.getHeight();
			int originwidth = 480;
			

			width = (int) ( (float) 340 * ( (float)(windowswidth)/ (float)(originwidth) )  );

			
			menu.setBehindOffset(windowswidth - width );
		}
		
		menu.setFadeDegree(0.35f);
		
		
		
		RefreshUI();
		
		AfterCreate(7);
		
		BtnEvent(R.id.reser_cancel);
		BtnEvent(R.id.reser_save);
		BtnEvent(R.id.reser_save_1);
		
		
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
		
		
		
		GetData();
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
	
	public String getDate ( int iDay )
	{
		calendar.set(year, month -1, day);
		StringBuffer sbDate=new StringBuffer ( );
		calendar.add ( Calendar.DAY_OF_MONTH, iDay );
		int nYear = calendar.get ( Calendar.YEAR );
		int nMonth = calendar.get ( Calendar.MONTH ) + 1;
		int nDay = calendar.get ( Calendar.DAY_OF_MONTH );
		sbDate.append ( nYear );
		sbDate.append ("/");
		if ( nMonth < 10 ) sbDate.append ( "0" );
		sbDate.append ( nMonth );
		sbDate.append ("/");
		if ( nDay < 10 ) sbDate.append ( "0" );
		sbDate.append ( nDay );

		return sbDate.toString ( );
	}
	

	public void GetData()
	{
		mProgress.show();
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				Map<String, String> data = new HashMap  <String, String>();

				
				data.put("resvNum", _AppManager.m_ReservationNumber);
				data.put("nationCode", _AppManager.m_ReservationNationCode);

				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/booking/bookingDetail.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						JSONObject usageList = (JSONObject)json.get("resvInfo");
						

						hotelCode = 	usageList.optString("hotelCode");	
						hotelName = 	usageList.optString("hotelName");	
						checkinDay = 	usageList.optString("checkinDay");	
						checkoutDay = 	usageList.optString("checkoutDay");	
						duration = 	usageList.optString("duration");	
						roomName = 	usageList.optString("roomName");	
						roomCount = 	usageList.optString("roomCount");	
						adultCount = 	usageList.optString("adultCount");	
						childCount = 	usageList.optString("childCount");	
						resvStatusCode = 	usageList.optString("resvStatusCode");	
						resvStatusName = 	usageList.optString("resvStatusName");	
						payStatusCode = 	usageList.optString("payStatusCode");	
						payStatusName = 	usageList.optString("payStatusName");	
						breakfastYn = 	usageList.optString("breakfastYn");	
						addInfo = 	usageList.optString("addInfo");	
						cityCode = 	usageList.optString("cityCode");	
						cityName = 	usageList.optString("cityName");	
						nationCode = 	usageList.optString("nationCode");	
						nationName = 	usageList.optString("nationName");
						chargeOnCancelYn = json.getString("chargeOnCancelYn");	
						
						


			
						JSONArray usageList2 = (JSONArray)json.get("chargeConditions");
						for(int i = 0; i < usageList2.length(); i++)
						{
							CancelPolicyList item = new CancelPolicyList();
							JSONObject list = (JSONObject)usageList2.get(i);
							
							if ( list.optString("chargeYn").equals("1") )
							{
								item.cancelDay = list.optString("fromDate");
								item.cancelPrice = list.optString("currencyCode");
								item.cancelPrice = list.optString("chargeAmount");
								m_RoomCancel.add(item);
							}
							
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
	
	public void GetCancelData()
	{
		mProgress.show();
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				Map<String, String> data = new HashMap  <String, String>();

				
				data.put("resvNum", _AppManager.m_ReservationNumber);
				data.put("nationCode", _AppManager.m_ReservationNationCode);
				if ( _AppManager.m_LoginCheck == false)
				{
		
					data.put("resvName", _AppManager.m_ReservationName);
					data.put("resvNum", _AppManager.m_ReservationNumber);
				}
				else
				{
					data.put("memberId", _AppManager.m_LoginID);
				}
				
				data.put("resvStatusCode", resvStatusCode);
				data.put("checkinDay", checkinDay);
				
				

				

				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/booking/bookingCancelRequest.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						

						handler.sendEmptyMessage(10);
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
	
	
	
	
	public void RefreshUI()
	{
		((TextView)findViewById(R.id.main_row_1_sub)).setText(resvStatusName);
		((TextView)findViewById(R.id.main_row_1_title)).setText(hotelName);
			((TextView)findViewById(R.id.reser_data_1)).setText("숙박 시설명 :" + hotelName );
			((TextView)findViewById(R.id.reser_data_3)).setText("객실종류 : "  + roomName);
			((TextView)findViewById(R.id.reser_data_4)).setText("객실수 : "  + roomCount);
			((TextView)findViewById(R.id.reser_data_5)).setText("객실당 인원 : " + adultCount );

			
			((TextView)findViewById(R.id.reser_data_6)).setText("추가선택 : "  + addInfo );
			
			((TextView)findViewById(R.id.reser_data_2)).setText("투숙기간 :" + checkinDay + "~" + checkoutDay );
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
			case 10:
			{
				self.ShowAlertDialLog( self ,"취소" , "예약 취소 요청이 완료되었습니다." );
				resvStatusName = "취소대기";
				((TextView)findViewById(R.id.main_row_1_sub)).setText(resvStatusName);
				
			}
				break; 
			case 20:
			{

				
		    	
			}
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
		case R.id.reser_cancel:
		{
			if ( chargeOnCancelYn.equals("1"))
			{
			   	new AlertDialog.Builder(this)
				 .setTitle("취소 메세지")
				 .setMessage("취소시 위약금이 발생하는 예약입니다. \n 정말 취소하시겠습니까?") //줄였음
				 .setPositiveButton("예", new DialogInterface.OnClickListener() 
				 {
				     public void onClick(DialogInterface dialog, int whichButton)
				     {   
				    	 GetCancelData();
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
			else
			{
				new AlertDialog.Builder(this)
				 .setTitle("취소 메세지")
				 .setMessage(hotelName+" 예약을 \n 취소 요청 하시겠습니까?") //줄였음
				 .setPositiveButton("예", new DialogInterface.OnClickListener() 
				 {
				     public void onClick(DialogInterface dialog, int whichButton)
				     {   
				    	 GetCancelData();
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
			break;
		case R.id.reser_save:
		{
			
			
				
				new AlertDialog.Builder(this)
				 .setTitle("예약내용 저장")
				 .setMessage(hotelName+" 예약 내용을 저장하시겠습니까?") //줄였음
				 .setPositiveButton("예", new DialogInterface.OnClickListener() 
				 {
				     public void onClick(DialogInterface dialog, int whichButton)
				     {   
				    	 AppManagement _AppManager = (AppManagement) getApplication();
				    	 try {
							_AppManager.screenshot((View)findViewById(R.id.main_layout_222),_AppManager.m_ReservationNumber);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
		case R.id.reser_save_1:
			
		{
			
			
			Scahedule();
			
		}
			break;
			
		}
		
	}
	
	
	public class MyDialogListener implements DialogListener {

	    public void onComplete(Bundle values) 
	    {



	    }

	    public void onFacebookError(FacebookError e) {}
	    public void onError(DialogError e) {}
	    public void onCancel() {}

	}
	
	
	
	void Scahedule()
	{

		
			String []	NationList = {"카카오톡으로 보내기", "카카오 스토리로 보내기", "페이스북으로 보내기", "일정에 등록하기", "문자메세지로 보내기"};
			
			
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	        alt_bld.setTitle("일정 내보내기 선택");
	        alt_bld.setSingleChoiceItems(NationList, -1, new DialogInterface.OnClickListener() 
	        {
	            public void onClick(DialogInterface dialog, int item) 
	            {
	            	AppManagement _AppManager = (AppManagement) getApplication();
	        		DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
	        		 Date tempDate = null;
	        		try {
	        			tempDate = sdFormat.parse(checkinDay);
	        		} catch (ParseException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	        		
	        		 Date tempDate2 = null;
	        			try {
	        				tempDate2 = sdFormat.parse(checkoutDay);
	        			} catch (ParseException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
	        			
	            	if (item == 2 )
	            	{
	            		
	            		Calendar cal = Calendar.getInstance(); 
	            		cal.setTime(tempDate);
	            		Calendar cal2 = Calendar.getInstance(); 
	            		cal2.setTime(tempDate2);
	            		String dateToString , dateToString2 ;
	            		dateToString = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	            		dateToString2 = String.format("%04d-%02d-%02d", cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH) + 1, cal2.get(Calendar.DAY_OF_MONTH));
	            		
	            		
	            		Bundle params = new Bundle();

	            		params.putString("name", "Hotel Join");
	    	            params.putString("caption",hotelName + " 예약");
	    	            params.putString("picture","http://file.hoteljoin.com/images/eboard/banner/2793.png"); 
	    	            params.putString("link","http://www.facebook.com/hoteljoinkr"); 
	    	            //params.putString("description","http://www.facebook.com/hoteljoinkr"); 
	            		params.putString("description", dateToString + "~" + dateToString2 +"까지 "+ hotelName + " 예약했습니다.");
	            		    
	            		facebook.dialog(
	            			    self,
	            			    "feed", 
	            			    params,
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
	            			Calendar cal = Calendar.getInstance(); 
		            		cal.setTime(tempDate);
		            		Calendar cal2 = Calendar.getInstance(); 
		            		cal2.setTime(tempDate2);
		            		String dateToString , dateToString2 ;
		            		dateToString = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
		            		dateToString2 = String.format("%04d-%02d-%02d", cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH) + 1, cal2.get(Calendar.DAY_OF_MONTH));
		            		
		            		// Text 전송할 때
		            		Intent intent = new Intent(Intent.ACTION_SEND);
		            		intent.setType("text/plain");
		            		intent.putExtra(Intent.EXTRA_SUBJECT, hotelName + " 예약");
		            		intent.putExtra(Intent.EXTRA_TEXT, dateToString + "~" + dateToString2 +"까지 "+ hotelName + " 예약했습니다.");

		            		// KakaoTalk으로 바로 보내시려면 아래 코드를 추가합니다.
		            		intent.setPackage("com.kakao.talk");
		            		startActivity(intent);
	            		}
	            		
	            	}
	            	else if ( item == 1)
	            	{
	            		Map<String, Object> urlInfoAndroid = new Hashtable<String, Object>(1);
	            		
	            		Calendar cal = Calendar.getInstance(); 
	            		cal.setTime(tempDate);
	            		Calendar cal2 = Calendar.getInstance(); 
	            		cal2.setTime(tempDate2);
	            		String dateToString , dateToString2 ;
	            		dateToString = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	            		dateToString2 = String.format("%04d-%02d-%02d", cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH) + 1, cal2.get(Calendar.DAY_OF_MONTH));
	            		
	            		
	            		urlInfoAndroid.put("title", hotelName + " 예약");
	            		urlInfoAndroid.put("desc", dateToString + "~" + dateToString2 +"까지 "+ hotelName + " 예약했습니다.");
	            		urlInfoAndroid.put("type", "article");
	            		//urlInfoAndroid.put("imageurl", "http://file.hoteljoin.com/images/eboard/banner/2793.png");
	            		

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
	            	else if ( item == 3)
	            	{
	            		Calendar cal = Calendar.getInstance(); 
	            		cal.setTime(tempDate);
	            		Calendar cal2 = Calendar.getInstance(); 
	            		cal2.setTime(tempDate2);
	            		Intent intent = new Intent(Intent.ACTION_EDIT);
	            		intent.setType("vnd.android.cursor.item/event");
	            		intent.putExtra("beginTime", cal.getTimeInMillis());
	            		intent.putExtra("allDay", true);
	            		intent.putExtra("title", hotelName + "예약");
	            		intent.putExtra("endTime", cal2.getTimeInMillis());
	            		intent.putExtra("eventLocation", hotelName);
	            		startActivity(intent);
	            	}
	            	else if ( item == 4)
	            	{
	            		Calendar cal = Calendar.getInstance(); 
	            		cal.setTime(tempDate);
	            		Calendar cal2 = Calendar.getInstance(); 
	            		cal2.setTime(tempDate2);
	            		String dateToString , dateToString2 ;
	            		dateToString = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	            		dateToString2 = String.format("%04d-%02d-%02d", cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH) + 1, cal2.get(Calendar.DAY_OF_MONTH));
	            		
	            		Intent it = new Intent(Intent.ACTION_VIEW);   
	            		it.putExtra("sms_body", dateToString + "~" + dateToString2 +"까지 "+  hotelName + " 예약했습니다."); 
	            		it.setType("vnd.android-dir/mms-sms");
	            		startActivity(it);  
	            	}
	            	
	            	dialog.cancel();
	            }
	        });
	        AlertDialog alert = alt_bld.create();
	        alert.show();
		

		

		
		
	
		
	}


    

}

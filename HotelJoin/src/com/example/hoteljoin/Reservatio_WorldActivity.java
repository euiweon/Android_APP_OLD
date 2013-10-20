package com.example.hoteljoin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import com.example.hoteljoin.HotelDetailActivity.MyDialogListener;
import com.example.hoteljoin.HotelSearchActivity.Destination;
import com.example.hoteljoin.HotelSearchListActivity.SearchList_Adapter;
import com.example.hoteljoin.Reservation1Activity.Main_Adapter;
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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
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

public class Reservatio_WorldActivity extends HotelJoinBaseActivity implements OnClickListener{
	Reservatio_WorldActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 
	 
	public class ReservationWorldData
	{
		ReservationWorldData()
		{
					
		}
		
		Integer  type;	// Layout Type
		
		/// Layout 1
		String 	roomName = "";
		String roomcount = "";
		String roomman = "";
		String roomtype= "";		// Req Option
		String roomreop= "";
		String roomseop="";
		String price="";
		String day="";
		
		/// Layout 2 ( Login )
		
		/// Layout 3 (예약자 정보 )
		String 	name1="";	// 이름
		String 	name2=""; 	// 성
		String  tel1="";
		String  tel2="";
		String 	mail="";
		
		/// Layout 4 ( 전달사항  )
		String contents="";
		
		/// Layout 5 ( 객실 번호 )
		Integer roomIndex = 1;
		
		
		/// Layout 6 ( 투숙자 정보 ) ( 예약자 정보랑 공유 )
		String sex ="남자▼";
		
		
		/// Layout 7 ( 체크 박스  )
		 Boolean checkbox1 = false;
		 Boolean checkbox2 = false;
		 Boolean checkbox3 = false;
		
	}
	 

	Integer day;
	Integer month;
	Integer year;
	Calendar calendar = Calendar.getInstance();
	ArrayList< ReservationWorldData > m_ListData;
	private Main_Adapter m_Adapter;
				

	private ListView m_ListView;	 
	



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rese_world_main);
		
		

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
		
		
		{
        	
	     	m_ListView = ((ListView)findViewById(R.id.main_list));
	        	
	        	
	        	
	    }
	        
	    m_ListData = new ArrayList< ReservationWorldData >();
	    m_ListData.clear();
	    m_Adapter = new Main_Adapter(this, R.layout.reser_4_row, m_ListData);

	    m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		m_Adapter.notifyDataSetChanged();
		
		
		AfterCreate(7);
		
		
		GetInputData();
		RefreshUI();
		
		

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
	
	
	public void RefreshUI()
	{
		m_ListData.clear();
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			// Layout 1 
			{
				ReservationWorldData item  = new ReservationWorldData();
				
				item.type = 0;
				item.roomName = _AppManager.m_RoomDetailData.roomName;
				item.roomcount = _AppManager.m_NumRoom;
				item.roomman = _AppManager.m_NumPer;
				item.roomtype = ""; 
				item.roomreop = ""; 
				item.roomseop = ""; 
				
				{
					DecimalFormat df = new DecimalFormat("#,##0");

					item.price = "￦ " + df.format(Double.parseDouble(_AppManager.m_TotalPrice2)) ;
				}
				
				{
					
					DateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
					 Date tempDate = null;
					try {
						tempDate = sdFormat.parse(_AppManager.m_CheckInDay);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
					calendar.setTime(tempDate);
					
					year = calendar.get(Calendar.YEAR);
					month = calendar.get(Calendar.MONTH) + 1;
					day = calendar.get(Calendar.DAY_OF_MONTH);
					StringBuffer sbDate=new StringBuffer ( );
					sbDate.append ( year );
					sbDate.append ("/");
					if ( month < 10 ) sbDate.append ( "0" );
					sbDate.append ( month );
					sbDate.append ("/");
					if ( day < 10 ) sbDate.append ( "0" );
					sbDate.append ( day );
					
					
					
					item.day = sbDate + "~" + getDate(Integer.parseInt(_AppManager.m_Duration)) ;

					
				}
				m_ListData.add(item);
			}
			
			// Layout 2 
			if ( _AppManager.m_LoginCheck == false )
			{
				ReservationWorldData item  = new ReservationWorldData();
				
				item.type = 1;
				m_ListData.add(item);
			}
			
			// Layout 3
			{
				ReservationWorldData item  = new ReservationWorldData();
				
				item.type = 2;
				m_ListData.add(item);
			}
			// Layout 4
			
			if ( _AppManager.m_RemarkerData.size() == 0 )
			{
				
			}
			else
			{
				{
					ReservationWorldData item  = new ReservationWorldData();
					
					item.type = 3;
					m_ListData.add(item);
				}
				
				for ( int i = 0 ; i < _AppManager.m_RemarkerData.size(); i++)
				{
					ReservationWorldData item  = new ReservationWorldData();
					
					item.type = 17;
					item.name1 = _AppManager.m_RemarkerData.get(i).remarkName;
					item.contents = _AppManager.m_RemarkerData.get(i).remarkCode;;
					m_ListData.add(item);
				}
				{
					ReservationWorldData item  = new ReservationWorldData();
					
					item.type = 18;
					m_ListData.add(item);
				}
			}
			
			// Layout 5&6
			{
				for ( int i  = 0 ;  i < Integer.parseInt(_AppManager.m_NumRoom); i++)
				{
					ReservationWorldData item  = new ReservationWorldData();
					
					item.type = 4;
					item.roomIndex  = i +1;
					m_ListData.add(item);
					
					for ( int j= 0 ; j <Integer.parseInt(_AppManager.m_NumPer); j++  )
					{
						ReservationWorldData item2  = new ReservationWorldData();
						item2.type = 5;
						item2.roomIndex  = i +1;
						m_ListData.add(item2);
					}
				}
			}
			
			// Layout 7
			{
				ReservationWorldData item  = new ReservationWorldData();
				
				item.type = 6;
				m_ListData.add(item);
			}
		}
		
		m_Adapter.notifyDataSetChanged();
	}
	

	@Override
	public void onResume()
	{
		super.onResume();
		AppManagement _AppManager = (AppManagement) getApplication();
		if (_AppManager.m_RefreshUI == true )
		{
			RefreshUI();
			_AppManager.m_RefreshUI  = false;
		}
		
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
	
	
	public void Next()
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		
		_AppManager.m_RemarkString = ""; 
		// 데이터 확인 
		for ( int i = 0  ; i < m_ListData.size() ; i++ )
		{
			if (m_ListData.get(i).type  ==  6 )
			{
				if ( m_ListData.get(i).checkbox1== false )
				{
					self.ShowAlertDialLog( self ,"에러" , "약관에 동의해주세요");
					return;
				}
				else if ( m_ListData.get(i).checkbox2== false )
				{
					self.ShowAlertDialLog( self ,"에러" , "약관에 동의해주세요");
					return;
				}
				
				else if ( m_ListData.get(i).checkbox3== false )
				{
					self.ShowAlertDialLog( self ,"에러" , "약관에 동의해주세요");
					return;
				}
			}
			else if ( m_ListData.get(i).type  ==  2  )
			{
				if (   m_ListData.get(i).tel2.equals("") ||
						m_ListData.get(i).name1.equals("") || m_ListData.get(i).name2.equals("") ||
						m_ListData.get(i).mail.equals("") )
				{
					self.ShowAlertDialLog( self ,"에러" , "예약자 정보를 입력해주세요");
					return;
				}
					
			}
			else if ( m_ListData.get(i).type  ==  5  )
			{
				if (m_ListData.get(i).name1.equals("") || m_ListData.get(i).name2.equals("")  	)
				{
					self.ShowAlertDialLog( self ,"에러" , "투숙자 정보를 입력해주세요");
					return;
				}
			}
			// 요청사항 입력 
			else if (  m_ListData.get(i).type  == 17 )
			{
				if ( m_ListData.get(i).checkbox1== true )
				{
					_AppManager.m_RemarkString += m_ListData.get(i).contents + "|";
				}
			}
		}
		
		_AppManager.m_RoomGuestData.clear();
		
		// 데이터 입력 
		for ( int i = 0  ; i < m_ListData.size() ; i++ )
		{
			// 예약자 정보 입력 
			if ( m_ListData.get(i).type  ==  2  )
			{
				_AppManager.m_lodgeName =  m_ListData.get(i).name2;
				_AppManager.m_resvName = m_ListData.get(i).name1;
				_AppManager.m_resvEmail = m_ListData.get(i).mail;
				_AppManager.m_resvTel = m_ListData.get(i).tel1;
				_AppManager.m_resvMobile = m_ListData.get(i).tel2;
				_AppManager.m_resvPasswd = "";
			}
			else  if ( m_ListData.get(i).type  ==  3  )
			{
				_AppManager.m_resvIyagi =  m_ListData.get(i).contents;
			}
			// 투숙자 정보 입력 
			else  if ( m_ListData.get(i).type  ==  5  )
			{
				RoomGuestList item = new RoomGuestList();
				
				item.guestFirstName = m_ListData.get(i).name1;
				item.guestLastName = m_ListData.get(i).name2;
				
				item.roomTypeNum = m_ListData.get(i).roomIndex.toString();
				
				if (  m_ListData.get(i).sex.equals("남자▼") )
				{
					item.guestSex = "M";
				}
				else if ( m_ListData.get(i).sex.equals("여자▼"))
				{
					item.guestSex = "F";
				}
				
				_AppManager.m_RoomGuestData.add(item);
				
			}
		}
		
		Intent intent;
        intent = new Intent().setClass(baseself, ReservationConfirm2Activity.class);
        startActivity( intent ); 
		
		
		
	}
	

	public void GetInputData()
	{
		mProgress.show();
		//mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				
				Map<String, String> data = new HashMap  <String, String>();

				data.put("hotelCode", _AppManager.m_HotelCode);
				data.put("supplyCode", _AppManager.supplyCode);
				
				{
					SimpleDateFormat original_format = new SimpleDateFormat("yyyyMMdd");
					SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
					// 문자열 타입을 날짜 타입으로 변환한다. 
					Date original_date = null;
					try {
						original_date = original_format.parse(_AppManager.m_CheckInDay);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// 날짜 형식을 원하는 타입으로 변경한다. 
					String new_date = new_format.format(original_date);
					
					data.put("checkinDay", new_date);
				}
				
				data.put("duration", _AppManager.m_Duration);
				data.put("roomCount1", _AppManager.m_NumRoom);
				data.put("numAdults1", _AppManager.m_NumPer);
				
				data.put("numChild", "0");
				
				
				//data.put("roomCode", _AppManager.m_RoomDetailData.roomCode);
				
				String value = "";
				//value = EncodeString(data.get(key));
				try {
					value = new String(_AppManager.roomRequestKey.getBytes(), "utf-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
				data.put("roomRequestKey", _AppManager.roomRequestKey);
				

				
				/*for ( Integer i = 0 ; i < _AppManager.m_RoomOptionData.size(); i++ )
				{
					data.put("optionCode"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionCode);
					data.put("optionName"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionName);
					data.put("optionSendPrice"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionSendPrice);
					data.put("optionPrice"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionPrice);
					data.put("optionPriceType"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionPriceType);
					data.put("optionMethodType"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionMethodType);
					
				}*/
				

				String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mweb/booking/bookingFormInt.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						
						
						_AppManager.m_RoomReserData.roomName = 	json.optString("roomName");
						_AppManager.m_RoomReserData.roomCode = 	json.optString("roomCode");
						_AppManager.m_RoomReserData.roomPrice = 	json.optString("roomPrice");
						_AppManager.m_RoomReserData.totalPrice = 	json.optString("totalPrice");
						_AppManager.m_RoomReserData.numRooms = 	json.optString("numRooms");
						_AppManager.m_RoomReserData.lguCstMid = 	json.optString("lguCstMid");
						_AppManager.m_RoomReserData.nonRefundableYn = 	json.optString("nonRefundableYn");
						_AppManager.m_RoomReserData.priceFormula = 	json.optString("priceFormula");
						
						if( _AppManager.m_SearchWorld == true )
						{
							_AppManager.m_RoomReserData.currencyCode = 	json.optString("currencyCode");
							_AppManager.m_RoomReserData.nativeRoomPrice = 	json.optString("nativeRoomPrice");
							_AppManager.m_RoomReserData.nativeCurrencyCode = 	json.optString("nativeCurrencyCode");
							_AppManager.m_RoomReserData.promoYn = 	json.optString("promoYn");
							_AppManager.m_RoomReserData.promoCode = 	json.optString("promoCode");
							_AppManager.m_RoomReserData.promoDesc = 	json.optString("promoDesc");
							_AppManager.m_RoomReserData.breakfastYn = 	json.optString("breakfastYn");
							_AppManager.m_RoomReserData.bookRequestKey = 	json.optString("bookRequestKey");
							_AppManager.m_RoomReserData.nonResvInfoUpdateYn = 	json.optString("nonResvInfoUpdateYn");
						}



						
						{
							_AppManager.m_RoomCancel.clear();
							JSONArray usageList = (JSONArray)json.get("chargeConditions");
							for(int i = 0; i < usageList.length(); i++)
							{
								CancelPolicyList item = new CancelPolicyList();
								JSONObject list = (JSONObject)usageList.get(i);
								
								if ( list.optString("chargeYn").equals("1") )
								{
									item.cancelDay = list.optString("fromDate");
									//item.cancelPrice = list.optString("currencyCode");
									item.cancelPrice = list.optString("chargeAmount");
									_AppManager.m_RoomCancel.add(item);
								}
								
							}
						}
						
						// 투숙인원 입력폼 정보.
						{
							JSONArray usageList = (JSONArray)json.get("roomPersonList");
							_AppManager.m_HotelGuestData.clear(); 
							
							for(int i = 0; i < usageList.length(); i++)
							{
								
								JSONObject list = (JSONObject)usageList.get(i);
								int size = list.optInt("numAdults");
								ReservationPersonList[] item = new ReservationPersonList[size];
								
								JSONArray usageList2 = (JSONArray)list.optJSONArray("personList");
								for(int j = 0; j < usageList2.length(); j++)
								{
									JSONObject list2 = (JSONObject)usageList2.get(j);
									item[j] =new ReservationPersonList();
									item[j].numAdultName = list2.optString("numAdultName");
									item[j].bedTypeCode = list2.optString("bedTypeCode");
									item[j].roomTypeDetailCode = list2.optString("roomTypeDetailCode");
									
								}
								_AppManager.m_HotelGuestData.add( item);
							}
						}
						
						// 추가 요청 사항
						{
							_AppManager.m_RemarkerData.clear();
							JSONArray usageList = (JSONArray)json.get("remarkList");
							for(int i = 0; i < usageList.length(); i++)
							{
								ReservationRemarkerList item = new ReservationRemarkerList();
								JSONObject list = (JSONObject)usageList.get(i);
								
								item.remarkCode = list.optString("remarkCode");
								item.remarkName = list.optString("remarkName");
								_AppManager.m_RemarkerData.add(item);
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


		}
		
	}
	
	public class Main_Adapter extends ArrayAdapter<ReservationWorldData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<ReservationWorldData> mList;
		private LayoutInflater mInflater;
		
    	public Main_Adapter(Context context, int layoutResource, ArrayList<ReservationWorldData> mTweetList)
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
    		final ReservationWorldData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{


				FrameLayout detailBar1 = (FrameLayout)convertView.findViewById(R.id.main_row_1);
				LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.main_row_2);
				FrameLayout detailBar3 = (FrameLayout)convertView.findViewById(R.id.main_row_3);
				LinearLayout detailBar4 = (LinearLayout)convertView.findViewById(R.id.main_row_4);
				LinearLayout detailBar5 = (LinearLayout)convertView.findViewById(R.id.main_row_5);
				FrameLayout detailBar6 = (FrameLayout)convertView.findViewById(R.id.main_row_6);
				FrameLayout detailBar7 = (FrameLayout)convertView.findViewById(R.id.main_row_7);
				LinearLayout detailBar4_1 = (LinearLayout)convertView.findViewById(R.id.main_row_4_1);
				LinearLayout detailBar4_2 = (LinearLayout)convertView.findViewById(R.id.main_row_4_2);
				
				detailBar4_1.setVisibility(View.GONE);
				detailBar4_2.setVisibility(View.GONE);
				switch( mBar.type)
				{
				case 0:
				{
					detailBar1.setVisibility(View.VISIBLE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					detailBar5.setVisibility(View.GONE);
					detailBar6.setVisibility(View.GONE);
					detailBar7.setVisibility(View.GONE);
					
					
					/*((TextView)convertView.findViewById(R.id.roomname)).setText(mBar.roomName);
					((TextView)convertView.findViewById(R.id.roomday)).setText(mBar.day);
					((TextView)convertView.findViewById(R.id.roomcount)).setText(mBar.roomcount);
					((TextView)convertView.findViewById(R.id.roomman)).setText(mBar.roomman);
					((TextView)convertView.findViewById(R.id.roomreop)).setText(mBar.roomreop);
					((TextView)convertView.findViewById(R.id.roomtype)).setText(mBar.roomtype);
					((TextView)convertView.findViewById(R.id.roomseop)).setText(mBar.roomseop);*/
					String temp  =  "객실명 : " + mBar.roomName + "\n";
					temp  +=  "투숙일 : " + mBar.day + "\n";
					 temp  +=  "객실수 : " + mBar.roomcount + "\n";
					 
						
						if ( _AppManager.m_NumChild.equals("0") )
						{
							temp  +=  "객실당 인원 : " +  _AppManager.m_NumPer + "[성인]" + "\n";
						}
						else
						{
							temp  += "객실당 인원 : " +  _AppManager.m_NumPer + "/" +_AppManager.m_NumChild + "[성인/어린이]";
							
						}
					if ( !mBar.roomreop.equals(""))
						temp  +=  "필수옵션 : " + mBar.roomreop + "\n";
					if ( !mBar.roomtype.equals(""))
						temp  +=  "객실타입 : " + mBar.roomtype + "\n";
					if ( !mBar.roomseop.equals(""))
						temp  +=  "선택옵션 : " + mBar.roomseop + "\n";
					 

					((TextView)convertView.findViewById(R.id.roomname)).setText(temp);
					
					
					((TextView)convertView.findViewById(R.id.main_row_1_price)).setText(mBar.price);
				}
					break;
				case 1:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.VISIBLE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					detailBar5.setVisibility(View.GONE);
					detailBar6.setVisibility(View.GONE);
					detailBar7.setVisibility(View.GONE);
					
					((ImageView)convertView.findViewById(R.id.main_row_2_pic)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							
							Intent intent;
				            intent = new Intent().setClass(baseself, LoginActivity.class);
				            startActivity( intent );
							 

						}
					});
					
				}
					break;
				case 2:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.VISIBLE);
					detailBar4.setVisibility(View.GONE);
					detailBar5.setVisibility(View.GONE);
					detailBar6.setVisibility(View.GONE);
					detailBar7.setVisibility(View.GONE);
					
					if ( _AppManager.m_LoginCheck == true)
					{
						//((EditText)convertView.findViewById(R.id.name1)).setText(mBar.price);
						((EditText)convertView.findViewById(R.id.tel2)).setText(_AppManager.m_Mobile);
						((EditText)convertView.findViewById(R.id.mail)).setText(_AppManager.m_Email);
					}
					
					{
						final EditText  text2 = (EditText)convertView.findViewById(R.id.name1); 
						//선언
						TextWatcher watcher = new TextWatcher()
						{    
							@Override    
							public void afterTextChanged(Editable s)
							{         
								//텍스트 변경 후 발생할 이벤트를 작성. 
							}     
							@Override    
							public void beforeTextChanged(CharSequence s, int start, int count, int after)    
							{        
								//텍스트의 길이가 변경되었을 경우 발생할 이벤트를 작성.   
							}   
							@Override    
							public void onTextChanged(CharSequence s, int start, int before, int count)    
							{         
								//텍스트가 변경될때마다 발생할 이벤트를 작성.
								if (text2.isFocusable())         
								{             
									//m_SearchText EditText 가 포커스 되어있을 경우에만 실행됩니다.  
									// 검색어가 변경 될때 마다 데이터 가져오기 
									String Text = text2.getText().toString();
									mBar.name1 = Text;
								}   
							}
						};
						//호출
						text2.addTextChangedListener(watcher);
					}
					{
						final EditText  text2 = (EditText)convertView.findViewById(R.id.name2); 
						//선언
						TextWatcher watcher = new TextWatcher()
						{    
							@Override    
							public void afterTextChanged(Editable s)	{  }     
							@Override    
							public void beforeTextChanged(CharSequence s, int start, int count, int after)  { }   
							@Override    
							public void onTextChanged(CharSequence s, int start, int before, int count)    
							{         
								if (text2.isFocusable())         
								{             
									String Text = text2.getText().toString();
									mBar.name2 = Text;
								}   
							}
						};
						//호출
						text2.addTextChangedListener(watcher);
					}
					
					{
						final EditText  text2 = (EditText)convertView.findViewById(R.id.tel1); 
						//선언
						TextWatcher watcher = new TextWatcher()
						{    
							@Override    
							public void afterTextChanged(Editable s)	{  }     
							@Override    
							public void beforeTextChanged(CharSequence s, int start, int count, int after)  { }   
							@Override    
							public void onTextChanged(CharSequence s, int start, int before, int count)    
							{         
								if (text2.isFocusable())         
								{             
									String Text = text2.getText().toString();
									mBar.tel1 = Text;
								}   
							}
						};
						//호출
						text2.addTextChangedListener(watcher);
					}
					
					{
						final EditText  text2 = (EditText)convertView.findViewById(R.id.tel2); 
						//선언
						TextWatcher watcher = new TextWatcher()
						{    
							@Override    
							public void afterTextChanged(Editable s)	{  }     
							@Override    
							public void beforeTextChanged(CharSequence s, int start, int count, int after)  { }   
							@Override    
							public void onTextChanged(CharSequence s, int start, int before, int count)    
							{         
								if (text2.isFocusable())         
								{             
									String Text = text2.getText().toString();
									mBar.tel2 = Text;
								}   
							}
						};
						//호출
						text2.addTextChangedListener(watcher);
					}
					
					{
						final EditText  text2 = (EditText)convertView.findViewById(R.id.mail); 
						//선언
						TextWatcher watcher = new TextWatcher()
						{    
							@Override    
							public void afterTextChanged(Editable s)	{  }     
							@Override    
							public void beforeTextChanged(CharSequence s, int start, int count, int after)  { }   
							@Override    
							public void onTextChanged(CharSequence s, int start, int before, int count)    
							{         
								if (text2.isFocusable())         
								{             
									String Text = text2.getText().toString();
									mBar.mail = Text;
								}   
							}
						};
						//호출
						text2.addTextChangedListener(watcher);
					}
					
				}
					break;
				case 3:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.VISIBLE);
					detailBar5.setVisibility(View.GONE);
					detailBar6.setVisibility(View.GONE);
					detailBar7.setVisibility(View.GONE);
					
					detailBar4_1.setVisibility(View.GONE);
					detailBar4_2.setVisibility(View.GONE);
				}
					break;
				case 17:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					detailBar5.setVisibility(View.GONE);
					detailBar6.setVisibility(View.GONE);
					detailBar7.setVisibility(View.GONE);
					
					detailBar4_1.setVisibility(View.VISIBLE);
					detailBar4_2.setVisibility(View.GONE);
					
					
					final View convertView2 =  convertView ;
					View.OnClickListener lisetn = new View.OnClickListener()
					{
						public void onClick(View v) 
						{
							
							switch( v.getId())
							{
							case R.id.checkbox_4:
							{
								mBar.checkbox1 = !mBar.checkbox1;
								if ( mBar.checkbox1 == true )
								{
									((ImageView)convertView2.findViewById(R.id.checkbox_4)).setBackgroundResource(R.drawable.checkbox_2);
								}
								else
								{

									((ImageView)convertView2.findViewById(R.id.checkbox_4)).setBackgroundResource(R.drawable.checkbox_1);
								}
								
							}
								break;	
					
							}
						}
					};
					
					((TextView)convertView.findViewById(R.id.main_row_4_title)).setText(mBar.name1); 
					((ImageView)convertView.findViewById(R.id.checkbox_4)).setOnClickListener( lisetn );
				}
					break;
				case 18:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					detailBar5.setVisibility(View.GONE);
					detailBar6.setVisibility(View.GONE);
					detailBar7.setVisibility(View.GONE);
					
					detailBar4_1.setVisibility(View.GONE);
					detailBar4_2.setVisibility(View.VISIBLE);
				}
					break;
				case 4:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					detailBar5.setVisibility(View.VISIBLE);
					detailBar6.setVisibility(View.GONE);
					detailBar7.setVisibility(View.GONE);
					
					((TextView)convertView.findViewById(R.id.main_row_5_title)).setText("객실 " + mBar.roomIndex + " 투숙자명");
				}
					break;
				case 5:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					detailBar5.setVisibility(View.GONE);
					detailBar6.setVisibility(View.VISIBLE);
					detailBar7.setVisibility(View.GONE);
					
					final View convertView2 =  convertView ;
	
					{
						final EditText  text2 = (EditText)convertView.findViewById(R.id.name1_6); 
						//선언
						TextWatcher watcher = new TextWatcher()
						{    
							@Override    
							public void afterTextChanged(Editable s)	{  }     
							@Override    
							public void beforeTextChanged(CharSequence s, int start, int count, int after)  { }   
							@Override    
							public void onTextChanged(CharSequence s, int start, int before, int count)    
							{         
								if (text2.isFocusable())         
								{             
									String Text = text2.getText().toString();
									mBar.name1 = Text;
								}   
							}
						};
						//호출
						text2.addTextChangedListener(watcher);
					}
					
					{
						final EditText  text2 = (EditText)convertView.findViewById(R.id.name2_6); 
						//선언
						TextWatcher watcher = new TextWatcher()
						{    
							@Override    
							public void afterTextChanged(Editable s)	{  }     
							@Override    
							public void beforeTextChanged(CharSequence s, int start, int count, int after)  { }   
							@Override    
							public void onTextChanged(CharSequence s, int start, int before, int count)    
							{         
								if (text2.isFocusable())         
								{             
									String Text = text2.getText().toString();
									mBar.name2 = Text;
								}   
							}
						};
						//호출
						text2.addTextChangedListener(watcher);
					}
					
					
					((TextView)convertView.findViewById(R.id.main_row_6_sex)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							
							String []	NationList = {"남자", "여자"};
							
							
							AlertDialog.Builder alt_bld = new AlertDialog.Builder(self);
					        alt_bld.setTitle("성별 선택");
					        alt_bld.setSingleChoiceItems(NationList, -1, new DialogInterface.OnClickListener() 
					        {
					            public void onClick(DialogInterface dialog, int item) 
					            {
					            	if ( item == 0 )
					            	{
					            		((TextView)convertView2.findViewById(R.id.main_row_6_sex)).setText("남자▼");
					            		mBar.sex = "남자▼";
					            	}
					            	else
					            	{
					            		((TextView)convertView2.findViewById(R.id.main_row_6_sex)).setText("여자▼");
					            		mBar.sex = "여자▼";
					            	}
					            	
					            	dialog.cancel();
					            }
					        });
					        AlertDialog alert = alt_bld.create();
					        alert.show();
							 

						}
					});
					
				}
					break;
				case 6:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					detailBar5.setVisibility(View.GONE);
					detailBar6.setVisibility(View.GONE);
					detailBar7.setVisibility(View.VISIBLE);
					final View convertView2 =  convertView ;
					View.OnClickListener lisetn = new View.OnClickListener()
					{

						public void onClick(View v) 
						{
							
							switch( v.getId())
							{
							case R.id.detail_btn_3:
							{
								{
									/*_AppManager.m_URL = "http://m.hoteljoin.com/mweb/noMemberPolicy.html";
									_AppManager.m_URLTitle = "취소 규정 ";
									Intent intent;
						            intent = new Intent().setClass(baseself, WebActivity.class);
						            startActivity( intent );*/
									
									String temp = "";
									for ( int i = 0 ; i < _AppManager.m_RoomCancel.size() ; i++ )
									{
										temp += _AppManager.m_RoomCancel.get(i).cancelDay;
										temp += " ";
										temp +=  _AppManager.m_RoomCancel.get(i).cancelPrice;
										temp += "원의 위약금이 발생";
										temp += "\n";
									}
									self.ShowAlertDialLog( self ,"취소 규정" , temp );
									
								}
							}
								break;
							case R.id.detail_btn_2:
							{
								{
									_AppManager.m_URL = "http://m.hoteljoin.com/mweb/noMemberPolicy.html";
									_AppManager.m_URLTitle = "예약규정";
									Intent intent;
						            intent = new Intent().setClass(baseself, WebActivity.class);
						            startActivity( intent ); 
								}
							}
								break;
							case R.id.detail_btn_1:
							{
								{
									_AppManager.m_URL = "http://m.hoteljoin.com/mweb/noMemberPolicy.html";
									_AppManager.m_URLTitle = "개인정보 취급방침";
									Intent intent;
						            intent = new Intent().setClass(baseself, WebActivity.class);
						            startActivity( intent ); 
								}
							}
								break;
							case R.id.checkbox_3:
							{
								mBar.checkbox3 = !mBar.checkbox3;
								if ( mBar.checkbox3 == true )
								{
									
									((ImageView)convertView2.findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.checkbox_2);
								}
								else
								{
									
									((ImageView)convertView2.findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.checkbox_1);
								}
							}
								break;
							case R.id.checkbox_2:
							{
								mBar.checkbox2 = !mBar.checkbox2;
								if ( mBar.checkbox2 == true )
								{
									
									((ImageView)convertView2.findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.checkbox_2);
								}
								else
								{
									
									((ImageView)convertView2.findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.checkbox_1);
								}
							}
								break;
							case R.id.checkbox_1:
							{
								mBar.checkbox1 = !mBar.checkbox1;
								if ( mBar.checkbox1 == true )
								{
									((ImageView)convertView2.findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.checkbox_2);
								}
								else
								{

									((ImageView)convertView2.findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.checkbox_1);
								}
								
							}
								break;	
							case R.id.main_row_7_pic:
							{
								// 다음 페이지로. 
								self.Next();
							}
								break;
							}
							 

						}
					};
					
					((ImageView)convertView.findViewById(R.id.checkbox_1)).setOnClickListener( lisetn );
					((ImageView)convertView.findViewById(R.id.checkbox_2)).setOnClickListener( lisetn );
					((ImageView)convertView.findViewById(R.id.checkbox_3)).setOnClickListener( lisetn );
					((ImageView)convertView.findViewById(R.id.detail_btn_1)).setOnClickListener( lisetn );
					((ImageView)convertView.findViewById(R.id.detail_btn_2)).setOnClickListener( lisetn );
					((ImageView)convertView.findViewById(R.id.detail_btn_3)).setOnClickListener( lisetn );
					((ImageView)convertView.findViewById(R.id.main_row_7_pic)).setOnClickListener( lisetn );
					
					
				}
					break;
				}

				

				
			}
			return convertView;
		}
    }

    

}

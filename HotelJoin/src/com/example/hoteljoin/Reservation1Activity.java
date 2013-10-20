package com.example.hoteljoin;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.example.hoteljoin.RoomDetailData;
import com.example.hoteljoin.HotelSearchListActivity.SearchList_Adapter;
import com.example.hoteljoin.ReviewActivity.ReviewListData;
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
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
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

public class Reservation1Activity extends HotelJoinBaseActivity implements OnClickListener{
	Reservation1Activity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 String HotelName;
	 
	 Integer day;
	 Integer month;
	 Integer year;
	 
	 Integer stayDay = 1;
	 Integer stayCount = 1;
	 Integer statMan = 1;
	
	ArrayList< RoomDetailData > m_ListData;
	private Main_Adapter m_Adapter;
			

	private ListView m_ListView;	 

	Calendar calendar = Calendar.getInstance();
	
	
	DatePickerDialog.OnDateSetListener dateSetListener = 
			new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth) {
					calendar.set(year1, monthOfYear, dayOfMonth);
					
					year = year1;
					month =monthOfYear + 1;
					day = dayOfMonth;
					
					String CountDay = (String)((TextView)findViewById(R.id.hotel_day_number)).getText();
					((TextView)findViewById(R.id.hotel_day)).setText(year+ "/" + month + "/" + day);
					((TextView)findViewById(R.id.check_out)).setText("체크아웃 : "+ getDate(Integer.parseInt(CountDay)));
				}
			};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation_1);
		
		

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
		
		AfterCreate(7);
		
		
		((View)findViewById(R.id.popup_menu)).setVisibility(View.GONE);
		{
	        	
	     	m_ListView = ((ListView)findViewById(R.id.main_list));
	        	
	        	
	        	
	    }
	        
	    m_ListData = new ArrayList< RoomDetailData >();
	    m_ListData.clear();
	    m_Adapter = new Main_Adapter(this, R.layout.reser_1_row, m_ListData);

	    m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		m_Adapter.notifyDataSetChanged();
		GetData();

		
		{
			AppManagement _AppManager = (AppManagement) getApplication();
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
			
			String CountDay = (String)((TextView)findViewById(R.id.hotel_day_number)).getText();
			((TextView)findViewById(R.id.hotel_day)).setText(sbDate);
			((TextView)findViewById(R.id.check_out)).setText("체크아웃 : "+ getDate(Integer.parseInt(CountDay)));
			
		}
		BtnEvent(R.id.main_row_1_class);
		
		
		BtnEvent( R.id.hotel_day);
		BtnEvent( R.id.inn_day_p);
		BtnEvent( R.id.inn_day_m);;
		BtnEvent( R.id.inn_count_p);
		BtnEvent( R.id.inn_count_m);
		BtnEvent( R.id.inn_man_p);
		BtnEvent( R.id.inn_man_m);
		BtnEvent( R.id.search_btn);
		

		{
			AppManagement _AppManager = (AppManagement) getApplication();
			stayDay= Integer.parseInt(_AppManager.m_Duration);
			stayCount= Integer.parseInt(_AppManager.m_NumRoom);
			statMan= Integer.parseInt(_AppManager.m_NumPer);
		}
		
		

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
	

	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	public void BtnEvent( int id )
    {
		View imageview = (View)findViewById(id);
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
	
	
	
	@Override    
	protected Dialog onCreateDialog(int id)
	{       
		return new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));  
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.main_row_1_class:
		{
			((View)findViewById(R.id.popup_menu)).setVisibility(View.VISIBLE);
		}
			break;
		case R.id.hotel_day:
			showDialog(0);
			break;
		case R.id.inn_day_p:
			stayDay++ ;
			{
				((TextView)findViewById(R.id.check_out)).setText("체크아웃 : "+ getDate(stayDay));
			}
			break;
		case R.id.inn_day_m:
			stayDay-- ;
			if (stayDay < 1 )
				stayDay = 1;
			{
				((TextView)findViewById(R.id.check_out)).setText("체크아웃 : "+ getDate(stayDay));
			}

			break;
			
		case R.id.inn_count_p:
			stayCount++ ;
			break;
		case R.id.inn_count_m:
			stayCount-- ;
			if (stayCount < 1 )
				stayCount = 1;
			break;
		case R.id.inn_man_p:
			statMan++ ;
			break;
		case R.id.inn_man_m:
			statMan-- ;
			if (statMan < 1 )
				statMan = 1;
			break;
		case R.id.search_btn:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			m_ListData.clear();
			
			m_Adapter.notifyDataSetChanged();
			
			_AppManager.m_CheckInDay = year.toString();
			if ( month < 10 )
				_AppManager.m_CheckInDay += "0";
			_AppManager.m_CheckInDay += month.toString();
			
			if ( day < 10 )
				_AppManager.m_CheckInDay += "0";
			_AppManager.m_CheckInDay += day.toString();
			
			_AppManager.m_NumRoom = self.stayCount.toString();
			_AppManager.m_Duration = stayDay.toString();
			_AppManager.m_NumPer = statMan.toString();
			
			 GetData();
			 
			 ((View)findViewById(R.id.popup_menu)).setVisibility(View.GONE);
		}
			break;

		}
		RefreshUI2();
		
	}
	
	public void RefreshUI2()
	{
		((TextView)findViewById(R.id.hotel_day_number)).setText(stayDay.toString());
		((TextView)findViewById(R.id.hotel_room)).setText(stayCount.toString());
		((TextView)findViewById(R.id.hotel_room_man)).setText(statMan.toString());
		
	}
	
	public void RefreshUI()
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		((TextView)findViewById(R.id.hotel_name)).setText(HotelName);
		
		if ( month > 10 ) 
		{
			if ( day > 10 ) 
			{
				((TextView)findViewById(R.id.main_row_1_title)).setText("숙박일 : " + year.toString()+"/"+ month.toString()+"/" 
						+ day.toString() +"~"+ getDate(Integer.parseInt(_AppManager.m_Duration)));
			}
			else
			{
				((TextView)findViewById(R.id.main_row_1_title)).setText("숙박일 : " + year.toString()+"/"+ month.toString()+"/" 
						+"0"+ day.toString() +"~"+ getDate(Integer.parseInt(_AppManager.m_Duration)));
			}
		}
		else
		{
			if ( day > 10 ) 
			{
				((TextView)findViewById(R.id.main_row_1_title)).setText("숙박일 : " + year.toString()+"/"+ "0"+month.toString()+"/" 
						+ day.toString() +"~"+ getDate(Integer.parseInt(_AppManager.m_Duration)));
			}
			else
			{
				((TextView)findViewById(R.id.main_row_1_title)).setText("숙박일 : " + year.toString()+"/"+ "0"+month.toString()+"/" 
						+"0"+ day.toString() +"~"+ getDate(Integer.parseInt(_AppManager.m_Duration)));
			}
		}
		
		
		((TextView)findViewById(R.id.main_row_1_nick)).setText("객실수 : "+ _AppManager.m_NumRoom + "개"  );
		((TextView)findViewById(R.id.main_row_1_day)).setText("투숙인원 : " + _AppManager.m_NumPer+ "명");
		
		m_Adapter.notifyDataSetChanged();
	}

	
	
	
	public void GetData()
	{
		if ( mProgress.isShowing())
			return;
		mProgress.show();
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				data.put("hotelCode", _AppManager.m_HotelCode);
				data.put("checkinDay", _AppManager.m_CheckInDay);
				data.put("duration", _AppManager.m_Duration);
				data.put("numOfRooms", _AppManager.m_NumRoom);
				data.put("numPerRoom", _AppManager.m_NumPer);
					
				

				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/search/searchHotelDetailRoomList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{

						HotelName =  _AppManager.GetHttpManager().DecodeString(json.optString("hotelName"));
						JSONArray usageList = (JSONArray)json.optJSONArray("roomPriceList");
						_AppManager.m_HotelName = HotelName;

	
							
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList.length(); i++)
						{
							RoomDetailData item = new RoomDetailData();
							JSONObject list = (JSONObject)usageList.get(i);


							item.roomCode =  _AppManager.GetHttpManager().DecodeString(list.optString("roomCode"));
							item.roomName =  list.optString("roomName");
							item.roomIncInfo =  list.optString("roomIncInfo");
							item.roomPrice =  _AppManager.GetHttpManager().DecodeString(list.optString("roomPrice"));
							item.breakfastYn =  _AppManager.GetHttpManager().DecodeString(list.optString("breakfastYn"));
							item.availableCode =  _AppManager.GetHttpManager().DecodeString(list.optString("availableCode"));
							item.promoYn =  _AppManager.GetHttpManager().DecodeString(list.optString("promoYn"));
							if ( item.promoYn.equals("1"))
								item.promoDesc =  list.optString("promoDesc");
							
							if (_AppManager.m_SearchWorld == false)
							{
								item.continueType =  _AppManager.GetHttpManager().DecodeString(list.optString("continueType"));
								item.continueDay =  _AppManager.GetHttpManager().DecodeString(list.optString("continueDay"));
							}
							else
							{
								item.supplyCode =  _AppManager.GetHttpManager().DecodeString(list.optString("supplyCode"));
								item.roomRequestKey =  _AppManager.GetHttpManager().DecodeString(list.optString("roomRequestKey"));
							}

							m_ListData.add(item);
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
			case 23:
			{

				RefreshUI();
			}
			case 11:
			{
				
				RefreshUI();

				
			}
				break;
			default:
				break;
			}

		}
    	
	};	
	
	@Override
 	protected void onStop() {
 		// TODO Auto-generated method stub
 		super.onStop();
 		if(true) 
 			Log.i(TAG, "Release Dialog Resource");
 		if ( mProgress != null)
 			mProgress.dismiss();
 	}
	
	
	public class Main_Adapter extends ArrayAdapter<RoomDetailData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<RoomDetailData> mList;
		private LayoutInflater mInflater;
		
    	public Main_Adapter(Context context, int layoutResource, ArrayList<RoomDetailData> mTweetList)
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
    		final RoomDetailData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{


				FrameLayout detailBar1 = (FrameLayout)convertView.findViewById(R.id.main_row_1);
				FrameLayout detailBar2 = (FrameLayout)convertView.findViewById(R.id.main_row_2);
				
				if ( mBar.promoYn.equals("0"))
				{
					detailBar1.setVisibility(View.VISIBLE);
					detailBar2.setVisibility(View.GONE);
					((TextView)convertView.findViewById(R.id.main_row_1_title)).setText(mBar.roomName);
					((TextView)convertView.findViewById(R.id.main_row_1_day)).setText(mBar.roomPrice);
					
					{
						DecimalFormat df = new DecimalFormat("#,##0");

						((TextView)convertView.findViewById(R.id.main_row_1_day)).setText("￦ " + df.format(Double.parseDouble(mBar.roomPrice)) );
					}
					
					if (mBar.breakfastYn.equals("1"))
						((TextView)convertView.findViewById(R.id.main_row_1_nick)).setText("조식 포함");
					else
						((TextView)convertView.findViewById(R.id.main_row_1_nick)).setText("");
					
					detailBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							_AppManager.m_RoomDetailData = mBar;
							if (_AppManager.m_SearchWorld == false)
							{
								Intent intent;
					            intent = new Intent().setClass(baseself, Reservation2Activity.class);
					            startActivity( intent );
							}
							else
							{
								_AppManager.supplyCode = mBar.supplyCode;
								_AppManager.roomRequestKey = mBar.roomRequestKey;
								_AppManager.m_TotalPrice2 = mBar.roomPrice;
								Intent intent;
					            intent = new Intent().setClass(baseself, Reservatio_WorldActivity.class);
					            startActivity( intent );
							}
							 

						}
					});
				}
				else
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.VISIBLE);
					
					((TextView)convertView.findViewById(R.id.main_row_2_title)).setText(mBar.roomName);
					((TextView)convertView.findViewById(R.id.main_row_2_day)).setText(mBar.roomPrice);
					{
						DecimalFormat df = new DecimalFormat("#,##0");

						((TextView)convertView.findViewById(R.id.main_row_2_day)).setText("￦ " + df.format(Double.parseDouble(mBar.roomPrice)) );
					}
					if (mBar.breakfastYn.equals("1"))
						((TextView)convertView.findViewById(R.id.main_row_2_nick)).setText("조식 포함");
					else
						((TextView)convertView.findViewById(R.id.main_row_2_nick)).setText("");
					
					((TextView)convertView.findViewById(R.id.main_row_2_promo)).setText(mBar.promoDesc);
					
					detailBar2.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							
							_AppManager.m_RoomDetailData = mBar;
							if (_AppManager.m_SearchWorld == false)
							{
								Intent intent;
					            intent = new Intent().setClass(baseself, Reservation2Activity.class);
					            startActivity( intent );
							}
							else
							{
								_AppManager.supplyCode = mBar.supplyCode;
								_AppManager.roomRequestKey = mBar.roomRequestKey;
								_AppManager.m_TotalPrice2 = mBar.roomPrice;
								
								Intent intent;
					            intent = new Intent().setClass(baseself, Reservatio_WorldActivity.class);
					            startActivity( intent );
							}

						}
					});
					
				}
				
			}
			return convertView;
		}
    }
	

    

}

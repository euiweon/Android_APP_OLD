package com.example.hoteljoin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
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

public class Reservation2Activity extends HotelJoinBaseActivity implements OnClickListener{
	Reservation2Activity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 

	 public class ReservationOptionData
	 {
		 ReservationOptionData()
		 {
			 
		 }
		 int type;
		 String title;
		 String price;
		 String desc;
		 Boolean check = false;
		 String code;
	 }

	ArrayList< ReservationOptionData > m_ListData;
	private Main_Adapter m_Adapter;
				

	private ListView m_ListView;	
	ArrayList< RoomOptionData > m_ListOptionData;
	
	int temprecode =0;
	int tempsecode =0;
	int temproomcode = 0;
	
	Integer child = 0;

	String RoomName;
	String RoomPrice;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation_2);
		
		

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
		
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			((TextView)findViewById(R.id.hotel_name)).setText(_AppManager.m_HotelName);
		}
	        
	    m_ListData = new ArrayList< ReservationOptionData >();
	    m_ListData.clear();
	    m_Adapter = new Main_Adapter(this, R.layout.reser_2_row, m_ListData);

	    m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		m_Adapter.notifyDataSetChanged();
		
		m_ListOptionData =  new ArrayList< RoomOptionData >();
		
		
		AfterCreate(7);

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

	
	public void RefreshUI()
	{

		m_ListData.clear();
		
		Boolean option1 = false; 
		Boolean option2 = false;
		Boolean option3 = false;
		
		AppManagement _AppManager = (AppManagement) getApplication();
		// Header
		{
			ReservationOptionData item = new ReservationOptionData();
			item.type = 0;
			item.title = _AppManager.m_RoomDetailData.roomName;
			item.price = _AppManager.m_RoomDetailData.roomPrice;
			
			m_ListData.add(item);
		}
		
		{
			ReservationOptionData item = new ReservationOptionData();
			item.type = 1;
			item.desc = _AppManager.m_RoomDetailData.roomIncInfo;
			m_ListData.add(item);
		}
		
		for ( int i = 0 ; i < m_ListOptionData.size() ; i++ )
		{
			if( m_ListOptionData.get(i).optionMethodType.equals("S"))
			{
				option1 = true;
				break;
			}
		}
		for ( int i = 0 ; i < m_ListOptionData.size() ; i++ )
		{
			if( m_ListOptionData.get(i).optionMethodType.equals("N"))
			{
				option2 = true;
				break;
			}
		}
		
		for ( int i = 0 ; i < m_ListOptionData.size() ; i++ )
		{
			if( m_ListOptionData.get(i).optionMethodType.equals("Y"))
			{
				option3 = true;
				break;
			}
		}
		
		if (option1 || option2|| option3)
		{
			{
				ReservationOptionData item = new ReservationOptionData();
				item.type = 2;
				m_ListData.add(item);
			}
		}
		
		//  필수 옵션
		if ( option3 == true)
		{
				
			for ( int i = 0 ; i < m_ListOptionData.size() ; i++ )
			{
				if( m_ListOptionData.get(i).optionMethodType.equals("Y"))
				{
					ReservationOptionData item = new ReservationOptionData();
					item.type = 9;
					item.title = m_ListOptionData.get(i).optionName;
					item.price = m_ListOptionData.get(i).optionPrice; 
					item.code = m_ListOptionData.get(i).optionCode;
					m_ListData.add(item);
				}
			}
		}
		
		//  필수 옵션
		if ( option1 == true)
		{
			
			
			{
				ReservationOptionData item = new ReservationOptionData();
				item.type = 3;
				m_ListData.add(item);
			}
			
			for ( int i = 0 ; i < m_ListOptionData.size() ; i++ )
			{
				if( m_ListOptionData.get(i).optionMethodType.equals("S"))
				{
					ReservationOptionData item = new ReservationOptionData();
					item.type = 4;
					item.title = m_ListOptionData.get(i).optionName;
					item.price = m_ListOptionData.get(i).optionPrice; 
					item.code = m_ListOptionData.get(i).optionCode;
					m_ListData.add(item);
				}
			}
		}
		
		if ( option2 == true)
		{
			
			
			{
				ReservationOptionData item = new ReservationOptionData();
				item.type = 5;
				m_ListData.add(item);
			}
			
			for ( int i = 0 ; i < m_ListOptionData.size() ; i++ )
			{
				if( m_ListOptionData.get(i).optionMethodType.equals("N"))
				{
					ReservationOptionData item = new ReservationOptionData();
					item.type = 6;
					item.title = m_ListOptionData.get(i).optionName;
					item.price = m_ListOptionData.get(i).optionPrice; 
					item.code = m_ListOptionData.get(i).optionCode;
					m_ListData.add(item);
				}
			}
		}
		
		{
			ReservationOptionData item = new ReservationOptionData();
			item.type = 7;
			m_ListData.add(item);
		}
		
		{
			ReservationOptionData item = new ReservationOptionData();
			item.type = 8;
			Double Price = ( Double.parseDouble(_AppManager.m_RoomDetailData.roomPrice) + 
					(Double.parseDouble(_AppManager.m_RoomDetailData.roomPrice) *21 ) /100);
			double tempprice = Price;
			Integer price2 = (int)tempprice;
			item.price = price2.toString();
			m_ListData.add(item);
		}


		
		m_Adapter.notifyDataSetChanged();
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
	
	public void SetRequiredOption()
	{
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if( m_ListData.get(i).type == 4)
			{
				m_ListData.get(i).check = false;
			}
		}
		
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if(m_ListData.get(i).code != null && Integer.parseInt(m_ListData.get(i).code) == temprecode)
			{
				m_ListData.get(i).check = true;
			}
		}
		AppManagement _AppManager = (AppManagement) getApplication();
		Integer price = (int)Double.parseDouble(_AppManager.m_RoomDetailData.roomPrice);
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if( m_ListData.get(i).type == 4 || m_ListData.get(i).type == 6  || m_ListData.get(i).type == 9 )
			{
				if (m_ListData.get(i).check  )
				{
					price += Integer.parseInt(m_ListData.get(i).price);
				}
			}
		}
		Integer Price2 = 0;
		{
			Price2 = price + ( (price * 21) / 100 ) ;
		}
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if( m_ListData.get(i).type == 8)
			{
				m_ListData.get(i).price = Price2.toString();
			}
		}
		
		
		m_Adapter.notifyDataSetChanged();
	}
	
	public void SetRoomOption()
	{
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if( m_ListData.get(i).type == 9)
			{
				m_ListData.get(i).check = false;
			}
		}
		
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if( m_ListData.get(i).code != null &&Integer.parseInt(m_ListData.get(i).code) == temproomcode)
			{
				m_ListData.get(i).check = true;
			}
		}
		
		AppManagement _AppManager = (AppManagement) getApplication();
		Integer price = (int)Double.parseDouble(_AppManager.m_RoomDetailData.roomPrice);
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if( m_ListData.get(i).type == 4 || m_ListData.get(i).type == 6  || m_ListData.get(i).type == 9 )
			{
				if (m_ListData.get(i).check  )
				{
					price += Integer.parseInt(m_ListData.get(i).price);
				}
			}
		}
		Integer Price2 = 0;
		{
			Price2 = price + ( (price * 21) / 100 ) ;
		}
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if( m_ListData.get(i).type == 8)
			{
				m_ListData.get(i).price = Price2.toString();
			}
		}
		
		
		m_Adapter.notifyDataSetChanged();
	}

	public void SetSelectOption()
	{

		
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if( m_ListData.get(i).code != null &&Integer.parseInt(m_ListData.get(i).code) == tempsecode)
			{
				m_ListData.get(i).check = !m_ListData.get(i).check ;
			}
		}
		
		AppManagement _AppManager = (AppManagement) getApplication();
		Integer price = (int)Double.parseDouble(_AppManager.m_RoomDetailData.roomPrice);
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if( m_ListData.get(i).type == 4 || m_ListData.get(i).type == 6  || m_ListData.get(i).type == 9 )
			{
				if (m_ListData.get(i).check  )
				{
					price += Integer.parseInt(m_ListData.get(i).price);
				}
			}
		}
		
		Integer Price2 = 0;
		{
			Price2 = price + ( (price * 21) / 100 ) ;
		}
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if( m_ListData.get(i).type == 8)
			{
				m_ListData.get(i).price = Price2.toString();
			}
		}
		
		m_Adapter.notifyDataSetChanged();
	}
	
	
	public void Next()
	{
		Boolean check1 = false;
		Boolean check2 = false;
		
		int count = 0;
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if(m_ListData.get(i).type ==4 && m_ListData.get(i).check == true)
			{
				check1 = true;
				count++ ;
				break;
			}
		}
		
		if ( count == 0 )
			check1 = true;
			
		count = 0;
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if(m_ListData.get(i).type ==9 && m_ListData.get(i).check == true)
			{
				check2 = true;
				count++;
				break;
			}
		}
		if ( count == 0 )
			check2 = true;
		
		if ( !(check1 && check2) )
		{
			self.ShowAlertDialLog( self ,"에러" , "객실 선택 및 필수 옵션을 선택해주세요");
			return;
		}
		
		AppManagement _AppManager = (AppManagement) getApplication();
		_AppManager.m_RoomOptionData.clear();
		
		for ( int i = 0 ; i <  m_ListOptionData.size(); i++ )
		{
			for ( int j = 0 ; j <  m_ListData.size(); j++ )
			{
				if ( m_ListData.get(j).check == true && m_ListData.get(j).code.equals(m_ListOptionData.get(i).optionCode) )
				{
					_AppManager.m_RoomOptionData.add(m_ListOptionData.get(i));
				}
			}
		}
		
		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if(m_ListData.get(i).type ==8 )
			{
				_AppManager.m_TotalPrice = m_ListData.get(i).price;
				break;
			}
		}
		
		_AppManager.m_NumChild = self.child.toString();
		
		
		
		
		Intent intent;
        intent = new Intent().setClass(baseself, Reservatio_KoreanActivity.class);
        startActivity( intent );
	}
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{


		}
		
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


				data.put("hotelCode", _AppManager.m_HotelCode);
				data.put("roomCode", _AppManager.m_RoomDetailData.roomCode);
				//data.put("roomRequestKey", _AppManager.m_RoomDetailData.roomRequestKey);
				data.put("checkinDay", _AppManager.m_CheckInDay);
				data.put("duration", _AppManager.m_Duration);
				data.put("numOfRooms", _AppManager.m_NumRoom);
				data.put("numPerRoom", _AppManager.m_NumPer);
					
				

				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/search/searchHotelDetailRoomOptionList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						JSONArray usageList = (JSONArray)json.get("optionList");
						

	
							
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList.length(); i++)
						{
							RoomOptionData item = new RoomOptionData();
							JSONObject list = (JSONObject)usageList.get(i);

							item.optionCode =  _AppManager.GetHttpManager().DecodeString(list.optString("optionCode"));
							item.optionName =  _AppManager.GetHttpManager().DecodeString(list.optString("optionName"));
							item.optionSendPrice =  _AppManager.GetHttpManager().DecodeString(list.optString("optionSendPrice"));
							item.optionPrice =  _AppManager.GetHttpManager().DecodeString(list.optString("optionPrice"));
							item.optionPriceType =  _AppManager.GetHttpManager().DecodeString(list.optString("optionPriceType"));
							item.optionMethodType =  _AppManager.GetHttpManager().DecodeString(list.optString("optionMethodType"));
							

							m_ListOptionData.add(item);
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
	
	
	public class Main_Adapter extends ArrayAdapter<ReservationOptionData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<ReservationOptionData> mList;
		private LayoutInflater mInflater;
		
    	public Main_Adapter(Context context, int layoutResource, ArrayList<ReservationOptionData> mTweetList)
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
    		final ReservationOptionData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				FrameLayout detailBar1 = (FrameLayout)convertView.findViewById(R.id.main_row_1);
				LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.main_row_2);
				LinearLayout detailBar3 = (LinearLayout)convertView.findViewById(R.id.main_row_3);
				LinearLayout detailBar4 = (LinearLayout)convertView.findViewById(R.id.main_row_4);
				FrameLayout detailBar5 = (FrameLayout)convertView.findViewById(R.id.main_row_5);
				LinearLayout detailBar6 = (LinearLayout)convertView.findViewById(R.id.main_row_6);
				FrameLayout detailBar7 = (FrameLayout)convertView.findViewById(R.id.main_row_7);
				FrameLayout detailBar8 = (FrameLayout)convertView.findViewById(R.id.main_row_8);
				FrameLayout detailBar9 = (FrameLayout)convertView.findViewById(R.id.main_row_9);
				FrameLayout detailBar10 = (FrameLayout)convertView.findViewById(R.id.main_row_10);
				
				switch(mBar.type)
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
					detailBar8.setVisibility(View.GONE);
					detailBar9.setVisibility(View.GONE);
					detailBar10.setVisibility(View.GONE);
					
					((TextView)convertView.findViewById(R.id.main_row_1_title)).setText(mBar.title);
					//((TextView)convertView.findViewById(R.id.main_row_1_day)).setText(mBar.price);
					
					if ( mBar.price.equals("0"))
					{
						((TextView)convertView.findViewById(R.id.main_row_1_day)).setText("");
					}
					else	
					{
						DecimalFormat df = new DecimalFormat("#,##0");

						((TextView)convertView.findViewById(R.id.main_row_1_day)).setText("￦ " + df.format(Double.parseDouble(mBar.price)) );
					}

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
					detailBar8.setVisibility(View.GONE);
					detailBar9.setVisibility(View.GONE);
					detailBar10.setVisibility(View.GONE);
					
					((TextView)convertView.findViewById(R.id.main_row_2_title)).setText(mBar.desc);
					

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
					detailBar8.setVisibility(View.GONE);
					detailBar9.setVisibility(View.GONE);
					detailBar10.setVisibility(View.GONE);
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
					detailBar8.setVisibility(View.GONE);
					detailBar9.setVisibility(View.GONE);
					detailBar10.setVisibility(View.GONE);
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
					detailBar8.setVisibility(View.GONE);
					detailBar9.setVisibility(View.GONE);
					detailBar10.setVisibility(View.GONE);
					
					
					((TextView)convertView.findViewById(R.id.main_row_5_title)).setText(mBar.title);
					detailBar5.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							temprecode = Integer.parseInt(mBar.code);
							SetRequiredOption();

						}
					});
					
					if ( mBar.price.equals("0"))
					{
						((TextView)convertView.findViewById(R.id.main_row_5_day)).setText("");
					}
					else
						
					{
						DecimalFormat df = new DecimalFormat("#,##0");

						((TextView)convertView.findViewById(R.id.main_row_5_day)).setText("￦ " + df.format(Integer.parseInt(mBar.price)) );
					}
					if( mBar.check)
					{
						((ImageView)convertView.findViewById(R.id.main_row_5_pic)).setBackgroundResource(R.drawable.hotelreserve_detail2_r_2_bt);
					}
					else
					{
						((ImageView)convertView.findViewById(R.id.main_row_5_pic)).setBackgroundResource(R.drawable.hotelreserve_detail2_r_1_bt);
					}
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
					detailBar8.setVisibility(View.GONE);
					detailBar9.setVisibility(View.GONE);
					detailBar10.setVisibility(View.GONE);
					
					
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
					detailBar8.setVisibility(View.GONE);
					detailBar9.setVisibility(View.GONE);
					detailBar10.setVisibility(View.GONE);
					((TextView)convertView.findViewById(R.id.main_row_7_title)).setText(mBar.title);
					
					if ( mBar.price.equals("0"))
					{
						((TextView)convertView.findViewById(R.id.main_row_7_day)).setText("");
					}
					else
					{
						DecimalFormat df = new DecimalFormat("#,##0");

						((TextView)convertView.findViewById(R.id.main_row_7_day)).setText("￦ " +df.format(Integer.parseInt(mBar.price)) );
					}
					
					detailBar7.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							tempsecode = Integer.parseInt(mBar.code);
							SetSelectOption();

						}
					});
					
					if( mBar.check)
					{
						((ImageView)convertView.findViewById(R.id.main_row_7_pic)).setBackgroundResource(R.drawable.checkbox_2);
					}
					else
					{
						((ImageView)convertView.findViewById(R.id.main_row_7_pic)).setBackgroundResource(R.drawable.checkbox_1);
					}
				}
					break;
				case 7:
				{
					final View tempview = convertView;
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					detailBar5.setVisibility(View.GONE);
					detailBar6.setVisibility(View.GONE);
					detailBar7.setVisibility(View.GONE);
					detailBar8.setVisibility(View.VISIBLE);
					detailBar9.setVisibility(View.GONE);
					detailBar10.setVisibility(View.GONE);
					((ImageView)convertView.findViewById(R.id.inn_man_p)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.child++;
							if (self.child > 9 )
								self.child = 9;
							((TextView)tempview.findViewById(R.id.hotel_room_man)).setText( self.child.toString() );

						}
					});
					
					((ImageView)convertView.findViewById(R.id.inn_man_m)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.child--;
							
							if (self.child < 0 )
								self.child = 0;
							((TextView)tempview.findViewById(R.id.hotel_room_man)).setText( self.child.toString() );

						}
					});
				}
					break;
				case 8:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					detailBar5.setVisibility(View.GONE);
					detailBar6.setVisibility(View.GONE);
					detailBar7.setVisibility(View.GONE);
					detailBar8.setVisibility(View.GONE);
					detailBar9.setVisibility(View.VISIBLE);
					detailBar10.setVisibility(View.GONE);
					
					
					if ( mBar.price.equals("0"))
					{
						((TextView)convertView.findViewById(R.id.main_row_9_day)).setText("");
					}
					else
						

					{
						DecimalFormat df = new DecimalFormat("#,##0");

						((TextView)convertView.findViewById(R.id.main_row_9_day)).setText("￦ " + df.format(Integer.parseInt(mBar.price)) );
					}
					
					((ImageView)convertView.findViewById(R.id.login_btn)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							Next();
						}
					});
					
				}
					break;

				case 9:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					detailBar5.setVisibility(View.GONE);
					detailBar6.setVisibility(View.GONE);
					detailBar7.setVisibility(View.GONE);
					detailBar8.setVisibility(View.GONE);
					detailBar9.setVisibility(View.GONE);
					detailBar10.setVisibility(View.VISIBLE);
					
					
					((TextView)convertView.findViewById(R.id.main_row_10_title)).setText(mBar.title);
					detailBar10.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							temproomcode = Integer.parseInt(mBar.code);
							SetRoomOption();

						}
					});
					
					if ( mBar.price.equals("0"))
					{
						((TextView)convertView.findViewById(R.id.main_row_10_day)).setText("");
					}
					else
					{
						DecimalFormat df = new DecimalFormat("#,##0");

						((TextView)convertView.findViewById(R.id.main_row_10_day)).setText("￦ " + df.format(Integer.parseInt(mBar.price)) );
					}
					if( mBar.check)
					{
						((ImageView)convertView.findViewById(R.id.main_row_10_pic)).setBackgroundResource(R.drawable.hotelreserve_detail2_r_2_bt);
					}
					else
					{
						((ImageView)convertView.findViewById(R.id.main_row_10_pic)).setBackgroundResource(R.drawable.hotelreserve_detail2_r_1_bt);
					}
				}
					break;

				}
				
			}
			return convertView;
		}
    }
	

    

}

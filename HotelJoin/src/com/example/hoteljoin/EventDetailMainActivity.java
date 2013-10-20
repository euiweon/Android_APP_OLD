package com.example.hoteljoin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.example.hoteljoin.HotelSearchActivity.Destination;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;




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
import android.text.Editable;
import android.text.TextWatcher;
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

public class EventDetailMainActivity extends HotelJoinBaseActivity implements OnClickListener{

	public class EventHotelData
	{
		EventHotelData()
		{
			
		}
		String hotelCode;
		String hotelName;
		String imageUrl;
		String starRating;
		Integer type;


	}



	EventDetailMainActivity self;
	 SlidingMenu menu ;
	 int MenuSize;
	 
	 Integer day;
	 Integer month;
	 Integer year;
	 
	 Integer stayDay = 1;
	 Integer stayCount = 1;
	 Integer statMan = 1;
	 
	 Integer m_CurrentTab =0;
	 
	 Integer m_CurrentPage = 1;
	 
	 Integer m_TotalPage = 1;
	 
	 
	 String eventNum;
	 String subject;
	 String summary;
	 String bgnDay;
	 String endDay;
	 String imageUrl;

	 
	 
	 

	 
	ArrayList< EventHotelData > m_ListData;
	private EventDetail_Adapter m_Adapter;
		
	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	
	private LayoutInflater mInflater;

	private ListView m_ListView;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_detail);
		
		self = this;

		{
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }

		// 비하인드 뷰 설정
		setBehindContentView(R.layout.main_menu_1);
		

		
		// 슬라이딩 뷰
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.RIGHT);
		
		{
			Display defaultDisplay = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    	
			int width;
			@SuppressWarnings("deprecation")
			int windowswidth = defaultDisplay.getWidth();
			@SuppressWarnings("deprecation")
			int windowsheight= defaultDisplay.getHeight();
			int originwidth = 480;
			

			width = (int) ( (float) 359 * ( (float)(windowswidth)/ (float)(originwidth) )  );

			
			sm.setBehindOffset(windowswidth - width );
		}
		
		sm.setFadeDegree(0.35f);
		
		
		AfterCreate(7);
		


       
        
        
        {
        	OnScrollListener listen = new OnScrollListener() 
        	{
      		  
     		   public void onScrollStateChanged(AbsListView view, int scrollState) {}
     		  
     		   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
     		   {

	        		    
	        		    // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이 
	        		    // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다. 
	        		    int count = totalItemCount - visibleItemCount; 

	        		    if(firstVisibleItem >= count && totalItemCount != 0   && mLockListView == false) 
	        		    { 
	        		    	      // 추가
	        		    	
	        		    	if (m_bFooter == true )
	        		    	{
	        		    		//GetMoreAlbumList();
	        		    	}

	        		    		
	        		    } 
     		   }
        	};
        	m_ListView = ((ListView)findViewById(R.id.event_list));
        	
        	m_ListView.setOnScrollListener(listen);
        	
        }
        
        m_ListData = new ArrayList< EventHotelData >();
        m_ListData.clear();
    	m_Adapter = new EventDetail_Adapter(this, R.layout.search_list_row, m_ListData);
    	

        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		

		
		
		mLockListView = true; 
        

		RefreshUI();
		
		GetData();
		
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{

					
		}
		
		RefreshUI();
		
	}

	
	
	
		public void GetData()
		{

			final  AppManagement _AppManager = (AppManagement) getApplication();
			
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					Map<String, String> data = new HashMap  <String, String>();
					data.put("eventNum", _AppManager.m_EventNum.toString());

					
		
					
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/event/eventDetail.gm", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("errorCode").equals("0"))
						{
							JSONObject usageList = (JSONObject)json.get("eventInfo");
							

							

								subject = _AppManager.GetHttpManager().DecodeString(usageList.optString("subject",""));
								summary = usageList.optString("summary","");
								bgnDay = _AppManager.GetHttpManager().DecodeString(usageList.optString("bgnDay"));
								endDay = _AppManager.GetHttpManager().DecodeString(usageList.optString("endDay",""));
								imageUrl =  _AppManager.GetHttpManager().DecodeString(usageList.optString("imageUrl",""));
							
							
							
							JSONArray usageList2 = (JSONArray)json.get("domHotelList");
							

							for(int i = 0; i < usageList2.length(); i++)
							{
								JSONObject list = (JSONObject)usageList2.get(i);
								
								EventHotelData hoteldata = new EventHotelData();
								hoteldata.type = 0;
								hoteldata.hotelCode = _AppManager.GetHttpManager().DecodeString(list.optString("hotelCode",""));
								hoteldata.hotelName = _AppManager.GetHttpManager().DecodeString(list.optString("hotelName",""));
								
								hoteldata.imageUrl = _AppManager.GetHttpManager().DecodeString(list.optString("imageUrl",""));
								hoteldata.starRating = _AppManager.GetHttpManager().DecodeString(list.optString("starRating",""));
								
								m_ListData.add(hoteldata);

							}


							handler.sendEmptyMessage(0);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.optString("errorMsg","")) ));
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
					{
						ImageView Image = (ImageView)findViewById(R.id.event_image);
						
						Image.setTag( imageUrl);;
						BitmapManager.INSTANCE.loadBitmap_3(imageUrl, Image);
					}
					m_Adapter.notifyDataSetChanged();
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

				case 20:
					break;
				default:
					break;
				}

			}
	    	
		};	
		
	
	
	public class EventDetail_Adapter extends ArrayAdapter<EventHotelData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<EventHotelData> mList;
		private LayoutInflater mInflater;
		
    	public EventDetail_Adapter(Context context, int layoutResource, ArrayList<EventHotelData> mTweetList)
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
    		final EventHotelData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				
				FrameLayout detailBar1 = (FrameLayout)convertView.findViewById(R.id.main_row_1);
				FrameLayout detailBar2 = (FrameLayout)convertView.findViewById(R.id.main_row_2);
				FrameLayout detailBar3 = (FrameLayout)convertView.findViewById(R.id.main_row_3);
				FrameLayout detailBar4 = (FrameLayout)convertView.findViewById(R.id.main_row_4);
				
				if (mBar.type == 0 )
				{
					detailBar1.setVisibility(View.VISIBLE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					
					((ImageView)convertView.findViewById(R.id.main_row_1_reser)).setVisibility(View.GONE);
					((TextView)convertView.findViewById(R.id.main_row_1_distance)).setVisibility(View.GONE);
					((TextView)convertView.findViewById(R.id.main_row_1_price)).setVisibility(View.GONE);
					
					
					ImageView Image = (ImageView)convertView.findViewById(R.id.main_row_1_pic);
					
					Image.setTag( mBar.imageUrl);;
					BitmapManager.INSTANCE.loadBitmap(mBar.imageUrl, Image, 138, 104);
					
					((TextView)convertView.findViewById(R.id.main_row_1_title)).setText(mBar.hotelName);
					
					detailBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							AppManagement _AppManager = (AppManagement) getApplication();
							

							
							_AppManager.m_HotelCode = mBar.hotelCode;
							Intent intent;
				            intent = new Intent().setClass(baseself, HotelDetail2Activity.class);
				            startActivity( intent ); 

						}
					});
					
					((TextView)convertView.findViewById(R.id.main_row_1_class)).setText(mBar.starRating);
					/*switch( Integer.parseInt(mBar.starRating))
					{
					case 1:
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText("3급(1성급)");
						break;
					case 2:
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText("2급(2성급)");
						break;
					case 3:
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText("1급(3성급)");
						break;
					case 4:
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText("특2급(4성급)");
						break;
					case 5:
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText("특1급(5성급)");
						break;
					case 11:
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText("레지던스");
						break;
					case 12:
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText("비즈니스");
						break;
					case 13:
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText("콘도");
						break;
					case 14:
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText("펜션");
						break;
					case 15:
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText("리조트");
						break;
					case 16:
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText("유스호스텔");
						break;

					}*/
					
				}
				else
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.VISIBLE);
					detailBar4.setVisibility(View.GONE);
					
					((ImageView)convertView.findViewById(R.id.main_row_3_reser)).setVisibility(View.GONE);
					((TextView)convertView.findViewById(R.id.main_row_3_distance)).setVisibility(View.GONE);
					((TextView)convertView.findViewById(R.id.main_row_3_price)).setVisibility(View.GONE);
					
					ImageView Image = (ImageView)convertView.findViewById(R.id.main_row_3_pic);
					
					Image.setTag( mBar.imageUrl);;
					BitmapManager.INSTANCE.loadBitmap(mBar.imageUrl, Image, 138, 104);
					
					((TextView)convertView.findViewById(R.id.main_row_3_title)).setText(mBar.hotelName);
					
					detailBar3.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							AppManagement _AppManager = (AppManagement) getApplication();
							

							
							_AppManager.m_HotelCode = mBar.hotelCode;
							Intent intent;
				            intent = new Intent().setClass(baseself, HotelDetail2Activity.class);
				            startActivity( intent ); 

						}
					});
					
					switch( Integer.parseInt(mBar.starRating))
					{
					case 1:
						((ImageView)convertView.findViewById(R.id.main_row_3_class)).setBackgroundResource(R.drawable.star1);
						break;
					case 2:
						((ImageView)convertView.findViewById(R.id.main_row_3_class)).setBackgroundResource(R.drawable.star2);
						break;
					case 3:
						((ImageView)convertView.findViewById(R.id.main_row_3_class)).setBackgroundResource(R.drawable.star3);
						break;
					case 4:
						((ImageView)convertView.findViewById(R.id.main_row_3_class)).setBackgroundResource(R.drawable.star4);
						break;
					case 5:
						((ImageView)convertView.findViewById(R.id.main_row_3_class)).setBackgroundResource(R.drawable.star5);
						break;


					}
				}

				
			}
			return convertView;
		}
    }
	
}

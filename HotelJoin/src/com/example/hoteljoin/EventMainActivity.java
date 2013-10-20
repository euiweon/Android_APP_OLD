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

public class EventMainActivity extends HotelJoinBaseActivity implements OnClickListener{

	public class EventMainData
	{
		EventMainData()
		{
			
		}
		Integer eventNum;
		String subject;
		String summary;
		String bgnDay;
		String endDay;
		String imageUrl;

	}

	EventMainActivity self;
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
	 
	 
	 private View m_Footer;
	 
	ArrayList< EventMainData > m_ListData;
	private EventMain_Adapter m_Adapter;
		
	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	
	private LayoutInflater mInflater;

	private ListView m_ListView;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_main);
		
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
		
		BtnEvent(R.id.tab_1);
		BtnEvent(R.id.tab_2);
		BtnEvent(R.id.tab_3);
		BtnEvent(R.id.tab_4);
		
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
	        		    		m_CurrentPage ++ ;
	        		    		GetData();
	        		    	}

	        		    		
	        		    } 
     		   }
        	};
        	m_ListView = ((ListView)findViewById(R.id.event_list));
        	
        	m_ListView.setOnScrollListener(listen);
        	
        }
        
        m_ListData = new ArrayList< EventMainData >();
        m_ListData.clear();
    	m_Adapter = new EventMain_Adapter(this, R.layout.event_row, m_ListData);
    	
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Footer  = mInflater.inflate(R.layout.footer, null);
        m_ListView.addFooterView(m_Footer);
        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		
		ChangeTab(0);
		
		
		mLockListView = true; 
        

		RefreshUI();
		
	}
	


	
	
	public void ChangeTab( int index )
	{
		// 탭이 전환 되면 검색어를 클리어 해준다. 
		m_ListData.clear();
		m_Adapter.notifyDataSetChanged();
		m_CurrentPage = 1;
		m_CurrentTab = index;
		
		
		 ((ImageView)findViewById(R.id.tab_1)).setBackgroundResource(R.drawable.all1);
		 ((ImageView)findViewById(R.id.tab_2)).setBackgroundResource(R.drawable.korea1);
		 ((ImageView)findViewById(R.id.tab_3)).setBackgroundResource(R.drawable.abroad1);
		 ((ImageView)findViewById(R.id.tab_4)).setBackgroundResource(R.drawable.recomend1);
		switch( index )
		{
		case 0 :
			((ImageView)findViewById(R.id.tab_1)).setBackgroundResource(R.drawable.all2);
			break;
		case 1:
			((ImageView)findViewById(R.id.tab_2)).setBackgroundResource(R.drawable.korea2);
			break;
		case 2:
			((ImageView)findViewById(R.id.tab_3)).setBackgroundResource(R.drawable.abroad2);
			break; 
		case 3:
			((ImageView)findViewById(R.id.tab_4)).setBackgroundResource(R.drawable.recomend2);
			break; 
		}
		
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
		case R.id.tab_1:
			ChangeTab(0);
			break;
		case R.id.tab_2:
			ChangeTab(1);
			break;
		case R.id.tab_3:
			ChangeTab(2);
			break;
		case R.id.tab_4:
			ChangeTab(3);
			break;
					
		}
		
		RefreshUI();
		
	}
    public void FooterHide()
    {
    	if (m_bFooter == true)
    	{
    		m_bFooter = false;
    		
    		(m_Footer).setVisibility(View.GONE);

    	}
    }
    public void FooterShow()
    {
    	if (m_bFooter == false)
    	{
    		m_bFooter = true;
    		(m_Footer).setVisibility(View.VISIBLE);

    	}
    }

	
	
	
		public void GetData()
		{

			final  AppManagement _AppManager = (AppManagement) getApplication();
			
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					EditText searchText = (EditText)findViewById(R.id.search_text_1);
					Map<String, String> data = new HashMap  <String, String>();

					Integer type = m_CurrentTab + 1;
					data.put("type1Code", type.toString());
					
					data.put("page", m_CurrentPage.toString());
					
		
					
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/event/eventList.gm", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("errorCode").equals("0"))
						{
							JSONArray usageList = (JSONArray)json.get("eventList");
							
							m_CurrentPage = json.getInt("currPage");
							m_TotalPage = json.getInt("totalPage");
							
							
							// 검색 정보를 얻는다. 
							for(int i = 0; i < usageList.length(); i++)
							{
								EventMainData item = new EventMainData();
								JSONObject list = (JSONObject)usageList.get(i);
								

								item.eventNum = list.getInt("eventNum");
								item.subject = _AppManager.GetHttpManager().DecodeString(list.optString("subject"));
								item.summary = list.optString("summary");
								item.bgnDay = _AppManager.GetHttpManager().DecodeString(list.optString("bgnDay"));
								item.endDay = _AppManager.GetHttpManager().DecodeString(list.optString("endDay"));
								item.imageUrl =  _AppManager.GetHttpManager().DecodeString(list.optString("imageUrl"));
								m_ListData.add(item);
							}
							
							
							if ( m_CurrentPage == m_TotalPage)
								handler.sendEmptyMessage(11);
							else
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
				case 10:
				{
					FooterHide();
				}
					break;
				case 11:
				{
					m_Adapter.notifyDataSetChanged();
					RefreshUI();
					FooterHide();
				}
					break;
				case 20:
					break;
				default:
					break;
				}

			}
	    	
		};	
		
	
	
	public class EventMain_Adapter extends ArrayAdapter<EventMainData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<EventMainData> mList;
		private LayoutInflater mInflater;
		
    	public EventMain_Adapter(Context context, int layoutResource, ArrayList<EventMainData> mTweetList)
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
    		final EventMainData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{

				((TextView)convertView.findViewById(R.id.main_row_1_title)).setText(mBar.subject);
				((TextView)convertView.findViewById(R.id.main_row_1_day)).setText(mBar.bgnDay + "~"+ mBar.endDay);
				
				ImageView Image = (ImageView)convertView.findViewById(R.id.main_row_1_pic);
				
				Image.setTag( mBar.imageUrl);;
				BitmapManager.INSTANCE.loadBitmap(mBar.imageUrl, Image, 138, 104);
				
				FrameLayout detailBar1 = (FrameLayout)convertView.findViewById(R.id.main_row_1);
				
				
				detailBar1.setOnClickListener(new View.OnClickListener() 
				{

					public void onClick(View v) 
					{
						final  AppManagement _AppManager = (AppManagement) getApplication();
						
						_AppManager.m_EventNum = mBar.eventNum;
		                
						Intent intent;
			            intent = new Intent().setClass(baseself, EventDetailMainActivity.class);
			            startActivity( intent );
					}
				});
				
			}
			return convertView;
		}
    }
	
}

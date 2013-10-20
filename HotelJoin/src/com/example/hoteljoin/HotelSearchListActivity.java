package com.example.hoteljoin;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.euiweonjeong.base.RangeSeekBar;
import com.euiweonjeong.base.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.euiweonjeong.base.RecycleUtils;
import com.example.hoteljoin.HotelSearchActivity.Destination;
import com.example.hoteljoin.HotelSearchActivity.Search_Adapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

public class HotelSearchListActivity extends HotelJoinBaseActivity implements OnClickListener{


	HotelSearchListActivity self;
	 SlidingMenu menu ;
	 int MenuSize;
	 

	 Integer m_CurrentPage = 1;
	 Integer m_TotalPage = 1;
	 
	 Integer m_TotalCount = 0;
	 
	 private View m_Footer;
	 
	ArrayList< SearchListData > m_ListData;
	private SearchList_Adapter m_Adapter;
		
	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	
	private LayoutInflater mInflater;

	private ListView m_ListView;
	
	
	
	private String m_SortOrder  =  "PRICE|DESC";
	
	Boolean m_Popup = false;
	
	Boolean m_Promotion = false; 
	Boolean m_Brackfast = false; 
	
	Integer bgnprice = 50000;
	Integer endprice = 1500000;
	
	Boolean m_Fillter = false;
	
	
	String hotelCode = "";
	String hotelName = "";
	
	
	/// 호텔 자동완성 리스트 
	private ListView m_ListView2;
	ArrayList<SearchListData> CityList;		// 
	private Search_Adapter m_Adapter2;
	
	EditText m_SearchText = null;
	
	boolean m_bPopup2 ;

	
	RangeSeekBar<Integer> seekBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotelsearchlist_main);
		
		self = this;

		{
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }

		// 비하인드 뷰 설정
		setBehindContentView(R.layout.search_menu_1);
		

		
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
			

			width = (int) ( (float) 340 * ( (float)(windowswidth)/ (float)(originwidth) )  );

			
			sm.setBehindOffset(windowswidth - width );
		}
		
		sm.setFadeDegree(0.35f);
		
		BtnEvent(R.id.tab_1);
		BtnEvent(R.id.tab_2);
		BtnEvent(R.id.tab_3);
		
		

		/////////////////////////////////
		ImageResize2( R.id.menu_1);
		ImageResize2( R.id.main_menu_1);
		TextResize2( R.id.main_menu_text);
		
		ImageResize2( R.id.main_menu_2);
		ImageBtnResize2( R.id.main_menu_2_btn);

		ImageResize2( R.id.main_menu_3);
		ImageBtnResize2( R.id.main_menu_3_btn);
		ImageResize2( R.id.main_menu_4);
		ImageBtnResize2( R.id.main_menu_4_btn);
		/*ImageResize2( R.id.main_menu_5);
		ImageBtnResize2( R.id.main_menu_5_btn);*/
		ImageResize2( R.id.main_menu_6);
		ImageBtnResize2( R.id.main_menu_6_btn);
		/////////////////////////////////////
		
		ImageResize2( R.id.menu_2);
		ImageResize2( R.id.menu_2_5);
		TextResize2( R.id.menu_2_1_5);
		
		ImageResize2( R.id.menu_2_1);
		ImageBtnResize2( R.id.menu_2_1_1);
		ImageResize2( R.id.menu_2_2);
		ImageBtnResize2( R.id.menu_2_1_2);
		ImageResize2( R.id.menu_2_3);
		ImageBtnResize2( R.id.menu_2_1_3);
		ImageResize2( R.id.menu_2_4);
		ImageBtnResize2( R.id.menu_2_1_4);
		
		ImageResize2( R.id.menu_2_6);
		ImageBtnResize2( R.id.menu_2_1_6);
		
		
		/////////////////////////////////////
		
		ImageResize2( R.id.menu_3);
		ImageResize2( R.id.menu_3_1_ex);
		TextResize2( R.id.menu_3_1_1_ex);
		
		ImageResize2( R.id.scrollview_1);
		//ImageResize2( R.id.menu_3_ex);
		ImageResize2( R.id.menu_3_1);
		ImageBtnResize2( R.id.menu_3_1_1);
		ImageResize2( R.id.menu_3_2);
		ImageBtnResize2( R.id.menu_3_1_2);
		ImageResize2( R.id.menu_3_3);
		ImageBtnResize2( R.id.menu_3_1_3);
		ImageResize2( R.id.menu_3_4);
		ImageBtnResize2( R.id.menu_3_1_4);
		ImageResize2( R.id.menu_3_5);
		ImageBtnResize2( R.id.menu_3_1_5);
		
		ImageResize2( R.id.menu_3_6);
		ImageBtnResize2( R.id.menu_3_1_6);
		ImageResize2( R.id.menu_3_7);
		ImageBtnResize2( R.id.menu_3_1_7);
		ImageResize2( R.id.menu_3_8);
		ImageBtnResize2( R.id.menu_3_1_8);
		ImageResize2( R.id.menu_3_9);
		ImageBtnResize2( R.id.menu_3_1_9);
		ImageResize2( R.id.menu_3_10);
		ImageBtnResize2( R.id.menu_3_1_10);
		
		
		ImageResize2( R.id.menu_4_100);
		ImageBtnResize2( R.id.menu_4_100_1);
		ImageResize2( R.id.menu_3_100);
		ImageBtnResize2( R.id.menu_3_100_1);
		
		
		///////////////////////////////////
		
		ImageResize2( R.id.menu_4);
		ImageResize2( R.id.menu_4_1_1_ex);
		TextResize2( R.id.menu_4_1_1_ex_1);
		
		ImageResize2( R.id.scrollview_2);
		//ImageResize2( R.id.menu_4_ex);
		ImageResize2( R.id.menu_4_1);
		ImageBtnResize2( R.id.menu_4_1_1);
		ImageResize2( R.id.menu_4_2);
		ImageBtnResize2( R.id.menu_4_1_2);
		ImageResize2( R.id.menu_4_3);
		ImageBtnResize2( R.id.menu_4_1_3);
		ImageResize2( R.id.menu_4_4);
		ImageBtnResize2( R.id.menu_4_1_4);
		ImageResize2( R.id.menu_4_5);
		ImageBtnResize2( R.id.menu_4_1_5);
		
		
		{
			BtnEvent( R.id.popup_btn1);
			BtnEvent( R.id.popup_btn2);
			BtnEvent( R.id.checkbox_1);
			BtnEvent( R.id.checkbox_2);
			BtnEvent( R.id.search_btn);
			BtnEvent( R.id.init_btn);
			
		}
		
		

		AfterCreate(1);
		


       
        
        
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
	        		    		
	        		    		if ( m_Fillter )
	        		    			GetData2();
	        		    		else
	        		    			GetData();
	        		    	}

	        		    		
	        		    } 
     		   }
        	};
        	m_ListView = ((ListView)findViewById(R.id.search_list));
        	
        	m_ListView.setOnScrollListener(listen);
        	
        }
        
        m_ListData = new ArrayList< SearchListData >();
        m_ListData.clear();
    	m_Adapter = new SearchList_Adapter(this, R.layout.search_list_row, m_ListData);
    	
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Footer  = mInflater.inflate(R.layout.footer, null);
        m_ListView.addFooterView(m_Footer);
        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		m_Adapter.notifyDataSetChanged();
    	
		
		
		
		mLockListView = true; 
        
		
		

		//RefreshUI();
		
		((TextView)findViewById(R.id.title_logo)).setText("검색중");
		
		{
			
			seekBar = new RangeSeekBar<Integer>(50000, 1500000, getBaseContext());
			seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>()
			{        
				public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) 
				{               
					// handle changed range values  
					long m = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					DecimalFormat df = new DecimalFormat("#,##0");

					((TextView)findViewById(R.id.popup_price_1)).setText("￦ " + df.format(minValue) );
					((TextView)findViewById(R.id.popup_price_2)).setText("￦ " + df.format(maxValue) );
					
					bgnprice = minValue;
					endprice = maxValue;
					Log.i(TAG, "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);        
				}
			});
			// add RangeSeekBar to pre-defined Layout
			
		
			
			ViewGroup layout = (ViewGroup) findViewById(R.id.popup_menu);
			layout.addView(seekBar);
			
			{
				DecimalFormat df = new DecimalFormat("#,##0");

				((TextView)findViewById(R.id.popup_price_1)).setText("￦ " + df.format(seekBar.getSelectedMinValue()) );
				((TextView)findViewById(R.id.popup_price_2)).setText("￦ " + df.format(seekBar.getSelectedMaxValue()) );
				
			}
			
			{
		    	Display defaultDisplay = ((WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		    	
				int windowswidth = defaultDisplay.getWidth();
				int windowsheight= defaultDisplay.getHeight();
				int originwidth = 480;
				int originheight = 800;
				
				int imageheight = 50;
				int imagewidth = 420;
				
				FrameLayout.LayoutParams margin = (FrameLayout.LayoutParams )(seekBar.getLayoutParams());
				
				int posx = 30;
				int posy = 540;
				
			
				seekBar.getLayoutParams().width = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
				seekBar.getLayoutParams().height = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
				
				posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
				//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
				posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  ); 
				margin.setMargins(posx , posy, 0, 0);
				seekBar.setLayoutParams(margin);
			}
				
			
		}
		
		ClosePopup();
		
		
		GetData();
		
		
		// 호텔 자동완성 검색...
		{
			
			CityList = new  ArrayList<SearchListData>();
			
			CityList.clear();

			m_ListView2 = (ListView)findViewById(R.id.search_list_2);
			
			
	    	m_Adapter2 = new Search_Adapter(this, R.layout.search_row, CityList);
	    	

	        
	    	m_ListView2.setAdapter(m_Adapter2);
			
		}
		
		// 텍스트 변경될때 마다 리스트 갱신
		{
			
			m_SearchText = (EditText)findViewById(R.id.search_text_1); 
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
					if (m_SearchText.isFocusable())         
					{             
						//m_SearchText EditText 가 포커스 되어있을 경우에만 실행됩니다.  
						// 검색어가 변경 될때 마다 데이터 가져오기 
						String Text = ((EditText)findViewById(R.id.search_text_1)).getText().toString();
						
						if( !Text.equals("")&& !m_bPopup2 )
						{
							OpenPopup2();
						}
						

					}   
				}
			};
			//호출
			m_SearchText.addTextChangedListener(watcher);

			ClosePopup2();
		}

		
	}
	


	@Override
	public void onResume()
	{
		super.onResume();
		this.showContent();
	}
	
	
	@Override
    public void onBackPressed() 
    {
    	if ( m_bPopup2 == true)
    	{
    		ClosePopup2();
    	}
    	else if ( m_Popup ==true)
		{
			ClosePopup();
		}
		else
		{
			super.onBackPressed();
		}
    }
	
	

	@Override
    protected void onDestroy() {
         //Adapter가 있으면 어댑터에서 생성한 recycle메소드를 실행 
        if (m_Adapter != null)
        	m_Adapter.recycle();
         RecycleUtils.recursiveRecycle(getWindow().getDecorView());
         System.gc();
 
         super.onDestroy();
     }

	
	
	
	public void RefreshUI()
	{

		m_Adapter.notifyDataSetChanged();
		
		mLockListView = false;  

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
	
	public void OpenPopup2()
	{
		/*m_bPopup2 = true;
		 ((View)findViewById(R.id.main_layout_2)).setVisibility(View.VISIBLE);*/
	}
	public void ClosePopup2()
	{
		m_bPopup2 = false;
		 ((View)findViewById(R.id.main_layout_2)).setVisibility(View.GONE);
	}
	
	
	public void OpenPopup()
	{
		m_Popup = true;
		((View)findViewById(R.id.main_screen)).setVisibility(View.GONE);
		((View)findViewById(R.id.popup_menu)).setVisibility(View.VISIBLE);
	}
	public void ClosePopup()
	{
		m_Popup = false;
		((View)findViewById(R.id.main_screen)).setVisibility(View.VISIBLE);
		((View)findViewById(R.id.popup_menu)).setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		case R.id.tab_1:
			
		{
			((View)findViewById(R.id.menu_1)).setVisibility(View.VISIBLE);
			((View)findViewById(R.id.menu_2)).setVisibility(View.GONE);
			((View)findViewById(R.id.menu_3)).setVisibility(View.GONE);
			((View)findViewById(R.id.menu_4)).setVisibility(View.GONE);
			
			this.showMenu();
		}
			break;
		case R.id.tab_2:
			OpenPopup();
			break;
		case R.id.tab_3:
		{
			if ( mLockListView == false)
			{
				AppManagement _AppManager = (AppManagement) getApplication();
				
				_AppManager.m_HotelSearchListData = m_ListData;
				
				Intent intent;
	            intent = new Intent().setClass(baseself, HotelLocationActivity.class);
	            startActivity( intent );
				
			}
			else
			{
				self.ShowAlertDialLog( self ,"에러" , "아직 데이터를 읽는 중입니다.\n 잠시만 기다려주세요" );
			}
		}
			
			break;
		case R.id.main_menu_2_btn:
		{
			this.showContent();
			
			m_SortOrder = "NAME|ASC";
		}
			break;
		case R.id.main_menu_3_btn:
		{
			this.showContent();
			m_SortOrder = "PRICE|ASC";
			m_ListData.clear();
			m_Adapter.notifyDataSetChanged();
			GetData();
		}
			break;
		case R.id.main_menu_4_btn:
		{
			this.showContent();
			m_SortOrder = "PRICE|DESC";
			m_ListData.clear();
			m_Adapter.notifyDataSetChanged();
			GetData();
		}
			break;
		case R.id.main_menu_5_btn:
		{
			this.showContent();
			m_SortOrder = "DISTANCE|ASC";
			m_ListData.clear();
			m_Adapter.notifyDataSetChanged();
			GetData();
		}
			break;
		case R.id.main_menu_6_btn:
		{
			this.showContent();
			m_SortOrder = "GRADE|ASC";
			m_ListData.clear();
			m_Adapter.notifyDataSetChanged();
			GetData();
		}
			break;
		case R.id.popup_btn1:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			((View)findViewById(R.id.menu_1)).setVisibility(View.GONE);
			if (_AppManager.m_SearchWorld)
			{
				((View)findViewById(R.id.menu_3)).setVisibility(View.GONE);
				((View)findViewById(R.id.menu_4)).setVisibility(View.VISIBLE);	
			}
			else
			{
				((View)findViewById(R.id.menu_3)).setVisibility(View.VISIBLE);
				((View)findViewById(R.id.menu_4)).setVisibility(View.GONE);	
			}

			((View)findViewById(R.id.menu_2)).setVisibility(View.GONE);
			
			this.showMenu();
		}
			break;
		case R.id.popup_btn2:
		{
			((View)findViewById(R.id.menu_1)).setVisibility(View.GONE);
			((View)findViewById(R.id.menu_2)).setVisibility(View.VISIBLE);
			((View)findViewById(R.id.menu_3)).setVisibility(View.GONE);
			((View)findViewById(R.id.menu_4)).setVisibility(View.GONE);
			
			this.showMenu();
		}
			break;
		case R.id.checkbox_1:
		{
			m_Brackfast = !m_Brackfast;
			if ( m_Brackfast == false )
				((ImageView)findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.checkbox_1);
			else
				((ImageView)findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.checkbox_2);
		}
			break;
		case R.id.checkbox_2:
		{
			m_Promotion = !m_Promotion;
			if ( m_Promotion == false )
				((ImageView)findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.checkbox_1);
			else
				((ImageView)findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.checkbox_2);
		}
			break;
		case R.id.search_btn:
		{
			m_Fillter = true;
			m_ListData.clear();
			GetData2();
		}
			break;

		case R.id.menu_2_1_1:
		{
			((TextView)findViewById(R.id.popup_btn_text2)).setText("확정예약");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelReceipt = "1";
			
			this.showContent();
		}
			break;
		case R.id.menu_2_1_2:
		{
			((TextView)findViewById(R.id.popup_btn_text2)).setText("대기예약");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelReceipt = "2";
			this.showContent();
		}
			break;
		case R.id.menu_2_1_3:
		{
			((TextView)findViewById(R.id.popup_btn_text2)).setText("확정접수");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelReceipt = "3";
			this.showContent();
		}
			break;
		case R.id.menu_2_1_4:
		{
			((TextView)findViewById(R.id.popup_btn_text2)).setText("일반접수");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelReceipt = "4";
			this.showContent();
		}
			break;
		case R.id.menu_2_1_6:
		{
			((TextView)findViewById(R.id.popup_btn_text2)).setText("전체선택");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelReceipt = "-1";

			this.showContent();
		}
			break;
		case R.id.menu_3_1_1:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("특1급");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "4";
			this.showContent();
		}
			break;
		case R.id.menu_3_1_2:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("특2급");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "5";
			this.showContent();
		}
			break;
		case R.id.menu_3_1_3:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("1급");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "3";
			
			this.showContent();
		}
			break;
		case R.id.menu_3_1_4:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("2급");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "2";
			this.showContent();
		}
			break;
		case R.id.menu_3_1_5:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("3급");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "1";
			this.showContent();
		}
			break;
		case R.id.menu_3_1_6:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("레지던스");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "0.6";
			
			this.showContent();
		}
			break;
		case R.id.menu_3_1_7:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("비즈니스");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "0.5";
			
			this.showContent();
		}
			break;
		case R.id.menu_3_1_8:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("콘도");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "0.4";
			
			this.showContent();
		}
			break;
		case R.id.menu_3_1_9:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("리조트");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "0.3";
			
			this.showContent();
		}
			break;
		case R.id.menu_3_1_10:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("유스호스텔");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "0.1";
			
			this.showContent();
		}
			break;
		case R.id.menu_3_100_1:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("전체선택");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "-1";
			
			this.showContent();
		}
			break;
			
		case R.id.menu_4_100_1:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("전체선택");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "-1";
			
			this.showContent();
		}
			break;

		case R.id.menu_4_1_1:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("1성급");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "1";
			this.showContent();
		}
			break;
		case R.id.menu_4_1_2:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("2성급");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "2";
			this.showContent();
		}
			break;
		case R.id.menu_4_1_3:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("3성급");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "3";
			this.showContent();
		}
			break;
		case R.id.menu_4_1_4:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("4성급");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "4";
			this.showContent();
		}
			break;
		case R.id.menu_4_1_5:
		{
			((TextView)findViewById(R.id.popup_btn_text1)).setText("5성급");
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "5";
			this.showContent();
		}
			break;
		case R.id.init_btn:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_HotelGrade = "-1";
			_AppManager.m_HotelReceipt = "-1";
			((TextView)findViewById(R.id.popup_btn_text1)).setText("전체선택");
			((TextView)findViewById(R.id.popup_btn_text1)).setText("전체선택");
			((TextView)findViewById(R.id.popup_btn_text2)).setText("전체선택");
			
			m_Promotion = false;
			m_Brackfast = false;
			
			((ImageView)findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.checkbox_1);
			((ImageView)findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.checkbox_1);
			EditText searchText = (EditText)findViewById(R.id.search_text_1);
			searchText.setText("");
			
			seekBar.setSelectedMinValue(50000);
			seekBar.setSelectedMaxValue(1500000);
			
			{
				DecimalFormat df = new DecimalFormat("#,##0");
				((TextView)findViewById(R.id.popup_price_1)).setText("￦ " + df.format(50000) );
				((TextView)findViewById(R.id.popup_price_2)).setText("￦ " + df.format(1500000) );
			}
			

			
		}
			break;


		}

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

		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				EditText searchText = (EditText)findViewById(R.id.search_text_1);
				Map<String, String> data = new HashMap  <String, String>();


				data.put("currPage", m_CurrentPage.toString());
				data.put("searchTypeCode", _AppManager.m_SearchCode);
				data.put("destinationCode", _AppManager.m_DestinationCode);
				data.put("checkinDay", _AppManager.m_CheckInDay);
				data.put("duration", _AppManager.m_Duration);
				data.put("numOfRooms", _AppManager.m_NumRoom);
				data.put("numPerRoom", _AppManager.m_NumPer);
				
				data.put("sortOrderKey", m_SortOrder);
					

		
				if (!_AppManager.m_MyLat.equals("-1"))
				{
					data.put("latitude",_AppManager.m_MyLat);
					data.put("longitude",_AppManager.m_MyLng);
				}
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/search/searchHotelPriceList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						JSONArray usageList = (JSONArray)json.get("hotelPriceList");
						
						m_CurrentPage = json.getInt("currPage");
						m_TotalPage = json.getInt("totalPage");
						m_TotalCount = json.getInt("totalCount");
							
							
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList.length(); i++)
						{
							SearchListData item = new SearchListData();
							JSONObject list = (JSONObject)usageList.get(i);
							item.supplyCode =  (list.optString("supplyCode"));
							item.nationCode =  (list.optString("nationCode"));
							item.cityCode =  (list.optString("cityCode"));
							item.hotelCode =  (list.optString("hotelCode"));
							item.hotelName =  (list.optString("hotelName"));
							item.hotelNameEn =  (list.optString("hotelNameEn"));
							item.starRating =  (list.optString("starRating"));
							item.bestYn =  (list.optString("bestYn"));
							item.bestSortSeq =  (list.optString("bestSortSeq"));
							item.breakfastYn =  (list.optString("breakfastYn"));
							item.availableCode =  (list.optString("availableCode"));
							item.promoYn =  (list.optString("promoYn"));
							if ( item.promoYn.equals("1"))
								item.promoDesc = (list.optString("promoDesc"));
							item.roomPrice =  (list.optString("roomPrice"));
							if ( _AppManager.m_SearchWorld == false)
								item.rating =  (list.optString("rating"));
							item.latitude =  (list.optString("latitude"));
							item.longitude =  (list.optString("longitude"));
							item.starRatingName =  (list.optString("starRatingName"));
							
							
								
							//if (!_AppManager.m_MyLat.equals("-1")&&  _AppManager.m_SearchWorld == false)
								item.distance =  (list.optString("distanceText"));
							
							item.thumbNailUrl =  (list.optString("thumbNailUrl"));

							


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
						handler.sendMessage(handler.obtainMessage(1,(json.getString("errorMsg")) ));
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
	
	public void GetData2()
	{
		// 데이터를 클리어
		
		m_Adapter.notifyDataSetChanged();
    	
		
		((TextView)findViewById(R.id.title_logo)).setText("검색중");
		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				EditText searchText = (EditText)findViewById(R.id.search_text_1);
				Map<String, String> data = new HashMap  <String, String>();

				
				data.put("currPage", m_CurrentPage.toString());
				data.put("searchTypeCode", _AppManager.m_SearchCode);
				data.put("destinationCode", _AppManager.m_DestinationCode);
				data.put("checkinDay", _AppManager.m_CheckInDay);
				data.put("duration", _AppManager.m_Duration);
				data.put("numOfRooms", _AppManager.m_NumRoom);
				data.put("numPerRoom", _AppManager.m_NumPer);
				
				data.put("sortOrderKey", m_SortOrder);
				
				data.put("hotelName", searchText.getText().toString());
				
				if (!_AppManager.m_MyLat.equals("-1"))
				{
					data.put("latitude",_AppManager.m_MyLat);
					data.put("longitude",_AppManager.m_MyLng);
				}
				
				if (!_AppManager.m_HotelGrade.equals("-1"))
					data.put("arrStarRating", _AppManager.m_HotelGrade);
					
				if (!_AppManager.m_HotelReceipt.equals("-1"))
					data.put("availableCode", _AppManager.m_HotelReceipt);

				
				
				Boolean m_Promotion = false; 
				Boolean m_Brackfast = false;
				if ( m_Brackfast )
					data.put("breakfastYn", "1");
				else
					data.put("breakfastYn", "0");
				
				if ( m_Promotion )
					data.put("promotionYn", "1");
				else
					data.put("promotionYn", "0");
				
				data.put("bgnPrice", bgnprice.toString());
				data.put("endPrice", endprice.toString());
				

		
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/search/searchHotelPriceList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						JSONArray usageList = (JSONArray)json.optJSONArray("hotelPriceList");
						
						m_CurrentPage = json.getInt("currPage");
						m_TotalPage = json.getInt("totalPage");
						m_TotalCount = json.getInt("totalCount");
							
							
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList.length(); i++)
						{
							SearchListData item = new SearchListData();
							JSONObject list = (JSONObject)usageList.get(i);
							item.supplyCode =  (list.optString("supplyCode"));
							item.nationCode =  (list.optString("nationCode"));
							item.cityCode =  (list.optString("cityCode"));
							item.hotelCode =  (list.optString("hotelCode"));
							item.hotelName =  (list.optString("hotelName"));
							item.hotelNameEn =  (list.optString("hotelNameEn"));
							item.starRating =  (list.optString("starRating"));
							item.bestYn =  (list.optString("bestYn"));
							item.bestSortSeq =  (list.optString("bestSortSeq"));
							item.breakfastYn =  (list.optString("breakfastYn"));
							item.availableCode =  (list.optString("availableCode"));
							item.promoYn =  (list.optString("promoYn"));
							if ( item.promoYn.equals("1"))
								item.promoDesc =  (list.optString("promoDesc"));
							item.roomPrice =  (list.optString("roomPrice"));
							if ( _AppManager.m_SearchWorld == false)
								item.rating =  (list.optString("rating"));
							item.latitude =  (list.optString("latitude"));
							item.longitude =  (list.optString("longitude"));
							item.starRatingName =  (list.optString("starRatingName"));
						
							//if (!_AppManager.m_MyLat.equals("-1")&&  _AppManager.m_SearchWorld == false)
								item.distance =  (list.optString("distanceText"));
							
							item.thumbNailUrl =  (list.optString("thumbNailUrl"));

							


							m_ListData.add(item);
						}
						
						
						if ( m_CurrentPage == m_TotalPage)
							handler.sendEmptyMessage(12);
						else
							handler.sendEmptyMessage(2);
					}
					else 
					{
						// 에러 메세지를 전송한다. 
						handler.sendMessage(handler.obtainMessage(1,(json.getString("errorMsg")) ));
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler.sendMessage(handler.obtainMessage(1,"json Data Error" ));
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
					try {
						Thread.sleep(900);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (m_ListData.size() == 0)
						((TextView)findViewById(R.id.title_logo)).setText("검색결과 없음");
					else
						((TextView)findViewById(R.id.title_logo)).setText(m_TotalCount+"건의 검색결과");
					m_Adapter.notifyDataSetChanged();
					RefreshUI();
					
				}
					break;
				case 1:
					// 오류처리 
					self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
					FooterHide();
					
					break;
				case 2:
				{
					try {
						Thread.sleep(900);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (m_ListData.size() == 0)
						((TextView)findViewById(R.id.title_logo)).setText("검색결과 없음");
					else
						((TextView)findViewById(R.id.title_logo)).setText(m_TotalCount+"건의 검색결과");
					m_Adapter.notifyDataSetChanged();
					RefreshUI();
					ClosePopup();
				}
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
					try {
						Thread.sleep(900);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (m_ListData.size() == 0)
						((TextView)findViewById(R.id.title_logo)).setText("검색결과 없음");
					else
						((TextView)findViewById(R.id.title_logo)).setText(m_TotalCount+"건의 검색결과");
					m_Adapter.notifyDataSetChanged();
					RefreshUI();
					FooterHide();
				}
					break;
				case 12:
				{
					try {
						Thread.sleep(900);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (m_ListData.size() == 0)
						((TextView)findViewById(R.id.title_logo)).setText("검색결과 없음");
					else
						((TextView)findViewById(R.id.title_logo)).setText(m_TotalCount+"건의 검색결과");
					m_Adapter.notifyDataSetChanged();
					RefreshUI();
					FooterHide();
					ClosePopup();
				}
					break;
				case 20:
					break;
				default:
					break;
				}

			}
	    	
		};	
		
	
	
	public class SearchList_Adapter extends ArrayAdapter<SearchListData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<SearchListData> mList;
		private LayoutInflater mInflater;
		
		private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();
		
    	public SearchList_Adapter(Context context, int layoutResource, ArrayList<SearchListData> mTweetList)
		{
			super(context, layoutResource, mTweetList);
			this.mContext = context;
			this.mResource = layoutResource;
			this.mList = mTweetList;
			this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
    	

    	 //onDestory에서 쉽게 해제할 수 있도록 메소드 생성
    	 

    	public void recycle() {

    		for (WeakReference<View> ref : mRecycleList) {

    	             RecycleUtils.recursiveRecycle(ref.get());
    		}

    	}

    	
    	
    	@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
    		final SearchListData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{

				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_1_pic)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_1_pic_2)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_1_reser)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_2_back)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_2_pic)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_2_pic_2)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_2_pic2)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_2_reser)) );
				
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_3_pic)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_3_pic_2)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_3_reser)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_4_back)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_4_pic)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_4_pic_2)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_4_pic2)) );
				mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_4_reser)) );

				

				FrameLayout detailBar1 = (FrameLayout)convertView.findViewById(R.id.main_row_1);
				FrameLayout detailBar2 = (FrameLayout)convertView.findViewById(R.id.main_row_2);
				FrameLayout detailBar3 = (FrameLayout)convertView.findViewById(R.id.main_row_3);
				FrameLayout detailBar4 = (FrameLayout)convertView.findViewById(R.id.main_row_4);
				
				if (_AppManager.m_SearchWorld == false )
				{
					if (mBar.promoYn.equals("0" ))
					{
						detailBar1.setVisibility(View.VISIBLE);
						detailBar2.setVisibility(View.GONE);
						detailBar3.setVisibility(View.GONE);
						detailBar4.setVisibility(View.GONE);
						
						if (mBar.availableCode.equals("1" ))
							((ImageView)convertView.findViewById(R.id.main_row_1_reser)).setBackgroundResource(R.drawable.hotellist_confirm_reserve);
						else  if (mBar.availableCode.equals("2" ))
							((ImageView)convertView.findViewById(R.id.main_row_1_reser)).setBackgroundResource(R.drawable.hotellist_wait_reserve);
						else if (mBar.availableCode.equals("3" ))
							((ImageView)convertView.findViewById(R.id.main_row_1_reser)).setBackgroundResource(R.drawable.hotellist_confirm_regist);
						else 
							((ImageView)convertView.findViewById(R.id.main_row_1_reser)).setBackgroundResource(R.drawable.hotellist_nomal_reserve);
						
						if (mBar.bestYn.equals("1" ))
							((ImageView)convertView.findViewById(R.id.main_row_1_pic_2)).setVisibility(View.VISIBLE);
						else
							((ImageView)convertView.findViewById(R.id.main_row_1_pic_2)).setVisibility(View.GONE);
							
						//((TextView)convertView.findViewById(R.id.main_row_1_distance)).setVisibility(View.GONE);
						{
							DecimalFormat df = new DecimalFormat("#,##0");

							String  name = df.format(Double.parseDouble(mBar.roomPrice));
							((TextView)convertView.findViewById(R.id.main_row_1_price)).setText("￦ " + name);
						}
						
						
						
						ImageView Image = (ImageView)convertView.findViewById(R.id.main_row_1_pic);
						
						Image.setTag( mBar.thumbNailUrl);;
						BitmapManager.INSTANCE.loadBitmap(mBar.thumbNailUrl, Image, 138, 104);
						
						((TextView)convertView.findViewById(R.id.main_row_1_title)).setText(mBar.hotelName);
						
						detailBar1.setOnClickListener(new View.OnClickListener() 
						{

							public void onClick(View v) 
							{
								_AppManager.m_HotelCode = mBar.hotelCode;
								Intent intent;
					            intent = new Intent().setClass(baseself, HotelDetailActivity.class);
					            startActivity( intent ); 

							}
						});
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText(mBar.starRatingName);
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
						detailBar2.setVisibility(View.VISIBLE);
						detailBar3.setVisibility(View.GONE);
						detailBar4.setVisibility(View.GONE);
						
						if (mBar.availableCode.equals("1" ))
							((ImageView)convertView.findViewById(R.id.main_row_2_reser)).setBackgroundResource(R.drawable.hotellist_confirm_reserve);
						else  if (mBar.availableCode.equals("2" ))
							((ImageView)convertView.findViewById(R.id.main_row_2_reser)).setBackgroundResource(R.drawable.hotellist_wait_reserve);
						else if (mBar.availableCode.equals("3" ))
							((ImageView)convertView.findViewById(R.id.main_row_2_reser)).setBackgroundResource(R.drawable.hotellist_confirm_regist);
						else 
							((ImageView)convertView.findViewById(R.id.main_row_2_reser)).setBackgroundResource(R.drawable.hotellist_nomal_reserve);
							
						((TextView)convertView.findViewById(R.id.main_row_2_distance)).setText(mBar.distance);
						{
							DecimalFormat df = new DecimalFormat("#,##0");

							((TextView)convertView.findViewById(R.id.main_row_2_price)).setText("￦ " + df.format(Double.parseDouble(mBar.roomPrice)) );
						}
						
						((TextView)convertView.findViewById(R.id.main_row_2_promo)).setText(mBar.promoDesc );
						
						
						ImageView Image = (ImageView)convertView.findViewById(R.id.main_row_2_pic);
						
						Image.setTag( mBar.thumbNailUrl);;
						BitmapManager.INSTANCE.loadBitmap(mBar.thumbNailUrl, Image, 138, 104);
						
						((TextView)convertView.findViewById(R.id.main_row_2_title)).setText(mBar.hotelName);
						
						if (mBar.bestYn.equals("1" ))
							((ImageView)convertView.findViewById(R.id.main_row_2_pic_2)).setVisibility(View.VISIBLE);
						else
							((ImageView)convertView.findViewById(R.id.main_row_2_pic_2)).setVisibility(View.GONE);
						
						
						detailBar2.setOnClickListener(new View.OnClickListener() 
						{

							public void onClick(View v) 
							{
								_AppManager.m_HotelCode = mBar.hotelCode;
								Intent intent;
					            intent = new Intent().setClass(baseself, HotelDetailActivity.class);
					            startActivity( intent ); 

							}
						});
						
						((TextView)convertView.findViewById(R.id.main_row_2_class)).setText(mBar.starRatingName);
						/*switch( Integer.parseInt(mBar.starRating))
						{
						case 1:
							((TextView)convertView.findViewById(R.id.main_row_2_class)).setText("3급(1성급)");
							break;
						case 2:
							((TextView)convertView.findViewById(R.id.main_row_2_class)).setText("2급(2성급)");
							break;
						case 3:
							((TextView)convertView.findViewById(R.id.main_row_2_class)).setText("1급(3성급)");
							break;
						case 4:
							((TextView)convertView.findViewById(R.id.main_row_2_class)).setText("특2급(4성급)");
							break;
						case 5:
							((TextView)convertView.findViewById(R.id.main_row_2_class)).setText("특1급(5성급)");
							break;
						case 11:
							((TextView)convertView.findViewById(R.id.main_row_2_class)).setText("레지던스");
							break;
						case 12:
							((TextView)convertView.findViewById(R.id.main_row_2_class)).setText("비즈니스");
							break;
						case 13:
							((TextView)convertView.findViewById(R.id.main_row_2_class)).setText("콘도");
							break;
						case 14:
							((TextView)convertView.findViewById(R.id.main_row_2_class)).setText("펜션");
							break;
						case 15:
							((TextView)convertView.findViewById(R.id.main_row_2_class)).setText("리조트");
							break;
						case 16:
							((TextView)convertView.findViewById(R.id.main_row_2_class)).setText("유스호스텔");
							break;

						}*/
					}

					
				}
				else
				{
					if (mBar.promoYn.equals("0" ))
					{
						detailBar1.setVisibility(View.GONE);
						detailBar2.setVisibility(View.GONE);
						detailBar3.setVisibility(View.VISIBLE);
						detailBar4.setVisibility(View.GONE);
						
						if (mBar.availableCode.equals("1" ))
							((ImageView)convertView.findViewById(R.id.main_row_3_reser)).setBackgroundResource(R.drawable.hotellist_confirm_reserve);
						else  if (mBar.availableCode.equals("2" ))
							((ImageView)convertView.findViewById(R.id.main_row_3_reser)).setBackgroundResource(R.drawable.hotellist_wait_reserve);
						else if (mBar.availableCode.equals("3" ))
							((ImageView)convertView.findViewById(R.id.main_row_3_reser)).setBackgroundResource(R.drawable.hotellist_confirm_regist);
						else 
							((ImageView)convertView.findViewById(R.id.main_row_3_reser)).setBackgroundResource(R.drawable.hotellist_nomal_reserve);
							
						//((TextView)convertView.findViewById(R.id.main_row_3_distance)).setVisibility(View.GONE);
						
						((TextView)convertView.findViewById(R.id.main_row_3_distance)).setText(mBar.distance);
						{
							DecimalFormat df = new DecimalFormat("#,##0");

							((TextView)convertView.findViewById(R.id.main_row_3_price)).setText("￦ " + df.format(Double.parseDouble(mBar.roomPrice)) );
						}
						
						
						
						ImageView Image = (ImageView)convertView.findViewById(R.id.main_row_3_pic);
						
						Image.setTag( mBar.thumbNailUrl);
						BitmapManager.INSTANCE.loadBitmap(mBar.thumbNailUrl, Image, 138, 104);
						
						((TextView)convertView.findViewById(R.id.main_row_3_title)).setText(mBar.hotelNameEn);
						
						if (mBar.bestYn.equals("1" ))
							((ImageView)convertView.findViewById(R.id.main_row_3_pic_2)).setVisibility(View.VISIBLE);
						else
							((ImageView)convertView.findViewById(R.id.main_row_3_pic_2)).setVisibility(View.GONE);
						
						detailBar3.setOnClickListener(new View.OnClickListener() 
						{

							public void onClick(View v) 
							{
								_AppManager.m_HotelCode = mBar.hotelCode;
								Intent intent;
					            intent = new Intent().setClass(baseself, HotelDetailActivity.class);
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
					else
					{
						detailBar1.setVisibility(View.GONE);
						detailBar2.setVisibility(View.GONE);
						detailBar3.setVisibility(View.GONE);
						detailBar4.setVisibility(View.VISIBLE);
						
						if (mBar.availableCode.equals("1" ))
							((ImageView)convertView.findViewById(R.id.main_row_4_reser)).setBackgroundResource(R.drawable.hotellist_confirm_reserve);
						else  if (mBar.availableCode.equals("2" ))
							((ImageView)convertView.findViewById(R.id.main_row_4_reser)).setBackgroundResource(R.drawable.hotellist_wait_reserve);
						else if (mBar.availableCode.equals("3" ))
							((ImageView)convertView.findViewById(R.id.main_row_4_reser)).setBackgroundResource(R.drawable.hotellist_confirm_regist);
						else 
							((ImageView)convertView.findViewById(R.id.main_row_4_reser)).setBackgroundResource(R.drawable.hotellist_nomal_reserve);
						
							
						//((TextView)convertView.findViewById(R.id.main_row_4_distance)).setVisibility(View.GONE);
						
						((TextView)convertView.findViewById(R.id.main_row_3_distance)).setText(mBar.distance);
						
						{
							DecimalFormat df = new DecimalFormat("#,##0");

							//((TextView)findViewById(R.id.main_row_4_price)).setText("￦ " + df.format(mBar.roomPrice) );
							((TextView)convertView.findViewById(R.id.main_row_4_price)).setText("￦ " + df.format(Double.parseDouble(mBar.roomPrice)) );
						}
						((TextView)convertView.findViewById(R.id.main_row_4_promo)).setText(mBar.promoDesc );
						
						
						ImageView Image = (ImageView)convertView.findViewById(R.id.main_row_4_pic);
						
						Image.setTag( mBar.thumbNailUrl);
						BitmapManager.INSTANCE.loadBitmap(mBar.thumbNailUrl, Image, 138, 104);
						
						((TextView)convertView.findViewById(R.id.main_row_4_title)).setText(mBar.hotelNameEn);
						
						if (mBar.bestYn.equals("1" ))
							((ImageView)convertView.findViewById(R.id.main_row_4_pic_2)).setVisibility(View.VISIBLE);
						else
							((ImageView)convertView.findViewById(R.id.main_row_4_pic_2)).setVisibility(View.GONE);
						
						
						detailBar4.setOnClickListener(new View.OnClickListener() 
						{

							public void onClick(View v) 
							{
								_AppManager.m_HotelCode = mBar.hotelCode;
								Intent intent;
					            intent = new Intent().setClass(baseself, HotelDetailActivity.class);
					            startActivity( intent ); 

							}
						});
						
						
						
						switch( Integer.parseInt(mBar.starRating))
						{
						case 1:
							((ImageView)convertView.findViewById(R.id.main_row_4_class)).setBackgroundResource(R.drawable.star1);
							break;
						case 2:
							((ImageView)convertView.findViewById(R.id.main_row_4_class)).setBackgroundResource(R.drawable.star2);
							break;
						case 3:
							((ImageView)convertView.findViewById(R.id.main_row_4_class)).setBackgroundResource(R.drawable.star3);
							break;
						case 4:
							((ImageView)convertView.findViewById(R.id.main_row_4_class)).setBackgroundResource(R.drawable.star4);
							break;
						case 5:
							((ImageView)convertView.findViewById(R.id.main_row_4_class)).setBackgroundResource(R.drawable.star5);
							break;


						}
					}

				}
				
			}
			return convertView;
		}
    }
	
	
	public class Search_Adapter extends ArrayAdapter<SearchListData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<SearchListData> mList;
		private LayoutInflater mInflater;
		
    	public Search_Adapter(Context context, int layoutResource, ArrayList<SearchListData> mTweetList)
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
    		final SearchListData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{

				((TextView)convertView.findViewById(R.id.search_row_text)).setText(mBar.hotelName);
				LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.search_row_1);
				detailBar1.setOnClickListener(new View.OnClickListener() 
				{

					public void onClick(View v) 
					{
						ClosePopup2();

					}
				});
				
				detailBar1.setOnLongClickListener(new View.OnLongClickListener() 
				{

					

					@Override
					public boolean onLongClick(View arg0) 
					{
						return false;
					}
				});
				
			}
			return convertView;
		}
    }

	
}

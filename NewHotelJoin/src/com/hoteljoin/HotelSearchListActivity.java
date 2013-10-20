package com.hoteljoin;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.hoteljoin.data.HotelPriceList.hotelPriceListData;




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

	int MenuSize;
	 

	Integer m_CurrentPage = 1;
	Integer m_TotalPage = 1;
	Integer m_TotalCount = 0;
	 
	private View m_Footer; 
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
		
		BtnEvent(R.id.tab_1);
		BtnEvent(R.id.tab_2);
		BtnEvent(R.id.tab_3);
		
		



		AfterCreate();
		


       
        
        
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
	        		    		
	        		    		/*if ( m_Fillter )
	        		    			GetData2();
	        		    		else
	        		    			GetData();*/
	        		    	}

	        		    		
	        		    } 
     		   }
        	};
        	m_ListView = ((ListView)findViewById(R.id.search_list));
        	
        	m_ListView.setOnScrollListener(listen);
        	
        }
        

        {
            AppManagement _AppManager = (AppManagement) getApplication();
            
        	m_Adapter = new SearchList_Adapter(self, _AppManager.PParsingData.hotelPriceList.hotelPriceList);
        	
        	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            m_Footer  = mInflater.inflate(R.layout.footer, null);
            m_ListView.addFooterView(m_Footer);
            
        	m_ListView.setAdapter(m_Adapter);
    		m_ListView.setDivider(null);
    		
    		m_Adapter.notifyDataSetChanged();

    		
    		mLockListView = false;   
    		FooterHide();
        }

        
        try {
			Thread.sleep(900);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        m_Adapter.notifyDataSetChanged();
        


		
	}
	


	@Override
	public void onResume()
	{
		super.onResume();
		this.showContent();
		m_Adapter.notifyDataSetChanged();
	}
	
	
	@Override
    public void onBackPressed() 
    {
    	if ( m_bPopup2 == true)
    	{

    	}
    	else if ( m_Popup ==true)
		{

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
	


	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		case R.id.tab_1:
		{
		}
			break;
		case R.id.tab_2:
			break;
		case R.id.tab_3:
		{
			if ( mLockListView == false)
			{

				
			}
			else
			{
				self.ShowAlertDialLog( self ,"에러" , "아직 데이터를 읽는 중입니다.\n 잠시만 기다려주세요" );
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


		
	
	
	public class SearchList_Adapter extends ArrayAdapter<hotelPriceListData>
    {
    	private Context mContext;
		private ArrayList<hotelPriceListData> mList;
		private LayoutInflater mInflater;
		
		private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();
		
    	public SearchList_Adapter(Context context,  ArrayList<hotelPriceListData> mTweetList)
		{
			super(context, R.layout.search_list_row1 , mTweetList);
			this.mContext = context;
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
    		final hotelPriceListData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				if (_AppManager.m_SearchWorld == false )
				{
					if (mBar.promoYn.equals("0" ))
					{
						convertView = mInflater.inflate(R.layout.search_list_row1, null);
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_1_pic)) );
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_1_pic_2)) );
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_1_reser)) );

					}
					else
					{
						convertView = mInflater.inflate(R.layout.search_list_row2, null);
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_2_pic)) );
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_2_pic_2)) );
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_2_reser)) );

					}
				}
				
				else
				{
					if (mBar.promoYn.equals("0" ))
					{
						convertView = mInflater.inflate(R.layout.search_list_row3, null);
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_3_pic)) );
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_3_pic_2)) );
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_3_reser)) );
					}
					else
					{
						convertView = mInflater.inflate(R.layout.search_list_row4, null);
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_4_pic)) );
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_4_pic_2)) );
						mRecycleList.add(new WeakReference<View>((ImageView)convertView.findViewById(R.id.main_row_4_reser)) );
					}
				}
				
			}

			if(mBar != null) 
			{

				if (_AppManager.m_SearchWorld == false )
				{
					if (mBar.promoYn.equals("0" ))
					{
						
						FrameLayout detailBar1 = (FrameLayout)convertView.findViewById(R.id.main_row_1);

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


							}
						});
						((TextView)convertView.findViewById(R.id.main_row_1_class)).setText(mBar.starRatingName);
						
					}
					else
					{


						FrameLayout detailBar2 = (FrameLayout)convertView.findViewById(R.id.main_row_2);
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
								

							}
						});
						
						((TextView)convertView.findViewById(R.id.main_row_2_class)).setText(mBar.starRatingName);
						
					}

					
				}
				else
				{
					if (mBar.promoYn.equals("0" ))
					{
						FrameLayout detailBar3 = (FrameLayout)convertView.findViewById(R.id.main_row_3);
						
						if (mBar.availableCode.equals("1" ))
							((ImageView)convertView.findViewById(R.id.main_row_3_reser)).setBackgroundResource(R.drawable.hotellist_confirm_reserve);
						else  if (mBar.availableCode.equals("2" ))
							((ImageView)convertView.findViewById(R.id.main_row_3_reser)).setBackgroundResource(R.drawable.hotellist_wait_reserve);
						else if (mBar.availableCode.equals("3" ))
							((ImageView)convertView.findViewById(R.id.main_row_3_reser)).setBackgroundResource(R.drawable.hotellist_confirm_regist);
						else 
							((ImageView)convertView.findViewById(R.id.main_row_3_reser)).setBackgroundResource(R.drawable.hotellist_nomal_reserve);
						
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
						FrameLayout detailBar4 = (FrameLayout)convertView.findViewById(R.id.main_row_4);
						
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
	
	


	
}

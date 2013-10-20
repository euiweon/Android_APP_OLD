package com.example.hoteljoin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
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
import android.view.Gravity;
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
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class ReviewActivity extends HotelJoinBaseActivity implements OnClickListener{
	ReviewActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 
	public class ReviewListData
	{
		ReviewListData()
		{
				
		}

			

		String boardNum;
		String subject;
		String writerName;
		String regDay;
		String rating;
		String recommendCount;
		String hitCount;
		String replyCount;
		Boolean opensub = false;
		String Content = "";
		ArrayList<String>	imageList = null;
		
		
		Boolean AddImage = false;
	}
	
	ArrayList< ReviewListData > m_ListData;
	private Review_Adapter m_Adapter;
		
	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	
	private LayoutInflater mInflater;


	private ListView m_ListView;
	private View m_Footer;

	private String hotelCode="91";
	
	 Integer m_CurrentPage = 1;
	 Integer m_TotalPage = 1;
	 
	 Integer m_TotalCount = 0;
	 
	 String boardNum = "";
	 
	 String Contents = "";
	 ArrayList<String>	imageList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_main);
		
		

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
		
		m_ListView = ((ListView)findViewById(R.id.zzim_list));
        m_ListData = new ArrayList< ReviewListData >();
        m_ListData.clear();
    	m_Adapter = new Review_Adapter(this, R.layout.review_row, m_ListData);
    	
    	
    	AppManagement _AppManager = (AppManagement) getApplication();

        
		
		((TextView)findViewById(R.id.title_logo)).setText(_AppManager.m_HotelName + "의 이용후기 ") ;
    	
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Footer  = mInflater.inflate(R.layout.footer, null);
        m_ListView.addFooterView(m_Footer);

        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		
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
	        	
	        	m_ListView.setOnScrollListener(listen);
	        	
	        }
		 
		m_Adapter.notifyDataSetChanged();
		
		BottomMenuDown ( true);

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
	
	public void RefreshUI()
	{

		m_Adapter.notifyDataSetChanged();
		
		mLockListView = false;  

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
		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				data.put("page", m_CurrentPage.toString());
				data.put("hotelCode", _AppManager.m_HotelCode);
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/board/reviewList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{


						JSONArray usageList = (JSONArray)json.get("boardList");

						m_CurrentPage = json.getInt("currPage");
						m_TotalPage = json.getInt("totalPage");
	
							
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList.length(); i++)
						{
							ReviewListData item = new ReviewListData();
							JSONObject list = (JSONObject)usageList.get(i);

							item.boardNum =  _AppManager.GetHttpManager().DecodeString(list.optString("boardNum"));
							item.subject =  _AppManager.GetHttpManager().DecodeString(list.optString("subject"));
							item.writerName =  _AppManager.GetHttpManager().DecodeString(list.optString("writerName"));
							item.regDay =  _AppManager.GetHttpManager().DecodeString(list.optString("regDay"));
							item.rating =  _AppManager.GetHttpManager().DecodeString(list.optString("rating"));
							item.recommendCount =  _AppManager.GetHttpManager().DecodeString(list.optString("recommendCount"));
							item.hitCount =  _AppManager.GetHttpManager().DecodeString(list.optString("hitCount"));
							item.replyCount =  _AppManager.GetHttpManager().DecodeString(list.optString("replyCount"));
							item.opensub = false;
							item.Content = "";
							
							{
								DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								 Date tempDate = null;
								try {
									tempDate = sdFormat.parse(item.regDay);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd"); 
								item.regDay = date.format(tempDate);
							}
							
							m_ListData.add(item);
						}

						if ( m_CurrentPage >= m_TotalPage)
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
				
				RefreshUI();
				
			}
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 20:
			{
				 m_ListData.clear();
				m_Adapter.notifyDataSetChanged();
				GetData();
				
			}
				break;
			case 23:
			{
				for ( int i = 0 ; i < m_ListData.size() ; i++  )
				{
					if( m_ListData.get(i).boardNum.equals(self.boardNum) )
					{
						m_ListData.get(i).opensub = true;
						m_ListData.get(i).Content = Contents;
						
						m_ListData.get(i).imageList = new ArrayList<String>();
						for ( int j = 0 ; j < imageList.size() ; j++)
						{
							m_ListData.get(i).imageList.add(imageList.get(j));
						}

					}
				}
				RefreshUI();
			}
			case 11:
			{
				
				RefreshUI();
				FooterHide();
				
			}
				break;
			default:
				break;
			}

		}
    	
	};	
	
	
	public class Review_Adapter extends ArrayAdapter<ReviewListData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<ReviewListData> mList;
		private LayoutInflater mInflater;
		
    	public Review_Adapter(Context context, int layoutResource, ArrayList<ReviewListData> mTweetList)
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
    		final ReviewListData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				FrameLayout detailBar1 = (FrameLayout)convertView.findViewById(R.id.main_row_1);

				
				
				detailBar1.setOnClickListener(new View.OnClickListener() 
				{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						_AppManager.m_BoardNum  = mBar.boardNum;
						
						Intent intent;
			            intent = new Intent().setClass(baseself, ReviewDetailActivity2.class);
			            startActivity( intent ); 
						


						
					}

				});
				
				{
					((TextView)convertView.findViewById(R.id.main_row_1_title)).setText(mBar.subject);
					((TextView)convertView.findViewById(R.id.main_row_1_nick)).setText(mBar.writerName);
					

					((TextView)convertView.findViewById(R.id.main_row_1_day)).setText(mBar.regDay);
					/*if ( Double.parseDouble(mBar.rating) > 80)
					{
						((ImageView)convertView.findViewById(R.id.main_row_1_class)).setBackgroundResource(R.drawable.star5);
					}
					else if ( Double.parseDouble(mBar.rating) > 60)
					{
						((ImageView)convertView.findViewById(R.id.main_row_1_class)).setBackgroundResource(R.drawable.star4);
					}
					else if ( Double.parseDouble(mBar.rating) > 40)
					{
						((ImageView)convertView.findViewById(R.id.main_row_1_class)).setBackgroundResource(R.drawable.star3);
					}
					else if ( Double.parseDouble(mBar.rating) > 20)
					{
						((ImageView)convertView.findViewById(R.id.main_row_1_class)).setBackgroundResource(R.drawable.star2);
					}
					else
					{
						((ImageView)convertView.findViewById(R.id.main_row_1_class)).setBackgroundResource(R.drawable.star1);
					}
						

					((ImageView)convertView.findViewById(R.id.main_row_1_class)).setVisibility(View.VISIBLE);*/
					
					((TextView)convertView.findViewById(R.id.main_row_1_rating)).setText("평점 : " + mBar.rating );
					((TextView)convertView.findViewById(R.id.main_row_1_class_2)).setText(mBar.replyCount);
					((TextView)convertView.findViewById(R.id.main_row_2_class_2)).setText(mBar.recommendCount);
					
				}


			}
			return convertView;
		}
    }
	

    

}

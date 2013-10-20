package com.example.hoteljoin;

import java.util.ArrayList;
import java.util.Calendar;
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

public class ZzimActivity extends HotelJoinBaseActivity implements OnClickListener{
	ZzimActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 
	public class ZZimListData
	{
		ZZimListData()
		{
				
		}

			

		String nationCode;
		String hotelCode;
		String hotelName;
		Integer Type;
		
	}
	
	ArrayList< ZZimListData > m_ListData;
	private ZzimList_Adapter m_Adapter;
		
	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	
	private LayoutInflater mInflater;

	private ListView m_ListView;

	private String hotelCode="";
	
	 Calendar calendar = Calendar.getInstance();
	 Integer day;
	 Integer month;
	 Integer year;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zzimbox_main);
		
		

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
        m_ListData = new ArrayList< ZZimListData >();
        m_ListData.clear();
    	m_Adapter = new ZzimList_Adapter(this, R.layout.zzimbox_row, m_ListData);
    	
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		m_Adapter.notifyDataSetChanged();
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
	
	
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{


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

				
				Map<String, String> data = new HashMap  <String, String>();


				data.put("memberId", _AppManager.m_LoginID);
					

		
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/bookmark/bookmarkList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						JSONArray usageList = (JSONArray)json.get("hotelList");

							
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList.length(); i++)
						{
							ZZimListData item = new ZZimListData();
							JSONObject list = (JSONObject)usageList.get(i);

							item.nationCode =  _AppManager.GetHttpManager().DecodeString(list.optString("nationCode"));
							item.hotelCode =  _AppManager.GetHttpManager().DecodeString(list.optString("hotelCode"));
							item.hotelName =  _AppManager.GetHttpManager().DecodeString(list.optString("hotelName"));
							item.Type = 0;


							


							m_ListData.add(item);
						}
						ZZimListData item2 = new ZZimListData();
						item2.Type = 1;
						
						m_ListData.add(item2);
			
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
	
	public void GetDelete()
	{

		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				data.put("memberId", _AppManager.m_LoginID);
				
				data.put("hotelCode", hotelCode);
					

		
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/bookmark/bookmarkDelete.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{


						handler.sendEmptyMessage(20);

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
			case 20:
			{
				 m_ListData.clear();
				m_Adapter.notifyDataSetChanged();
				GetData();
				
			}
				break;
			default:
				break;
			}

		}
    	
	};	
	
	public class ZzimList_Adapter extends ArrayAdapter<ZZimListData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<ZZimListData> mList;
		private LayoutInflater mInflater;
		
    	public ZzimList_Adapter(Context context, int layoutResource, ArrayList<ZZimListData> mTweetList)
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
    		final ZZimListData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				FrameLayout detailBar1 = (FrameLayout)convertView.findViewById(R.id.main_row_1);
				FrameLayout detailBar2 = (FrameLayout)convertView.findViewById(R.id.main_row_2);
				
				if ( mBar.Type == 0  )
				{
					detailBar1.setVisibility(View.VISIBLE);
					detailBar2.setVisibility(View.GONE);
					
					
					((TextView)convertView.findViewById(R.id.main_row_1_day)).setText(mBar.nationCode + " " + mBar.hotelCode );
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
					
					
					detailBar1.setOnLongClickListener(new View.OnLongClickListener() 
					{

						


						@Override
						public boolean onLongClick(View arg0)
						{
							// TODO Auto-generated method stub
						  	new AlertDialog.Builder(self)
							 .setTitle("삭제 메세지")
							 .setMessage("삭제 하시겠습니까?") //줄였음
							 .setPositiveButton("예", new DialogInterface.OnClickListener() 
							 {
							     public void onClick(DialogInterface dialog, int whichButton)
							     {   
							    	 hotelCode = mBar.hotelCode;
									GetDelete();
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
							
							return true;
						}
					});
				}
				else
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.VISIBLE);
				}
				
			}
			return convertView;
		}
    }
	

    

}

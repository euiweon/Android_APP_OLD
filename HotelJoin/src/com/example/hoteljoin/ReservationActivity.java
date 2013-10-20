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

public class ReservationActivity extends HotelJoinBaseActivity implements OnClickListener{
	ReservationActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 

	public class resvList
	{
		resvList()
		{
			
		}
			
		String resvNum;
		String hotelName;
		String nationCode;
		String resvStatusCode;
		String resvStatusName;

	}
	ArrayList< resvList > m_ListData;
	private resvList_Adapter m_Adapter;
		
	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	
	private LayoutInflater mInflater;
	
	private ListView m_ListView;




	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation_main);
		
		

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
		
		
		
    	m_ListView = ((ListView)findViewById(R.id.main_list));
    	
    
    	m_ListData = new ArrayList< resvList >();
    	m_ListData.clear();
		m_Adapter = new resvList_Adapter(this, R.layout.reser_main_row, m_ListData);

		m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		m_Adapter.notifyDataSetChanged();

		
		BtnEvent(R.id.call1_btn);
		BtnEvent(R.id.call2_btn);
		AfterCreate(3);
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
	
	
	
	public void GetData()
	{

		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				Map<String, String> data = new HashMap  <String, String>();

				if ( _AppManager.m_LoginCheck == false)
				{
					//data.put("resvName", "오정현");
					//data.put("resvNum", "130204-KR105947");
					
					data.put("resvName", _AppManager.m_ReservationName);
					data.put("resvNum", _AppManager.m_ReservationNumber);
					

				}
				else
				{

					//data.put("memberId", "drlys73");
					data.put("memberId", _AppManager.m_LoginID);
					
				}

				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/booking/bookingList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						JSONArray usageList = (JSONArray)json.get("resvList");
						

							
							
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList.length(); i++)
						{

							resvList item = new resvList();
							JSONObject list = (JSONObject)usageList.get(i);
							item.resvNum =  _AppManager.GetHttpManager().DecodeString(list.optString("resvNum"));
							item.hotelName =  _AppManager.GetHttpManager().DecodeString(list.optString("hotelName"));
							item.nationCode =  _AppManager.GetHttpManager().DecodeString(list.optString("nationCode"));
							item.resvStatusCode =  _AppManager.GetHttpManager().DecodeString(list.optString("resvStatusCode"));
							item.resvStatusName =  _AppManager.GetHttpManager().DecodeString(list.optString("resvStatusName"));
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
				m_Adapter.notifyDataSetChanged();
				RefreshUI();
				
			}
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;

			case 20:
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

		case R.id.call1_btn:
			startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0220174600" )));
			break;
		case R.id.call2_btn:
			startActivity(new Intent(Intent.ACTION_DIAL , Uri.parse("tel:" + "0220174644")));
			break;





		}
		
	}
	
	public class resvList_Adapter extends ArrayAdapter<resvList>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<resvList> mList;
		private LayoutInflater mInflater;
		
    	public resvList_Adapter(Context context, int layoutResource, ArrayList<resvList> mTweetList)
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
    		final resvList mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				((TextView)convertView.findViewById(R.id.main_row_1_title)).setText(mBar.hotelName);
				((TextView)convertView.findViewById(R.id.main_row_1_sub)).setText(mBar.resvStatusName);
				
				FrameLayout detailBar1 = (FrameLayout)convertView.findViewById(R.id.main_row_1);
				detailBar1.setOnClickListener(new View.OnClickListener() 
				{

					@Override
					public void onClick(View v) 
					{

						_AppManager.m_ReservationNationCode = mBar.nationCode;
						if ( _AppManager.m_LoginCheck == false)
						{

						}
						else
						{

							_AppManager.m_ReservationNumber = mBar.resvNum;
							_AppManager.m_ReservationNationCode = mBar.nationCode;
						}
						Intent intent;
			            intent = new Intent().setClass(baseself, ReservationDetailActivity.class);
			            startActivity( intent ); 
					}

				});
			}

			return convertView;
		}
    }

    

}

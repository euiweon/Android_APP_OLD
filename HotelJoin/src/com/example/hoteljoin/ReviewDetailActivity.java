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
import android.util.TypedValue;
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

public class ReviewDetailActivity extends HotelJoinBaseActivity implements OnClickListener{
	ReviewDetailActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;


		
	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	
	private LayoutInflater mInflater;


	private ListView m_ListView;
	private View m_Footer;

	private String hotelCode="91";
	
	 Integer m_CurrentPage = 1;
	 Integer m_TotalPage = 1;
	 
	 Integer m_TotalCount = 0;
	 

	 
	 
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_detail);
		
		

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
		

		
		//((TextView)findViewById(R.id.title_logo)).setText(_AppManager.m_HotelName + "의 이용후기 ") ;
		
		BottomMenuDown ( true);
		
		GetDetailData();



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
		mLockListView = false;  
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{


		}
		
	}

	

	
	public void GetDetailData()
	{

		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				data.put("boardNum", _AppManager.m_BoardNum);
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/board/reviewDetail.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{


						JSONObject usageList = (JSONObject)json.get("boardInfo");

						boardNum =  _AppManager.GetHttpManager().DecodeString(usageList.optString("boardNum"));
						subject =  _AppManager.GetHttpManager().DecodeString(usageList.optString("subject"));
						writerName =  _AppManager.GetHttpManager().DecodeString(usageList.optString("writerName"));
						regDay =  _AppManager.GetHttpManager().DecodeString(usageList.optString("regDay"));
						rating =  _AppManager.GetHttpManager().DecodeString(usageList.optString("rating"));
						recommendCount =  _AppManager.GetHttpManager().DecodeString(usageList.optString("recommendCount"));
						hitCount =  _AppManager.GetHttpManager().DecodeString(usageList.optString("hitCount"));
						replyCount =  _AppManager.GetHttpManager().DecodeString(usageList.optString("replyCount"));
						Content =  _AppManager.GetHttpManager().DecodeString(usageList.optString("contents"));

						
						JSONArray usageList2 = (JSONArray)json.get("imageList");
						
						imageList = new ArrayList<String>();
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList2.length(); i++)
						{
						
							JSONObject list = (JSONObject)usageList2.get(i);
							imageList.add( _AppManager.GetHttpManager().DecodeString(list.optString("middleImageUrl")));

							
							
						}
	
						
						handler.sendEmptyMessage(23);

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

				// 헤더
				((TextView)findViewById(R.id.main_row_1_title)).setText(subject);
				((TextView)findViewById(R.id.main_row_1_nick)).setText(writerName);
				((TextView)findViewById(R.id.main_row_1_day)).setText(regDay);
				if ( Double.parseDouble(rating) > 80)
				{
					((ImageView)findViewById(R.id.main_row_1_class)).setBackgroundResource(R.drawable.star5);
				}
				else if ( Double.parseDouble(rating) > 60)
				{
					((ImageView)findViewById(R.id.main_row_1_class)).setBackgroundResource(R.drawable.star4);
				}
				else if ( Double.parseDouble(rating) > 40)
				{
					((ImageView)findViewById(R.id.main_row_1_class)).setBackgroundResource(R.drawable.star3);
				}
				else if ( Double.parseDouble(rating) > 20)
				{
					((ImageView)findViewById(R.id.main_row_1_class)).setBackgroundResource(R.drawable.star2);
				}
				else
				{
					((ImageView)findViewById(R.id.main_row_1_class)).setBackgroundResource(R.drawable.star1);
				}
					

				((ImageView)findViewById(R.id.main_row_1_class)).setVisibility(View.VISIBLE);
				
				
				
				
				for ( int i = 0 ; i < imageList.size() ; i++ )
				{
					LinearLayout linear = (LinearLayout)findViewById(R.id.main_row_2_1);
					ImageView iv = new ImageView(self);
					iv.setAdjustViewBounds(true);
			        iv.setLayoutParams(new Gallery.LayoutParams(
			        		LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			        iv.setScaleType(ImageView.ScaleType.CENTER); // 레이아웃 크기에 이미지를 맞춘다
			        
			        iv.setTag( imageList.get(i));
					BitmapManager.INSTANCE.loadBitmap_2(imageList.get(i), iv);
			        
			        linear.addView(iv);

				}
				
				
				
				TextView des = new TextView(self);
				des.setText( Content );
				des.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18 );
				LinearLayout linear1 = (LinearLayout)findViewById(R.id.main_row_2);
				linear1.addView(des);
				
				LinearLayout.LayoutParams margin = (LinearLayout.LayoutParams )(des.getLayoutParams());
				margin.setMargins(15, 15, 0, 0);
				des.setLayoutParams(margin);
				
			}

			default:
				break;
			}

		}
    	
	};	
	

	

    

}

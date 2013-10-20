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

public class ReviewDetailActivity2 extends HotelJoinBaseActivity implements OnClickListener{
	ReviewDetailActivity2  self;
	 SlidingMenu menu ;
	 int MenuSize;


		
	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	


	private String hotelCode="91";
	
	public class BBS_ViewData
	{
		Integer Type;
		String diaryNum;
		String subject;
		String contents;
		String imageUrl;
		String writerName;
		String regDay;
	}
	
	ArrayList< BBS_ViewData > m_ListData;
	private Review_Adapter m_Adapter;

	
	
	private LayoutInflater mInflater;


	private ListView m_ListView;
	private View m_Footer;

	
	 Integer m_CurrentPage = 1;
	 Integer m_TotalPage = 1;
	 
	 Integer m_TotalCount = 0;
	 
	 String boardNum = "";
	 
	 String Contents = "";
	 ArrayList<String>	imageList;
	 
		String subject;
		String writerName;
		String regDay;
		String rating;
		String recommendCount;
		String hitCount;
		String replyCount;
		Boolean opensub = false;
	 

		String replyNum = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_detail2);
		
		

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
		
		m_ListView = ((ListView)findViewById(R.id.main_list));
        m_ListData = new ArrayList< BBS_ViewData >();
        m_ListData.clear();
    	m_Adapter = new Review_Adapter(this, R.layout.review_detail_row, m_ListData);
        

    	{
    		AppManagement _AppManager = (AppManagement) getApplication();
    		((TextView)findViewById(R.id.title_logo)).setText(_AppManager.m_HotelName + "의 이용후기 ") ;
    	}
    	
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		 
		m_Adapter.notifyDataSetChanged();
		
		BottomMenuDown ( true);
		
		BtnEvent(R.id.repl_add);
		BtnEvent(R.id.repl_good);

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
		AppManagement _AppManager = (AppManagement) getApplication();
		switch(v.getId())
		{
		case R.id.repl_add:
		{
			if (_AppManager.m_LoginCheck == true)
				AddReplyData();
			else
			{
				self.ShowAlertDialLog( self ,"에러" , "로그인시 이용가능합니다. " );
			}
		}
			break;
			
		case R.id.repl_good:
			AddGoodData();
			break;

		}
		
	}

	
	public void GetData()
	{
		m_ListData.clear();
		mProgress.show();
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
						

						
						
						JSONArray usageList2 = (JSONArray)json.get("imageList");
						
						imageList = new ArrayList<String>();
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList2.length(); i++)
						{
							JSONObject list = (JSONObject)usageList2.get(i);
							BBS_ViewData item2 = new BBS_ViewData();
							item2.Type = 0; 
							item2.imageUrl = list.optString("middleImageUrl");
							
							m_ListData.add(item2);
						}
						
						BBS_ViewData item = new BBS_ViewData();
						item.Type = 1; 
						
						item.contents = usageList.optString("contents");
						m_ListData.add(item);
						
						handler.sendEmptyMessage(11);

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
					handler.sendMessage(handler.obtainMessage(1,"json Data Error" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	
	public void GetReplyData()
	{
		mProgress.show();
		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();



				data.put("boardNum", _AppManager.m_BoardNum);
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/board/reviewDetailReplies.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						
						
						JSONArray usageList2 = (JSONArray)json.get("replyList");
						for(int i = 0; i < usageList2.length(); i++)
						{
							JSONObject list = (JSONObject)usageList2.get(i);
							
							BBS_ViewData item = new BBS_ViewData();
							item.diaryNum =  _AppManager.GetHttpManager().DecodeString(list.optString("replyNum"));
							item.writerName = _AppManager.GetHttpManager().DecodeString(list.optString("writerName"));
							item.contents =  list.optString("contents");
							item.regDay =  _AppManager.GetHttpManager().DecodeString(list.optString("regDay"));
							
							item.Type = 3; 
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
	
	
	public void AddReplyData()
	{
		mProgress.show();
		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				EditText text = (EditText)findViewById(R.id.repl_data);
				data.put("boardNum", _AppManager.m_BoardNum);
				data.put("contents", text.getText().toString());
				data.put("memberId", _AppManager.m_LoginID);
				data.put("writerName", _AppManager.m_Name);
				
					
				String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mweb/board/reviewDetailAddReply.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						handler.sendEmptyMessage(72);



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
	
	
	public void DeleteData()
	{
		mProgress.show();
		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				data.put("replyNum", self.replyNum);
				data.put("memberId", _AppManager.m_LoginID);

					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/board/reviewDetailDeleteReply.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						handler.sendEmptyMessage(80);

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
	
	
	public void AddGoodData()
	{
		mProgress.show();
		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				data.put("boardNum",_AppManager.m_BoardNum);
				
				if ( _AppManager.m_LoginCheck == true)
					data.put("memberId", _AppManager.m_LoginID);
				
					
				String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mweb/board/reviewAddRecommend.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						handler.sendEmptyMessage(90);



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
				FooterHide();
				break;
			}
			case 11:
			{
				
				
				((TextView)findViewById(R.id.main_row_1_title)).setText(subject);
				((TextView)findViewById(R.id.main_row_1_nick)).setText(writerName);
				((TextView)findViewById(R.id.main_row_1_day)).setText(regDay);
				
				{
					DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					 Date tempDate = null;
					try {
						tempDate = sdFormat.parse(regDay);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd"); 
					((TextView)findViewById(R.id.main_row_1_day)).setText(date.format(tempDate));
				}
				
				/*if ( Double.parseDouble(rating) > 80)
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
					

				((ImageView)findViewById(R.id.main_row_1_class)).setVisibility(View.VISIBLE);*/
				
				((TextView)findViewById(R.id.main_row_1_rating)).setText("평점 : " + rating );
				((TextView)findViewById(R.id.main_row_1_class_2)).setText(replyCount);
				((TextView)findViewById(R.id.main_row_2_class_2)).setText(recommendCount);
				
				{
					BBS_ViewData item = new BBS_ViewData();
					item.Type = 2; 
					m_ListData.add(item);
				}
				
				
				GetReplyData();
				
			}
				break;
			case 72:
			{
				GetData();
			}
				break; 
			case 80:
				onBackPressed();
				break;
			case 90:
				self.ShowAlertDialLog( self ,"추천완료" ,"추천 처리 되었습니다." );
				break;
			default:
				break;
			}

		}
    	
	};	
	
	public class Review_Adapter extends ArrayAdapter<BBS_ViewData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<BBS_ViewData> mList;
		private LayoutInflater mInflater;
		
    	public Review_Adapter(Context context, int layoutResource, ArrayList<BBS_ViewData> mTweetList)
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
    		final BBS_ViewData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.main_row_1);
				LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.main_row_2);
				LinearLayout detailBar3 = (LinearLayout)convertView.findViewById(R.id.main_row_3);
				LinearLayout detailBar4 = (LinearLayout)convertView.findViewById(R.id.main_row_4);
				
				switch(mBar.Type)
				{
				case 0:
				{
					detailBar1.setVisibility(View.VISIBLE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					
					ImageView Image = (ImageView)convertView.findViewById(R.id.main_row_1_image);
					
					Image.setTag( mBar.imageUrl);;
					BitmapManager.INSTANCE.loadBitmap_2(mBar.imageUrl, Image);
					
				}
					break; 
				case 1:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.VISIBLE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					((TextView)convertView.findViewById(R.id.main_row_2_title)).setText(mBar.contents);
				}
					break;
				case 2:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.VISIBLE);
					detailBar4.setVisibility(View.GONE);
					
					
					((ImageView)convertView.findViewById(R.id.main_row_3_btn_1)).setVisibility(View.GONE);
					((ImageView)convertView.findViewById(R.id.main_row_3_btn_2)).setVisibility(View.GONE);
					((ImageView)convertView.findViewById(R.id.main_row_3_btn_3)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{

						}
					});
					
				}
					break;
				case 3:
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.VISIBLE);
					((TextView)convertView.findViewById(R.id.main_row_4_detail_nick)).setText(mBar.writerName);
					((TextView)convertView.findViewById(R.id.main_row_4_detail_time)).setText(mBar.regDay);
					((TextView)convertView.findViewById(R.id.main_row_4_detail_data)).setText(mBar.contents);
					
					
					detailBar4.setOnLongClickListener(new View.OnLongClickListener() 
					{

						

						@Override
						public boolean onLongClick(View arg0) 
						{
							
							if (mBar.writerName.equals(_AppManager.m_Name))
							{
								new AlertDialog.Builder(self)
								 .setTitle("삭제")
								 .setMessage("삭제 하시겠습니까?") //줄였음
								 .setPositiveButton("예", new DialogInterface.OnClickListener() 
								 {
								     public void onClick(DialogInterface dialog, int whichButton)
								     {   
								    	 DeleteData();
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
							}
							return false;
						}
					});
				}
					break;
					
				}

			


			}
			return convertView;
		}
    }
	

	

    

}

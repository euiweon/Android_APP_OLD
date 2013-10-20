package com.example.hoteljoin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.example.hoteljoin.TravelActivity.TravelListData;
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

public class TravelDetailActivity extends HotelJoinBaseActivity implements OnClickListener{
	TravelDetailActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 
	public class TravelListData
	{
		TravelListData()
		{
					
		}

				

		String diaryNum;
		String prevDiaryNum;
		String nextDiaryNum;
		String subject;
		String contents;
		String imageUrl;
		String writerName;
		String regDay;
		String nationCode;
		String cityCode;
		String hotelName;
		String hotelCode;
		String recommendCount;
		String hitCount;
		String replyCount;
		String cntntNum;

	}
	 

	public class BBS_ViewData
	{
		Integer Type;
		String cntntNum;
		String diaryNum;
		String subject;
		String contents;
		String imageUrl;
		String writerName;
		String regDay;
	}
	
	ArrayList< BBS_ViewData > m_ListData;
	private Review_Adapter m_Adapter;
		
	public Boolean m_bFooter = true;
	
	
	private LayoutInflater mInflater;


	private ListView m_ListView;
	private View m_Footer;

	private boolean mLockListView; 

	
	 Integer m_CurrentPage = 1;
	 Integer m_TotalPage = 1;
	 
	 Integer m_TotalCount = 0;
	 
	 String boardNum = "";
	 
	 String Contents = "";
	 ArrayList<String>	imageList;
	 
	 TravelListData m_TrvelListData = new TravelListData();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.travel_detail);
		
		

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
    	m_Adapter = new Review_Adapter(this, R.layout.travel_detail_row, m_ListData);
        
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
	        		    		GetReplyData();
	        		    	}

	        		    		
	        		    } 
     		   }
        	};
        	
        	m_ListView.setOnScrollListener(listen);
        	
        }
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Footer  = mInflater.inflate(R.layout.footer, null);
        m_ListView.addFooterView(m_Footer);

        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		 
		m_Adapter.notifyDataSetChanged();
		
		BottomMenuDown ( true);
		
		BtnEvent(R.id.repl_add);
		BtnEvent(R.id.repl_good);
		
		

		

	}
	

	@Override
	public void onResume()
	{
		super.onResume();
		GetData();
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
				self.ShowAlertDialLog( self ,"에러" , "로그인후 사용할수 있습니다. " );
			}
		}
			break;
			
		case R.id.repl_good:
		{
			if (_AppManager.m_LoginCheck == true)
				AddGoodData();
			else
			{
				self.ShowAlertDialLog( self ,"에러" , "로그인후 사용할수 있습니다. " );
			}
		}
			break;
			

		}
		
	}

	
	public void GetData()
	{
		mProgress.show();
		mLockListView = true;
		m_ListData.clear();
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();



				data.put("diaryNum", _AppManager.m_DirNum);
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/board/diaryDetail.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						
						JSONObject usageList = (JSONObject)json.get("tourDiaryInfo");
						{
							m_TrvelListData.diaryNum =  _AppManager.GetHttpManager().DecodeString(usageList.optString("diaryNum"));
							m_TrvelListData.prevDiaryNum =  _AppManager.GetHttpManager().DecodeString(usageList.optString("prevDiaryNum"));
							m_TrvelListData.nextDiaryNum = _AppManager.GetHttpManager().DecodeString(usageList.optString("nextDiaryNum"));
							m_TrvelListData.subject =  _AppManager.GetHttpManager().DecodeString(usageList.optString("subject"));
							m_TrvelListData.writerName =  _AppManager.GetHttpManager().DecodeString(usageList.optString("writerName"));
							m_TrvelListData.regDay =  _AppManager.GetHttpManager().DecodeString(usageList.optString("regDay"));
							m_TrvelListData.nationCode =  _AppManager.GetHttpManager().DecodeString(usageList.optString("nationCode"));
							m_TrvelListData.cityCode =  _AppManager.GetHttpManager().DecodeString(usageList.optString("cityCode"));
							m_TrvelListData.recommendCount =  _AppManager.GetHttpManager().DecodeString(usageList.optString("recommendCount"));
							m_TrvelListData.hitCount =  _AppManager.GetHttpManager().DecodeString(usageList.optString("hitCount"));
							m_TrvelListData.replyCount =  _AppManager.GetHttpManager().DecodeString(usageList.optString("replyCount"));
							
							m_TrvelListData.hotelName = (usageList.optString("hotelName"));
							m_TrvelListData.hotelCode = usageList.optString("hotelCode");
						}
						
						JSONArray usageList2 = (JSONArray)json.get("subDiaryList");
						m_ListData.clear();

						
						

						

						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList2.length(); i++)
						{

							JSONObject list = (JSONObject)usageList2.get(i);

							if (_AppManager.m_LoginCheck == true && m_TrvelListData.writerName.equals(_AppManager.m_Name))
							{
								BBS_ViewData item = new BBS_ViewData();
								item.Type = -1; 
								item.contents =list.optString("contents");
								item.imageUrl =list.optString("imageUrl");
								item.cntntNum = list.optString("cntntNum");
								m_ListData.add(item);
							}
							
							{
								BBS_ViewData item = new BBS_ViewData();
								item.Type = 0; 
								item.cntntNum = list.optString("cntntNum");
								item.imageUrl =list.optString("imageUrl");
								m_ListData.add(item);
							}
							
							{
								BBS_ViewData item = new BBS_ViewData();
								item.Type = 1; 
								item.cntntNum = list.optString("cntntNum");
								item.contents =list.optString("contents");
								m_ListData.add(item);
							}
						}
						

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


				data.put("page", m_CurrentPage.toString());
				data.put("diaryNum", _AppManager.m_DirNum);
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/board/diaryReplyList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						m_CurrentPage = json.getInt("currPage");
						m_TotalPage = json.getInt("totalPage");
						
						
						JSONArray usageList2 = (JSONArray)json.get("replyList");
						for(int i = 0; i < usageList2.length(); i++)
						{
							JSONObject list = (JSONObject)usageList2.get(i);
							
							BBS_ViewData item = new BBS_ViewData();
							item.diaryNum =  _AppManager.GetHttpManager().DecodeString(list.optString("replyNum"));
							item.subject =  _AppManager.GetHttpManager().DecodeString(list.optString("memberId"));
							item.writerName = _AppManager.GetHttpManager().DecodeString(list.optString("writerName"));
							item.contents =  _AppManager.GetHttpManager().DecodeString(list.optString("contents"));
							item.regDay =  _AppManager.GetHttpManager().DecodeString(list.optString("regDay"));
							
							item.Type = 3; 
							m_ListData.add(item);

						}

						if ( m_CurrentPage == m_TotalPage)
							handler.sendEmptyMessage(23);
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
				data.put("diaryNum", _AppManager.m_DirNum);
				data.put("contents", text.getText().toString());
				data.put("memberId", _AppManager.m_LoginID);
				data.put("writerName", _AppManager.m_Name);
				
					
				String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mweb/board/diaryReplyAdd.gm", data);

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


				data.put("diaryNum", _AppManager.m_DirNum);
				data.put("cntntNum", self.m_TrvelListData.cntntNum);
				data.put("memberId", _AppManager.m_LoginID);

					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/board/diaryDelete.gm", data);

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


				data.put("diaryNum", _AppManager.m_DirNum);
				data.put("memberId", _AppManager.m_LoginID);
				
					
				String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mweb/board/diaryAddRecommend.gm", data);

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
				if(m_ListData.size() == 0)
				{
					onBackPressed();
				}
				else
				{
					((TextView)findViewById(R.id.main_row_1_title)).setText(m_TrvelListData.subject);
					((TextView)findViewById(R.id.main_row_1_nick)).setText(m_TrvelListData.writerName);
					((TextView)findViewById(R.id.main_row_1_day)).setText(m_TrvelListData.regDay);
					((TextView)findViewById(R.id.main_row_1_class_2)).setText(m_TrvelListData.replyCount);
					((TextView)findViewById(R.id.main_row_2_class_2)).setText(m_TrvelListData.recommendCount);

					GetReplyData();
				}
				

				
			}
				break;
			case 72:
			{
				GetData();
			}
				break; 
			case 80:
				GetData();
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
				LinearLayout detailBar0 = (LinearLayout)convertView.findViewById(R.id.main_row_0);
				LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.main_row_1);
				LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.main_row_2);
				LinearLayout detailBar3 = (LinearLayout)convertView.findViewById(R.id.main_row_3);
				LinearLayout detailBar4 = (LinearLayout)convertView.findViewById(R.id.main_row_4);
				
				switch(mBar.Type)
				{
				case -1:
				{
					detailBar0.setVisibility(View.VISIBLE);
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					
					((ImageView)convertView.findViewById(R.id.main_row_0_image)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							String []	NationList = new String[]{"수정" , "삭제", "추가등록", "SNS 공유"};
							

							AlertDialog.Builder alt_bld = new AlertDialog.Builder(self);
					        alt_bld.setTitle("항목 선택");
					        alt_bld.setSingleChoiceItems(NationList, -1, new DialogInterface.OnClickListener() 
					        {
					            public void onClick(DialogInterface dialog, int item) 
					            {
					            	
					            	dialog.cancel();
					            	switch(item)
					            	{
					            	case 0:
						            	{
						            		// 수정
						            		_AppManager.m_diaryNum = _AppManager.m_DirNum;
						            		_AppManager.m_cntntNum  = mBar.cntntNum;
						            		_AppManager.m_rewriteImage = mBar.imageUrl;
						            		_AppManager.m_rewriteContent =  mBar.contents;
						            		
						            		_AppManager.m_rewriteTitle = m_TrvelListData.subject;
						            		
						            		
						            		_AppManager.m_rewritenation = m_TrvelListData.nationCode;
						            		_AppManager.m_rewritecity = m_TrvelListData.cityCode;
						            		_AppManager.m_rewritehotelcode  = m_TrvelListData.hotelCode;
						            		_AppManager.m_rewritehotelname  = m_TrvelListData.hotelName;
						            		
						            		
						            		_AppManager.m_RefreshUI = true;
						            		Intent intent;
								            intent = new Intent().setClass(baseself, TravelReWriteActivity.class);
								            startActivity( intent ); 
						            		
						            	}
					            		break;
					            	case 1:
						            	{
						            		// 삭제
						            		_AppManager.m_diaryNum = _AppManager.m_DirNum;
						            		 self.m_TrvelListData.cntntNum  = mBar.cntntNum;
						            		 DeleteData();
						            	}
						            	break;
					            	case 2:
						            	{
						            		// 추가등록
						            		_AppManager.m_diaryNum = _AppManager.m_DirNum;
						            		
						            		Intent intent;
								            intent = new Intent().setClass(baseself, TravelAddWriteActivity.class);
								            startActivity( intent ); 
						            	}
						            	break;
					            	case 3:
						            	{
						            		// SNS 공유
						            		self.ShowAlertDialLog( self ,"테스트중" ,"현재 테스트 중이라, 잠시 막아 둡니다. " );
						            	}
						            	break;
					            	}
					            }
					        });
					        AlertDialog alert = alt_bld.create();
					        alert.show();
						}
					});
					
				}
					break;
				case 0:
				{
					detailBar0.setVisibility(View.GONE);
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
					detailBar0.setVisibility(View.GONE);
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.VISIBLE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.GONE);
					((TextView)convertView.findViewById(R.id.main_row_2_title)).setText(mBar.contents);
				}
					break;
				case 2:
				{
					detailBar0.setVisibility(View.GONE);
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.VISIBLE);
					detailBar4.setVisibility(View.GONE);
					
					((ImageView)convertView.findViewById(R.id.main_row_3_btn_1)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{

						}
					});
					((ImageView)convertView.findViewById(R.id.main_row_3_btn_2)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							if(_AppManager.m_LoginID.equals(mBar.writerName) )
									DeleteData();
							else
							{
								self.ShowAlertDialLog( self ,"에러" , "삭제 권한이 없습니다." );
							}
						}
					});
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
					detailBar0.setVisibility(View.GONE);
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.GONE);
					detailBar3.setVisibility(View.GONE);
					detailBar4.setVisibility(View.VISIBLE);
					((TextView)convertView.findViewById(R.id.main_row_4_detail_nick)).setText(mBar.writerName);
					((TextView)convertView.findViewById(R.id.main_row_4_detail_time)).setText(mBar.regDay);
					((TextView)convertView.findViewById(R.id.main_row_4_detail_data)).setText(mBar.contents);
				}
					break;
					
				}

			


			}
			return convertView;
		}
    }
	

    

}

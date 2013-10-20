package com.humapcontents.mapp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.euiweonjeong.base.BitmapManager;
import com.humapcontents.mapp.data.HomeAlbum;




public class SquareMainActivity extends MappBaseActivity implements OnClickListener {

	private class SquareListData
	{
		int  type;
		int  idx;
		Bitmap img;
		String title;
		String nick;
		String imgurl;
		ArrayList<Integer>  branddetail;
	} 
	
	private class SquareListCo
	{
		int count;
		
		int type1;
		int  no;
		int  idx;
		String imgurl;
		Bitmap img;
		String title;
		String nick;
		ArrayList<Integer> imgn;
		
		
		int  type2;
		int  idx2;
		String imgurl2;
		Bitmap img2;
		String title2;
		String nick2;
		
		String url;
		
		ArrayList<Integer> imgn2;
	}

	protected static final int CAPTURE_MOVIE = 1000;
	
	private SquareMainActivity self;
	
	int m_SelectStageIndex = 0;
	
	private ListView m_ListView;
	
	
	ArrayList< SquareListData >	m_ListData;
	ArrayList< SquareListCo > m_ListCoData;
	private AuditionRow_Adapter m_Adapter;
	
    Integer m_Offset  = -1; 
    Integer m_SortIndex = 0;
    Boolean m_Refresh = true;
    

	
	Gallery m_Gallery ;
	
	private boolean mLockListView; 
	
	public ProgressDialog mProgress2;
	private LayoutInflater mInflater;
	
	private View m_Footer;
	
	public Boolean m_bFooter = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);  // 인트로 레이아웃 출력     

        self = this;

        mLockListView = true; 
        
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        {
        	mProgress2 = new ProgressDialog(this);
        	mProgress2.setMessage("문제가 생겨 리스트를 갱신중입니다. ");
        	mProgress2.setIndeterminate(true);
        	mProgress2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        	mProgress2.setCancelable(false);
        }
		

        ImageResize(R.id.main_layout);
        ImageResize(R.id.map_list_view);
        {
        	OnScrollListener listen = new OnScrollListener() 
        	{
      		  
     		   public void onScrollStateChanged(AbsListView view, int scrollState) {}
     		  
     		   // 첫번째 아이템이 안 보이면 하단 바를 꺼준다. 
     		   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
     		   {
	        		    if(totalItemCount > 0 && firstVisibleItem != 0) 
	        		    {
	        		    	self.BottomMenuDown(true);
	        		    }
	        		    else
	        		    {
	        		    	self.BottomMenuDefault();
	        		    }
	        		    
	        		    // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이 
	        		    // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다. 
	        		    int count = totalItemCount - visibleItemCount; 

	        		    if(firstVisibleItem >= count && totalItemCount != 0   && mLockListView == false) 
	        		    { 
	        		    	      // 추가
	        		    	
	        		    	if (m_bFooter == true )
	        		    	{
	        		    		GetMoreAudiotionList();
	        		    	}

	        		    		
	        		    } 
     		   }
        	};
        	m_ListView = ((ListView)findViewById(R.id.map_list_view));
        	
        	m_ListView.setOnScrollListener(listen);
        	
        }
        
        {
        	
        	
        	FrameLayout imageview = (FrameLayout)findViewById(R.id.title_bar);
            imageview.setOnClickListener(this);
            ((TextView)findViewById(R.id.title_desc)).setOnClickListener(this);
            ((TextView)findViewById(R.id.title_name)).setOnClickListener(this);
        }

        m_ListData = new ArrayList< SquareListData >();
        m_ListCoData = new ArrayList< SquareListCo >();
    	m_ListCoData.clear();
    	m_ListData.clear();
    	m_Adapter = new AuditionRow_Adapter(this, R.layout.audition_row, m_ListCoData);
    	
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Footer  = mInflater.inflate(R.layout.footer, null);
        m_ListView.addFooterView(m_Footer);
        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null); 
       
        BottomMenuDown(true);
        AfterCreate( 3 );
        
        GetAudiotionList();

    }
    
    public void FooterHide()
    {
    	if (m_bFooter == true)
    	{
    		m_bFooter = false;
    		
    		(m_Footer).setVisibility(View.GONE);
 /*   		 m_ListView.removeFooterView(mInflater.inflate(R.layout.footer, null));
    		m_ListView.setAdapter(m_Adapter);*/
    	}
    }
    public void FooterShow()
    {
    	if (m_bFooter == false)
    	{
    		m_bFooter = true;
    		(m_Footer).setVisibility(View.VISIBLE);
 /*   	    m_ListView.addFooterView(mInflater.inflate(R.layout.footer, null));
    		m_ListView.setAdapter(m_Adapter);*/
    	}
    }
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
    



	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		
		switch(arg0.getId() )
		{
		
		case R.id.title_bar:
			break;
		case R.id.title_name:
		case R.id.title_desc:
			
		{

		}
			
			break;
		}
		
	}
	
	public void RefreshUI()
	{
		if ( m_SortIndex != 2  && m_ListData.size() > 19 )
		{
			FooterShow();
		}
		else
		{
			FooterHide();
		}

		
		// 데이터가 하나만 존재 할경우에 대한 예외 처리 
		if ( m_ListData.size() == 1)
		{
			SquareListCo data = new SquareListCo();; 
			
			data.type1 = m_ListData.get(0 ).type;
			data.idx = m_ListData.get(0 ).idx;
			data.img = m_ListData.get(0 ).img;
			data.title = m_ListData.get(0 ).title;
			data.nick = m_ListData.get(0 ).nick;
			data.imgurl = m_ListData.get(0 ).imgurl;
			data.imgn = m_ListData.get(0 ).branddetail;
			data.count = 1;
			m_ListCoData.add(data);
		}
		else
		{
			int loopcount = (m_ListData.size()/2);
			if ( (m_ListData.size()%2) != 0 )
				loopcount =  (m_ListData.size()/2) + 1;
			
			for ( int i = 0 ; i < loopcount ; i++  )
			{
				
				
				SquareListCo data = new SquareListCo();

				data.type1 = m_ListData.get(i * 2 ).type;
				data.idx = m_ListData.get(i * 2 ).idx;
				data.img = m_ListData.get(i * 2 ).img;
				data.title = m_ListData.get(i * 2 ).title;
				data.nick = m_ListData.get(i * 2 ).nick;
				data.imgurl = m_ListData.get(i * 2 ).imgurl;
				data.imgn = m_ListData.get(i * 2 ).branddetail;
				

				// 마지막 줄의 데이터가 한개만 존재 할경우 검사
				
				// 두개 다 존재할경우 
				if ( (i== ((m_ListData.size()/2)-1) ) && (m_ListData.size()%2) == 0 )
				{
					data.type2 = m_ListData.get(i * 2  +1).type;
					data.idx2 = m_ListData.get(i * 2  +1).idx;
					data.img2 = m_ListData.get(i * 2 +1).img;
					data.title2 = m_ListData.get(i * 2 +1).title;
					data.nick2 = m_ListData.get(i * 2 +1 ).nick;
					data.imgurl2 = m_ListData.get(i * 2 + 1 ).imgurl;
					data.imgn2 = m_ListData.get(i * 2 + 1).branddetail;
					data.count = 2;
				}
				//하나만 존재할경우 
				else if ( (i== (loopcount-1) ) && (m_ListData.size()%2) == 1 )
				{
					data.count = 1;
				}
				else
				{
					data.type2 = m_ListData.get(i * 2  +1).type;
					data.idx2 = m_ListData.get(i * 2  +1).idx;
					data.img2 = m_ListData.get(i * 2 +1).img;
					data.title2 = m_ListData.get(i * 2 +1).title;
					data.nick2 = m_ListData.get(i * 2 +1 ).nick;
					data.imgurl2 = m_ListData.get(i * 2 + 1 ).imgurl;
					data.imgn2 = m_ListData.get(i * 2 + 1).branddetail;
					data.count = 2;
				}			
				m_ListCoData.add(data);
				
			}
		}

		
		m_Adapter.notifyDataSetChanged();
		
		mLockListView = false;  

	}
	
	public void GetAudiotionList()
	{
		mLockListView = true;
		{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					Map<String, String> data = new HashMap  <String, String>();


					
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mapp/Square", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{
							
							
							JSONObject magazine = (JSONObject)json.get("magazine");
							if ( magazine != null)
							{
								SquareListData item = new SquareListData();
								
								item.type = 0;
								
								item.title = (magazine.optString("title"));
								item.imgurl = _AppManager.DEF_URL +  "/mapp/ImageLoad?page=Common&idx="
											+  (magazine.optString("imgn"));
								
								m_ListData.add(item);
							}
							
							JSONObject event = (JSONObject)json.get("event");
							if ( event != null)
							{
								SquareListData item = new SquareListData();
								
								item.type = 1;
								
								item.title = (event.optString("title"));
								item.imgurl =  _AppManager.DEF_URL +  "/mapp/ImageLoad?page=Common&idx=" + 
										(event.optString("imgn"));
								
								m_ListData.add(item);
							}
							
							/*JSONObject mappclass = (JSONObject)json.get("class");
							if ( mappclass != null)
							{
								SquareListData item = new SquareListData();
								
								item.type = 2;
								
								item.title = (mappclass.optString("title"));
								item.imgurl =  _AppManager.DEF_URL +  "/mapp/ImageLoad?page=Common&idx=" + 
								(mappclass.optString("imgn"));
								
								m_ListData.add(item);
							}*/
							
							
							JSONObject network = (JSONObject)json.get("network");
							if ( network != null)
							{
								SquareListData item = new SquareListData();
								
								item.type = 3;
								
								item.title = (network.optString("title"));
								item.imgurl = _AppManager.DEF_URL +  "/mapp/ImageLoad?page=Common&idx=" + 
								(network.optString("imgn"));
								
								m_ListData.add(item);
							}
							
							
							JSONArray usageList = (JSONArray)json.get("brand");
							
							if ( usageList != null )
							{
								for(int i = 0; i < usageList.length(); i++)
								{
									SquareListData item = new SquareListData();
									JSONObject list = (JSONObject)usageList.get(i);
								
									item.type = 4;
									item.idx =  Integer.parseInt(list.optString("idx"));
									item.title = (list.optString("title"));
									
									//item.nick = URLDecoder.decode(list.optString("text"));
									
									//item.nick =  (list.optString("text"));
									item.nick = list.optString("text");
									
									item.imgurl = _AppManager.DEF_URL +  "/mapp/ImageLoad?page=Brand&idx=" + 
											item.idx;
									
									item.branddetail = new ArrayList<Integer>();
									
									JSONArray usageList1 = (JSONArray)list.get("img");
									
									for(int j = 0; j < usageList1.length(); j++)
									{
										Integer list2 = (int)usageList1.getInt(j);
										item.branddetail.add( Integer.parseInt(list2.toString()));
										
									}
									
									
									m_ListData.add(item);
								}
							}
							
							
							
							handler.sendEmptyMessage(0);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(1,_AppManager.GetError(json.optInt("errcode")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler.sendMessage(handler.obtainMessage(1,"Error" ));
						e.printStackTrace();
					} /*catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(12,"Error" ));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(13,"Error" ));
					} */
				}
			});
			thread.start();
		}
	}
	
	
	
	public void GetMoreAudiotionList()
	{
		m_Offset = m_ListData.get(m_ListData.size() -1 ).idx;
		m_ListData.clear();
		GetAudiotionList();
		
		
	}
	
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				RefreshUI();
				
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 2:
				
				break;
				
			case 3:
				GetAudiotionList();
				break;

				
			case 20:
				GetAudiotionList();
				
				break;
			default:
				break;
			}

		}
    	
	};
	public class AuditionRow_Adapter extends ArrayAdapter<SquareListCo>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<SquareListCo> mList;
		private LayoutInflater mInflater;
		
    	public AuditionRow_Adapter(Context context, int layoutResource, ArrayList<SquareListCo> mTweetList)
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
    		final SquareListCo mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				LinearLayout frameBar1 = (LinearLayout)convertView.findViewById(R.id.audition_row_1);
				LinearLayout frameBar2 = (LinearLayout)convertView.findViewById(R.id.audition_row_2);


				{
					frameBar1.setVisibility(View.VISIBLE);
					frameBar2.setVisibility(View.GONE);
					
					LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.audition_row_1_1);
					LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.audition_row_1_2);
					
					((TextView)convertView.findViewById(R.id.audition_row_1_text1)).setText("");
					((TextView)convertView.findViewById(R.id.audition_row_1_text2)).setText(mBar.title);
					
					((TextView)convertView.findViewById(R.id.audition_row_1_text2)).setTextColor(0xffffffff);
					
					ImageView Image = (ImageView)convertView.findViewById(R.id.audition_row_1_img);
					
					Image.setTag(mBar.imgurl);
					BitmapManager.INSTANCE.loadBitmap(mBar.imgurl, Image, 216, 136);

					
					detailBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
					
							switch ( mBar.type1)
							{
							case 0:
							{
								Intent intent;
				                intent = new Intent().setClass(self, SquareMagazineActivty.class);
				                startActivity( intent );
								
							}
								break;
							case 1:
							{
								Intent intent;
				                intent = new Intent().setClass(self, SquareEventActivity.class);
				                startActivity( intent );
								
							}
								
								
								break;
							case 2:
								break;
							case 3:
							{
								Intent intent;
				                intent = new Intent().setClass(self, SquareNetworkActivity.class);
				                startActivity( intent );
							}
								break;
							case 4:
							{
								_AppManager.m_SquareDetailString = mBar.nick;
								
								if (_AppManager.m_SquarePicture == null )
								{
									_AppManager.m_SquarePicture = new ArrayList<String>();
								}
								_AppManager.m_SquarePicture.clear();
								
								for ( int j= 0; j < mBar.imgn.size() ; j++ )
								{
									_AppManager.m_SquarePicture.add(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=BrandJacket&idx=" + 
											mBar.imgn.get(j));
								}
								
								_AppManager.m_SquareDetailTitle = mBar.title;
								
								Intent intent;
					               
				                intent = new Intent().setClass(self, SquareBrandActivity.class);
				                
				                
				                startActivity( intent );
							}
								break;
							}
						}
					});
					
		    		
		    		
		    		((ImageView)convertView.findViewById(R.id.audition_row_1_del)).setVisibility(View.GONE);
					if ( mBar.count == 1 )
					{
						
						
						detailBar2.setVisibility(View.VISIBLE);
						detailBar2.setBackgroundDrawable(null);
						((TextView)convertView.findViewById(R.id.audition_row_2_text1)).setVisibility(View.GONE);
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setVisibility(View.GONE);
						
						((TextView)convertView.findViewById(R.id.audition_row_2_text1)).setText("");
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setText("");
						((ImageView)convertView.findViewById(R.id.audition_row_2_img)).setBackgroundResource(R.drawable.audition_vacuum);
						((ImageView)convertView.findViewById(R.id.audition_row_2_img)).setVisibility(View.GONE);
						
						((ImageView)convertView.findViewById(R.id.audition_row_2_del)).setVisibility(View.GONE);
						
						detailBar2.setOnClickListener(new View.OnClickListener() 
						{

							public void onClick(View v) 
							{
						


							}
						});
					}
					else
					{
						detailBar2.setVisibility(View.VISIBLE);
						detailBar2.setBackgroundResource(R.drawable.audition_area);
						((TextView)convertView.findViewById(R.id.audition_row_2_text1)).setText("");
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setText(mBar.title2);
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setTextColor(0xffffffff);
						
						ImageView Image1 = (ImageView)convertView.findViewById(R.id.audition_row_2_img);
						((ImageView)convertView.findViewById(R.id.audition_row_2_img)).setVisibility(View.VISIBLE);
			    		
						Image1.setTag(mBar.imgurl2);
						BitmapManager.INSTANCE.loadBitmap(mBar.imgurl2, Image1, 216, 136);

			    		((ImageView)convertView.findViewById(R.id.audition_row_2_del)).setVisibility(View.GONE);
			    		
			    		
			    		detailBar2.setOnClickListener(new View.OnClickListener() 
						{

							public void onClick(View v) 
							{

								switch ( mBar.type2)
								{
								case 0:
								{
									Intent intent;
					                intent = new Intent().setClass(self, SquareMagazineActivty.class);
					                startActivity( intent );
									
								}
									break;
								case 1:
								{
									Intent intent;
					                intent = new Intent().setClass(self, SquareEventActivity.class);
					                startActivity( intent );
									
								}
									
									
									break;
								case 2:
									break;
								case 3:
								{
									Intent intent;
					                intent = new Intent().setClass(self, SquareNetworkActivity.class);
					                startActivity( intent );
									
								}
									break;
								case 4:
								{
									_AppManager.m_SquareDetailString = mBar.nick2;
									
									if (_AppManager.m_SquarePicture == null )
									{
										_AppManager.m_SquarePicture = new ArrayList<String>();
									}
									_AppManager.m_SquarePicture.clear();
									
									for ( int j= 0; j < mBar.imgn2.size() ; j++ )
									{
										_AppManager.m_SquarePicture.add(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=BrandJacket&idx=" + 
												mBar.imgn2.get(j));
									}
									
									_AppManager.m_SquareDetailTitle = mBar.title2;
									
									Intent intent;
						               
					                intent = new Intent().setClass(self, SquareBrandActivity.class);
					                
					                
					                startActivity( intent );
								}
									break;
								}
							}
						});
					}
				}
				


			}
			return convertView;
		}
    }
	
	

	
}

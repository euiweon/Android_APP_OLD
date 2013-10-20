package com.humapcontents.mapp;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.BitmapManager;
import com.humapcontents.mapp.AuditionActivity.AuditionRow_Adapter;
import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeData;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;

public class SquareMagazineActivty extends MappBaseActivity implements OnClickListener {

	private class AuditionListData
	{
		int  no;
		int  idx;
		Bitmap img;
		String title;
		String nick;
		String imgurl;
	}
	
	private class AuditionListAd
	{
		int  idx;
		String title;
		Bitmap img;
		String url;
	}
	
	private class AuditionListCo
	{
		int type;
		
		int count;
		int  no;
		int  idx;
		String imgurl;
		Bitmap img;
		String title;
		String nick;
		
		
		int  no2;
		int  idx2;
		String imgurl2;
		Bitmap img2;
		String title2;
		String nick2;
		
		String url;
	}

	protected static final int CAPTURE_MOVIE = 1000;
	
	private SquareMagazineActivty self;
	
	int m_SelectStageIndex = 0;
	
	private ListView m_ListView;
	
	
	ArrayList< AuditionListAd > 	m_ListADData;
	ArrayList< AuditionListCo > m_ListCoData;
	private AuditionRow_Adapter m_Adapter;
	
    Integer m_Offset  = -1; 
    Integer m_SortIndex = 0;
    Boolean m_Refresh = true;
    
    String[] m_Sort = {"all", "interview" , "star", "music", "soul","special" };
	
	
	private boolean mLockListView; 
	
	public ProgressDialog mProgress2;
	private LayoutInflater mInflater;
	
	private View m_Footer;
	
	public Boolean m_bFooter = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auditionlist_layout);  // 인트로 레이아웃 출력     

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
		
        ((TextView)findViewById(R.id.title_desc)).setText(m_Sort[m_SortIndex]);
        ((TextView)findViewById(R.id.title_desc)).setPaintFlags(((TextView)findViewById(R.id.title_desc)).getPaintFlags() 
        		| Paint.UNDERLINE_TEXT_FLAG);
        ImageResize(R.id.main_layout);
        ImageResize(R.id.audi_list_view);
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
        	m_ListView = ((ListView)findViewById(R.id.audi_list_view));
        	
        	m_ListView.setOnScrollListener(listen);
        	
        }
        
        {
        	
        	
        	FrameLayout imageview = (FrameLayout)findViewById(R.id.title_bar);
            imageview.setOnClickListener(this);
            ((TextView)findViewById(R.id.title_desc)).setOnClickListener(this);
            ((TextView)findViewById(R.id.title_name)).setOnClickListener(this);
        }

        
    	m_ListADData = new ArrayList< AuditionListAd >();
    	m_ListCoData = new ArrayList< AuditionListCo >();
    	
    	m_ListADData.clear();
    	m_ListCoData.clear();
    	
    	m_Adapter = new AuditionRow_Adapter(this, R.layout.audition_row, m_ListCoData);
    	
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Footer  = mInflater.inflate(R.layout.footer, null);
        m_ListView.addFooterView(m_Footer);
        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null); 
        
        
        
        {
        	((TextView)findViewById(R.id.title_name)).setText("뮤직 스퀘어");
        	
        	((ImageView)findViewById(R.id.title_icon)).setBackgroundResource(R.drawable.right_back_icon);
        	ImageView imageview = (ImageView)findViewById(R.id.title_icon2);
        	imageview.setVisibility(View.GONE);
        }
        BottomMenuDown(true);
        AfterCreate( 3 );
        
        ImageBtnEvent(R.id.title_icon , this);
        
        GetMagazineList();

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
		case R.id.title_icon:
		{
			onBackPressed();
		}
		break;
		case R.id.title_bar:
			break;
		case R.id.title_name:
		case R.id.title_desc:
			
		{
	        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	        
	        alt_bld.setTitle("Select a Category");
	        alt_bld.setSingleChoiceItems(m_Sort, -1, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) 
	            {
	            	
	            	
	            	m_ListCoData.clear();
	            	m_ListADData.clear();
	            	m_Offset  = -1; 
	            	m_SortIndex = item;
	            	((TextView)findViewById(R.id.title_desc)).setText(m_Sort[m_SortIndex]);
	            	
	            	m_Adapter.notifyDataSetChanged();
	            	
	            	GetMagazineList();
	                
	                
	                
	                // Top 30 때는 Footer 삭제
	                
	                if ( m_SortIndex == 2 )
	                {
	                	FooterHide();
	                }
	                dialog.cancel();
	                
	            }
	        });
	        AlertDialog alert = alt_bld.create();
	        alert.show();
		}
			
			break;
		}
		
	}
	
	public void RefreshUI()
	{
		if ( m_SortIndex != 2  && m_ListADData.size() > 19 )
		{
			FooterShow();
		}
		else
		{
			FooterHide();
		}
		int adcount = 0;
		int listcount = 0;
		
		// 데이터가 하나만 존재 할경우에 대한 예외 처리 

		
		
		for ( int i = 0; i < m_ListADData.size() ; i++ )
		{
			AuditionListCo data2 = new AuditionListCo();
			data2.type = 1; 
			data2.idx = m_ListADData.get(i).idx; 
			data2.img = m_ListADData.get(i).img; 
			data2.url = m_ListADData.get(i).url; 
			data2.title = m_ListADData.get(i).title;
			m_ListCoData.add(data2);
		}

		m_Adapter.notifyDataSetChanged();
		
		mLockListView = false;  

	}
	
	public void GetMagazineList()
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

					data.put("category", m_Sort[m_SortIndex]);
					data.put("offset", m_Offset.toString() );
					
					if ( m_SortIndex !=2 )
						data.put("range", "20" );
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Square/Magazine", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{
							
							JSONArray usageList = (JSONArray)json.get("magazine");
							
							for(int i = 0; i < usageList.length(); i++)
							{
								AuditionListAd item = new AuditionListAd();
								JSONObject list = (JSONObject)usageList.get(i);
								
								item.idx =  Integer.parseInt(list.optString("idx"));
								item.title = (list.optString("title"));
								item.url =  (list.optString("url"));

								m_ListADData.add(item);
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
					} 
				}
			});
			thread.start();
		}
	}
	
	
	
	
	public void GetMoreAudiotionList()
	{
		m_Offset = m_ListADData.get(m_ListADData.size() -1 ).idx -1;
		m_ListADData.clear();
		GetMagazineList();
		
		
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
				GetMagazineList();
				break;

				
			case 20:
				GetMagazineList();
				
				break;
			default:
				break;
			}

		}
    	
	};
	public class AuditionRow_Adapter extends ArrayAdapter<AuditionListCo>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<AuditionListCo> mList;
		private LayoutInflater mInflater;
		
    	public AuditionRow_Adapter(Context context, int layoutResource, ArrayList<AuditionListCo> mTweetList)
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
    		final AuditionListCo mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				LinearLayout frameBar1 = (LinearLayout)convertView.findViewById(R.id.audition_row_1);
				LinearLayout frameBar2 = (LinearLayout)convertView.findViewById(R.id.audition_row_2);
				switch( mBar.type )
				{
				case 0:
				{
					frameBar1.setVisibility(View.VISIBLE);
					frameBar2.setVisibility(View.GONE);
					
					LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.audition_row_1_1);
					LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.audition_row_1_2);
					
					((TextView)convertView.findViewById(R.id.audition_row_1_text1)).setText(mBar.title);
					((TextView)convertView.findViewById(R.id.audition_row_1_text2)).setText(mBar.nick);
					
					ImageView Image = (ImageView)convertView.findViewById(R.id.audition_row_1_img);
					
					Image.setTag(mBar.imgurl);
					BitmapManager.INSTANCE.loadBitmap(mBar.imgurl, Image, 216, 136);
					/*if ( Image != null && mBar.img != null )
		    		{
		    			Drawable d =new BitmapDrawable(getResources(),mBar.img);
		    			Image.setBackgroundDrawable( d );
		    		}*/
					
					detailBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
					
			            	AppManagement _AppManager = (AppManagement) getApplication();
			            	_AppManager.m_PublicIndex =mBar.idx;
			          
			            	Intent intent;
			               
			                intent = new Intent().setClass(self, AuditionDetailActivity.class);
			                
			                
			                startActivity( intent ); 

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
						((TextView)convertView.findViewById(R.id.audition_row_2_text1)).setText(mBar.title2);
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setText(mBar.nick2);
						
						ImageView Image1 = (ImageView)convertView.findViewById(R.id.audition_row_2_img);
						((ImageView)convertView.findViewById(R.id.audition_row_2_img)).setVisibility(View.VISIBLE);
			    		
						Image1.setTag(mBar.imgurl2);
						BitmapManager.INSTANCE.loadBitmap(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Magazine&idx=" + 
								mBar.idx, Image1, 216, 136);
			    		
			    		((ImageView)convertView.findViewById(R.id.audition_row_2_del)).setVisibility(View.GONE);
			    		
			    		
			    		detailBar2.setOnClickListener(new View.OnClickListener() 
						{

							public void onClick(View v) 
							{
						
				            	AppManagement _AppManager = (AppManagement) getApplication();
				            	_AppManager.m_PublicIndex =mBar.idx2;
				          
				            	Intent intent;
				               
				                intent = new Intent().setClass(self, AuditionDetailActivity.class);
				                
				                
				                startActivity( intent ); 

							}
						});
					}
					break;
				}
				
				case 1:
				{
					frameBar1.setVisibility(View.GONE);
					frameBar2.setVisibility(View.VISIBLE);
					((TextView)convertView.findViewById(R.id.audition_ad_text1)).setText("");
					((TextView)convertView.findViewById(R.id.audition_ad_text2)).setText(mBar.title);
					
					{
			    		ImageView Image = (ImageView)convertView.findViewById(R.id.audition_ad_img);
			    		
			    		
			    		
			    		Image.setTag(mBar.imgurl2);
						BitmapManager.INSTANCE.loadBitmap(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Magazine&idx=" + 
								mBar.idx, Image, 465, 136);
			    	}
					
					
					frameBar2.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							AppManagement _AppManager = (AppManagement) getApplication();
							_AppManager.m_SquareDetailTitle = mBar.title;
							_AppManager.m_SquareURL = mBar.url;
					
							Intent intent;
				               
			                intent = new Intent().setClass(self, SquareWebActivity.class);
			                
			                
			                startActivity( intent ); 
							
						}
					});
					
					break;
				}
				
				
				}
			}
			return convertView;
		}
    }
	

	
}

package com.humapcontents.mapp;



import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.humapcontents.mapp.AuditionDetailActivity.ReplyData;
import com.humapcontents.mapp.data.HomeWeekLyRank;

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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView.OnScrollListener;
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


public class FavoriteActivity extends MappBaseActivity implements OnClickListener {

	private class AuditionListData
	{
		int  no;
		int  idx;
		String title;
		String nick;
		String img;
		String type;
	}
	

	private class AuditionListCo
	{
		int type;
		
		int count;
		int  no;
		int  idx;
		String img;
		String title;
		String nick;
		String type_1;
		
		
		int  no2;
		int  idx2;
		String img2;
		String title2;
		String nick2;
		String type_2;
	}

	protected static final int CAPTURE_MOVIE = 1000;
	
	private FavoriteActivity self;
	
	int m_SelectStageIndex = 0;
	
	private ListView m_ListView;
	
	
	ArrayList< AuditionListData >	m_ListTrackData;
	ArrayList< AuditionListData >	m_ListAuditionData;
	ArrayList< AuditionListCo > m_ListCoData;
	private AuditionRow_Adapter m_Adapter;
	
    Integer m_Offset  = -1; 
    Integer m_SortIndex = 0;
    Boolean m_Delete = false;
    
    Integer m_LastBar = -1;
    
    
    Boolean m_DeleteType = false;
    

	

	private boolean mLockListView; 
	
	public ProgressDialog mProgress2;
	private LayoutInflater mInflater;
	
	private View m_Footer;
	
	public Boolean m_bFooter = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_layout);  // 인트로 레이아웃 출력     

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
        ImageResize(R.id.favorite_list_view);
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
	        		    	self.BottomMenuFullUp();
	        		    }
	        		    

     		   }
        	};
        	m_ListView = ((ListView)findViewById(R.id.favorite_list_view));
        	
        	m_ListView.setOnScrollListener(listen);
        	
        }
        


        m_ListTrackData = new ArrayList< AuditionListData >();
        m_ListAuditionData = new ArrayList< AuditionListData >();
    	m_ListCoData = new ArrayList< AuditionListCo >();
    	
    	m_ListCoData.clear();
    	m_ListAuditionData.clear();
    	m_ListTrackData.clear();
    	m_Adapter = new AuditionRow_Adapter(this, R.layout.favorite_row, m_ListCoData);
    	
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Footer  = mInflater.inflate(R.layout.footer, null);
        m_ListView.addFooterView(m_Footer);
        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null); 
        
        

		ImageBtnResize(R.id.title_icon2);
		
        BottomMenuFullUp();
        AfterCreate( 6 );
        
        FooterHide();
        
        ClearList();
        
        RefreshUI();
        

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


		case R.id.title_icon2:
		{
			m_Delete = !m_Delete;
			ImageView imageview = (ImageView)findViewById(R.id.title_icon2);
			
			if ( m_Delete == true)
			{
				imageview.setBackgroundResource(R.drawable.right_check_icon);
			}
			else
			{
				imageview.setBackgroundResource(R.drawable.right_edit_icon);
			}
			
			m_Adapter.notifyDataSetChanged();
		}
			break;
		}
		
	}
	
	public void TrackDelete( int index )
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		_AppManager.RemoveTrackData( index);
		ClearList();
		RefreshUI();
	}
	
	
	public void StageDelete( int index )
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		_AppManager.RemoveStageData( index);
		ClearList();
		RefreshUI();
	}
	
	public void ClearList()
	{
		m_ListCoData.clear();
    	m_ListAuditionData.clear();
    	m_ListTrackData.clear();
    	
    	FooterHide();
    	
    	m_Adapter.notifyDataSetChanged();
	}
	
	public void RefreshUI()
	{
		FooterHide();
		
		AppManagement _AppManager = (AppManagement) getApplication();
		{
			
			for ( int i  = 0 ; i < _AppManager.m_TrackData.size() ; i++ )
			{
				AuditionListData data = new AuditionListData();
				
				data.idx = _AppManager.m_TrackData.get(i).idx;
				data.nick = _AppManager.m_TrackData.get(i).nick;
				data.img = _AppManager.m_TrackData.get(i).img;
				data.title = _AppManager.m_TrackData.get(i).title;
				data.no = _AppManager.m_TrackData.get(i).no;
				data.type = _AppManager.m_TrackData.get(i).type;
				m_ListTrackData.add(data);
			}
			
			for ( int i  = 0 ; i < _AppManager.m_StageData.size() ; i++ )
			{
				AuditionListData data = new AuditionListData();
				
				data.idx = _AppManager.m_StageData.get(i).idx;
				data.nick = _AppManager.m_StageData.get(i).nick;
				data.img = _AppManager.m_StageData.get(i).img;
				data.title = _AppManager.m_StageData.get(i).title;
				data.no = _AppManager.m_StageData.get(i).no;
				data.type = _AppManager.m_StageData.get(i).type;
				m_ListAuditionData.add(data);
			}
			
		}
		
		
		
		if ( m_LastBar != 0  && m_ListAuditionData.size() != 0 )
		{
			AuditionListCo data = new AuditionListCo();
			data.type = 0; 
			m_LastBar = 0;
			m_ListCoData.add(data);
		}
		
		// 데이터가 하나만 존재 할경우에 대한 예외 처리 
		if ( m_ListAuditionData.size() == 1)
		{
			AuditionListCo data = new AuditionListCo();
			data.type = 2; 
			
			data.no = m_ListAuditionData.get(0 ).no;
			data.idx = m_ListAuditionData.get(0 ).idx;
			data.img = m_ListAuditionData.get(0 ).img;
			data.title = m_ListAuditionData.get(0 ).title;
			data.nick = m_ListAuditionData.get(0 ).nick;
			data.count = 1;
			m_ListCoData.add(data);
		}
		else
		{
			int loopcount = (m_ListAuditionData.size()/2);
			if ( (m_ListAuditionData.size()%2) != 0 )
				loopcount =  (m_ListAuditionData.size()/2) + 1;
			
			for ( int i = 0 ; i < loopcount ; i++  )
			{
				
				
				AuditionListCo data = new AuditionListCo();
				data.type = 2; 
				
				data.no = m_ListAuditionData.get(i * 2 ).no;
				data.idx = m_ListAuditionData.get(i * 2 ).idx;
				data.img = m_ListAuditionData.get(i * 2 ).img;
				data.title = m_ListAuditionData.get(i * 2 ).title;
				data.nick = m_ListAuditionData.get(i * 2 ).nick;
				

				// 마지막 줄의 데이터가 한개만 존재 할경우 검사
				
				// 두개 다 존재할경우 
				if ( (i== ((m_ListAuditionData.size()/2)-1) ) && (m_ListAuditionData.size()%2) == 0 )
				{
					data.no2 = m_ListAuditionData.get(i * 2  +1).no;
					data.idx2 = m_ListAuditionData.get(i * 2  +1).idx;
					data.img2 = m_ListAuditionData.get(i * 2 +1).img;
					data.title2 = m_ListAuditionData.get(i * 2 +1).title;
					data.nick2 = m_ListAuditionData.get(i * 2 +1 ).nick;
					data.count = 2;
				}
				//하나만 존재할경우 
				else if ( (i== (loopcount-1) ) && (m_ListAuditionData.size()%2) == 1 )
				{
					data.count = 1;
				}
				else
				{
					data.no2 = m_ListAuditionData.get(i * 2  +1).no;
					data.idx2 = m_ListAuditionData.get(i * 2  +1).idx;
					data.img2 = m_ListAuditionData.get(i * 2 +1).img;
					data.title2 = m_ListAuditionData.get(i * 2 +1).title;
					data.nick2 = m_ListAuditionData.get(i * 2 +1 ).nick;

					data.count = 2;
				}
				
				
				

				
				m_ListCoData.add(data);

				
				
				
			}
		}
		
		if ( m_LastBar != 1 && m_ListTrackData.size() != 0)
		{
			AuditionListCo data = new AuditionListCo();
			data.type = 1; 
			m_LastBar = 1;
			m_ListCoData.add(data);
		}
		
		{
			for ( int i = 0 ; i < m_ListTrackData.size() ; i++  )
			{
				
				
				AuditionListCo data = new AuditionListCo();
				data.type = 3; 
				
				data.no = m_ListTrackData.get(i  ).no;
				data.idx = m_ListTrackData.get(i  ).idx;
				data.img = m_ListTrackData.get(i  ).img;
				data.title = m_ListTrackData.get(i ).title;
				data.nick = m_ListTrackData.get(i  ).nick;
				
				m_ListCoData.add(data);
			}
		}

		m_ListTrackData.clear();
    	m_ListAuditionData.clear();
    	
		m_Adapter.notifyDataSetChanged();
		
		mLockListView = false;  

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
				LinearLayout frameBar2 = (LinearLayout)convertView.findViewById(R.id.album_row_1);
				
				LinearLayout frameBar3 = (LinearLayout)convertView.findViewById(R.id.stage_row_title);
				LinearLayout frameBar4 = (LinearLayout)convertView.findViewById(R.id.album_row_title);
				switch( mBar.type )
				{
				case 0:
				{
					frameBar1.setVisibility(View.GONE);
					frameBar2.setVisibility(View.GONE);
					frameBar3.setVisibility(View.VISIBLE);
					frameBar4.setVisibility(View.GONE);
				}
					break;
				case 1:
				{
					frameBar1.setVisibility(View.GONE);
					frameBar2.setVisibility(View.GONE);
					frameBar3.setVisibility(View.GONE);
					frameBar4.setVisibility(View.VISIBLE);
				}
					break;
					
				case 2:
				{
					frameBar1.setVisibility(View.VISIBLE);
					frameBar2.setVisibility(View.GONE);
					frameBar3.setVisibility(View.GONE);
					frameBar4.setVisibility(View.GONE);
					
					LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.audition_row_1_1);
					LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.audition_row_1_2);
					
					if ( self.m_Delete == true  )
						((ImageView)convertView.findViewById(R.id.audition_row_1_del)).setVisibility(View.VISIBLE);
					else
						((ImageView)convertView.findViewById(R.id.audition_row_1_del)).setVisibility(View.GONE);
						
					((ImageView)convertView.findViewById(R.id.audition_row_1_del)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.StageDelete(mBar.idx);
						}
					});
					
					((TextView)convertView.findViewById(R.id.audition_row_1_text1)).setText(mBar.title);
					((TextView)convertView.findViewById(R.id.audition_row_1_text2)).setText(mBar.nick);
					
					ImageView Image = (ImageView)convertView.findViewById(R.id.audition_row_1_img);
					
					Image.setTag(mBar.img);
					BitmapManager.INSTANCE.loadBitmap(mBar.img, Image, 216, 136);

					
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
					
		    		
		    		
		    		
					if ( mBar.count == 1 )
					{
						detailBar2.setVisibility(View.VISIBLE);
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
						
						((TextView)convertView.findViewById(R.id.audition_row_2_text1)).setText(mBar.title2);
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setText(mBar.nick2);
						
						ImageView Image1 = (ImageView)convertView.findViewById(R.id.audition_row_2_img);
						((ImageView)convertView.findViewById(R.id.audition_row_2_img)).setVisibility(View.VISIBLE);
			    		
						Image1.setTag(mBar.img2);
						BitmapManager.INSTANCE.loadBitmap(mBar.img2, Image1, 216, 136);

						
						if ( self.m_Delete == true  )
							((ImageView)convertView.findViewById(R.id.audition_row_2_del)).setVisibility(View.VISIBLE);
						else
							((ImageView)convertView.findViewById(R.id.audition_row_2_del)).setVisibility(View.GONE);
			    		
						
						((ImageView)convertView.findViewById(R.id.audition_row_2_del)).setOnClickListener(new View.OnClickListener() 
						{

							public void onClick(View v) 
							{
								self.StageDelete(mBar.idx2);
							}
						});
						
			    		
			    		
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
				
				case 3:
				{
					frameBar1.setVisibility(View.GONE);
					frameBar2.setVisibility(View.VISIBLE);
					frameBar3.setVisibility(View.GONE);
					frameBar4.setVisibility(View.GONE);
					
					LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.album_row_1);
					
					detailBar1.setVisibility(View.VISIBLE);

					if ( self.m_Delete == true  )
						((ImageView)convertView.findViewById(R.id.album_row_1_del)).setVisibility(View.VISIBLE);
					else
						((ImageView)convertView.findViewById(R.id.album_row_1_del)).setVisibility(View.GONE);
					
					
					((ImageView)convertView.findViewById(R.id.album_row_1_del)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.TrackDelete(mBar.idx);
						}
					});
					
					ImageView Image = (ImageView)convertView.findViewById(R.id.album_row_img);
					
					Image.setTag(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Track&idx="+ mBar.idx);
					BitmapManager.INSTANCE.loadBitmap(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Track&idx="+ mBar.idx, Image, 465, 287);
					
					((TextView)convertView.findViewById(R.id.album_row_text2)).setText(mBar.title);
					
					detailBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							_AppManager.m_AlbumTrackIndex = mBar.idx;
							
							_AppManager.m_AlbumTitle = mBar.title;
			            	_AppManager.m_AlbumDesc = mBar.nick;
							
							Intent intent;
				               
			                intent = new Intent().setClass(self, AlbumTrackActivity.class);
			                
			                
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

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class SquareNetworkDetailActivity extends MappBaseActivity implements OnClickListener {


	
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

	
	private SquareNetworkDetailActivity self;
	
	int m_SelectStageIndex = 0;
	
	private ListView m_ListView;
	
	
	ArrayList< SquareListData > 	m_ListADData;
	ArrayList< SquareListCo > m_ListCoData;
	private NeworkDetailAdapter m_Adapter;
	
    Integer m_Offset  = -1; 
    Integer m_SortIndex = 0;
    Boolean m_Refresh = true;
    
	
	
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
		
        ((TextView)findViewById(R.id.title_desc)).setText("HUMAP의 특별한날의 이벤트");

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

        
    	m_ListADData = new ArrayList< SquareListData >();
    	m_ListCoData = new ArrayList< SquareListCo >();
    	
    	m_ListADData.clear();
    	m_ListCoData.clear();
    	
    	m_Adapter = new NeworkDetailAdapter(this, R.layout.audition_row, m_ListCoData);
    	
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Footer  = mInflater.inflate(R.layout.footer, null);
        m_ListView.addFooterView(m_Footer);
        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null); 
        
        
        
        {
        	((TextView)findViewById(R.id.title_name)).setText("이벤트");
        	
        	((ImageView)findViewById(R.id.title_icon)).setBackgroundResource(R.drawable.right_back_icon);
        	ImageView imageview = (ImageView)findViewById(R.id.title_icon2);
        	imageview.setVisibility(View.GONE);
        }
        BottomMenuDown(true);
        AfterCreate( 3 );
        FooterHide();
        
        ImageBtnEvent(R.id.title_icon , this);
        
        RefreshUI();
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

		case R.id.title_icon:
		{
			onBackPressed();
		}
		break;
		}
		
	}
	
	public void RefreshUI()
	{

		
		// 데이터가 하나만 존재 할경우에 대한 예외 처리 
		AppManagement _AppManager = (AppManagement) getApplication();
		
		
		for ( int i = 0; i < _AppManager.m_SquareDetailNetworkData.size() ; i++ )
		{
			SquareListCo data2 = new SquareListCo();
			data2.idx = _AppManager.m_SquareDetailNetworkData.get(i).idx; 
			data2.nick = _AppManager.m_SquareDetailNetworkData.get(i).text; 
			data2.title = _AppManager.m_SquareDetailNetworkData.get(i).title;
			data2.imgn = _AppManager.m_SquareDetailNetworkData.get(i).imgn;

			m_ListCoData.add(data2);
		}

		m_Adapter.notifyDataSetChanged();
		
		mLockListView = false;  

	}

	public class NeworkDetailAdapter extends ArrayAdapter<SquareListCo>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<SquareListCo> mList;
		private LayoutInflater mInflater;
		
    	public NeworkDetailAdapter(Context context, int layoutResource, ArrayList<SquareListCo> mTweetList)
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
				frameBar1.setVisibility(View.GONE);
				frameBar2.setVisibility(View.VISIBLE);
				((TextView)convertView.findViewById(R.id.audition_ad_text1)).setText("");
				((TextView)convertView.findViewById(R.id.audition_ad_text2)).setText(mBar.title);
				
				{
		    		ImageView Image = (ImageView)convertView.findViewById(R.id.audition_ad_img);
		    		
		    		
		    		
		    		Image.setTag(mBar.imgurl2);
					BitmapManager.INSTANCE.loadBitmap(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Network&idx=" + 
							mBar.idx, Image, 465, 136);
		    	}
				
				
				frameBar2.setOnClickListener(new View.OnClickListener() 
				{

					public void onClick(View v) 
					{
						
						_AppManager.m_SquareDetailString = mBar.nick;
						
						if (_AppManager.m_SquarePicture == null )
						{
							_AppManager.m_SquarePicture = new ArrayList<String>();
						}
						_AppManager.m_SquarePicture.clear();
						
						for ( int j= 0; j < mBar.imgn.size() ; j++ )
						{
							_AppManager.m_SquarePicture.add(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=NetworkJacket&idx=" + 
									mBar.imgn.get(j));
						}
						
						_AppManager.m_SquareDetailTitle = mBar.title;
						
						Intent intent;
			               
		                intent = new Intent().setClass(self, SquareBrandActivity.class);
		                
		                
		                startActivity( intent );
		                
 
						
					}
				});
			}
			return convertView;
		}
    }
	
	

	
}

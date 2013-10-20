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

public class SquareClassActivity extends MappBaseActivity implements OnClickListener {

	private class AlbumData
	{
		int  idx;
		String title;
		String name;
		Bitmap img;
		String url;
	}
	private SquareClassActivity self;
	
	HomeData m_HomeData = new HomeData();
	
	Integer m_Offset  = -1; 
	int m_SelectStageIndex = 0;
	
	private ListView m_ListView;
	
	
	ArrayList< AlbumData > m_ListData;
	private AlbumRow_Adapter m_Adapter;
	
	private View m_Footer;
	
	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	
	private LayoutInflater mInflater;
	
	private boolean m_NotData = false;
	
	Gallery m_Gallery ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_layout);  // 인트로 레이아웃 출력     

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
	        		    		GetMoreAlbumList();
	        		    	}

	        		    		
	        		    } 
     		   }
        	};
        	m_ListView = ((ListView)findViewById(R.id.album_list_view));
        	
        	m_ListView.setOnScrollListener(listen);
        	
        }
        
        
        String token ="";

       
        
        
        m_ListData = new ArrayList< AlbumData >();
        m_ListData.clear();
    	m_Adapter = new AlbumRow_Adapter(this, R.layout.album_row, m_ListData);
    	
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Footer  = mInflater.inflate(R.layout.footer, null);
        m_ListView.addFooterView(m_Footer);
        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
		
        ImageResize(R.id.main_layout);
        ImageResize(R.id.album_list_view);
        
		BottomMenuDown(false);
        AfterCreate( 2 );
        

        ImageBtnEvent(R.id.title_icon , this);
        
		
		GetAlbumList();

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
		}
		
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
			case 2:

				break;
				
			case 3:
	
				break;
			case 10:
			{
				self.ShowAlertDialLog( self ,"에러" , "더이상 데이터가 없습니다" );
				FooterHide();
			}
				break;
			case 11:
			{
				RefreshUI();
				FooterHide();
			}
				break;
			case 20:
				break;
			default:
				break;
			}

		}
    	
	};
	
	
	public void RefreshUI()
	{

		m_Adapter.notifyDataSetChanged();
		
		mLockListView = false;  

	}
	
	
	public void GetAlbumList()
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

					
					data.put("offset", m_Offset.toString() );
					
					
					data.put("range", "20" );
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Album", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("error").equals("0"))
						{
							
							JSONArray usageList = (JSONArray)json.get("album");
							
							if ( usageList.length() == 0 )
							{
								handler.sendEmptyMessage(10);
							}
							for(int i = 0; i < usageList.length(); i++)
							{
								AlbumData item = new AlbumData();
								JSONObject list = (JSONObject)usageList.get(i);
								
								item.idx =  Integer.parseInt(list.optString("idx"));
								item.title = (list.optString("title"));
								item.name =  (list.optString("nick"));

								m_ListData.add(item);
							}
							
							if ( usageList.length() >= 20)
								handler.sendEmptyMessage(0);
							else
								handler.sendEmptyMessage(11);
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
	
	public void GetMoreAlbumList()
	{
		m_Offset = m_ListData.get(m_ListData.size() -1 ).idx -1;
		//m_ListData.clear();
		GetAlbumList();
	}
	
	
	public class AlbumRow_Adapter extends ArrayAdapter<AlbumData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<AlbumData> mList;
		private LayoutInflater mInflater;
		
    	public AlbumRow_Adapter(Context context, int layoutResource, ArrayList<AlbumData> mTweetList)
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
    		final AlbumData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				((TextView)convertView.findViewById(R.id.album_row_text1)).setText(mBar.title);
				((TextView)convertView.findViewById(R.id.album_row_text2)).setText(mBar.name);
				
				ImageView Image = (ImageView)convertView.findViewById(R.id.album_row_img);
				
				Image.setTag(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Album&idx="+ mBar.idx);
				BitmapManager.INSTANCE.loadBitmap(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Album&idx="+ mBar.idx, Image, 442, 442);
			}
			return convertView;
		}
    }
	

	
}

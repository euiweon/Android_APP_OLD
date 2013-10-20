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


public class SettingNoticeActivity extends MappBaseActivity implements OnClickListener {


	
	private class NoticeData
	{
		int  idx;
		int type;
		String title;
		String date;
		String text;
	}
	


	
	private SettingNoticeActivity self;
	

	private ListView m_ListView;
	
	

	ArrayList< NoticeData > 	m_NoticeList;
	private Notice_Adapter m_Adapter;
	

	public Integer m_SelectIndex =-1;
	public String m_SelectText ="";

	private boolean mLockListView; 
	
	public ProgressDialog mProgress2;
	private LayoutInflater mInflater;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_notice_layout);  // 인트로 레이아웃 출력     

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
        ImageResize(R.id.notice_list_view);
        {
        	
        	m_ListView = ((ListView)findViewById(R.id.notice_list_view));
        	
        	
        	
        }
        


        
    	m_NoticeList = new ArrayList< NoticeData >();
    	
    	
    	m_NoticeList.clear();
    	
    	m_Adapter = new Notice_Adapter(this, R.layout.notice_row, m_NoticeList);
    	
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        
    	m_ListView.setAdapter(m_Adapter);
		//m_ListView.setDivider(null); 
        
        
	
		
    	ImageBtnEvent(R.id.title_icon , this);
        
        
        {
        	NoticeData item = new NoticeData();
			
			item.idx =  -1;
			item.title = "S앨범 선정 기준";
			item.date = "2012-04-14 20:56:50.0";
			item.text = "S-LBUM 선정 기준 및 과정\n\n① HUMAP CONTENTS 의 제작 음원 공개 (미리듣기로 제공 약 30초)\n② 음원 공개 후 한달간, STAGE 를 통해, 인디 혹은 아마추어 아티스트들의 영상 접수 \n③ 한달 후, 가장 많은 추천수를 받은 VOCAL 파트 한명, 나머지 세션(기타,베이스,피아노 등) 파트 한명, 합 2명을 뽑아 S-ALBUM 제작에 들어감\n④ S-ALBUM 이 제작된 뮤지션은, 전주 MAPP STAR에서 소개될 예정\n\n\nMAPP STAR 선정 기준 및 과정\n\n① 한달간, 3명이 선발됨 (마지막 주는 다음달 S-LBUM 출시자)\n② 전 달, 화제가 되었던 인물 3명을 선정하여(HUMAP측 자체 기준에 따라), 일주일에 한명씩,  MAPP STAR에서 소개함.\n";
			
			item.type = 0;

			m_NoticeList.add(item);
        }
        
        BottomMenuDown(true);
        AfterCreate( 7 );
        
        GetNoticeList();
        
        ImageBtnEvent(R.id.title_icon , this);

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

		}
			
			break;
		}
		
	}
	
	public void RefreshUI()
	{

		m_Adapter.notifyDataSetChanged();
		
		mLockListView = false;  

	}
	
	

	
	public void GetNoticeList()
	{
		{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					Map<String, String> data = new HashMap  <String, String>();

					
					
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mapp/Notice", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{
							JSONArray usageList = (JSONArray)json.get("notice");
							
							for(int i = 0; i < usageList.length(); i++)
							{
								NoticeData item = new NoticeData();
								JSONObject list = (JSONObject)usageList.get(i);
								
								item.idx =  Integer.parseInt(list.optString("idx"));
								item.title = (list.optString("title"));
								item.date =  (list.optString("date"));
								item.type = 0;

								m_NoticeList.add(item);
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
	
	public void GetNoticeData()
	{
		{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			
			
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					if ( m_SelectIndex == -1 )
					{
						handler.sendEmptyMessage(20);
						return;
					}
					Map<String, String> data = new HashMap  <String, String>();

					data.put("idx", m_SelectIndex.toString());
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Notice/Detail", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{
							
							m_SelectText = (json.optString("text"));
							
							
							
							handler.sendEmptyMessage(20);
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
			case 20:
			{
				for ( int i = 0 ; i < m_NoticeList.size() ; i++ )
				{
					m_NoticeList.get(i ).type = 0;
					
					if ( m_SelectIndex != -1 && m_SelectIndex == m_NoticeList.get(i ).idx )
					{
						m_NoticeList.get(i ).type = 1;
						m_NoticeList.get(i ).text = m_SelectText;
					}
					else if ( m_SelectIndex == -1 )
					{
						m_NoticeList.get(i ).type = 1;
					}
				}
				
			
				m_Adapter.notifyDataSetChanged();
			}
				break;
				

			default:
				break;
			}

		}
    	
	};
	public class Notice_Adapter extends ArrayAdapter<NoticeData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<NoticeData> mList;
		private LayoutInflater mInflater;
		
    	public Notice_Adapter(Context context, int layoutResource, ArrayList<NoticeData> mTweetList)
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
    		final NoticeData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				LinearLayout frameBar1 = (LinearLayout)convertView.findViewById(R.id.notice_row_main);
				LinearLayout frameBar3 = (LinearLayout)convertView.findViewById(R.id.notice_row1);
				
				frameBar1.setVisibility(View.VISIBLE);
				
				switch( mBar.type )
				{
				case 0:
				{
					
					frameBar3.setVisibility(View.GONE);
					

					
					((TextView)convertView.findViewById(R.id.notice_row_title)).setText(mBar.title);
					((TextView)convertView.findViewById(R.id.notice_row_desc)).setText(mBar.date);
					frameBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							m_SelectIndex = mBar.idx;
							self.GetNoticeData();
						}
					});
					

					

				}
				break;
				
				case 1:
				{
					
					frameBar3.setVisibility(View.VISIBLE);
					((TextView)convertView.findViewById(R.id.notice_row_content)).setText(mBar.text);
					
					
					frameBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							mBar.type = 0;
							self.RefreshUI();
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

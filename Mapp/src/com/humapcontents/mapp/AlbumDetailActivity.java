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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.humapcontents.mapp.AlbumActivty.AlbumRow_Adapter;
import com.humapcontents.mapp.MappBaseActivity.ImageAdapter;
import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeData;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;

public class AlbumDetailActivity extends MappBaseActivity implements OnClickListener {

	private AlbumDetailActivity self;
	

	private class AlbumDetail
	{

		String title;
		
		String nick;
		String artist;
		String biography;
		String date;
		String genre;
		String type;
		String session;
		String creator;
		String words;
		String compose;
		String thanksto;
		String sponsor;
		String[] jacket2;
		Bitmap[] jacket;
	}
	
	private class AlbumTrack
	{
		int type;
		int  idx;
		String title;
	}
	
	int m_SelectIndex = 0;
	
	ArrayList<AlbumTrack> m_TrackData;
	AlbumDetail	m_AlbumData;
	
	

	private AlbumDetail_Adapter m_Adapter;
	private ListView m_ListView;
	Boolean m_DetailShow = false;
	
	Gallery m_Gallery ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_track_detail_layout);  // 인트로 레이아웃 출력     

        self = this;
        
        m_TrackData = new ArrayList<AlbumTrack>();
        
        m_AlbumData = new AlbumDetail();

     // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        

        m_ListView = ((ListView)findViewById(R.id.album_list_view));
        
        m_Adapter = new AlbumDetail_Adapter(this, R.layout.album_detail_row, m_TrackData);
        
        m_ListView.setDivider(null);
    	m_ListView.setAdapter(m_Adapter);
        
        
        m_Gallery = (Gallery)findViewById(R.id.album_detail_gallery);
        
        m_Gallery.setOnItemClickListener(new OnItemClickListener() 
        {
       	 
            public void onItemClick(AdapterView parent, View v, int position, long id) 
            {
            	AppManagement _AppManager = (AppManagement) getApplication();	
            	
            	_AppManager.m_AlbumTitle = m_AlbumData.title;
            	_AppManager.m_AlbumDesc = m_AlbumData.artist;
            	
            	_AppManager.m_Jacket = m_AlbumData.jacket2;
          
            	Intent intent;
               
                intent = new Intent().setClass(self, AlbumDetailTrackActivity.class);
                startActivity( intent ); 
            	
            }

        });
        
        m_Gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
        	 
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        	{
        		// 포커스가 간 View에 대한 정보를 얻어  이벤트 처리를 할 수 있다. 
        		
        		
        		m_SelectIndex = arg2;
        		
        		SelectPhoto();
        	}
        	 
        	public void onNothingSelected(AdapterView<?> arg0) 
        	{
        	}
        	  
        }
        );
        
        
        ImageResize(R.id.main_layout);
        ImageResize(R.id.album_detail_gallery);
        ImageResize(R.id.album_desc);
        ImageResize(R.id.detail_buttons);
        ImageBtnResize(R.id.album_plus_icon);
        ImageResize(R.id.album_list_view);
        ImageResize(R.id.album_line);
        
        
        
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	ImageView imageview = (ImageView)findViewById(R.id.title_icon);
           
            imageview.setOnClickListener(this);
        }
       
        BottomMenuDown(true);
        AfterCreate( 2 );
        
        
        GetAlbumData();
        
        
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
		case R.id.album_plus_icon:
		{
			m_DetailShow = !m_DetailShow;
			m_Adapter.notifyDataSetChanged();
			
			if ( m_DetailShow == true)
			{
				ImageView imageview = (ImageView)findViewById(R.id.album_plus_icon);
				
				imageview.setBackgroundResource(R.drawable.right_cancel_icon);
			}
			else
			{
				ImageView imageview = (ImageView)findViewById(R.id.album_plus_icon);
				
				imageview.setBackgroundResource(R.drawable.album_plus_bt);
			}
		}
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
		
		m_Adapter.notifyDataSetChanged();
		
		((TextView)findViewById(R.id.title_name)).setText(m_AlbumData.title);
		((TextView)findViewById(R.id.title_desc)).setText(m_AlbumData.artist);
		
		TextView textView = ((TextView)findViewById(R.id.album_desc));
		textView.setText("");
		
		
		
		final SpannableStringBuilder sps = new SpannableStringBuilder();
		
		{
			String TempString = "앨범명\n";
			SpannableString ss = new SpannableString(TempString);
			//sp.setSpan(new ForegroundColorSpan(Color.rgb(255, 255, 255)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);
		}
		
		{
			
			SpannableString ss = new SpannableString(m_AlbumData.title+ "\n");
			//sp.setSpan(new ForegroundColorSpan(Color.rgb(255, 255, 255)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);
		}
		
		{
			String TempString = "아티스트\n";
			SpannableString ss = new SpannableString(TempString);
			ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);
		}
		
		{
			SpannableString ss = new SpannableString(m_AlbumData.artist + "\n");
			
			ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.artist.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);
		}
		
		
		{
			String TempString = "발매일\n";
			
			SpannableString ss = new SpannableString(TempString);
			ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);
		}
		
		{
			SpannableString ss = new SpannableString(m_AlbumData.date + "\n");
			ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.date.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);
		}
		
		textView.setText(sps);
		
		
		
		
		m_Gallery.setAdapter(new ImageAdapter2( this, m_AlbumData.jacket2, m_Gallery.getWidth(), m_Gallery.getHeight() ));
		
		
		
		int count =  m_AlbumData.jacket2.length;
		
		if (  m_AlbumData.jacket2.length > 7 )
			count = 7;
		
		
		{
			((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_4)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_5)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_6)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_7)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_6_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_7_1)).setVisibility(View.GONE);
			
			switch( count )
			{
			case 0:
				break;
			case 1:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				break;
			case 2:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				
				break;
			case 3:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
				break;
			case 4:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.VISIBLE);
				break;
			case 5:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.VISIBLE);
				break;
			case 6:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_6)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_6_1)).setVisibility(View.VISIBLE);
				break;
			case 7:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_6)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_7)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_6_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_7_1)).setVisibility(View.VISIBLE);
				
				break;
			}
		}


	}
	
	
	public void GetAlbumData()
    {
    	{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					// ID와 패스워드를 가져온다 
					

					
					Map<String, String> data = new HashMap  <String, String>();
					data.put("idx", _AppManager.m_AlbumIndex.toString());

					
					String strJSON = _AppManager.GetHttpManager().PostHTTPData(_AppManager.DEF_URL +  "/mapp/Album/Detail", data);
					try
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{
							m_AlbumData.title = (json.optString("title"));
							m_AlbumData.nick = (json.optString("nick"));
							m_AlbumData.artist = (json.optString("artist"));
							m_AlbumData.biography = (json.optString("biography"));
							m_AlbumData.date = (json.optString("date"));
							m_AlbumData.genre = (json.optString("genre"));
							m_AlbumData.type = (json.optString("type"));
							m_AlbumData.session = (json.optString("session"));
							m_AlbumData.creator = (json.optString("creator"));
							m_AlbumData.words = (json.optString("words"));
							m_AlbumData.compose = (json.optString("compose"));
							m_AlbumData.thanksto = (json.optString("thanksto"));
							m_AlbumData.sponsor = (json.optString("sponsor"));
							
							
							
							
							
							JSONArray usageList3 = (JSONArray)json.get("jacket");
							
							if ( usageList3.length() != 0 )
							{
								m_AlbumData.jacket = new Bitmap[usageList3.length()];
								m_AlbumData.jacket2 = new String[usageList3.length()];
								
								for(int k = 0; k < usageList3.length(); k++)
								{
									m_AlbumData.jacket2[k] = _AppManager.DEF_URL +  "/mapp/ImageLoad?page=AlbumJacket&idx="+ 
											usageList3.getInt(k);
									/*URL imgUrl = new URL(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=AlbumJacket&idx="+ 
											usageList3.getInt(k) );
									URLConnection conn = imgUrl.openConnection();
									conn.connect();
									BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
									m_AlbumData.jacket[k] = BitmapFactory.decodeStream(bis);
									bis.close();*/
								}
							}
							
							
							
							{
								AlbumTrack item = new AlbumTrack();				
								item.type = 0;
								m_TrackData.add(item);
							}

							
							JSONArray usageList = (JSONArray)json.get("track");
							
							for(int i = 0; i < usageList.length(); i++)
							{
								AlbumTrack item = new AlbumTrack();
								JSONObject list = (JSONObject)usageList.get(i);
								
								item.type = 1;
								item.idx =  Integer.parseInt(list.optString("idx"));
								item.title = (list.optString("title"));

								m_TrackData.add(item);
							}
							
							handler.sendEmptyMessage(0);
						}
						
					}
					catch (JSONException e) 
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
	
	
	private void SelectPhoto()
    {
    	((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_6_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_7_1)).setVisibility(View.GONE);
    	
    	
    	switch( m_SelectIndex )
    	{
    	case 0:
    		((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
    		break;
    	case 1:
    		((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
    		break;
    	case 2:
    		((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
    		break;
    	case 3:
    		((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.VISIBLE);
    		break;
    	case 4:
    		((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.VISIBLE);
    		break;
    	case 5:
    		((ImageView)findViewById(R.id.square_btn_6_1)).setVisibility(View.VISIBLE);
    		break;
    	case 6:
    		((ImageView)findViewById(R.id.square_btn_7_1)).setVisibility(View.VISIBLE);
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
				RefreshUI();
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 2:

				break;
				
			case 3:
	
				break;
			case 20:
				break;
			default:
				break;
			}

		}
    	
	};
	
	
	public class AlbumDetail_Adapter extends ArrayAdapter<AlbumTrack>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<AlbumTrack> mList;
		private LayoutInflater mInflater;
		
    	public AlbumDetail_Adapter(Context context, int layoutResource, ArrayList<AlbumTrack> mTweetList)
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
    		final AlbumTrack mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				
				LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.album_row_1);
				
				LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.album_row_2);
				if( mBar.type == 0 )
				{
					detailBar1.setVisibility(View.GONE);
					
					if ( self.m_DetailShow == false )
					{
						detailBar2.setVisibility(View.GONE);
					}
					else
					{
						detailBar2.setVisibility(View.VISIBLE);
						
						TextView textView = ((TextView)convertView.findViewById(R.id.album_row2_text));
						textView.setText("");
						
						
						
						final SpannableStringBuilder sps = new SpannableStringBuilder();
						
						{
							String TempString = "Type : ";
							SpannableString ss = new SpannableString(TempString);
							//sp.setSpan(new ForegroundColorSpan(Color.rgb(255, 255, 255)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							
							SpannableString ss = new SpannableString(m_AlbumData.type+ "\n\n");
							//sp.setSpan(new ForegroundColorSpan(Color.rgb(255, 255, 255)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.type.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							String TempString = "Genre : ";
							SpannableString ss = new SpannableString(TempString);
							ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							SpannableString ss = new SpannableString(m_AlbumData.genre + "\n\n");
							
							ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.genre.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						
						{
							String TempString = "Biography : ";
							
							SpannableString ss = new SpannableString(TempString);
							ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							SpannableString ss = new SpannableString(m_AlbumData.biography + "\n\n");
							ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.biography.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							String TempString = "Compose : ";
							
							SpannableString ss = new SpannableString(TempString);
							ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							SpannableString ss = new SpannableString(m_AlbumData.compose + "\n\n");
							ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.compose.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						
						{
							String TempString = "Words : ";
							
							SpannableString ss = new SpannableString(TempString);
							ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							SpannableString ss = new SpannableString(m_AlbumData.words + "\n\n");
							ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.words.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							String TempString = "Session : ";
							
							SpannableString ss = new SpannableString(TempString);
							ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							SpannableString ss = new SpannableString(m_AlbumData.session + "\n\n");
							ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.session.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							String TempString = "Thanks to : ";
							
							SpannableString ss = new SpannableString(TempString);
							ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							SpannableString ss = new SpannableString(m_AlbumData.thanksto + "\n\n");
							ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.thanksto.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							String TempString = "Creator : ";
							
							SpannableString ss = new SpannableString(TempString);
							ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							SpannableString ss = new SpannableString(m_AlbumData.creator + "\n\n");
							ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.creator.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							String TempString = "Sponsor : ";
							
							SpannableString ss = new SpannableString(TempString);
							ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						{
							SpannableString ss = new SpannableString(m_AlbumData.sponsor + "\n\n");
							ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, m_AlbumData.sponsor.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							
							sps.append(ss);
						}
						
						
						textView.setText(sps);
						
						
					}
					
				}
				else
				{
					
					detailBar1.setVisibility(View.VISIBLE);
					detailBar2.setVisibility(View.GONE);
					
					ImageView Image = (ImageView)convertView.findViewById(R.id.album_row_img);
					
					Image.setTag(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Track&idx="+ mBar.idx);
					BitmapManager.INSTANCE.loadBitmap(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Track&idx="+ mBar.idx, Image, 465, 287);
					
					((TextView)convertView.findViewById(R.id.album_row_text2)).setText(mBar.title);
					
					detailBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							_AppManager.m_AlbumTrackIndex = mBar.idx;
							
							_AppManager.m_AlbumTitle = m_AlbumData.title;
			            	_AppManager.m_AlbumDesc = m_AlbumData.artist;
							
							Intent intent;
				               
			                intent = new Intent().setClass(self, AlbumTrackActivity.class);
			                
			                
			                startActivity( intent );
			                

						}
					});
					
				}
			}
			return convertView;
		}
    }

}

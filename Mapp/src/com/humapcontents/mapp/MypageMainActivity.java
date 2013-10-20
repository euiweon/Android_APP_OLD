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
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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


public class MypageMainActivity extends MappBaseActivity implements OnClickListener {

	private MypageMainActivity self;
	

	protected static final int CAPTURE_MOVIE = 1000;

	int m_SelectIndex = 0;
	

	private ListView m_ListView;
	Boolean m_DetailShow = false;
	

	
	Integer m_ImageWidth;
	Integer m_ImageHeight;

	
	ArrayList<UploadData> m_Data;
	
	
	public String nick;
	public String name;
	public String commment;
	public String location;
	public String age;
	
	

	ArrayList< AuditionListCo > m_ListCoData;
	private AuditionRow_Adapter m_Adapter;
	

	
	
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
	
	public class UploadData
	{
		Integer idx;
		String img;
		String video;
		String title;
		String time;
		String deletable;
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_my_layout);  // 인트로 레이아웃 출력     

        self = this;
        
       
        


     // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        GetImageSize();
        
        m_Data = new ArrayList<UploadData>();
        m_Data.clear();

        ImageResize(R.id.main_layout);
        ImageResize(R.id.album_detail_gallery);
        
        ImageResize(R.id.album_desc);
        
        ImageResize(R.id.album_line);
        
        ImageBtnResize(R.id.title_popup);

        
        ImageResize(R.id.mypage_list);
        ImageResize2(R.id.mypage_list_no_movie);
        
        ImageResize2(R.id.album_list_view);


        BottomMenuDown(true);
        AfterCreate( 6 );
        
        
        m_ListView = ((ListView)findViewById(R.id.album_list_view));
        
        
    	m_ListCoData = new ArrayList< AuditionListCo >();
    	
    	m_ListCoData.clear();
    	m_Adapter = new AuditionRow_Adapter(this, R.layout.mypage_row, m_ListCoData);
    	

        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null); 
        
       
        
    }
    
    
	public void GetMyData()
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
					data.put("id", _AppManager.m_LoginID);
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Mypage", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{

							
							nick =  (json.optString("nick"));
							name =  (json.optString("name"));
							commment =  (json.optString("comment"));
							location =  (json.optString("location"));
							age=  (json.optString("age"));
							
							JSONArray usageList = (JSONArray)json.get("upload");
							for(int i = 0; i < usageList.length(); i++)
							{
								UploadData item = new UploadData();
								
								JSONObject list = (JSONObject)usageList.get(i);
								
								item.idx =  Integer.parseInt(list.optString("idx"));
								item.title = (list.optString("title"));
								item.img = (list.optString("img"));
								item.video = (list.optString("video"));
								item.time = (list.optString("time"));
								item.deletable = (list.optString("deletable"));
								
								m_Data.add(item);
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
	

    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
    
    
    @Override
    public void  onResume()
    {
    	super.onResume();
    	m_ListCoData.clear();
    	m_Data.clear();
    	GetMyData();
    }
    
    



    

    


	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		
		switch(arg0.getId() )
		{
		case R.id.title_popup:
		{
			Intent intent;
            intent = new Intent().setClass(baseself, MypageEditActivity.class);
            startActivity( intent ); 
		}
			break;
			
		}
		
	}
	
	public void RefreshUI()
	{
		
		
		
		AppManagement _AppManager = (AppManagement) getApplication();
		m_ListCoData.clear();
		
		{
			// 데이터가 하나만 존재 할경우에 대한 예외 처리 
			if ( m_Data.size() == 1)
			{
				AuditionListCo data = new AuditionListCo();
				data.type = 0; 
				
				
				data.idx = m_Data.get(0 ).idx;
				data.title = m_Data.get(0 ).title;
				data.imgurl = m_Data.get(0 ).img;
				data.count = 1;
				m_ListCoData.add(data);
			}
			else
			{
				int loopcount = (m_Data.size()/2);
				if ( (m_Data.size()%2) != 0 )
					loopcount =  (m_Data.size()/2) + 1;
				
				for ( int i = 0 ; i < loopcount ; i++  )
				{
					
					
					AuditionListCo data = new AuditionListCo();
					data.type = 0; 
					
					data.idx = m_Data.get(i*2 ).idx;
					data.title = m_Data.get(i*2 ).title;
					data.imgurl = m_Data.get(i*2 ).img;
					

					// 마지막 줄의 데이터가 한개만 존재 할경우 검사
					
					// 두개 다 존재할경우 
					if ( (i== ((m_Data.size()/2)-1) ) && (m_Data.size()%2) == 0 )
					{
						data.idx2 = m_Data.get(i * 2  +1).idx;
						data.imgurl2 = m_Data.get(i * 2 +1).img;
						data.title2 = m_Data.get(i * 2 +1).title;
						data.count = 2;
					}
					//하나만 존재할경우 
					else if ( (i== (loopcount-1) ) && (m_Data.size()%2) == 1 )
					{
						data.count = 1;
					}
					else
					{
						data.idx2 = m_Data.get(i * 2  +1).idx;
						data.imgurl2 = m_Data.get(i * 2 +1).img;
						data.title2 = m_Data.get(i * 2 +1).title;
						data.count = 2;
					}
					
					
					

					
					m_ListCoData.add(data);

					
					
					
				}
			}
			
			{
				AuditionListCo data = new AuditionListCo();
				data.type = 1; 
				
				

				m_ListCoData.add(data);
			}

			
			m_Adapter.notifyDataSetChanged();

		}
	
		((TextView)findViewById(R.id.title_name)).setText("내 페이지");
		((TextView)findViewById(R.id.title_desc)).setText("");
		
		{
			ImageView Image = (ImageView)findViewById(R.id.album_detail_gallery);
			
			Image.setTag(_AppManager.DEF_URL + "ImageLoad?page=Profile&id=" +_AppManager.m_LoginID);
			BitmapManager.INSTANCE.loadBitmap(_AppManager.DEF_URL +  "ImageLoad?page=Profile&id=" +_AppManager.m_LoginID, Image, m_ImageWidth, m_ImageHeight);
		}
		
		final SpannableStringBuilder sps = new SpannableStringBuilder();
		
		{
			String TempString = "이름  \n";
			SpannableString ss = new SpannableString(TempString);
			//sp.setSpan(new ForegroundColorSpan(Color.rgb(255, 255, 255)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);
		}
		
		{
		
			if ( name == null )
			{
				name = "";
			}
			
			SpannableString ss = new SpannableString(name+ "\n");
			//sp.setSpan(new ForegroundColorSpan(Color.rgb(255, 255, 255)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);

		}
		
		{
			String TempString = "나이  \n";
			SpannableString ss = new SpannableString(TempString);
			ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);
		}
		
		{
			if ( age == null )
			{
				age = "";
			}
			SpannableString ss = new SpannableString(age + "\n");
			
			ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, age.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);
		}
		
		
		{
			String TempString = "지역 \n";
			
			SpannableString ss = new SpannableString(TempString);
			ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, TempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);
		}
		
		{
			if ( location == null )
			{
				location = "";
			}
			
			SpannableString ss = new SpannableString(location + "\n");
			ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, location.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			sps.append(ss);
		}
		
		TextView textView = ((TextView)findViewById(R.id.album_desc));
		textView.setText(sps);
		
		
		
		if ( m_Data.size() == 0 )
		{
			//m_ListView.setVisibility(View.GONE);
			((TextView)findViewById(R.id.mypage_list_no_movie)).setVisibility(View.VISIBLE);
		}
		else
		{
			//m_ListView.setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.mypage_list_no_movie)).setVisibility(View.GONE);
		}
		
		

	}
	
	public void GetImageSize(  )
	{
		
		View imageview = (View)findViewById(R.id.album_detail_gallery);
    	Display defaultDisplay = ((WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = imageview.getLayoutParams().height;
		int imagewidth = imageview.getLayoutParams().width;
		
		FrameLayout.LayoutParams margin = (FrameLayout.LayoutParams )(imageview.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		m_ImageWidth = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		m_ImageHeight = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
	}
	
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
         
        if (resultCode == RESULT_OK)
        {
            if (requestCode == CAPTURE_MOVIE)
            {
                Uri uri = intent.getData();
                String path = getPath(uri);
                String name = getName(uri);
                String uriId = getUriId(uri);
                self.ShowAlertDialLog( self ,"에러" , "아직 동영상을 바로 올리는 기능은 지원하지 않습니다.\n 유튜브에 먼저 동영상을 올려주세요." );
                Log.e("###", "실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId);
            }
        }
    }
     
 // 실제 경로 찾기
    private String getPath2(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
     
    // 파일명 찾기
    private String getName(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
     
    // uri 아이디 찾기
    private String getUriId(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns._ID };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
	
	
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
					
					
					
					((TextView)convertView.findViewById(R.id.audition_row_1_text1)).setText("");
					((TextView)convertView.findViewById(R.id.audition_row_1_text2)).setText(mBar.title);
					
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
						
						((TextView)convertView.findViewById(R.id.audition_row_2_text1)).setText("");
						
	
						
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setText(mBar.title2);
						
						ImageView Image1 = (ImageView)convertView.findViewById(R.id.audition_row_2_img);
						((ImageView)convertView.findViewById(R.id.audition_row_2_img)).setVisibility(View.VISIBLE);
			    		
						Image1.setTag(mBar.imgurl2);
						BitmapManager.INSTANCE.loadBitmap(mBar.imgurl2, Image1, 216, 136);
			    		/*if ( Image1 != null && mBar.img2 != null )
			    		{
			    			Drawable d =new BitmapDrawable(getResources(),mBar.img2);
			    			Image1.setBackgroundDrawable( d );
			    		}*/
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
					
					
					LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.mypage_album_1);
					LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.mypage_album_2);
					LinearLayout detailBar3 = (LinearLayout)convertView.findViewById(R.id.mypage_album_3);
					
					

					
					
					detailBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
					
							// 직접 촬영하기 


							/*Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		            	    startActivityForResult(intent, CAPTURE_MOVIE);*/
		            	    self.ShowAlertDialLog( self ,"에러" , "현재 이기능을 지원하지 않습니다. \n 유튜브에 먼저 동영상을 올려주세요." );

						}
					});
					
					detailBar2.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
					
							// 앨범에서 가져오기  

							self.ShowAlertDialLog( self ,"에러" , "현재 유튜브에 동영상을 바로 올리는 기능은 지원하지 않습니다.\n 유튜브에 먼저 동영상을 올려주세요." );
							
						}
					});
					
					detailBar3.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
					
							Intent intent;
			                intent = new Intent().setClass(self, UploadActivity.class);
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


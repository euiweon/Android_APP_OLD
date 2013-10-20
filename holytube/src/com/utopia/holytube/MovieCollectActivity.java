package com.utopia.holytube;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.BitmapManager;
import com.utopia.holytube.KingWayActivity.KingWay_List_Adapter;
import com.utopia.holytube.mainSingleton.TodayData;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MovieCollectActivity extends HolyTubeBaseActivity {
    /** Called when the activity is first created. */
	
	private ArrayList<TodayContentObject> m_ObjectArray;
	private KingWay_List_Adapter m_Adapter;
	private ListView m_ListView;
	
	MovieCollectActivity self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviecollect);
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주세요.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);

		
		m_ListView = (ListView)findViewById(R.id.kingway_listview);

		m_ObjectArray = new ArrayList<TodayContentObject>();
		m_Adapter = new KingWay_List_Adapter(this, R.layout.today_layout_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
		
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        // Tab
        {
            Button TabBTN2 = (Button)findViewById(R.id.today_tab_btn_1);
            TabBTN2.setOnClickListener(new Today_ClickListen(this));
            Button TabBTN3 = (Button)findViewById(R.id.today_tab_btn_2);
            TabBTN3.setOnClickListener(new Today_ClickListen(this));
            Button TabBTN4 = (Button)findViewById(R.id.today_tab_btn_3);
            TabBTN4.setOnClickListener(new Today_ClickListen(this));
            Button TabBTN5 = (Button)findViewById(R.id.today_tab_btn_4);
            TabBTN5.setOnClickListener(new Today_ClickListen(this));
        }
        
        {
        	 Button TabBTN2 = (Button)findViewById(R.id.homebutton);
             TabBTN2.setOnClickListener(new Today_ClickListen(this));
             
        	 Button TabBTN3 = (Button)findViewById(R.id.backbutton);
        	 TabBTN3.setOnClickListener(new Today_ClickListen(this));
        }
        AfterCreate();
        GetDataInfo();
    }
    
    public void GetDataInfo()
    {
    	m_ObjectArray.clear();
		final mainSingleton myApp = (mainSingleton) getApplication();

		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				myApp.todayData.clear();
				

				TodayData today = myApp.tempTodayData;

				String strJSON = myApp.MovieXMLString;
				
				strJSON = strJSON.replaceAll("\r", "");
				strJSON = strJSON.replaceAll("\n", "");
				
				XmlPullParserFactory xmlpf = null;
				try 
				{
					// XML 파서를 초기화

					myApp.movieCollectData.clear();
					xmlpf = XmlPullParserFactory.newInstance();
					XmlPullParser xmlp = xmlpf.newPullParser();
					xmlp.setInput(new StringReader(strJSON));
					// 파싱에 발생하는 event?를 처리하기 위한 메소드
		            int eventType = xmlp.getEventType();

		            
		            CURR_XML_STATE estate = CURR_XML_STATE.CURR_XML_UNKNOWN;
		            
		            // XML문서 읽기를 반복함미다.
		            while( eventType != XmlPullParser.END_DOCUMENT )
		            {

		                switch ( eventType ) 
		                {
		                case XmlPullParser.START_DOCUMENT: // 문서 시작 태그를 만난 경우
		                case XmlPullParser.END_DOCUMENT: // 문서 끝 태그를 만난 경우
		                    break;
		                case XmlPullParser.START_TAG: // 쌍으로 구성된 태그의 시작을 만난 경우
		                    // 요소명 체크
		                    if ( xmlp.getName().equals("tid") )
		                    { 
		                        estate = CURR_XML_STATE.CURR_XML_VERSION;
		                    }
		                    else if ( xmlp.getName().equals("mainimg")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_MANNIMG;

		                    }
		                    else if ( xmlp.getName().equals("title")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_TITLE;
		                    }
		                    else if ( xmlp.getName().equals("mid")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_MID;

		                    }
		                    else if ( xmlp.getName().equals("preacher")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_PREACHER;

		                    }
		                    else if ( xmlp.getName().equals("playtime")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_PLAYTIME;

		                    }
		                    else if ( xmlp.getName().equals("img")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_IMG;

		                    }
		                    else if ( xmlp.getName().equals("dataname")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_MOVIE;
		                    	
		                    }
		                    else if ( xmlp.getName().equals("datalength")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_DATALENG;
		                    }
		                    else
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_UNKNOWN;
		                    }

		                    break;
		                case XmlPullParser.END_TAG: // 쌍으로 구성된 태그의 끝을 만난 경우
		                	estate = CURR_XML_STATE.CURR_XML_UNKNOWN;
		                    break;
		                case XmlPullParser.TEXT: // TEXT로 이루어진 내용을 만난 경우
		                    switch ( estate )
		                    {
			                    case CURR_XML_VERSION:
			                    {
			                    	myApp.version = Integer.parseInt(xmlp.getText());
			                    }
			                    	break;
			                    case CURR_XML_MANNIMG:
			                    {
			                    	myApp.mainImage = xmlp.getText();
			                    }
			                    	break;
			                    case CURR_XML_MID:
			                    {
			                    	today.mid = Integer.parseInt(xmlp.getText());
			                    }
			                    	break;
			                    	
			                    case CURR_XML_TITLE:
			                    {
			                    	today.title = xmlp.getText();
			                    	
			                    }
			                    	break;
			                    case CURR_XML_PREACHER:
			                    {
			                    	today.preacher = xmlp.getText();
			                    }
			                    	
			                    	break;
			                    case CURR_XML_PLAYTIME:
			                    {
			                    	today.playtime = xmlp.getText();
			                    }
			                    	break;
			                    case CURR_XML_IMG:
			                    	today.img = xmlp.getText();
			                    	break;
			                    case CURR_XML_MOVIE:
			                    	today.movie = xmlp.getText();
			                    	Log.e("msg", today.title );
			                    	myApp.pushMovieData(today);
			                    	break;
			                    case CURR_XML_DATALENG:
			                    	today.datalength = xmlp.getText();
			                    	break;
		                    }

		                    break;
		                } // switch end
		                
		                // 다음 내용을 읽어옵니다
		                try
		                {
							eventType = xmlp.next();
						} 
		                catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                
		            } // while end
		            
		            handler.sendEmptyMessage(20);

		            

				} 
				catch (XmlPullParserException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(20);
				}
			}
		});
		thread.start();
    }
    
    
    
    public void RefreshUI()
    {
    	m_ObjectArray.clear();
    	if ( mProgress.isShowing() == true)
	   		return;
		mProgress.show();
		
		final mainSingleton myApp = (mainSingleton) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				File file1 = new File(Environment.getExternalStorageDirectory() +"/android/data/com.utopia.holytube/" ); 
				
				if( !file1.exists() ) // 원하는 경로에 폴더가 있는지 확인
				{
					handler.sendEmptyMessage(40);
					return;
				}
				else
				{
					
					for (File file : file1.listFiles())
					{
						
						// 기타 이미지
						for ( int i = 0 ; i < myApp.movieCollectData.size() ; i++ )
						{
							
							 String sFileName = file.getName();
							 int FileIdx = sFileName.lastIndexOf(".");
							 if ( FileIdx < 0 )
								 break;
							 String kk = sFileName.substring(0, FileIdx);
							 
							 
							 
							if ( kk.equals(myApp.movieCollectData.get(i).title))
							{
								TodayContentObject item = new TodayContentObject();
								item.isMain = false;
								
								/*URL imgUrl;
								try 
								{
									imgUrl = new URL( myApp.DEF_HOME_URL + myApp.movieCollectData.get(i).img);
									URLConnection conn = imgUrl.openConnection();
									conn.connect();
									BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
									Bitmap bm = BitmapFactory.decodeStream(bis);
									bis.close();
									item.img = bm;
									item.mid =  myApp.movieCollectData.get(i).mid;
									item.playtime = myApp.movieCollectData.get(i).playtime;
									item.title = myApp.movieCollectData.get(i).title;
									item.preacher = myApp.movieCollectData.get(i).preacher;
									item.movie = Environment.getExternalStorageDirectory() +"/android/data/com.utopia.holytube/" +sFileName;
									
									
								} catch (MalformedURLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}*/
								
								item.imgURL =  myApp.DEF_HOME_URL + myApp.movieCollectData.get(i).img;
								item.mid =  myApp.movieCollectData.get(i).mid;
								item.playtime = myApp.movieCollectData.get(i).playtime;
								item.title = myApp.movieCollectData.get(i).title;
								item.preacher = myApp.movieCollectData.get(i).preacher;
								item.movie = Environment.getExternalStorageDirectory() +"/android/data/com.utopia.holytube/" +sFileName;
								
								
								
								m_ObjectArray.add(item);
							}

							
						}
						
					}

				}
				
				

				
				handler.sendEmptyMessage(0);
				
			}
			
		});
		
		thread.start();
		
    }
    
    final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			String message = null;
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				m_Adapter.notifyDataSetChanged();
				break;
			case 20:
				RefreshUI();
				break;
			case 40:
				Toast.makeText(getBaseContext(), 
			  	           "파일이 없거나 지정된 경로가 틀립니다.", 
			  	           Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
    
    public  class Today_ClickListen implements OnClickListener
    {
    	MovieCollectActivity Parentactivity;
    	public Today_ClickListen( MovieCollectActivity activity)
    	{
    		Parentactivity = activity;
    	}
    	
    	public void onClick(View v )
        {
        	switch(v.getId())
        	{
	        	case R.id.today_tab_btn_1:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, TodayMovieActivity.class);
			        startActivity( intent );  
	        	}
	        		break;
	        		
	        	case R.id.today_tab_btn_3:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, PazzleDetailActivity.class);
			        startActivity( intent );   
	        	}
	        		break;
	        		
	        	case R.id.today_tab_btn_4:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, ContryActivity.class);
			        startActivity( intent );  
	        	}
	        		break;
	        		
	        	case R.id.today_tab_btn_2:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, KingWayActivity.class);
			        startActivity( intent );  
	        	}
	        		break;
	        	case R.id.homebutton:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, HolytubeActivity.class);
			        startActivity( intent ); 
	        	}
	        	break;
	        	case R.id.backbutton:
	        	{
	        		onBackPressed();
	        	}
	        	break;
	        	

        	}
        }
    }
    
    
    public class KingWay_List_Adapter extends ArrayAdapter<TodayContentObject>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<TodayContentObject> mList;
		private LayoutInflater mInflater;
		
    	public KingWay_List_Adapter(Context context, int layoutResource, ArrayList<TodayContentObject> mTweetList)
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
    		final TodayContentObject mBar = mList.get(position);
			final mainSingleton myApp = (mainSingleton) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}
			if(mBar != null) 
			{				
				if ( mBar.isMain == true)
				{
					LinearLayout frameBar1 = (LinearLayout)convertView.findViewById(R.id.todaylist_intro_1);
					LinearLayout frameBar2 = (LinearLayout)convertView.findViewById(R.id.todaylist_intro_2);
					
					RelativeLayout frameBar3 = (RelativeLayout)convertView.findViewById(R.id.todaylist_intro_3);
					frameBar3.setVisibility(View.GONE);
					
					
					frameBar1.setVisibility(View.GONE);
					frameBar2.setVisibility(View.VISIBLE);
					ImageView itemPic = (ImageView)convertView.findViewById(R.id.todaylist_intro_image);

					BitmapManager.INSTANCE.loadBitmap_3(mBar.imgURL, itemPic);
					itemPic.setScaleType(ImageView.ScaleType.FIT_XY);
					
				}
				else
				{
					LinearLayout frameBar1 = (LinearLayout)convertView.findViewById(R.id.todaylist_intro_1);
					LinearLayout frameBar2 = (LinearLayout)convertView.findViewById(R.id.todaylist_intro_2);
					
					RelativeLayout frameBar3 = (RelativeLayout)convertView.findViewById(R.id.todaylist_intro_3);
					frameBar3.setVisibility(View.GONE);
					
					
					frameBar1.setVisibility(View.VISIBLE);
					frameBar2.setVisibility(View.GONE);
					
					ImageView itemPic = (ImageView)convertView.findViewById(R.id.todaylist_main_image);
					TextView itemName = (TextView)convertView.findViewById(R.id.todaylist_intro_name);
					TextView itemComName = (TextView)convertView.findViewById(R.id.todaylist_intro_body);
					
					//itemPic.setImageBitmap(mBar.img);
					itemName.setText(mBar.title);
					itemComName.setText(mBar.playtime+"/" + mBar.preacher);
					
					

					BitmapManager.INSTANCE.loadBitmap_3(mBar.imgURL, itemPic);
					itemPic.setScaleType(ImageView.ScaleType.FIT_XY);

					


					itemPic.setScaleType(ImageView.ScaleType.FIT_XY);
					
					
					
					// 영상 다운로드...
					frameBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							// 파일이 있는지 체크해서 있으면 비디오뷰로 아니면 다운로드 받기...
							// 일단 무조건 다운로드로...
							{
					
								myApp.VideoPath = mBar.movie;
								Intent intent;
						        intent = new Intent().setClass(self, VideoPlay.class);
						        startActivity( intent ); 

							}
						}
					});
					
					
					frameBar1.setOnLongClickListener(new View.OnLongClickListener() 
					{


						public boolean onLongClick(View v) {

					    	new AlertDialog.Builder(self)
							 .setTitle("삭제 확인")
							 .setMessage("정말 삭제 하겠습니까?") //줄였음
							 .setPositiveButton("예", new DialogInterface.OnClickListener() 
							 {
							     public void onClick(DialogInterface dialog, int whichButton)
							     {   
							    	 {
											TodayData temp = null ; 
											

											if (new File(mBar.movie).delete())
											{
												 GetDataInfo();
												 
											}
												
										}
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
					    	
							
							return false;
						}
					});
					
					
				}
					
				
			}

			return convertView;
		}
    }
    
}
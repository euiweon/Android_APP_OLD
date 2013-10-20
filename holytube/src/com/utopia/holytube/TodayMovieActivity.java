package com.utopia.holytube;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.BitmapManager;
import com.utopia.holytube.mainSingleton.TodayData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TodayMovieActivity extends HolyTubeBaseActivity {
    /** Called when the activity is first created. */
	
	TodayMovieActivity self;
	
	private ArrayList<TodayContentObject> m_ObjectArray;
	private Today_List_Adapter m_Adapter;
	private ListView m_ListView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todaymovie);
        self  = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주세요.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		m_ListView = (ListView)findViewById(R.id.today_listview);

		m_ObjectArray = new ArrayList<TodayContentObject>();
		m_Adapter = new Today_List_Adapter(this, R.layout.today_layout_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        // Tab
        {
            Button TabBTN2 = (Button)findViewById(R.id.today_tab_btn_2);
            TabBTN2.setOnClickListener(new Today_ClickListen(this));
            Button TabBTN3 = (Button)findViewById(R.id.today_tab_btn_3);
            TabBTN3.setOnClickListener(new Today_ClickListen(this));
            Button TabBTN4 = (Button)findViewById(R.id.today_tab_btn_4);
            TabBTN4.setOnClickListener(new Today_ClickListen(this));
            Button TabBTN5 = (Button)findViewById(R.id.today_tab_btn_5);
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
		final mainSingleton myApp = (mainSingleton) getApplication();

		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				myApp.todayData.clear();
				

				TodayData today = myApp.tempTodayData;

				String strJSON = myApp.TodayXMLString;
				
				XmlPullParserFactory xmlpf = null;
				try 
				{
					// XML 파서를 초기화

					myApp.todayData.clear();
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
		                    
		                    else if ( xmlp.getName().equals("youtube")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_YOUTUBE;
		                    }
		                    
		                    
		                    else
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_UNKNOWN;
		                    }

		                case XmlPullParser.END_TAG: // 쌍으로 구성된 태그의 끝을 만난 경우
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
		                    	break;
		                    case CURR_XML_DATALENG:
		                    	today.datalength = xmlp.getText();
		                    	Log.e("msg", today.title );
		                    	myApp.pushTodayData(today);
		                    	break;
		                    case CURR_XML_YOUTUBE:
		                    	today.youtube = xmlp.getText();
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
				// TODO Auto-generated method stub
				
				// 메인 이미지 
				{
					
					TodayContentObject item = new TodayContentObject();
					item.isMain = true;
					
					URL imgUrl;
					/*try {
						imgUrl = new URL( myApp.DEF_HOME_URL + myApp.mainImage);
						URLConnection conn = imgUrl.openConnection();
						conn.connect();
						BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
						Bitmap bm = BitmapFactory.decodeStream(bis);
						bis.close();
						
						item.img = bm;
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					item.imgURL =  myApp.DEF_HOME_URL + myApp.mainImage;
					m_ObjectArray.add(item);
				}
				
				
				// 기타 이미지
				for ( int i = 0 ; i < myApp.todayData.size() ; i++ )
				{
					TodayContentObject item = new TodayContentObject();
					item.isMain = false;
					
					URL imgUrl;
					//try 
					{
						/*imgUrl = new URL(  myApp.DEF_HOME_URL +myApp.todayData.get(i).img);
						URLConnection conn = imgUrl.openConnection();
						conn.connect();
						BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
						Bitmap bm = BitmapFactory.decodeStream(bis);
						bis.close();*/
						item.imgURL = myApp.DEF_HOME_URL +myApp.todayData.get(i).img;
						//item.img = bm;
						item.imgName =  myApp.todayData.get(i).img;
						item.playtime = myApp.todayData.get(i).playtime;
						item.title = myApp.todayData.get(i).title;
						item.preacher = myApp.todayData.get(i).preacher;
						item.movie = myApp.todayData.get(i).movie;
						item.youtube = myApp.todayData.get(i).youtube;
						item.datalength = myApp.todayData.get(i).datalength;
						
					} /*catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					
					
					m_ObjectArray.add(item);
					
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
			case 201:
				self.ShowAlertDialLog(self, "추천", "추천 하셨습니다.");
				break;
			case 202:
				self.ShowAlertDialLog(self, "추천", "이미 추천 하셨습니다.");
				
				break;
			case 203:
				self.ShowAlertDialLog(self, "에러", "추천할 수 없습니다.");
				break;
			}
		}
	};
	public void RefreshUI2()
	{
		m_Adapter.notifyDataSetChanged();
	}
	public void SelectClear()
	{
		for ( int i = 0; i  < m_ObjectArray.size();  i++ )
		{
			m_ObjectArray.get(i).bVisible = false; 
		}
	}
    
    public  class Today_ClickListen implements OnClickListener
    {

    	TodayMovieActivity Parentactivity;
    	public Today_ClickListen( TodayMovieActivity activity)
    	{
    		Parentactivity = activity;
    	}
    	
    	public void onClick(View v )
        {
        	switch(v.getId())
        	{
	        	case R.id.today_tab_btn_2:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, KingWayActivity.class);
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
	        		
	        	case R.id.today_tab_btn_5:
	        	{
	        		Intent intent;
			        intent = new Intent().setClass(self, MovieCollectActivity.class);
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
    
    public class Today_List_Adapter extends ArrayAdapter<TodayContentObject>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<TodayContentObject> mList;
		private LayoutInflater mInflater;
		
    	public Today_List_Adapter(Context context, int layoutResource, ArrayList<TodayContentObject> mTweetList)
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
					
					
					Display defaultDisplay = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
					 
					int windowswidth = defaultDisplay.getWidth();

					
					/*if ( mBar.img.getWidth() > windowswidth )
					{
						itemPic.getLayoutParams().width = windowswidth;
						itemPic.getLayoutParams().height = (int) (( (float)(mBar.img.getHeight())/ (float)(mBar.img.getWidth()) ) * windowswidth);
						
						itemPic.setScaleType(ImageView.ScaleType.FIT_START);
					}
					else
					{
						itemPic.getLayoutParams().width = windowswidth;
						itemPic.getLayoutParams().height = (int) (( (float)(mBar.img.getHeight())/ (float)(mBar.img.getWidth()) ) * windowswidth);
						
						itemPic.setScaleType(ImageView.ScaleType.FIT_XY);
					}
					
										
					itemPic.setMaxHeight(mBar.img.getHeight());
					itemPic.setMaxWidth(mBar.img.getWidth());*/
					itemPic.setScaleType(ImageView.ScaleType.FIT_XY);

				}
				else
				{
					/*if ( mBar.bResize == false)
					{
						self.setResize( 1 , (ViewGroup)convertView, true );
						mBar.bResize =true;
					}*/
					
					
					LinearLayout frameBar1 = (LinearLayout)convertView.findViewById(R.id.todaylist_intro_1);
					LinearLayout frameBar2 = (LinearLayout)convertView.findViewById(R.id.todaylist_intro_2);
					
					RelativeLayout frameBar3 = (RelativeLayout)convertView.findViewById(R.id.todaylist_intro_3);
					
					frameBar1.setVisibility(View.VISIBLE);
					frameBar2.setVisibility(View.GONE);
					
					ImageView itemPic = (ImageView)convertView.findViewById(R.id.todaylist_main_image);
					TextView itemName = (TextView)convertView.findViewById(R.id.todaylist_intro_name);
					TextView itemComName = (TextView)convertView.findViewById(R.id.todaylist_intro_body);
					
					BitmapManager.INSTANCE.loadBitmap_2(mBar.imgURL, itemPic);
					
					//itemPic.setImageBitmap(mBar.img);
					itemName.setText(mBar.title);
					itemComName.setText(mBar.playtime+"/" + mBar.preacher);
					
					
					Display defaultDisplay = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
					 
					int windowswidth = defaultDisplay.getWidth();

					
					/*itemPic.getLayoutParams().width = 64;
					itemPic.getLayoutParams().height = 48;;*/
					itemPic.setScaleType(ImageView.ScaleType.FIT_XY);
					

					if ( mBar.bVisible)
					{
						((RelativeLayout)convertView.findViewById(R.id.todaylist_intro_3)).setVisibility(View.VISIBLE);
					}
					else
					{
						((RelativeLayout)convertView.findViewById(R.id.todaylist_intro_3)).setVisibility(View.GONE);
					}
					
					((LinearLayout)convertView.findViewById(R.id.todaylist_intro_1)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							
							
							if ( mBar.bVisible)
							{
								mBar.bVisible = false;
							}
							else
							{
								SelectClear();
								mBar.bVisible =true;
							}
							RefreshUI2();
						}
					});
					
					((ImageView)convertView.findViewById(R.id.play)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mBar.youtube));
				    		startActivity(intent);
							
						}
					});
					
					((ImageView)convertView.findViewById(R.id.like)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.Like(Integer.toString(mBar.mid), handler);
							
						}
					});
					
					((ImageView)convertView.findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							
							
						}
					});
					
					// 영상 다운로드...
					((ImageView)convertView.findViewById(R.id.down)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							// 파일이 있는지 체크해서 있으면 비디오뷰로 아니면 다운로드 받기...
							// 일단 무조건 다운로드로...
							{
					
						    	new AlertDialog.Builder(self)
								 .setTitle("다운로드 확인")
								 .setMessage(mBar.title +"를 다운로드 하시겠습니까?") //줄였음
								 .setPositiveButton("예", new DialogInterface.OnClickListener() 
								 {
								     public void onClick(DialogInterface dialog, int whichButton)
								     {   
									    	TodayData data = myApp.tempTodayData;
									    	data.mid = mBar.mid;
									    	data.img = mBar.imgName;
									    	data.playtime = mBar.playtime;
									    	data.datalength = mBar.datalength;
									    	data.title = mBar.title;
									    	data.preacher = mBar.preacher;
									    	myApp.pushMovieData(data);
									    	myApp.MovieXMLSave();
											myApp.startDownload(Uri.parse(myApp.DEF_HOME_URL + mBar.movie ), mBar.title , "/android/data/com.utopia.holytube/", mBar.title+".mp4", false);
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

							}
						}
					});
					
					
				}
			}
			return convertView;
		}
    }
}
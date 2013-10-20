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
import com.utopia.holytube.KingWayDetailActivity.Today_ClickListen;
import com.utopia.holytube.PazzleActivity.KingWay_List_Adapter;
import com.utopia.holytube.mainSingleton.TodayData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ContryActivity extends HolyTubeBaseActivity {
    /** Called when the activity is first created. */
	
	ContryActivity self;
	
	private ArrayList<TodayContentObject> m_ObjectArray;
	private KingWay_List_Adapter m_Adapter;
	private ListView m_ListView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contry);
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주세요.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		m_ListView = (ListView)findViewById(R.id.kingway_listview);

		m_ObjectArray = new ArrayList<TodayContentObject>();
		m_Adapter = new KingWay_List_Adapter(this, R.layout.common_layout_row, m_ObjectArray);
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
        RefreshUI();
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
				Map<String, String> data = new HashMap  <String, String>();
				
				//data.put("subidx", "0");
				//data.put("lastversion", "0");

				TodayData today = myApp.tempTodayData;

				//String strJSON = myApp.GetHTTPGetData(mainSingleton.DEF_HOME_URL + "/movie_group_s.asp", data);
				String strJSON = myApp.GetHTTPGetData(mainSingleton.DEF_HOME_URL + "/kog_large_Get.asp", data);
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
		                    else if ( xmlp.getName().equals("img")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_IMG;

		                    }
		                    else if ( xmlp.getName().equals("lgid")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_MID;

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
		                    case CURR_XML_MID:
		                    {
		                    	today.mid = Integer.parseInt(xmlp.getText());
		                    }
		                    	break;
		                    	
		                    case CURR_XML_IMG:
		                    	today.img = xmlp.getText();
		                    	myApp.pushTodayData(today);
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
		            
		            handler.sendEmptyMessage(0);

		            

				} 
				catch (XmlPullParserException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 기타 이미지
				for ( int i = 0 ; i < myApp.todayData.size() ; i++ )
				{
					TodayContentObject item = new TodayContentObject();
					item.isMain = false;
					
					/*URL imgUrl;
					try 
					{
						imgUrl = new URL( myApp.DEF_HOME_URL + myApp.todayData.get(i).img);
						URLConnection conn = imgUrl.openConnection();
						conn.connect();
						BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
						Bitmap bm = BitmapFactory.decodeStream(bis);
						bis.close();
						item.img = bm;						
						item.mid = myApp.todayData.get(i).mid;
						item.imgURL = myApp.DEF_HOME_URL + myApp.todayData.get(i).img;
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
										
					item.mid = myApp.todayData.get(i).mid;
					item.imgURL = myApp.DEF_HOME_URL + myApp.todayData.get(i).img;
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
			}
		}
	};
    
	
    
    public  class Today_ClickListen implements OnClickListener
    {
    	ContryActivity Parentactivity;
    	public Today_ClickListen( ContryActivity activity)
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
				ImageView itemPic = (ImageView)convertView.findViewById(R.id.common_main_image);					
				

				
				/*Display defaultDisplay = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
				 
				int windowswidth = defaultDisplay.getWidth();

				
				if ( mBar.img.getWidth() > windowswidth )
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
				}*/
				
				
				BitmapManager.INSTANCE.loadBitmap_3(mBar.imgURL, itemPic);
				itemPic.setScaleType(ImageView.ScaleType.FIT_XY);
			
				LinearLayout frameBar1 = (LinearLayout)convertView.findViewById(R.id.common_intro_1);
				
				frameBar1.setOnClickListener(new View.OnClickListener() 
				{

					public void onClick(View v) 
					{
						myApp.lgid = mBar.mid;
						
		        		Intent intent;
				        intent = new Intent().setClass(self, ContryMidActivity.class);
				        startActivity( intent );  
				        
					}
				});
			}
			return convertView;
		}
    }
    
    
}
package kr.co.rcsoft.mediatong;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.rcsoft.mediatong.RecruitFindActivity.RecruitDetail_List_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;

public class EventMainActivity extends BaseActivity implements OnClickListener{

	EventMainActivity self;
	
	Integer m_TabIndex = 0;

	private ArrayList<EventData> m_ObjectArray;
	private EventMain_List_Adapter m_Adapter;
	private ListView m_ListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout);  // 인트로 레이아웃 출력
        self = this;
        
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        m_ListView = (ListView)findViewById(R.id.event_detail_list_view);
        
		m_ObjectArray = new ArrayList<EventData>();
		m_Adapter = new EventMain_List_Adapter(this, R.layout.event_list_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
        
        // 전체 레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.event_main);
            _AppManager.GetUISizeConverter().ParentRelativeConvertLinearLayout(layout);
        }
        // 헤더레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.event_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
            _AppManager.GetUISizeConverter().ConvertLinearLayoutListView(m_ListView);
        }
        ImageBtnResize(R.id.event_back );
        ImageBtnResize2(R.id.event_now );
        ImageBtnResize2(R.id.event_result );
        
        TabBtnRefrash( );
        SetBtnEvent(R.id.event_bottom_home);
        SetBtnEvent(R.id.event_bottom_myjob);
        SetBtnEvent(R.id.event_bottom_scrap);
        SetBtnEvent(R.id.event_bottom_setting);
        
        
    }
    
	public void SetBtnEvent( int id )
	{
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
	}
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    public void ImageBtnResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    
    public void GetEventList()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();

    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();


					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getEventList.php", data);
					
					
					if ( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"서버에 문제가 있으므로 나중에 다시 시도하세요." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							int total_count = json.getInt("total_count");
							
							if ( total_count != 0 )
							{
								JSONArray usageList = (JSONArray)json.get("list");
								
								for(int i = 0; i < usageList.length(); i++)
								{
									EventData item = new EventData();
									
									JSONObject list = (JSONObject)usageList.get(i);
									
									if ( list != null )
									{
										item.bd_idx = Integer.parseInt(list.getString("bd_idx"));
										item.bd_title = _AppManager.GetHttpManager().DecodeString(list.getString("bd_title"));
										item.bd_contents = _AppManager.GetHttpManager().DecodeString(list.getString("bd_contents"));
										
										
										// 로고 이미지 
										if ( list.getString("bd_file") ==  null ||list.getString("bd_file").equals("") )
										{
											
										}
										else
										{
											URL imgUrl = new URL( _AppManager.GetHttpManager().DecodeString(list.getString("bd_file") ));
											URLConnection conn = imgUrl.openConnection();
											conn.connect();
											BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
											item.bd_file = BitmapFactory.decodeStream(bis);
											bis.close();									
										}
											
										m_ObjectArray.add(item);
										
									}
									else
									{
										handler.sendMessage(handler.obtainMessage(2,"이벤트 정보가 없습니다." ));
									}
								}
								handler.sendEmptyMessage(0);
								
							}
							else
							{
								handler.sendMessage(handler.obtainMessage(2,"이벤트 정보가 없습니다." ));
							}
						}
					}
					
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MalformedURLException e) {
						// TODO 자동 생성된 catch 블록
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 자동 생성된 catch 블록
						e.printStackTrace();
					}
					
				}
			});
			thread.start();
    	}
    }
    
    
    public void GetEventResultList()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();

    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();


					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getEventResultList.php", data);
					
					
					if ( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"서버에 문제가 있으므로 나중에 다시 시도하세요." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							int total_count = json.getInt("total_count");
							
							if ( total_count != 0 )
							{
								JSONArray usageList = (JSONArray)json.get("list");
								
								for(int i = 0; i < usageList.length(); i++)
								{
									EventData item = new EventData();
									
									JSONObject list = (JSONObject)usageList.get(i);
									
									if ( list != null )
									{
										item.bd_idx = Integer.parseInt(list.getString("bd_idx"));
										item.bd_title = _AppManager.GetHttpManager().DecodeString(list.getString("bd_title"));
										item.bd_contents = _AppManager.GetHttpManager().DecodeString(list.getString("bd_contents"));
										
										
										// 로고 이미지 
										if ( list.getString("bd_file") ==  null ||list.getString("bd_file").equals("") )
										{
											
										}
										else
										{
											URL imgUrl = new URL( _AppManager.GetHttpManager().DecodeString(list.getString("bd_file") ));
											URLConnection conn = imgUrl.openConnection();
											conn.connect();
											BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
											item.bd_file = BitmapFactory.decodeStream(bis);
											bis.close();									
										}
											
										m_ObjectArray.add(item);
									}
									else
									{
										handler.sendMessage(handler.obtainMessage(2,"이벤트 정보가 없습니다." ));
									}
								}
								handler.sendEmptyMessage(0);
								
							}
							else
							{
								handler.sendMessage(handler.obtainMessage(2,"이벤트 정보가 없습니다." ));
							}
						}
					}
					
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MalformedURLException e) {
						// TODO 자동 생성된 catch 블록
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 자동 생성된 catch 블록
						e.printStackTrace();
					}
					
				}
			});
			thread.start();
    	}
    }
    
    
    public void TabBtnRefrash( )
    {
		ImageBtnRefresh( R.id.event_now, R.drawable.m_app_014_btn02 );
		ImageBtnRefresh( R.id.event_result, R.drawable.m_app_014_btn04 );
		
		m_ObjectArray.clear();
		m_Adapter.notifyDataSetChanged();
		if ( m_TabIndex == 0 )
		{
			
			ImageBtnRefresh( R.id.event_now, R.drawable.m_app_014_btn01 );
			GetEventList();
		}
		else if ( m_TabIndex == 1 )
		{
			ImageBtnRefresh( R.id.event_result, R.drawable.m_app_014_btn03 );
			GetEventResultList();
		}

		
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
			case 1:
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
				

			case 8 :
				break;	
			default:
  				mProgress.dismiss();
  				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			}
		}
	};
	
	public class EventMain_List_Adapter extends ArrayAdapter<EventData>
	{

	  	private Context mContext;
	   	private int mResource;
	   	private ArrayList<EventData> mList;
	   	private LayoutInflater mInflater;
	    	
	   	public EventMain_List_Adapter(Context context, int layoutResource, 
	  			ArrayList<EventData> mTweetList)
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
	   		
	   		final EventData recruitData = mList.get(position);
	   		
	   		if(convertView == null)
	   		{
	   			convertView = mInflater.inflate(mResource, null);
	   		}
	   		
	   		
	   		if ( recruitData != null )
	   		{
	   			
	   			ImageView star1 = (ImageView)convertView.findViewById(R.id.event_row_1);
	   			
	   			if ( recruitData.bd_file != null )
	   			{
	   				AppManagement _AppManager = (AppManagement) getApplication();
	   		        _AppManager.GetUISizeConverter().ConvertLinearLayoutImage(star1); 
	   				star1.setImageBitmap( recruitData.bd_file );

	   			}
	   			

	   			LinearLayout linearbar = (LinearLayout)convertView.findViewById(R.id.event_row);
	   			linearbar.setOnClickListener(new View.OnClickListener() 
				{
					
						
					public void onClick(View v) 
					{
						final AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.m_EventTabIndex = 0;
						_AppManager.m_Eventdata = recruitData;
						Intent intent;
			            intent = new Intent().setClass(self, EventDetailActivity.class);
			            startActivity( intent );

					}
				});
	   			
	   		}
	   		
	   		return convertView;
	   	}
	}
	
	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		switch( arg0.getId())
		{
			case R.id.event_now:
			{
				if ( m_TabIndex != 0 )
				{
					m_TabIndex = 0;
					TabBtnRefrash();
				}
			}
				break;
			case R.id.event_result:
			{
				if ( m_TabIndex != 1 )
				{
					m_TabIndex = 1;
					TabBtnRefrash();
				}
			}
				break;
				
			case R.id.event_back:
			{
				onBackPressed();
			}
				break;
			case R.id.event_bottom_home:
			{
				Intent intent;
	            intent = new Intent().setClass(self, HomeActivity.class);
	            startActivity( intent ); 
			}
				break;
			case R.id.event_bottom_myjob:
			{
				final AppManagement _AppManager = (AppManagement) getApplication();
	    		if ( _AppManager.m_LoginCheck == true )
	    		{
	   			
	    			Intent intent;
	                intent = new Intent().setClass(self, MyJobListActivity.class);
	                startActivity( intent );

	    		}
	    		else
	    		{
	    			self.ShowAlertDialLog( self ,"로그인 에러" , "로그인을 해야 이 메뉴를 사용할수 있습니다." );
	    		}
			}
				break;
			case R.id.event_bottom_scrap:
			{
	    		final AppManagement _AppManager = (AppManagement) getApplication();
	    		if ( _AppManager.m_LoginCheck == true )
	    		{
	   			
	    			Intent intent;
	                intent = new Intent().setClass(self, ScrapMainActivity.class);
	                startActivity( intent );

	    		}
	    		else
	    		{
	    			self.ShowAlertDialLog( self ,"로그인 에러" , "로그인을 해야 이 메뉴를 사용할수 있습니다." );
	    		}
			}
				break;
			case R.id.event_bottom_setting:
			{
				Intent intent;
	            intent = new Intent().setClass(self, SettingActivity.class);
	            startActivity( intent ); 
			}
				break;
		}
	}
}
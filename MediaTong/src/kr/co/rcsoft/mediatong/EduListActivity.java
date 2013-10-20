package kr.co.rcsoft.mediatong;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.euiweonjeong.base.BaseActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;



public class EduListActivity extends BaseActivity implements OnClickListener{

	private ArrayList<EduData> m_ObjectArray;
	private EduDetail_List_Adapter m_Adapter;
	EduListActivity self;
	private ListView m_ListView;
	

	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        
        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.edu_list_layout, null); 
        this.setContentView(viewToLoad); 
        
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
        
		
		m_ListView = (ListView)findViewById(R.id.edu_detail_list_view);

		m_ObjectArray = new ArrayList<EduData>();
		m_Adapter = new EduDetail_List_Adapter(this, R.layout.edu_list_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
     // 전체 레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.edu_main);
            _AppManager.GetUISizeConverter().ParentRelativeConvertLinearLayout(layout);
        }
        // 헤더레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.edu_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
            _AppManager.GetUISizeConverter().ConvertLinearLayoutListView(m_ListView);
        }
        ImageBtnResize(R.id.edu_back );
        ImageBtnResize2(R.id.edu_detail_search );
        //GetArea1();
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText box = (EditText)findViewById(R.id.edu_find);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutEditText(box);
        }
        GeteduFindList();
        
        SetBtnEvent(R.id.edu_bottom_home);
        SetBtnEvent(R.id.edu_bottom_myjob);
        SetBtnEvent(R.id.edu_bottom_scrap);
        SetBtnEvent(R.id.edu_bottom_setting);
        
        
    }
    
	public void SetBtnEvent( int id )
	{
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
	}
    
    
    public void GeteduFindList()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();

    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();


					EditText SearchBox = (EditText)findViewById(R.id.edu_find);
					String search = SearchBox.getText().toString();
					
					data.put("stx",search);

					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getEduListBySearch.php", data);
					
					
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
									EduData item = new EduData();
									
									JSONObject list = (JSONObject)usageList.get(i);
									
									item.hr_idx = Integer.parseInt(list.getString("hr_idx"));
									
									item.hr_days = _AppManager.GetHttpManager().DecodeString(list.getString("hr_days"));
									item.hr_degree = _AppManager.GetHttpManager().DecodeString(list.getString("hr_degree"));
									item.hr_day_type = _AppManager.GetHttpManager().DecodeString(list.getString("hr_day_type"));
									item.hr_title = _AppManager.GetHttpManager().DecodeString(list.getString("hr_title"));
									item.mb_com_name = _AppManager.GetHttpManager().DecodeString(list.getString("mb_com_name"));
									item.hr_type = _AppManager.GetHttpManager().DecodeString(list.getString("hr_type"));
									
									
									m_ObjectArray.add(item);
								}
								handler.sendEmptyMessage(0);
							}
							else
							{
								handler.sendEmptyMessage(3);
							}
						}
					}
					
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			thread.start();
    	}
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


	public void onClick(View v) {
		// TODO 자동 생성된 메소드 스텁
		
		AppManagement _AppManager = (AppManagement) getApplication();
		switch(v.getId())
    	{
			case R.id.edu_back:
			{
				onBackPressed();
			}
				break;
			case R.id.edu_detail_search:
			{
				m_ObjectArray.clear();
				m_Adapter.notifyDataSetChanged();
				GeteduFindList();
			}
				break;
				
				
			case R.id.edu_bottom_home:
			{
				Intent intent;
	            intent = new Intent().setClass(self, HomeActivity.class);
	            startActivity( intent ); 
			}
				break;
			case R.id.edu_bottom_myjob:
			{
			
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
			case R.id.edu_bottom_scrap:
			{
	    		
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
			case R.id.edu_bottom_setting:
			{
				Intent intent;
	            intent = new Intent().setClass(self, SettingActivity.class);
	            startActivity( intent ); 
			}
				break;

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
				// 더보기 버튼...

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
				
			case 3:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , "등록된 교육정보가 없습니다." );
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
		
    
	
	public class EduDetail_List_Adapter extends ArrayAdapter<EduData>
	{

	  	private Context mContext;
	   	private int mResource;
	   	private ArrayList<EduData> mList;
	   	private LayoutInflater mInflater;
	    	
	   	public EduDetail_List_Adapter(Context context, int layoutResource, 
	  			ArrayList<EduData> mTweetList)
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
	   		
	   		final EduData eduData = mList.get(position);
	   		
	   		if(convertView == null)
	   		{
	   			convertView = mInflater.inflate(mResource, null);
	   		}
	   		
	   		
	   		if ( eduData != null )
	   		{
	   			TextView itemName = (TextView)convertView.findViewById(R.id.edu_detail_list_row_name);
				TextView itemTitle = (TextView)convertView.findViewById(R.id.edu_detail_list_row_title);
				TextView itemDesc = (TextView)convertView.findViewById(R.id.edu_detail_list_row_desc);
				
	   			LinearLayout linearbar = (LinearLayout)convertView.findViewById(R.id.edu_detail_list_row);
	   			
	   			
	   			itemName.setText(eduData.mb_com_name  );
	   			itemTitle.setText(eduData.hr_title);
	   			itemDesc.setText("마감일  " + eduData.hr_days+" | "  +  eduData.hr_degree + " | " +  eduData.hr_type + " | " + eduData.hr_day_type );
	   			
	   			
	   			linearbar.setOnClickListener(new View.OnClickListener() 
				{
					
						
					public void onClick(View v) 
					{
						// 다음 액티비티로 이동하고 보여줄 채용정보를 저장한다. 
						 AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.m_CompanyIndex = eduData.hr_idx;
						
						// 일단 문제 있어 막아둠. 
						Intent intent;
			            intent = new Intent().setClass(self, EduDetailActivity.class);
			            startActivity( intent );

					}
				});
	   			
	   		}
	   		
	   		return convertView;
	   	}
	}
}
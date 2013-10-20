package kr.co.rcsoft.mediatong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.rcsoft.mediatong.RecruitFindActivity.RecruitDetail_List_Adapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

import com.euiweonjeong.base.BaseActivity;

public class MyJobListActivity extends BaseActivity implements OnClickListener{
	
	private ArrayList<RecruitData> m_ObjectArray;
	private MyJobList_Adapter m_Adapter;
	MyJobListActivity self;
	private ListView m_ListView;
	
	String [] items1 = {"최신순" , "마감일순" };
	int sortIndex = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.myjob_layout, null); 
        this.setContentView(viewToLoad);
        
        self = this;
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
        
		
		m_ListView = (ListView)findViewById(R.id.myjob_detail_list_view);

		m_ObjectArray = new ArrayList<RecruitData>();
		m_Adapter = new MyJobList_Adapter(this, R.layout.recruit_detail_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
		// 전체 레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.myjob_main);
            _AppManager.GetUISizeConverter().ParentRelativeConvertLinearLayout(layout);
        }
        // 헤더레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.myjob_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
            _AppManager.GetUISizeConverter().ConvertLinearLayoutListView(m_ListView);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText box = (EditText)findViewById(R.id.myjob_find);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutEditText(box);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	Spinner box = (Spinner)findViewById(R.id.myjob_detail_spinner1);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutSpinner(box);
        }
        
        ImageBtnResize(R.id.myjob_back );
        ImageBtnResize(R.id.myjob_set );
        
        ImageBtnResize2(R.id.myjob_detail_search );
        //GetArea1();
        SetBtnEvent(R.id.myjob_bottom_home);
        SetBtnEvent(R.id.myjob_bottom_myjob);
        SetBtnEvent(R.id.myjob_bottom_scrap);
        SetBtnEvent(R.id.myjob_bottom_setting);
        
        InputSpinnerItem();
    }
    
    
    
    public void SetBtnEvent( int id )
	{
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
	}
	public void SpinnerResize(int id )
	{
		AppManagement _AppManager = (AppManagement) getApplication();
    	Spinner box = (Spinner)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutSpinner(box);
	}
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    public void ImageResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        
    }
    
    public void ImageBtnResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    public void ImageResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutImage(imageview); 
        
    }

	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		AppManagement _AppManager = (AppManagement) getApplication();
		switch( arg0.getId())
		{
		case R.id.myjob_back:
		{
			onBackPressed();
		}
			break;
		case R.id.myjob_set :
		{
			Intent intent;
            intent = new Intent().setClass(self, MyJobSettingActivity.class);
            startActivity( intent );
		}

			break;
		case R.id.myjob_detail_search:
		{
			m_ObjectArray.clear();
			m_Adapter.notifyDataSetChanged();
			
			// 리스트 가져오기 
			
			GetMyjobList();
		}
			break;
		case R.id.myjob_bottom_home:
		{
			Intent intent;
            intent = new Intent().setClass(self, HomeActivity.class);
            startActivity( intent ); 
		}
			break;
		case R.id.myjob_bottom_myjob:
		{
			

		}
			break;
		case R.id.myjob_bottom_scrap:
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
		case R.id.myjob_bottom_setting:
		{
			Intent intent;
            intent = new Intent().setClass(self, SettingActivity.class);
            startActivity( intent ); 
		}
			break;
		}
	}
    
	public void GetMyjobList()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();

    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();


					EditText SearchBox = (EditText)findViewById(R.id.myjob_find);
					String search = SearchBox.getText().toString();
					
					if ( sortIndex == 0 )
					{
						data.put("order_by","new");
						
					}
					else
					{
						data.put("order_by","end");
					}
					
					data.put("stx",search);

					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getHireListByMyJob.php", data);
					
					
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
									RecruitData item = new RecruitData();
									
									JSONObject list = (JSONObject)usageList.get(i);
									
									item.hr_idx = Integer.parseInt(list.getString("hr_idx"));
									item.hr_carrier_type = _AppManager.GetHttpManager().DecodeString(list.getString("hr_carrier_type"));
									item.hr_days = _AppManager.GetHttpManager().DecodeString(list.getString("hr_days"));
									item.hr_degree = _AppManager.GetHttpManager().DecodeString(list.getString("hr_degree"));
									item.hr_area = _AppManager.GetHttpManager().DecodeString(list.getString("hr_area"));
									item.hr_title = _AppManager.GetHttpManager().DecodeString(list.getString("hr_title"));
									item.mb_com_name = _AppManager.GetHttpManager().DecodeString(list.getString("mb_com_name"));
									
									if ( list.getString("is_new").equals("0"))
									{
										item.is_new = false; 
									}
									else
									{
										item.is_new = true;
									}
									
									
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
						
						handler.sendMessage(handler.obtainMessage(2,"정상적이지 않은 데이터가 넘어왔습니다." ));
					}
					
				}
			});
			thread.start();
    	}
    }
	
	 public void InputSpinnerItem()
	 {
	       

	    	
	        final Spinner spin = (Spinner)findViewById(R.id.myjob_detail_spinner1);
	        
	        spin.setPrompt("정렬 방법을 선택하세요.");
	        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, items1);
	        
	        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spin.setAdapter(aa);

	        spin.setOnItemSelectedListener(new OnItemSelectedListener()
	        {           
	        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	        	{       
	        		AppManagement _AppManager = (AppManagement) getApplication();
	        		_AppManager.m_SelectScrapType = position;
	        		m_ObjectArray.clear();
	    			m_Adapter.notifyDataSetChanged();
	    			
	    			sortIndex = position;

	    			GetMyjobList();

	        	}            
	        	public void onNothingSelected(AdapterView<?> parent) 
	        	{               
	        		// TODO Auto-generated method stub          
	        	}
	     
	        });
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
					message = "해당하는 데이터가 없습니다. ";
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
			
	 
	
	
	
	public class MyJobList_Adapter extends ArrayAdapter<RecruitData>
	{

	  	private Context mContext;
	   	private int mResource;
	   	private ArrayList<RecruitData> mList;
	   	private LayoutInflater mInflater;
	    	
	   	public MyJobList_Adapter(Context context, int layoutResource, 
	  			ArrayList<RecruitData> mTweetList)
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
	   		
	   		final RecruitData recruitData = mList.get(position);
	   		
	   		if(convertView == null)
	   		{
	   			convertView = mInflater.inflate(mResource, null);
	   		}
	   		
	   		
	   		if ( recruitData != null )
	   		{
	   			TextView itemName = (TextView)convertView.findViewById(R.id.recruit_detail_list_row_name);
				TextView itemTitle = (TextView)convertView.findViewById(R.id.recruit_detail_list_row_title);
				TextView itemDesc = (TextView)convertView.findViewById(R.id.recruit_detail_list_row_desc);
				
	   			LinearLayout linearbar = (LinearLayout)convertView.findViewById(R.id.recruit_detail_list_row);
	   			
	   			
	   			if ( !recruitData.is_new)
	   			{
	   				((ImageView)convertView.findViewById(R.id.recruit_detail_list_new)).setVisibility(View.GONE);
	   			}
	   			else
	   			{
	   				((ImageView)convertView.findViewById(R.id.recruit_detail_list_new)).setVisibility(View.VISIBLE);
	   			}
	   			itemName.setText(recruitData.mb_com_name  );
	   			itemTitle.setText(recruitData.hr_title);
	   			itemDesc.setText("마감일  " + recruitData.hr_days+" | " + recruitData.hr_carrier_type + " | " +  recruitData.hr_degree + " | " + recruitData.hr_area );
	   			
	   			
	   			linearbar.setOnClickListener(new View.OnClickListener() 
				{
					
						
					public void onClick(View v) 
					{
						// 다음 액티비티로 이동하고 보여줄 채용정보를 저장한다. 
						 AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.m_CompanyIndex = recruitData.hr_idx;
						
						// 일단 문제 있어 막아둠. 
						Intent intent;
			            intent = new Intent().setClass(self, RecruitMoreDetailActivity.class);
			            startActivity( intent );

					}
				});
	   			
	   		}
	   		
	   		return convertView;
	   	}
	}
    
   
    

    
}
package kr.co.rcsoft.mediatong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.rcsoft.mediatong.RecruitDetailActivity.RecruitDetail_List_Adapter;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.euiweonjeong.base.BaseActivity;

public class ScrapMainActivity extends BaseActivity implements OnClickListener{

	Integer selectSort;
	
	private ArrayList<ScrapData> m_ObjectArray;
	private Scrap_List_Adapter m_Adapter;
	ScrapMainActivity self;
	private ListView m_ListView;

	Boolean m_ResumeCheck = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.scrap_layout, null); 
        this.setContentView(viewToLoad);
        
        self = this;
        
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
        
		m_ListView = (ListView)findViewById(R.id.scrap_detail_list_view);

		m_ObjectArray = new ArrayList<ScrapData>();
		m_Adapter = new Scrap_List_Adapter(this, R.layout.scrap_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
     // 전체 레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.scrap_main);
            _AppManager.GetUISizeConverter().ParentRelativeConvertLinearLayout(layout);
        }
        // 헤더레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.scrap_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
            _AppManager.GetUISizeConverter().ConvertLinearLayoutListView(m_ListView);
        }
        {
        	
        	AppManagement _AppManager = (AppManagement) getApplication();
        	Spinner box = (Spinner)findViewById(R.id.scrap_detail_spinner1);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutSpinner(box);
        	
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText box = (EditText)findViewById(R.id.scrap_find);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutEditText(box);
        }
        ImageBtnResize(R.id.scrap_back );
        ImageBtnResize(R.id.scrap_delete );
        ImageBtnResize2(R.id.scrap_detail_search );
        

        InputSpinnerItem();
        SetBtnEvent(R.id.scrap_bottom_home);
        SetBtnEvent(R.id.scrap_bottom_myjob);
        SetBtnEvent(R.id.scrap_bottom_scrap);
        SetBtnEvent(R.id.scrap_bottom_setting);
        
        
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	// 데이터를 삭제 하고 왔을경우 갱신이 필요하기 이에 대한 처리를 한다. 
    	
    	
    	if ( m_ResumeCheck == false )
    	{
        	m_ObjectArray.clear();
    		m_Adapter.notifyDataSetChanged();
    		
        	AppManagement _AppManager = (AppManagement) getApplication();
        	if ( _AppManager.m_SelectScrapType == 0 ) 
    		{
        		
    			GetRecruitScrapFindList();
    		}
    		else
    		{
    			GetEduFindScrapList();
    		}
    	}
    	m_ResumeCheck = true;

    	
    	
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
    
    
    
    public void InputSpinnerItem()
    {
        // 스피너 처리 
    	String[] items = new  String[2];
    	items[0] = "채용정보";
    	items[1] = "교육정보";
    	
        Spinner spin = (Spinner)findViewById(R.id.scrap_detail_spinner1);
        
        spin.setPrompt("보실 정보를 선택하세요");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{   
        		
            		AppManagement _AppManager = (AppManagement) getApplication();
            		_AppManager.m_SelectScrapType = position;
            		EditText SearchBox = (EditText)findViewById(R.id.scrap_find);
            		SearchBox.setText("");
            		m_ObjectArray.clear();
        			m_Adapter.notifyDataSetChanged();
            		if ( position == 0 )
            		{
            			GetRecruitScrapFindList();
            		}
            		else
            		{
            			GetEduFindScrapList();
            		}
        		

        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}
     
        });
    }

    
    public void GetRecruitScrapFindList()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();

    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					EditText SearchBox = (EditText)findViewById(R.id.scrap_find);
					String search = SearchBox.getText().toString();
					
					data.put("stx",search);
					

					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getHireListByScrap.php", data);
					
					
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
									ScrapData item = new ScrapData();
									
									JSONObject list = (JSONObject)usageList.get(i);
									
									item.hr_idx = Integer.parseInt(list.getString("hr_idx"));
									item.hr_carrier_type = _AppManager.GetHttpManager().DecodeString(list.getString("hr_carrier_type"));
									item.hr_days = _AppManager.GetHttpManager().DecodeString(list.getString("hr_days"));
									item.hr_degree = _AppManager.GetHttpManager().DecodeString(list.getString("hr_degree"));
									item.hr_area = _AppManager.GetHttpManager().DecodeString(list.getString("hr_area"));
									item.hr_title = _AppManager.GetHttpManager().DecodeString(list.getString("hr_title"));
									item.mb_com_name = _AppManager.GetHttpManager().DecodeString(list.getString("mb_com_name"));
									
									m_ObjectArray.add(item);
								}
								handler.sendEmptyMessage(0);
							}
							else
							{
								handler.sendEmptyMessage(1);
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
    
    
    public void GetEduFindScrapList()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();

    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();


					EditText SearchBox = (EditText)findViewById(R.id.scrap_find);
					String search = SearchBox.getText().toString();
					
					data.put("stx",search);

					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getEduListByScrap.php", data);
					
					
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
									ScrapData item = new ScrapData();
									
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
								handler.sendEmptyMessage(1);
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
    

	public void onClick(View v) {
		// TODO 자동 생성된 메소드 스텁
		AppManagement _AppManager = (AppManagement) getApplication();
		switch(v.getId())
		{
		case R.id.scrap_back:
			onBackPressed();
			
			break;
		case R.id.scrap_detail_search:
		{
			m_ObjectArray.clear();
    		m_Adapter.notifyDataSetChanged();
    		
			if ( _AppManager.m_SelectScrapType == 0 ) 
			{
				GetRecruitScrapFindList();
			}
			else
			{
				GetEduFindScrapList();
			}
		}
		break;
		case R.id.scrap_delete:
		{
			self.m_ResumeCheck = false;
			
			Intent intent;
            intent = new Intent().setClass(self, ScrapDeleteActivity.class);
            startActivity( intent );
			
			
		}
			break;
		case R.id.scrap_bottom_home:
		{
			Intent intent;
	        intent = new Intent().setClass(self, HomeActivity.class);
	        startActivity( intent ); 
		}
			break;
		case R.id.scrap_bottom_myjob:
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
		case R.id.scrap_bottom_scrap:
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
		case R.id.scrap_bottom_setting:
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

				m_Adapter.notifyDataSetChanged();
				break;
			case 1:
				self.ShowAlertDialLog( self ,"No Data" , "검색 결과가 없습니다. " );
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
		
	
	
	public class Scrap_List_Adapter extends ArrayAdapter<ScrapData>
	{

	  	private Context mContext;
	   	private int mResource;
	   	private ArrayList<ScrapData> mList;
	   	private LayoutInflater mInflater;
	    	
	   	public Scrap_List_Adapter(Context context, int layoutResource, 
	  			ArrayList<ScrapData> mTweetList)
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
	   		
	   		final ScrapData recruitData = mList.get(position);
	   		
	   		if(convertView == null)
	   		{
	   			convertView = mInflater.inflate(mResource, null);
	   		}
	   		
	   		
	   		if ( recruitData != null )
	   		{

	   			AppManagement _AppManager = (AppManagement) getApplication();
	   			if ( _AppManager.m_SelectScrapType == 0 ) 
	   			{
		   			TextView itemName = (TextView)convertView.findViewById(R.id.scrap_1__row_name);
					TextView itemTitle = (TextView)convertView.findViewById(R.id.scrap_1__row_title);
					TextView itemDesc = (TextView)convertView.findViewById(R.id.scrap_1__row_desc);
					
		   			LinearLayout linearbar = (LinearLayout)convertView.findViewById(R.id.scrap_1__row);
		   			
		   			
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
							/*Intent intent;
				            intent = new Intent().setClass(self, RecruitMoreDetailActivity.class);
				            startActivity( intent );*/

						}
					});
		   			
	   			}
	   			else
	   			{
		   			TextView itemName = (TextView)convertView.findViewById(R.id.scrap_1__row_name);
					TextView itemTitle = (TextView)convertView.findViewById(R.id.scrap_1__row_title);
					TextView itemDesc = (TextView)convertView.findViewById(R.id.scrap_1__row_desc);
					
		   			LinearLayout linearbar = (LinearLayout)convertView.findViewById(R.id.scrap_1__row);
		   			
		   			
		   			itemName.setText(recruitData.mb_com_name  );
		   			itemTitle.setText(recruitData.hr_title);
		   			itemDesc.setText("마감일  " + recruitData.hr_days+" | "  +  recruitData.hr_degree + " | " +  recruitData.hr_type + " | " + recruitData.hr_day_type );
		   			
		   			linearbar.setOnClickListener(new View.OnClickListener() 
					{
						
							
						public void onClick(View v) 
						{
							// 다음 액티비티로 이동하고 보여줄 채용정보를 저장한다. 
							 AppManagement _AppManager = (AppManagement) getApplication();
							_AppManager.m_CompanyIndex = recruitData.hr_idx;
							
							// 일단 문제 있어 막아둠. 
							Intent intent;
				            intent = new Intent().setClass(self, EduDetailActivity.class);
				            startActivity( intent );

						}
					});
		   			
	   			}
	   			
	   		}
	   		
	   		return convertView;
	   	}
	}
    
}
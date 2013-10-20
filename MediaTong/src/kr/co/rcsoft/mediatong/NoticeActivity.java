package kr.co.rcsoft.mediatong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.rcsoft.mediatong.ScrapMainActivity.Scrap_List_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.euiweonjeong.base.BaseActivity;

public class NoticeActivity extends BaseActivity implements OnClickListener{

	String selectSort;
	
	private ArrayList<NoticeData> m_ObjectArray;
	private Notice_List_Adapter m_Adapter;
	private ListView m_ListView;
	
	
	String [] items1;
	
	NoticeActivity self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.notice_layout, null); 
        this.setContentView(viewToLoad);
        
        self = this;
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
        
		m_ListView = (ListView)findViewById(R.id.notice_detail_list_view);

		m_ObjectArray = new ArrayList<NoticeData>();
		m_Adapter = new Notice_List_Adapter(this, R.layout.notice_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
     // 전체 레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.notice_main);
            _AppManager.GetUISizeConverter().ParentRelativeConvertLinearLayout(layout);
        }
        // 헤더레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.notice_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
            _AppManager.GetUISizeConverter().ConvertLinearLayoutListView(m_ListView);
        }
        {
        	
        	AppManagement _AppManager = (AppManagement) getApplication();
        	Spinner box = (Spinner)findViewById(R.id.notice_detail_spinner1);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutSpinner(box);
        	
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText box = (EditText)findViewById(R.id.notice_find);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutEditText(box);
        }
        ImageBtnResize(R.id.notice_back );
        ImageBtnResize2(R.id.notice_detail_search );
        
        GetCategoryList();
        SetBtnEvent(R.id.notice_bottom_home);
        SetBtnEvent(R.id.notice_bottom_myjob);
        SetBtnEvent(R.id.notice_bottom_scrap);
        SetBtnEvent(R.id.notice_bottom_setting);
        
        
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
       

    	
        final Spinner spin = (Spinner)findViewById(R.id.notice_detail_spinner1);
        
        spin.setPrompt("보실 정보를 선택하세요");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, items1);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{       
        		AppManagement _AppManager = (AppManagement) getApplication();
        		_AppManager.m_SelectScrapType = position;
        		EditText SearchBox = (EditText)findViewById(R.id.notice_find);
        		SearchBox.setText("");

    			selectSort =  spin.getSelectedItem().toString();
    			
    			
    			GetNoticeList();

        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}
     
        });
    }
    

    
    public void GetCategoryList()
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
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getNoticeCategoryList.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(1,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							if ( Qlist == null )
							{
								
							}
							else
							{
								items1 = new String[Qlist.length()];
								for( int i = 0 ; i < Qlist.length(); i++ )
								{
									items1[i] = _AppManager.GetHttpManager().DecodeString(Qlist.getJSONObject(i).getString("bd_category"));
								
								}
								// 스피너를 갱신하라는 메세지를 넘겨준다. 
								handler.sendEmptyMessage(10);
							}

						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    	
    }
    
    public void GetNoticeList()
    {
    	m_ObjectArray.clear();
    	m_Adapter.notifyDataSetChanged();
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();

    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					EditText SearchBox = (EditText)findViewById(R.id.notice_find);
					String search = SearchBox.getText().toString();
					
					data.put("stx",search);
					data.put("bd_category",selectSort.toString());
					
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getNoticeList.php", data);
					
					
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
									NoticeData item = new NoticeData();
									
									JSONObject list = (JSONObject)usageList.get(i);
									
									item.bd_idx = Integer.parseInt(list.getString("bd_idx"));
									item.bd_title = _AppManager.GetHttpManager().DecodeString(list.getString("bd_title"));
									item.bd_contents = _AppManager.GetHttpManager().DecodeString(list.getString("bd_contents"));
									item.bd_file = _AppManager.GetHttpManager().DecodeString(list.getString("bd_file"));
									item.bd_regdate = _AppManager.GetHttpManager().DecodeString(list.getString("bd_regdate"));
									
									
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
		case R.id.notice_detail_search:
		{
			GetNoticeList();
		}
			break;
			
		case R.id.notice_back:
		{
			onBackPressed();
		}
		break;
		
		case R.id.notice_bottom_home:
		{
			Intent intent;
            intent = new Intent().setClass(self, HomeActivity.class);
            startActivity( intent ); 
		}
			break;
		case R.id.notice_bottom_myjob:
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
		case R.id.notice_bottom_scrap:
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
		case R.id.notice_bottom_setting:
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
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			case 8 :
				break;	
			case 10: 
				InputSpinnerItem();
				break;
			default:
  				mProgress.dismiss();
  				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			}
		}
	};
		
	
	
	public class Notice_List_Adapter extends ArrayAdapter<NoticeData>
	{

	  	private Context mContext;
	   	private int mResource;
	   	private ArrayList<NoticeData> mList;
	   	private LayoutInflater mInflater;
	    	
	   	public Notice_List_Adapter(Context context, int layoutResource, 
	  			ArrayList<NoticeData> mTweetList)
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
	   		
	   		final NoticeData recruitData = mList.get(position);
	   		
	   		if(convertView == null)
	   		{
	   			convertView = mInflater.inflate(mResource, null);
	   		}
	   		
	   		
	   		if ( recruitData != null )
	   		{
	   			
	   			TextView itemName = (TextView)convertView.findViewById(R.id.notice_row_title);
				TextView itemDesc = (TextView)convertView.findViewById(R.id.notice_row_desc);
				
	   			LinearLayout linearbar = (LinearLayout)convertView.findViewById(R.id.notice_row);
	   			
	   			
	   			itemName.setText(recruitData.bd_title  );
	   			itemDesc.setText(recruitData.bd_regdate  );
	   			
	   			
	   			linearbar.setOnClickListener(new View.OnClickListener() 
				{
					
						
					public void onClick(View v) 
					{
						// 다음 액티비티로 이동하고 보여줄 채용정보를 저장한다. 
						 AppManagement _AppManager = (AppManagement) getApplication();
						
						 _AppManager.m_NoticeData=  recruitData;
						// 일단 문제 있어 막아둠. 
						Intent intent;
			            intent = new Intent().setClass(self, NoticeDetailActivity.class);
			            startActivity( intent );

					}
				});

	   			
	   		}
	   		
	   		return convertView;
	   	}
	}
}
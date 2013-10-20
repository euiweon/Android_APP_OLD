package kr.co.rcsoft.mediatong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.rcsoft.mediatong.QNAActivity.QNA_List_Adapter;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;

public class FAQActivity extends BaseActivity implements OnClickListener{


	private ArrayList<FAQData> m_ObjectArray;
	private FAQ_List_Adapter m_Adapter;
	private ListView m_ListView;
	FAQActivity  self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_layout);  // 인트로 레이아웃 출력   
        
        self = this;
        
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
        
        m_ListView = (ListView)findViewById(R.id.faq_detail_list_view);

		m_ObjectArray = new ArrayList<FAQData>();
		m_Adapter = new FAQ_List_Adapter(this, R.layout.faq_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
        
		 {
	        	AppManagement _AppManager = (AppManagement) getApplication();
	        	LinearLayout layout = (LinearLayout)findViewById(R.id.faq_main);
	            _AppManager.GetUISizeConverter().ParentRelativeConvertLinearLayout(layout);
	    }
		 
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.faq_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }

        ImageBtnResize(R.id.faq_back );
        
        
        GetFAQFindList();
        SetBtnEvent(R.id.faq_bottom_home);
        SetBtnEvent(R.id.faq_bottom_myjob);
        SetBtnEvent(R.id.faq_bottom_scrap);
        SetBtnEvent(R.id.faq_bottom_setting);
        
        
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
    
    
    
    public void GetFAQFindList()
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
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getFaqList.php", data);
					
					
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
									FAQData item = new FAQData();
									
									JSONObject list = (JSONObject)usageList.get(i);
									
									item.bd_idx = Integer.parseInt(list.getString("bd_idx"));
									item.bd_title = _AppManager.GetHttpManager().DecodeString(list.getString("bd_title"));
									item.bd_contents = _AppManager.GetHttpManager().DecodeString(list.getString("bd_contents"));
									item.bd_file = _AppManager.GetHttpManager().DecodeString(list.getString("bd_file"));
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
    
	public void onClick(View v) 
	{
		// TODO 자동 생성된 메소드 스텁
		
		switch(v.getId())
		{
		case R.id.faq_back:
		{
			onBackPressed();
		}
			break;
			
		case R.id.faq_bottom_home:
		{
			Intent intent;
            intent = new Intent().setClass(self, HomeActivity.class);
            startActivity( intent ); 
		}
			break;
		case R.id.faq_bottom_myjob:
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
		case R.id.faq_bottom_scrap:
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
		case R.id.faq_bottom_setting:
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
				

			case 8 :
				break;	
			default:
  				mProgress.dismiss();
  				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			}
		}
	};
	
	
	
	public class FAQ_List_Adapter extends ArrayAdapter<FAQData>
	{

	  	private Context mContext;
	   	private int mResource;
	   	private ArrayList<FAQData> mList;
	   	private LayoutInflater mInflater;
	    	
	   	public FAQ_List_Adapter(Context context, int layoutResource, 
	  			ArrayList<FAQData> mTweetList)
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
	   		
	   		final FAQData FaqData = mList.get(position);
	  
	   		if(convertView == null)
	   		{
	   			convertView = mInflater.inflate(mResource, null);
	   		}
	   		
	   		
	   		if ( FaqData != null )
	   		{
	   			((TextView)convertView.findViewById(R.id.faq_row_title)).setText(FaqData.bd_title);	
	   			((TextView)convertView.findViewById(R.id.faq_row_content)).setText(FaqData.bd_contents);
	   			
	   			LinearLayout linearbar = (LinearLayout)convertView.findViewById(R.id.faq_row_2);
	   			final LinearLayout linearbar2 = (LinearLayout)convertView.findViewById(R.id.faq_row_3);
	   			
				 if ( FaqData.is_Popup == false )
				 {
					 linearbar2.setVisibility(View.GONE);
				 }
				 else
				 {
					 linearbar2.setVisibility(View.VISIBLE);
				 }

	   			linearbar.setOnClickListener(new View.OnClickListener() 
				{
					
						
					public void onClick(View v) 
					{

						 if ( FaqData.is_Popup == false )
						 {
							 FaqData.is_Popup = true;
							 linearbar2.setVisibility(View.VISIBLE);
						 }
						 else
						 {
							 FaqData.is_Popup = false;
							 linearbar2.setVisibility(View.GONE);
						 }

					}
				});
	   			
	   		}
	   		
	   		return convertView;
	   	}
	}
    
}
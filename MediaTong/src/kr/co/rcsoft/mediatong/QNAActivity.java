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

public class QNAActivity extends BaseActivity implements OnClickListener{

	private ArrayList<QNAData> m_ObjectArray;
	private QNA_List_Adapter m_Adapter;
	private ListView m_ListView;
	QNAActivity  self;
	
	boolean m_ResumeCheck = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qna_layout);  // 인트로 레이아웃 출력      
        
        self = this;
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		m_ListView = (ListView)findViewById(R.id.qna_list_view);

		m_ObjectArray = new ArrayList<QNAData>();
		m_Adapter = new QNA_List_Adapter(this, R.layout.qna_list_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.qna_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText box = (EditText)findViewById(R.id.qna_find);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutEditText(box);
        }
        
        ImageBtnResize(R.id.qna_back );
        ImageBtnResize(R.id.qna_write_btn );
        
        ImageBtnResize2( R.id.qna_search);
        GetQnaFindList();
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
        	GetQnaFindList();
    	}
    	m_ResumeCheck = true;

    	
    	
    }
    	
    
    
    
    public void GetQnaFindList()
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


					EditText SearchBox = (EditText)findViewById(R.id.qna_find);
					String search = SearchBox.getText().toString();
					
					data.put("stx",search);

					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getQnaList.php", data);
					
					
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
									QNAData item = new QNAData();
									
									JSONObject list = (JSONObject)usageList.get(i);
									
									item.bd_idx = Integer.parseInt(list.getString("bd_idx"));
									item.bd_title = _AppManager.GetHttpManager().DecodeString(list.getString("bd_title"));
									item.bd_contents = _AppManager.GetHttpManager().DecodeString(list.getString("bd_contents"));
									item.bd_file = _AppManager.GetHttpManager().DecodeString(list.getString("bd_file"));
									item.is_answered = _AppManager.GetHttpManager().DecodeString(list.getString("is_answered"));
									item.bd_answer = _AppManager.GetHttpManager().DecodeString(list.getString("bd_answer"));
									
									item.bd_regdate = _AppManager.GetHttpManager().DecodeString(list.getString("bd_regdate"));
									item.mb_name = _AppManager.GetHttpManager().DecodeString(list.getString("mb_name"));
									
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
		case R.id.qna_write_btn:
		{
			
	  		AppManagement _AppManager = (AppManagement) getApplication();
    		if ( _AppManager.m_LoginCheck == true )
    		{
        		Intent intent;
                intent = new Intent().setClass(this, QNAWriteActivity.class);
                startActivity( intent );
                
                m_ResumeCheck = false;
    		}
    		else
    		{
    			self.ShowAlertDialLog( self ,"로그인 에러" , "로그인을 해야 이 메뉴를 사용할수 있습니다." );
    		}
    		

		}
			break;
		case R.id.qna_search:
			GetQnaFindList();
			break; 
			
		case R.id.qna_back:
		{
			onBackPressed();
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
	
	
	
	public class QNA_List_Adapter extends ArrayAdapter<QNAData>
	{

	  	private Context mContext;
	   	private int mResource;
	   	private ArrayList<QNAData> mList;
	   	private LayoutInflater mInflater;
	    	
	   	public QNA_List_Adapter(Context context, int layoutResource, 
	  			ArrayList<QNAData> mTweetList)
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
	   		
	   		final QNAData QnaData = mList.get(position);
	   		
	   		if(convertView == null)
	   		{
	   			convertView = mInflater.inflate(mResource, null);
	   		}
	   		
	   		
	   		if ( QnaData != null )
	   		{
	   			((TextView)convertView.findViewById(R.id.notice_row_title)).setText(QnaData.bd_title);	
	   			((TextView)convertView.findViewById(R.id.notice_row_desc)).setText( QnaData.mb_name + " | " + QnaData.bd_regdate);
	   			
	   			
	   			if ( QnaData.is_answered.equals("0") == true)
	   			{
	   				((ImageView)convertView.findViewById(R.id.qna_ans)).setVisibility(View.GONE);
	   			}
	   			else
	   			{
	   				((ImageView)convertView.findViewById(R.id.qna_ans)).setVisibility(View.VISIBLE);
	   			}
	   			LinearLayout linearbar = (LinearLayout)convertView.findViewById(R.id.notice_row);
	   			

	   			linearbar.setOnClickListener(new View.OnClickListener() 
				{
					
						
					public void onClick(View v) 
					{
						// 다음 액티비티로 이동하고 보여줄 채용정보를 저장한다. 
						 AppManagement _AppManager = (AppManagement) getApplication();
						 _AppManager.m_QnaData = QnaData;
						 
							Intent intent;
				            intent = new Intent().setClass(self, QNADetailActivity.class);
				            startActivity( intent );

					}
				});
	   			
	   		}
	   		
	   		return convertView;
	   	}
	}
	
}
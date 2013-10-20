package oppa.rcsoft.co.kr;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


// 쪽지함 
public class My_Memo_Collect extends BaseActivity implements OnClickListener {

	My_Memo_Collect self;
	private ArrayList<BBS_Content_Object> m_ObjectArray;
	private BBS_View_Adapter m_Adapter;
	private ListView m_ListView;
	
	private ArrayList<String> m_DeleteList;
	
	
	int total_count;
	int total_page = 0;
	Integer current_count = 0;
	Integer current_page = 1;
	
	int deletecount = 0;
	
	String memoType;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.myinfo_memotab); 
        self = this;
		m_ListView = (ListView)findViewById(R.id.myinfo_memo_listview);

		m_ObjectArray = new ArrayList<BBS_Content_Object>();
		m_Adapter = new BBS_View_Adapter(this, R.layout.myinfo_memo_view_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		m_DeleteList =new ArrayList<String>();
		m_DeleteList.clear();

		memoType ="memo1";
		
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		Button deleteBtn  = (Button)findViewById(R.id.myinfo_memo_listview_more_btn);
		
		deleteBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v )
	        {
	        	switch(v.getId())
	        	{
	        	case R.id.myinfo_memo_listview_more_btn:
	        	{
	        		self.MoreRefreshUI();
	        	}
	        		break;
	        	}
	        }
		});
		
        {
        	ImageButton MainBtn1 = (ImageButton)findViewById(R.id.my_page_memo_tab_btn_1);
            MainBtn1.setOnClickListener(this);
            ImageButton MainBtn2 = (ImageButton)findViewById(R.id.my_page_memo_tab_btn_2);
            MainBtn2.setOnClickListener(this);

        }

		///////////////////////////////////////////////////////////////////////////////////////////////////
		// Tab
		{
    		Button TabBTN2 = (Button)findViewById(R.id.mypage1_main_tab_btn_1);
    		TabBTN2.setOnClickListener(this);
    		Button TabBTN3 = (Button)findViewById(R.id.mypage1_main_tab_btn_2);
    		TabBTN3.setOnClickListener(this);
    		Button TabBTN4 = (Button)findViewById(R.id.mypage1_main_tab_btn_3);
    		TabBTN4.setOnClickListener(this);
    		Button TabBTN5 = (Button)findViewById(R.id.mypage1_main_tab_btn_4);
    		TabBTN5.setOnClickListener(this);
		}
		
        RefreshUI();
    }
    
    
    public void onClick(View v )
    {
    	switch(v.getId())
    	{
	
	    	case R.id.my_page_memo_tab_btn_1 :
	    		
	    	{
	        	ImageButton MainBtn1 = (ImageButton)findViewById(R.id.my_page_memo_tab_btn_1);
	            ImageButton MainBtn2 = (ImageButton)findViewById(R.id.my_page_memo_tab_btn_2);
	            
				MainBtn1.setBackgroundResource(R.drawable.n_5_10on);
				MainBtn2.setBackgroundResource(R.drawable.n_5_11);
				
				memoType = "memo1";
				
				RefreshUI();
	    	}
	    		break;
	    		
	    		
	    	case R.id.my_page_memo_tab_btn_2 :
	    		
	    	{
	        	ImageButton MainBtn1 = (ImageButton)findViewById(R.id.my_page_memo_tab_btn_1);
	            ImageButton MainBtn2 = (ImageButton)findViewById(R.id.my_page_memo_tab_btn_2);
	            
				MainBtn1.setBackgroundResource(R.drawable.n_5_10);
				MainBtn2.setBackgroundResource(R.drawable.n_5_11on);	
				
				memoType = "memo2";
				
				RefreshUI();
	    	}
	    		break;
    		
    		
	    	case R.id.mypage1_main_tab_btn_2:

	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, Shop_MainList.class);
		        startActivity( intent );  
	    	}
	    		break;
	    		
	    	case R.id.mypage1_main_tab_btn_3:
	 
	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, ToyMainList.class);
		        startActivity( intent );   
	    	}
	    		break;
	    		
	    	case R.id.mypage1_main_tab_btn_4:
	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, Community_Main.class);
		        startActivity( intent );  
	    	}
	    		break;
	    		
	    	case R.id.mypage1_main_tab_btn_1:
	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, Home.class);
		        startActivity( intent );  
	    	}
	    		break;
	    		
	    	default:
	    		break;
    	}
    }
    

    public  void DeletePopup()
    {
    	m_DeleteList.clear();
		// 지워야 할것들의 리스트를 수집한다. 
		
		for ( int i = 0; i < m_ObjectArray.size() ; i++ )
		{
			if ( m_ObjectArray.get(i).isDelete )
			{
				m_DeleteList.add(m_ObjectArray.get(i).getWrId().toString());
			}
		}
		
		//  삭제 할것인지 묻는 팝업창을 띄운다. 
		if ( m_DeleteList.size() != 0)
		{
			 new AlertDialog.Builder(self.getParent().getParent())
			 .setTitle("삭제 확인")
			 .setMessage("정말 삭제 하겠습니까?") //줄였음
			 .setPositiveButton("예", new DialogInterface.OnClickListener() 
			 {
			     public void onClick(DialogInterface dialog, int whichButton)
			     {   
			    	 deletecount = 0;
			    	 DeleteMemo();
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
    
    
	void DeleteMemo()
	{
		mProgress.show();
		{
			final MyInfo myApp = (MyInfo) getApplication();
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{


					Map<String, String> data = new HashMap  <String, String>();
					data.put("me_id",m_DeleteList.get(deletecount) );
					data.put("return_type", "json");


					String strJSON;
					
					if ( memoType == "memo1")
					{
						strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_memoRecvBoxDelete.php", data);
					}
					else
					{
						strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_memoSendBoxDelete.php", data);
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{

							handler.sendEmptyMessage(4);
						}
						else if(json.getString("result").equals("error"))
						{
							handler.sendMessage(handler.obtainMessage(2,myApp.DecodeString(json.getString("result_text")) ));
							//handler.sendEmptyMessage(1);
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
	
    
    public void RefreshUI()
    {
    	current_page  = 1;
    	MemoList( current_page);
    	m_ObjectArray.clear();
		m_Adapter.notifyDataSetChanged();
    }
    
    public void MoreRefreshUI()
    {
    	current_page++;
    	
    	if ( current_page > total_page )
    		return; 

    	MemoList( current_page);
    }
   
    
	public void MemoList( Integer current_page)
	{

		
		mProgress.show();
		{
			final MyInfo myApp = (MyInfo) getApplication();
			final String strPage = current_page.toString();
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{


					Map<String, String> data = new HashMap  <String, String>();

					data.put("page",strPage );
					data.put("return_type", "json");


					String strJSON ;
					if ( memoType == "memo1")
					{
						strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_memoRecvBox.php", data);
					}
					else
					{
						strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_memoSendBox.php", data);
					}
					
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							total_count = json.getInt("total_count");
							total_page = json.getInt("total_page");
							
							if ( total_count != 0 )
							{
								JSONArray usageList = (JSONArray)json.get("list");

								for(int i = 0; i < usageList.length(); i++)
								{
									BBS_Content_Object item = new BBS_Content_Object();
									JSONObject list = (JSONObject)usageList.get(i);
									
									if ( memoType == "memo1")
									{
										item.setWrId(list.getInt("me_id"));
										item.setMbId(myApp.DecodeString(list.getString("me_send_mb_id")));
										item.setWrName(myApp.DecodeString(list.getString("mb_nick")));
										item.setWrSubject(myApp.DecodeString(list.getString("me_send_datetime")));
										item.setWrContent(myApp.DecodeString(list.getString("me_memo")));	
										item.isMainContent = true;
									}
									else
									{
										item.setWrId(list.getInt("me_id"));
										item.setMbId(myApp.DecodeString(list.getString("me_recv_mb_id")));
										item.setWrName(myApp.DecodeString(list.getString("mb_nick")));
										item.setWrSubject(myApp.DecodeString(list.getString("me_send_datetime")));
										item.setWrDatetime(myApp.DecodeString(list.getString("me_read_datetime")));
										item.setWrContent(myApp.DecodeString(list.getString("me_memo")));
										
										item.isMainContent = false;
									}

	
									m_ObjectArray.add(item);

								}								
							}

							handler.sendEmptyMessage(0);
						}
						else if(json.getString("result").equals("error"))
						{
							handler.sendMessage(handler.obtainMessage(2,myApp.DecodeString(json.getString("result_text")) ));
							//handler.sendEmptyMessage(1);
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
	
	
	
	final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{

			String message = null;
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				// 더보기 버튼을 비활성화 시킨다. 
				if ( total_page > current_page )
				{
					Button btn = (Button)findViewById(R.id.myinfo_memo_listview_more_btn);
					btn.setVisibility(View.VISIBLE);
				}
				else
				{
					Button btn = (Button)findViewById(R.id.myinfo_memo_listview_more_btn);
					btn.setVisibility(View.GONE);				
				}
				m_Adapter.notifyDataSetChanged();
				break;
			case 1:
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				// 오류처리 
				self.ShowAlertDialLog( self.getParent().getParent() ,"에러" , (String) msg.obj );
				break;
			case 3:
				message = "데이터가 없습니다";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 4 :
			{
				deletecount++;
				if ( deletecount == m_DeleteList.size() )
				{
					//
					current_page = 1;
					RefreshUI();
				}
				else
				{
					DeleteMemo();
				}
			}
				break;
			default:
				//	message = "실패하였습니다.";

				break;
			}

			//			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
		}
	};
	
	
    
    public class BBS_View_Adapter extends ArrayAdapter<BBS_Content_Object>
    {

    	private Context mContext;
    	private int mResource;
    	private ArrayList<BBS_Content_Object> mList;
    	private LayoutInflater mInflater;
    	
    	public BBS_View_Adapter(Context context, int layoutResource, 
    			ArrayList<BBS_Content_Object> mTweetList)
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
    		final BBS_Content_Object item = mList.get(position);

    		if(convertView == null)
    		{
    			convertView = mInflater.inflate(mResource, null);
    		}

    		if(item != null) 
    		{
    			// 삭제 버튼 관련 처리 
    			{
        			final ImageButton deleteButton = (ImageButton)convertView.findViewById(R.id.n_memo_del_btn);
        			deleteButton.setOnClickListener(new OnClickListener()
        			{
        				public void onClick(View v )
            	        {
            	        	switch(v.getId())
            	        	{
            	        	case R.id.n_memo_del_btn:
            	        	{
            	        		if ( item.isDelete) 
            	        		{
            	        			deleteButton.setBackgroundResource(R.drawable.select_btn2);
            	        		}
            	        		else
            	        		{
            	        			deleteButton.setBackgroundResource(R.drawable.n_5_16on);
            	        		}
            	        		item.isDelete = !item.isDelete;
            	        		
            	        	}
            	        		break;
            	        	}
            	        }
        			});
        			
        			// 실제로 convertView를 재활용 하기 때문에 문제가 생겨서 
        			// 여기에 한번더 이미지 변경을 한다. 
        			if ( item.isDelete )
        			{
        				deleteButton.setBackgroundResource(R.drawable.n_5_16on);
        			}
        			else
        			{
        				deleteButton.setBackgroundResource(R.drawable.select_btn2);
        			}  				
    			}
    			
    			// 받은 쪽지함
    			if (self.memoType == "memo1" )
    			{
    				TextView itemName = (TextView)convertView.findViewById(R.id.my_info_memo_title);
    				TextView itemAdd = (TextView)convertView.findViewById(R.id.my_info_memo_content);
    				TextView itemDist = (TextView)convertView.findViewById(R.id.my_info_memo_writer);
    				TextView itemZZim = (TextView)convertView.findViewById(R.id.my_info_memo_confirm);  	
    				
    				itemName.setText("보내신분 : " + item.getWrName());
    				itemAdd.setText(item.getWrContent());
    				
    				itemDist.setText(item.getWrDatetime());

    				itemZZim.setText("");
    			}
    			// 보낸쪽지함
    			else
    			{
    				TextView itemName = (TextView)convertView.findViewById(R.id.my_info_memo_title);
    				TextView itemAdd = (TextView)convertView.findViewById(R.id.my_info_memo_content);
    				TextView itemDist = (TextView)convertView.findViewById(R.id.my_info_memo_writer);
    				TextView itemZZim = (TextView)convertView.findViewById(R.id.my_info_memo_confirm);  	
    				
    				itemName.setText("받으시는 분 : " + item.getWrName());
    				itemAdd.setText(item.getWrContent());
    				
    				itemDist.setText("");

    				if ( item.getWrDatetime().equals("0000-00-00 00:00:00") )
    				{
    					itemZZim.setText("운영자 미확인");
    				}
    				else
    				{
    					itemZZim.setText("운영자 확인 " + item.getWrDatetime());
    				}
    			}
    		}
    		return convertView;
    	}
    }
}

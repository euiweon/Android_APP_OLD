package oppa.rcsoft.co.kr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import oppa.rcsoft.co.kr.BBS_Main_List.BBSMainBTNClick;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



// 커뮤니티 QNA 게시판 
public class BBS_QNA_List extends  BaseActivity 
{
	static BBS_QNA_List  self; 
	int total_count;
	int total_page = 0;
	Integer current_count = 0;
	Integer current_page = 1;
	
	private ArrayList<BBS_Content_Object> m_ObjectArray;
	private BBS_View_Adapter m_Adapter;
	private ListView m_ListView;
	
	Button Morebtn;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbs_main_list);
     
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		m_ListView = (ListView)findViewById(R.id.bbs_main_list_view);

		m_ObjectArray = new ArrayList<BBS_Content_Object>();
		m_Adapter = new BBS_View_Adapter(this, R.layout.oppa_qna, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
		m_ListView.setDividerHeight(0);
		m_ListView.setCacheColorHint(0);
		
		
        
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// Tab
		{
			Button TabBTN2 = (Button)findViewById(R.id.bbs_main_list_tab_btn_1);
			TabBTN2.setOnClickListener(new BBSMainBTNClick());
			Button TabBTN3 = (Button)findViewById(R.id.bbs_main_list_tab_btn_2);
			TabBTN3.setOnClickListener(new BBSMainBTNClick());
			Button TabBTN4 = (Button)findViewById(R.id.bbs_main_list_tab_btn_3);
			TabBTN4.setOnClickListener(new BBSMainBTNClick());
			Button TabBTN5 = (Button)findViewById(R.id.bbs_main_list_tab_btn_5);
			TabBTN5.setOnClickListener(new BBSMainBTNClick());
		}
		
        Morebtn = (Button)findViewById(R.id.bbs_main_list_more_123);
        Morebtn.setOnClickListener(new BBSMainBTNClick());
        
        Button writebtn = (Button) findViewById(R.id.bbs_main_write);
        
        writebtn.setOnClickListener(new BBSMainBTNClick());
        
        RefreshUI();
    }
    

    
    public  class BBSMainBTNClick implements OnClickListener
    {

		public void onClick(View v )
        {
			MyInfo myApp = (MyInfo) getApplication();
        	switch(v.getId())
        	{

        	case R.id.bbs_main_list_more_123:
        		MoreRefreshUI();
        		break;
        		
        	case R.id.bbs_main_write:
	        	{
	        		myApp.m_BBSID.bo_table = "qna";
	           		Intent intent;
	    	        intent = new Intent().setClass(self, BBS_Writer.class);
	    	        startActivity( intent );	
	        	}
	        	break;
	        	
	    	case R.id.bbs_main_list_tab_btn_1:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, Home.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.bbs_main_list_tab_btn_2:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.bbs_main_list_tab_btn_3:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, ToyMainList.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.bbs_main_list_tab_btn_5:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, MyPage_Main.class);
		        startActivity( intent );  		
	    	}
	    		break;
        		

        	}
        }
    }
    
    
    @Override
    public void RefreshUI()
    {
    	m_ObjectArray.clear();
    	m_Adapter.clear();
    	mProgress.show();
    	
    	
		ImageView title = (ImageView) findViewById(R.id.bbs_main_list_title_3);
		title.setImageResource(R.drawable.n_4_title5);
		title.setMaxWidth(150);
		
		Button writebtn = (Button) findViewById(R.id.bbs_main_write);
		writebtn.setVisibility(View.VISIBLE);
		
	
		current_page  = 1;

		GetBBSPage();	
    }
    
    // 더보기를 클릭했을때...
    public void MoreRefreshUI()
    {
    	mProgress.show();
    	current_page++;
    	
    	if ( current_page > total_page )
    		return; 

    	GetBBSPage();
    }
    
    
    // 게시물 가져오기 
    void GetBBSPage( )
    {
    	{
    		
    		final MyInfo myApp = (MyInfo) getApplication();
			final String strPage = current_page.toString();
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					data.put("bo_table",myApp.m_BBSID.bo_table );
					data.put("page", strPage);
					data.put("return_type", "json");

					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_getBoardList.php", data);	
					if ( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"서버에 문제가 있으므로 나중에 다시 시도하세요." ));
						return;
					}
					
					JSONObject json = null;
	 				try 
	 				{
	 					json = new JSONObject(strJSON);
	 				} 
	 				catch (JSONException e1) 
	 				{
	 					// TODO Auto-generated catch block
	 					e1.printStackTrace();
	 				}
	 				
					try 
					{
						if(json.getString("result").equals("ok"))
	 					{

							total_page = json.getInt("total_page");
	 						total_count = json.getInt("total_count");
	 						if ( total_count != 0 )
	 						{
	 							
	 						}
	 						JSONArray usageList = (JSONArray)json.get("list");

	 						for(int i = 0; i < usageList.length(); i++)
	 						{
	 							BBS_Content_Object item = new BBS_Content_Object();
	 							JSONObject list = (JSONObject)usageList.get(i);
	 							item.setIsNotice(list.getInt("is_notice"));
	 							item.setWrId(list.getInt("wr_id"));
	 							item.setCaName(myApp.DecodeString(list.getString("ca_name")));
	 							item.setReplyLen(list.getInt("reply_len"));
	 							item.setIsSecret(list.getInt("is_secret"));
	 							item.setWrSubject(myApp.DecodeString(list.getString("wr_subject")));
	 							item.setWrContent(myApp.DecodeString(list.getString("wr_content")));
	 							item.setWrName(myApp.DecodeString(list.getString("wr_name")));
	 							item.setMbId(myApp.DecodeString(list.getString("mb_id")));
	 							item.setWrDatetime(myApp.DecodeString(list.getString("wr_datetime")));
	 							item.setWrHit(list.getInt("wr_hit"));
	 							item.setWr5(myApp.DecodeString(list.getString("wr_5")));
	 							item.setWr8(myApp.DecodeString(list.getString("wr_8")));
	 							item.setCommentCnt(list.getInt("comment_cnt"));
	 							item.mb_grade = myApp.DecodeString(list.getString("mb_grade")); 
	 						
	 							item.isMainContent = true;
	 							m_ObjectArray.add(item);
	 							
	 							JSONArray usageList2 = (JSONArray)list.get("comment_list");
								for(int j = 0; j < usageList2.length(); j++)
								{
									BBS_Content_Object item2 = new BBS_Content_Object();
									JSONObject list2 = (JSONObject)usageList2.get(j);
									item2.isMainContent  =false;
									
									item2.setWrName(myApp.DecodeString(list2.getString("cmt_name")));
									item2.setWrContent(myApp.DecodeString(list2.getString("cmt_content")));
									item2.setWrDatetime(myApp.DecodeString(list2.getString("cmt_datetime")));
									item2.cmt_id = list2.getInt("cmt_id");
									
									item2.mb_grade = myApp.DecodeString(list2.getString("cmt_mb_grade")); 

									
			
									m_ObjectArray.add(item2);
								}
								
		
								BBS_Content_Object item3 = new BBS_Content_Object();
								item3.isLine = true; 
								m_ObjectArray.add(item3);
	 						}
	 						handler.sendEmptyMessage(0);
	 					}

						else if(json.getString("result").equals("error"))
						{
							handler.sendMessage(handler.obtainMessage(2,myApp.DecodeString(json.getString("result_text")) ));
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
			{
				m_Adapter.notifyDataSetChanged();
				// 더보기 버튼을 비활성화 시킨다. 
				if ( total_page > current_page )
				{
					Button btn = (Button)findViewById(R.id.bbs_main_list_more_123);
					btn.setVisibility(View.VISIBLE);
				}
				else
				{
					Button btn = (Button)findViewById(R.id.bbs_main_list_more_123);
					btn.setVisibility(View.GONE);				
				}
			}
				
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
			default:
				//	message = "실패하였습니다.";

				break;
			}

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

			}
			
			LinearLayout linearBar = (LinearLayout)convertView.findViewById(R.id.qna_q_layout );
			LinearLayout linearBar2 = (LinearLayout)convertView.findViewById(R.id.qna_a_layout );
			
			LinearLayout linearLine = (LinearLayout)convertView.findViewById(R.id.qna_line );
			final MyInfo myApp = (MyInfo) getApplication();
			if ( item.isLine )
			{
				linearBar.setVisibility(View.GONE);
				linearBar2.setVisibility(View.GONE);
				linearLine.setVisibility(View.VISIBLE);
			}
			else
			{
				if ( item.isMainContent)
				{
					linearBar.setVisibility(View.VISIBLE);
					linearBar2.setVisibility(View.GONE);
					linearLine.setVisibility(View.GONE);
					
					
					
					TextView  txtName = (TextView)convertView.findViewById(R.id.txt_qna_question);
					txtName.setText(item.getWrContent());
					
					TextView  txttime = (TextView)convertView.findViewById(R.id.img_qwriter);
					txttime.setText(  item.getWrName()+ "  "+ item.getWrDatetime());
					
					ImageView grade = (ImageView)convertView.findViewById(R.id.bbs_title_writer_2_icon);
        			grade.setBackgroundResource(myApp.GetGradeIcon(item.mb_grade));
					
					
					Button  itemAddIcon = (Button)convertView.findViewById(R.id.img_qna_reply);
					// 클릭이벤트 추가     					
					itemAddIcon.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v )
				        {
				        	switch(v.getId())
				        	{
				        	case R.id.img_qna_reply:
				        	{
				        		myApp.m_BBSReplyContent.bo_table = "qna";
								myApp.m_BBSReplyContent.wr_id = item.getWrId().toString();
								myApp.m_BBSReplyContent.isReply = false;
						
				           		Intent intent;
				    	        intent = new Intent().setClass(self, BBS_Reply.class);
				    	        startActivity( intent );	
								
				        	}
				        		break;
				        	}
				        }
					});
					
					
				}
				else
				{
					linearBar.setVisibility(View.GONE);
					linearBar2.setVisibility(View.VISIBLE);
					linearLine.setVisibility(View.GONE);
					
					TextView  txtName = (TextView)convertView.findViewById(R.id.txt_qna_answer);
					txtName.setText(item.getWrContent());
					
					TextView  txttime = (TextView)convertView.findViewById(R.id.img_awriter);
					txttime.setText(  item.getWrName()+ "  "+ item.getWrDatetime());
					
					
					ImageView grade = (ImageView)convertView.findViewById(R.id.bbs_title_writer_4_icon);
        			grade.setBackgroundResource(myApp.GetGradeIcon(item.mb_grade));
				}
				
			}

    		return convertView;
    	}
    }
    
    
}

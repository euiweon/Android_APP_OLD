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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// 찜한 언니 
public class My_ZZimSister extends  BaseActivity   implements OnClickListener
{

	My_ZZimSister self;

	int total_count;
	int total_page = 0;
	int current_count;
	Integer current_page = 1;
	
	int deletecount = 0;
	
	private ArrayList<BBS_Content_Object> m_ObjectArray;
	private MyInfoShop_View_Adapter m_Adapter;
	private ListView m_ListView;
	
	private ArrayList<String> m_DeleteList;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        
        setContentView(R.layout.myinfo_shop_main_list);          // 인트로 레이아웃 출력  
        
        self = this;
        
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		m_ListView = (ListView)findViewById(R.id.myinfo_main_list_view);

		m_ObjectArray = new ArrayList<BBS_Content_Object>();
		m_Adapter = new MyInfoShop_View_Adapter(this, R.layout.myinfo_shoplist_view_row, m_ObjectArray);
		m_ListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		m_ListView.setAdapter(m_Adapter);
		
		m_DeleteList = new ArrayList<String>();
	

		ImageView title = (ImageView)findViewById(R.id.mypage_shop_main_list_title);
		
		title.setImageResource(R.drawable.n_5_title4);
		
		
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
		
		
		Button deleteBtn  = (Button)findViewById(R.id.my_shop_delete);
		
		deleteBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v )
	        {
	        	switch(v.getId())
	        	{
	        	case R.id.my_shop_delete:
	        	{

	        		m_DeleteList.clear();
	        		// 지워야 할것들의 리스트를 수집한다. 
	        		
	        		for ( int i = 0; i < m_ObjectArray.size() ; i++ )
	        		{
	        			if ( m_ObjectArray.get(i).isDelete )
	        			{
	        				m_DeleteList.add(m_ObjectArray.get(i).getMbId());
	        			}
	        		}
	        		
	        		//  삭제 할것인지 묻는 팝업창을 띄운다. 
	        		if ( m_DeleteList.size() != 0)
	        		{
	        			 new AlertDialog.Builder(self)
	        			 .setTitle("삭제 확인")
	        			 .setMessage("정말 삭제 하겠습니까?") //줄였음
	        			 .setPositiveButton("예", new DialogInterface.OnClickListener() 
	        			 {
	        			     public void onClick(DialogInterface dialog, int whichButton)
	        			     {   
	        			    	 deletecount = 0;
	        			    	 DeleteSister();
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
	        		break;
	        	}
	        }
		});
		
		 RefreshUI();
		//setListView();
    }
    
    
	public void onClick(View v )
    {
		
    	switch(v.getId())
    	{

    		
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

    	}
    }
    

    
    public void RefreshUI()
    {
    	m_ObjectArray.clear();
    	m_Adapter.clear();
    	SisterList( current_page);
    }
    
   
    
	public void SisterList( Integer current_page)
	{

		
		m_Adapter.notifyDataSetChanged();
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


					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_favoriteGirlList.php", data);
					
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
									item.setMbId(myApp.DecodeString(list.getString("gr_id")));
									item.setWrName(myApp.DecodeString(list.getString("gr_name")));
									item.setWrSubject(myApp.DecodeString(list.getString("sh_addr")));
									item.setWrHit(Integer.parseInt(myApp.DecodeString(list.getString("gr_favorite_cnt"))));
									item.setWrContent(myApp.DecodeString(list.getString("sh_tel")));
									
									String temp = "";
									String temp2 = list.getString("gr_img");
									
									if ( !temp2.equals(temp) )
									{
										URL imgUrl = new URL(myApp.DecodeString(list.getString("gr_img")));
										URLConnection conn = imgUrl.openConnection();
										conn.connect();
										BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
										Bitmap bm = BitmapFactory.decodeStream(bis);
										bis.close();
										
										item.th_img = bm;	
									
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
					} catch (MalformedURLException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
				}
			});
			thread.start();	
		}

	}
	
	void DeleteSister()
	{
		
		mProgress.show();
		{
			final MyInfo myApp = (MyInfo) getApplication();
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{


					Map<String, String> data = new HashMap  <String, String>();
					data.put("gr_id",m_DeleteList.get(deletecount) );
					data.put("return_type", "json");


					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_favoriteGirlDelete.php", data);
					
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
					Button btn = (Button)findViewById(R.id.bbs_main_list_more);
					btn.setVisibility(View.VISIBLE);
				}
				else
				{
					Button btn = (Button)findViewById(R.id.bbs_main_list_more);
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
					DeleteSister();
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
	
	
    
    public class MyInfoShop_View_Adapter extends ArrayAdapter<BBS_Content_Object>
    {

    	private Context mContext;
    	private int mResource;
    	private ArrayList<BBS_Content_Object> mList;
    	private LayoutInflater mInflater;
    	
    	public MyInfoShop_View_Adapter(Context context, int layoutResource, 
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
        			final ImageButton deleteButton = (ImageButton)convertView.findViewById(R.id.n_5_16_btn);
        			deleteButton.setOnClickListener(new OnClickListener()
        			{
        				public void onClick(View v )
            	        {
            	        	switch(v.getId())
            	        	{
            	        	case R.id.n_5_16_btn:
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
				ImageView itemPic = (ImageView)convertView.findViewById(R.id.my_info_list_view_row_image);
				TextView itemName = (TextView)convertView.findViewById(R.id.my_info_list_view_row_title);
				TextView itemAdd = (TextView)convertView.findViewById(R.id.my_info_list_view_row_addr);
				TextView itemDist = (TextView)convertView.findViewById(R.id.my_info_list_view_row_tel);
				TextView itemZZim = (TextView)convertView.findViewById(R.id.my_info_list_view_row_score);
				
				ImageView star1 = (ImageView)convertView.findViewById(R.id.my_info_main_score1);
				ImageView star2 = (ImageView)convertView.findViewById(R.id.my_info_main_score2);
				ImageView star3 = (ImageView)convertView.findViewById(R.id.my_info_main_score3);
				ImageView star4 = (ImageView)convertView.findViewById(R.id.my_info_main_score4);
				ImageView star5 = (ImageView)convertView.findViewById(R.id.my_info_main_score5);
	
				// 전화 걸기 
				Button CallBtn  = (Button)convertView.findViewById(R.id.my_detail_call_btn);
				CallBtn.setOnClickListener(new OnClickListener()
    			{
    				public void onClick(View v )
        	        {
        	        	switch(v.getId())
        	        	{
        	        	case R.id.my_detail_call_btn:
        	        	{
        	        		Intent intent = new Intent(Intent.ACTION_CALL);
        	        		intent.setData(Uri.parse("tel:"+item.getWrContent()));
        	        		startActivity(intent);
        	        	}
        	        		break;
        	        	}
        	        }
    			});
				
				itemPic.setImageBitmap(item.th_img);
				itemName.setText(item.getWrName());
				itemAdd.setText(item.getWrSubject());
				itemZZim.setText("찜 횟수 : " + item.getWrHit().toString());
				//itemAvr.setText("평점 :" + mBar.getShEvalPoint());
				
				
				itemDist.setText("전화번호 :  "+ item.getWrContent());
								
				star1.setVisibility(View.GONE);
				star2.setVisibility(View.GONE);
				star3.setVisibility(View.GONE);
				star4.setVisibility(View.GONE);
				star5.setVisibility(View.GONE);
				
    		}
    		return convertView;
    	}
    }
}

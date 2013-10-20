package oppa.rcsoft.co.kr;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import oppa.rcsoft.co.kr.BBS_Main_List.BBSMainBTNClick;
import oppa.rcsoft.co.kr.Shop_Detail_News.MoreBTNClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;





// Shop 업소 후기 게시판 클래스
 
public class Shop_Detail_After_ViewFlipper extends BaseActivity  
{


	
	String m_ShopID;
	String m_TelNumber = "01012341234";
	String m_Title;
	String m_Addr;
	String m_Score;
	String m_C1Name;
	String m_C2Name;
	Bitmap m_Bitmap;
	int m_Distance;
	int m_evlCount = 0;
	int m_ZZimeCount = 0;
	
	int total_count;
	int total_page = 0;
	Integer current_count = 0;
	Integer current_page = 1;
	
	
	Boolean 		m_bFirstPage;
	
	public static Shop_Detail_After_ViewFlipper self;
	
	
	// 게시판
    private ArrayList<BBS_Content_Object> m_AfterList;
	private Shop_Detail_After_List_Adapter m_AfterAdapter;
	private ListView m_AfterListView;

	
	Button Morebtn;

	
	// FirstPage 가 게시판 아닐경우가 게시물 뷰어다
	public void setFirstPage( Boolean bPage )
	{
		m_bFirstPage = bPage;
	}
    @Override  
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_detail_after);   
        
        self = this;
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
    
		
		 Morebtn = (Button)findViewById(R.id.bbs_main_list_more_123);
	     Morebtn.setOnClickListener(new MoreBTNClick());
		
		{

	        ImageButton MainBtn2 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_1);
	        MainBtn2.setOnClickListener(new MoreBTNClick());
	        ImageButton MainBtn3 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_2);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
	        ImageButton MainBtn4 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_4);
	        MainBtn4.setOnClickListener(new MoreBTNClick());
	    }
		

		{
			Button MainBtn3 = (Button)findViewById(R.id.shop_detail_call_btn);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
			Button MainBtn4 = (Button)findViewById(R.id.shop_detail_title_bar_qna);
	        MainBtn4.setOnClickListener(new MoreBTNClick());
		}
		
		{
			Button MainBtn1 = (Button)findViewById(R.id.shop_main_tab_btn_11);
	        MainBtn1.setOnClickListener(new MoreBTNClick());
	        Button MainBtn2 = (Button)findViewById(R.id.shop_main_tab_btn_31);
	        MainBtn2.setOnClickListener(new MoreBTNClick());
	        Button MainBtn3 = (Button)findViewById(R.id.shop_main_tab_btn_41);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
	        Button MainBtn4 = (Button)findViewById(R.id.shop_main_tab_btn_51);
	        MainBtn4.setOnClickListener(new MoreBTNClick());
	    }
        
        m_bFirstPage = true;
        ChangeUI();
        RefreshIntroduce( );
        
    }

    
    @Override
    public void onBackPressed() 
    {
		
        Intent intent;
        intent = new Intent().setClass(self, Shop_MainList.class);
        startActivity( intent );  
    }
    
    
    
    public  class MoreBTNClick implements OnClickListener
    {

    	MyInfo myApp = (MyInfo) getApplication();
		public void onClick(View v )
        {
        	switch(v.getId())
        	{
            
        	case R.id.bbs_main_list_more_123:
        	{
        		MoreRefreshUI();
        	}
        		break;
        	
        	case R.id.shop_main_tab_btn_11:
        	{
        		Intent intent;
    	        intent = new Intent().setClass(self, Home.class);
    	        startActivity( intent );  
        	}
        		break;
        		
        	case R.id.shop_main_tab_btn_31:
        	{
        		Intent intent;
    	        intent = new Intent().setClass(self, ToyMainList.class);
    	        startActivity( intent );   
        	}
        		break;
        		
        	case R.id.shop_main_tab_btn_41:
        	{
        		Intent intent;
    	        intent = new Intent().setClass(self, Community_Main.class);
    	        startActivity( intent );  
        	}
        		break;
        		
        	case R.id.shop_main_tab_btn_51:
        	{
        		Intent intent;
    	        intent = new Intent().setClass(self, MyPage_Main.class);
    	        startActivity( intent );  
        	}
        		break;
    			
        		
        	case R.id.shop_detail_tab_btn_2:
        	{
        		Intent intent;
        	    intent = new Intent().setClass(self, Shop_Detail_QNA.class);
        	    startActivity( intent );   
        	}
        		break;
        		
        	case R.id.shop_detail_tab_btn_1:
        	{
        		Intent intent;
        	    intent = new Intent().setClass(self, Shop_Detail_Introduce.class);
        	    startActivity( intent );  
        	}
        		break;
        		
        	case R.id.shop_detail_tab_btn_4:
        	{
        		Intent intent;
        	    intent = new Intent().setClass(self, Shop_Detail_News.class);
        	    startActivity( intent );  
        	}
        		break;  
        		
        	case R.id.shop_detail_title_bar_qna:
        	{
        		myApp.m_BBSReplyContent.isWriteType =false;
                Intent intent;
                intent = new Intent().setClass(self, Shop_Detail_After_Write.class);
                startActivity( intent );  
        	}
        	break;
        	
        	case R.id.shop_detail_call_btn:
        	{
        		Intent intent = new Intent(Intent.ACTION_DIAL);
        		intent.setData(Uri.parse("tel:"+m_TelNumber));
        		startActivity(intent);
        		break;
        	}
        	}
        }
    }

    
    
    public void RefreshIntroduce( )
    {
		mProgress.show();
		JSONObject json = null;
		MyInfo myApp = (MyInfo) getApplication();
		String strJSON = myApp.m_CurrShopInfo.IntroMessage;
		try 
		{
			json = new JSONObject(strJSON);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try 
		{
			

			if(json.getString("result").equals("ok"))
			{

				{
					m_ShopID = myApp.DecodeString(json.getString("sh_id"));
					myApp.m_CurrShopInfo.ShopID = m_ShopID;
					m_Title = myApp.DecodeString(json.getString("sh_name"));
					
					
					
					m_TelNumber = myApp.DecodeString(json.getString("sh_tel"));

					m_Addr= myApp.DecodeString(json.getString("sh_addr"));
					m_Score = myApp.DecodeString(json.getString("sh_eval_point"));
					
					
					m_C1Name = myApp.DecodeString(json.getString("c1_name"));
					m_C2Name = myApp.DecodeString(json.getString("c2_name"));
								
					m_evlCount = json.getInt("sh_eval_cnt");
					m_ZZimeCount = json.getInt("hugi_cnt");
					
					String temp = "";
					String temp2 = json.getString("sh_img");
					
					if ( !temp2.equals(temp) )
					{
						URL imgUrl = new URL(myApp.DecodeString(json.getString("sh_img")));
						URLConnection conn = imgUrl.openConnection();
						conn.connect();
						BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
						m_Bitmap = BitmapFactory.decodeStream(bis);
						bis.close();
					}
					
					m_Distance = json.getInt("distance");			
					
					handler.sendEmptyMessage(20);	
				}

			}
			else if(json.getString("result").equals("error"))
			{
				mProgress.dismiss();
				//self.ShowAlertDialLog( self.getParent() ,"에러" , (String) msg.obj );
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
 
    // 리스트를 갱신한다. 
    @Override
    public void RefreshUI()
    {
    	m_AfterList.clear();
    	m_AfterAdapter.clear();
		mProgress.show();
    	getHugi();
    }
    
    
    public void MoreRefreshUI()
    {
    	mProgress.show();
    	current_page++;
    	MyInfo myApp = (MyInfo) getApplication();

    	
    	if ( current_page > total_page )
    		return; 

    	getHugi();
    }
    // 게시물 리스트를 가져운다. 
    void getHugi()
	{

		final String strPage = current_page.toString();
		final MyInfo myApp = (MyInfo) getApplication();
		Thread thread = new Thread(new Runnable()
		{

			
			public void run() {


				Map<String, String> data = new HashMap  <String, String>();
				data.put("sh_id",myApp.m_CurrShopInfo.ShopID );
				data.put("return_type", "json");
				data.put("bo_table", "hugi");
				data.put("shop", "shop");
				data.put("page", strPage);
				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_getBoardList.php", data);
				
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

						total_count = json.getInt("total_count");
						total_page = json.getInt("total_page");
						
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
							item.setWr6(list.getInt("wr_6"));
							item.setWr8(myApp.DecodeString(list.getString("wr_8")));
							item.setCommentCnt(list.getInt("comment_cnt"));
							item.mb_grade = myApp.DecodeString(list.getString("mb_grade")); 
							//item.setFileCnt(list.getInt("file_cnt"));
							
							m_AfterList.add(item);
						}
						handler.sendEmptyMessage(3);
					}
					else if(json.getString("result").equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(6,myApp.DecodeString(json.getString("result_text")) ));
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		thread.start();
	}
    
     
    // 각 페이지가 수정될때 이함수를 호출한다. 
    public void ChangeUI()
    {

    	m_AfterListView = (ListView)findViewById(R.id.shop_detail_after_listview);
    	m_AfterList = new ArrayList<BBS_Content_Object>();
    	m_AfterAdapter = new Shop_Detail_After_List_Adapter(this, R.layout.shop_detail_after_list_view_row, m_AfterList);
    	m_AfterListView.setAdapter(m_AfterAdapter);


    }

	
	// 다른 스레드과 동기화를 위한 핸들러 처리 작업 
	final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 1:
				break;
			case 2:
				break;
			case 3:
				//후기
				mProgress.dismiss();
				m_AfterAdapter.notifyDataSetChanged();
				
				{
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
			case 4:
				//오늘의 뉴스 
				mProgress.dismiss();
				break;
			case 5:
				//후기 댓글 
				mProgress.dismiss();
				break;
				
			case 20:
			{
				TextView titleText = (TextView)findViewById(R.id.shop_detail_main_title);
				TextView addrText = (TextView)findViewById(R.id.shop_detail_main_addr);
				TextView scoreText = (TextView)findViewById(R.id.shop_detail_main_score);
				TextView itemDist = (TextView)findViewById(R.id.shop_detail_main_distance);
				ImageView mainImage = (ImageView)findViewById(R.id.shop_detail_main_image);
				TextView itemTel = (TextView)findViewById(R.id.shop_detail_main_tel);
				TextView itemZZim = (TextView)findViewById(R.id.shop_detail_main_zzimcounter);
				TextView itemCate = (TextView)findViewById(R.id.shop_detail_main_cate);
				
				
				
				
				mainImage.setImageBitmap(m_Bitmap);
				titleText.setText(m_Title);
				addrText.setText(m_Addr);
				
				
				final MyInfo myApp = (MyInfo) getApplication();
				if (myApp.mLat == -100000.0 )
				{
					itemDist.setText("거리측정불가");
				}
				else
				{
					float dis = ((float)(m_Distance )) /1000.0f;
					itemDist.setText("거리 : "+dis + "km");
				}
				
				itemTel.setText("전화 : " + m_TelNumber );
				itemZZim.setText("("+m_ZZimeCount+")");
				itemCate.setText( m_C2Name );
				
				
				
				
				{
					ImageView star1 = (ImageView)findViewById(R.id.shop_detail_main_score1);
					ImageView star2 = (ImageView)findViewById(R.id.shop_detail_main_score2);
					ImageView star3 = (ImageView)findViewById(R.id.shop_detail_main_score3);
					ImageView star4 = (ImageView)findViewById(R.id.shop_detail_main_score4);
					ImageView star5 = (ImageView)findViewById(R.id.shop_detail_main_score5);
					switch( Integer.parseInt(m_Score) )
					{
					case 1:
						star1.setVisibility(0);
						star2.setVisibility(4);
						star3.setVisibility(4);
						star4.setVisibility(4);
						star5.setVisibility(4);
						
						break;
					case 2:
						star1.setVisibility(0);
						star2.setVisibility(0);
						star3.setVisibility(4);
						star4.setVisibility(4);
						star5.setVisibility(4);
						break;
					case 3:
						star1.setVisibility(0);
						star2.setVisibility(0);
						star3.setVisibility(0);
						star4.setVisibility(4);
						star5.setVisibility(4);
						break;
					case 4:
						star1.setVisibility(0);
						star2.setVisibility(0);
						star3.setVisibility(0);
						star4.setVisibility(0);
						star5.setVisibility(4);
						break;
					case 5:
						star1.setVisibility(0);
						star2.setVisibility(0);
						star3.setVisibility(0);
						star4.setVisibility(0);
						star5.setVisibility(0);
						break;
					case 0:
						star1.setVisibility(4);
						star2.setVisibility(4);
						star3.setVisibility(4);
						star4.setVisibility(4);
						star5.setVisibility(4);
						
						break;
					}
				}
				scoreText.setText("평점 : ");
			}
				RefreshUI();
				break;
			default:
				//	message = "실패하였습니다."
				mProgress.dismiss();
				self.ShowAlertDialLog( self.getParent() ,"에러" , (String) msg.obj );
				break;
			}
		}
	};
	

	/// 게시물 리스트 클래스 
	public class Shop_Detail_After_List_Adapter extends ArrayAdapter<BBS_Content_Object>
    {

    	private Context mContext;
    	private int mResource;
    	private ArrayList<BBS_Content_Object> mList;
    	private LayoutInflater mInflater;
    	
    	public Shop_Detail_After_List_Adapter(Context context, int layoutResource, 
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

    		if(convertView == null)
    		{
    			convertView = mInflater.inflate(mResource, null);
    		}
    		
    		final BBS_Content_Object mBoard = mList.get(position);
    		final MyInfo myApp = (MyInfo) getApplication();

    		if(mBoard != null) 
    		{
    			{
    				
    				ImageView star1 = (ImageView)convertView.findViewById(R.id.shop_detail_after_list_score1);
    				ImageView star2 = (ImageView)convertView.findViewById(R.id.shop_detail_after_list_score2);
    				ImageView star3 = (ImageView)convertView.findViewById(R.id.shop_detail_after_list_score3);
    				ImageView star4 = (ImageView)convertView.findViewById(R.id.shop_detail_after_list_score4);
    				ImageView star5 = (ImageView)convertView.findViewById(R.id.shop_detail_after_list_score5);
    					
        			TextView text1 = (TextView)convertView.findViewById(R.id.shop_detail_after_list_row_title2);
        			text1.setText(mBoard.getWrContent());
        			
        			TextView text2 = (TextView)convertView.findViewById(R.id.shop_detail_after_list_row_reply_count2);
        			text2.setText("(" + mBoard.getCommentCnt().toString() + ")");
        			
        			TextView text3 = (TextView)convertView.findViewById(R.id.shop_detail_after_title_writer_4);
        			text3.setText( mBoard.getWrDatetime());
        			
        			TextView text4 = (TextView)convertView.findViewById(R.id.shop_detail_after_title_writer_8);
        			text4.setText(mBoard.getWrName());

        			
        			
        			ImageView grade = (ImageView)convertView.findViewById(R.id.bbs_title_writer_1_icon);
        			grade.setBackgroundResource(myApp.GetGradeIcon(mBoard.mb_grade));

    				switch( mBoard.getWr6() )
    				{
    				case 1:
    					star1.setVisibility(0);
    					star2.setVisibility(4);
    					star3.setVisibility(4);
    					star4.setVisibility(4);
    					star5.setVisibility(4);
    					
    					break;
    				case 2:
    					star1.setVisibility(0);
    					star2.setVisibility(0);
    					star3.setVisibility(4);
    					star4.setVisibility(4);
    					star5.setVisibility(4);
    					break;
    				case 3:
    					star1.setVisibility(0);
    					star2.setVisibility(0);
    					star3.setVisibility(0);
    					star4.setVisibility(4);
    					star5.setVisibility(4);
    					break;
    				case 4:
    					star1.setVisibility(0);
    					star2.setVisibility(0);
    					star3.setVisibility(0);
    					star4.setVisibility(0);
    					star5.setVisibility(4);
    					break;
    				case 5:
    					star1.setVisibility(0);
    					star2.setVisibility(0);
    					star3.setVisibility(0);
    					star4.setVisibility(0);
    					star5.setVisibility(0);
    					break;
    				case 0:
    					star1.setVisibility(4);
    					star2.setVisibility(4);
    					star3.setVisibility(4);
    					star4.setVisibility(4);
    					star5.setVisibility(4);
    					break;
    				}
    				
    			}
    			
    			
    			//추가적인 아이템 추가시 위와 같이 만들어주면 됨. 단 아이템으로 사용된 xml파일을 수정해주어야 함.

				LinearLayout linearBar = (LinearLayout)convertView.findViewById(R.id.shop_detail_after_row123 );

				
				//후기를 구현하기 위해서 이부분을 구현해야함.

				linearBar.setOnClickListener(new View.OnClickListener() 
				{

					
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						//후기 내용을 누르면 이부분에서 처리한다. 
						

						// 다음 게시판 뷰로 이동한다. 게시판 아이디와 글 번호를 Application 클래스에 넘겨준다. 
						MyInfo myApp = (MyInfo) getApplication();
						myApp.m_BBSViewerID.bo_table = "hugi";
						myApp.m_BBSViewerID.wr_id = mBoard.getWrId().toString();
						
						
				        Intent intent;
				        intent = new Intent().setClass(self, Shop_Detail_After_BBS.class);
				        startActivity( intent );  
						
						
					}
				});
    		}

    		return convertView;
    	}
    }
    
}

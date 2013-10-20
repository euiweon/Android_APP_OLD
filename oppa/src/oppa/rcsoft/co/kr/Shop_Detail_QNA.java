package oppa.rcsoft.co.kr;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import oppa.rcsoft.co.kr.Shop_Detail_After_ViewFlipper.MoreBTNClick;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



// Shop QNA

public class Shop_Detail_QNA extends BaseActivity{
    private ArrayList<BBS_Content_Object> m_ObjectArray;
	private Shop_Detail_QNA_List_Adapter m_Adapter;
	private ListView m_ListView;
	
	public static Shop_Detail_QNA self;
	
	
	
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
	
	Button Morebtn;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_detail_qna);
        self =this;
        
		m_ListView = (ListView)findViewById(R.id.shop_detail_qna_listview);

		m_ObjectArray = new ArrayList<BBS_Content_Object>();
		m_Adapter = new Shop_Detail_QNA_List_Adapter(this, R.layout.oppa_qna, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		 Morebtn = (Button)findViewById(R.id.bbs_main_list_more_123);
	     Morebtn.setOnClickListener(new MoreBTNClick());
		
        Button QNABtn = (Button)findViewById(R.id.shop_detail_title_bar_qna);
        QNABtn.setOnClickListener(new MoreBTNClick());
        

		{

	        ImageButton MainBtn2 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_1);
	        MainBtn2.setOnClickListener(new MoreBTNClick());
	        ImageButton MainBtn3 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_3);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
	        ImageButton MainBtn4 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_4);
	        MainBtn4.setOnClickListener(new MoreBTNClick());
	    }
		
		{
			Button MainBtn3 = (Button)findViewById(R.id.shop_detail_call_btn);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
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
		
		//setListView();
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
    			
        		
        	case R.id.shop_detail_title_bar_qna:
        	{
        		Intent intent;
        	    intent = new Intent().setClass(self, Shop_Detail_QNA_Write.class);
        	    myApp.m_BBSReplyContent.isReply  = false;
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
            		
            	case R.id.shop_detail_tab_btn_3:
            	{
            		Intent intent;
            	    intent = new Intent().setClass(self, Shop_Detail_After_ViewFlipper.class);
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
    
    public void MoreRefreshUI()
    {
    	mProgress.show();
    	current_page++;
    	MyInfo myApp = (MyInfo) getApplication();

    	
    	if ( current_page > total_page )
    		return; 

    	getQNA();
    }
    
	@Override
	public void RefreshUI()
	{
		m_ObjectArray.clear();
		m_Adapter.clear();
		getQNA();
	}

	
	
    public void RefreshIntroduce( )
    {
    	m_ObjectArray.clear();
    	m_Adapter.clear();
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
    
	

	

    // ListActivity의 메소드인 onListitemClick을 오버라이딩 하여
    // tv에 선택된 item의 text가 표시 되도록 함.
    public void onListItemClick(ListView l, View v, int position, long id) 
    {
    	
    }

    
    void getQNA()
 	{
 		mProgress.show();
 		final String strPage = current_page.toString();
 		final MyInfo myApp = (MyInfo) getApplication();
 		Thread thread = new Thread(new Runnable()
 		{

 			
 			public void run() {


 				Map<String, String> data = new HashMap  <String, String>();
 				data.put("sh_id",myApp.m_CurrShopInfo.ShopID );
 				data.put("return_type", "json");
 				data.put("bo_table", "ask");
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
    
    
    final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:

				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				//후기
				mProgress.dismiss();
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
	
	
	public class Shop_Detail_QNA_List_Adapter extends ArrayAdapter<BBS_Content_Object>
	{

		private Context mContext;
		private int mResource;
		private ArrayList<BBS_Content_Object> mList;
		private LayoutInflater mInflater;
		
		public Shop_Detail_QNA_List_Adapter(Context context, int layoutResource, 
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
					
					
					if ( myApp.m_BBSID.bo_table.equals( myApp.m_AccountData.ID))
					{
						itemAddIcon.setVisibility(View.VISIBLE);
					}
					else
					{
						itemAddIcon.setVisibility(View.GONE);
					}
					// 클릭이벤트 추가     					
					itemAddIcon.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v )
				        {
				        	switch(v.getId())
				        	{
				        	case R.id.img_qna_reply:
				        	{
				        		myApp.m_BBSReplyContent.bo_table = "ask";
								myApp.m_BBSReplyContent.wr_id = item.getWrId().toString();
								myApp.m_BBSReplyContent.isReply = true;
						

								// 답변 달기 화면으로 
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

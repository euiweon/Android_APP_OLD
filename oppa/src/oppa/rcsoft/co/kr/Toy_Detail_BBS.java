package oppa.rcsoft.co.kr;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import oppa.rcsoft.co.kr.MyInfo.MyAccountData;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


// 언니 관련 게시물 클래스 
public class Toy_Detail_BBS extends  BaseActivity implements OnClickListener{
    /** Called when the activity is first created. */
	
	private ArrayList<BBS_Content_Object> m_ObjectArray;
	private BBS_View_Adapter m_Adapter;
	private ListView m_ListView;
	
	
	static Toy_Detail_BBS self;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toy_bbs_viewer);
        
        
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		m_ListView = (ListView)findViewById(R.id.toy_bbs_list_view);

		m_ObjectArray = new ArrayList<BBS_Content_Object>();
		m_Adapter = new BBS_View_Adapter(this, R.layout.toy_bbs_main_ui_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
        Button CallBtn = (Button)findViewById(R.id.toy_bbs_call_btn);
        CallBtn.setOnClickListener(this);
        
        
        {
			Button TabBTN2 = (Button)findViewById(R.id.toy_main_tab_btn_1);
			TabBTN2.setOnClickListener(this);
			Button TabBTN3 = (Button)findViewById(R.id.toy_main_tab_btn_2);
			TabBTN3.setOnClickListener(this);
			Button TabBTN4 = (Button)findViewById(R.id.toy_main_tab_btn_4);
			TabBTN4.setOnClickListener(this);
			Button TabBTN5 = (Button)findViewById(R.id.toy_main_tab_btn_5);
			TabBTN5.setOnClickListener(this);
		}
        
        {
            MyInfo myApp = (MyInfo) getApplication();
            MyAccountData Mydata = myApp.GetAccountData();
            
            if ( Mydata.Level == 2 )
            {
                Button ZZimBtn = (Button)findViewById(R.id.toy_detail_title_bar_zzim);
                ZZimBtn.setOnClickListener(this);
            }
            else
            {
            	Button ZZimBtn = (Button)findViewById(R.id.toy_detail_title_bar_zzim);
            	ZZimBtn.setVisibility(View.GONE);
            }
	
        }
        

        
        RefreshUI();
    }
    
	private void ZzimOneButton()
	{
		mProgress.show();
		final MyInfo myApp = (MyInfo) getApplication();
		Thread thread = new Thread(new Runnable()
		{
			public void run()
			{
				Map<String, String> data = new HashMap  <String, String>();
				data.put("gr_id",myApp.m_CurrGirlInfo.GirlID );
				data.put("return_type", "json");
				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_favoriteGirl.php", data);

				JSONObject json = null;
				try {
					json = new JSONObject(strJSON);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try 
				{
					if(json.getString("result").equals("ok"))
					{
						handler.sendEmptyMessage(8);
					}
					else if(json.getString("result").equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(10,myApp.DecodeString(json.getString("result_text")) ));
					}
				}  catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

			}
		});
		thread.start();
	}
	
    
    
    public void onClick(View v )
    {
    	switch(v.getId())
    	{
    	case R.id.toy_bbs_call_btn:
    	{
    		MyInfo myApp = (MyInfo) getApplication();
    		
    		Intent intent = new Intent(Intent.ACTION_DIAL);
    		intent.setData(Uri.parse("tel:" + myApp.m_SisterInfo.m_TelNumber));
    		startActivity(intent);
    		break;
    	}
    	case R.id.toy_detail_title_bar_zzim:
    	{
    		ZzimOneButton();
    	}
    		break;
    		
    		
    	case R.id.toy_main_tab_btn_1:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Home.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_2:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Shop_MainList.class);
	        startActivity( intent );   
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_4:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Community_Main.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_5:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, MyPage_Main.class);
	        startActivity( intent );  
    	}
    	break;

    	default:
    		break;
    	}
    }
    
    
    
    // 게시물에 덧글을 썼거나 수정을 했을때 갱신이 요구 될때...

    @Override
    public void RefreshUI()
    {
    	
		{
			
			MyInfo myApp = (MyInfo) getApplication();
			
			TextView titleText = (TextView)findViewById(R.id.toy_bbs_name);
			TextView addrText = (TextView)findViewById(R.id.toy_bbs_addr);
			TextView scoreText = (TextView)findViewById(R.id.toy_bbs_score);
			//TextView charText = (TextView)findViewById(R.id.toy_detail_main_char);
			ImageView mainImage = (ImageView)findViewById(R.id.toy_bbs_image_main);
			TextView itemTel = (TextView)findViewById(R.id.toy_bbs_tel);
			TextView itemShopName = (TextView)findViewById(R.id.toy_bbs_shop_name);
			mainImage.setImageBitmap(myApp.m_SisterInfo.m_Bitmap);
			titleText.setText("이름 : "+ myApp.m_SisterInfo.m_Title + "  (키)  " + myApp.m_SisterInfo.m_Ki + "cm    (몸무게)"+ myApp.m_SisterInfo.m_Weight + "kg");
			addrText.setText(" 주소 : " + myApp.m_SisterInfo.m_Addr);
			itemTel.setText("전화 : " + myApp.m_SisterInfo.m_TelNumber );
			scoreText.setText("찜횟수 : " + myApp.m_SisterInfo.m_Score + "회"  + "     /    거리  : " + myApp.m_SisterInfo.m_Distance);
			//charText.setText("성격 : " + myApp.m_SisterInfo.m_hams );
			
			itemShopName.setText( "소속 : " + myApp.m_SisterInfo.m_ShopName);
		}
		
    	m_Adapter.clear();
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();

    	
    	
    	{
			TextView titleText = (TextView)findViewById(R.id.toy_bbs_name);
			TextView addrText = (TextView)findViewById(R.id.toy_bbs_addr);
			TextView scoreText = (TextView)findViewById(R.id.toy_bbs_score);
			//TextView charText = (TextView)findViewById(R.id.toy_detail_main_char);
			ImageView mainImage = (ImageView)findViewById(R.id.toy_bbs_image_main);
			TextView itemTel = (TextView)findViewById(R.id.toy_bbs_tel);
			TextView itemShopName = (TextView)findViewById(R.id.toy_bbs_shop_name);
			mainImage.setImageBitmap(myApp.m_SisterInfo.m_Bitmap);
			titleText.setText("이름 : "+ myApp.m_SisterInfo.m_Title + "( " + myApp.m_SisterInfo.m_Ki + "cm / "+ myApp.m_SisterInfo.m_Weight + "kg )\n"
					 + "성격 : " + myApp.m_SisterInfo.m_hams );
			addrText.setText("주소 : " + myApp.m_SisterInfo.m_Addr);
			itemTel.setText("전화 : " + myApp.m_SisterInfo.m_TelNumber );
			
			

			if (myApp.mLat == -100000.0 )
			{
				scoreText.setText("찜횟수 : " + myApp.m_SisterInfo.m_Score + "회"   );
			}
			else
			{
				float dis = ((float)(myApp.m_SisterInfo.m_Distance )) /1000.0f;
				scoreText.setText("찜횟수 : " + myApp.m_SisterInfo.m_Score + "회" ) ;
			}
			
			
			
			//charText.setText("성격 : " + myApp.m_SisterInfo.m_hams );
			
			itemShopName.setText( "소속 : " + myApp.m_SisterInfo.m_ShopName);
    	}
    	
    	{
 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					data.put("bo_table",myApp.m_BBSViewerID.bo_table );
					data.put("wr_id", myApp.m_BBSViewerID.wr_id);
					data.put("return_type", "json");

					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_getBoardView.php", data);	
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
							// 본문 부터 리스트에 추가한다. 
							int wr_id = 0;
							{
								BBS_Content_Object item = new BBS_Content_Object();
								item.isMainContent  =true;
								
								item.setWrSubject(myApp.DecodeString(json.getString("wr_subject")));
								item.setWrName(myApp.DecodeString(json.getString("wr_name")));
								item.setWrContent(myApp.DecodeString(json.getString("wr_content")));
								item.setWrDatetime(myApp.DecodeString(json.getString("wr_datetime")));
								item.setWrId(json.getInt("wr_id"));
								item.setWrHit(json.getInt("wr_hit"));
								item.setCommentCnt(json.getInt("comment_cnt"));
								
								item.mb_grade = myApp.DecodeString(json.getString("mb_grade")); 
								
								if ( myApp.m_BBSID.bo_table.equals("hugi"))
								{
									item.setWr6(json.getInt("wr_6"));
								}
								
								// 첨부파일을 여기서 연다
								// 일반 게시판이기에 Pass
								String fileDir = myApp.DecodeString(json.getString("file_dir"));
								JSONArray bitmaplist = (JSONArray)json.get("file_list");
								ArrayList<Bitmap> bitmapArray = null;
								for(int j = 0; j < bitmaplist.length(); j++)
								{
									JSONObject bitmaplist1 = (JSONObject)bitmaplist.get(j);
									bitmapArray = new ArrayList<Bitmap>();
									

									URL imgUrl = new URL(fileDir+ "/" + myApp.DecodeString(bitmaplist1.getString("files") ));
									URLConnection conn = imgUrl.openConnection();
									conn.connect();
									BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
									Bitmap bm = BitmapFactory.decodeStream(bis);
									bis.close();

									bitmapArray.add(bm);
									
								}
								
								if ( bitmapArray != null)
									item.setBitmap(bitmapArray);
								
								wr_id = item.getWrId();
								m_ObjectArray.add(item);
							}
							
							// 댓글 리스트를 입력한다.
							JSONArray usageList = (JSONArray)json.get("comment_list");
							for(int i = 0; i < usageList.length(); i++)
							{
								BBS_Content_Object item = new BBS_Content_Object();
								JSONObject list = (JSONObject)usageList.get(i);
								item.isMainContent  =false;
								
								item.setWrName(myApp.DecodeString(list.getString("cmt_name")));
								item.setWrContent(myApp.DecodeString(list.getString("cmt_content")));
								item.setWrDatetime(myApp.DecodeString(list.getString("cmt_datetime")));
								item.setWrId(wr_id);
								item.cmt_id = list.getInt("cmt_id");
								
								item.mb_grade = myApp.DecodeString(list.getString("cmt_mb_grade")); 
								
								// 댓글에 댓글이 달린지 판단하는 여부 
								if ( list.getInt("cmt_reply_len") > 0  )
								{
									item.isOverLapReply = true;
								}
								else
								{
									item.isOverLapReply = false;
								}
								m_ObjectArray.add(item);
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
					} catch (MalformedURLException e) {
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
			}
				m_Adapter.notifyDataSetChanged();
				break;
			case 1:
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				break;
			case 3:
				message = "데이터가 없습니다";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 8 :
				mProgress.dismiss();
				self.ShowAlertDialLog( self , "찜 완료", "찜이 완료 되었습니다." );
				break;	
			default:
				//	message = "실패하였습니다.";
  				mProgress.dismiss();
  				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
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
    		final BBS_Content_Object tweet = mList.get(position);
    		
    		final MyInfo myApp = (MyInfo) getApplication();

    		if(convertView == null)
    		{
    			convertView = mInflater.inflate(mResource, null);
    		}

    		if(tweet != null) 
    		{
    			// 입력된 정보를 바탕으로 Visible Gone 시킨다음
    			// 데이터를 입력해준다
    			// 이미지 리사이징도 해줘야 한다. 
    			
    			
    			// 게시물 헤더 1
    			LinearLayout mainTitle1  = (LinearLayout)convertView.findViewById(R.id.bbs_main_header1);
    			// 게시물 헤더 2
    			LinearLayout mainTitle2  = (LinearLayout)convertView.findViewById(R.id.bbs_main_header2);
    			// 게시물 이미지
    			LinearLayout mainImage = (LinearLayout)convertView.findViewById(R.id.bbs_main_image_layout);
    			// 게시물 텍스트
    			LinearLayout mainContent  = (LinearLayout)convertView.findViewById(R.id.bbs_main_content_1);
    			// 댓글 헤더
    			LinearLayout subContent  = (LinearLayout)convertView.findViewById(R.id.bbs_sub_title);
    			// 댓글 텍스트
    			LinearLayout subContent1 = (LinearLayout)convertView.findViewById(R.id.bbs_sub_content_2);


    			// 타입이 본문 타입인지 체크한다. 
    			if ( tweet.isMainContent )
    			{
    				
        			mainTitle1.setVisibility(View.GONE);
        			mainTitle2.setVisibility(View.VISIBLE);
        				
        			// 헤더 부분
        			{
            			// 제목
                		TextView itemTitle = (TextView)convertView.findViewById(R.id.bbs_main_content_title);
                		itemTitle.setText(tweet.getWrSubject());
                			
                		// 리플 카운트 
                		TextView itemReply = (TextView)convertView.findViewById(R.id.bbs_main_content_reply_count);
                		itemReply.setText("("+tweet.getCommentCnt() + ")");
                			
                		// 작성 시각
                		TextView itemTime = (TextView)convertView.findViewById(R.id.bbs_title_writer_2);
                		itemTime.setText( tweet.getWrName()+ " "+ tweet.getWrDatetime());        					
        			}
        			
        			ImageView grade = (ImageView)convertView.findViewById(R.id.bbs_title_writer_2_icon);
        			grade.setBackgroundResource(myApp.GetGradeIcon(tweet.mb_grade));
        			
        			{
        				// Image 내용....
        			}
        				
        			// 본문 내용
        			{
        				TextView itemContent = (TextView)convertView.findViewById(R.id.bbs_main_content_12);
        				itemContent.setText(tweet.getWrContent());   
        			}
        			
        			
					if ( tweet.getBitmap() != null)
					{
						ImageView Image1 = (ImageView)convertView.findViewById(R.id.bbs_title_image_1);
						
						ImageView Image2 = (ImageView)convertView.findViewById(R.id.bbs_title_image_2);
						
						ImageView Image3 = (ImageView)convertView.findViewById(R.id.bbs_title_image_3);
						
						if ( tweet.getBitmap().size() == 1 )
						{
							Image1.setImageBitmap(tweet.getBitmap().get(0));
							Image1.setVisibility(View.VISIBLE);
							Image2.setVisibility(View.GONE);
							Image3.setVisibility(View.GONE);
						}
						else if ( tweet.getBitmap().size() == 2  )
						{
							Image1.setImageBitmap(tweet.getBitmap().get(0));
							Image2.setImageBitmap(tweet.getBitmap().get(1));
							Image1.setVisibility(View.VISIBLE);
							Image2.setVisibility(View.VISIBLE);
							Image3.setVisibility(View.GONE);
						}
						else if ( tweet.getBitmap().size() == 3 )
						{
							Image1.setImageBitmap(tweet.getBitmap().get(0));
							Image2.setImageBitmap(tweet.getBitmap().get(1));
							Image3.setImageBitmap(tweet.getBitmap().get(2));
							Image1.setVisibility(View.VISIBLE);
							Image2.setVisibility(View.VISIBLE);
							Image3.setVisibility(View.VISIBLE);
						}
						
					}
					else
					{
						ImageView Image1 = (ImageView)convertView.findViewById(R.id.bbs_title_image_1);
						Image1.setVisibility(View.GONE);
						
						ImageView Image2 = (ImageView)convertView.findViewById(R.id.bbs_title_image_2);
						Image2.setVisibility(View.GONE);
						
						ImageView Image3 = (ImageView)convertView.findViewById(R.id.bbs_title_image_3);
						Image3.setVisibility(View.GONE);

					}
					
        			
					ImageView itemAddIcon = (ImageView)convertView.findViewById(R.id.bbs_main_reply_add);
					itemAddIcon.setVisibility(View.GONE);
					
					ImageView itemReply = (ImageView)convertView.findViewById(R.id.bbs_main_title_add);
					itemReply.setVisibility(View.VISIBLE);
        			// 클릭이벤트 추가     					
					itemReply.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v )
				        {
				        	switch(v.getId())
				        	{
				        	case R.id.bbs_main_title_add:
				        	{
				        		myApp.m_BBSReplyContent.bo_table = "play";
								myApp.m_BBSReplyContent.wr_id = tweet.getWrId().toString();
								myApp.m_BBSReplyContent.isReply = false;
								
								myApp.m_BBSReplyContent.isWriteType =true;
								
								
								

								Intent intent;
						        intent = new Intent().setClass(self, Toy_Reply.class);
						        startActivity( intent ); 
								/*Sister_Toy_Main_Activity parentActi = (Sister_Toy_Main_Activity)getParent();
								
								parentActi.ToyAfterReplyWrite();
								parentActi.setContentView(BaseActivityGroup.CHILD_THREE);
								*/
				        	}
				        		break;
				        	}
				        }
					});
					
					
					{
    					ImageView itemAddSingo = (ImageView)convertView.findViewById(R.id.bbs_title_singo_2);
    					itemAddSingo.setVisibility(View.VISIBLE);
    					
    					// 클릭이벤트 추가     					
    					itemAddSingo.setOnClickListener(new OnClickListener()
    					{
    						public void onClick(View v )
    				        {
    				        	switch(v.getId())
    				        	{
    				        	case R.id.bbs_title_singo_2:
    				        	{
    				        		myApp.m_BBSSingo.bo_table = "play";
    								myApp.m_BBSSingo.wr_id = tweet.getWrId().toString();
    								myApp.m_BBSSingo.isReply = false;
    								
    								
    								Intent intent;
    						        intent = new Intent().setClass(self, Toy_Detail_Singo.class);
    						        startActivity( intent );  
    								
    				        	}
    				        		break;
    				        	}
    				        }
    					});
					}
					
        			
    				mainContent.setVisibility(View.VISIBLE);
    				mainImage.setVisibility(View.VISIBLE);
    				subContent.setVisibility(View.GONE);
    				subContent1.setVisibility(View.GONE);

    			}
    			else 
    			{
    				mainTitle1.setVisibility(View.GONE);
    				mainTitle2.setVisibility(View.GONE);
    				mainImage.setVisibility(View.GONE);
    				mainContent.setVisibility(View.GONE);
    				subContent.setVisibility(View.VISIBLE);
    				subContent1.setVisibility(View.VISIBLE);
    				
    				ImageView itemAddIcon = (ImageView)convertView.findViewById(R.id.bbs_sub_reply_add);
    				// 댓글에 댓글 아이콘 
    				if ( tweet.isOverLapReply)
    				{
    					ImageView replIcon = (ImageView)convertView.findViewById(R.id.bbs_sub_reply_icon);
    					replIcon.setVisibility(View.VISIBLE);
    					
    					itemAddIcon.setVisibility(View.GONE);
    				}
    				else
    				{
    					ImageView replIcon = (ImageView)convertView.findViewById(R.id.bbs_sub_reply_icon);
    					replIcon.setVisibility(View.GONE);
    					
    					itemAddIcon.setVisibility(View.VISIBLE);
    				}
    				
    				// 작성 시각
        			TextView itemTime = (TextView)convertView.findViewById(R.id.bbs_main_content_date);
        			itemTime.setText(  tweet.getWrDatetime());  
        			
        			// 작성자
        			TextView itemName = (TextView)convertView.findViewById(R.id.bbs_main_content_writer);
        			itemName.setText( tweet.getWrName());  
    				
    				// 본문 내용
    				{
    					TextView itemContent = (TextView)convertView.findViewById(R.id.bbs_main_content_title123);
    					itemContent.setText(tweet.getWrContent());   
    				}
    				
					
					
					
					ImageView grade = (ImageView)convertView.findViewById(R.id.bbs_main_content_writer_icon);
        			grade.setBackgroundResource(myApp.GetGradeIcon(tweet.mb_grade));
					
					// 이미지 추가 
					
					
					
					
					// 클릭이벤트 추가     					
					itemAddIcon.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v )
				        {
				        	switch(v.getId())
				        	{
				        	case R.id.bbs_sub_reply_add:
				        	{
				        		myApp.m_BBSReplyContent.bo_table = "play";
								myApp.m_BBSReplyContent.wr_id = tweet.getWrId().toString();
								myApp.m_BBSReplyContent.isReply = true;
								myApp.m_BBSReplyContent.re_id = tweet.cmt_id.toString();
								myApp.m_BBSReplyContent.isWriteType =true;
								
								Intent intent;
						        intent = new Intent().setClass(self, Toy_Reply.class);
						        startActivity( intent ); 
								

				        	}
				        		break;
				        	}
				        }
					});
					
					
    			}
    		}
    		return convertView;
    	}
    }
}

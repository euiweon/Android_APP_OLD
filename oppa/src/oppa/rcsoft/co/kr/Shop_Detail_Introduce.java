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
import oppa.rcsoft.co.kr.Shop_MainList.MoreBTNClick;

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
import android.util.Log;
import android.view.KeyEvent;
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




// Shop 업소 소개 클래스
public class Shop_Detail_Introduce extends BaseActivity{

	public static Shop_Detail_Introduce self; 
    private ArrayList<BBS_Content_Object> m_ObjectArray;
	private Shop_Detail_Intro_List_Adapter m_Adapter;
	private ListView m_ListView;

	
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

	
	String m_GirlID;
	String m_TelNumber2 = "01012341234";
	String m_Title2;
	String m_Addr2;
	String m_Score2;
	String m_Ki2;
	String m_Weight2;
	String m_hams2;
	String m_ShopName2;
	Integer m_Distance2;
	Bitmap m_Bitmap2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.shop_detail_intro);
        
		m_ListView = (ListView)findViewById(R.id.shop_detail_introduce_listview);

		m_ObjectArray = new ArrayList<BBS_Content_Object>();
		m_Adapter = new Shop_Detail_Intro_List_Adapter(this, R.layout.shop_detail_introduce_list_view_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		{

	        ImageButton MainBtn2 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_2);
	        MainBtn2.setOnClickListener(new MoreBTNClick());
	        ImageButton MainBtn3 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_3);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
	        ImageButton MainBtn4 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_4);
	        MainBtn4.setOnClickListener(new MoreBTNClick());
	    }
		
		{
			Button MainBtn3 = (Button)findViewById(R.id.shop_detail_call_btn);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
	        {
	            MyInfo myApp = (MyInfo) getApplication();
	            MyAccountData Mydata = myApp.GetAccountData();
	            
	            if ( Mydata.Level == 2 )
	            {
	    			Button MainBtn4 = (Button)findViewById(R.id.shop_detail_title_bar_zzim);
	    	        MainBtn4.setVisibility(View.VISIBLE);
	            }
	            else
	            {
	    			Button MainBtn4 = (Button)findViewById(R.id.shop_detail_title_bar_zzim);
	    			 MainBtn4.setVisibility(View.INVISIBLE);
	            }
	        }

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
		
       	
		RefreshIntroduce( );
		//setListView();
		
		
    }
    
    @Override
    public void onBackPressed() 
    {
		
        Intent intent;
        intent = new Intent().setClass(self, Shop_MainList.class);
        startActivity( intent );  
    }
    
    
    @Override
    public void onPause()
    {
    	super.onPause();
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    }
    
    
    public  class MoreBTNClick implements OnClickListener
    {

		public void onClick(View v )
        {
        	switch(v.getId())
        	{
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
        		
        	case R.id.shop_detail_title_bar_zzim:
        	{
        		ZzimOneButton();
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

    
	private void ZzimOneButton()
	{
		mProgress.show();
		final MyInfo myApp = (MyInfo) getApplication();
		Thread thread = new Thread(new Runnable()
		{
			public void run()
			{
				Map<String, String> data = new HashMap  <String, String>();
				data.put("sh_id",myApp.m_CurrShopInfo.ShopID );
				data.put("return_type", "json");
				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_favoriteShop.php", data);

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
				}

				
				// 제목 
				{
					BBS_Content_Object item = new BBS_Content_Object();
					item.isMainContent = true;
					item.setWrContent(myApp.DecodeString(json.getString("sh_profile")));
					m_ObjectArray.add(item);
				}

				String temp123122= json.getString("list");
				if ( json.getString("list")  != "null" )
				{
					// 리스트 
					JSONArray usageList = (JSONArray)json.get("list");
					for(int i = 0; i < usageList.length(); i++)
					{
						BBS_Content_Object item = new BBS_Content_Object();
						JSONObject list = (JSONObject)usageList.get(i);
						item.isMainContent = false;
						item.setMbId(myApp.DecodeString(list.getString("gr_id")));
						item.setCaName(myApp.DecodeString(list.getString("gr_name")));
						item.setWrContent(myApp.DecodeString(list.getString("gr_profile")));
						item.setReplyLen(list.getInt("gr_weight"));
						item.setWrHit(list.getInt("gr_height"));							
						String temp = "";
						String temp2 = myApp.DecodeString(list.getString("gr_img"));
						
						if ( !temp2.equals(temp) )
						{
							URL imgUrl = new URL(myApp.DecodeString(list.getString("gr_img")));
							URLConnection conn = imgUrl.openConnection();
							conn.connect();
							BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
							item.th_img = BitmapFactory.decodeStream(bis);
							bis.close();
						}
						m_ObjectArray.add(item);
					}
					
					handler.sendEmptyMessage(0);	
					
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
    
	// 다른 스레드과 동기화를 위한 핸들러 처리 작업 
	final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:
			{
				mProgress.dismiss();
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
				m_Adapter.notifyDataSetChanged();
				
			}
				
				break;
			case 1:
				break;
			case 2:
				break;
			case 4:
				//오늘의 뉴스 
				mProgress.dismiss();
				break;
			case 5:
				//후기 댓글 
				mProgress.dismiss();
				break;
				
			case 8:
			{
				mProgress.dismiss();
				// 일단 찜이 제대로 되었는지 확인한다. 
				// 세가지의 경우에 따라 처리한다.
				// 1. 정상적으로 등록 되었을 경우
				// 2. 이미 등록이 되어 있었을 경우
				// 3. 원인 모를 문제로 등록이 안될 경우 
				
				// 다이얼로그를 생성 
				new AlertDialog.Builder(self)

			
				.setMessage("제대로 찜이 되었습니다.")
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				})
				.show();
			}
				break;
				
			case 20:
			{
				
				MyInfo myApp = (MyInfo) getApplication();
				myApp.m_SisterInfo.m_Addr = m_Addr2;
				myApp.m_SisterInfo.m_Bitmap = m_Bitmap2;
				myApp.m_SisterInfo.m_Title = m_Title2;
				myApp.m_SisterInfo.m_Ki = m_Ki2;
				myApp.m_SisterInfo.m_Weight = m_Weight2;
				myApp.m_SisterInfo.m_TelNumber = m_TelNumber2;
				myApp.m_SisterInfo.m_Score = m_Score2;
				myApp.m_SisterInfo.m_hams = m_hams2;
				myApp.m_SisterInfo.m_ShopName = m_ShopName2;
				myApp.m_SisterInfo.m_Distance = m_Distance2;
				
				
				
				
	    		Intent intent;
		        intent = new Intent().setClass(self, Toy_Detail_List.class);
		        startActivity( intent );  
			}
				break;
			default:
				//	message = "실패하였습니다."
				mProgress.dismiss();
				self.ShowAlertDialLog( self,"에러" , (String) msg.obj );
				break;
			}
		}
	};
	
	public void getGirlIntroduce()
	{
		if ( mProgress.isShowing() )
			return ;
			mProgress.show();
			final MyInfo myApp = (MyInfo) getApplication();
			Thread thread = new Thread(new Runnable()
			{

				
				public void run()
				{


					
					Map<String, String> data = new HashMap  <String, String>();
					data.put("gr_id",myApp.m_CurrGirlInfo.GirlID );
					data.put("return_type", "json");
				
					data.put("geoy", myApp.mLat.toString());
					data.put("geox", myApp.mLon.toString());
					
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getGirlInfo.php", data);

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
							m_GirlID = myApp.DecodeString(json.getString("gr_id"));
							m_Title2 = myApp.DecodeString(json.getString("gr_name"));
							m_TelNumber2 = myApp.DecodeString(json.getString("sh_tel"));
							m_Addr2= myApp.DecodeString(json.getString("sh_addr"));
							
							String temp = "";
							String temp2 = json.getString("gr_img");
							
							if ( !temp2.equals(temp) )
							{
								URL imgUrl = new URL(myApp.DecodeString(json.getString("gr_img")));
								URLConnection conn = imgUrl.openConnection();
								conn.connect();
								BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
								m_Bitmap2 = BitmapFactory.decodeStream(bis);
								bis.close();
							}
							
							
							m_ShopName2 = myApp.DecodeString(json.getString("sh_name"));
							m_Score2 = myApp.DecodeString(json.getString("gr_favorite_cnt"));
							m_Ki2 = myApp.DecodeString(json.getString("gr_height"));
							m_Weight2 = myApp.DecodeString(json.getString("gr_weight"));
							m_hams2 = myApp.DecodeString(json.getString("gr_character"));
							
							m_Distance2  = json.getInt("distance") ; 
							handler.sendEmptyMessage(20);
						}
						else if(json.getString("result").equals("error"))
						{
							handler.sendMessage(handler.obtainMessage(2,myApp.DecodeString(json.getString("result_text")) ));
						}
					}  catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(2,"JSON Parsing Error" ));
					} 
					catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			thread.start();
	}
    

	
	public class Shop_Detail_Intro_List_Adapter extends ArrayAdapter<BBS_Content_Object>
	{

		private Context mContext;
		private int mResource;
		private ArrayList<BBS_Content_Object> mList;
		private LayoutInflater mInflater;
		
		public Shop_Detail_Intro_List_Adapter(Context context, int layoutResource, 
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

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(tweet != null) 
			{

    			// 게시물 헤더 1
    			LinearLayout mainTitle1  = (LinearLayout)convertView.findViewById(R.id.shop_intro_1);
    			// 게시물 헤더 2
    			LinearLayout mainTitle2  = (LinearLayout)convertView.findViewById(R.id.shop_intro_2);
    			
    			if ( tweet.isMainContent )
    			{
    				mainTitle1.setVisibility(View.GONE);
    				mainTitle2.setVisibility(View.VISIBLE);
    				
    				TextView  title = (TextView)convertView.findViewById(R.id.shop_intro_text);
    				title.setText(tweet.getWrContent());
    			}
    			else
    			{
    				mainTitle1.setVisibility(View.VISIBLE);
    				mainTitle2.setVisibility(View.GONE);
    				
    				ImageView image = (ImageView)convertView.findViewById(R.id.shop_intro_image);
    				image.setImageBitmap(tweet.th_img);
    				
    				
    				TextView  txtName = (TextView)convertView.findViewById(R.id.shop_intro_name);
    				txtName.setText("이름  :  " + tweet.getCaName());
    				
    				TextView  txtBody = (TextView)convertView.findViewById(R.id.shop_intro_body);
    				txtBody.setText(" 키 : " + tweet.getWrHit().toString() + "cm   /  몸무게 : " + tweet.getReplyLen().toString() + "kg"  );
    				
    				TextView  txtContent = (TextView)convertView.findViewById(R.id.shop_intro_content);
    				txtContent.setText(" 성격 : "+ tweet.getWrContent());
    				
    				mainTitle1.setOnClickListener(new View.OnClickListener() 
    				{
    					
    					
    					public void onClick(View v) 
    					{

    						
    						// 다음 액티비티로 이동하고 ShopID를 전역변수에 저장한다. 
    						MyInfo myApp = (MyInfo) getApplication();
    						myApp.m_CurrGirlInfo.GirlID = tweet.getMbId();
    						myApp.m_CurrGirlInfo.forceIn = true;

    						getGirlIntroduce();
    					}
    				});

    				
    			}
    			
			}

			return convertView;
		}
	}
}

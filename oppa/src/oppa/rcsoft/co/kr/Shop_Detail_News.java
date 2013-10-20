package oppa.rcsoft.co.kr;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import oppa.rcsoft.co.kr.Shop_Detail_QNA.MoreBTNClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

// Shop 업소 정보 새소식

public class Shop_Detail_News extends BaseActivity{

	public static Shop_Detail_News self; 
	
	
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
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shop_detail_news);
        
        self = this;
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
     
		{

	        ImageButton MainBtn2 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_1);
	        MainBtn2.setOnClickListener(new MoreBTNClick());
	        ImageButton MainBtn3 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_2);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
	        ImageButton MainBtn4 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_3);
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
            		
            	case R.id.shop_detail_tab_btn_2:
            	{
            		Intent intent;
            	    intent = new Intent().setClass(self, Shop_Detail_QNA.class);
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
    
    
    @Override
    public void RefreshUI()
    {
    	getNews();
    }
    
	void getNews()
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
				data.put("bo_table", "news");
				data.put("shop", "shop");
				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_getBoardList.php", data);

				JSONObject json = null;
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

						if ( json.getInt("total_count") == 0 )
						{
							handler.sendMessage(handler.obtainMessage(0,"뉴스가 없습니다. " ));		
						}
						else
						{
							JSONArray usageList = (JSONArray)json.get("list");
							JSONObject list = (JSONObject)usageList.get(0);
							
							handler.sendMessage(handler.obtainMessage(0,myApp.DecodeString(list.getString("wr_content")) ));							
						}

					}
					else if(json.getString("result").equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(6,myApp.DecodeString(json.getString("result_text")) ));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
				mProgress.dismiss();
				TextView newsText = (TextView)findViewById(R.id.shop_detail_news_text);
				newsText.setText( (String) msg.obj );
				break; 
			case 6:
				// 찜 완료
				mProgress.dismiss();
				self.ShowAlertDialLog( self.getParent() ,"에러" , (String) msg.obj );
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

				

			}
		}
	};
}

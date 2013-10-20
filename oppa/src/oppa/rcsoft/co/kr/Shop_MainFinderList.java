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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

// 업소 검색후 리스트 
public class Shop_MainFinderList extends BaseActivity 
{    

    
    private ArrayList<Shop_Main_List_Object> m_ObjectArray;
	private Shop_Main_List_Adapter2 m_Adapter;
	private ListView m_ListView;

	Shop_MainFinderList self;
	
	int total_count;
	int total_page = 0;
	int current_count;
	Integer current_page = 1;
	String currCategory = "finder";
	
	Intent listIntent;
	
	Button imgBtnSeeMore;
	
	
	int  current_Btn  = 0; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_info_main2);
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
        
		
		
		m_ListView = (ListView)findViewById(R.id.shop_main2_list_view);

		m_ObjectArray = new ArrayList<Shop_Main_List_Object>();
		m_Adapter = new Shop_Main_List_Adapter2(this, R.layout.shop_info_list_view_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
		
		imgBtnSeeMore= (Button)findViewById(R.id.shop_main2_list_more_123);
		imgBtnSeeMore.setOnClickListener(new MoreBTNClick());


		
		{
			Button MainBtn1 = (Button)findViewById(R.id.shop_main2_tab_btn_1);
	        MainBtn1.setOnClickListener(new MoreBTNClick());
	        Button MainBtn2 = (Button)findViewById(R.id.shop_main2_tab_btn_3);
	        MainBtn2.setOnClickListener(new MoreBTNClick());
	        Button MainBtn3 = (Button)findViewById(R.id.shop_main2_tab_btn_4);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
	        Button MainBtn4 = (Button)findViewById(R.id.shop_main2_tab_btn_5);
	        MainBtn4.setOnClickListener(new MoreBTNClick());
	    }
		RefreshUI();
    }
    
    public  class MoreBTNClick implements OnClickListener
    {

		public void onClick(View v )
        {
        	switch(v.getId())
        	{
           	case R.id.shop_main_list_more_123:
        		MoreRefreshUI();
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
    			

        	}
        }
    }
    
    
    @Override
    public void RefreshUI()
    {
    	m_ObjectArray.clear();
    	m_Adapter.notifyDataSetChanged();
    	
    	current_page = 1;
    	shopList(currCategory, current_page);
    }
    
    public void MoreRefreshUI()
    {
    	current_page++;
    	shopList(currCategory, current_page);
    }
   
	

    
    
    public void RefreshFind()
    {
    	current_page = 1;
    	shopList(currCategory, current_page);
    }
    
    

    
	public void shopList(String str_input, Integer current_page)
	{

		if ( mProgress.isShowing() == true )
			return;
		
		mProgress.show();

		final MyInfo myApp = (MyInfo) getApplication();
		final String strInput = "distance";
		final String strPage = current_page.toString();
		Thread thread = new Thread(new Runnable()
		{
			public void run() 
			{
				int kind = myApp.GetShopKind();
				Map<String, String> data = new HashMap  <String, String>();

				data.put("page",strPage );
				data.put("order",strInput);
				data.put("return_type", "json");
				
				data.put("sh_name", myApp.m_ShopFindInfo.Name);
				
				// 1차 카테고리
				if (kind < 6)
					data.put("c1_idx", Integer.toString(kind) );
				
				// 2차카테고리 
				data.put("c2_idx", myApp.m_ShopFindInfo.Category);
				
				data.put("a1_name", myApp.m_ShopFindInfo.Location);
				data.put("dist", myApp.m_ShopFindInfo.Distance);
				data.put("geoy",myApp.mLat.toString());
				data.put("geox", myApp.mLon.toString());
				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getShopList.php", data);
				
				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("result").equals("ok"))
					{
						total_count = json.getInt("total_count");
						total_page =  json.getInt("total_page");
						if ( total_count != 0 )
						{
							JSONArray usageList = (JSONArray)json.get("list");

							for(int i = 0; i < usageList.length(); i++)
							{
								Shop_Main_List_Object item = new Shop_Main_List_Object();
								JSONObject list = (JSONObject)usageList.get(i);
								item.setShId(myApp.DecodeString(list.getString("sh_id")));
								item.setShName(myApp.DecodeString(list.getString("sh_name")));
								item.setC1Name(myApp.DecodeString(list.getString("c1_name")));
								item.setC2Name(myApp.DecodeString(list.getString("c2_name")));
								
								
								item.setShAddress(myApp.DecodeString(list.getString("sh_addr")));
								item.setShEvalPoint(myApp.DecodeString(list.getString("sh_eval_point")));
								
								String temp = "";
								String temp2 = list.getString("sh_img");
								
								if ( !temp2.equals(temp) )
								{
									URL imgUrl = new URL(myApp.DecodeString(list.getString("sh_img")));
									URLConnection conn = imgUrl.openConnection();
									conn.connect();
									BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
									Bitmap bm = BitmapFactory.decodeStream(bis);
									bis.close();
									
									item.setShImg(bm);	
								
								}
										
								item.setDistance(list.getInt("distance"));
								m_ObjectArray.add(item);

							}
							handler.sendEmptyMessage(0);								
						}
						else
						{
							handler.sendEmptyMessage(3);
						}

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
	
	final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{

			String message = null;
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				//company list를 가져옴.
				//				message = "등록이 완료되었습니다.";
				
				
				// 더보기 버튼...
				if( current_page < total_page)
				{
					imgBtnSeeMore.setVisibility(View.VISIBLE);
				}
				else 
				{
					imgBtnSeeMore.setVisibility(View.GONE);
				}
				
				m_Adapter.notifyDataSetChanged();
				break;
			case 1:
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				imgBtnSeeMore.setVisibility(View.GONE);
				break;
			case 2:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			case 3:
				message = "데이터가 없습니다";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				imgBtnSeeMore.setVisibility(View.GONE);
				break;
			case 10:
			{
	    		Intent intent;
		        intent = new Intent().setClass(self, Shop_Detail_Introduce.class);
		        startActivity( intent );  	
			}

				break;
			default:
				//	message = "실패하였습니다.";

				break;
			}

			//			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
		}
	};
	
	
    public void onBackPressed() 
    {
		
        Intent intent;
        intent = new Intent().setClass(self, Home.class);
        startActivity( intent );  
    }
	
    public void getIntroduce()
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
				
				data.put("geoy", myApp.mLat.toString());
				data.put("geox", myApp.mLon.toString());
				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getShopInfo.php", data);

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
						myApp.m_CurrShopInfo.IntroMessage = strJSON;
						handler.sendEmptyMessage(10);
					}
					else if(json.getString("result").equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(6,myApp.DecodeString(json.getString("result_text")) ));
					}
				}  catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

			}
		});
		thread.start();
    }
    
	
	
	public class Shop_Main_List_Adapter2 extends ArrayAdapter<Shop_Main_List_Object>
	{

		private Activity mContext;
		private int mResource;
		private ArrayList<Shop_Main_List_Object> mList;
		private LayoutInflater mInflater;
		
		public Shop_Main_List_Adapter2(Activity context, int layoutResource, ArrayList<Shop_Main_List_Object> objects)
		{
			super(context, layoutResource, objects);
			this.mContext = context;
			this.mResource = layoutResource;
			this.mList = objects;
			this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		

		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			

			if(convertView == null)
			{
				mInflater = mContext.getLayoutInflater();
				convertView = mInflater.inflate(mResource, null);
			}

			final Shop_Main_List_Object mBar = mList.get(position);
			
			if(mBar != null)
			{
				ImageView itemPic = (ImageView)convertView.findViewById(R.id.shop_info_list_view_row_image);
				TextView itemName = (TextView)convertView.findViewById(R.id.shop_info_list_view_row_title);
				TextView itemAdd = (TextView)convertView.findViewById(R.id.shop_info_list_view_row_addr);
				TextView itemDist = (TextView)convertView.findViewById(R.id.shop_info_list_view_row_distance);
				TextView itemCate = (TextView)convertView.findViewById(R.id.shop_info_list_view_row_cate);
				ImageView star1 = (ImageView)convertView.findViewById(R.id.shop_info_main_score1);
				ImageView star2 = (ImageView)convertView.findViewById(R.id.shop_info_main_score2);
				ImageView star3 = (ImageView)convertView.findViewById(R.id.shop_info_main_score3);
				ImageView star4 = (ImageView)convertView.findViewById(R.id.shop_info_main_score4);
				ImageView star5 = (ImageView)convertView.findViewById(R.id.shop_info_main_score5);
	
				
				
				TextView itemReply = (TextView)convertView.findViewById(R.id.shop_main_content_reply_count);
				

				LinearLayout frameBar = (LinearLayout)convertView.findViewById(R.id.shop_info_list_view_main);
				
				itemPic.setImageBitmap(mBar.getShImg());
				itemName.setText(mBar.getShName());
				itemCate.setText("["+mBar.getC2Name()+"]");
				itemAdd.setText(mBar.getShAddress());
				itemReply.setText("("+mBar.getEvalCnt()+")");

				
				final MyInfo myApp = (MyInfo) getApplication();
				if (myApp.mLat == -100000.0 )
				{
					itemDist.setText("거리측정불가");
				}
				else
				{
					float dis = ((float)(mBar.getDistance() )) /1000.0f;
					itemDist.setText("거리 : "+dis + "km");
				}
				
				
				switch( Integer.parseInt(mBar.getShEvalPoint()) )
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
				
				
				mBar.setHeight(frameBar.getHeight());
				
				mList.set(position, mBar);
				
				// 다음 State 넘겨준다. 
				frameBar.setOnClickListener(new View.OnClickListener() 
				{
					
					
					public void onClick(View v) 
					{

						
						// 다음 액티비티로 이동하고 ShopID를 전역변수에 저장한다. 
						MyInfo myApp = (MyInfo) getApplication();
						myApp.m_CurrShopInfo.ShopID = mBar.getShId();
						
						getIntroduce();
						
					}
				});

			}
			return convertView;
		}
	}
     
}
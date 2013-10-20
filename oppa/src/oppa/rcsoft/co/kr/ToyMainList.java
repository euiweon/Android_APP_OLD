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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


// 언니들 리스트 
public class ToyMainList extends BaseActivity implements OnClickListener
{    
	int current_Btn = 0;
	
	private ArrayList<Toy_Main_List_Object> m_ObjectArray;
	private Toy_Main_List_Adapter m_Adapter;
	private ListView m_ListView;
		
	int total_count;
	int current_count;
		
	int total_page;

	Integer current_page = 1;
	String currCategory  = "update";

	Intent listIntent;
	ToyMainList self;
	Button imgBtnSeeMore;

	String m_GirlID;
	String m_TelNumber = "01012341234";
	String m_Title;
	String m_Addr;
	String m_Score;
	String m_Ki;
	String m_Weight;
	String m_hams;
	String m_ShopName;
	Integer m_Distance;
	Bitmap m_Bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.toy_main);          // 인트로 레이아웃 출력  
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		m_ListView = (ListView)findViewById(R.id.toy_mainpage_listview);

		m_ObjectArray = new ArrayList<Toy_Main_List_Object>();
		m_Adapter = new Toy_Main_List_Adapter(this, R.layout.toy_list_view_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
        
        {
        	ImageButton MainBtn1 = (ImageButton)findViewById(R.id.toy_main_tab_btn_1);
            MainBtn1.setOnClickListener(this);
            ImageButton MainBtn2 = (ImageButton)findViewById(R.id.toy_main_tab_btn_2);
            MainBtn2.setOnClickListener(this);
            ImageButton MainBtn3 = (ImageButton)findViewById(R.id.toy_main_tab_btn_3);
            MainBtn3.setOnClickListener(this);
        }
        
        {
			Button TabBTN2 = (Button)findViewById(R.id.toy_main_tab_btn_11);
			TabBTN2.setOnClickListener(this);
			Button TabBTN3 = (Button)findViewById(R.id.toy_main_tab_btn_21);
			TabBTN3.setOnClickListener(this);
			Button TabBTN4 = (Button)findViewById(R.id.toy_main_tab_btn_41);
			TabBTN4.setOnClickListener(this);
			Button TabBTN5 = (Button)findViewById(R.id.toy_main_tab_btn_51);
			TabBTN5.setOnClickListener(this);
		}
        
        
        // 언니 글쓰기 제한. 
        {
            MyInfo myApp = (MyInfo) getApplication();
            MyAccountData Mydata = myApp.GetAccountData();
            
            if ( Mydata.Level == 3 )
            {
                Button WriteBTN = (Button)findViewById(R.id.toy_main_write);
                WriteBTN.setOnClickListener(this);  
            }
            else
            {
            	Button WriteBTN = (Button)findViewById(R.id.toy_main_write);
            	WriteBTN.setVisibility(View.GONE);
            }
	
        }
        
        
        imgBtnSeeMore= (Button)findViewById(R.id.toy_main_list_more_123);
		imgBtnSeeMore.setOnClickListener(this);
		
		RefreshUI();
    }
    
    
    public void onBackPressed() 
    {
		
        Intent intent;
        intent = new Intent().setClass(self, Home.class);
        startActivity( intent );  
    }

	public void onClick(View v) 
	{
		switch(v.getId())
    	{
		case R.id.toy_main_tab_btn_1:
		{
			if ( current_Btn != 0 )
			{
				{
		        	ImageButton MainBtn1 = (ImageButton)findViewById(R.id.toy_main_tab_btn_1);
		            ImageButton MainBtn2 = (ImageButton)findViewById(R.id.toy_main_tab_btn_2);
		            ImageButton MainBtn3 = (ImageButton)findViewById(R.id.toy_main_tab_btn_3);
		            
		            MainBtn1.setBackgroundResource(R.drawable.toy_main_tab_1);
		            MainBtn2.setBackgroundResource(R.drawable.toy_main_tab_2);
		            MainBtn3.setBackgroundResource(R.drawable.toy_main_tab_3);

				}
				ImageButton MainBtn1 = (ImageButton)findViewById(R.id.toy_main_tab_btn_1);
				MainBtn1.setBackgroundResource(R.drawable.toy_main_tab_1on);
				current_Btn = 0;
				currCategory  = "update";
				RefreshUI();
			}
		}
			break;
		case R.id.toy_main_tab_btn_2:
			if ( current_Btn != 1 )
			{
				{
		        	ImageButton MainBtn1 = (ImageButton)findViewById(R.id.toy_main_tab_btn_1);
		            ImageButton MainBtn2 = (ImageButton)findViewById(R.id.toy_main_tab_btn_2);
		            ImageButton MainBtn3 = (ImageButton)findViewById(R.id.toy_main_tab_btn_3);
		            
		            MainBtn1.setBackgroundResource(R.drawable.toy_main_tab_1);
		            MainBtn2.setBackgroundResource(R.drawable.toy_main_tab_2);
		            MainBtn3.setBackgroundResource(R.drawable.toy_main_tab_3);

				}
				ImageButton MainBtn1 = (ImageButton)findViewById(R.id.toy_main_tab_btn_2);
				MainBtn1.setBackgroundResource(R.drawable.toy_main_tab_2on);
				current_Btn = 1;
				currCategory  = "regist";
				RefreshUI();
				
			}
			break;
		case R.id.toy_main_tab_btn_3:
			if ( current_Btn != 2 )
			{
				{
		        	ImageButton MainBtn1 = (ImageButton)findViewById(R.id.toy_main_tab_btn_1);
		            ImageButton MainBtn2 = (ImageButton)findViewById(R.id.toy_main_tab_btn_2);
		            ImageButton MainBtn3 = (ImageButton)findViewById(R.id.toy_main_tab_btn_3);
		            
		            MainBtn1.setBackgroundResource(R.drawable.toy_main_tab_1);
		            MainBtn2.setBackgroundResource(R.drawable.toy_main_tab_2);
		            MainBtn3.setBackgroundResource(R.drawable.toy_main_tab_3);

				}
				ImageButton MainBtn1 = (ImageButton)findViewById(R.id.toy_main_tab_btn_3);
				MainBtn1.setBackgroundResource(R.drawable.toy_main_tab_3on);
				current_Btn = 2;
				currCategory  = "favorite";
				RefreshUI();
			}
			break;
    	case R.id.toy_main_list_content_more:
    		MoreRefreshUI();
    		break;
    		
    	case R.id.toy_main_write:
    	{
    		// 글쓰기
    		Intent intent;
	        intent = new Intent().setClass(self, Toy_Write.class);
	        startActivity( intent ); 
    	}
    		break;
    	case R.id.toy_main_tab_btn_11:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Home.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_21:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Shop_MainList.class);
	        startActivity( intent );   
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_41:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Community_Main.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_51:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, MyPage_Main.class);
	        startActivity( intent );  
    	}
    		break;

			
    	}
	}
    
    
    
	
	
	
	@Override
	public void RefreshUI()
	{
	   	m_ObjectArray.clear();
	   	m_Adapter.notifyDataSetChanged();
	    	
	   	current_page = 1;
	   	SisterList(currCategory, current_page);
	}
	    
	public void MoreRefreshUI()
	{
	   	current_page++;
	   	SisterList(currCategory, current_page);
	}
	
	void SisterList(String str_input, Integer current_page)
	{
	   	if ( mProgress.isShowing() ==true)
	   		return;
		mProgress.show();
		final MyInfo myApp = (MyInfo) getApplication();
		final String strInput = currCategory;
		final String strPage = current_page.toString();
		Thread thread = new Thread(new Runnable()
		{
			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();
				data.put("page",strPage );
				data.put("order",strInput);

				data.put("return_type", "json");
					
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getGirlList.php", data);
					
				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("result").equals("ok"))
					{
						total_count = json.getInt("total_count");
						total_page = json.getInt("total_page");
						JSONArray usageList1 = (JSONArray)json.get("list");

						for(int i = 0; i < usageList1.length(); i++)
						{
							Toy_Main_List_Object item = new Toy_Main_List_Object();
							JSONObject list = (JSONObject)usageList1.get(i);
							item.setGrId(myApp.DecodeString(list.getString("gr_id")));
							item.setGrName(myApp.DecodeString(list.getString("gr_name")));
							
								
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
								item.setImg(bm);
							}
								
							item.setGrProfile(myApp.DecodeString(list.getString("girl_contents")));
							item.setGirlUpdate(myApp.DecodeString(list.getString("girl_update")));
							item.setC1Name(myApp.DecodeString(list.getString("c2_name")));
							item.setShName(myApp.DecodeString(list.getString("sh_name")));
							
							m_ObjectArray.add(item);

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
	
	
	public void getIntroduce()
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
							m_Title = myApp.DecodeString(json.getString("gr_name"));
							m_TelNumber = myApp.DecodeString(json.getString("sh_tel"));
							m_Addr= myApp.DecodeString(json.getString("sh_addr"));
							
							String temp = "";
							String temp2 = json.getString("gr_img");
							
							if ( !temp2.equals(temp) )
							{
								URL imgUrl = new URL(myApp.DecodeString(json.getString("gr_img")));
								URLConnection conn = imgUrl.openConnection();
								conn.connect();
								BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
								m_Bitmap = BitmapFactory.decodeStream(bis);
								bis.close();
							}
							
							
							m_ShopName = myApp.DecodeString(json.getString("sh_name"));
							m_Score = myApp.DecodeString(json.getString("gr_favorite_cnt"));
							m_Ki = myApp.DecodeString(json.getString("gr_height"));
							m_Weight = myApp.DecodeString(json.getString("gr_weight"));
							m_hams = myApp.DecodeString(json.getString("gr_character"));
							
							m_Distance  = json.getInt("distance") ; 
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
				if(current_page < total_page)
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
				break;
			case 2:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
				
			case 20:
			{
				
				MyInfo myApp = (MyInfo) getApplication();
				myApp.m_SisterInfo.m_Addr = m_Addr;
				myApp.m_SisterInfo.m_Bitmap = m_Bitmap;
				myApp.m_SisterInfo.m_Title = m_Title;
				myApp.m_SisterInfo.m_Ki = m_Ki;
				myApp.m_SisterInfo.m_Weight = m_Weight;
				myApp.m_SisterInfo.m_TelNumber = m_TelNumber;
				myApp.m_SisterInfo.m_Score = m_Score;
				myApp.m_SisterInfo.m_hams = m_hams;
				myApp.m_SisterInfo.m_ShopName = m_ShopName;
				myApp.m_SisterInfo.m_Distance = m_Distance;
				
				
				
				
	    		Intent intent;
		        intent = new Intent().setClass(self, Toy_Detail_List.class);
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
		
			
	
	public class Toy_Main_List_Adapter extends ArrayAdapter<Toy_Main_List_Object>
	{

		private Context mContext;
		private int mResource;
		private ArrayList<Toy_Main_List_Object> mList;
		private LayoutInflater mInflater;
		
		public Toy_Main_List_Adapter(Context context, int layoutResource, 
				ArrayList<Toy_Main_List_Object> mTweetList)
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
			final Toy_Main_List_Object mBar = mList.get(position);
			final MyInfo myApp = (MyInfo) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				/*TextView tvText = (TextView) convertView.findViewById(R.id.home_login_mainpage_row_text);
				// 텍스트 세팅
				tvText.setText(tweet.getText());*/
				
				ImageView itemPic = (ImageView)convertView.findViewById(R.id.toy_list_view_row_image);
				TextView itemName = (TextView)convertView.findViewById(R.id.toy_list_view_row_name);
				TextView itemComName = (TextView)convertView.findViewById(R.id.toy_list_view_row_shop);
				TextView itemContents = (TextView)convertView.findViewById(R.id.toy_list_view_row_comment);
				TextView itemUpdate = (TextView)convertView.findViewById(R.id.toy_list_view_row_update);
				//추가적인 아이템 추가시 위와 같이 만들어주면 됨. 단 아이템으로 사용된 xml파일을 수정해주어야 함.
				
				FrameLayout frameBar = (FrameLayout)convertView.findViewById(R.id.toy_list_view_row123);
				
				itemPic.setImageBitmap(mBar.getImg());
				itemName.setText(mBar.getGrName());
				itemComName.setText(mBar.getC1Name()+"/" + mBar.getShName());
				itemContents.setText(mBar.getGrProfile());
				itemUpdate.setText(mBar.getGirlUpdate());
				
				// 다음 State 넘겨준다. 
				frameBar.setOnClickListener(new View.OnClickListener() 
				{
					
					
					public void onClick(View v) 
					{

						
						// 다음 액티비티로 이동하고 ShopID를 전역변수에 저장한다. 
						
						myApp.m_CurrGirlInfo.GirlID = mBar.getGrId();

						getIntroduce();
						//Sister_Toy_Main_Activity parent = (Sister_Toy_Main_Activity) (self.getParent().getParent());
						//parent.setContentView(BaseActivityGroup.CHILD_TWO);
						
						//parent.RefreshShop();
					}
				});
			}

			return convertView;
		}
	}
     
}
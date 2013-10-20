package kr.co.rcsoft.mediatong;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.rcsoft.mediatong.RecruitDetailActivity.RecruitDetail_List_Adapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;



public class RecruitMoreDetailActivity extends BaseActivity implements OnClickListener {

	
	final RecruitMoreData m_RecruitData = new RecruitMoreData();
	
	RecruitMoreDetailActivity self;
	
	private ListView m_ListView;
	
	private Integer m_TabIndex = 0; // TabIndex
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruit_more_detail_layout);  // 인트로 레이아웃 출력     
        self = this;
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		


        
     // 전체 레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_main);
            _AppManager.GetUISizeConverter().ParentRelativeConvertLinearLayout(layout);
        }
        // 헤더레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.recruit_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	ScrollView view = (ScrollView)findViewById(R.id.recruit_detail_scroll_view);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutScrollView(view);
        }
        
        // 이미지및 버튼 리사이징 
        {
            ImageBtnResize(R.id.recruit_back );
            ImageBtnResize2(R.id.recruit_more_detail_desc );
            ImageBtnResize2(R.id.recruit_more_detail_info );
            ImageBtnResize2(R.id.recruit_more_detail_man );
            ImageResize2(R.id.recruit_more_detail_list_row_2_1_image );
            ImageBtnResize2(R.id.recruit_more_detail_list_row_2_2_1_image1 );
            ImageBtnResize2(R.id.recruit_more_detail_list_row_2_2_1_image2 );
            ImageResize2(R.id.recruit_more_detail_list_row_8_1_image );
            ImageResize2(R.id.recruit_more_detail_list_row_8_2_image );
            ImageResize2(R.id.recruit_more_detail_list_row_8_3_image );
            

            {
            	AppManagement _AppManager = (AppManagement) getApplication();
            	LinearLayout layout = (LinearLayout)findViewById(R.id.recruit_more_detail_list_row_3);
            	layout.setOnClickListener(this);
            	
            }
            
        }
        
        GetRecruitMoreDetailData();
        
        SetBtnEvent(R.id.recruit_bottom_home);
        SetBtnEvent(R.id.recruit_bottom_myjob);
        SetBtnEvent(R.id.recruit_bottom_scrap);
        SetBtnEvent(R.id.recruit_bottom_setting);
        
        
    }
    
	public void SetBtnEvent( int id )
	{
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
	}
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    public void ImageResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        
    }
    
    public void ImageBtnResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    public void ImageResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutImage(imageview); 
        
    }
    
    

    
    public void GetRecruitMoreDetailData()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();


					data.put("hr_idx",_AppManager.m_CompanyIndex.toString());
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getHireData.php", data);
					
					
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
							m_RecruitData.hr_idx = json.getInt("hr_idx");
							m_RecruitData.hr_title = _AppManager.GetHttpManager().DecodeString(json.getString("hr_title"));
							m_RecruitData.mb_com_name = _AppManager.GetHttpManager().DecodeString(json.getString("mb_com_name"));
							m_RecruitData.upjong = _AppManager.GetHttpManager().DecodeString(json.getString("upjong"));
							m_RecruitData.part = _AppManager.GetHttpManager().DecodeString(json.getString("part"));
							m_RecruitData.area_name1 = _AppManager.GetHttpManager().DecodeString(json.getString("area_name1"));
							m_RecruitData.area_name2 = _AppManager.GetHttpManager().DecodeString(json.getString("area_name2"));
							m_RecruitData.area_name3 = _AppManager.GetHttpManager().DecodeString(json.getString("area_name3"));
							m_RecruitData.subway = _AppManager.GetHttpManager().DecodeString(json.getString("subway"));
							m_RecruitData.hr_work = _AppManager.GetHttpManager().DecodeString(json.getString("hr_work"));
							m_RecruitData.hr_man_cnt = _AppManager.GetHttpManager().DecodeString(json.getString("hr_man_cnt"));
							m_RecruitData.hr_salary = _AppManager.GetHttpManager().DecodeString(json.getString("hr_salary"));
							m_RecruitData.hr_apply_type = _AppManager.GetHttpManager().DecodeString(json.getString("hr_apply_type"));
							m_RecruitData.hr_carrier_type = _AppManager.GetHttpManager().DecodeString(json.getString("hr_carrier_type"));
							m_RecruitData.hr_degree = _AppManager.GetHttpManager().DecodeString(json.getString("hr_degree"));
							m_RecruitData.hr_sex = _AppManager.GetHttpManager().DecodeString(json.getString("hr_sex"));
							m_RecruitData.hr_prime_name = _AppManager.GetHttpManager().DecodeString(json.getString("hr_prime_name"));
							m_RecruitData.hr_major_name = _AppManager.GetHttpManager().DecodeString(json.getString("hr_major_name"));
							m_RecruitData.ce_name = _AppManager.GetHttpManager().DecodeString(json.getString("ce_name"));
							m_RecruitData.hr_video = _AppManager.GetHttpManager().DecodeString(json.getString("hr_video"));
							m_RecruitData.hr_design = _AppManager.GetHttpManager().DecodeString(json.getString("hr_design"));
							m_RecruitData.hr_office = _AppManager.GetHttpManager().DecodeString(json.getString("hr_office"));
							m_RecruitData.hr_method1 = _AppManager.GetHttpManager().DecodeString(json.getString("hr_method1"));
							m_RecruitData.hr_method2 = _AppManager.GetHttpManager().DecodeString(json.getString("hr_method2"));
							m_RecruitData.hr_method3 = _AppManager.GetHttpManager().DecodeString(json.getString("hr_method3"));
							m_RecruitData.hr_document = _AppManager.GetHttpManager().DecodeString(json.getString("hr_document"));
							m_RecruitData.hr_days = _AppManager.GetHttpManager().DecodeString(json.getString("hr_days"));
							
							
							m_RecruitData.hr_how = _AppManager.GetHttpManager().DecodeString(json.getString("hr_how"));
							m_RecruitData.mb_ceo_name = _AppManager.GetHttpManager().DecodeString(json.getString("mb_ceo_name"));
							m_RecruitData.mb_employee = _AppManager.GetHttpManager().DecodeString(json.getString("mb_employee"));
							m_RecruitData.mb_capital = _AppManager.GetHttpManager().DecodeString(json.getString("mb_capital"));
							m_RecruitData.mb_com_type = _AppManager.GetHttpManager().DecodeString(json.getString("mb_com_type"));
							m_RecruitData.mb_upjong = _AppManager.GetHttpManager().DecodeString(json.getString("mb_upjong"));
							m_RecruitData.mb_com_date = _AppManager.GetHttpManager().DecodeString(json.getString("mb_com_date"));
							m_RecruitData.mb_homepage = _AppManager.GetHttpManager().DecodeString(json.getString("mb_homepage"));
							m_RecruitData.mb_tel = _AppManager.GetHttpManager().DecodeString(json.getString("mb_tel"));
							m_RecruitData.mb_fax = _AppManager.GetHttpManager().DecodeString(json.getString("mb_fax"));
							m_RecruitData.mb_com_info = _AppManager.GetHttpManager().DecodeString(json.getString("mb_com_info"));
							m_RecruitData.mb_com_history = _AppManager.GetHttpManager().DecodeString(json.getString("mb_com_history"));
							m_RecruitData.hr_counter = _AppManager.GetHttpManager().DecodeString(json.getString("hr_counter"));
							m_RecruitData.hr_email = _AppManager.GetHttpManager().DecodeString(json.getString("hr_email"));
							m_RecruitData.hr_tel = _AppManager.GetHttpManager().DecodeString(json.getString("hr_tel"));
							m_RecruitData.hr_addr = _AppManager.GetHttpManager().DecodeString(json.getString("hr_addr"));
							
							m_RecruitData.mb_x_pos = _AppManager.GetHttpManager().DecodeString(json.getString("mb_x_pos"));					
							m_RecruitData.mb_y_pos = _AppManager.GetHttpManager().DecodeString(json.getString("mb_y_pos"));
							m_RecruitData.web_link = _AppManager.GetHttpManager().DecodeString(json.getString("web_link"));
							
							
							
							{
								// 로고 이미지 
								if ( json.getString("logo_img").equals("") )
								{
									
								}
								else
								{
									URL imgUrl = new URL( _AppManager.GetHttpManager().DecodeString(json.getString("logo_img") ));
									URLConnection conn = imgUrl.openConnection();
									conn.connect();
									BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
									m_RecruitData.logo_img = BitmapFactory.decodeStream(bis);
									bis.close();									
								}
								
								// 이미지 1
								if ( json.getString("mb_com_img1").equals("") )
								{
									
								}
								else
								{
									URL imgUrl = new URL( _AppManager.GetHttpManager().DecodeString(json.getString("mb_com_img1") ));
									URLConnection conn = imgUrl.openConnection();
									conn.connect();
									BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
									m_RecruitData.mb_com_img1 = BitmapFactory.decodeStream(bis);
									bis.close();									
								}
								
								if ( json.getString("mb_com_img2").equals("") )
								{
									
								}
								else
								{
									URL imgUrl = new URL( _AppManager.GetHttpManager().DecodeString(json.getString("mb_com_img2") ));
									URLConnection conn = imgUrl.openConnection();
									conn.connect();
									BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
									m_RecruitData.mb_com_img2 = BitmapFactory.decodeStream(bis);
									bis.close();									
								}
								
								if ( json.getString("mb_com_img3").equals("") )
								{
									
								}
								else
								{
									URL imgUrl = new URL( _AppManager.GetHttpManager().DecodeString(json.getString("mb_com_img3") ));
									URLConnection conn = imgUrl.openConnection();
									conn.connect();
									BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
									m_RecruitData.mb_com_img3 = BitmapFactory.decodeStream(bis);
									bis.close();									
								}
							}
							handler.sendEmptyMessage(0);
						}
						else
						{
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
						}
					}
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 자동 생성된 catch 블록
						e.printStackTrace();
					}
					
				}
			});
			thread.start();
    	}
    }
    
    
    public void ScrapRecruit()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	{
    		Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();


					data.put("hr_idx",_AppManager.m_CompanyIndex.toString());
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_scrapHire.php", data);
					
					
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
							handler.sendEmptyMessage(11);
						}
						else
						{
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
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
				TabBtnRefrash();
		        RefreshUI();
		        
		        // 데이터들 넣어줌. 
		        {
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_1_2_text)).setText(m_RecruitData.upjong
		        			+ " "+ m_RecruitData.part );
		        	
		        	((TextView)findViewById(R.id.recruit_detail_list_row_title)).setText(m_RecruitData.hr_title);
		        	((TextView)findViewById(R.id.recruit_detail_list_row_name)).setText(m_RecruitData.mb_com_name);
		        	
		        	
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_2_2_text)).setText(m_RecruitData.area_name1 +
		        			" "+ m_RecruitData.area_name2 + " " + m_RecruitData.area_name3  );
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_3_2_text)).setText(m_RecruitData.subway);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_4_2_text)).setText(m_RecruitData.hr_work);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_5_2_text)).setText(m_RecruitData.hr_man_cnt);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_6_2_text)).setText(m_RecruitData.hr_salary);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_7_2_text)).setText(m_RecruitData.hr_apply_type);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_8_2_text)).setText(m_RecruitData.hr_carrier_type);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_9_2_text)).setText(m_RecruitData.hr_degree);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_10_2_text)).setText(m_RecruitData.hr_sex);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_11_2_text)).setText(m_RecruitData.hr_prime_name);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_12_2_text)).setText(m_RecruitData.hr_major_name);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_13_2_text)).setText(m_RecruitData.ce_name);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_14_2_text)).setText(m_RecruitData.hr_video 
		        			+ " " + m_RecruitData.hr_design + " " + m_RecruitData.hr_office);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_16_2_text)).setText("1. " + 
		        			m_RecruitData.hr_method1 + "\n2." + m_RecruitData.hr_method2 + "\n3." +m_RecruitData.hr_method3);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_17_2_text)).setText(m_RecruitData.hr_document);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_18_2_text)).setText(m_RecruitData.hr_days);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_4_19_2_text)).setText(m_RecruitData.hr_how);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_5_1_2_text)).setText(m_RecruitData.mb_ceo_name);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_5_2_2_text)).setText(m_RecruitData.mb_employee);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_5_3_2_text)).setText(m_RecruitData.mb_capital);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_5_4_2_text)).setText(m_RecruitData.mb_com_type);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_5_5_2_text)).setText(m_RecruitData.mb_upjong);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_5_6_2_text)).setText(m_RecruitData.mb_com_date);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_5_7_2_text)).setText(m_RecruitData.mb_homepage);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_5_8_2_text)).setText(m_RecruitData.mb_tel);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_5_9_2_text)).setText(m_RecruitData.mb_fax);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_6_1_2_text)).setText(m_RecruitData.mb_com_info);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_7_1_2_text)).setText(m_RecruitData.mb_com_history);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_9_1_2_text)).setText(m_RecruitData.hr_counter);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_9_2_2_text)).setText(m_RecruitData.hr_email);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_9_3_2_text)).setText(m_RecruitData.hr_tel);
		        	((TextView)findViewById(R.id.recruit_more_detail_list_row_9_4_2_text)).setText(m_RecruitData.hr_addr);
		        	
		        	{
		        		ImageView Image = (ImageView)findViewById(R.id.recruit_more_detail_list_row_2_1_image);
		        		
		        		if ( Image != null && m_RecruitData.logo_img != null )
		        		{
		        			Image.setImageBitmap(m_RecruitData.logo_img);
		        		}
		        	}
		        	{
		        		
		        		ImageView Image = (ImageView)findViewById(R.id.recruit_more_detail_list_row_8_1_image);
		        		
		        		if ( Image != null && m_RecruitData.mb_com_img1 != null  )
		        		{
		        			Image.setImageBitmap(m_RecruitData.mb_com_img1);
		        		}
		        		
		        	}
		        	
		        	{
		        		
		        		ImageView Image = (ImageView)findViewById(R.id.recruit_more_detail_list_row_8_2_image);
		        		
		        		if ( Image != null && m_RecruitData.mb_com_img2 != null  )
		        		{
		        			Image.setImageBitmap(m_RecruitData.mb_com_img2);
		        		}
		        		
		        	}
		        	
		        	{
		        		
		        		ImageView Image = (ImageView)findViewById(R.id.recruit_more_detail_list_row_8_3_image);
		        		
		        		if ( Image != null && m_RecruitData.mb_com_img3 != null )
		        		{
		        			Image.setImageBitmap(m_RecruitData.mb_com_img3);
		        		}
		        		
		        	}

		        }

				break;
			case 1:
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
				
			case 10:
				break;
			case 11:
				
				Toast.makeText(getApplicationContext(), "스크랩 되었습니다.", Toast.LENGTH_LONG).show();
				break; 
			case 8 :
				break;	
			default:
  				mProgress.dismiss();
  				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			}
		}
	};
    
    
    public void TabBtnRefrash( )
    {
		ImageBtnRefresh( R.id.recruit_more_detail_desc, R.drawable.m_app_008_btn01 );
		ImageBtnRefresh( R.id.recruit_more_detail_info, R.drawable.m_app_008_btn03 );
		ImageBtnRefresh( R.id.recruit_more_detail_man, R.drawable.m_app_008_btn05 );
		
		if ( m_TabIndex == 0 )
		{
			ImageBtnRefresh( R.id.recruit_more_detail_desc, R.drawable.m_app_008_btn02 );
		}
		else if ( m_TabIndex == 1 )
		{
			ImageBtnRefresh( R.id.recruit_more_detail_info, R.drawable.m_app_008_btn04 );
		}
		else if ( m_TabIndex == 2 )
		{
			ImageBtnRefresh( R.id.recruit_more_detail_man, R.drawable.m_app_008_btn06 );	
		}
		
    }
    
	public void onClick(View v) {
		// TODO 자동 생성된 메소드 스텁
		
		switch( v.getId() )
		{
		case R.id.recruit_back:
			onBackPressed();
			
			break;
		case R.id.recruit_more_detail_desc:
		{

			if ( m_TabIndex != 0 )
			{
				m_TabIndex = 0;
				RefreshUI();
			}
			TabBtnRefrash();
		}
			break;
		case R.id.recruit_more_detail_info:
		{
			if ( m_TabIndex != 1 )
			{
				m_TabIndex = 1;
				RefreshUI();
			}
			TabBtnRefrash();
		}
			break;
		case R.id.recruit_more_detail_man:
		{
			if ( m_TabIndex != 2 )
			{
				m_TabIndex = 2;
				RefreshUI();
			}
			TabBtnRefrash();
		}
			break;
			// 스크랩
		case R.id.recruit_more_detail_list_row_2_2_1_image1:
		{
			ScrapRecruit();
		}
			break;
			// 지도 화면으로 
		case R.id.recruit_more_detail_list_row_2_2_1_image2:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			
			
			_AppManager.m_MapInfomation.Latitude = m_RecruitData.mb_x_pos;
			_AppManager.m_MapInfomation.Longitude= m_RecruitData.mb_y_pos;
			_AppManager.m_MapInfomation.Name = m_RecruitData.mb_com_name;
			Intent intent;
            intent = new Intent().setClass(this, NaverMapActivity.class);
            startActivity( intent );
		}
			break;
			
		case R.id.recruit_more_detail_list_row_3:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(m_RecruitData.web_link)));
			break;
			
		case R.id.recruit_bottom_home:
		{
			Intent intent;
	        intent = new Intent().setClass(self, HomeActivity.class);
	        startActivity( intent ); 
		}
			break;
		case R.id.recruit_bottom_myjob:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			if ( _AppManager.m_LoginCheck == true )
			{
				
				Intent intent;
	            intent = new Intent().setClass(self, MyJobListActivity.class);
	            startActivity( intent );
	
			}
			else
			{
				self.ShowAlertDialLog( self ,"로그인 에러" , "로그인을 해야 이 메뉴를 사용할수 있습니다." );
			}
		}
			break;
		case R.id.recruit_bottom_scrap:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			if ( _AppManager.m_LoginCheck == true )
			{
				
				Intent intent;
	            intent = new Intent().setClass(self, ScrapMainActivity.class);
	            startActivity( intent );
	
			}
			else
			{
				self.ShowAlertDialLog( self ,"로그인 에러" , "로그인을 해야 이 메뉴를 사용할수 있습니다." );
			}
		}
			break;
		case R.id.recruit_bottom_setting:
		{
			Intent intent;
	        intent = new Intent().setClass(self, SettingActivity.class);
	        startActivity( intent ); 
		}
			break;
			
		}
		

	}
	
	public void RefreshUI()
	{

   		LinearLayout layout1 = (LinearLayout)findViewById(R.id.recruit_more_detail_list_row_1);
   		LinearLayout layout2 = (LinearLayout)findViewById(R.id.recruit_more_detail_list_row_2);
   		LinearLayout layout3 = (LinearLayout)findViewById(R.id.recruit_more_detail_list_row_3);
   		LinearLayout layout4 = (LinearLayout)findViewById(R.id.recruit_more_detail_list_row_4);
   		LinearLayout layout5 = (LinearLayout)findViewById(R.id.recruit_more_detail_list_row_5);
   		LinearLayout layout6 = (LinearLayout)findViewById(R.id.recruit_more_detail_list_row_6);
   		LinearLayout layout7 = (LinearLayout)findViewById(R.id.recruit_more_detail_list_row_7);
   		LinearLayout layout8 = (LinearLayout)findViewById(R.id.recruit_more_detail_list_row_8);
   		LinearLayout layout9 = (LinearLayout)findViewById(R.id.recruit_more_detail_list_row_9);
   		
   		
   		layout1.setVisibility(View.GONE);
   		layout2.setVisibility(View.GONE);
   		layout3.setVisibility(View.GONE);
   		layout4.setVisibility(View.GONE);
   		layout5.setVisibility(View.GONE);
   		layout6.setVisibility(View.GONE);
   		layout7.setVisibility(View.GONE);
   		layout8.setVisibility(View.GONE);
   		layout9.setVisibility(View.GONE);
   		
		
		if ( m_TabIndex == 0 )
		{
	   		layout1.setVisibility(View.VISIBLE);
	   		layout2.setVisibility(View.VISIBLE);
	   		layout3.setVisibility(View.VISIBLE);
	   		layout4.setVisibility(View.VISIBLE);
			
		}
		else if ( m_TabIndex == 1 )
		{
	   		layout1.setVisibility(View.VISIBLE);
	   		layout2.setVisibility(View.VISIBLE);
	   		layout3.setVisibility(View.VISIBLE);
	   		layout5.setVisibility(View.VISIBLE);
	   		layout6.setVisibility(View.VISIBLE);
	   		layout7.setVisibility(View.VISIBLE);
	   		layout8.setVisibility(View.VISIBLE);

		}
		else if ( m_TabIndex == 2 )
		{
			layout1.setVisibility(View.VISIBLE);
	   		layout2.setVisibility(View.VISIBLE);
	   		layout3.setVisibility(View.VISIBLE);
	   		layout9.setVisibility(View.VISIBLE);
		}
		

		
	}
	

    


}
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



public class EduDetailActivity extends BaseActivity implements OnClickListener {

	
	final EduDetailData m_EduDetailData = new EduDetailData();
	
	EduDetailActivity self;
	
	
	private Integer m_TabIndex = 0; // TabIndex
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edu_detail_layout);  // 인트로 레이아웃 출력     
        self = this;
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		


        
     // 전체 레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout = (LinearLayout)findViewById(R.id.edu_main);
            _AppManager.GetUISizeConverter().ParentRelativeConvertLinearLayout(layout);
        }
        // 헤더레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.edu_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	ScrollView view = (ScrollView)findViewById(R.id.edu_detail_scroll_view);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutScrollView(view);
        }
        
        // 이미지및 버튼 리사이징 
        {
            ImageBtnResize(R.id.edu_back );

            

            {
            	LinearLayout layout = (LinearLayout)findViewById(R.id.edu_more_detail_list_row_3);
            	layout.setOnClickListener(this);
            	
            }
            
        }
        
        ImageBtnResize2(R.id.edu_more_detail_list_row_2_2_1_image1 );
        ImageBtnResize2(R.id.edu_more_detail_list_row_2_2_1_image2 );
        
        GetEduDetailData();
        
        SetBtnEvent(R.id.edu_bottom_home);
        SetBtnEvent(R.id.edu_bottom_myjob);
        SetBtnEvent(R.id.edu_bottom_scrap);
        SetBtnEvent(R.id.edu_bottom_setting);
        
        
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
    
    
    public void ImageBtnRefresh( int id , int bitmapID )
    {
    	
    	ImageView imageview = (ImageView)findViewById(id);
    	imageview.setBackgroundResource(bitmapID);
    }
    
    public void GetEduDetailData()
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
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getEduData.php", data);
					
					
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
							m_EduDetailData.hr_idx = json.getInt("hr_idx");
							m_EduDetailData.hr_title = _AppManager.GetHttpManager().DecodeString(json.getString("hr_title"));
							m_EduDetailData.mb_com_name = _AppManager.GetHttpManager().DecodeString(json.getString("mb_com_name"));
							m_EduDetailData.mb_ceo_name = _AppManager.GetHttpManager().DecodeString(json.getString("mb_ceo_name"));
							m_EduDetailData.mb_business = _AppManager.GetHttpManager().DecodeString(json.getString("mb_business"));
							m_EduDetailData.mb_tel = _AppManager.GetHttpManager().DecodeString(json.getString("mb_tel"));
							m_EduDetailData.hr_homepage = _AppManager.GetHttpManager().DecodeString(json.getString("hr_homepage"));
							m_EduDetailData.hr_type = _AppManager.GetHttpManager().DecodeString(json.getString("hr_type"));
							m_EduDetailData.hr_man_cnt = _AppManager.GetHttpManager().DecodeString(json.getString("hr_man_cnt"));
							m_EduDetailData.hr_days = _AppManager.GetHttpManager().DecodeString(json.getString("hr_day_type"));
							m_EduDetailData.hr_degree = _AppManager.GetHttpManager().DecodeString(json.getString("hr_degree"));
							m_EduDetailData.hr_days = _AppManager.GetHttpManager().DecodeString(json.getString("hr_days"));
							
							m_EduDetailData.hr_counter = _AppManager.GetHttpManager().DecodeString(json.getString("hr_counter"));
							m_EduDetailData.hr_email = _AppManager.GetHttpManager().DecodeString(json.getString("hr_email"));
							m_EduDetailData.hr_tel = _AppManager.GetHttpManager().DecodeString(json.getString("hr_tel"));
							
							if ( json.getString("mb_x_pos").equals("") || json.getString("mb_x_pos").equals("0") )
							{
								m_EduDetailData.mb_x_pos = "0";
							}
							else
							{
								m_EduDetailData.mb_x_pos = _AppManager.GetHttpManager().DecodeString(json.getString("mb_x_pos"));	
							}
							
							if ( json.getString("mb_y_pos").equals("") || json.getString("mb_y_pos").equals("0"))
							{
								m_EduDetailData.mb_y_pos = "0";
							}
							else
							{
								m_EduDetailData.mb_y_pos = _AppManager.GetHttpManager().DecodeString(json.getString("mb_y_pos"));	
							}
							
											
							
							m_EduDetailData.web_link = _AppManager.GetHttpManager().DecodeString(json.getString("web_link"));

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
									m_EduDetailData.logo_img = BitmapFactory.decodeStream(bis);
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
    
    
    public void ScrapEdu()
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
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_scrapEdu.php", data);
					
					
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

		        
		        // 데이터들 넣어줌. 
		        {
		        	((TextView)findViewById(R.id.edu_detail_list_row_title)).setText(m_EduDetailData.hr_title);
		        	((TextView)findViewById(R.id.edu_detail_list_row_name)).setText(m_EduDetailData.mb_com_name);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_7_1_2_text)).setText(m_EduDetailData.mb_ceo_name);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_7_2_2_text)).setText(m_EduDetailData.mb_business);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_7_3_2_text)).setText(m_EduDetailData.mb_tel);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_7_4_2_text)).setText(m_EduDetailData.hr_homepage);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_7_5_2_text)).setText(m_EduDetailData.hr_type);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_8_1_2_text)).setText(m_EduDetailData.hr_man_cnt);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_8_2_2_text)).setText(m_EduDetailData.hr_day_type);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_8_3_2_text)).setText(m_EduDetailData.hr_degree);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_8_4_2_text)).setText(m_EduDetailData.hr_days);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_9_1_2_text)).setText(m_EduDetailData.hr_counter);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_9_2_2_text)).setText(m_EduDetailData.hr_tel);
		        	((TextView)findViewById(R.id.edu_more_detail_list_row_9_3_2_text)).setText(m_EduDetailData.hr_email);
		        	
		        	
		        	// 이미지 
		        	{
		        		ImageView Image = (ImageView)findViewById(R.id.edu_more_detail_list_row_2_1_image);
		        		
		        		if ( Image != null && m_EduDetailData.logo_img != null )
		        		{
		        			Image.setImageBitmap(m_EduDetailData.logo_img);
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
    
    

    
	public void onClick(View v) {
		// TODO 자동 생성된 메소드 스텁
		
		switch( v.getId() )
		{

			// 스크랩
		case R.id.edu_more_detail_list_row_2_2_1_image1:
		{
			ScrapEdu();
		}
			break;
			// 지도 화면으로 
		case R.id.edu_more_detail_list_row_2_2_1_image2:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			
			
			if ( m_EduDetailData.mb_x_pos.equals("0") || m_EduDetailData.mb_y_pos.equals("0"))
			{
				self.ShowAlertDialLog( self ,"에러" , "지역정보가 없어서 지도보기를 할수 없습니다." );
			}
			else
			{
				_AppManager.m_MapInfomation.Latitude = m_EduDetailData.mb_x_pos;
				_AppManager.m_MapInfomation.Longitude= m_EduDetailData.mb_y_pos;
				_AppManager.m_MapInfomation.Name = m_EduDetailData.mb_com_name;
				Intent intent;
	            intent = new Intent().setClass(this, NaverMapActivity.class);
	            startActivity( intent );				
			}
			

		}
			break;
			
		case R.id.edu_more_detail_list_row_3:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(m_EduDetailData.web_link)));
			break;
			
		case R.id.edu_back:
			onBackPressed();
			break;
			
			
		case R.id.edu_bottom_home:
		{
			Intent intent;
            intent = new Intent().setClass(self, HomeActivity.class);
            startActivity( intent ); 
		}
			break;
		case R.id.edu_bottom_myjob:
		{
			final AppManagement _AppManager = (AppManagement) getApplication();
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
		case R.id.edu_bottom_scrap:
		{
    		final AppManagement _AppManager = (AppManagement) getApplication();
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
		case R.id.edu_bottom_setting:
		{
			Intent intent;
            intent = new Intent().setClass(self, SettingActivity.class);
            startActivity( intent ); 
		}
			break;
			
		}
	}
	

	

    


}
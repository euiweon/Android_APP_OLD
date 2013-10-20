package kr.co.rcsoft.mediatong;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BaseActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class MyJobSettingActivity extends BaseActivity implements OnClickListener{

	RecruitDataArea [] Area1items1;
	RecruitDataArea [] Area2items1;
	
	RecruitDataArea [] Part1items1;
	RecruitDataArea [] Part2items1;
	
	RecruitDataArea [] Sexitems1;
	RecruitDataArea [] Degreeitems1;
	RecruitDataArea [] Caitems1;
	RecruitDataArea [] Typeitems1;
	
	RecruitDataArea [] Saitems1;
	
	
	Integer selectArea1 = 0;
	Integer selectArea2 = 0;
	
	Integer selectPart1 = 0;
	Integer selectPart2 = 0;
	
	Integer selectSex = 0;
	Integer selectDegree = 0;
	Integer selectCa = 0;
	
	Integer selectType = 0;
	
	Integer selectSa = 0;
	MyJobSettingActivity self;
	
	Boolean firstcheck;
	
	public MyjobInfo m_MyjobInfo = new MyjobInfo();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.myjob_setting_layout, null); 
        this.setContentView(viewToLoad);
        
        self = this;
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.myjob_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        // 헤더레이아웃 리사이징
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.myjob_body);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        
        ImageBtnResize(R.id.myjob_back );
        SpinnerResize(R.id.myjob_job1);
        SpinnerResize(R.id.myjob_job2);
        
        
        SpinnerResize(R.id.myjob_sex);
        SpinnerResize(R.id.myjob_school);
        SpinnerResize(R.id.myjob_money);
        SpinnerResize(R.id.myjob_area1);
        SpinnerResize(R.id.myjob_area2);
        SpinnerResize(R.id.myjob_type);
        
        ImageBtnResize2(R.id.myjob_setting );
        
        

        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText box = (EditText)findViewById(R.id.myjob_old1);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutEditText(box);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText box = (EditText)findViewById(R.id.myjob_old2);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutEditText(box);
        }
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText box = (EditText)findViewById(R.id.myjob_ca1);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutEditText(box);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText box = (EditText)findViewById(R.id.myjob_ca2);
        	_AppManager.GetUISizeConverter().ConvertLinearLayoutEditText(box);
        }
        
        
        
        
        firstcheck = false; 
        GetPart1();
    }
    
	public void SetBtnEvent( int id )
	{
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
	}
	public void SpinnerResize(int id )
	{
		AppManagement _AppManager = (AppManagement) getApplication();
    	Spinner box = (Spinner)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutSpinner(box);
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

	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		switch( arg0.getId())
		{
		case R.id.myjob_back:
			onBackPressed();
			break;
		case R.id.myjob_setting:
			SaveMyjob();
			break;
		}
	}
	
	public void SaveMyjob()
	{
		mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					
					
					
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					{
						Spinner box = (Spinner)findViewById(R.id.myjob_job1);
						Integer index = box.getSelectedItemPosition();
						
						data.put("mb_mj_p1_idx", Part1items1[index].Index.toString() );
					}
					
					{
						Spinner box = (Spinner)findViewById(R.id.myjob_job2);
						Integer index = box.getSelectedItemPosition();
						data.put("mb_mj_p2_idx", Part2items1[index].Index.toString() );
					}
					
					{
						Spinner box = (Spinner)findViewById(R.id.myjob_area1);
						Integer index = box.getSelectedItemPosition();
						data.put("mb_mj_a1_idx", Area1items1[index].Index.toString() );
					}
					
					{
						Spinner box = (Spinner)findViewById(R.id.myjob_area2);
						Integer index = box.getSelectedItemPosition();
						data.put("mb_mj_a2_idx", Area2items1[index].Index.toString() );
					}
					
					{
						Spinner box = (Spinner)findViewById(R.id.myjob_sex);
						Integer index = box.getSelectedItemPosition();
						data.put("mb_mj_sex", Sexitems1[index].Index.toString() );
					}
					
					// 나이 1
					{
						EditText box = (EditText)findViewById(R.id.myjob_old1);
						
						data.put("mb_mj_s_age", box.getText().toString() );
					}
					
					{
						EditText box = (EditText)findViewById(R.id.myjob_old2);
						
						data.put("mb_mj_e_age", box.getText().toString() );
					}
					
					// 나이 무관 여부 
					{
						CheckBox box = (CheckBox)findViewById(R.id.myjob_checkbox);
						
						
						if (box.isChecked() == true )
						{
							data.put("mb_mj_n_age", "1" );
						}
						else
						{
							data.put("mb_mj_n_age", "-1" );
						}
					}
					
					// 경력
					{
						EditText box = (EditText)findViewById(R.id.myjob_ca1);
						
						data.put("mb_mj_carr", box.getText().toString() );
					}
					
					{
						EditText box = (EditText)findViewById(R.id.myjob_ca2);
						
						data.put("mb_mj_e_carrier", box.getText().toString() );
					}
					
					// 학력 
					{
						Spinner box = (Spinner)findViewById(R.id.myjob_school);
						Integer index = box.getSelectedItemPosition();
						data.put("mb_mj_degree", Degreeitems1[index].Index.toString() );
					}
					
					// 연봉
					{
						Spinner box = (Spinner)findViewById(R.id.myjob_money);
						Integer index = box.getSelectedItemPosition();
						data.put("mb_mj_salary", Saitems1[index].Index.toString() );
					}
					
					
					// 고용형태
					{
						Spinner box = (Spinner)findViewById(R.id.myjob_type);
						Integer index = box.getSelectedItemPosition();
						data.put("mb_mj_applytype", Typeitems1[index].Index.toString() );
					}
					
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_saveMyjobData.php", data);
					
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							
							handler.sendEmptyMessage(40);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				
			});
			
			thread.start();
    	}
    	
	}
    
	
	public void GetMyJob()
	{
		mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getMyjobData.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							
							m_MyjobInfo.mb_mj_p1_idx = Integer.parseInt(json.getString("mb_mj_p1_idx"));
							m_MyjobInfo.mb_mj_p2_idx = Integer.parseInt(json.getString("mb_mj_p2_idx"));
							m_MyjobInfo.mb_mj_a1_idx = Integer.parseInt(json.getString("mb_mj_a1_idx"));
							m_MyjobInfo.mb_mj_a2_idx = Integer.parseInt(json.getString("mb_mj_a2_idx"));
							m_MyjobInfo.mb_mj_sex = Integer.parseInt(json.getString("mb_mj_sex"));
							m_MyjobInfo.mb_mj_s_age = Integer.parseInt(json.getString("mb_mj_s_age"));
							m_MyjobInfo.mb_mj_e_age = Integer.parseInt(json.getString("mb_mj_e_age"));
							m_MyjobInfo.mb_mj_n_age = Integer.parseInt(json.getString("mb_mj_n_age"));
							/*m_MyjobInfo.mb_mj_s_carrier = Integer.parseInt(json.getString("mb_mj_s_carrier"));
							m_MyjobInfo.mb_mj_e_carrier = Integer.parseInt(json.getString("mb_mj_e_carrier"));*/
							m_MyjobInfo.mb_mj_degree = Integer.parseInt(json.getString("mb_mj_degree"));
							m_MyjobInfo.mb_mj_salary = Integer.parseInt(json.getString("mb_mj_salary"));
							m_MyjobInfo.mb_mj_carr = Integer.parseInt(json.getString("mb_mj_carr"));
							m_MyjobInfo.mb_mj_applytype = Integer.parseInt(json.getString("mb_mj_applytype"));
							handler.sendEmptyMessage(30);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
	}
    
	
	public void GetPart1()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getPart1Data.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							Part1items1 = new RecruitDataArea[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								Part1items1[i] = new RecruitDataArea();
								Part1items1[i].Area = _AppManager.GetHttpManager().DecodeString(Qlist.getJSONObject(i).getString("p1_name"));
								Part1items1[i].Index = Integer.parseInt(Qlist.getJSONObject(i).getString("p1_idx"));
							}
							// 스피너를 갱신하라는 메세지를 넘겨준다. 
							handler.sendEmptyMessage(12);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    	
    }
	
	public void GetPart2( int regeon )
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	final Integer Regeon= regeon;
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					data.put("p1_idx", Regeon.toString());
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getPart2Data.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							Part2items1 = new RecruitDataArea[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								Part2items1[i] = new RecruitDataArea();
								Part2items1[i].Area = _AppManager.GetHttpManager().DecodeString(Qlist.getJSONObject(i).getString("p2_name"));
								Part2items1[i].Index = Integer.parseInt(Qlist.getJSONObject(i).getString("p2_idx"));
							}
							// 스피너를 갱신하라는 메세지를 넘겨준다. 
							handler.sendEmptyMessage(13);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    }
	
	public void GetArea1()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getArea1Data.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							Area1items1 = new RecruitDataArea[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								Area1items1[i] = new RecruitDataArea();
								Area1items1[i].Area = _AppManager.GetHttpManager().DecodeString(Qlist.getJSONObject(i).getString("a1_name"));
								Area1items1[i].Index = Integer.parseInt(Qlist.getJSONObject(i).getString("a1_idx"));
							}
							// 스피너를 갱신하라는 메세지를 넘겨준다. 
							handler.sendEmptyMessage(10);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    	
    }

    public void GetArea2( int regeon )
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	final Integer Regeon= regeon;
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					// 일부러 스레드를 대기 시킨다. 
					// 화면들어와서 첫 로딩시 여러개의 데이터를 한번에 로드하기 때문에 문제가 생긴다. 
					
					try {
						Thread.sleep(1000, 1);
					} catch (InterruptedException e1) {
						// TODO 자동 생성된 catch 블록
						e1.printStackTrace();
					}
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					data.put("a1_idx", Regeon.toString());
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getArea2Data.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							Area2items1 = new RecruitDataArea[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								Area2items1[i] = new RecruitDataArea();
								Area2items1[i].Area = _AppManager.GetHttpManager().DecodeString(Qlist.getJSONObject(i).getString("a2_name"));
								Area2items1[i].Index = Integer.parseInt(Qlist.getJSONObject(i).getString("a2_idx"));
							}
							// 스피너를 갱신하라는 메세지를 넘겨준다. 
							handler.sendEmptyMessage(11);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    }
   
    
    
    
    public void GetSex(  )
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getSexData.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							Sexitems1 = new RecruitDataArea[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								Sexitems1[i] = new RecruitDataArea();
								Sexitems1[i].Area = _AppManager.GetHttpManager().DecodeString(Qlist.getJSONObject(i).getString("txt"));
								Sexitems1[i].Index = Integer.parseInt(Qlist.getJSONObject(i).getString("val"));
							}
							// 스피너를 갱신하라는 메세지를 넘겨준다. 
							handler.sendEmptyMessage(14);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    }
    
    
    public void GetDegree(  )
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getDegreeData.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							Degreeitems1 = new RecruitDataArea[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								Degreeitems1[i] = new RecruitDataArea();
								Degreeitems1[i].Area = _AppManager.GetHttpManager().DecodeString(Qlist.getJSONObject(i).getString("txt"));
								Degreeitems1[i].Index = Integer.parseInt(Qlist.getJSONObject(i).getString("val"));
							}
							// 스피너를 갱신하라는 메세지를 넘겨준다. 
							handler.sendEmptyMessage(15);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    }
    
    
    public void GetCa(  )
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getDegreeData.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							Caitems1 = new RecruitDataArea[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								Caitems1[i] = new RecruitDataArea();
								Caitems1[i].Area = _AppManager.GetHttpManager().DecodeString(Qlist.getJSONObject(i).getString("txt"));
								Caitems1[i].Index = Integer.parseInt(Qlist.getJSONObject(i).getString("val"));
							}
							// 스피너를 갱신하라는 메세지를 넘겨준다. 
							handler.sendEmptyMessage(16);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    }
    
    
    public void GetType(  )
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getApplyTypeData.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							Typeitems1 = new RecruitDataArea[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								Typeitems1[i] = new RecruitDataArea();
								Typeitems1[i].Area = _AppManager.GetHttpManager().DecodeString(Qlist.getJSONObject(i).getString("txt"));
								Typeitems1[i].Index = Integer.parseInt(Qlist.getJSONObject(i).getString("val"));
							}
							// 스피너를 갱신하라는 메세지를 넘겨준다. 
							handler.sendEmptyMessage(18);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    }
    
    
    public void GetSa(  )
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();
    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getSalaryData.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,"접속에 문제가 있습니다." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							Saitems1 = new RecruitDataArea[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								Saitems1[i] = new RecruitDataArea();
								Saitems1[i].Area = _AppManager.GetHttpManager().DecodeString(Qlist.getJSONObject(i).getString("txt"));
								Saitems1[i].Index = Integer.parseInt(Qlist.getJSONObject(i).getString("val"));
							}
							// 스피너를 갱신하라는 메세지를 넘겨준다. 
							handler.sendEmptyMessage(17);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(2,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
    	}
    }
    
    
    public void InputSpinner1Area()
    {
        // 스피너 처리 
    	String[] Area1items = new  String[Area1items1.length];
    	for ( int i = 0; i < Area1items1.length; i++ )
    	{
    		Area1items[i] = Area1items1[i].Area;
    	}
        Spinner spin = (Spinner)findViewById(R.id.myjob_area1);
        
        spin.setPrompt("1차지역을 선택하세요.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Area1items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{                
        		GetArea2(Area1items1[position].Index);   
        		selectArea1 = Area1items1[position].Index;
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}
     
        });
    }
    
    public void InputSpinner2Area()
    {
        // 스피너 처리 
    	String[] Area1items = new  String[Area2items1.length];
    	for ( int i = 0; i < Area2items1.length; i++ )
    	{
    		Area1items[i] = Area2items1[i].Area;
    	}
        Spinner spin = (Spinner)findViewById(R.id.myjob_area2);
        
        spin.setPrompt("2차지역을 선택하세요.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Area1items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{            
        		selectArea2 = Area2items1[position].Index;
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}
     
        });
    }
    
    
    public void InputSpinner1Part()
    {
        // 스피너 처리 
    	String[] Area1items = new  String[Part1items1.length];
    	for ( int i = 0; i < Part1items1.length; i++ )
    	{
    		Area1items[i] = Part1items1[i].Area;
    	}
        Spinner spin = (Spinner)findViewById(R.id.myjob_job1);
        
        spin.setPrompt("1차직종을 선택하세요.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Area1items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{                
        		GetPart2(Part1items1[position].Index);   
        		selectPart1 = Part1items1[position].Index;
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}
     
        });
    }
    
    public void InputSpinner2Part()
    {
        // 스피너 처리 
    	String[] Area1items = new  String[Part2items1.length];
    	for ( int i = 0; i < Part2items1.length; i++ )
    	{
    		Area1items[i] = Part2items1[i].Area;
    	}
        Spinner spin = (Spinner)findViewById(R.id.myjob_job2);
        
        spin.setPrompt("2차지역을 선택하세요.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Area1items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{            
        		selectPart2 = Part2items1[position].Index;
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}
     
        });
    }
    
    
    public void InputSpinnerSex()
    {
        // 스피너 처리 
    	String[] Area1items = new  String[Sexitems1.length];
    	for ( int i = 0; i < Sexitems1.length; i++ )
    	{
    		Area1items[i] = Sexitems1[i].Area;
    	}
        Spinner spin = (Spinner)findViewById(R.id.myjob_sex);
        
        spin.setPrompt("성별을 선택하세요.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Area1items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{            
        		selectSex = Sexitems1[position].Index;
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}
     
        });
    }
    
    public void InputSpinnerDegree()
    {
        // 스피너 처리 
    	String[] Area1items = new  String[Degreeitems1.length];
    	for ( int i = 0; i < Degreeitems1.length; i++ )
    	{
    		Area1items[i] = Degreeitems1[i].Area;
    	}
        Spinner spin = (Spinner)findViewById(R.id.myjob_school);
        
        spin.setPrompt("학력을 선택하세요.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Area1items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{            
        		selectDegree = Degreeitems1[position].Index;
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}
     
        });
    }
    
    
    public void InputSpinnerCa()
    {
        // 스피너 처리 
    	String[] Area1items = new  String[Caitems1.length];
    	for ( int i = 0; i < Caitems1.length; i++ )
    	{
    		Area1items[i] = Caitems1[i].Area;
    	}
        Spinner spin = (Spinner)findViewById(R.id.myjob_carrier);
        
        spin.setPrompt("경력을 선택하세요.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Area1items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{            
        		selectCa = Caitems1[position].Index;
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}
     
        });
    }
    
    
    public void InputSpinnerSa()
    {
        // 스피너 처리 
    	String[] Area1items = new  String[Saitems1.length];
    	for ( int i = 0; i < Saitems1.length; i++ )
    	{
    		Area1items[i] = Saitems1[i].Area;
    	}
        Spinner spin = (Spinner)findViewById(R.id.myjob_money);
        
        spin.setPrompt("연봉을 선택하세요.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Area1items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{            
        		selectSa = Saitems1[position].Index;
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}
     
        });
    }
    
    public void InputSpinnerType()
    {
        // 스피너 처리 
    	String[] Area1items = new  String[Typeitems1.length];
    	for ( int i = 0; i < Typeitems1.length; i++ )
    	{
    		Area1items[i] = Typeitems1[i].Area;
    	}
        Spinner spin = (Spinner)findViewById(R.id.myjob_type);
        
        spin.setPrompt("고용형태를 선택하세요.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Area1items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{            
        		selectType = Typeitems1[position].Index;
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}
     
        });
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
				InputSpinner1Area();
				break;
			case 11:
				InputSpinner2Area();
				if ( firstcheck == false )
				{
					GetType();
				}
				
				break; 
			case 12:
				InputSpinner1Part();
				break; 
			case 13:
				InputSpinner2Part();
				if ( firstcheck == false )
				{
					GetSex();
				}
				
				break;
				
			case 14:
				InputSpinnerSex();
				
				if ( firstcheck == false )
				{
					GetDegree();
				}
				
				break;
				
			case 15:
				InputSpinnerDegree();
				if ( firstcheck == false )
				{
					GetSa();
				}
				
				break;
				
			case 16:

				
				break;
			case 17:
				InputSpinnerSa();
				if ( firstcheck == false )
				{
					GetArea1();
				}
				break;
			case 18:
			{
				// 스피너 추가 
				
				InputSpinnerType();
				// 저장된 데이터 불러오기  
				if ( firstcheck == false )
				{
					GetMyJob();
				}
				firstcheck = true; 
			}
				
				break;
			case 30:
				RefreshUI();
				break;
			case 40:
				message = "저장되었습니다.";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

				{
		  			Intent intent;
	                intent = new Intent().setClass(self, MyJobListActivity.class);
	                startActivity( intent );
					
				}
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
	
	
	public void RefreshUI()
	{
		{
			// 1차 직종 
			int index = -1;
			for( int i = 0 ; i < Part1items1.length ;i++ )
			{
				if ( Part1items1[i].Index == m_MyjobInfo.mb_mj_p1_idx )
				{
					index = i;
				}
			}
			Spinner box = (Spinner)findViewById(R.id.myjob_job1);
			if ( index < 0 )
				index = 0; 
			box.setSelection(index);
		}
		
		{
			// 2차 직종 
			int index = -1;
			for( int i = 0 ; i < Part2items1.length ;i++ )
			{
				if ( Part2items1[i].Index == m_MyjobInfo.mb_mj_p2_idx )
				{
					index = i;
				}
			}
			
			Spinner box = (Spinner)findViewById(R.id.myjob_job2);
			if ( index < 0 )
				index = 0; 
			box.setSelection(index);
		}
		
		{
			// 성별 
			int index = -1;
			for( int i = 0 ; i < Sexitems1.length ;i++ )
			{
				if ( Sexitems1[i].Index == m_MyjobInfo.mb_mj_sex )
				{
					index = i;
				}
			}
			
			Spinner box = (Spinner)findViewById(R.id.myjob_sex);
			if ( index < 0 )
				index = 0; 
			box.setSelection(index);
		}
		
		{
			// 나이 1
			{
				EditText Box = (EditText)findViewById(R.id.myjob_old1);
				if (m_MyjobInfo.mb_mj_s_age < 0 )
				{
					Box.setText("20");
				}
				else
				{
					Box.setText( m_MyjobInfo.mb_mj_s_age.toString() );
				}
			}
			// 나이 2
			{
				EditText Box = (EditText)findViewById(R.id.myjob_old2);
				if (m_MyjobInfo.mb_mj_e_age < 0 )
				{
					Box.setText("80");
				}
				else
				{
					Box.setText( m_MyjobInfo.mb_mj_e_age.toString() );
				}
			}
			
			// 무관 여부 체크 
			{
				CheckBox box = (CheckBox)findViewById(R.id.myjob_checkbox);
				
				if ( m_MyjobInfo.mb_mj_n_age == -1 )
				{
					box.setChecked(false);
				}
				else
				{
					box.setChecked(true);
				}
			}
		}
		
		{
			// 학력
			int index = -1;
			for( int i = 0 ; i < Degreeitems1.length ;i++ )
			{
				if ( Degreeitems1[i].Index == m_MyjobInfo.mb_mj_degree )
				{
					index = i;
				}
			}
			Spinner box = (Spinner)findViewById(R.id.myjob_school);
			if ( index < 0 )
				index = 0; 
			box.setSelection(index);
		}
		
		
		{
			// 경력 1
			{
				EditText Box = (EditText)findViewById(R.id.myjob_ca1);
				if (m_MyjobInfo.mb_mj_carr < 0 )
				{
					Box.setText("1");
				}
				else
				{
					Box.setText( m_MyjobInfo.mb_mj_carr.toString() );
				}
			}
			// 경력 2
			{
				EditText Box = (EditText)findViewById(R.id.myjob_ca2);
				if (m_MyjobInfo.mb_mj_e_carrier < 0 )
				{
					Box.setText("3");
				}
				else
				{
					Box.setText( m_MyjobInfo.mb_mj_e_carrier.toString() );
				}
			}
			
		}
		
		{
			// 연봉
			
			int index = -1;
			for( int i = 0 ; i < Saitems1.length ;i++ )
			{
				if ( Saitems1[i].Index == m_MyjobInfo.mb_mj_salary )
				{
					index = i;
				}
			}
			
			Spinner box = (Spinner)findViewById(R.id.myjob_money);
			if ( index < 0 )
				index = 0; 
			box.setSelection(index);
			
		}
		
		{
			// 근무 희망지역 1
			int index = -1;
			for( int i = 0 ; i < Area1items1.length ;i++ )
			{
				if ( Area1items1[i].Index == m_MyjobInfo.mb_mj_a1_idx )
				{
					index = i;
				}
			}
			
			Spinner box = (Spinner)findViewById(R.id.myjob_area1);
			if ( index < 0 )
				index = 0; 
			box.setSelection(index);			
			
		}
		{
			// 근무 희망지역 2
			int index = -1;
			for( int i = 0 ; i < Area2items1.length ;i++ )
			{
				if ( Area2items1[i].Index == m_MyjobInfo.mb_mj_a2_idx )
				{
					index = i;
				}
			}
			
			Spinner box = (Spinner)findViewById(R.id.myjob_area2);
			if ( index < 0 )
				index = 0; 
			box.setSelection(index);			
			
		}
		
		{
			// 고용형태
			int index = -1;
			for( int i = 0 ; i < Typeitems1.length ;i++ )
			{
				if ( Typeitems1[i].Index == m_MyjobInfo.mb_mj_applytype )
				{
					index = i;
				}
			}
			
			Spinner box = (Spinner)findViewById(R.id.myjob_type);
			if ( index < 0 )
				index = 0; 
			box.setSelection(index);				
		}
		
		
	}
    

    
}

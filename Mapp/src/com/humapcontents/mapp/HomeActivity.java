package com.humapcontents.mapp;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;
import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeData;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;

public class HomeActivity extends MappBaseActivity implements OnClickListener {

	private HomeActivity self;
	
	HomeData m_HomeData = new HomeData();
	
	int m_SelectStageIndex = 0;
	
	int type;
	
	Gallery m_Gallery ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);  // 인트로 레이아웃 출력     

        self = this;
        
        
        
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
		
        m_Gallery = (Gallery)findViewById(R.id.home_stage_img);
        
        m_Gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
        	 
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        	{
        		// 포커스가 간 View에 대한 정보를 얻어  이벤트 처리를 할 수 있다. 
        		((TextView)findViewById(R.id.home_stage_text)).setText(m_HomeData.rank[arg2].title + " - " + m_HomeData.rank[arg2].nick );
        		
        		m_SelectStageIndex = arg2;
        		
        		SetStagePhoto();
        	}
        	 
        	public void onNothingSelected(AdapterView<?> arg0) 
        	{
        	}
        	  
        }
        );
        
        m_Gallery.setOnItemClickListener(new OnItemClickListener() {
        	 
            public void onItemClick(AdapterView parent, View v, int position, long id) 
            {
            	Log.i("Home Select ", m_HomeData.rank[position].title );
            	AppManagement _AppManager = (AppManagement) getApplication();
            	_AppManager.m_PublicIndex = m_HomeData.rank[position].idx;
          
            	Intent intent;
               
                intent = new Intent().setClass(self, AuditionDetailActivity.class);
                
                
                startActivity( intent ); 
            	
            }

        });


        ImageResize(R.id.home_layout);
        ImageBtnResize(R.id.home_stage_btn_1);
        ImageBtnResize(R.id.home_stage_btn_2);
        ImageBtnResize(R.id.home_stage_btn_3);
        
        ImageResize(R.id.home_stage_btn_1_1);
        ImageResize(R.id.home_stage_btn_2_1);
        ImageResize(R.id.home_stage_btn_3_1);

        
        ImageResize(R.id.home_logo);
        
        
        
        ImageResize(R.id.home_stage_text);
        ImageResize(R.id.home_stage_img);
        
        ImageResize(R.id.home_stage_icon);
        ImageBtnResize(R.id.home_album_img);
        ImageResize(R.id.home_album_icon);
        ImageBtnResize(R.id.home_square_img);
        ImageResize(R.id.home_square_icon);
        
        String token ="";
        
        
        
        

		
        
        
        
       
        BottomMenuDown(false);
        AfterCreate( 0 );
        SetStagePhoto();
        
        GetHomeData();
        
        
    }
    
    

    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
    
    
    @Override
    public void onBackPressed() 
    {
    	
    	// 이전 화면으로 못 돌아가게 막는다.( 아예 종료가 되도록한다 )
    	// 일단은 돌아갈수 있게 테스트용으로 열어둔다. 
    	new AlertDialog.Builder(this)
		 .setTitle(this.getResources().getString(R.string.exit_title))
		 .setMessage(this.getResources().getString(R.string.exit_message)) //줄였음
		 .setPositiveButton(this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() 
		 {
		     public void onClick(DialogInterface dialog, int whichButton)
		     {   
		    	 AppManagement _AppManager = (AppManagement) getApplication();
	              
	        	   moveTaskToBack(true);
	        	   for ( int i = 0; i < _AppManager.activityList1.size() ; i++ )
			    		 _AppManager.activityList1.get(i).finish();
			    	 
			    	 android.os.Process.killProcess(android.os.Process.myPid());
		     }
		 })
		 .setNegativeButton(this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() 
		 {
		     public void onClick(DialogInterface dialog, int whichButton) 
		     {
		         //...할일
		     }
		 })
		 .show();
    	
    }
    
    private void SetStagePhoto()
    {
    	((ImageView)findViewById(R.id.home_stage_btn_1_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.home_stage_btn_2_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.home_stage_btn_3_1)).setVisibility(View.GONE);
    	
    	switch( m_SelectStageIndex )
    	{
    	case 0:
    		((ImageView)findViewById(R.id.home_stage_btn_1_1)).setVisibility(View.VISIBLE);
    		break;
    	case 1:
    		((ImageView)findViewById(R.id.home_stage_btn_2_1)).setVisibility(View.VISIBLE);
    		break;
    	case 2:
    		((ImageView)findViewById(R.id.home_stage_btn_3_1)).setVisibility(View.VISIBLE);
    		break;
    	}
    }

	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		
		AppManagement _AppManager = (AppManagement) getApplication();
		switch(arg0.getId() )
		{
		case R.id.home_album_img:
		{
			_AppManager.m_AlbumIndex = m_HomeData.album.idx;
			
			Intent intent;
               
            intent = new Intent().setClass(self, AlbumDetailActivity.class);
            
            
            startActivity( intent );
		}
			break;
		case R.id.home_square_img:
		{
			switch (type )
			{
			case 0:
			{
				_AppManager.m_SquareDetailTitle = m_HomeData.sqaure.title;
				_AppManager.m_SquareURL = m_HomeData.sqaure.url;
		
				Intent intent;
	               
                intent = new Intent().setClass(self, SquareWebActivity.class);
                
                
                startActivity( intent ); 
			}
			break;
			case 1:
			{
				
				_AppManager.m_SquareDetailTitle =  m_HomeData.sqaure.title;
				_AppManager.m_SquarePicture2 = _AppManager.DEF_URL +  "/mapp/ImageLoad?page=EventDetail&idx=" + 
						m_HomeData.sqaure.idx;
		
				Intent intent;
	               
                intent = new Intent().setClass(self, SquareEventDetailActivity.class);
                
                
                startActivity( intent ); 
			}
			break;
			case 2:
			{
				
			}
			break;
			case 3:
			{
				Intent intent;
                intent = new Intent().setClass(self, SquareNetworkActivity.class);
                startActivity( intent );
			}
			break;
			}
		}
			break;
		}
		
	}
	
	public void RefreshUI()
	{
		
        
    	
    	/*{
    		ImageView Image = (ImageView)findViewById(R.id.home_stage_img);
    		
    		if ( Image != null && m_HomeData.rank[0].imgn != null )
    		{
    			Image.setImageBitmap(m_HomeData.rank[0].imgn);
    		}
    	}*/
    	
    	{
    		ImageView Image = (ImageView)findViewById(R.id.home_album_img);
    		
    		if ( Image != null && m_HomeData.album.imgn != null )
    		{
    			Drawable d =new BitmapDrawable(getResources(),m_HomeData.album.imgn);
    			Image.setBackgroundDrawable( d );
    		}
    	}
    	{
    		ImageView Image = (ImageView)findViewById(R.id.home_square_img);
    		
    		if ( Image != null && m_HomeData.sqaure.imgn != null )
    		{
    			 Drawable d =new BitmapDrawable(getResources(),m_HomeData.sqaure.imgn);
	   		        
	   		    
    			Image.setBackgroundDrawable( d );
    			
    		}
    	}
    	{
        	Bitmap[] temp = new Bitmap[ m_HomeData.rank.length ];
        	for ( int i = 0 ; i < m_HomeData.rank.length; i++ )
        	{
        		temp[i] = m_HomeData.rank[i].imgn;
        	}
        	
        	m_Gallery.setAdapter(new ImageAdapter( this, temp));
    	}

	}
	
	
	public void GetHomeData()
    {
    	{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					// ID와 패스워드를 가져온다 
					

					
					Map<String, String> data = new HashMap  <String, String>();

					
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mapp/Home", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{

							JSONArray usageList = (JSONArray)json.get("weekly rank");
							
							m_HomeData.rank = new HomeWeekLyRank[ usageList.length()];
							for(int i = 0; i < usageList.length(); i++)
							{
								JSONObject list = (JSONObject)usageList.get(i);
								m_HomeData.rank[i] = new HomeWeekLyRank();
								m_HomeData.rank[i].rank =  Integer.parseInt(list.optString("rank"));
								m_HomeData.rank[i].img =  (list.optString("img"));
								m_HomeData.rank[i].idx =  Integer.parseInt(list.optString("idx"));
								m_HomeData.rank[i].title =  (list.optString("title"));
								m_HomeData.rank[i].nick =  (list.optString("nick"));
								m_HomeData.rank[i].replies =  Integer.parseInt(list.optString("replies"));
								m_HomeData.rank[i].likes =  Integer.parseInt(list.optString("likes"));
								
								
								if (  (list.optString("imgn")).equals("") )
								{
									
								}
								else
								{
									URL imgUrl = new URL(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Common&idx="+ 
											(list.optString("imgn")) );
									URLConnection conn = imgUrl.openConnection();
									conn.connect();
									BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
									m_HomeData.rank[i].imgn = BitmapFactory.decodeStream(bis);
									bis.close();									
								}
							}
							JSONObject album = (JSONObject)json.get("recent album");
							{
								m_HomeData.album = new HomeAlbum();
								m_HomeData.album.idx =  Integer.parseInt(album.optString("idx"));
								m_HomeData.album.title =  (album.optString("title"));
								m_HomeData.album.nick =  (album.optString("nick"));
								
								// image
								URL imgUrl = new URL(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Album&idx="+ 
										m_HomeData.album.idx );
								URLConnection conn = imgUrl.openConnection();
								conn.connect();
								BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
								m_HomeData.album.imgn = BitmapFactory.decodeStream(bis);
								bis.close();	
							}
							
							
							JSONObject sqaure = (JSONObject)json.get("square");
							{
								m_HomeData.sqaure = new HomeSqaure();
								m_HomeData.sqaure.categoty =  (sqaure.optString("category"));
								
								
								if (  (sqaure.optString("imgn")).equals("") )
								{
									
								}
								else
								{
									URL imgUrl = new URL(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=Common&idx="+ 
											(sqaure.optString("imgn")) );
									URLConnection conn = imgUrl.openConnection();
									conn.connect();
									BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
									m_HomeData.sqaure.imgn = BitmapFactory.decodeStream(bis);
									bis.close();									
								}
								
								
								if ( m_HomeData.sqaure.categoty.equals("magazine") )
								{
									type = 0;
									m_HomeData.sqaure.idx=  Integer.parseInt(sqaure.optString("idx"));	
									m_HomeData.sqaure.title =  (sqaure.optString("title"));
									m_HomeData.sqaure.url =  (sqaure.optString("url"));
								}
								else if (  m_HomeData.sqaure.categoty.equals("event")  )
								{
									type = 1;
									m_HomeData.sqaure.idx=  Integer.parseInt(sqaure.optString("idx"));
									m_HomeData.sqaure.title =  (sqaure.optString("title"));
								}
								else if (  m_HomeData.sqaure.categoty.equals("class")  )
								{
									type = 2;
									m_HomeData.sqaure.title =  (sqaure.optString("title"));
									m_HomeData.sqaure.group =  (sqaure.optString("group"));
									m_HomeData.sqaure.video =  (sqaure.optString("video"));
								}
								else if (   m_HomeData.sqaure.categoty.equals("network") )
								{
									type = 3;
									m_HomeData.sqaure.group =  (sqaure.optString("group"));
									m_HomeData.sqaure.idx=  Integer.parseInt(sqaure.optString("idx"));
									m_HomeData.sqaure.title =  (sqaure.optString("title"));
									//m_HomeData.sqaure.text =  (sqaure.optString("text"));
									m_HomeData.sqaure.text = sqaure.optString("text");
									
									// 네트워크 세부 이미지는 로드 하지 않음. 
									
								}
								
								
							}
							handler.sendEmptyMessage(0);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							if ( json.optString("errcode", null ) == null)
							{
								handler.sendMessage(handler.obtainMessage(1,_AppManager.GetError(-1) ));
							}
							else
							{
								handler.sendMessage(handler.obtainMessage(1,_AppManager.GetError(json.optInt("errcode")) ));
							}
							
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler.sendMessage(handler.obtainMessage(1,"Error" ));
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
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				RefreshUI();
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 2:

				break;
				
			case 3:
	
				break;
			case 20:
				break;
			default:
				break;
			}

		}
    	
	};
	
	

	
}

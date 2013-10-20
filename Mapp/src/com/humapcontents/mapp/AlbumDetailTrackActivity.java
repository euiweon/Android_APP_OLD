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
import java.util.ArrayList;
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
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.BitmapManager;
import com.humapcontents.mapp.AlbumActivty.AlbumRow_Adapter;
import com.humapcontents.mapp.MappBaseActivity.ImageAdapter;
import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeData;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;

public class AlbumDetailTrackActivity extends MappBaseActivity implements OnClickListener {

	private AlbumDetailTrackActivity self;
	


	int m_SelectIndex = 0;
	

	private ListView m_ListView;
	Boolean m_DetailShow = false;
	
	Integer m_GalleryWidth;
	Integer m_GalleryHeight;
	
	Gallery m_Gallery ;
	String[] jacket2;
	Bitmap[] jacket;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_track_photo_layout);  // 인트로 레이아웃 출력     

        self = this;
        
       
        


     // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        


        
        
        m_Gallery = (Gallery)findViewById(R.id.album_detail_gallery);
        
        GetGallerySize();
        
        m_Gallery.setOnItemClickListener(new OnItemClickListener() 
        {
       	 
            public void onItemClick(AdapterView parent, View v, int position, long id) 
            {
            	/*
          
            	Intent intent;
               
                intent = new Intent().setClass(self, AuditionDetailActivity.class);
                
                
                startActivity( intent );*/ 
            	
            }

        });
        
        m_Gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
        	 
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        	{
        		// 포커스가 간 View에 대한 정보를 얻어  이벤트 처리를 할 수 있다. 
        		
        		
        		m_SelectIndex = arg2;
        		
        		SelectPhoto();
        	}
        	 
        	public void onNothingSelected(AdapterView<?> arg0) 
        	{
        	}
        	  
        }
        );
        
        
        ImageResize(R.id.main_layout);
        ImageResize(R.id.album_detail_gallery);
        ImageResize(R.id.detail_buttons);


        BottomMenuDown(true);
        AfterCreate( 2 );
        
        
        AppManagement _AppManager = (AppManagement) getApplication();
        jacket2 = _AppManager.m_Jacket;
        
        

		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				jacket= new Bitmap[ jacket2.length ];
				for ( int i = 0; i < jacket2.length; i++)
				{
					URL imgUrl = null;
					try {
						imgUrl = new URL(jacket2[i] );
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					URLConnection conn = null;
					try {
						conn = imgUrl.openConnection();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						conn.connect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					BufferedInputStream bis = null;
					try {
						bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Rect rect = new Rect();
					rect.left = 0;
					rect.top = 0;
					rect.bottom = m_GalleryHeight;
					rect.right = m_GalleryWidth;
					
					
					
					BitmapFactory.Options bo = new BitmapFactory.Options();
					bo.inSampleSize = 2;
					
					jacket[i] = BitmapFactory.decodeStream(bis, rect,bo);
					try {
						bis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				
				handler.sendEmptyMessage(0);
													
				
			}
		});
		
		thread.start();
		
		
		{
        	ImageView imageview = (ImageView)findViewById(R.id.title_icon);
           
            imageview.setOnClickListener(this);
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
	

    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
    
    
    
    



    

    


	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		
		switch(arg0.getId() )
		{

		case R.id.title_icon:
		{
			onBackPressed();
		}
		break;
		}
		
	}
	
	public void RefreshUI()
	{
		
		
		AppManagement _AppManager = (AppManagement) getApplication();
		((TextView)findViewById(R.id.title_name)).setText(_AppManager.m_AlbumTitle);
		((TextView)findViewById(R.id.title_desc)).setText(_AppManager.m_AlbumDesc);

		
		
		jacket2 = _AppManager.m_Jacket;
		
		
		
		m_Gallery.setAdapter(new ImageAdapter4( this, jacket, m_GalleryWidth, m_GalleryHeight ));
		
		
		
		int count =  jacket2.length;
		
		if ( jacket2.length > 7 )
			count = 7;
		
		
		{
			((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_4)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_5)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_6)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_7)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_6_1)).setVisibility(View.GONE);
			((ImageView)findViewById(R.id.square_btn_7_1)).setVisibility(View.GONE);
			
			switch( count )
			{
			case 0:
				break;
			case 1:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				break;
			case 2:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				
				break;
			case 3:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
				break;
			case 4:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.VISIBLE);
				break;
			case 5:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.VISIBLE);
				break;
			case 6:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_6)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_6_1)).setVisibility(View.VISIBLE);
				break;
			case 7:
				((ImageView)findViewById(R.id.square_btn_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_6)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_7)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_6_1)).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.square_btn_7_1)).setVisibility(View.VISIBLE);
				
				break;
			}
		}


	}
	
	

	
	
	private void SelectPhoto()
    {
    	((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_6_1)).setVisibility(View.GONE);
    	((ImageView)findViewById(R.id.square_btn_7_1)).setVisibility(View.GONE);
    	
    	
    	switch( m_SelectIndex )
    	{
    	case 0:
    		((ImageView)findViewById(R.id.square_btn_1_1)).setVisibility(View.VISIBLE);
    		break;
    	case 1:
    		((ImageView)findViewById(R.id.square_btn_2_1)).setVisibility(View.VISIBLE);
    		break;
    	case 2:
    		((ImageView)findViewById(R.id.square_btn_3_1)).setVisibility(View.VISIBLE);
    		break;
    	case 3:
    		((ImageView)findViewById(R.id.square_btn_4_1)).setVisibility(View.VISIBLE);
    		break;
    	case 4:
    		((ImageView)findViewById(R.id.square_btn_5_1)).setVisibility(View.VISIBLE);
    		break;
    	case 5:
    		((ImageView)findViewById(R.id.square_btn_6_1)).setVisibility(View.VISIBLE);
    		break;
    	case 6:
    		((ImageView)findViewById(R.id.square_btn_7_1)).setVisibility(View.VISIBLE);
    		break;
    	}
    }
	
	
	public void GetGallerySize(  )
	{
		
    	Display defaultDisplay = ((WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = m_Gallery.getLayoutParams().height;
		int imagewidth = m_Gallery.getLayoutParams().width;
		
		FrameLayout.LayoutParams margin = (FrameLayout.LayoutParams )(m_Gallery.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		m_GalleryWidth = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		m_GalleryHeight = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
	}
	
	


}

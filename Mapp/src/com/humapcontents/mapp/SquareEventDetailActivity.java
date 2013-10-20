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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
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
import com.humapcontents.mapp.AuditionActivity.AuditionRow_Adapter;
import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeData;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;

public class SquareEventDetailActivity extends MappBaseActivity implements OnClickListener {



	private SquareEventDetailActivity self;
	
	Bitmap m_GalleryImage; 
	
	int m_SelectIndex = 0;
	
	Gallery m_Gallery ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.square_event_detail_layout);     

        self = this;
        
        
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }

        ImageResize(R.id.main_layout);
        ImageResize(R.id.event_view);
        
       

        ImageBtnEvent(R.id.title_icon , this);
        
        RefreshUI();
        
		BottomMenuDown(true);
        AfterCreate( 3 );

    }


    
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
	
	
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			
			switch(msg.what)
			{
			case 0:
			{
				ImageView Image = (ImageView)findViewById(R.id.event_view);
	    		
	    		if ( Image != null && m_GalleryImage != null )
	    		{
	    			 Drawable d =new BitmapDrawable(getResources(),m_GalleryImage);
   		    
	    			Image.setBackgroundDrawable( d );
	    		}
			}
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
	

	
	
	public void RefreshUI()
	{
		
		final AppManagement _AppManager = (AppManagement) getApplication();

		
		((TextView)findViewById(R.id.title_name)).setText( _AppManager.m_SquareDetailTitle);
		

		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				
				{
					
					URL imgUrl = null;
					try {
						imgUrl = new URL(_AppManager.m_SquarePicture2);
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						handler.sendEmptyMessage(1);
					}
					URLConnection conn = null;
					try {
						conn = imgUrl.openConnection();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						handler.sendEmptyMessage(1);
					}
					try {
						conn.connect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(1);
					}
					BufferedInputStream bis = null;
					try {
						bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(1);
					}
					m_GalleryImage = BitmapFactory.decodeStream(bis);
					try {
						bis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(1);
					}

				}
				
				handler.sendEmptyMessage(0);
			}
		});
		thread.start();

	}


}

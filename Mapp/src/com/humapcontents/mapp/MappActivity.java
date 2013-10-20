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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;
import com.facebook.*;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.*;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.humapcontents.mapp.data.TwitterConstant;


public class MappActivity extends MappBaseActivity implements OnClickListener {

	private MappActivity self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapp_layout);  // 인트로 레이아웃 출력     

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


        ImageResize(R.id.main_tab_1_name);
        ImageResize(R.id.main_tab_1_line);
        ImageBtnResize(R.id.main_tab_1);
        ImageResize(R.id.main_tab_1_arrow);
        
        ImageResize(R.id.main_tab_2_name);
        ImageResize(R.id.main_tab_2_line);
        ImageBtnResize(R.id.main_tab_2);
        ImageResize(R.id.main_tab_2_arrow);
        
        ImageResize(R.id.main_tab_3_name);
        ImageResize(R.id.main_tab_3_line);
        ImageBtnResize(R.id.main_tab_3);
        ImageResize(R.id.main_tab_3_arrow);
        ImageResize(R.id.main_tab_4_name);
        ImageResize(R.id.main_tab_4_line);
        ImageBtnResize(R.id.main_tab_4);
        ImageResize(R.id.main_tab_4_arrow);
        ImageResize(R.id.main_tab_5_name);
        ImageResize(R.id.main_tab_5_line);
        ImageBtnResize(R.id.main_tab_5);
        ImageResize(R.id.main_tab_5_arrow);
        
        
        

        String token ="";

        BottomMenuFullUp();
        AfterCreate( 9 );
        
        
        ImageBtnEvent(R.id.title_icon , this);
        
        
        
    }
    

	@Override
    public void  onResume()
    {
    	super.onResume();
    	RefreshUI();
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
		case R.id.main_tab_1:
		{
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mapptown.com"));

			startActivity(intent);
		}
	
			break;
		case R.id.main_tab_2:
		{
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/humapcontents"));

			startActivity(intent);
		}
	
			break;
		case R.id.main_tab_3:
		{
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/MAPPTOWN"));

			startActivity(intent);
		}
	
			break;
		case R.id.main_tab_4:
		{
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ustream.tv/channel/mapptv"));

			startActivity(intent);
		}
	
			break;
		case R.id.main_tab_5:
		{
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/user/mapptown"));

			startActivity(intent);
		}
	
			break;


			
		
		}
		
	}
	

	
	

	
}

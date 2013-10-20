package com.humapcontents.mapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;



import com.euiweonjeong.base.BaseActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class SettingAppInfoActivity extends MappBaseActivity implements OnClickListener {

	SettingAppInfoActivity self;
	private Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_infomation_layout);  // 인트로 레이아웃 출력      
        
        
        self = this;
        
        ImageResize(R.id.infomation_layout);
		ImageResize(R.id.infomation_header);
		ImageResize(R.id.mapp_ver);
		ImageResize(R.id.mapp_desc);
		ImageResize(R.id.infomation_layout2);
		ImageResize(R.id.mapp_ver2);
		ImageResize(R.id.mapp_desc2);
		ImageResize(R.id.faq_row_content);
		
        BottomMenuDown( true);
        AfterCreate( 7 );
        
        {
        	ImageView imageview = (ImageView)findViewById(R.id.title_icon);
            imageview.setOnClickListener(this);
        }
        
    }
	public void onClick(View arg0) 
	{
		// TODO Auto-generated method stub
		
		switch( arg0.getId() )
        {
        case R.id.title_icon:
        {
        	onBackPressed();
        	break;
        }
        }
		
	}
    
    
    
}

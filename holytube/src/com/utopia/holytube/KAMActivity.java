package com.utopia.holytube;

import java.io.IOException;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.utopia.holytube.mainSingleton;
import com.utopia.holytube.ContryActivity.Today_ClickListen;
import com.utopia.holytube.mainSingleton.TodayData;




public class KAMActivity extends HolyTubeBaseActivity implements OnClickListener  {


	/** Called when the activity is first created. */
	
	KAMActivity  self;
	

	
	private ArrayList<String> tempstring;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kam);
        self = this;


        {
       	 Button TabBTN2 = (Button)findViewById(R.id.homebutton);
            TabBTN2.setOnClickListener((this));
            
       	 Button TabBTN3 = (Button)findViewById(R.id.backbutton);
       	 TabBTN3.setOnClickListener((this));
       }

       AfterCreate();
       

        
    }
    
    public void RefreshUI()
    {
 


    }

	@Override
	protected void onPause() {
		super.onPause();
	}

	
    public void onClick(View v )
    {
    	switch(v.getId())
    	{
    	case R.id.homebutton:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, HolytubeActivity.class);
	        startActivity( intent ); 
    	}
    	break;
    	case R.id.backbutton:
    	{
    		onBackPressed();
    	}
    	break;
    	

    	default:
    		break;
    	}
    }

    


}
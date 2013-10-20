package com.goodmate.tayotayo;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.BitmapManager;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavoriteActivity extends BaseTayoActivity {

	FavoriteActivity self;
	
	
	private ListView m_ListView;
	

	private ContactAdapter m_Adapter;
	
	String code = ""; 
	private Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);  // 인트로 레이아웃 출력      
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        

        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	_AppManager.activityList1.add(this);
        	
        	m_ListView = ((ListView)findViewById(R.id.list_view));	    
    		m_Adapter = new ContactAdapter(this, R.layout.destnatio_row, _AppManager.FavoriteArray);
        }
      
        self = this;
        
        AfterCreate();
        
        BtnEvent(R.id.plus);

        
        
		
	    
		m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);
	
        
        RefreshUI();

    }

    
    @Override
    public void  onResume()
    {
    	super.onResume();
    	RefreshUI();
    }
    

	

	
	public void BtnEvent( int id )
    {
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(new OnClickListener()
        {
            public void onClick(View arg0)
            {
            	switch(arg0.getId())
            	{
            	case R.id.plus:
            	{
            		AppManagement _AppManager = (AppManagement) getApplication();
            		_AppManager.FavoriteResi = true;
            		onBackPressed();
            	}
            		break;

            		
            		
            	}
            	
            }
        });

    }
	
	public void RefreshUI()
	{
		m_Adapter.notifyDataSetChanged();
	}
	
	public class ContactAdapter extends ArrayAdapter<FavoriteData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<FavoriteData> mList;
		private LayoutInflater mInflater;
		
    	public ContactAdapter(Context context, int layoutResource, ArrayList<FavoriteData> mTweetList)
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
    		final FavoriteData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				((TextView)convertView.findViewById(R.id.audition_deteil_row_reply)).setText(mBar.Name);
			
				
				LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.audition_deteil_row_3);
				detailBar1.setOnLongClickListener(new View.OnLongClickListener() 
				{

					public boolean onLongClick(View v) 
					{
						new AlertDialog.Builder(self)

						
						.setTitle("삭제")
						.setMessage("즐겨찾기를 삭제하시겠습니까?")
						.setPositiveButton("확인", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								_AppManager.RemoveFavorite(mBar);
								RefreshUI();
								dialog.dismiss();
							}
						})
						.setNegativeButton("취소", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
							}
						})
						.show();
		                return true;
						
					}
				});
				
				
			}
			return convertView;
		}
    }

}

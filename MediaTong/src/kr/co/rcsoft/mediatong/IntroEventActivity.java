package kr.co.rcsoft.mediatong;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.ImageViewDoubleTab;
import com.euiweonjeong.base.ImageViewDoubleTabListener;
import com.euiweonjeong.base.UISizeConverter;



public class IntroEventActivity extends BaseActivity implements OnClickListener , ImageViewDoubleTabListener {

	
	
	public Integer 	bd_idx;
	public String  	bd_title;
	public String  	bd_contents;
	public Bitmap 	bd_file;
	
	IntroEventActivity self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_event);  // 인트로 레이아웃 출력      
        
        
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        self = this;
        
        AppManagement _AppManager = (AppManagement) getApplication();
        
        
        // 레이아웃 리사이징 
        {
            LinearLayout layout = (LinearLayout)findViewById(R.id.intro_event_image_layout);
            _AppManager.GetUISizeConverter().ConvertLinearLayout(layout);
            
            // 자동으로 밑으로 내려간다. 
            LinearLayout layout2 = (LinearLayout)findViewById(R.id.intro_event_bottom);
            _AppManager.GetUISizeConverter().ConvertLinearLayout(layout2);       	
        }
        // 이미지 리사이징 & 이벤트 
        {
        	ImageView imageview = (ImageView)findViewById(R.id.intro_event_close);
            _AppManager.GetUISizeConverter().ConvertLinearLayoutImage(imageview);  
            imageview.setOnClickListener(this);
            
        }
        {
        	CheckBox box = (CheckBox)findViewById(R.id.intro_event_checkbox);
        	box.setOnClickListener(this);
        }
        
        {
        	ImageViewDoubleTab view = (ImageViewDoubleTab)findViewById(R.id.intro_event_image);
        	
        	view.SetDoubleTabListener(this);
        }
        GetEventList();
        
    }
    
    
    public void GetEventList()
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
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getEventPopupData.php", data);
					
					
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
							
							

							bd_title = _AppManager.GetHttpManager().DecodeString(json.getString("bd_title"));
							bd_contents = _AppManager.GetHttpManager().DecodeString(json.getString("bd_contents"));
							
							
							// 로고 이미지 
							if ( json.getString("bd_file") ==  null ||json.getString("bd_file").equals("") )
							{
								
							}
							else
							{
								URL imgUrl = new URL( _AppManager.GetHttpManager().DecodeString(json.getString("bd_file") ));
								URLConnection conn = imgUrl.openConnection();
								conn.connect();
								BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
								bd_file = BitmapFactory.decodeStream(bis);
								bis.close();									
							}
							
							handler.sendEmptyMessage(0);
							
						}
						
						else
						{
							handler.sendMessage(handler.obtainMessage(2,"에러" ));
						}
					}
					
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(2,"에러" ));
					} catch (MalformedURLException e) {
						// TODO 자동 생성된 catch 블록
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(2,"에러" ));
					} catch (IOException e) {
						// TODO 자동 생성된 catch 블록
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(2,"에러" ));
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
			{
				ImageView star1 = (ImageView)findViewById(R.id.intro_event_image);
	   			
	   			if ( bd_file != null )
	   			{
	   				AppManagement _AppManager = (AppManagement) getApplication();
	   		        _AppManager.GetUISizeConverter().ConvertLinearLayoutImage(star1); 
	   		        
	   		        Drawable d =new BitmapDrawable(getResources(),bd_file);
	   		        
	   		        // API 10
	   				star1.setBackgroundDrawable( d );
	   				
	   				// API 16
	   				//star1.setBackground( d );
	   			}
			}

				break;
			case 1:
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
			{
				Intent intent;
	            intent = new Intent().setClass(self, HomeActivity.class);
	            startActivity( intent ); 
			}

				break;

			}
		}
	};
	
    
    @Override
    public void onBackPressed() 
    {
    	// 이전 화면으로 못 돌아가게 막는다.( 아예 종료가 되도록한다 )
    	// 일단은 돌아갈수 있게 테스트용으로 열어둔다. 
    	new AlertDialog.Builder(this)
		 .setTitle("종료 확인")
		 .setMessage("정말 종료 하겠습니까?") //줄였음
		 .setPositiveButton("예", new DialogInterface.OnClickListener() 
		 {
		     public void onClick(DialogInterface dialog, int whichButton)
		     {   
				moveTaskToBack(true);
			    android.os.Process.killProcess(android.os.Process.myPid());
		     }
		 })
		 .setNegativeButton("아니요", new DialogInterface.OnClickListener() 
		 {
		     public void onClick(DialogInterface dialog, int whichButton) 
		     {
		         //...할일
		     }
		 })
		 .show();
    	
    }


	public void onClick(View v) {
		// TODO 자동 생성된 메소드 스텁
		
		switch(v.getId())
    	{
    	case R.id.intro_event_close:
    	{
            Intent intent;
            intent = new Intent().setClass(this, HomeActivity.class);
            startActivity( intent ); 
			
    	}
    		break;
    	case R.id.intro_event_checkbox:	
    	{
    		CheckBox box = (CheckBox)findViewById(R.id.intro_event_checkbox);
    		
    		Date today = new Date(); 
            
            SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd"); 
             
            
    		if ( box.isChecked() )
    		{
    			SharedPreferences prefdefault = getSharedPreferences("Event", MODE_PRIVATE); 
    		    SharedPreferences.Editor edit = prefdefault.edit();
    		    edit.putString("CAL", date.format(today));  
    		    edit.commit(); 

    		}
    		else
    		{
    			SharedPreferences prefdefault = getSharedPreferences("Event", MODE_PRIVATE); 
    		    SharedPreferences.Editor edit = prefdefault.edit();
    		    edit.putString("CAL", "0");  
    		    edit.commit(); 

    		}
    	}

    	default:
    		break;
    	}
		
	}
	
	class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) 
        {
            // doubletap이 발생할때 할일.
            return true;
        }
	}

	public void ImageViewDoubleTabEvent(View v) {
		// TODO 자동 생성된 메소드 스텁
		
		// 더블탭 이벤트 
		
		switch ( v.getId() )
		{
		case R.id.intro_event_image:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediatong.rcsoft.co.kr/company/board_list.php?bd_name=event_result")));
			Log.d("doubleTab", "doubleTab");
			break;
		}
		
	}

}

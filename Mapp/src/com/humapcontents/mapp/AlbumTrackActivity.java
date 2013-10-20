package com.humapcontents.mapp;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BitmapManager;
import com.facebook.android.Facebook;
import com.humapcontents.mapp.AppManagement.FavoriteData;
import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeData;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;
import com.humapcontents.mapp.data.TwitterConstant;

public class AlbumTrackActivity extends MappBaseActivity implements OnClickListener {

	
	
	
	
	
	
	private static AlbumTrackActivity self;
	
	private String m_Title;
	private String m_Youtube ;
	private String m_Genre;
	private String m_Comment;
	private String m_Nick;
	private String m_ID;
	private Integer m_IDX;

	
	Integer m_ImageWidth;
	Integer m_ImageHeight;
	
	
	String[] m_Data = {"페이스북에 올리기", "트위터에 올리기" };
	
	Facebook facebook = new Facebook("463598960369393");
	
	private SharedPreferences mPrefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_track_layout);  // 인트로 레이아웃 출력     
        self = this;
        
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
        	mPrefs = getSharedPreferences( "facebooks" ,MODE_PRIVATE);
             
             String access_token = mPrefs.getString("access_token", null);
      
             long expires = mPrefs.getLong("access_expires", 0);
      
             if(access_token != null) {
      
                 facebook.setAccessToken(access_token);
      
             }
      
             if(expires != 0) {
      
                 facebook.setAccessExpires(expires);
      
             }
        }
        
        
        
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        
         
        ImageResize(R.id.main_layout);
        
        ImageResize(R.id.album_track_scroll_view);
        

        
        
        ImageBtnResize(R.id.title_popup);
        ImageResize(R.id.audition_detail_movie_back);
        ImageBtnResize(R.id.audition_detail_movie_favorite);
        ImageBtnResize(R.id.audition_detail_movie_play);
        ImageBtnResize(R.id.audition_detail_movie_like);
        
        
        ImageResize(R.id.audition_detail_movie_like_count);
        ImageBtnResize(R.id.audition_detail_profile_back);
        ImageResize(R.id.audition_detail_profile_img);
        ImageResize(R.id.audition_detail_profile_name);
        ImageResize(R.id.audition_detail_profile_cur);
        BottomMenuDown(true);
        AfterCreate( 2 );
       
        GetData();
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	ImageView imageview = (ImageView)findViewById(R.id.title_icon);
           
            imageview.setOnClickListener(this);
        }
    }
    
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
    
    public void RefreshUI()
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	// Back
    	{
    		m_IDX = _AppManager.m_AlbumTrackIndex;
    		GetImageSize();
    		ImageView image = (ImageView)findViewById(R.id.audition_detail_movie_back);
    		
    		image.setTag(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=TrackDetail&idx=" + m_IDX );
			BitmapManager.INSTANCE.loadBitmap(_AppManager.DEF_URL +  "/mapp/ImageLoad?page=TrackDetail&idx=" + m_IDX, image, m_ImageWidth, m_ImageHeight);
   			
   			
    	}
    	
    	
    	( (TextView)findViewById(R.id.title_name) ).setText( m_Title );
    	( (TextView)findViewById(R.id.title_desc) ).setText( _AppManager.m_AlbumDesc );
    	( (TextView)findViewById(R.id.audition_detail_profile_name) ).setText( m_Nick );
    	
    	( (TextView)findViewById(R.id.album_track_desc) ).setText( m_Comment );
    	
    	
 
    	
    }
    
    public void GetData()
    {
    	{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("idx", _AppManager.m_AlbumTrackIndex.toString());

					
					String strJSON = _AppManager.GetHttpManager().PostHTTPData(_AppManager.DEF_URL +  "/mapp/Album/Track", data);
					
					try
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("error").equals("0"))
						{
							
							m_Title= (json.getString("title"));
							m_Youtube = (json.getString("video"));
							m_Nick = (json.getString("nick"));
							m_ID = (json.getString("id"));
							m_Comment = (json.getString("lylics"));
							m_Genre = (json.getString("genre"));

							
							handler.sendEmptyMessage(0);
						}
						
					}
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler.sendMessage(handler.obtainMessage(1,"Error" ));
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
				
			{
				Toast.makeText(self
	                    .getApplicationContext(), 
	                    "토큰을 얻을수 없습니다. 설정화면으로 이동합니다.",
	                    Toast.LENGTH_LONG).show();
	        	
	        	Intent intent;
	            intent = new Intent().setClass(baseself, SettingMainActivity.class);
	            startActivity( intent ); 
			}
	
				break;
			case 10:
			{
				//( (TextView)findViewById(R.id.audition_detail_movie_like_count) ).setText( m_Like.toString() );
			}
				break; 
			case 20:
			{
				Toast.makeText(self
	                    .getApplicationContext(), 
	                    "트위터에 글을 남겼습니다.",
	                    Toast.LENGTH_LONG).show();
			}
				break;
				
			default:
				break;
			}

		}
    	
	};


	
    
	public void onClick(View v)
	{

        
        switch( v.getId() )
        {
        case R.id.title_icon:
        {
        	onBackPressed();
        	break;
        }
        case R.id.audition_detail_movie_favorite:
        {
        	
        	
        	// 데이터 저장 
        	
        	AppManagement _AppManager = (AppManagement) getApplication();
        	
        	String img = _AppManager.DEF_URL +  "/mapp/ImageLoad?page=TrackDetail&idx=" + m_IDX;
        	if (_AppManager.AddTrackData(m_IDX, m_Title, m_Nick, img , "track") == true)
        	{
        		Toast.makeText(self
	                    .getApplicationContext(), 
	                    "즐겨찾기 되었습니다.",
	                    Toast.LENGTH_LONG).show();
        	}
        	else
        	{
        		self.ShowAlertDialLog( self ,"에러" , "이미 즐겨찾기 되어 있습니다. " );
        	}
        	 
        	/*SharedPreferences preferences = getSharedPreferences( "album" ,MODE_PRIVATE);
        	
        	
        	Set<String> set = preferences.getStringSet("favorite_album", new HashSet<String>());
        	
        	/// 같은것이 있는지 검사 
        	Boolean bCheck = false; 
        	for ( int i = 0 ; i < set.size(); i++ )
        	{
        		if ( set.toArray()[i].equals(_AppManager.m_PublicIndex.toString()))
        		{
        			bCheck = true;
        		}
        	}
        	
        	if ( bCheck != true )
        	{
        		SharedPreferences.Editor editor = preferences.edit(); 
        		set.add(_AppManager.m_PublicIndex.toString());
        		
        		editor.putStringSet("favorite_album", set);
        		editor.commit();
        	}
        	else
        	{
        		self.ShowAlertDialLog( self ,"에러" , "이미 즐겨찾기 되어 있습니다. " );
        	}*/





        }
        	break;
        case R.id.audition_detail_movie_play:
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	
        	Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.parse(m_Youtube),"video/*");
			startActivity(i);
        	
        	/*if ( _AppManager.m_bYoutube )
        	{
        		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://"+ m_Youtube)); 
        		startActivity(i);
        	}
        	else
        	{
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://youtu.be/" + m_Youtube)));
        	}*/
        }
        	break;
        case R.id.audition_detail_movie_like:
        {
        	
        }
        	break;
        	
        case R.id.audition_detail_profile_back:
        {
        	// mypage
        }
        	break;
        case R.id.title_popup:
        {
        	{
    	        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
    	        
    	        alt_bld.setTitle("업로드 할곳을 선택 하세요");
    	        alt_bld.setSingleChoiceItems(m_Data, -1, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int item) 
    	            {
    	            	
    	            	if ( item ==  0)
    	            	{
    	            		publishStory();
    	            	}
    	            	else
    	            	{
    	            		Twitterwrite();
    	            	}
    	            	
    	                dialog.cancel();
    	                
    	            }
    	        });
    	        AlertDialog alert = alt_bld.create();
    	        alert.show();
    		}
        	
        }
        	break;
        	
        }
		
	}
	
	private void publishStory() {

		if(!facebook.isSessionValid())
    	{
			Toast.makeText(self
                    .getApplicationContext(), 
                    "세션이 닫혀있습니다. 페이스북 로그인을 다시해주세요. 설정화면으로 이동합니다.",
                    Toast.LENGTH_LONG).show();
        	
        	Intent intent;
            intent = new Intent().setClass(baseself, SettingMainActivity.class);
            startActivity( intent );
    	}
    	else
    	{
    		
    		mProgress.show();
    		Thread thread = new Thread(new Runnable()
    		{
    			public void run() 
    			{
    	    		Bundle postParams = new Bundle();
    	            postParams.putString("name", "Mapp For Android ICS");
    	            postParams.putString("caption", m_Title);
    	            postParams.putString("description", m_Comment);
    	            postParams.putString("link",  m_Youtube );
    	            //postParams.putString("picture", "http://img.youtube.com/vi/"+ m_Youtube+"/default.jpg");
    	            
    	            
    	            try {
    					facebook.request("me/feed", postParams, "POST");
    					handler.sendEmptyMessage(30);
    					
    				} catch (FileNotFoundException e) {
    					// TODO Auto-generated catch block
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
	
	public void Twitterwrite()
    {
    	{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					Twitterwrite1();
				}
				
			});
			thread.start();
		}
    }
	
	
	private void Twitterwrite1() {
		 
		AppManagement _AppManager = (AppManagement) getApplication();
		        String accessToken = TwitUtil.getAppPreferences(this, TwitterConstant.TWITTER_ACCESS_TOKEN);
		        String accessTokenSecret = TwitUtil.getAppPreferences(this, TwitterConstant.TWITTER_ACCESS_TOKEN_SECRET);
		
		         /*String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		         String fileName = "그림파일 이름";
		         InputStream is = null;*/
		 
		         try {
		             /*if (new File(path + File.separator + fileName).exists())
		                 is = new FileInputStream(path + File.separator + fileName);
		             else
		                 is = null;*/
		 
		        	 if ( _AppManager.mAccessToken == null && ( accessToken == null|| accessTokenSecret == null))
		        	 {
		        		 // 트위터 토큰을 얻지못함 로그인 페이지로...
		        		 
		        		 handler.sendEmptyMessage(3);
		        	 }
		        	 else
		        	 {
		        		 ConfigurationBuilder cb = new ConfigurationBuilder();
		        		 String oAuthAccessToken ="";
		        		 String oAuthAccessTokenSecret = "";
		        		 if ( accessToken == null )
		        		 {
		        			 oAuthAccessToken = _AppManager.mAccessToken.getToken();
		        		 }
		        		 else if ( _AppManager.mAccessToken != null )
		        		 {
		        			 oAuthAccessToken = accessToken;
		        		 }
		        		 else
		        		 {
		        			 handler.sendEmptyMessage(3);
		        		 }
		        		 
		        		 if ( accessTokenSecret == null )
		        		 {
		        			 oAuthAccessTokenSecret = _AppManager.mAccessToken.getTokenSecret();
		        		 }
		        		 else if ( _AppManager.mAccessToken != null )
		        		 {
		        			 oAuthAccessTokenSecret = accessTokenSecret;
		        		 }
		        		 
		        		 else                          
		        		 {
		        			 handler.sendEmptyMessage(3);
		        		 }
		        		 
		        		 
			             
		        		 
			             
			             String oAuthConsumerKey = TwitterConstant.TWITTER_CONSUMER_KEY;
			             String oAuthConsumerSecret = TwitterConstant.TWITTER_CONSUMER_SECRET;
			             cb.setOAuthAccessToken(oAuthAccessToken);
			             cb.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
			             cb.setOAuthConsumerKey(oAuthConsumerKey);
			             cb.setOAuthConsumerSecret(oAuthConsumerSecret);
			             Configuration config = cb.build();
			             OAuthAuthorization auth = new OAuthAuthorization(config);
			 
			             TwitterFactory tFactory = new TwitterFactory(config);
			             Twitter twitter = tFactory.getInstance();
			             /*ImageUploadFactory iFactory = new ImageUploadFactory(getConfiguration(TwitterConstant.TWITPIC_API_KEY));
			             ImageUpload upload = iFactory.getInstance(MediaProvider.TWITPIC, auth-);*/
			 
			             Log.d("TAG", "accessToken : " + accessToken + "// accessTokenSecret : " + accessTokenSecret);
			             
			             /*if (is != null && !accessToken.equals("STATE_IS_LOGOUT") && !accessTokenSecret.equals("STATE_IS_LOGOUT")) {
			                 String strResult = upload.upload("example.jpg", is, mEtContent.getText().toString());
			                 twitter.updateStatus(mEtContent.getText().toString() + " " + strResult);
			             } else */if (!accessToken.equals("STATE_IS_LOGOUT") && !accessTokenSecret.equals("STATE_IS_LOGOUT")) {
			                 Log.d("TAG", "글쓰기");
			                twitter.updateStatus( "Mapp Android Test " + m_Title+ "-"+ _AppManager.m_AlbumDesc + " Video"+  " " + m_Youtube);
			                handler.sendEmptyMessage(20);
			            } else {
			                 Log.d("TAG", "로그인 해라.");
			                 handler.sendEmptyMessage(3);
			             }
			             //mEtContent.setText("");
		        	 }
		             
		         } catch (Exception e) {
		             e.printStackTrace();
		             handler.sendEmptyMessage(1);
		             
		         } finally 
		         {
		             
		         }
		    }

	
	
	
	public void GetImageSize(  )
	{
		
		View imageview = (View)findViewById(R.id.audition_detail_movie_back);
    	Display defaultDisplay = ((WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = imageview.getLayoutParams().height;
		int imagewidth = imageview.getLayoutParams().width;
		
		FrameLayout.LayoutParams margin = (FrameLayout.LayoutParams )(imageview.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		m_ImageWidth = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		m_ImageHeight = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
	}
	
  
}


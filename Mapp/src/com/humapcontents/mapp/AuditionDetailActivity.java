package com.humapcontents.mapp;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Facebook;
import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeData;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;
import com.humapcontents.mapp.data.TwitterConstant;

public class AuditionDetailActivity extends MappBaseActivity implements OnClickListener {

	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	
	public class ReplyData
	{
		private Integer idx;
		private String id;
		private String nick;
		private String date;
		private String text;
	}
	
	public class DeteilRow
	{
		private int type;
		private String Text;
		private String nick;
		private String time;
		
	}
	
	
	private ArrayList<DeteilRow> m_ObjectArray;
	private DeteilRow_Adapter m_Adapter;
	private ListView m_ListView;
	
	
	private AuditionDetailActivity self;
	
	private String m_Title;
	private String m_Youtube ;
	private String m_Date;
	private String m_Comment;
	private String m_Nick;
	private String m_ID;
	private String m_Time;
	private String m_Category;
	private Integer m_Hit;
	private Integer m_Reply;
	private Integer m_Like;
	private String m_Thumname;
	private Bitmap m_Thum;
	
	
	Facebook facebook = new Facebook("463598960369393");
	
	private SharedPreferences mPrefs;
	
	private ReplyData[] m_ReplyData;
	
	String[] m_Data = {"페이스북에 올리기", "트위터에 올리기" };
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auditionlist_detail_layout);  // 인트로 레이아웃 출력     
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
        
        
       
        
        m_ListView = (ListView)findViewById(R.id.audition_info_listview);
        m_ObjectArray = new ArrayList<DeteilRow>();
		m_Adapter = new DeteilRow_Adapter(this, R.layout.audition_detail_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null); 
        ImageResize(R.id.main_layout);
        

        
        
        ImageBtnResize(R.id.title_popup);
        ImageResize(R.id.audition_detail_movie_back);
        ImageBtnResize(R.id.audition_detail_movie_favorite);
        ImageBtnResize(R.id.audition_detail_movie_play);
        ImageBtnResize(R.id.audition_detail_movie_like);
        
        
        ImageResize(R.id.audition_detail_movie_like_count);
        ImageResize(R.id.audition_info_listview);
        ImageBtnResize(R.id.audition_detail_profile_back);
        ImageResize(R.id.audition_detail_profile_img);
        ImageResize(R.id.audition_detail_profile_name);
        ImageResize(R.id.audition_detail_profile_cur);
        BottomMenuDown(true);
        AfterCreate( 1 );
       
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
    	_AppManager.m_ReplyDesc = m_Nick;
    	// Back
    	{
    		ImageView image = (ImageView)findViewById(R.id.audition_detail_movie_back);
   			
   			if ( m_Thum != null )
   			{
   				
   		        
   		        Drawable d =new BitmapDrawable(getResources(),m_Thum);
   		        
   		        // API 10
   		        image.setBackgroundDrawable( d );
   			}
    	}
    	
    	( (TextView)findViewById(R.id.audition_detail_movie_like_count) ).setText( m_Like.toString() );
    	( (TextView)findViewById(R.id.title_name) ).setText( m_Title );
    	( (TextView)findViewById(R.id.title_desc) ).setText( m_Nick );
    	( (TextView)findViewById(R.id.audition_detail_profile_name) ).setText( m_Nick );
    	
    	
    	{
    		{
    			DeteilRow item1 = new DeteilRow();
        		item1.type = 0 ; 
        		item1.Text = m_Comment;
        		m_ObjectArray.add(item1);
    		}
    		{
    			DeteilRow item1 = new DeteilRow();
        		item1.type = 1 ; 
        		
        		m_ObjectArray.add(item1);
    		}
    		
    		if ( m_ReplyData.length >0 )
    		{
    			for ( int i = 0;  i < m_ReplyData.length ; i++ )
    			{
    				DeteilRow item1 = new DeteilRow();
            		item1.type = 2 ;
            		item1.Text = "";
            		
            		item1.Text = m_ReplyData[i].text;
            		item1.time = m_ReplyData[i].date;
            		item1.nick = m_ReplyData[i].nick;
            		            		
            		m_ObjectArray.add(item1);
            		
            		DeteilRow item2 = new DeteilRow();
            		item2.type = 1 ; 
            		m_ObjectArray.add(item2);
            		
    			}
    		}
    		
    		{
    			DeteilRow item1 = new DeteilRow();
        		item1.type = 3 ; 
        		item1.Text = "댓글 더 보기/쓰기 ";
        		m_ObjectArray.add(item1);
        		
        		DeteilRow item2 = new DeteilRow();
        		item2.type = 1 ; 
        		m_ObjectArray.add(item2);
    		}
    		
    		m_Adapter.notifyDataSetChanged();
    		
    	}
    	
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

					data.put("idx", _AppManager.m_PublicIndex.toString());
					data.put("replies", "3" );
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Audition/Detail", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("errcode").equals("0"))
						{
							m_Title = (json.getString("title"));;
							m_Youtube =  (json.getString("video"));
							m_Date = (json.getString("date"));
							m_Comment = json.getString("comment");
							m_Nick = (json.getString("nick"));
							m_ID= (json.getString("id"));
							m_Time = (json.getString("time"));;
							m_Category = (json.getString("category"));;
							m_Hit = Integer.parseInt(json.getString("hit"));
							m_Reply = Integer.parseInt(json.getString("replies"));
							m_Like= Integer.parseInt(json.getString("likes"));
							
							
							String []  value = m_Youtube.split("youtu.be/");
							
							for ( int x =0 ;  x < value.length; x++ )
							{
								m_Youtube = value[x];
							}
							
							
							JSONArray usageList = (JSONArray)json.get("reply");
							
							m_ReplyData = new ReplyData[ usageList.length()];
							for(int i = 0; i < usageList.length(); i++)
							{
								JSONObject list = (JSONObject)usageList.get(i);
								m_ReplyData[i] = new ReplyData();
								m_ReplyData[i].idx = Integer.parseInt(list.getString("idx"));
								m_ReplyData[i].id = (list.getString("id"));
								m_ReplyData[i].date = (list.getString("date"));
								
								m_ReplyData[i].text = list.getString("text");
								m_ReplyData[i].nick = (list.getString("nick"));
							}
							
							if (  (json.getString("img")).equals("") )
							{
								
							}
							else
							{
								URL imgUrl = null;
								try 
								{
									imgUrl = new URL((json.getString("img")) );
								} catch (MalformedURLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								URLConnection conn = null;
								try {
									conn = imgUrl.openConnection();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								conn.connect();
								BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
								m_Thum = BitmapFactory.decodeStream(bis);
								bis.close();									
							}
							
							
							handler.sendEmptyMessage(0);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(1,(json.getString("errcode")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler.sendMessage(handler.obtainMessage(1,"Error" ));
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(2302);
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
				( (TextView)findViewById(R.id.audition_detail_movie_like_count) ).setText( m_Like.toString() );
			}
				break; 
			case 20:
				Toast.makeText(self
	                    .getApplicationContext(), 
	                    "글이 잘 전송되었습니다.",
	                    Toast.LENGTH_LONG).show();
				break;
			case 30:
			{
				Toast.makeText(self
	                    .getApplicationContext(), 
	                    "글이 잘 전송되었습니다.",
	                    Toast.LENGTH_LONG).show();
			}
				break;
			case 2302:
			{
				Toast.makeText(self
	                    .getApplicationContext(), 
	                    "이미지를 로드할수 없습니다.",
	                    Toast.LENGTH_LONG).show();
			}
			break;
				
			default:
				break;
			}

		}
    	
	};
	
	public void SetLike()
	{
	  	{
				final  AppManagement _AppManager = (AppManagement) getApplication();
				mProgress.show();
				Thread thread = new Thread(new Runnable()
				{

					public void run() 
					{

						Map<String, String> data = new HashMap  <String, String>();

						data.put("idx", _AppManager.m_PublicIndex.toString());
						data.put("UUID", android.os.Build.SERIAL );
						
						String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Audition/Like", data);

						try 
						{
							JSONObject json = new JSONObject(strJSON);
							if(json.getString("errcode").equals("0"))
							{
								m_Like= Integer.parseInt(json.getString("likes"));
								handler.sendEmptyMessage(10);
							}
							else 
							{
								// 에러 메세지를 전송한다. 
								handler.sendMessage(handler.obtainMessage(1,(json.getString("errcode")) ));
								return ;
							}
						} catch (JSONException e) 
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
	
	
    
	public void onClick(View v)
	{

        
        switch( v.getId() )
        {
        case R.id.title_icon:
        {
        	onBackPressed();
        	break;
        }
       /* case R.id.title_icon2:
        {
        	publishStory();
        	break;
        }*/
        case R.id.audition_detail_movie_favorite:
        {
        	// 데이터 저장 
        	 AppManagement _AppManager = (AppManagement) getApplication();
        	 
        	SharedPreferences preferences = getSharedPreferences( "audition" ,MODE_PRIVATE);
        	
        	Set<String> set = preferences.getStringSet("favorite_audition", new HashSet<String>());
        	
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
        		
        		editor.putStringSet("favorite_audition", set);
        		editor.commit();
        	}
        	else
        	{
        		self.ShowAlertDialLog( self ,"에러" , "이미 즐겨찾기 되어 있습니다. " );
        	}





        }
        	break;
        case R.id.audition_detail_movie_play:
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	
        	if ( _AppManager.m_bYoutube )
        	{
        		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://"+ m_Youtube)); 
        		startActivity(i);
        	}
        	else
        	{
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://youtu.be/" + m_Youtube)));
        	}
        }
        	break;
        case R.id.audition_detail_movie_like:
        {
        	SetLike();
        }
        	break;
        	
        case R.id.audition_detail_profile_back:
        {
        	// mypage
        	AppManagement _AppManager = (AppManagement) getApplication();
        	_AppManager.m_MyPageID = m_ID;
        	Intent intent;
            
            intent = new Intent().setClass(self, MypageActivity.class);
            
            
            startActivity( intent ); 
        	
        	
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
	
	
	 @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
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
				                twitter.updateStatus( "Mapp Android Test " + m_Title+ "-"+ _AppManager.m_AlbumDesc + " Video"+  " " + "http://youtu.be/"+ m_Youtube);
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
    	            postParams.putString("link", "http://youtu.be/" + m_Youtube );
    	            postParams.putString("picture", "http://img.youtube.com/vi/"+ m_Youtube+"/default.jpg");
    	            
    	            
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
	
	
	
	
    public class DeteilRow_Adapter extends ArrayAdapter<DeteilRow>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<DeteilRow> mList;
		private LayoutInflater mInflater;
		
    	public DeteilRow_Adapter(Context context, int layoutResource, ArrayList<DeteilRow> mTweetList)
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
    		final DeteilRow mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				LinearLayout frameBar1 = (LinearLayout)convertView.findViewById(R.id.audition_deteil_row_1);
				LinearLayout frameBar2 = (LinearLayout)convertView.findViewById(R.id.audition_deteil_row_2);
				LinearLayout frameBar3 = (LinearLayout)convertView.findViewById(R.id.audition_deteil_row_3);
				LinearLayout frameBar4 = (LinearLayout)convertView.findViewById(R.id.audition_deteil_row_4);
				frameBar1.setVisibility(View.GONE);
				frameBar2.setVisibility(View.GONE);
				frameBar3.setVisibility(View.GONE);
				frameBar4.setVisibility(View.GONE);
				
				switch( mBar.type )
				{
				case 0:
				{
					frameBar1.setVisibility(View.VISIBLE);
					((TextView)convertView.findViewById(R.id.audition_deteil_row_intro)).setText(mBar.Text);
					break;
				}
				
				case 1:
				{
					frameBar2.setVisibility(View.VISIBLE);
					break;
				}
				
				case 2:
				{
					frameBar3.setVisibility(View.VISIBLE);
					((TextView)convertView.findViewById(R.id.audition_deteil_row_reply_nick)).setText(mBar.nick);
					((TextView)convertView.findViewById(R.id.audition_deteil_row_reply_time)).setText(mBar.time);
					((TextView)convertView.findViewById(R.id.audition_deteil_row_reply)).setText(mBar.Text);
					break;
				}
				
				case 3:
				{
					frameBar4.setVisibility(View.VISIBLE);
					((TextView)convertView.findViewById(R.id.audition_deteil_row_replybtn)).setText(Html.fromHtml("<b>"+mBar.Text+ "</b>"));
					
					
					// 댓글 더보기 & 쓰기 
					frameBar4.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
					
							_AppManager.m_ReplyIndex = _AppManager.m_PublicIndex;
							_AppManager.m_ReplyTitle = m_Title;
							_AppManager.m_ReplyDesc = m_Nick;
							Intent intent;
					        intent = new Intent().setClass(self, AuditionReplyActivity.class);
					        startActivity( intent );

						}
					});
					break;
				}
				
				}
				
			}
			return convertView;
		}
    }
}


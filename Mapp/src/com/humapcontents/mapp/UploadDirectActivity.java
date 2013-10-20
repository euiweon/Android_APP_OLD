package com.humapcontents.mapp;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import com.humapcontents.mapp.AppManagement.FavoriteData;
import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeData;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;

public class UploadDirectActivity extends MappBaseActivity implements OnClickListener {

	private UploadDirectActivity self;

	private final int SELECT_IMAGE = 1;
	private final int SELECT_MOVIE = 2;
	ProgressDialog mProgress2;
	
	String m_SelectCategory = "vocal";
	
	String filePath= "";
	String authToken = "";
	String filename ="";
	
	String VideoID = "";
	String YoutubeAddr = "";
	
	Uri fileUri;

	String[] m_Data = {"vocal", "drums","guitar","bass","piano", "dance", "midi", "performance" };
	String[] m_Data2 = {"Vocal", "Drums","Guitar","Bass","Piano", "Dance", "Midi", "Performance" };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audition_upload2_layout);  // 인트로 레이아웃 출력     

        self = this;
        
        
        
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
		
       
        
      

        ImageBtnResize(R.id.title_popup);
        ImageResize(R.id.main_layout);
        ImageResize(R.id.upload_tag_1);
        ImageResize(R.id.upload_tag_2);
        
        ImageResize(R.id.upload_tag_3);
        ImageResize(R.id.upload_tag_4);
        ImageBtnResize(R.id.upload_url);

        

        
        
        
        ImageResize(R.id.upload_url_line);
        ImageBtnResize(R.id.upload_cate);
        
        ImageResize(R.id.upload_cate_line);
        ImageResize(R.id.upload_name);
        ImageResize(R.id.upload_name_line);
        ImageResize(R.id.upload_intro);
 

        
       
        BottomMenuDown(true);
        AfterCreate( 1 );

        
        ImageBtnEvent(R.id.title_icon , this);
        
        
        
        {
        	doSelectMovie();
        }
        
        
    }
    
 // 동영상선택
    private void doSelectMovie()
    {   
    	Intent i = new Intent(Intent.ACTION_GET_CONTENT);
    	i.setType("video/*"); 
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    
    	try    
    	{        
    		startActivityForResult(i, SELECT_MOVIE);
    		} 
    	catch (android.content.ActivityNotFoundException e)   
    	{        
    		e.printStackTrace();   
    	}
    } 
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {    
    	super.onActivityResult(requestCode, resultCode, intent);
    	if (resultCode == RESULT_OK) 
    	{      
    		if (requestCode == SELECT_IMAGE)       
    		{            
    			Uri uri = intent.getData();            
    			String path = getPath(uri); 
    			String name = getName(uri); 
    			String uriId = getUriId(uri);
    			Log.e("###", "실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId); 
    		}       
    		else if (requestCode == SELECT_MOVIE)        
    		{           
    			Uri uri = intent.getData(); 
    			
    			String path = getPath(uri);          
    			String name = getName(uri);           
    			String uriId = getUriId(uri);            
    			Log.e("###", "실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId);
    			filePath = path;
    			filename = name;
    			fileUri = uri;
    			((TextView)findViewById(R.id.upload_url)).setText(name);
    		}   
    	}
    } 
    
 // 파일명 찾기
    private String getName(Uri uri)
    {    
    	String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
    	Cursor cursor = managedQuery(uri, projection, null, null, null); 
    	int column_index = cursor .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
    	cursor.moveToFirst();    
    	return cursor.getString(column_index);
    } 
    // uri 아이디 찾기
    private String getUriId(Uri uri)
    {    
    	String[] projection = { MediaStore.Images.ImageColumns._ID }; 
    	Cursor cursor = managedQuery(uri, projection, null, null, null);
    	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
    	cursor.moveToFirst();   
    	return cursor.getString(column_index);
    }
    
    
  
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
    

    

	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
	
		switch(arg0.getId() )
		{
		case R.id.upload_url:
			doSelectMovie();
			break;
		case R.id.title_icon:
		{
			onBackPressed();
		}
		break;
		case R.id.title_popup:
			GetToken();
			

			break;
		case R.id.upload_cate:
		{
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	        
	        alt_bld.setTitle("Select a Category");
	        alt_bld.setSingleChoiceItems(m_Data2, -1, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) 
	            {
	            	
	            	
	            	m_SelectCategory = m_Data[item];
	            	
	            	((TextView)findViewById(R.id.upload_cate)).setText(m_Data2[item]);
	            	
	            	
	                dialog.cancel();
	                
	            }
	        });
	        alt_bld.show();
		}
			
			break;
		}
		
	}
	
	public void RefreshUI()
	{
		
        


	}
	
	public void YoutubeUpload()
	{
		new YoutubeUploadTask().execute();
	}
	
	
	
	public void GetToken()
    {
    	final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				handler.sendMessage(handler.obtainMessage(20,getAuthToken()  ));
				
			}
		});
		thread.start();
		
    }
    
    public String getAuthToken() 
	{

        
       
 
        Log.v("C2DM", "AuthToken : " + authToken);
 
       // if (authToken == null)
        {
            StringBuffer postDataBuilder = new StringBuffer();
             postDataBuilder.append("&Email=mapp.contents@gmail.com");
            postDataBuilder.append("&Passwd=mapptown8"); 
            postDataBuilder.append("&service=youtube");
            postDataBuilder.append("&source=MappContents");
 
            byte[] postData = null;
			try {
				postData = postDataBuilder.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            URL url = null;
			try {
				url = new URL("https://www.google.com/accounts/ClientLogin");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            try {
				conn.setRequestMethod("POST");
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",
                    Integer.toString(postData.length));
 
            // 출력스트림을 생성하여 서버로 송신
            OutputStream out = null;
			try {
				out = conn.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
            	if ( out != null)
            		out.write(postData);
            	else
            	{
            		return "";

            	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            // 서버로부터 수신받은 스트림 객체를 버퍼에 넣어 읽는다.
            BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(
				        conn.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            String sIdLine = null;
			try {
				sIdLine = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String lsIdLine = null;
			try {
				lsIdLine = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String authLine = null;
			try {
				authLine = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            authToken = authLine.substring(5, authLine.length());
        }

        return authToken;
    }
    
	
	public void UploadData()
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

					data.put("id", _AppManager.m_LoginID);
					data.put("skey", _AppManager.m_MappToken);
					
					EditText intro = (EditText)findViewById(R.id.upload_intro);
					EditText name = (EditText)findViewById(R.id.upload_name);
					TextView url = (TextView)findViewById(R.id.upload_url);
					
					String URLAddress =  url.getText().toString();
					
					
					
					
					if ( intro.getText().toString().length() > 1  )
					{
						
					}
					else
					{
						handler.sendEmptyMessage(4);
						return;
					}
					
					if ( name.getText().toString().length() > 1 )
					{
						
						
					}
					else
					{
						handler.sendEmptyMessage(5);
						return;
					}
					
					YoutubeAddr = "http://youtu.be/"+ VideoID;
					

					data.put("img", "http://img.youtube.com/vi/"+ VideoID+"/hqdefault.jpg");
					data.put("video", YoutubeAddr);
					data.put("title", _AppManager.GetHttpManager().EncodeString(name.getText().toString()));
					data.put("comment", _AppManager.GetHttpManager().EncodeString(intro.getText().toString()));
					data.put("deletable", "N" );
					data.put("category", m_SelectCategory );
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Audition/Upload", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{


						

							handler.sendEmptyMessage(0);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(1,_AppManager.GetError(json.optInt("errcode")) ));
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
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				onBackPressed();
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 2:

				break;
				
			case 3:
	
				break;
			case 4:
				self.ShowAlertDialLog( self ,"에러" , "영상 소개를 입력해주세요. " );
				break;
			case 5:
				self.ShowAlertDialLog( self ,"에러" , "이름을 입력해주세요. " );
				break;
			case 6:
				self.ShowAlertDialLog( self ,"에러" , "정확한 URL을 입력해주세요 " );
				break;
			case 20:
				if ( msg.obj.equals(""))
					
				{
            		Toast.makeText(self, "토큰을 얻을수 없습니다. ", Toast.LENGTH_SHORT);
            		
				}
				else
				{
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.m_YoutubeToken = (String) msg.obj;
					Log.v("Youtube", "AuthToken : " + _AppManager.m_YoutubeToken);
					
					YoutubeUpload();
				}
				break;
			case 30:
				UploadData();
				break;
			default:
				break;
			}

		}
    	
	};
	
	public String GetIDYoutubeXML( String xmlString)
	{
		String VideoID = "";
		
		XmlPullParserFactory xmlpf = null;
		
		xmlString = xmlString.replaceAll("\r", "");
		xmlString = xmlString.replaceAll("\n", "");
		
		//try 
		{
			// XML 파서를 초기화

			try {
				xmlpf = XmlPullParserFactory.newInstance();
			} catch (XmlPullParserException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
			XmlPullParser xmlp = null;
			try {
				xmlp = xmlpf.newPullParser();
			} catch (XmlPullParserException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			try {
				xmlp.setInput(new StringReader(xmlString));
			} catch (XmlPullParserException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			// 파싱에 발생하는 event?를 처리하기 위한 메소드
            int eventType = 0;
			try {
				eventType = xmlp.getEventType();
			} catch (XmlPullParserException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            
            CURR_XML_STATE estate = CURR_XML_STATE.CURR_XML_UNKNOWN;
            
            // XML문서 읽기를 반복함미다.
            while( eventType != XmlPullParser.END_DOCUMENT )
            {

                switch ( eventType ) 
                {
                case XmlPullParser.START_DOCUMENT: // 문서 시작 태그를 만난 경우
                case XmlPullParser.END_DOCUMENT: // 문서 끝 태그를 만난 경우
                    break;
                case XmlPullParser.START_TAG: // 쌍으로 구성된 태그의 시작을 만난 경우
                    // 요소명 체크
                    if ( xmlp.getName().equals("yt:videoid") )
                    { 
                        estate = CURR_XML_STATE.CURR_XML_NO;
                    }

                    break;
                case XmlPullParser.END_TAG: // 쌍으로 구성된 태그의 끝을 만난 경우
                    break;
                case XmlPullParser.TEXT: // TEXT로 이루어진 내용을 만난 경우
                    switch ( estate )
                    {
                    case CURR_XML_NO:
	                {
	                	VideoID = xmlp.getText();
	                }
	                    break;
					default:
						break;


                    }
                    break;
                } // switch end
                
                // 다음 내용을 읽어옵니다
                try
                {
					try {
						eventType = xmlp.next();
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
                catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            } // while end
		}
		return VideoID;
	}
	
	
	
	public class YoutubeUploadTask extends AsyncTask<Void, Integer, Void> 
	{     
	     
		Integer mValue = 0;
	     //doInBackground(Params...) 메소드 실행 전 호출
	     //UI Thread
	     @Override
	     protected void onPreExecute() 
	     {
	    	 mValue = 0;
	    	 mProgress2 = new ProgressDialog(self);
	    	 mProgress2.setTitle("유튜브 업로드중 ..");
	    	 mProgress2.setMessage("잠시만 기다리세요..");
	    	 mProgress2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    	 mProgress2.setCancelable(false);
	    	 mProgress2.setProgress(0);
	    	 mProgress2.setButton("Cancel", new DialogInterface.OnClickListener() 
	    	 {    
	    		 public void onClick(DialogInterface dialog, int which) {
	    			 cancel(true);
	    		 }
	    	 });
	    	 mProgress2.show();
	     }
	     
	     //BackGround Thread
	  // 백그라운드에서 작업      
	     protected Void doInBackground(Void... params)      
	     {    
	    	 AppManagement _AppManager = (AppManagement) getApplication();
	    	 EditText intro = (EditText)findViewById(R.id.upload_intro);
			EditText name = (EditText)findViewById(R.id.upload_name);
				
			self.VideoID =  _AppManager.GetHttpManager().YoutubeUpload(self, 
					authToken, filename, filePath, fileUri, name.getText().toString(), intro.getText().toString());
			
			self.VideoID = GetIDYoutubeXML(self.VideoID );
	    	               
	    	 return null;
	     }
	          
	     //publishProgress(Progress...) 메소드로 호출 됨.
	     //UI Thread
	     @Override
	     protected void onProgressUpdate(Integer... values) 
	     {
	    	 //mProgress2.setProgress(values[0]);
	    	 
	     }    
	     
	     //doInBackground(Params...) 메소드의 리턴 값 처리
	     //UI Thread
	     protected void onPostExecute(Void  result) {
	    	 mProgress2.dismiss();
	    	 Toast.makeText(self, "유튜브 업로드에 성공하였습니다. .", Toast.LENGTH_SHORT).show();
	    	 handler.sendEmptyMessage(30);
	     }     
	     
	     //cancel(boolean) 호출 시 또는 doInBackground(Object[]) 완료 후 처리.
	     //UI Thread
	     @Override
	     protected void onCancelled() 
	     {
	    	 mProgress2.dismiss();
	    	 Toast.makeText(self, "업로드를 취소하였습니다.", Toast.LENGTH_SHORT).show();
	     }


	}
}

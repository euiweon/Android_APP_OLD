package oppa.rcsoft.co.kr;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

// 프로필 수정 ( 언니회원 전용 )
public class My_Profile extends  BaseActivity  implements OnClickListener
{
	
	Bitmap m_IconBitmap;
	Bitmap [] girl_Image;
	
	String m_Ki;
	String m_Weight;
	String m_hams;
	
	Uri   iConUri;
	public static My_Profile  self;
	Uri	[] picture;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        
        setContentView(R.layout.my_profile);          // 인트로 레이아웃 출력   
        self = this;
        
        
        picture = new Uri[6];
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		
        Button WriteBtn = (Button)findViewById(R.id.profile_save_btn);
        WriteBtn.setOnClickListener(this);
        
        ImageButton Pic1Btn = (ImageButton)findViewById(R.id.profile_image1_btn);
        Pic1Btn.setOnClickListener(this);
        ImageButton Pic2Btn = (ImageButton)findViewById(R.id.profile_image2_btn);
        Pic2Btn.setOnClickListener(this);
        ImageButton Pic3Btn = (ImageButton)findViewById(R.id.profile_image3_btn);
        Pic3Btn.setOnClickListener(this);
        
        ImageButton Pic4Btn = (ImageButton)findViewById(R.id.profile_image4_btn);
        Pic4Btn.setOnClickListener(this);
        ImageButton Pic5Btn = (ImageButton)findViewById(R.id.profile_image5_btn);
        Pic5Btn.setOnClickListener(this);
        ImageButton Pic6Btn = (ImageButton)findViewById(R.id.profile_image6_btn);
        Pic6Btn.setOnClickListener(this);
        ImageButton PicIconBtn = (ImageButton)findViewById(R.id.profile_icon_btn);
        PicIconBtn.setOnClickListener(this);
        
        
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// Tab
		{
    		Button TabBTN2 = (Button)findViewById(R.id.mypage1_main_tab_btn_1);
    		TabBTN2.setOnClickListener(this);
    		Button TabBTN3 = (Button)findViewById(R.id.mypage1_main_tab_btn_2);
    		TabBTN3.setOnClickListener(this);
    		Button TabBTN4 = (Button)findViewById(R.id.mypage1_main_tab_btn_3);
    		TabBTN4.setOnClickListener(this);
    		Button TabBTN5 = (Button)findViewById(R.id.mypage1_main_tab_btn_4);
    		TabBTN5.setOnClickListener(this);
		}
		
        RefreshUI();
        
    }
    
    
    public void RefreshUI()
    {
    	GetMyInfo1();
    }
    
    
    public void onClick(View v )
    {
    	switch(v.getId())
    	{

	    	case R.id.profile_save_btn:
	    	{
	    		// 데이터 전송후 뒤로 가기 누른다. 
	    		SendWrite();
	    	}
	    		break;
	    	
	    	case R.id.profile_image1_btn:
	    	{
	    		doSelectImage(1);
	    	}
	    		break;
	    		
	    	case R.id.profile_image2_btn:
	    	{
	    		doSelectImage(2);
	    	}
	    		break;
	    		
	    	case R.id.profile_image3_btn:
	    	{
	    		doSelectImage(3);
	    	}
	    		break;
	    		
	    	case R.id.profile_image4_btn:
	    	{
	    		doSelectImage(4);
	    	}
	    		break;
	    		
	    	case R.id.profile_image5_btn:
	    	{
	    		doSelectImage(5);
	    	}
	    		break;
	    		
	    	case R.id.profile_image6_btn:
	    	{
	    		doSelectImage(6);
	    	}
	    		break;
	    		
	    	case R.id.profile_icon_btn:
	    	{
	    		doSelectImage(7);
	    	}
	    		break;
	    		
	    	case R.id.mypage1_main_tab_btn_2:

	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, Shop_MainList.class);
		        startActivity( intent );  
	    	}
	    		break;
	    		
	    	case R.id.mypage1_main_tab_btn_3:
	 
	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, ToyMainList.class);
		        startActivity( intent );   
	    	}
	    		break;
	    		
	    	case R.id.mypage1_main_tab_btn_4:
	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, Community_Main.class);
		        startActivity( intent );  
	    	}
	    		break;
	    		
	    	case R.id.mypage1_main_tab_btn_1:
	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, Home.class);
		        startActivity( intent );  
	    	}
	    		break;
	    		
	    	default:
	    		break;
    	}
    }
    
	
	public void doSelectImage( int index )
	{    
		Intent i = new Intent(Intent.ACTION_GET_CONTENT); 
	  	i.setType("image/*");
	  	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	  	startActivityForResult(i, index);
	} 
    
    public void GetMyInfo1()
    {
    	mProgress.show();
		final MyInfo myApp = (MyInfo) getApplication();
		Thread thread = new Thread(new Runnable()
		{
			public void run()
			{
				Map<String, String> data = new HashMap  <String, String>();
				data.put("gr_id",myApp.m_AccountData.ID );
				data.put("return_type", "json");
			
				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getGirlInfo.php", data);

				JSONObject json = null;
				try {
					json = new JSONObject(strJSON);
				} 
				catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try 
				{
					if(json.getString("result").equals("ok"))
					{

						String temp = "";
						String temp2 = json.getString("gr_img");
						
						m_Ki = myApp.DecodeString(json.getString("gr_height"));
						m_Weight = myApp.DecodeString(json.getString("gr_weight"));
						m_hams = myApp.DecodeString(json.getString("gr_character"));
						
						if ( !temp2.equals(temp) )
						{
							URL imgUrl = new URL(myApp.DecodeString(json.getString("gr_img")));
							URLConnection conn = imgUrl.openConnection();
							conn.connect();
							BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
							m_IconBitmap = BitmapFactory.decodeStream(bis);
							bis.close();
						}
						handler.sendEmptyMessage(1);

					}
					else if(json.getString("result").equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(10,myApp.DecodeString(json.getString("result_text")) ));
					}
				}  
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendMessage(handler.obtainMessage(10,"JSON Parsing Error" ));
				} 
				catch (MalformedURLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
    }
    
    
    void GetMyInfo2()
   	{
   		mProgress.show();
   		final MyInfo myApp = (MyInfo) getApplication();
   		Thread thread = new Thread(new Runnable()
   		{
   			public void run() 
   			{
   				Map<String, String> data = new HashMap  <String, String>();
   				data.put("gr_id",myApp.m_AccountData.ID );
   				data.put("return_type", "json");

   				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getGirlPicture.php", data);
   				JSONObject json = null;
   				try 
   				{
   					json = new JSONObject(strJSON);
   				} 
   				catch (JSONException e1) 
   				{
   					// TODO Auto-generated catch block
   					e1.printStackTrace();
   				}
   				try 
   				{
   					if(json.getString("result").equals("ok"))
   					{

   						JSONArray usageList = (JSONArray)json.get("list");

   						if ( usageList.length() != 0 )
   						{
   							girl_Image = new Bitmap[usageList.length()];
   						}
   						for(int i = 0; i < usageList.length(); i++)
   						{   						
   							JSONObject list = (JSONObject)usageList.get(i);
   							
   							
   							String temp = "";
							String temp2 = list.getString("img");
							
							if ( !temp2.equals(temp) )
							{
   								URL imgUrl = new URL(myApp.DecodeString(list.getString("img")));
   								URLConnection conn = imgUrl.openConnection();
   								conn.connect();
   								BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
   								girl_Image[i] = BitmapFactory.decodeStream(bis);
   								bis.close();  		
							}
							
							
   						}
   						handler.sendEmptyMessage(0);
   					}
   					else if(json.getString("result").equals("error"))
   					{
   						handler.sendMessage(handler.obtainMessage(6,myApp.DecodeString(json.getString("result_text")) ));
   					}
   				} catch (JSONException e1) {
   					// TODO Auto-generated catch block
   					e1.printStackTrace();
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
    

    final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:
			{
				mProgress.dismiss();
				
				for ( int i = 0 ; i < girl_Image.length; i++)
 				{
 					if ( girl_Image[i] != null)
 					{
 	 					ImageView image = (ImageView)findViewById(R.id.profile_image1_btn + i);					
 	 					image.setImageBitmap( girl_Image[i]);					
 					}
 				}
			}
				break;
			case 1:
			{
				// 
				{
					EditText KiText = (EditText)findViewById(R.id.profile_ki);
					KiText.setText(m_Ki);
					EditText WeightText = (EditText)findViewById(R.id.profile_weight);
					WeightText.setText(m_Weight);
					EditText KaramaText = (EditText)findViewById(R.id.profile_karama);
					KaramaText.setText(m_hams);
					ImageButton Icon = (ImageButton)findViewById(R.id.profile_icon_btn);
					Icon.setImageBitmap(m_IconBitmap);
				}
				mProgress.dismiss();
				GetMyInfo2();
			}
				break;
			case 8 :
				mProgress.dismiss();
				break;			
			case 10:
				mProgress.dismiss();
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
				

			}
		}
	};
	
    void SendWrite(  )
    {
    	mProgress.show();
    	
    	final MyInfo myApp = (MyInfo) getApplication();

    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					
					
					EditText KiText = (EditText)findViewById(R.id.profile_ki);
					m_Ki = KiText.getText().toString();
					EditText WeightText = (EditText)findViewById(R.id.profile_weight);
					m_Weight = WeightText.getText().toString();
					EditText KaramaText = (EditText)findViewById(R.id.profile_karama);
					m_hams = KaramaText.getText().toString();
					

					MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );
					

 
					
					Charset charset = Charset.forName("UTF-8"); 
					ContentBody brFile1 = null;
					ContentBody brFile2 = null;
					ContentBody brFile3 = null;
					ContentBody brFile4 = null;
					ContentBody brFile5 = null;
					ContentBody brFile6 = null;
					ContentBody brFile7 = null;
					

					if ( self.picture[5] != null )
					{
						File file3 = new File(getPath(self.picture[5]));
						brFile6 = new FileBody(file3 , "image/jpeg" );
						entity.addPart("mb_images6" , brFile6);
					}
					
					if ( self.picture[4] != null )
					{
						File file3 = new File(getPath(self.picture[3]));
						brFile5 = new FileBody(file3 , "image/jpeg" );
						entity.addPart("mb_images5" , brFile5);
					}
					
					
					if ( self.picture[3] != null )
					{
						File file3 = new File(getPath(self.picture[3]));
						brFile4 = new FileBody(file3 , "image/jpeg" );
						entity.addPart("mb_images4" , brFile4);
					}
					
					
					if ( self.picture[2] != null )
					{
						File file3 = new File(getPath(self.picture[2]));
						brFile3 = new FileBody(file3 , "image/jpeg" );
						entity.addPart("mb_images3" , brFile3);
					}
					
					
					if ( self.picture[1] != null )
					{
						File file3 = new File(getPath(self.picture[1]));
						brFile2 = new FileBody(file3 , "image/jpeg" );
						entity.addPart("mb_images2" , brFile2);
					}
					
					
					if ( self.picture[0] != null )
					{
						File file3 = new File(getPath(self.picture[0]));
						brFile1 = new FileBody(file3 , "image/jpeg" );
						entity.addPart("mb_images1" , brFile1);
					}

					
					if (iConUri != null )
					{
						File file1 = new File(getPath(iConUri));
						brFile1 = new FileBody(file1 , "image/jpeg" );
						
						entity.addPart("mb_icon" , brFile7);
					}
					/////////////////////////////////////////////////////////////////////
					{
						try {
							entity.addPart("gr_height" , new StringBody(m_Ki, charset));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						try {
							entity.addPart("gr_weight" , new StringBody(m_Weight, charset));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						try {
							entity.addPart("gr_character" , new StringBody(m_hams, charset));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						try {
							entity.addPart("return_type" , new StringBody("json", charset));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}					

					
					String strJSON = myApp.PostHTTPFileData("http://oppa.rcsoft.co.kr/api/oppa_saveGirlProfile.php", entity);	
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

							handler.sendEmptyMessage(8);
							
						}
						else if(json.getString("result").equals("error"))
						{
							handler.sendMessage(handler.obtainMessage(10,myApp.DecodeString(json.getString("result_text")) ));
						}
					}
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			});
			thread.start();
    	}
    		
    }
    
    
	
	
	 public void SetImage( Integer index , Uri uri)
	    {
			String path = getPath(uri);   
			String name = getName(uri);
			String uriId = getUriId(uri);
			
			Log.e("###", index.toString() + " - 실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId);   
			
			// 비트맵 입력
			ImageButton PicBtn = null;
			if ( index == 1 )
			{
				PicBtn = (ImageButton)findViewById(R.id.profile_image1_btn);
				picture[0] = uri;
			}
			else if ( index == 2)
			{
				PicBtn = (ImageButton)findViewById(R.id.profile_image2_btn);
				picture[1] = uri;
			}
			else if ( index == 3)
			{
				PicBtn = (ImageButton)findViewById(R.id.profile_image3_btn);
				picture[2] = uri;
			}
			else if ( index == 4)
			{
				PicBtn = (ImageButton)findViewById(R.id.profile_image4_btn);
				picture[3] = uri;
			}
			
			else if ( index == 5)
			{
				PicBtn = (ImageButton)findViewById(R.id.profile_image5_btn);
				picture[4] = uri;
			}
			
			
			else if ( index == 6)
			{
				PicBtn = (ImageButton)findViewById(R.id.profile_image6_btn);
				picture[5] = uri;
			}
			else if ( index == 7)
			{
				PicBtn = (ImageButton)findViewById(R.id.profile_icon_btn);
				iConUri = uri;
			}
			Bitmap selPhoto = null;
			try {
				selPhoto = Images.Media.getBitmap( getContentResolver(), uri );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			PicBtn.setImageBitmap(selPhoto);
	    }
	 
	    
	    // 실제 경로 찾기
	    private String getPath(Uri uri)
	    {    
	    	String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
	    } 
	    // 파일명 찾기
	    private String getName(Uri uri)
	    {    
	    	String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
	        Cursor cursor = managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
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

}

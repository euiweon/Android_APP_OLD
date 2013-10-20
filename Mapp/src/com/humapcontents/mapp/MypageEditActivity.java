package com.humapcontents.mapp;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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



import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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


public class MypageEditActivity extends MappBaseActivity implements OnClickListener {

	private MypageEditActivity self;


	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;






	 
	
	Integer m_ImageWidth;
	Integer m_ImageHeight;


	
	
	public String nick;
	public String name;
	public String commment;
	public String location;
	public String age;
	
	public Bitmap bitmap; 
	private Uri mImageCaptureUri = null;
	private ImageView mPhotoImageView;

	
	public String filepath = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_modi_layout);  
        self = this;
        
       
        


     // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        GetImageSize();
        
        
        mPhotoImageView = (ImageView)findViewById(R.id.album_detail_gallery);

        ImageResize(R.id.main_layout);
        ImageBtnResize(R.id.title_popup);
        
        
        
        ImageResize(R.id.album_detail_gallery);
        ImageBtnResize(R.id.album_desc);
        ImageBtnResize(R.id.album_desc_2);
        
        ImageResize(R.id.album_line);
        ImageResize(R.id.mypage_list);
        ImageResize(R.id.mypage_modi_name);
        ImageResize(R.id.mypage_modi_name_line);
        ImageResize(R.id.mypage_modi_age);
        ImageResize(R.id.mypage_modi_age_line);
        ImageResize(R.id.mypage_modi_loca);
        ImageResize(R.id.mypage_modi_loca_line);
        
        


        BottomMenuDown(true);
        AfterCreate( 6 );
        
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	ImageView imageview = (ImageView)findViewById(R.id.title_icon);
           
            imageview.setOnClickListener(this);
        }
        
        

        
        
    }
    
    
    public void SendMydata()
    {
    	final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				// ID와 패스워드를 가져온다 
				

				
				
				EditText oldpass = (EditText)findViewById(R.id.mypage_modi_name);
				EditText pass2 = (EditText)findViewById(R.id.mypage_modi_age);
				EditText pass = (EditText)findViewById(R.id.mypage_modi_loca);
				
				
				
				
				if ( oldpass.getText().toString().length() != 0 )
				{
					
				}
				else
				{
					handler.sendEmptyMessage(4);
					return;
				}
				
				if ( pass2.getText().toString().length() != 0 )
				{
					
					
				}
				else
				{
					handler.sendEmptyMessage(5);
					return;
				}
				
				if ( pass.getText().toString().length() != 0 )
				{
					
					
				}
				else
				{
					handler.sendEmptyMessage(6);
					return;
				}
					
					
				MultipartEntity entity = new MultipartEntity();
				try {
					entity.addPart("name", new StringBody(_AppManager.GetHttpManager().EncodeString(oldpass.getText().toString())));
					entity.addPart("age", new StringBody(_AppManager.GetHttpManager().EncodeString(pass2.getText().toString())));
					entity.addPart("location", new StringBody(_AppManager.GetHttpManager().EncodeString(pass.getText().toString())));
					entity.addPart("id", new StringBody(_AppManager.m_LoginID));
					entity.addPart("skey", new StringBody(_AppManager.m_MappToken));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				ContentBody brFile1 = null;
				if ( mImageCaptureUri != null )
				{
					if ( filepath == null )
					{
						File file1 = new File(getRealImagePath(mImageCaptureUri));
						brFile1 = new FileBody(file1 , "image/jpeg" );
						
						//entity.addPart("picture" , brFile1);
					}
					else
					{
						File file1 = new File(filepath);
						brFile1 = new FileBody(file1 , "image/jpeg" );
						
						//entity.addPart("picture" , brFile1);
					}
				}
				
				String strJSON = _AppManager.GetHttpManager().PostHTTPFileData(_AppManager.DEF_URL +  "/mapp/Mypage/Modify", entity);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.optString("errcode").equals("0"))
					{

						

						handler.sendEmptyMessage(7);
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
    
    
    public String getRealImagePath (Uri uriPath)
	{
		String []proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = self.managedQuery (uriPath, proj, null, null, null);
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		String path = cursor.getString(index);
		path = path.substring(5);

		return path;
	}
    
	public void GetMyData()
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
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Mypage", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{

							
							nick =  (json.optString("nick"));
							name =  (json.optString("name"));
							commment =  (json.optString("comment"));
							location =  (json.optString("location"));
							age=  (json.optString("age"));
							
							
							
							
							URL	imgUrl = new URL(_AppManager.DEF_URL + "ImageLoad?page=Profile&id=" +_AppManager.m_LoginID );
							
							URLConnection conn;
							
							conn = imgUrl.openConnection();
							
							conn.connect();
							BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
							
							
							Rect rect = new Rect();
							rect.left = 0;
							rect.top = 0;
							rect.bottom = m_ImageHeight;
							rect.right = m_ImageWidth;
							
							
							
							BitmapFactory.Options bo = new BitmapFactory.Options();
							bo.inSampleSize = 4;
							
							bitmap = BitmapFactory.decodeStream(bis, rect,bo);
							
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
					catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						handler.sendMessage(handler.obtainMessage(2,"프로필에 사진 없음" ));
						e.printStackTrace();
					}
					 catch (IOException e) {
							// TODO Auto-generated catch block
						 handler.sendMessage(handler.obtainMessage(2,"프로필에 사진 없음" ));
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
				Log.d(null,  (String) msg.obj);
				break;
				
			case 3:
	
				break;
				
			case 4:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , "이름을 입력해주세요." );
				
				break;
			case 5:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , "나이를 입력해주세요." );
				
				break;
			case 6:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , "지역을 입력해주세요." );
				
				break;
			case 7:
				onBackPressed();
				break;
			case 20:
				break;
			default:
				break;
			}

		}
    	
	};
	

    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
    
    
    @Override
    public void  onResume()
    {
    	super.onResume();
    	GetMyData();
    }
    
    



    /**
     * 카메라에서 이미지 가져오기
     */
    private void doTakePhotoAction()
    {
     /*
      * 참고 해볼곳
      * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
      * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
      * http://www.damonkohler.com/2009/02/android-recipes.html
      * http://www.firstclown.us/tag/android/
      */
	     File file = new File(Environment.getExternalStorageDirectory()+"/DCIM/Camera");
	     file.mkdir();
	     
	     Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	     
	     // 임시로 사용할 파일의 경로를 생성
	     String url = "Mapp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
	     mImageCaptureUri = Uri.fromFile(new File(file.getPath(), url));
	     
	     filepath = file.getPath()+"/"+url;
	     filepath = filepath.substring(5);
	     intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
	     intent.putExtra("return-data", true);
	     startActivityForResult(intent, PICK_FROM_CAMERA);
    
    }
    
    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumAction()
    {
    	filepath = null;
     // 앨범 호출
	     Intent intent = new Intent(Intent.ACTION_PICK);
	     intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
	     startActivityForResult(intent, PICK_FROM_ALBUM);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	     if(resultCode != RESULT_OK)
	     {
	    	 return;
	     }
	    
	     switch(requestCode)
	     {
		      case CROP_FROM_CAMERA:
		      {
		       // 크롭이 된 이후의 이미지를 넘겨 받습니다. 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
		       // 임시 파일을 삭제합니다.
		       final Bundle extras = data.getExtras();
		    
		       if(extras != null)
		       {
		    	   Bitmap photo = extras.getParcelable("data");
		    	   mPhotoImageView.setImageBitmap(photo);
		       }
		    
		       // 임시 파일 삭제
		       /*File f = new File(mImageCaptureUri.getPath());
		       if(f.exists())
		       {
		    	   f.delete();
		       }*/
		    
		       	break;
		      }
	    
	      case PICK_FROM_ALBUM:
	      {
	       // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
	       // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
	       
		       mImageCaptureUri = data.getData();
		       setImage();
	      }
	      
	      case PICK_FROM_CAMERA:
	      {
	    	  setImage();
		       // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
		       // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
		       /*Intent intent = new Intent("com.android.camera.action.CROP");
		       intent.setDataAndType(mImageCaptureUri, "image/*");
		 
		       intent.putExtra("outputX", m_ImageWidth);
		       intent.putExtra("outputY", m_ImageHeight);
		       intent.putExtra("aspectX", 1);
		       intent.putExtra("aspectY", 1);
		       intent.putExtra("scale", true);
		       intent.putExtra("return-data", true);
		       startActivityForResult(intent, CROP_FROM_CAMERA);*/
	    
	       break;
	      }
	     }
	     




    }







    
    public void setImage()
    {
    	

    	if ( getBitmapOfWidth(mImageCaptureUri.getPath() ) > 2047 || getBitmapOfHeight(mImageCaptureUri.getPath()  ) > 2047)
    	{
    		BitmapFactory.Options options = new BitmapFactory.Options();
    		options.inSampleSize = 8;        // 줄이고자 하는 사이즈 배수??  1/4 만큼 줄인다.
    		Bitmap src = BitmapFactory.decodeFile(mImageCaptureUri.getPath(), options);
    		Bitmap resized = Bitmap.createScaledBitmap(src, m_ImageWidth, m_ImageHeight, true);
    		mPhotoImageView.setImageBitmap(resized);
    		
    	}
    	else
    	{
    		mPhotoImageView.setImageURI(mImageCaptureUri);
    	}

		
		/*BitmapFactory.Options resizeOpts = new Options();
		 resizeOpts.inSampleSize = 2;
		 Bitmap tempBitmap = null;
		try {
			tempBitmap = BitmapFactory.decodeStream(new FileInputStream(getRealImagePath(mImageCaptureUri)), null, resizeOpts);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Bitmap finalBitmap = Bitmap.createScaledBitmap(tempBitmap, m_ImageWidth, m_ImageHeight, false);*/
		 
		
    	 
    	//mPhotoImageView.setImageBitmap(finalBitmap);
    	//mPhotoImageView.setImageURI(mImageCaptureUri);



    	/*ImageView Image = (ImageView)findViewById(R.id.album_detail_gallery);
		
		Image.setTag(mImageCaptureUri.toString());
		BitmapManager.INSTANCE.loadBitmap(mImageCaptureUri.toString(), Image, m_ImageWidth, m_ImageHeight);*/
		

  	}
    


	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		
		switch(arg0.getId() )
		{
		case R.id.title_popup:
		{
			SendMydata();
		}
			break;
		case R.id.album_desc:
			doTakeAlbumAction();
			break;
		case R.id.album_desc_2:
			doTakePhotoAction();
			break;
			
		case R.id.title_icon:
		{
			onBackPressed();
		}
		break;
		
		}
		
	}

    
	public void RefreshUI()
	{
		
		
		
		AppManagement _AppManager = (AppManagement) getApplication();
		

	
		((TextView)findViewById(R.id.title_name)).setText("내 페이지");
		((TextView)findViewById(R.id.title_desc)).setText("");
		
		{
			/*ImageView Image = (ImageView)findViewById(R.id.album_detail_gallery);
			
			Image.setTag(_AppManager.DEF_URL + "ImageLoad?page=Profile&id=" +_AppManager.m_MyPageID);
			BitmapManager.INSTANCE.loadBitmap(_AppManager.DEF_URL +  "ImageLoad?page=Profile&id=" +_AppManager.m_MyPageID, Image, m_ImageWidth, m_ImageHeight);
			*/
			mPhotoImageView.setImageBitmap(bitmap);
		}
		
		((EditText)findViewById(R.id.mypage_modi_name)).setText(name);
		((EditText)findViewById(R.id.mypage_modi_age)).setText(age);
		((EditText)findViewById(R.id.mypage_modi_loca)).setText(location);
		
		
	}
	
	public void GetImageSize(  )
	{
		
		View imageview = (View)findViewById(R.id.album_detail_gallery);
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


package com.example.hoteljoin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.example.hoteljoin.HotelSearchActivity.Destination;
import com.example.hoteljoin.HotelSearchActivity.Search_Adapter;
import com.example.hoteljoin.TravelReWriteActivity.NationData;


import com.slidingmenu.lib.SlidingMenu;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class TravelAddWriteActivity extends HotelJoinBaseActivity implements OnClickListener{
	TravelAddWriteActivity  self;
	SlidingMenu menu ;
	int MenuSize;
	 
	private final int SELECT_IMAGE = 1;
	private final int SELECT_MOVIE = 2;
	private final int TAKE_GALLERY = 3;
	private final int TAKE_CAMERA = 4;
		
	String filePath= "";
	
	Integer flag = 0;

	String filename ="";
	String[] m_Video = {"지금 사진 찍기", "저장된 사진 불러오기"  };
	Uri fileUri;
	
	Integer m_Tab = -1;
	
	Boolean m_World = false;
	
	public class NationData
	{
		String nationName;
		String nationCode;
	}
	
	NationData[]	m_NationList;
	NationData[]	m_CityList;

	
	
	Integer			m_NationIndex = 0;
	Integer			m_CityIndex = 0;
	
	
	 Boolean UseNetwork = false;			// 현재 네트워크가 사용중인지 체크 ( 사용중이라면 사용이 끝난후 데이터 전송해야함 )
	 Boolean MoreGetData = false;			// 한번더 얻어와야할 정보가 있는지 확인한다. 
	
	

	
	EditText m_SearchText = null;
	
	boolean m_bPopup2 ;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.travel_rewrite);
		
		

		// 비하인드 뷰 설정
		setBehindContentView(R.layout.main_menu_1);
		
		self = this;
		

	    // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
		
		// 슬라이딩 뷰
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.RIGHT);
		
		{
			Display defaultDisplay = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    	
			int width;
			@SuppressWarnings("deprecation")
			int windowswidth = defaultDisplay.getWidth();
			@SuppressWarnings("deprecation")
			int windowsheight= defaultDisplay.getHeight();
			int originwidth = 480;
			

			width = (int) ( (float) 340 * ( (float)(windowswidth)/ (float)(originwidth) )  );

			
			menu.setBehindOffset(windowswidth - width );
		}
		
		menu.setFadeDegree(0.35f);
		
		AfterCreate(7);
		

		BottomMenuDown ( true);

		BtnEvent(R.id.write);

		

		BtnEvent(R.id.confirm);

	}
	
	

	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	public void BtnEvent( int id )
    {
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
    }

	
	public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	public void ImageBtnResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	public void OpenPopup2()
	{
		m_bPopup2 = true;
		 ((View)findViewById(R.id.main_layout_2)).setVisibility(View.VISIBLE);
	}
	public void ClosePopup2()
	{
		m_bPopup2 = false;
		 ((View)findViewById(R.id.main_layout_2)).setVisibility(View.GONE);
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
	   			filePath = path;
	   			filename = name;
	   			fileUri = uri;
	   			SetImage();
	   		}       
	   		else if (requestCode == SELECT_MOVIE)        
	   		{           
	   			Uri uri = intent.getData(); 
	   			
	   			/*String path = getPath(uri);          
	   			String name = getName(uri);           
	   			String uriId = getUriId(uri);     */       
	   			Log.e("###", "실제경로 : " + filePath);
	   			//filePath = path;
	   			//filename = name;
	   			fileUri = uri;
	   			Bitmap bm = (Bitmap) intent.getExtras().get( "data" );
	   			SetImage2(bm);
	   			
	   		}   
	   	}
	 }
	 
	// 동영상선택
	private void doSelectImage()
	{   
		
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        
        alt_bld.setTitle("사진 선택");
        alt_bld.setSingleChoiceItems(m_Video, -1, new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int item) 
            {
            	switch( item )
            	{
            	case 0:
            	{
            		{
            			File file1 = new File(Environment.getExternalStorageDirectory() +"/android/data/com.hoteljoin/"); 

            			if( !file1.exists() ) // 원하는 경로에 폴더가 있는지 확인
            			{
            				if(file1.mkdirs() )
            				{
            		
            				}
            				else
            				{
            			         Toast.makeText(getBaseContext(), 
            			  	           "폴더를 만들수 없습니다.", 
            			  	           Toast.LENGTH_LONG).show();
            				}
            			}
            			else
            			{

            			}
            		}

            		// 사진 저장 경로를 바꾸기
            		Intent it = new Intent( ) ;
            		File file = new File( Environment.getExternalStorageDirectory( ), "/android/data/com.hoteljoin"+ "/" + System.currentTimeMillis() + ".jpg"  ) ;
            		
 
            		
            		filePath = file.getAbsolutePath( ) ;
            		 
            		it.putExtra( MediaStore.EXTRA_OUTPUT, filePath ) ;
            		 
            		it.setAction( MediaStore.ACTION_IMAGE_CAPTURE ) ; // 모든 단말에서 안될 수 있기 때문에 수정해야 함
            		startActivityForResult( it, SELECT_MOVIE ) ;
            		
            	}
            		break;
            	case 1:
            	{

            	   	Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            	   	i.setType("image/*"); 
            	   	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    
            	   	try    
            	   	{        
            	   		startActivityForResult(i, SELECT_IMAGE);
            		} 
            	   	catch (android.content.ActivityNotFoundException e)   
            	   	{        
            	   		e.printStackTrace();   
            	   	}	
            		
            	}
            		break;

            	}

            	dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();

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
	    
	    


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.write:
			doSelectImage();
			break;



		case R.id.confirm:
		{
			GetWrite();
		}
			break;
			

		}
		
	}

	
	public void SetImage()
	{
		BitmapFactory.Options options = new BitmapFactory.Options( ) ; 	 // 비트맵 이미지의 옵션 받을 변수 생성
        options.inJustDecodeBounds = true;  							 // 비트맴을 바로 로드하지 말고 옵션만 받아오라고 설정
        BitmapFactory.decodeFile( filePath, options ) ; 				 // 매트맵을 읽는데 윗줄에 의해 옵션만 받아오고 비트맵 다 읽지는 않음
    	/// 이미지의 높이를 얻음
        
        int fscale = options.outHeight ;
        if( options.outWidth > options.outHeight )	 // 이미지의 높이보다 넓이가 클 경우
        {
        	 fscale = options.outWidth ;				 // 이미지의 넓이를 스케일에 저장
        }
        Bitmap bmp ;	 // 실제 이미지를 저장할 변수
        
        
        
       	/// 이미지의 넓이가 800보다 크면
        
        if( 800 < fscale )
        {
        
       	/// 이미지의 사이즈를 800으로 나눈 만큼 리사이징 할거다
        
          	 fscale = fscale / 800 ;
        
          	 /// 새 비트맵 옵션 생성
        
       	    BitmapFactory.Options options2 = new BitmapFactory.Options();
        
       	    options2.inSampleSize = fscale ;	 /// 리사이징할 비율 설정
        
       	    bmp = BitmapFactory.decodeFile( filePath, options2 ) ;	 /// 실제로 비트맵을 읽어온다.
        
            }
        
            else
        
            {	 /// 사이즈가 적당 하면 그냥 읽는다.
        
            	bmp = BitmapFactory.decodeFile( filePath ) ;
        
            }
        
        /// 읽은 배트맵을 형변환해서 새로 생성
        
        BitmapDrawable dbmp = new BitmapDrawable( bmp );
		Drawable dr = (Drawable)dbmp ;	 /// 그걸 다시 형변환
		ImageView imageview = (ImageView)findViewById(R.id.photo_thum);
		imageview.setBackgroundDrawable( dr ) ; /// 뷰 객체의 백그라운드로 설정
	}
	
	
	public void SetImage2(Bitmap bm)
	{
		BitmapDrawable bmd = new BitmapDrawable( bm ) ;
		// tempPictuePath는 사진 저장 경로를 바꾼 다음 카메라로 사진을 찍었을 때 파일의 경로이다.
		File copyFile = new File( filePath ) ;
		OutputStream out = null;
		 
		try {
			out = new FileOutputStream(copyFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bm.compress(CompressFormat.JPEG, 70, out) ;

		try {
			out.close( ) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( copyFile.exists( ) && copyFile.length( ) > 0 ) /// 성공적으로파일이 저장되어 존재함 
		{
			SetImage();
		}
		else
		{
		}


		


		
	}

	

	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			UseNetwork = false;
			switch(msg.what)
			{
			case 0:
			{
				m_CityIndex = 0;
				((TextView)findViewById(R.id.text_2)).setText(m_CityList[m_CityIndex].nationName);
				RefreshUI();
				
			}
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 21:
			{

				onBackPressed();
			}
				break;
			case 23:
			{

			}


			default:
				break;
			}

		}
    	
	};	
	

		
	
	
	public void GetWrite()
	{
		mProgress.show();

		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				
				/*data.put("diaryNum", "-1");	
				//data.put("memberId", _AppManager.m_LoginID );	
				//data.put("writerName", _AppManager.m_Name);
				data.put("memberId", "ds1983" );	
				data.put("writerName", "바나나");
				data.put("subject", "테스트");
				data.put("contents", "가나다라마바사");	
				data.put("cityCode", m_CityList[m_CityIndex].nationCode);	
				data.put("nationCode", m_CurrNation);	*/
				EditText id = (EditText)findViewById(R.id.write_title);
				EditText pass = (EditText)findViewById(R.id.write_text);
				
					
				MultipartEntity entity = new MultipartEntity();
				try {
					entity.addPart("diaryNum", new StringBody(_AppManager.m_diaryNum));
					entity.addPart("memberId", new StringBody( _AppManager.m_LoginID));
					
					{
						String value = "";
						//value = EncodeString(data.get(key));
						value = new String(_AppManager.m_Name.getBytes(), "utf-8");
						entity.addPart("writerName", new StringBody(value));
					}
					{
						String value = "";
						//value = EncodeString(data.get(key));
						value = new String(pass.getText().toString().getBytes(), "utf-8");
						entity.addPart("contents", new StringBody(value));
					}
					{
						String value = "";
						//value = EncodeString(data.get(key));
						value = new String(id.getText().toString().getBytes(), "utf-8");
						entity.addPart("subject", new StringBody(value));
					}
					
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				ContentBody brFile1 = null;
				
				if ( !filePath.equals(""))
				{
					File file1 = new File(filePath);
					brFile1 = new FileBody(file1 , "image/jpeg" );
					entity.addPart("image" , brFile1);
				}

				String strJSON = _AppManager.GetHttpManager().PostHTTPFileData(_AppManager.DEF_URL +  "/mweb/board/diaryAdd.gm", entity);


				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{



						handler.sendEmptyMessage(21);
					}
					else 
					{
						// 에러 메세지를 전송한다. 
						handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.getString("errorMsg")) ));
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler.sendMessage(handler.obtainMessage(1,"json Data Error \n" + e.getMessage() ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}

	

    

}

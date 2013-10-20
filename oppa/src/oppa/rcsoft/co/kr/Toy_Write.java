package oppa.rcsoft.co.kr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.Toast;


// 글쓰기 
public class Toy_Write extends BaseActivity implements OnClickListener{
	static public Toy_Write self;
	
	Uri	[] picture;
	
	public boolean m_bReply;
	
	String  shopID;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toy_detail_write);
        
        self = this;
    
        Button WriteBtn = (Button)findViewById(R.id.toy_detail_write_save_btn);
        WriteBtn.setOnClickListener(this);
        
        picture = new Uri[3];
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		m_bReply = false;
		
		RefreshUI();
    }
    

    
    public void RefreshUI()
    {

            setContentView(R.layout.toy_detail_write);
            
            Button WriteBtn = (Button)findViewById(R.id.toy_detail_write_save_btn);
            WriteBtn.setOnClickListener(this);
            
            ImageButton Pic1Btn = (ImageButton)findViewById(R.id.toy_detail_write_pic1_btn);
            Pic1Btn.setOnClickListener(this);
            ImageButton Pic2Btn = (ImageButton)findViewById(R.id.toy_detail_write_pic2_btn);
            Pic2Btn.setOnClickListener(this);
            ImageButton Pic3Btn = (ImageButton)findViewById(R.id.toy_detail_write_pic3_btn);
            Pic3Btn.setOnClickListener(this);
            
            
            
            
            mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주십시오.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);

    }
    
    
	public void doSelectImage( int index )
	{    
		Intent i = new Intent(Intent.ACTION_GET_CONTENT); 
	  	i.setType("image/*");
	  	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	  	startActivityForResult(i, index);
	} 
    

    public void onClick(View v )
    {
    	switch(v.getId())
    	{

	    	case R.id.toy_detail_write_save_btn:
	    	{
	    		// 데이터 전송후 뒤로 가기 누른다. 
	    		SendWrite();
	    	}
	    		break;
	    	case R.id.toy_detail_write_pic1_btn:
	    	{
	    		self.doSelectImage(1);
	    	}
	    		break;
	    		
	    	case R.id.toy_detail_write_pic2_btn:
	    	{
	    		self.doSelectImage(2);
	    	}
	    		break;
	    		
	    	case R.id.toy_detail_write_pic3_btn:
	    	{
	    		self.doSelectImage(3);
	    	}
	    		break;
	    	default:
	    		break;
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
			PicBtn = (ImageButton)findViewById(R.id.toy_detail_write_pic1_btn);
			picture[0] = uri;
		}
		else if ( index == 2)
		{
			PicBtn = (ImageButton)findViewById(R.id.toy_detail_write_pic2_btn);
			picture[1] = uri;
		}
		else if ( index == 3)
		{
			PicBtn = (ImageButton)findViewById(R.id.toy_detail_write_pic3_btn);
			picture[2] = uri;
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
  				// 정상적으로 메세지가 전송 되었을 경우 
  				onBackPressed();
  				m_bReply = false;
  				message = "글이 잘 써졌습니다.";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				
  			}
  				break;
  			case 1:
  				message = "No data";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				break;
  			case 2:
  				self.ShowAlertDialLog( self.getParent() ,"에러" , (String) msg.obj );
  				break;
  			case 3:
  				message = "데이터가 없습니다";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				break;
  			default:

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
					

					
					EditText content = (EditText)findViewById(R.id.toy_wr_content);
					String strcon = content.getText().toString();
					
					// 파일들 사용하는 파일들만 정렬하기 ..
					ArrayList < Uri >   UriSort = new ArrayList < Uri >() ;
					
					for ( int i = 0 ; i < 3 ; i++ )
					{
						if ( self.picture[i] != null )
						{
							UriSort.add(self.picture[i]);
						}
					}
					
					MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );
					

 
					
					Charset charset = Charset.forName("UTF-8"); 
					ContentBody brFile1 = null;
					ContentBody brFile2 = null;
					ContentBody brFile3 = null;
					switch ( UriSort.size())
					{
					case 3:
					{
						File file3 = new File(getPath(UriSort.get(2)));
						brFile3 = new FileBody(file3 , "image/jpeg" );
						
						entity.addPart("bf_file3" , brFile3);
					}
					case 2:
					{
						File file2 = new File(getPath(UriSort.get(1)));
						brFile2 = new FileBody(file2 , "image/jpeg" );
						
						entity.addPart("bf_file2" , brFile2);
					}
					case 1:
					{
						File file1 = new File(getPath(UriSort.get(0)));
						brFile1 = new FileBody(file1 , "image/jpeg" );
						
						entity.addPart("bf_file1" , brFile1);
					}
						break;
						
					default:
						break;
							
					}
					/////////////////////////////////////////////////////////////////////
					{
						try {
							entity.addPart("bo_table" , new StringBody("play", charset));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						
						try {
							entity.addPart("wr_content" , new StringBody(strcon, charset));
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

					
					String strJSON = myApp.PostHTTPFileData("http://oppa.rcsoft.co.kr/api/gnu_saveBoardArticle.php", entity);	
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

							handler.sendEmptyMessage(0);
							
						}
						else if(json.getString("result").equals("error"))
						{
							handler.sendMessage(handler.obtainMessage(2,myApp.DecodeString(json.getString("result_text")) ));
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
    
    
}

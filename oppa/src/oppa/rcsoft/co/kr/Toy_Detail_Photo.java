package oppa.rcsoft.co.kr;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import oppa.rcsoft.co.kr.MyInfo.MyAccountData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

// 언니 사진 
public class Toy_Detail_Photo extends BaseActivity implements OnClickListener {
	public static Toy_Detail_Photo self; 
	Bitmap [] girl_Image;
	Uri [] girl_Image_uri;
	
	String gr_id = "";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toy_detail_photo);
    
        self = this;
        
		
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("잠시만 기다려 주십시오.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
	
		
		ImageView Photo1 = (ImageView)findViewById(R.id.toy_detail_photo1);
		Photo1.setOnClickListener(this);
		ImageView Photo2 = (ImageView)findViewById(R.id.toy_detail_photo2);
		Photo2.setOnClickListener(this);
		ImageView Photo3 = (ImageView)findViewById(R.id.toy_detail_photo3);
		Photo3.setOnClickListener(this);
		ImageView Photo4 = (ImageView)findViewById(R.id.toy_detail_photo4);
		Photo4.setOnClickListener(this);
		ImageView Photo5 = (ImageView)findViewById(R.id.toy_detail_photo5);
		Photo5.setOnClickListener(this);
		ImageView Photo6 = (ImageView)findViewById(R.id.toy_detail_photo6);
		Photo6.setOnClickListener(this);
		
		
		{
			Button TabBtn = (Button)findViewById(R.id.toy_detail_tab_btn_1);
			TabBtn.setOnClickListener(this);
			
	        Button CallBtn = (Button)findViewById(R.id.toy_bbs_call_btn);
	        CallBtn.setOnClickListener(this);
		}
		

        
        {
            MyInfo myApp = (MyInfo) getApplication();
            MyAccountData Mydata = myApp.GetAccountData();
            
            if ( Mydata.Level == 2 )
            {
                Button ZZimBtn = (Button)findViewById(R.id.toy_detail_title_bar_zzim);
                ZZimBtn.setOnClickListener(this);
            }
            else
            {
            	Button ZZimBtn = (Button)findViewById(R.id.toy_detail_title_bar_zzim);
            	ZZimBtn.setVisibility(View.GONE);
            }
	
        }
        
        {
			Button TabBTN2 = (Button)findViewById(R.id.toy_main_tab_btn_1);
			TabBTN2.setOnClickListener(this);
			Button TabBTN3 = (Button)findViewById(R.id.toy_main_tab_btn_2);
			TabBTN3.setOnClickListener(this);
			Button TabBTN4 = (Button)findViewById(R.id.toy_main_tab_btn_4);
			TabBTN4.setOnClickListener(this);
			Button TabBTN5 = (Button)findViewById(R.id.toy_main_tab_btn_5);
			TabBTN5.setOnClickListener(this);
		}

		
		
		{
			final MyInfo myApp = (MyInfo) getApplication();
			TextView titleText = (TextView)findViewById(R.id.toy_bbs_name);
			TextView addrText = (TextView)findViewById(R.id.toy_bbs_addr);
			TextView scoreText = (TextView)findViewById(R.id.toy_bbs_score);
			//TextView charText = (TextView)findViewById(R.id.toy_detail_main_char);
			ImageView mainImage = (ImageView)findViewById(R.id.toy_bbs_image_main);
			TextView itemTel = (TextView)findViewById(R.id.toy_bbs_tel);
			TextView itemShopName = (TextView)findViewById(R.id.toy_bbs_shop_name);
			mainImage.setImageBitmap(myApp.m_SisterInfo.m_Bitmap);
			titleText.setText("이름 : "+ myApp.m_SisterInfo.m_Title + "( " + myApp.m_SisterInfo.m_Ki + "cm / "+ myApp.m_SisterInfo.m_Weight + "kg )\n"
					 + "성격 : " + myApp.m_SisterInfo.m_hams );
			addrText.setText("주소 : " + myApp.m_SisterInfo.m_Addr);
			itemTel.setText("전화 : " + myApp.m_SisterInfo.m_TelNumber );
			
			

			if (myApp.mLat == -100000.0 )
			{
				scoreText.setText("찜횟수 : " + myApp.m_SisterInfo.m_Score + "회"   );
			}
			else
			{
				float dis = ((float)(myApp.m_SisterInfo.m_Distance )) /1000.0f;
				scoreText.setText("찜횟수 : " + myApp.m_SisterInfo.m_Score + "회" ) ;
			}
			
			
			
			
			itemShopName.setText( "소속 : " + myApp.m_SisterInfo.m_ShopName);
		}
		
		RefreshUI();

    }
	private void ZzimOneButton()
	{
		mProgress.show();
		final MyInfo myApp = (MyInfo) getApplication();
		Thread thread = new Thread(new Runnable()
		{
			public void run()
			{
				Map<String, String> data = new HashMap  <String, String>();
				data.put("gr_id",myApp.m_CurrGirlInfo.GirlID );
				data.put("return_type", "json");
				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_favoriteGirl.php", data);

				JSONObject json = null;
				try {
					json = new JSONObject(strJSON);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try 
				{
					if(json.getString("result").equals("ok"))
					{
						handler.sendEmptyMessage(8);
					}
					else if(json.getString("result").equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(10,myApp.DecodeString(json.getString("result_text")) ));
					}
				}  catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

			}
		});
		thread.start();
	}
	
    
    @Override
    public void onBackPressed() 
    {
        Intent intent;
        intent = new Intent().setClass(self, ToyMainList.class);
        startActivity( intent ); 
    }

    public void onClick(View v )
    {
    	switch(v.getId())
    	{
    	
    	case R.id.toy_main_tab_btn_1:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Home.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_2:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Shop_MainList.class);
	        startActivity( intent );   
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_4:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Community_Main.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_5:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, MyPage_Main.class);
	        startActivity( intent );  
    	}
    	
    	case R.id.toy_detail_title_bar_zzim:
    	{
    		ZzimOneButton();
    	}
    		break;
    	case R.id.toy_bbs_call_btn:
    	{
    		MyInfo myApp = (MyInfo) getApplication();
    		
    		Intent intent = new Intent(Intent.ACTION_DIAL);
    		intent.setData(Uri.parse("tel:" + myApp.m_SisterInfo.m_TelNumber));
    		startActivity(intent);
    		break;
    	}
    	
    	case R.id.toy_detail_tab_btn_1:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Toy_Detail_List.class);
	        startActivity( intent );  
    	}
    	case R.id.toy_detail_photo1:
    	{
    		ImageViewer(0);
    	}
    		break;
    	case R.id.toy_detail_photo2:
    	{
    		ImageViewer(1);
    	}
    		break;
    	case R.id.toy_detail_photo3:
    	{
    		ImageViewer(2);
    	}
    		break;
    	case R.id.toy_detail_photo4:
    	{
    		ImageViewer(3);
    	}
    		break;
    	case R.id.toy_detail_photo5:
    	{
    		ImageViewer(4);
    	}
    		break;
    	case R.id.toy_detail_photo6:
    	{
    		ImageViewer(5);
    	}
    		break;
	    default:
	    	break;
    	}
    }

    private void ImageViewer( Integer  index )
    {
		if ( girl_Image[index] !=  null )
		{
			
			
			Uri uri = Uri.parse( "file://" + Environment.getExternalStorageDirectory() +"/android/data/kr.co.rcoft.runoppa"+ "/" + gr_id  + "/"+ "_38219032810"+index.toString() + ".jpg" );
			Intent intent = new Intent(Intent.ACTION_VIEW); 
			
			intent.setDataAndType(uri, "image/*");
			
			startActivity(intent);
		}
    }
    
    private void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath)
    {
    	
    	{
    		File file;

    		String path = Environment.getExternalStorageDirectory()+"/android/data/kr.co.rcoft.runoppa"+ "/" + gr_id;

    		file = new File(path); 

    		if( !file.exists() ) // 원하는 경로에 폴더가 있는지 확인
    			file.mkdirs();
    		
    		strFilePath =  path +  "/" + strFilePath;
    	}
        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;
        	


        try
        {
        	fileCacheItem.createNewFile();
        	out = new FileOutputStream(strFilePath);
        	            
        	bitmap.compress(CompressFormat.JPEG, 100, out);
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
        finally
        {
        	try
        	{
        		out.close();
        	}
        	catch (IOException e)
        	{
        		e.printStackTrace();
        	}
        }
    }

    @Override
    public void RefreshUI()
    {
    	MyInfo myApp = (MyInfo) getApplication();
    	gr_id = myApp.m_CurrGirlInfo.GirlID;
    	getImage();
    }
    
    
    
    void getImage()
   	{
   		mProgress.show();
   		
   		if (girl_Image != null )
   		{
   	   		for ( int i = 0 ; i < girl_Image.length; i++)
   			{
   				
   				{
   					girl_Image_uri[i] = null;
   					girl_Image[i] = null;
   					ImageView image = (ImageView)findViewById(R.id.toy_detail_photo1 + i);					
   					image.setImageResource(R.drawable.toy_detail_sample_photo);			
   				}
   			}	
   		}

   		
   		final MyInfo myApp = (MyInfo) getApplication();
   		Thread thread = new Thread(new Runnable()
   		{

   			
   			public void run() {


   				Map<String, String> data = new HashMap  <String, String>();
   				data.put("gr_id",gr_id );
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
   							girl_Image_uri = new Uri[usageList.length()];
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
   								girl_Image_uri[i] = Uri.parse(myApp.DecodeString(list.getString("img")));
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
 	 					ImageView image = (ImageView)findViewById(R.id.toy_detail_photo1 + i);					
 	 					image.setImageBitmap( girl_Image[i]);
 	 					SaveBitmapToFileCache(girl_Image[i], "_38219032810"+i + ".jpg" );
 					}
 				}
 				
  			}
  				break;
  			case 1:
  				break;
  			case 2:
  				break;
  				
			case 8 :
				mProgress.dismiss();
				self.ShowAlertDialLog( self , "찜 완료", "찜이 완료 되었습니다." );
				break;	
  			default:
  				//	message = "실패하였습니다."
  				mProgress.dismiss();
  				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
  				break;
  			}
  		}
  	};
}

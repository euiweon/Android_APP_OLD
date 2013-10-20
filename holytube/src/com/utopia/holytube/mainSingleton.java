package com.utopia.holytube;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import com.euiweonjeong.base.CookieHTTP;
import com.euiweonjeong.base.UISizeConverter;

import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


public class mainSingleton  extends Application  {
	
	
	public class TodayData implements Cloneable 
	{
		public int mid;
		public String preacher;
		public String playtime;
		public String title;
		public String img;
		public String movie;
		public String datalength;
		public String youtube;
		
		public Object clone() throws CloneNotSupportedException
		{
			TodayData a = (TodayData)super.clone();
			a.mid = mid;
			return a;
		 }
	}
	
	public int StatusBarSize = 0 ;
	public int 	version;
	public String mainImage;
	
	public UISizeConverter 	m_UISizeConverter;
	
	//static public String DEF_HOME_URL=  "http://www.holy-tube.com:8080"
	static public String DEF_HOME_URL = "http://www.g-wealth.com:8080";
	
	public String MovieXMLString="";
	private int		m_CurrShopKind;
	public CookieHTTP		m_Cookie = new CookieHTTP();
	public String 			ResultString = "";
	public int				mgid = 0;
	public int				lgid = 0;

	public String 	DeviceID;

	public long id = 0;
	
	private SharedPreferences m_Prefs;
	
	public String TodayXMLString;
	public  ArrayList<TodayData> todayData;;
	
	public  ArrayList<TodayData> movieCollectData;;
	
	public TodayData		tempTodayData;
	public String 			VideoPath = "";

	public ArrayList<Activity> 	activityList1 = new ArrayList<Activity>();
	
	@Override
	public void onCreate()
	{
		super.onCreate();

		// 상태들을 초기화 한다. 
		tempTodayData = new TodayData();
		todayData = new ArrayList<TodayData>();
		todayData.clear();
		
		movieCollectData = new ArrayList<TodayData>();
		movieCollectData.clear();
		
		DeviceID = getMD5Hash(android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
		
		Log.e("Device ID",DeviceID);
		
		
		{
	        /////
	        {
	        	FileReader reader = null;
	        	
	        	try {
					reader = new FileReader( Environment.getExternalStorageDirectory() +"/android/data/com.utopia.holytube/" + "temp.xml" );
					StringBuffer buffer = new StringBuffer();
					
					int data = 1;
					while ( data > 0 )
					{
						data = reader.read();
						buffer.append((char)data );
					}
					MovieXMLString = buffer.toString();
					reader.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		}
		
		// 저장되어 있던 데이터들을 가져 온다
		LoadInfo();

	}
	
	
	public String getMD5Hash(String s) {
        MessageDigest m = null;
        String hash = null;
   	
        try {
       	 m = MessageDigest.getInstance("MD5");
       	 m.update(s.getBytes(),0,s.length());
   	     hash = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
       	 e.printStackTrace();
        }
   	
        return hash;
   }
	
	
	public void DataLoad()
	{
		
	}
	
	public UISizeConverter GetUISizeConverter()
	{
		return m_UISizeConverter;
	}
	public void CreateUISizeConverter( int size )
	{
		m_UISizeConverter = new UISizeConverter(getBaseContext(), size);
	}

	public void pushTodayData( TodayData data)
	{
		try {
			todayData.add((TodayData) data.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clearTodayData()
	{
		todayData.clear();
	}
	
	public void pushMovieData( TodayData data)
	{
		try {
			movieCollectData.add((TodayData) data.clone());
			MovieXMLSave();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clearMovieData()
	{
		movieCollectData.clear();
	}
	
	public void removeMoviedata(TodayData data)
	{
		try {
			movieCollectData.remove((TodayData) data.clone());
			MovieXMLSave();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void MovieXMLSave()
	{
		
		// 중복 데이터 삭제 
		{
			
		}
		Element listdata=new Element("listData");
		Document doc=new Document(listdata);
		
		// XML 파일 저장 시작... 
		
		for ( int i = 0 ; i < movieCollectData.size() ; i++  )
		{
			
			
			{
				Integer temp = movieCollectData.get(i).mid;
				Element mid=new Element("mid"); 
				mid.addContent( temp.toString() );
				
				listdata.addContent(mid);
			}
			{
				Element title=new Element("title"); 
				title.addContent( movieCollectData.get(i).title );
				
				listdata.addContent(title);
			}
			{
				Element preacher=new Element("preacher"); 
				preacher.addContent( movieCollectData.get(i).preacher );
				listdata.addContent(preacher);
			}
			{
				Element playtime=new Element("playtime"); 
				playtime.addContent( movieCollectData.get(i).playtime );
				listdata.addContent(playtime);				
			}
			{
				Element img=new Element("img"); 
				img.addContent( movieCollectData.get(i).img );
				listdata.addContent(img);	
			}
			{
				Element movie=new Element("dataname"); 
				movie.addContent( movieCollectData.get(i).movie );
				listdata.addContent(movie);	
			}
			
			{
				/*Element datalength=new Element("datalength"); 
				datalength.addContent( movieCollectData.get(i).datalength );
				listdata.addContent(datalength);	*/
			}
			

		}
		//파일로 저장하기 위해서 XMLOutputter 객체가 필요하다
        XMLOutputter xout = new XMLOutputter();
		{
			File file1 = new File(Environment.getExternalStorageDirectory() +"/android/data/com.utopia.holytube/"); 

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
        try {
			xout.output(doc, new FileWriter(Environment.getExternalStorageDirectory() +"/android/data/com.utopia.holytube/" + "temp.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
		
		{
	        /////
	        {
	        	FileReader reader = null;
	        	
	        	try {
					reader = new FileReader( Environment.getExternalStorageDirectory() +"/android/data/com.utopia.holytube/" + "temp.xml" );
					StringBuffer buffer = new StringBuffer();
					
					int data = 1;
					while ( data > 0 )
					{
						data = reader.read();
						buffer.append((char)data );
					}
					MovieXMLString = buffer.toString();
					reader.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		}
	}
	//------------------------------------------------------------------------------------------------------
	// 다운로드 매니저로 다운로드 시작 
	public long startDownload(Uri downloadUri,String ContentName, String Folder, String file,  boolean onlyWifi)
	{
		
		// 다운로드 매니저는 SD카드 경로에 바로 저장하기 때문에 SD카드 경로가 생략 되지만
		// 폴더를 생성해줄려면 전체 경로가 필요함.
		{
			File file1 = new File(Environment.getExternalStorageDirectory() +Folder); 

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
	    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
	    request.setTitle(ContentName); // Notification에서 보여질 Main String 입니다.
	    request.setDescription("Holytube");
	    request.setDestinationInExternalPublicDir(Folder, file);

	    if (onlyWifi)
	        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
	    else
	        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

	    final DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
	    
        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(completeReceiver, completeFilter); 
        
        id = dManager.enqueue(request);
	    return id;

	}
	
	
    private BroadcastReceiver completeReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			
			if ( checkDownloadComplete(id) == true)
				Toast.makeText(context, "다운로드가 완료되었습니다.",Toast.LENGTH_SHORT).show();
		}
    	
    };
	
    
	// 다운로드 매니저에서 다운로드가 제대로 되었는지 체크 
	private boolean checkDownloadComplete(long downloadID)
	{
	    final DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
	    Query query = new Query();
	    query.setFilterById(downloadID);

	    Cursor downloadCursor = dManager.query(query);
	    if (downloadCursor != null)
	    {
	        downloadCursor.moveToFirst();
	        int statusKey = downloadCursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
	        int reasonKey = downloadCursor.getColumnIndex(DownloadManager.COLUMN_REASON);

	        
	        int status = downloadCursor.getInt(statusKey);
	        int reason = downloadCursor.getInt(reasonKey);
	        
	        switch(status)
	        {
	        case DownloadManager.STATUS_FAILED:
		         String failedReason = "";
		         switch(reason)
		         {
		          
			         case DownloadManager.ERROR_CANNOT_RESUME:
			          failedReason = "일시적인 이유로 다운로드를 재개할수 없습니다.";
			          break;
			         case DownloadManager.ERROR_DEVICE_NOT_FOUND:
			          failedReason = "외부저장소를 찾을수 없습니다.";
			          break;
			         case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
			          failedReason = "파일이 이미 존재합니다.";
			          break;
			         case DownloadManager.ERROR_FILE_ERROR:
			          failedReason = "ERROR_FILE_ERROR";
			          break;
			         case DownloadManager.ERROR_HTTP_DATA_ERROR:
			          failedReason = "HTTP 데이터의 문제점이 발생했습니다.";
			          break;
			         case DownloadManager.ERROR_INSUFFICIENT_SPACE:
			          failedReason = "저장공간이 부족합니다. 확인해주세요.";
			          break;
			         case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
			          failedReason = "ERROR_TOO_MANY_REDIRECTS";
			          break;
			         case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
			          failedReason = "부정확한 HTTP 코드를 받았습니다";
			          break;
			         case DownloadManager.ERROR_UNKNOWN:
			          failedReason = "알수 없는 오류가 발생했습니다.";
			          break;
	         }

	         Toast.makeText(getBaseContext(), 
	           "FAILED: " + failedReason, 
	           Toast.LENGTH_LONG).show();

	        }

	        if (status == DownloadManager.STATUS_SUCCESSFUL || reason == DownloadManager.ERROR_FILE_ALREADY_EXISTS)
	            return true;
	    }
	    return false;
	}
	
	public void FileDownload( String path, String filename,  String Urlpath )
	{
        final int DOWNLOAD_DONE = 0;
        final int DEFAULT_TIMEOUT = 30000;
        long fileSize = 0, remains, lenghtOfFile = 0;


        File filepath = new File(path);
        File file = new File(path+filename);	
		
        if (filepath.exists() == false) {
            try {
            	filepath.mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        RandomAccessFile output = null ;
		try {
			output = new RandomAccessFile(file.getAbsolutePath(), "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        try {
			fileSize = output.length();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        try {
			output.seek(fileSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
         
        URL url = null;
		try {
			url = new URL(Urlpath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        URLConnection conn = null;
		try {
			conn = url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        conn.setRequestProperty("Range", "bytes=" + String.valueOf(fileSize) + '-'); 
        try {
			conn.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        conn.setConnectTimeout(DEFAULT_TIMEOUT); 
        conn.setReadTimeout(DEFAULT_TIMEOUT); 
        remains = conn.getContentLength(); 
        lenghtOfFile = remains + fileSize; 
         
        if ((remains <= DOWNLOAD_DONE) || (remains == fileSize))
        { 
            return ; 
        } 
        InputStream input = null;
		try 
		{
			input = conn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        	
        byte data[] = new byte[1024]; 
        int count = 0; 
         
        if (fileSize < lenghtOfFile) { 
            try {
				while((count = input.read(data)) != -1) { 
				    output.write(data, 0, count); 
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        } 
        try {
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	


	//-----------------------------------------------------------------------------------
	// 업소 종류
	public int GetShopKind()
	{
		return m_CurrShopKind;
	}
	public void 	SetShopKind( int data )
	{
		m_CurrShopKind = data;
	}
	
	//-----------------------------------------------------------------------------------
	// 개인정보 로드 저장 관련 

	public void LoadInfo()
	{

	}
	public void SaveInfo()
	{
		if ( m_Prefs != null)
		{
			SharedPreferences.Editor ed = m_Prefs.edit();

			ed.commit();
		}
	}
	
	//-----------------------------------------------------------------------------------
    // 문자열 인코딩 관련 
	public String DecodeString( String Original )
	{
		String returnstring = "";
		try {
			returnstring = URLDecoder.decode(Original, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnstring;
	}
	
	public String EncodeString( String Original )
	{
		String returnstring = "";
		try {
			returnstring = URLEncoder.encode(Original, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnstring;
	}
	//-----------------------------------------------------------------------------------
	// HTTP 데이터 전송 관련...
	public String GetHTTPGetData( String address , Map<String, String> data)
	{
		return m_Cookie.GetHTTPGetData(address, data);
	}

	public String PostHTTPData( String address , Map<String, String> data)
	{
		return m_Cookie.PostHTTPData(address, data);
	}
	
	
	public String PostCookieHTTPData( String address , Map<String, String> data)
	{

		return m_Cookie.PostCookieHTTPData(address, data);
	}

	
	
	
	public String PostHTTPFileData( String address , MultipartEntity data)
	{

		return m_Cookie.PostHTTPFileData( address , data);
	}

	//-----------------------------------------------------------------------------------
	
	@Override
	public void onTerminate() 
	{
		// 각종 상태 저장한다.
		
		SaveInfo();
		super.onTerminate();
	}
	
	public class CookieHTTP
	{
		public HttpClient client = new DefaultHttpClient();  
		public BasicCookieStore cookieStore = new BasicCookieStore();
		public BasicHttpContext localContext = new BasicHttpContext();

		// GetType의 URL을 만들어 준다
		private HttpGet makeHttpGet(String address  , Map<String, String> data) 
		{
			 // TODO Auto-generated method stub
			 Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;

			 Iterator<String> keys = data.keySet().iterator();   
			 while( keys.hasNext() )
			 {            
				 String key = keys.next();     
				 // value 값들을 전부 UTF-8 로 인코딩해준다.
				
				String value = "";
				try 
				{
					value = new String(data.get(key).getBytes(), "utf-8");
				} 
				catch (UnsupportedEncodingException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 nameValue.add( new BasicNameValuePair( key, value) );
			 }
			 
			 String url = address + "?" + URLEncodedUtils.format (nameValue, "UTF-8") ;
			 HttpGet request = new HttpGet( url ) ;
			 return request;
		}
		
		public String GetHTTPGetData( String address , Map<String, String> data)
		{
			String returnstring = "error";
			HttpGet httpget = makeHttpGet(address , data);


			try
			{          
					HttpParams params1 = client.getParams();
					params1.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		            HttpConnectionParams.setConnectionTimeout(params1, 10000);
		            HttpConnectionParams.setSoTimeout(params1, 10000);
					HttpResponse responseGet = client.execute(httpget , localContext );          
					HttpEntity resEntityGet = responseGet.getEntity();                
					if (resEntityGet != null)        
					{                  
						// 결과를 처리합니다.  
						returnstring = EntityUtils.toString(resEntityGet);
						
					}
					else
					{
						// 에러 처리 
						
					}
			}
			catch (Exception e)
			{        
				returnstring = " { \"result\" :\"error\",\"result_text\":\"에러\"} ";
				e.printStackTrace();
			}

				
			return returnstring;
		}
		
		public String PostCookieHTTPData( String address , Map<String, String> data)
		{
			String returnstring = "error";
			BufferedReader br;
			try
			{             
				HttpPost post = new HttpPost(address);         
				Vector<NameValuePair> params = new Vector<NameValuePair>();    
				
				 
				for( String key : data.keySet() )
				{            
					 // value 값들을 전부 UTF-8 로 인코딩해준다.
					
					String value = "";
					//value = EncodeString(data.get(key));
					try 
					{
						value = new String(data.get(key).getBytes(), "utf-8");
						
					} 
					catch (UnsupportedEncodingException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					params.add( new BasicNameValuePair( key, value ) );
				 }
				 
	     
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);       
				post.setEntity(ent);
				
				HttpParams params1 = client.getParams();
				params1.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	            HttpConnectionParams.setConnectionTimeout(params1, 10000);
	            HttpConnectionParams.setSoTimeout(params1, 10000);
	            
				
				localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

				HttpResponse responsePOST = null;
				try 
				{
					responsePOST = client.execute( post , localContext);
				} 
				catch (ConnectTimeoutException e)
				{
					returnstring = " { \"result\" :\"error\",\"result_text\":\"대기 시간 초과\"} ";
					return returnstring;
				}
				catch (UnknownHostException  e)
				{
					returnstring = " { \"result\" :\"error\",\"result_text\":\"응답없음\"} ";
					return returnstring;
				}
				catch (ClientProtocolException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				HttpEntity resEntity = responsePOST.getEntity();                
				if(resEntity != null)
				{
					br = new BufferedReader(new InputStreamReader(resEntity.getContent()));
					String str = null;
					StringBuffer sb = new StringBuffer();


					while((str = br.readLine()) != null)
					{
						sb.append(str);
					}
					br.close();

					returnstring = sb.toString();
				}
				
				List<Cookie> cookies = cookieStore.getCookies();
				for (int i = 0; i < cookies.size(); i++) 
				{
				    Log.e("HTTP","Local cookie: " + cookies.get(i));
				}
				
				returnstring = URLDecoder.decode(returnstring, "utf-8");
			}
			catch (Exception e)
			{        
				e.printStackTrace();
			}
			return returnstring;
		}
		
		public String PostHTTPData( String address , Map<String, String> data)
		{
			String returnstring = "error";
			BufferedReader br;
			try
			{            
				HttpPost post = new HttpPost(address);         
				Vector<NameValuePair> params = new Vector<NameValuePair>();    
				 
				for( String key : data.keySet() )
				{            
					 // value 값들을 전부 UTF-8 로 인코딩해준다.
					
					String value = "";
					//value = EncodeString(data.get(key));
					try 
					{
						value = new String(data.get(key).getBytes(), "utf-8");
						
					} 
					catch (UnsupportedEncodingException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					params.add( new BasicNameValuePair( key, value ) );
				 }
				
	     
				
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);       
				post.setEntity(ent);   
				
				
				HttpParams params1 = client.getParams();
				params1.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	            HttpConnectionParams.setConnectionTimeout(params1, 10000);
	            HttpConnectionParams.setSoTimeout(params1, 10000);
				
				
				localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);    
				HttpResponse responsePOST = null;
				try 
				{
					responsePOST = client.execute( post , localContext);
				} 
				catch (ConnectTimeoutException e)
				{
					returnstring = " { \"result\" :\"error\",\"result_text\":\"대기 시간 초과\"} ";
					return returnstring;
				}
				catch (UnknownHostException  e)
				{
					returnstring = " { \"result\" :\"error\",\"result_text\":\"응답없음\"} ";
					return returnstring;
				}
				catch (ClientProtocolException e) 
				{
					returnstring = " { \"result\" :\"error\",\"result_text\":\"알수 없는 에러 - 1\"} ";
					// TODO Auto-generated catch block
					e.printStackTrace();
					return returnstring;
				} catch (IOException e) 
				{
					// TODO Auto-generated catch block
					returnstring = " { \"result\" :\"error\",\"result_text\":\"알수 없는 에러 - 2\"} ";
					e.printStackTrace();
					return returnstring;
				} 
				
				   
				HttpEntity resEntity = responsePOST.getEntity();                
				if(resEntity != null)
				{
					br = new BufferedReader(new InputStreamReader(resEntity.getContent()));
					String str = null;
					StringBuffer sb = new StringBuffer();


					while((str = br.readLine()) != null)
					{
						sb.append(str);
					}
					br.close();

					returnstring = sb.toString();
				}
				
				returnstring = URLDecoder.decode(returnstring, "utf-8");
			}
			catch (Exception e)
			{        
				e.printStackTrace();
			}
			return returnstring;
		}
		
		public String PostHTTPFileData( String address , MultipartEntity data)
		{
			String returnstring = "error";
			BufferedReader br = null;
			
			HttpPost post = new HttpPost(address);   
			post.setHeader("Connection", "Keep-Alive"); 
			post.setHeader("Accept-Charset", "UTF-8");
			post.setHeader("ENCTYPE", "multipart/form-data");
			post.setEntity(data);  
			
			
			HttpParams params = client.getParams();
            params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);
			
			
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);    
			HttpResponse responsePOST = null;
			try 
			{
				responsePOST = client.execute( post , localContext);
			} 
			catch (ConnectTimeoutException e)
			{
				returnstring = " { \"result\" :\"error\",\"result_text\":\"대기 시간 초과\"} ";
				return returnstring;
			}
			catch (UnknownHostException  e)
			{
				returnstring = " { \"result\" :\"error\",\"result_text\":\"응답없음\"} ";
				return returnstring;
			}
			catch (ClientProtocolException e) 
			{
				returnstring = " { \"result\" :\"error\",\"result_text\":\"알수 없는 에러 - 1\"} ";
				// TODO Auto-generated catch block
				e.printStackTrace();
				return returnstring;
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				returnstring = " { \"result\" :\"error\",\"result_text\":\"알수 없는 에러 - 2\"} ";
				e.printStackTrace();
				return returnstring;
			} 
			
			HttpEntity resEntity = responsePOST.getEntity();                
			if(resEntity != null)
			{
				try {
					br = new BufferedReader(new InputStreamReader(resEntity.getContent()));
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String str = null;
				StringBuffer sb = new StringBuffer();


				try 
				{
					while((str = br.readLine()) != null)
					{
						sb.append(str);
					}
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try 
				{
					br.close();
				} catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				returnstring = sb.toString();
			}
			
			try 
			{
				returnstring = URLDecoder.decode(returnstring, "utf-8");
			} 
			catch (UnsupportedEncodingException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return returnstring;
		}
		
	}

}




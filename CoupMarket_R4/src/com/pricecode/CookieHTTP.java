package com.pricecode;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;



import android.accounts.Account;
import android.accounts.AccountManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class CookieHTTP 
{
	public HttpClient client = new DefaultHttpClient();  
	public BasicCookieStore cookieStore = new BasicCookieStore();
	public BasicHttpContext localContext = new BasicHttpContext();
	
	
	public HttpClient client2 = new DefaultHttpClient();  
	public BasicHttpContext localContext2 = new BasicHttpContext();

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

		/*try 
		{
			returnstring = new String(Original.getBytes(), "utf-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}*/
		
		return returnstring;
	}
	

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
		 if ( data.size() != 0)
		 {
			 String url = address + "?" + URLEncodedUtils.format (nameValue, "UTF-8") ;
			 HttpGet request = new HttpGet( url ) ;
			 return request;
		 }
		 String url = address ;
		 HttpGet request = new HttpGet( url ) ;
		 return request;
	}
	
	public String GetHTTPData( String address , Map<String, String> data)
	{
		String returnstring = "error";
		HttpGet httpget = makeHttpGet(address , data);


		try
		{          
				HttpParams params1 = client.getParams();
				params1.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	            HttpConnectionParams.setConnectionTimeout(params1, 20000);
	            HttpConnectionParams.setSoTimeout(params1, 20000);
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
	
	
	public String GetHTTPData2( String address , Map<String, String> data)
	{
		String returnstring = "error";
		HttpGet httpget = makeHttpGet(address , data);


		try
		{          
				HttpParams params1 = client2.getParams();
				params1.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	            HttpConnectionParams.setConnectionTimeout(params1, 20000);
	            HttpConnectionParams.setSoTimeout(params1, 20000);
				HttpResponse responseGet = client2.execute(httpget , localContext2 );          
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

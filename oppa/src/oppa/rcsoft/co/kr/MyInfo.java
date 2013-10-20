package oppa.rcsoft.co.kr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
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
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.location.LocationManager;
import android.util.Log;

enum Curr_LoginState
{
	NOT_LOGIN,
	LOGIN,	
}
// 전체 싱글턴 클래스
// 로그인 상태 체크 하는 부분 아직 미완성 
public class MyInfo extends Application 
{
	public class MyAccountData
	{
		public String  	ID;
		public String  	Password;
		public boolean 	AutoLogin;
		public boolean 	IDSave;
		public String 	Name;
		public String 	NickName;
		public int		Level;
		public int 		Point;
		public String 	Grade;
		public String 	Status;
	}
	public class CurrShopInfo
	{
		public String	ShopID;
		public String  	IntroMessage;
	}
	public class CurrGirlInfo
	{
		public String	GirlID;
		public boolean  forceIn;
		public String  	IntroMessage;
	}
	
	public class ShopFind
	{
		public String Name;
		public String Category;
		public String Location;
		public String Distance;
		
	}
	
	public class BBSContent
	{
		public String TableID ="";
		public String Write_ID ="";
	}
	
	public class BBSID
	{
		public String bo_table = "";
	}
	
	public class BBSViewerID
	{
		public String bo_table ="";
		public String wr_id = "";
	}
	
	public class BBSReplyContent
	{
		public boolean isWriteType = false; 
		public String bo_table ="";
		public String wr_id = "";
		public boolean isReply = false; 
		public String re_id = "";
	}
	
	public class BBSSingo
	{
		public String bo_table ="";
		public String wr_id = "";
		public boolean isReply = false; 
		public String re_id = "";
	}
	
	public class ZZim
	{
		public String Type;
	}
	
	public class SisterInfo
	{
		public String m_GirlID;
		public String m_TelNumber = "01012341234";
		public String m_Title;
		public String m_Addr;
		public String m_Score;
		public String  m_Ki;
		public String m_Weight;
		public String m_hams;
		public Bitmap m_Bitmap;
		public String m_ShopName;
		
		public int m_Distance;
	}
	
	private Curr_LoginState  LoginState;
	private int		m_CurrShopKind;
	public CookieHTTP		m_Cookie = new CookieHTTP();
	public String 			ResultString = "";
	MyAccountData m_AccountData = new MyAccountData();
	
	public CurrShopInfo m_CurrShopInfo = new CurrShopInfo(); 
	public CurrGirlInfo m_CurrGirlInfo = new CurrGirlInfo();
	
	public ShopFind		m_ShopFindInfo = new ShopFind();
	public BBSContent   m_BBSContent = new BBSContent(); 
	public BBSID		m_BBSID = new BBSID();
	public BBSViewerID  m_BBSViewerID = new BBSViewerID();
	public ZZim 		m_ZZime		= new ZZim();
	public BBSReplyContent	m_BBSReplyContent = new BBSReplyContent();
	public BBSSingo		m_BBSSingo = new BBSSingo();
	
	public SisterInfo  	m_SisterInfo = new SisterInfo();
	LocationManager 	mLocationManager;
	
	
	public Double mLat = -100000.0, mLon = -100000.0;
	
	public LocationHelper loca;
	
	
	private SharedPreferences m_Prefs;
	
	
	String[] grade = {"이병",
					"일병",
					"상병",
					"병장",
					"하사",
					"중사",
					"상사",
					"원사",
					"소위",
					"중위",
					"대위",
					"소령",
					"중령",
					"대령",
					"준장",
					"소장",
					"중장",
					"대장"};
			
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		m_AccountData.ID = " ";
		m_AccountData.Password = "";
		LoginState = Curr_LoginState.NOT_LOGIN;
		
		// 상태들을 초기화 한다. 
		
		// 저장되어 있던 데이터들을 가져 온다
		LoadInfo();
		
		loca = new LocationHelper(this.getApplicationContext() , this );
		
		loca.run();
		
		
		
		
	}
	
	public int GetGradeIcon( String name )
	{
		if ( grade[0].equals(name))
		{
			return R.drawable.t_1;
		}
		else if ( grade[0].equals(name))
		{
			return R.drawable.t_1;
		}
		else if ( grade[1].equals(name))
		{
			return R.drawable.t_2;
		}
		else if ( grade[2].equals(name))
		{
			return R.drawable.t_3;
		}
		else if ( grade[3].equals(name))
		{
			return R.drawable.t_4;
		}
		else if ( grade[4].equals(name))
		{
			return R.drawable.t_5;
		}
		else if ( grade[5].equals(name))
		{
			return R.drawable.t_6;
		}
		else if ( grade[6].equals(name))
		{
			return R.drawable.t_7;
		}
		else if ( grade[7].equals(name))
		{
			return R.drawable.t_8;
		}
		else if ( grade[8].equals(name))
		{
			return R.drawable.t_9;
		}
		else if ( grade[9].equals(name))
		{
			return R.drawable.t_10;
		}
		else if ( grade[10].equals(name))
		{
			return R.drawable.t_11;
		}
		else if ( grade[11].equals(name))
		{
			return R.drawable.t_12;
		}
		else if ( grade[12].equals(name))
		{
			return R.drawable.t_13;
		}
		else if ( grade[13].equals(name))
		{
			return R.drawable.t_14;
		}
		else if ( grade[14].equals(name))
		{
			return R.drawable.t_15;
		}
		else if ( grade[15].equals(name))
		{
			return R.drawable.t_16;
		}
		else if ( grade[16].equals(name))
		{
			return R.drawable.t_17;
		}
		else if ( grade[17].equals(name))
		{
			return R.drawable.t_18;
		}
		else if ( grade[18].equals(name))
		{
			return R.drawable.t_19;
		}
		else if ( grade[19].equals(name))
		{
			return R.drawable.t_20;
		}
		else if ( grade[20].equals(name))
		{
			return R.drawable.t_21;
		}
		else if ( grade[21].equals(name))
		{
			return R.drawable.t_22;
		}
	
		return R.drawable.t_1;
	}
	
	public Curr_LoginState GetLoginState()
	{
		return LoginState;
	}
	
	//-----------------------------------------------------------------------------------

	//-----------------------------------------------------------------------------------
	// 로그인 체크 
	// 항상 무슨 데이터를 보내기 전에 로그인 체크를 한다. ( 단 Home 탭에서는 예외 )
	public boolean GetLoginCheck() throws JSONException
	{
		Map<String, String> data = new HashMap  <String, String>();

		data.put("return_type", "json");
		String str = GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_loginCheck.php", data);
		JSONObject json = new JSONObject(str);
		
		
		{
			if(json.getString("result").equals("ok"))
			{
				LoginState = Curr_LoginState.LOGIN;
				return true;
			}
			else
			{
				LoginState = Curr_LoginState.NOT_LOGIN;
				return false;
			}
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
	
	public MyAccountData GetAccountData()
	{
		return m_AccountData;
	}
	public void 	SetAccountData( MyAccountData data )
	{
		m_AccountData = data;
	}
	public void LoadInfo()
	{
		m_Prefs = getSharedPreferences("RunnOppa", MODE_PRIVATE);
		
		if ( m_Prefs != null)
		{
			m_AccountData.ID = m_Prefs.getString("account", "");
			m_AccountData.Password = m_Prefs.getString("password", "");
			m_AccountData.AutoLogin = m_Prefs.getBoolean("autologin", false);
			m_AccountData.IDSave = m_Prefs.getBoolean("IDSave", false);
		}
	}
	public void SaveInfo()
	{
		if ( m_Prefs != null)
		{
			SharedPreferences.Editor ed = m_Prefs.edit();
			ed.putString("account", m_AccountData.ID);
			ed.putString("password", m_AccountData.Password);
			ed.putBoolean("autologin", m_AccountData.AutoLogin);
			ed.putBoolean("IDSave", m_AccountData.IDSave);
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

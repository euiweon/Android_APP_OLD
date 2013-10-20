package com.goodmate.tayotayo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.euiweonjeong.base.CookieHTTP;
import com.euiweonjeong.base.UISizeConverter;



import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


public class AppManagement extends Application  {
	

	//public String 			DEF_URL ="http://121.133.123.239:8080";
	public String 			DEF_URL ="http://tayotayo.offonkorea.com/passenger";
	public CookieHTTP		m_Cookie ;
	

	
	public UISizeConverter 	m_UISizeConverter;
	
	
	public ArrayList<Activity> 	activityList1 = new ArrayList<Activity>();

	public String 	DeviceID;
	
	public String 	UserIdx = "";
	public String 	CallIdx = "";
	public String 	DriverIdx = "";
	public String 	CouponIdx= "";
	
	
	public String 	PhoneNumber = "";
	
	public Boolean 	FavoriteResi =false;

	
	public ArrayList<MainCouponData> 	CouponArray = new ArrayList<MainCouponData>();
	
	public ArrayList<AroundTaxiData> 	AroundTaxiArray = new ArrayList<AroundTaxiData>();
	
	
	public  ArrayList<ContactData> 	ContactArray = new ArrayList<ContactData>();
	public  ArrayList<FavoriteData> FavoriteArray = new ArrayList<FavoriteData>();
	public  ArrayList<BoardLog> BoardArray = new ArrayList<BoardLog>();
	
	public ArrayList<TaxiCompanyInfo> TaxiInfoArray = new ArrayList< TaxiCompanyInfo>();
	


	@Override
	public void onCreate()
	{
		super.onCreate();
		m_Cookie = new CookieHTTP();
		DeviceID = getMD5Hash(android.os.Build.SERIAL + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
		
		Log.e("Device ID",DeviceID);
		SharedPreferences preferences = getSharedPreferences( "userinfo" ,MODE_PRIVATE);
		if (preferences.getString("uuid", "").equals(DeviceID) )
		{
			
		}
		else
		{
			SharedPreferences.Editor edit = preferences.edit();
			edit.putString("uuid", "");
			edit.commit();
		}
		LoadContact();
		LoadFavorite();
		LoadBoard();
		
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

    
	

	public void screenshot(View view, String number)throws Exception 
	{      
		
		{
			File file1 = new File(Environment.getExternalStorageDirectory() +"/DCIM/Camera/"); 

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
		view.setDrawingCacheEnabled(true);
		 
		Bitmap screenshot = view.getDrawingCache();
		 
		String filename = number +".jpg";
		 
		try {
		 
		File f = new File(Environment.getExternalStorageDirectory() +"/DCIM/Camera/" , filename);
		 
		f.createNewFile();
		 
		OutputStream outStream = new FileOutputStream(f);
		 
		screenshot.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
		 
		outStream.close();
		 
		} catch (IOException e) {
		 
		e.printStackTrace();
		 
		}
		
		Toast.makeText(this
                .getApplicationContext(), 
                "/DCIM/Camera/" + filename + " 로 저장되었습니다.",
                Toast.LENGTH_LONG).show();
		 
		view.setDrawingCacheEnabled(false);
		 
	}
	
	public void SaveContact()
	{
		SharedPreferences preferences = getSharedPreferences( "contacts" ,MODE_PRIVATE);
		SharedPreferences.Editor edit = preferences.edit();
		
		Set<String> set = new HashSet<String>();
		for ( int i = 0 ; i <ContactArray.size() ;i++)
		{
			String data= ContactArray.get(i).Name + "," + ContactArray.get(i).Number+ "," + ContactArray.get(i).Check;
			set.add(data);
		}
		edit.putStringSet("Contact", set);

		
		edit.commit();
		

	}
	public void LoadContact()
	{
		Set<String> set ;
		SharedPreferences preferences = getSharedPreferences( "contacts" ,MODE_PRIVATE);
		{
			set = preferences.getStringSet("Contact", null);
		}
		
		if ( set== null || set.size() == 0 )
		{
			
		}
		else
		{
			
			String[] array = set.toArray(new String[set.size()]);
			for ( int i = 0 ; i < array.length ; i++  )
			{
				String Data =  array[i]; 
				
				StringTokenizer token = new StringTokenizer(Data,",");
				
				ContactData data = new ContactData();
				data.Name = token.nextToken();
				data.Number = token.nextToken();
				data.Check = token.nextToken();
				ContactArray.add(data);
				
			}
		}
		
		
	}
	public void AddContact( String Name , String Number )
	{
		ContactData data = new ContactData();
		data.Name = Name;
		data.Number = Number;
		ContactArray.add(data);
		SaveContact();
	}
	
	public void RemoveContact( ContactData data )
	{
		ContactArray.remove(data);
		SaveContact();
	}
	
	
	
	public void SaveFavorite()
	{
		SharedPreferences preferences = getSharedPreferences( "favorite" ,MODE_PRIVATE);
		SharedPreferences.Editor edit = preferences.edit();
		
		Set<String> set = new HashSet<String>();
		for ( int i = 0 ; i <FavoriteArray.size() ;i++)
		{
			String data= FavoriteArray.get(i).Name + "," + FavoriteArray.get(i).Lat + ","+ FavoriteArray.get(i).Lng;
			set.add(data);
		}
		edit.putStringSet("Favorite", set);
		edit.commit();
	}
	public void LoadFavorite()
	{
		Set<String> set ;
		SharedPreferences preferences = getSharedPreferences( "favorite" ,MODE_PRIVATE);
		{
			set = preferences.getStringSet("Favorite", null);
		}
		if ( set== null || set.size() == 0 )
		{
			
		}
		else
			
		{
			String[] array = set.toArray(new String[set.size()]);
			for ( int i = 0 ; i < array.length ; i++  )
			{
				String Data =  array[i]; 
				
				StringTokenizer token = new StringTokenizer(Data,",");
				
				FavoriteData data = new FavoriteData();
				data.Name = token.nextToken();
				data.Lat = token.nextToken();
				data.Lng = token.nextToken();
				FavoriteArray.add(data);
				
			}
		}
		
	}
	public void AddFavorite( String Name , String lat,String lng )
	{
		FavoriteData data = new FavoriteData();
		data.Name = Name;
		data.Lat = lat;
		data.Lng = lng;
		FavoriteArray.add(data);
		SaveFavorite();
	}
	
	public void RemoveFavorite( FavoriteData data )
	{
		FavoriteArray.remove(data);
		SaveFavorite();
	}
	
	public void SaveBoard()
	{
		SharedPreferences preferences = getSharedPreferences( "board" ,MODE_PRIVATE);
		SharedPreferences.Editor edit = preferences.edit();
		
		Set<String> set = new HashSet<String>();
		for ( int i = 0 ; i <BoardArray.size() ;i++)
		{
			String data= BoardArray.get(i).Idx + "," + BoardArray.get(i).DateTime+ ","+ BoardArray.get(i).CarNumber + ","+ BoardArray.get(i).Number;
			set.add(data);
		}
		edit.putStringSet("BOARD", set);
		edit.commit();
	}
	public void LoadBoard()
	{
		Set<String> set ;
		SharedPreferences preferences = getSharedPreferences( "board" ,MODE_PRIVATE);
		{
			set = preferences.getStringSet("BOARD", null);
		}
		if ( set== null || set.size() == 0 )
		{
			
		}
		else
			
		{
			String[] array = set.toArray(new String[set.size()]);
			for ( int i = 0 ; i < array.length ; i++  )
			{
				String Data =  array[i]; 
				
				StringTokenizer token = new StringTokenizer(Data,",");
				
				;
				BoardLog data = new BoardLog();
				if ( token.hasMoreTokens())
					data.Idx = token.nextToken();
				else
					data.Idx ="";
				if ( token.hasMoreTokens())
					data.DateTime = token.nextToken();
				else
					data.DateTime ="";
				if ( token.hasMoreTokens())
					data.CarNumber = token.nextToken();
				else
					data.CarNumber ="";
				if ( token.hasMoreTokens())
					data.Number = token.nextToken();
				else
					data.Number ="";
				BoardArray.add(data);
				
			}
		}
		
	}
	public void AddBoard( String index, String Time, String Car, String Number )
	{
		BoardLog data = new BoardLog();
		data.Idx = index;
		data.DateTime = Time;
		data.CarNumber = Car;
		data.Number = Number;
		BoardArray.add(data);
		SaveBoard();
	}
	
	public void RemoveBoard( BoardLog data )
	{
		BoardArray.remove(data);
		SaveBoard();
	}
	
	
	public void GetTaxiCompanyInfo( final String LocationName , final Handler handle)
	{

		TaxiInfoArray.clear();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{


				Map<String, String> data = new HashMap  <String, String>();
				

				
				
				
				data.put("target","local");
				data.put("query",LocationName + " 콜택시");
				data.put("key","1d95a6f23243ab0d61a2fec4dd0b42e3");
				data.put("display","30");
				
				
				
				String strJSON = GetHttpManager().GetHTTPData( "http://openapi.naver.com/search", data);

				try{
					// DOM 파서 생성
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					
					DocumentBuilder b = factory.newDocumentBuilder();
					

					InputStream istream = new ByteArrayInputStream(strJSON.getBytes("utf-8"));
					org.w3c.dom.Document doc = b.parse(istream);
					
					org.w3c.dom.Element root = doc.getDocumentElement();
					
					NodeList items = root.getElementsByTagName("channel").item(0).getChildNodes();
					
					String result = "";
					for ( int i = 0 ; i < items.getLength() ; i++ ) 
					{
						
						Node item = items.item(i);
						
						if ( item.getNodeName().equals("item"))
						{
							Node temp = item.getFirstChild();
							
							TaxiCompanyInfo info = new TaxiCompanyInfo();
							while(true)
							{
								if (temp.getNodeName().equals("title") )
									info.TaxiCompanyName = temp.getFirstChild().getNodeValue();
								else if (temp.getNodeName().equals("telephone"))
								{
									info.Telephone = temp.getFirstChild().getNodeValue();
								}
								temp = temp.getNextSibling();
								if ( temp == null)
									break;
									
							}
							if(info.Telephone != null && info.Telephone.equals("") == false)
							{
								TaxiInfoArray.add(info);
							}
						}
					}
					
				}
				
				catch (Exception e) {
					Log.d("TAG", e+"dom fail");
				}

				handle.sendEmptyMessage(20001);
			}
			
		});
		thread.start();
	}

	public CookieHTTP GetHttpManager()
	{
		return m_Cookie;
	}
	
	public UISizeConverter GetUISizeConverter()
	{
		return m_UISizeConverter;
	}
	public void CreateUISizeConverter( int size )
	{
		m_UISizeConverter = new UISizeConverter(getBaseContext(), size);
	}
	
	public void SendSMS(  String CarNumber, String Loaction )
	{
		SmsManager sms = SmsManager.getDefault();   
		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);   

		for ( int i = 0 ; i <ContactArray.size() ;i++)
		{
			if ( !ContactArray.get(i).Check.equals("0"))
				sms.sendTextMessage(ContactArray.get(i).Number, null, "[타요타요 알림]" + mgr.getLine1Number() +"님이 " + Loaction + "에서 "+ "택시("+CarNumber+")를 탑승했습니다." , null, null);
		}

	}
  
	
	
	


}

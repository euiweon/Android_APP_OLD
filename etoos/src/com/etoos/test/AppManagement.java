package com.etoos.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.etoos.data.DefaultTestData;
import com.etoos.data.EventData;
import com.etoos.data.LoginData;
import com.etoos.data.OMRContent;
import com.etoos.data.OMRHistoryContent;
import com.etoos.data.SchoolData;
import com.euiweonjeong.base.CookieHTTP;
import com.euiweonjeong.base.UISizeConverter;



public class AppManagement extends Application  {




	/////////////////////////////////////////////////////////////////////////////////
	/// 어플리케이션 정보들 ( Http , UI 등등 )
	public CookieHTTP		m_Cookie ;
	public UISizeConverter 	m_UISizeConverter;
	public String 			DEF_URL ="http://smc.hum-soft.com:8080";
	public String 			WEB_URL ="http://smc.hum-soft.com:8080";
	
	public String 			TestProblemURL = "http://www.naver.com";



	public Integer 			m_FastSelectIndex = -1;


	/////////////////////////////////////////////////////////////////////////////////
	//  빠른 응시 관련 데이터들....

	public ArrayList< OMRHistoryContent > m_FastHistoryListData ;	// 마킹한 히스토리를 출력한다. 
	public ArrayList< OMRContent > m_FastListData;	// 


	public String 	FastTestTitle ="";
	public String 	FastTestName = "";
	public Integer	FastTestSex = 0;
	public String 	FastTestGrade = "";
	public String 	FastTestSchool = "";
	public String 	FastTestInstute = "";


	public String 	FastTimer = "";
	public Integer 	FastTestmakingCount = 0;


	public Boolean 	FastTestLoad = false;



	//////////////////////////////////////////////////////////////////////////////
	// API 연동 
	public String ParseString ="";
	public Map<String, String> ParamData = new HashMap  <String, String>();

	public ArrayList< EventData > m_EventList = new ArrayList< EventData >();

	public ArrayList<Activity> 	activityList1 = new ArrayList<Activity>();


	// 정식 OMR 관련 데이터 
	public ArrayList<OMRContent>	m_DefaultOMR = new 	ArrayList<OMRContent>();

	public boolean					m_LoginCheck = false; 
	public DefaultTestData			m_DefaultTestData = new DefaultTestData();


	// 
	public String 		m_ResultCode = "";
	public String 		m_ResultMsg = "";
	public ArrayList<SchoolData>	m_SchoolList = new 	ArrayList<SchoolData>();
	
	
	public boolean 		m_Connect = true;
	
	public ArrayList<Integer>			m_OMRMaingIDList =  new  ArrayList<Integer>();
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// 
	
	public String	uid;
	public String 	gid;
	public String 	eid;
	

	LoginData m_LoginData = new LoginData();

	@Override
	public void onCreate()
	{
		super.onCreate();
		m_Cookie = new CookieHTTP();


		// 가장 먼저 응시중인 리스트부터 로드한다. 
		LoadOMRMaingIDList();
		{
			SharedPreferences sharedPref;
			sharedPref = getSharedPreferences("cookie", Activity.MODE_PRIVATE);

			Map<String, String> cookieList = (Map<String, String>)sharedPref.getAll();
			Map<String, String> cookieList2 = new HashMap<String, String >();

			String domain = null;
			String path = null;

			if ( cookieList != null )
			{
				// 데이터 출력 
				for( String key : cookieList.keySet() )
				{
					Log.d("Cookie11",  "Key: "+key + "  Value : " + cookieList.get(key));
				}

				for( String key : cookieList.keySet() )
				{
					if ( key.equals("domain") )
					{
						domain= cookieList.get(key);
					}
					else if ( key.equals("path")  )
					{
						path= cookieList.get(key);
					}
					else
					{
						cookieList2.put(key, cookieList.get(key));
					}
				}
				m_Cookie.SetCookie(cookieList, domain, path);
			}

		}
		m_Cookie.GetCookies();
	}
	
	
	public void LoadOMRMaingIDList()
	{
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("OMRMainList", Activity.MODE_PRIVATE);

		Set<String> temp =  sharedPref.getStringSet("gIDList", null);
		m_OMRMaingIDList.clear();
		if ( temp != null )
		{
			for ( int i = 0 ; i < temp.size() ; i++ )
			{
				m_OMRMaingIDList.add(Integer.parseInt((String) temp.toArray()[i]));
			}
		}
		
	}
	
	public void SaveOMRMaingIDList()
	{
		Set<String> temp =  new HashSet<String>();
		
		temp.clear();
		
		for ( int i = 0 ; i < m_OMRMaingIDList.size() ; i++ )
		{
			temp.add(m_OMRMaingIDList.get(i).toString());
		}
		
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("OMRMainList", Activity.MODE_PRIVATE);
	
		SharedPreferences.Editor sharedEditor = sharedPref.edit();
		sharedEditor.putStringSet("gIDList", temp);
		sharedEditor.commit();
	}
	
	public void AddOMRMaingIDList( int gid)
	{
		boolean check = false; 
		
		for ( int i = 0 ; i < m_OMRMaingIDList.size() ; i++ )
		{
			if (m_OMRMaingIDList.get(i) == gid)
			{
				check = true; 
			}
		}
		
		if ( check )
		{
			// 중복된 아이디가 있으므로 에러
		}
		else
		{
			m_OMRMaingIDList.add(gid);
			SaveOMRMaingIDList();
		}
	}
	
	public void RemoveOMRMaingIDList(  int gid )
	{
		int check = -1; 
		
		for ( int i = 0 ; i < m_OMRMaingIDList.size() ; i++ )
		{
			if (m_OMRMaingIDList.get(i) == gid)
			{
				check = i; 
			}
		}
		
		if ( check != -1)
		{
			m_OMRMaingIDList.remove(check);
			SaveOMRMaingIDList();
		}
	}
	
	public boolean CheckOMRMaingIDList(  int gid )
	{
		boolean check = false; 

		for ( int i = 0 ; i < m_OMRMaingIDList.size() ; i++ )
		{
			if (m_OMRMaingIDList.get(i) == gid)
			{
				check = true; 
			}
		}
		
		return check;

	}


	public void CheckinTempSoluation()
	{
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("test1", Activity.MODE_PRIVATE);


		SharedPreferences.Editor sharedEditor = sharedPref.edit();
		sharedEditor.putBoolean("check", true);
		sharedEditor.commit();
	}
	



	public void CheckOutTempSoluation()
	{
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("test1", Activity.MODE_PRIVATE);


		SharedPreferences.Editor sharedEditor = sharedPref.edit();
		sharedEditor.putBoolean("check", false);
		sharedEditor.commit();
	}

	public boolean CheckTest()
	{
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("test1", Activity.MODE_PRIVATE);

		return sharedPref.getBoolean("check", false); 
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

	public void LogInDataParsing( )
	{

		JSONObject json;
		try 
		{
			json = new JSONObject(ParseString);
			m_ResultCode = json.getString("resultcode");
			m_ResultMsg = json.getString("resultmsg");
			if(json.getString("resultcode").equals("success"))
			{

				m_LoginData.name = (json.optString("name"));
				m_LoginData.gender = (json.optString("gender"));
				m_LoginData.gcode = (json.optString("gcode"));
				m_LoginData.gname = (json.optString("gname"));
				m_LoginData.shcode = (json.optString("shcode"));
				m_LoginData.shname = (json.optString("shname"));
				m_LoginData.eduinstitute1 = (json.optString("eduinstitute1"));
				m_LoginData.eduinstitute2 = (json.optString("eduinstitute2"));
				m_LoginData.eduinstitute3 = (json.optString("eduinstitute3"));
				m_LoginData.pushset1 = (json.optString("pushset1"));



			}
			else
			{
				// 에러...
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void DefaultOMRData()
	{
		JSONObject json;
		try 
		{
			json = new JSONObject(ParseString);
			m_ResultCode = json.getString("resultcode");
			if(json.getString("resultcode").equals("success")||json.getString("resultcode").equals("SUCCESS") )
			{

				m_DefaultTestData.title = (json.optString("title"));
				m_DefaultTestData.target = (json.optString("target"));
				m_DefaultTestData.scode = (json.optString("scode"));
				m_DefaultTestData.sname = (json.optString("sname"));
				m_DefaultTestData.premark = (json.optString("premark"));
				m_DefaultTestData.trialtime = (json.optString("trialtime"));
				m_DefaultTestData.totalitem = (json.optString("totalitem"));
				m_DefaultTestData.objectivecnt = (json.optString("objectivecnt"));
				m_DefaultTestData.answercnt = (json.optString("answercnt"));
				m_DefaultTestData.answertype = (json.optString("answertype"));

				m_DefaultOMR.clear();
				JSONArray usageList = (JSONArray)json.get("omrlist");

				for(int i = 0; i < usageList.length(); i++)
				{
					OMRContent item = new OMRContent();
					JSONObject list = (JSONObject)usageList.get(i);

					item.Number = (list.optInt("number"));
					item.QuestionType = (list.optInt("answertype"));
					item.AnswerCount = (list.optInt("answercnt"));	
					item.refQuestion = (list.optInt("iid"));
					m_DefaultOMR.add(item);
				}


			}
			else
			{
				// 에러...
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void AfterPostOMRData()
	{
		JSONObject json;
		try 
		{
			json = new JSONObject(ParseString);
			m_ResultCode = json.getString("resultcode");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SchoolList()
	{
		JSONObject json;
		try 
		{
			json = new JSONObject(ParseString);
			m_ResultCode = json.getString("resultcode");
			if(json.getString("resultcode").equals("success"))
			{


				m_SchoolList.clear();
				JSONArray usageList = (JSONArray)json.get("schoollist");

				for(int i = 0; i < usageList.length(); i++)
				{
					SchoolData item = new SchoolData();
					JSONObject list = (JSONObject)usageList.get(i);

					item.shcode = (list.optString("shcode"));
					item.name = (list.optString("answertype"));
					m_SchoolList.add(item);
				}


			}
			else
			{
				// 에러...
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}

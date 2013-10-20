package com.pricecode;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.rangboq.xutil.XManager;
import com.rangboq.xutil.XResultList;

public class CMManager extends XManager
{
	// //////////////////////////////////////////////////////////////////////////////////
	public static boolean enableHttps = false;
	public static final String MAP_API_KEY = "292f93e196213727b55fcf02083221c7783edbf3";
	public static final String LOCAL_API_KEY = "046e13ffc132df21824e0ecb7de77122e8cadbb5";
	
	
	
	// //////////////////////////////////////////////////////////////////////////////////
	private static XResultList m_MyInfoList = null;
	public static XResultList getMyInfoList()
	{
		return m_MyInfoList;
	}
	public static void setMyInfoList(XResultList myInfoList)
	{
		m_MyInfoList = myInfoList;
		if(m_MyInfoList != null)
		{
			if(m_MyInfoList.size() > 0)
				m_MyInfoList.get(0).remove("passwd");
			saveObjectToFile(m_MyInfoList, m_strMyInfoFileName);
		}
	}
	private static XResultList m_AreaList = null;
	public static XResultList getAreaList()
	{
		return m_AreaList;
	}
	public static void setAreaList(XResultList areaList)
	{
		m_AreaList = areaList;
		if(m_AreaList != null)
			saveObjectToFile(m_AreaList, m_strAreaListFileName);
	}
	public static String getMyInfo(String strKey)
	{
		if(strKey == null || strKey.length() == 0)
			return null;
		if(m_MyInfoList == null || m_MyInfoList.size() == 0)
			return null;
		return m_MyInfoList.get(0).get(strKey);
	}

	// //////////////////////////////////////////////////////////////////////////////////
	private static boolean m_bIsOnline = false;
	public static boolean isOnline()
	{
		return m_bIsOnline;
	}

	public static void setOnline( boolean bOnline, String strId, String strPassword )
	{
		m_bIsOnline = bOnline;
		setPrefBoolean(PREF_IS_ONLINE, m_bIsOnline);
		if(strId != null)
			setPrefString(PREF_USER_ID, strId);
		if(strPassword != null)
			setPrefStringS(PREF_USER_PASSWORD, strPassword);
	}

	public static boolean checkOnline( CMActivity curActivity, boolean bShowLogin )
	{
		setContext(curActivity);
		
		if( isOnline() == false && loadOnlineInfo() == false )
		{
			curActivity.showToast("먼저 로그인하여 주십시오.");

			try
			{
				Thread.sleep(1000);
			}
			catch( InterruptedException e )
			{
				e.printStackTrace();
			}

			if( bShowLogin )
			{
				Intent intent = new Intent(curActivity, LoginActivity.class);
				curActivity.startActivity(intent);
			}
			return false;
		}

		return true;
	}


	// //////////////////////////////////////////////////////////////////////////////////
	public static final String PREF_AUTO_LOGIN 		= "AUTO_LOGIN";
	public static final String PREF_IS_ONLINE 		= "IS_ONLINE";
	public static final String PREF_USER_ID 		= "USER_ID";
	public static final String PREF_USER_PASSWORD 	= "USER_PASSWORD";

	// //////////////////////////////////////////////////////////////////////////////////
	public static void setContext( Context context )
	{
		if( context == null )
			return;

		if( m_AppContext == null )
		{
			XManager.setContext(context);
			initFolders();
		}
	}

	public static Context getContext()
	{
		return m_AppContext;
	}
	
	private static final String m_strMyInfoFileName = "myinfo_data.dat";
	private static final String m_strAreaListFileName = "arealist_data.dat";
	public static boolean loadOnlineInfo()
	{
		if(getContext() == null)
			return false;
		m_bIsOnline = getPrefBoolean(PREF_IS_ONLINE, false);
		if(m_bIsOnline == false)
			return false;
		
		XResultList myInfoList = (XResultList) loadObjectFromFile(getContext(), m_strMyInfoFileName);
		XResultList areaList = (XResultList) loadObjectFromFile(getContext(), m_strAreaListFileName);
		if(myInfoList != null && myInfoList.size() > 0 && areaList != null && areaList.size() > 0)
		{
			m_MyInfoList = myInfoList;
			m_AreaList = areaList;
			return true;
		}

        return false;
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public static Intent getIntent( Activity curActivity, Class<?> cls )
    {
		setContext(curActivity);
		
	    Intent intent = new Intent(curActivity, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return intent;
    }

	public static Intent getIntent( Context context, Class<?> cls )
    {
		setContext(context);
		
	    Intent intent = new Intent(context, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return intent;
    }

	public static String ImageTempPath = null;
	public static String ImageThumbPath = null;
	public static String ImageSourcePath = null;
	public static void initFolders()
	{
		  String strAppName = "coopMarket";
		  
		  File sdcardFolder = Environment.getExternalStorageDirectory();
		  String sdcardPath = sdcardFolder.getPath();
		  
		  ImageTempPath = sdcardPath + "/" + strAppName + "/" + ".temp/";
		  File tempFolder = new File(ImageTempPath);
		  if(tempFolder.exists() == false)
			  tempFolder.mkdirs();
		  
		  ImageThumbPath = sdcardPath + "/" + strAppName + "/" + ".thumb/";  
		  File thumbFolder = new File(ImageThumbPath);
		  if(thumbFolder.exists() == false)
			  thumbFolder.mkdirs();
		  
		  ImageSourcePath = sdcardPath + "/" + strAppName + "/" + ".source/";
		  File sourceFolder = new File(ImageSourcePath);
		  if(sourceFolder.exists() == false)
			  sourceFolder.mkdirs();
	}

	private static final String IMAGE_SUB_PATH = "/coupon_img/"; 
	private static final int IMAGE_SUB_PATH_LENGTH = IMAGE_SUB_PATH.length();
	public static String getImageLocalSubPath(String strLink)
	{
		String strSubPath = "";
		
		int nFound = strLink.indexOf(IMAGE_SUB_PATH);
		if(nFound > 0)
		{
			strSubPath = strLink.substring(nFound + IMAGE_SUB_PATH_LENGTH);
			nFound = strSubPath.lastIndexOf("/");
			if(nFound >= 0)
				strSubPath = strSubPath.substring(0, nFound + 1);
		}
		
		return strSubPath;
	}
}

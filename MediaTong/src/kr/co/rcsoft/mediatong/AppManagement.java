package kr.co.rcsoft.mediatong;

import com.euiweonjeong.base.CookieHTTP;
import com.euiweonjeong.base.UISizeConverter;

import android.app.Application;

public class AppManagement extends Application  {
	
	public CookieHTTP		m_Cookie = new CookieHTTP();
	
	public UISizeConverter 	m_UISizeConverter;
	
	public Boolean m_LoginCheck = false;
	public Integer m_RecruitSelectIndex = -1;
	
	public Integer m_UpjongIndex = -1;
	public Integer m_CompanyIndex = -1;
	
	public Integer m_EventTabIndex = 0;
	public Integer m_SelectScrapType = 0;
	
	public String m_PCVerAddress;
	
	public QNAData m_QnaData = new QNAData();
	
	public NoticeData  m_NoticeData= new NoticeData();
	public EventData m_Eventdata = new EventData();
	
	public MyjobInfo m_MyjobInfo = new MyjobInfo();
	
	public class MapPosition
	{
		public String Latitude;
		public String Longitude;
		public String Name;
	}
	
	

	
	
	MapPosition m_MapInfomation;
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		// 맵의 기본값 설정 
		{
			m_MapInfomation = new MapPosition();
			m_MapInfomation.Latitude = "126.978371";
			m_MapInfomation.Longitude = "37.5666091";
			m_MapInfomation.Name = "임시 장소";
		}

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
		

}

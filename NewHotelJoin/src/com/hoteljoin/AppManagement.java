package com.hoteljoin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.euiweonjeong.base.CookieHTTP;
import com.euiweonjeong.base.UISizeConverter;
import com.hoteljoin.data.*;




enum CURR_XML_STATE
{
	CURR_XML_DES,
	CURR_XML_LOCAL,
	CURR_XML_NAME,

	CURR_XML_UNKNOWN,
}

public class AppManagement extends Application  {
	
	
	/////////////////////////////////////////////////////////////////////////////////
	/// 어플리케이션 정보들 ( Http , UI 등등 )
	public CookieHTTP		m_Cookie ;
	public UISizeConverter 	m_UISizeConverter;
	//public String 			DEF_URL ="http://121.133.123.239:8080";
	public String 			DEF_URL ="http://222.231.25.30";
	

	/////////////////////////////////////////////////////////////////////////////////
	// 공용 
	public String		m_HotelCode = "";	// 호텔 코드 
	public String 		m_CityCode = "";
	public String 		m_NationCode = "";
	
	/////////////////////////////////////////////////////////////////////////////////
	//  개인정보
	public String m_LoginID = "";
	public String m_Password = "";
	public String m_NickName = "";
	public String m_Name = "";
	public String m_Email = "";
	public String m_Mobile = "";
	public boolean 			m_LoginCheck = false;
	
	
	/////////////////////////////////////////////////////////////////////////////////
	// 이벤트 
	
	public Integer m_EventNum;		// 이벤트 액티비티에서 선택한 이벤트 번호
	
	
	/////////////////////////////////////////////////////////////////////////////////
	// 호텔 검색 
	
	public String		m_MyLng= "-1";				// 경도
	public String		m_MyLat=  "-1";				// 위도

	public String 		m_SearchCode = "S1";		// 검색 분류 코드 
	public String 		m_DestinationCode ="";		// 목적지 코드 
	public Boolean 		m_SearchWorld = true; 		// 국내 해외 여부 
	public String 		m_CheckInDay ="";			// 체크인
	public String 		m_Duration ="";				// 숙박 일수
	public String 		m_NumRoom ="";				// 객실 갯수
	public String 		m_NumPer ="";				// 인원수 
	
	
	/////////////////////////////////////////////////////////////////////////////////
	// 호텔 검색 결과
	
	public ArrayList< HotelSearchData > m_HotelSearchData = new ArrayList< HotelSearchData >();
	ArrayList< SearchListData > m_HotelSearchListData;
	
	/////////////////////////////////////////////////////////////////////////////////
	// 호텔 상세 페이지 
	
	
	// 호텔 위치
	public String 		m_HotelLng = "";
	public String 		m_HotelLat ="";
	
	
	public String 		m_HotelName = "";  // 호텔 이름 
	public ArrayList<String>	m_GallayList;	// 호텔 사진 리스트 
	
	/////////////////////////////////////////////////////////////////////////////////
	// 예약 
	
	public String 		m_ReservationName;
	public String 		m_ReservationNumber;
	public String 		m_ReservationNationCode;
	public RoomDetailData	m_RoomDetailData = new RoomDetailData();
	public ArrayList<RoomOptionData>	m_RoomOptionData = new ArrayList<RoomOptionData>();
	public ArrayList<RoomGuestList>	m_RoomGuestData = new ArrayList<RoomGuestList>();
	public ArrayList<CancelPolicyList>	m_RoomCancel = new ArrayList<CancelPolicyList>();
	
	public ArrayList< ReservationPersonList []>  m_HotelGuestData = new ArrayList< ReservationPersonList[] >();
	public ArrayList< ReservationRemarkerList > m_RemarkerData = new ArrayList< ReservationRemarkerList >();

	public RoomReserData 	m_RoomReserData = new RoomReserData();
	
	
	public String 		m_TotalPrice = "";
	public String 		m_TotalPrice2 = "";
	
	public String 		m_ResvNum = "";
	public String 		m_lodgeName = "";
	public String 		m_resvName = "";
	public String 		m_resvEmail = "";
	public String 		m_resvTel = "";
	public String 		m_resvMobile = "";
	public String 		m_resvPasswd = "";
	public String 		m_resvIyagi = "";
	
	public String 		m_PayURL = "";
	
	public String 		supplyCode = "";
	public String 		roomRequestKey = "";
	
	public String 		m_NumChild = "0";
	
	/////////////////////////////////////////////////////////////////////////////////
	// 여행일지 
	public Boolean		m_DirectMyTravel = false;
	public String 		m_DirNum ="";
	
	public String 		m_BoardNum = "";	
	
	public Boolean 		m_UsbHotelcode = false;

	public String 		m_diaryNum = "";
	public String 		m_cntntNum = "";
	public String 		m_rewriteImage = "";
	public String 		m_rewriteContent = "";
	public String 		m_rewriteTitle = "";
	public String 		m_rewritenation = "";
	public String 		m_rewritecity = "";

	public String 		m_rewritehotelcode = "";
	public String 		m_rewritehotelname = "";
	
	
	public String 		m_writeNationCode;
	public String 		m_writeCityCode;
	
	
	
	/////////////////////////////////////////////////////////////////////////////////
	// 웹뷰 
	
	public String 		m_URL= "";
	public String		m_URLTitle ="";


	/////////////////////////////////////////////////////////////////////////////////
	// 그외 
	public Integer		m_SNSSelect = 2;
	public String 		m_MainimageURL = "";

	public String 		m_HotelGrade = "-1";
	public String 		m_HotelReceipt = "-1";

	public Boolean 		m_RefreshUI = false;	
	public String 		m_RemarkString = "";
	
	
	/////////////////////////////////////////////////////////////////////////////////
	// Loading 
	
	public Activity 	NextActivity;		// 다음 액티비티로 넘어갈 것
	public String 		LoadingBarTitle; 	// Loading Activity Title
	public String 		LoadingBatDesc; 	// Loading Activity  설명 
	public String 		ParseString;		// 다음 액티비티에서 파싱해야할 데이터 
	
	public Map<String, String> ParamData = new HashMap  <String, String>();	// API 에 넘겨줘야할 파라미터 
	
	/////////////////////////////////////////////////////////////////////////////////
	// Parsing 
	public Integer		PErrorCode;			// 에러코드 
	public String 		PErrorMsg;			// 에러메세지
	public JSONDATAParsing	PParsingClass;	// JSON 파싱만 하는 클래스 
	public DataManagement	PParsingData;	// 파싱을 끝내고 난 데이터...
	
	public ArrayList<Activity> 	activityList1 = new ArrayList<Activity>();

	@Override
	public void onCreate()
	{
		super.onCreate();
		m_Cookie = new CookieHTTP();

		m_GallayList = new ArrayList<String>();
		
		PParsingClass = new JSONDATAParsing(this);
		PParsingData = new DataManagement();

		
		LoadHotelSearchData();
		
		ParamData.clear();
		
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
	
	// 호텔 검색 정보 읽어 오기 
	public void LoadHotelSearchData()
	{
		m_HotelSearchData.clear();
		
		String favoriteString = "";
		{
	        /////
	        {
	        	FileReader reader = null;
	        	
	        	try {
					reader = new FileReader( Environment.getExternalStorageDirectory() +"/android/data/com.hoteljoin/" + "hotelsearch.xml" );
					StringBuffer buffer = new StringBuffer();
					
					int data = 1;
					while ( data > 0 )
					{
						data = reader.read();
						buffer.append((char)data );
					}
					favoriteString = buffer.toString();
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
		
		
		XmlPullParserFactory xmlpf = null;
		
		favoriteString = favoriteString.replaceAll("\r", "");
		favoriteString = favoriteString.replaceAll("\n", "");
		
		//try 
		{
			// XML 파서를 초기화

			try {
				xmlpf = XmlPullParserFactory.newInstance();
			} catch (XmlPullParserException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
			XmlPullParser xmlp = null;
			try {
				xmlp = xmlpf.newPullParser();
			} catch (XmlPullParserException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			try {
				xmlp.setInput(new StringReader(favoriteString));
			} catch (XmlPullParserException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			// 파싱에 발생하는 event?를 처리하기 위한 메소드
            int eventType = 0;
			try {
				eventType = xmlp.getEventType();
			} catch (XmlPullParserException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            
            CURR_XML_STATE estate = CURR_XML_STATE.CURR_XML_UNKNOWN;
            
            HotelSearchData data = null; ;
            // XML문서 읽기를 반복함미다.
            while( eventType != XmlPullParser.END_DOCUMENT )
            {


                switch ( eventType ) 
                {
                case XmlPullParser.START_DOCUMENT: // 문서 시작 태그를 만난 경우
                case XmlPullParser.END_DOCUMENT: // 문서 끝 태그를 만난 경우
                    break;
                case XmlPullParser.START_TAG: // 쌍으로 구성된 태그의 시작을 만난 경우
                    // 요소명 체크
                    if ( xmlp.getName().equals("destination") )
                    { 
                        estate = CURR_XML_STATE.CURR_XML_DES;
                    }
                    else if ( xmlp.getName().equals("local")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_LOCAL;

                    }
                    else if ( xmlp.getName().equals("name")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_NAME;
                    }
                   

                case XmlPullParser.END_TAG: // 쌍으로 구성된 태그의 끝을 만난 경우
                    break;
                case XmlPullParser.TEXT: // TEXT로 이루어진 내용을 만난 경우
                    switch ( estate )
                    {
                    case CURR_XML_DES:
                    {
                    	
                    	data.Destination = xmlp.getText();
                    }
                    	break;
                    case CURR_XML_LOCAL:
                    {
                    	data = new HotelSearchData();
                    	if ( Integer.parseInt(xmlp.getText()) ==1 )
                    	{
                    		data.bLocal = true;
                    	}
                    	else
                    	{
                    		data.bLocal = false;
                    	}
                    }
                    	break;
                    case CURR_XML_NAME:
                    {
                    	data.Name = xmlp.getText();
                    	m_HotelSearchData.add(data);
                    }
                    	break;


                    }
                    break;
                } // switch end
                
                // 다음 내용을 읽어옵니다
                try
                {
					try {
						eventType = xmlp.next();
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
                catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            } // while end
		}
	}
	
	
	// 호텔 검색 정보 저장 
	public void SaveHotelSearchData()
	{
		Element listdata=new Element("Hotel");
		Document doc=new Document(listdata);
		
		// XML 파일 저장 시작... 
		
		for ( int i = 0 ; i < m_HotelSearchData.size() ; i++  )
		{
		
			
			{
				Boolean temp = m_HotelSearchData.get(i).bLocal;
				Element mid=new Element("local"); 
				
				if ( temp )
					mid.addContent( "1");
				else
					mid.addContent( "0");
				
				listdata.addContent(mid);
			}
			
			{
				Element title=new Element("name"); 
				title.addContent( m_HotelSearchData.get(i).Name );			
				listdata.addContent(title);
			}
			{
				Element preacher=new Element("destination"); 
				preacher.addContent( m_HotelSearchData.get(i).Destination );
				listdata.addContent(preacher);
			}
		}
		//파일로 저장하기 위해서 XMLOutputter 객체가 필요하다
        XMLOutputter xout = new XMLOutputter();
		{
			File file1 = new File(Environment.getExternalStorageDirectory() +"/android/data/com.hoteljoin.hoteljoin/"); 

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
			xout.output(doc, new FileWriter(Environment.getExternalStorageDirectory() +"/android/data/com.hoteljoin.hoteljoin/" + "hotelsearch.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 호텔 검색 정보 추가 
	public void AddHotelSearchData( HotelSearchData data )
	{
		// 중복 검사
		Boolean Check = false;
		int index = 0;
		
		for ( int i = 0 ; i < m_HotelSearchData.size() ; i++  )
		{
			if ( m_HotelSearchData.get(i).Destination.equals(data.Destination) )
			{
				Check = true;
				index = i;
			}
		}
		
		// 중복일 경우 현재위치에서 빼내고 리스트의 마지막 위치로 옮긴다. 
		
		if ( Check )
		{
			m_HotelSearchData.remove(index);
		}

		// 중복이 아닐경우 그냥 추가만 한다. 
		m_HotelSearchData.add( data );
		SaveHotelSearchData();
	}

	// 호텔 검색 정보 단일 삭제
	public void RemoveHotelSearchData( HotelSearchData data )
	{
		m_HotelSearchData.remove(data);
		SaveHotelSearchData();
	}
	
	// 호텔 검색 정보 전체 삭제 
	public void RemoveAllHotelSearchData( )
	{
		m_HotelSearchData.clear();
		SaveHotelSearchData();
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

package com.pricecode;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.pricecode.*;
import com.pricecode.CMarketActivity.CURR_TOP_MENU_XML_STATE;
import com.rangboq.xutil.XFileDownloader;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XResultMap;
import com.rangboq.xutil.XUtilFunc;

public class DeliveryDetailActivity extends CMActivity implements OnClickListener
{
	DeliveryDetailActivity self;
	ImageView m_ivPhoto, m_ivMiniMenu, m_ivMiniCard;
	TextView m_tvCoopName, m_tvPoint, m_tvDeliveryArea, m_tvWorkTime, m_tvSavingMileage;
	
	Button m_btnAddress;
	ImageButton m_btnOrderCall, m_btnShowMenu;
	
	String m_strCategoryName = "배달";
	int m_nCategoryNo = 0;
	
	public CookieHTTP		m_Cookie ;
	public ProgressDialog mProgress;
	
	public String m_Productumber ="";
	
	
	class ReviewData
	{
		String Title = "";
		String Name;
		String Day;
		String Contents;
		Integer star;
	}
	
	enum CURR_XML_STATE
	{
		CURR_XML_NO,
		CURR_XML_ID,
		CURR_XML_GNO,
		CURR_XML_PNO,
		CURR_XML_CONTENT,
		CURR_XML_STAR,
		CURR_XML_DAY,
		CURR_XML_RESULT,

		CURR_XML_UNKNOWN,
	}
	
	ArrayList<ReviewData> m_ReviewData = new ArrayList<ReviewData>();
	
	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_delivery_detail);
	    
	    self = this;
	    m_ReviewData.clear();
		Intent curIntent = getIntent();
		if(curIntent != null)
		{
			m_nCategoryNo = curIntent.getIntExtra("category_no", 0);
			m_strCategoryName = curIntent.getStringExtra("category_name");
			if(m_strCategoryName == null)
				m_strCategoryName = "배달";
		}
		
		setTitleBarText(m_strCategoryName);
		
		m_Cookie = new CookieHTTP();
	    
	    m_ivPhoto = (ImageView) findViewById(R.id.imageView_photo);
	    m_ivMiniMenu = (ImageView) findViewById(R.id.imageView_menu);
	    m_ivMiniCard = (ImageView) findViewById(R.id.imageView_card);
	    m_tvCoopName = (TextView) findViewById(R.id.textView_coop_name);
	    m_tvPoint = (TextView) findViewById(R.id.textView_point);
	    m_tvDeliveryArea = (TextView) findViewById(R.id.textView_delivery_area);
	    m_tvWorkTime = (TextView) findViewById(R.id.textView_work_time);
	    m_tvSavingMileage = (TextView) findViewById(R.id.textView_saving_mileage);
	    m_btnAddress = (Button) findViewById(R.id.button_address);
	    m_btnOrderCall = (ImageButton) findViewById(R.id.imageButton_order_call);
	    m_btnShowMenu = (ImageButton) findViewById(R.id.imageButton_show_menu);
	    
	    m_ivMiniMenu.setOnClickListener(this);
	    m_btnAddress.setOnClickListener(this);
	    m_btnOrderCall.setOnClickListener(this);
	    m_btnShowMenu.setOnClickListener(this);
	    
	    
	 // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
	    
	    updateDeliveryInfo();
    }

	HashMap<String, String> m_infoMap = null;
	private void updateDeliveryInfo()
    {
		@SuppressWarnings("unchecked")
        HashMap<String, String> infoMap = (HashMap<String, String>)getIntent().getSerializableExtra("item_info");
	    if(infoMap == null && m_infoMap == null)
	    {
	    	showToast("상품정보가 없습니다.");
	    	finish();
	    	return;
	    }
	    m_infoMap = infoMap;
	    
		String strCoopName = null, strPoint = null, strDeliveryArea = null, strWorkTime = null, strAddress = null, strUseCard = null;
		strCoopName = m_infoMap.get("comp_name");
		strPoint = m_infoMap.get("point");
		strDeliveryArea = m_infoMap.get("address");
		strWorkTime = m_infoMap.get("work_hour");
		strUseCard = m_infoMap.get("use_card");
		strAddress = m_infoMap.get("address");
		m_Productumber  = m_infoMap.get("no");
		if( strCoopName == null )
			strCoopName = "";
		if( strDeliveryArea == null )
			strDeliveryArea = "";
		if( strPoint == null )
			strPoint = "";
		if( strWorkTime == null )
			strWorkTime = "";
		if( strUseCard == null )
			strUseCard = "";
		if( strAddress == null )
			strAddress = "";
		else
			strAddress = strAddress.replace("||", " ");
		
		if(strDeliveryArea.length() > 0)
		{
			int nFound = strDeliveryArea.indexOf("||");
			if(nFound > 0)
				strDeliveryArea = strDeliveryArea.substring(0, nFound);
		}
		
		m_tvCoopName.setText(strCoopName);
		m_tvPoint.setText(strPoint + "원");
		m_tvDeliveryArea.setText(strDeliveryArea);
		m_tvWorkTime.setText(strWorkTime);
		if(strUseCard == null || strUseCard.equals("Y") == false)
			m_ivMiniCard.setVisibility(View.INVISIBLE);

		Bitmap photoBitmap = null;
		String strDetailUrl = m_infoMap.get("goods_img");
		if(strDetailUrl != null && strDetailUrl.length() > 0)
		{
    		String strDetailName = XFileDownloader.getLastPart(strDetailUrl);
			String strSubDir = CMManager.getImageLocalSubPath(strDetailUrl);
    		String strDetailDir = CMManager.ImageSourcePath + strSubDir;
    		String strDetailPath = strDetailDir + strDetailName;
    		File file = new File(strDetailPath);
    		if( file.exists() )
    		{
    			try
    			{
    				photoBitmap = BitmapFactory.decodeFile(strDetailPath);
    				if(photoBitmap != null)
    					m_ivPhoto.setImageBitmap(photoBitmap);
        			photoBitmap = null;
    			}
    			catch( OutOfMemoryError e )
    			{
    				e.printStackTrace();
    			}
    		}
    		else
    		{
    			XFileDownloader.downloadFileFromWeb(getApplicationContext(), strDetailUrl, strDetailDir, true, 
    			                                    m_Handler, MSG_FOR_NOTIFY_PHOTO_DOWNLOADED);
    		}
		}
		
		m_tvSavingMileage.setText("전화 주문 확인시 " + strPoint + "원 적립");
		m_btnAddress.setText(strAddress);
		
		
		GetReplyData();
    }

	@Override
    protected void onDestroy()
    {
	    m_Handler = null;
	    super.onDestroy();
    }

	
	static final int MSG_FOR_NOTIFY_PHOTO_DOWNLOADED = 1;
	XHandler m_Handler = new XHandler()
	{
		@Override
        public void handleMessage( Message msg )
        {
			removeTimeoutMsg();
			if(m_Handler == null)
				return;
			
			if(msg.what == MSG_FOR_NOTIFY_PHOTO_DOWNLOADED)
			{
				updateDeliveryInfo();
				return;
			}
			else if(msg.what == XHandler.RESULT_TIME_OUT)
	        {
	        	cancelWait();
	        	toastCheckInternet();
	        	return;
	        }
	        else if(msg.what == CMHttpConn.TYPE_REQ_CALL_FOR_DELIVERY)
			{
	        	cancelWait();
				if( msg.obj != null && msg.obj instanceof CMHttpConn )
				{
					CMHttpConn conn = (CMHttpConn) msg.obj;
					if( conn.m_nWebOpenResult == CMHttpConn.XHTTP_200_OK )
					{
						XResultList resultList = conn.m_XmlParser.getResultList();
						if( resultList != null && resultList.size() > 0 )
						{
							String strResult = resultList.get(0).get("result");
							if(strResult != null && strResult.equals("true"))
							{
								String strShopPhone = m_infoMap.get("comp_tel");
								if(strShopPhone == null)
									return;
								Intent callIntent = new Intent(Intent.ACTION_CALL);
								callIntent.setData(Uri.parse("tel:" + strShopPhone));
								startActivity(callIntent);
								return;
							}
							else
							{
								String strError = resultList.get(0).get("errorMsg");
								if(strError != null && strError.length() > 0)
									showToast(strError);
								else
									toastRequestFail();
								return;
							}
						}
						
						toastRequestFail();
						return;
					}
				}

				toastCheckInternet();
				return;
			}
	        else if(msg.what == CMHttpConn.TYPE_REQ_QUERY_MAP_POS)
			{
	        	cancelWait();
				if( msg.obj != null && msg.obj instanceof CMHttpConn )
				{
					CMHttpConn conn = (CMHttpConn) msg.obj;
					if( conn.m_nWebOpenResult == CMHttpConn.XHTTP_200_OK )
					{
						XResultList resultList = conn.m_XmlParser.getResultList();
						if( resultList != null && resultList.size() > 0 )
						{
							XResultMap resultMap = resultList.get(0);
							if(resultMap != null && resultMap.size() > 0)
							{
								String strLat = resultMap.get("lat");
								String strLng = resultMap.get("lng");
								if(strLat != null && strLat.length() > 0 && strLng != null)
								{
    								m_infoMap.put("lat", strLat);
    								m_infoMap.put("lng", strLng);
    								
    								Intent intent = CMManager.getIntent(DeliveryDetailActivity.this, DeliveryMapActivity.class);
    								intent.putExtra("category_name", m_strCategoryName);
    								intent.putExtra("item_info", m_infoMap);
    								startActivity(intent);
    								return;
								}
							}
						}
						
						if(resultList != null)
						{
							String strError = resultList.getOtherValue("dmessage");
							if(strError != null)
							{
								showToast(strError);
								return;
							}
						}
						
						toastResultIsEmpty();
						return;
					}
				}

				toastCheckInternet();
				return;
			}
        }
	};

	@Override
    public void onClick( View v )
    {
		int nId = v.getId();
		if(nId == R.id.imageView_menu || nId == R.id.imageButton_show_menu)
		{
			if(m_infoMap == null)
				return;
			
			Intent intent = CMManager.getIntent(DeliveryDetailActivity.this, ShowMenuActivity.class);
			intent.putExtra("category_name", m_strCategoryName);
			intent.putExtra("item_info", m_infoMap);
			startActivity(intent);
		}
		else if(nId == R.id.imageButton_order_call)
		{
			if(m_infoMap == null)
				return;
			
			String strShopPhone = m_infoMap.get("comp_tel");
			if(strShopPhone == null || strShopPhone.length() == 0)
			{
				showAlert("전화번호정보가 없습니다.");
				return;
			}
			
			String strId, strName, strAddress, strMobile, strNo;
			strId = CMManager.getMyInfo("uid");
			strName = CMManager.getMyInfo("name");
			strMobile = CMManager.getMyInfo("hphone");
			strAddress = m_infoMap.get("address");
			strNo = m_infoMap.get("no");
			if(strId == null)
				return;
			if(strName == null)
				return;
			if(strMobile == null)
				return;
			if(strNo == null)
				return;
			if(strAddress == null)
				strAddress = "";
			
			strMobile = strMobile.replace("[^[0-9|]]", "");
			
			if(CMHttpConn.reqCallForDelivery(m_Handler, strId, strName, strAddress, strMobile, strNo) == null)
				toastCheckInternet();
			else
				showWait(null);
		}
		else if(nId == R.id.button_address)
		{
			if(m_infoMap == null)
				return;
			
//			Intent intent = CMManager.getIntent(DeliveryDetailActivity.this, DeliveryMapActivity.class);
//			intent.putExtra("category_name", m_strCategoryName);
//			intent.putExtra("item_info", m_infoMap);
//			startActivity(intent);

			String strAddress = m_infoMap.get("address");
			if(strAddress == null || strAddress.length() == 0)
				return;
			
			String strShopName = m_infoMap.get("comp_name");
			int nFound = strAddress.indexOf("||");
			if(nFound > 0)
			{
				String strSub1 = strAddress.substring(0, nFound);
				String strSub2 = strAddress.substring(nFound + 2);
				
				nFound = strSub2.indexOf("번지");
				if(nFound > 0)
					strSub2 = strSub2.substring(0, nFound);
				else
				{
					if(strShopName != null && strShopName.length() > 0)
					{
						nFound = strSub2.indexOf(strShopName);
						if(nFound > 0)
							strSub2 = strSub2.substring(0, nFound); 
					}
					
					nFound = -1;
					for(int nIndex=strSub2.length()-1; nIndex>=0; nIndex--)
					{
						char chCur = strSub2.charAt(nIndex);
						if(chCur >= '0' && chCur <= '9')
						{
							nFound = nIndex;
							break;
						}
					}
					if(nFound > 0)
						strSub2 = strSub2.substring(0, nFound + 1);
				}
				
				strAddress = strSub1 + " " + strSub2;
			}
			
			strAddress = strAddress.replace("||", " ");
			if(CMHttpConn.reqQueryMapPos(m_Handler, strAddress) == null)
				toastCheckInternet();
			else
				showWait(null);
		}
    }
	
	public void GetReplyData()
    {
    	{
			
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					String strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");

					String strCmdMode = "getDeliveryEpilogue";
					String strHashData = XUtilFunc.getMD5Hex( strCmdMode + "coupmarket");
					
					data.put("key", "coupmarket");
					data.put("cmd_mode", strCmdMode);
					data.put("type", "list");
					data.put("hashdata", strHashData);
					data.put("gno", m_Productumber);
					
					
					
					String strJSON = m_Cookie.GetHTTPData("http://www.coupmarket.com/interx/req_info.php", data );
					
					
					// XML 파서를 초기화
					XmlPullParserFactory xmlpf = null;

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
						xmlp.setInput(new StringReader(strJSON));
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
					String temp= "";
					
					ReviewData item = null;
					while( eventType != XmlPullParser.END_DOCUMENT )
		            {


		                switch ( eventType ) 
		                {
		                case XmlPullParser.START_DOCUMENT: // 문서 시작 태그를 만난 경우
		                case XmlPullParser.END_DOCUMENT: // 문서 끝 태그를 만난 경우
		                    break;
		                case XmlPullParser.START_TAG: // 쌍으로 구성된 태그의 시작을 만난 경우
		                    // 요소명 체크
		                    if ( xmlp.getName().equals("no") )
		                    { 
		                        estate = CURR_XML_STATE.CURR_XML_NO;
		                    }
		                    else if ( xmlp.getName().equals("uid")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_ID;

		                    }
		                    else if ( xmlp.getName().equals("gno")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_GNO;

		                    }
		                    else if ( xmlp.getName().equals("pno")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_PNO;

		                    }
		                    else if ( xmlp.getName().equals("content")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_CONTENT;

		                    }
		                    else if ( xmlp.getName().equals("star")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_STAR;

		                    }
		                    else if ( xmlp.getName().equals("sdate")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_DAY;

		                    }

		                    else if ( xmlp.getName().equals("star")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_RESULT;
		                    }
		                    
		                    else if ( xmlp.getName().equals("pnum")   )
		                    {
		                    	estate = CURR_XML_STATE.CURR_XML_UNKNOWN;
		                    }

		                case XmlPullParser.END_TAG: // 쌍으로 구성된 태그의 끝을 만난 경우
		                    break;
		                case XmlPullParser.TEXT: // TEXT로 이루어진 내용을 만난 경우
		                    switch ( estate )
		                    {
		                    case CURR_XML_NO:
		                    {
		                    	item = new ReviewData();
		                    	
		                    }
		                    	break;
		                    case CURR_XML_ID:
		                    {
		                    	item.Name = xmlp.getText();
		                    }
		                    	break;
		                    case CURR_XML_GNO:
		                    {
		                    }
		                    	break;
		                    case CURR_XML_PNO:
		                    {
		                    }
		                    	break;
		                    case CURR_XML_CONTENT:
		                    {
		                    	item.Contents =xmlp.getText();
		                    }
		                    	break;
		                    case CURR_XML_STAR:
		                    {
		                    	item.star = Integer.parseInt(xmlp.getText());
		                    	m_ReviewData.add(item);
		                    }
		                    	break;
		                    case CURR_XML_DAY:
		                    {
		                    	item.Day = xmlp.getText();
		                    }
		                    	break;


		                    case CURR_XML_RESULT:
		                    {
		                    	temp = xmlp.getText();
		                    	
		                    	if ( !temp.equals("true") )
		                    	{
		                    		handler.sendEmptyMessage(17);
		                    	}
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
		                
		            } //
					

					
					
					
					handler.sendEmptyMessage(0);
					

				}
			});
			thread.start();
		}
    }
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				//RefreshUI();
				
				// 리뷰 데이터 추가...
				for ( int i = 0 ; i <m_ReviewData.size() ; i++ )
				{
					if (i <3 )
					{
						DeliveryDetailReviewItem item = new DeliveryDetailReviewItem(self);
						item.SetData(m_ReviewData.get(i).Title, m_ReviewData.get(i).star, m_ReviewData.get(i).Name, m_ReviewData.get(i).Day, m_ReviewData.get(i).Contents);
						LinearLayout layout = ((LinearLayout)self.findViewById(R.id.detail_list));
						((LinearLayout)findViewById(R.id.detail_list)).addView(item);
					}

					
				}
				
				if ( m_ReviewData.size() > 0 )
				{
					DeliveryDetailReviewItemBtn item = new DeliveryDetailReviewItemBtn(self);
					
					OnClickListener ol  = new OnClickListener()
					{

						@Override
						public void onClick(View v) 
						{
							// 이벤트
							
							Intent intent = CMManager.getIntent(self, DeliveryDetailReViewActivity.class);
						    intent.putExtra("category_name", m_strCategoryName);
						    intent.putExtra("category_no", m_nCategoryNo);
						    intent.putExtra("item_info", m_infoMap);
							startActivity(intent);
							
							
						}
					
					};
					item.setOnClickListener (ol);
					
					((LinearLayout)findViewById(R.id.detail_list)).addView(item);
				}
				
				break;
			case 1:
				// 오류처리 
				//self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 2:

				break;
				
			case 3:
	
				break;
			case 20:
				break;
			default:
				break;
			}

		}
    	
	};
	
}

package com.hoteljoin;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NetPopup  extends Activity implements OnClickListener 
{
	Thread ConnectThread;
	boolean bFinish = false;
	int APINumber= -1;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.netpopup);

		
		
		{
			ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
			setResize( 0, root, true);
			
			APINumber =  getIntent().getIntExtra("API", -1);
			ConnectAPI( APINumber );
		}
	}
	

	public void onClick(View arg0) 
	{
	
	}
	
	
	public void ImageResize(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	
    	View box = (View)findViewById(id);
    	Log.v("Type", box.getClass().getName() );
    	_AppManager.GetUISizeConverter().ConvertFrameLayoutView(box);
    }
    public void ImageResize2(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View box = (View)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutView(box);
    }
    
    public void ImageResize3(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View box = (View)findViewById(id);
    	if ( box != null )
    		_AppManager.GetUISizeConverter().ConvertRelativeLayoutView(box);
    }
    
    public void TextResize(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertFrameLayoutTextView(box);
    }
    public void TextResize2(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutTextView(box);
    }
    
    public void TextResize3(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertRelativeLayoutTextView(box);
    }
	
	void setResize( int index , ViewGroup root , Boolean first )
    {
    	for (int i = 0; i < root.getChildCount(); i++) 
    	{
            View child = root.getChildAt(i);
            if ( first == false)
            {
            	if ( index == 0 )
                {
            		 if (child instanceof TextView)
            		 {
            			 TextResize3(child.getId());
            			 
            		 }
            		 else
            		 {
            			 ImageResize3(child.getId());
            		 }
                	
                }
                else if ( index == 1 )
                {
                	if (child instanceof TextView)
	           		{
                		TextResize2(child.getId());
                		
	           		}
	           		else
	           		{
	           			ImageResize2(child.getId());
	           		}

                }
                else if ( index == 2 )
                {
                	if (child instanceof TextView)
	           		{
                		TextResize(child.getId());
                		
	           		}
                	else if ( child instanceof CalendarView)
                	{
                		ImageResize(child.getId());
                	}
	           		else
	           		{
	           			ImageResize(child.getId());
	           		}
                }
            }
            
            if (child instanceof LinearLayout)
            {
            	setResize( 1 , (ViewGroup)child, false );
            }
                
            else if (child instanceof FrameLayout)
            {
            	if ( child instanceof CalendarView)
            	{
            		
            	}
            	else
            	{
            		setResize( 2 , (ViewGroup)child, false );
            	}
            	
            }
            else if (child instanceof RelativeLayout)
            {
            	setResize( 0 , (ViewGroup)child, false );
            } 
        }
    }
	private void ConnectAPI( int index )
	{
		
		if ( index == -1 )
			return;
		bFinish = false;
		final AppManagement _AppManager = (AppManagement) getApplication();
		_AppManager.ParseString = "";
		String URL = _AppManager.DEF_URL;
		switch ( index )
		{
//		0. 목적지목록조회
		case 0:
		{
			URL += "/mweb/search/searchDestinationList.gm";
		}
			break;
//			1. 목적지도시목록조회
		case 1:
			URL += "/mweb/search/searchDestinationCityList.gm";
			break;
//			2. 호텔코드목록조회
		case 2:
			URL += "/mweb/search/searchHotelCodeList.gm";
			break;
//			3. 호텔가격목록조회
		case 3:
			URL += "/mweb/search/searchHotelPriceList.gm";
			break;
//			4. 호텔상세정보조회
		case 4:
			URL += "/mweb/search/searchHotelDetail.gm";
			break; 
//			5. 객실가격목록조회
		case 5:
			URL += "/mweb/search/searchHotelDetailRoomList.gm";
			break;
//			6. 객실옵션목록조회
		case 6:
			URL += "/mweb/search/searchHotelDetailRoomOptionList.gm";
			break;
//			7. 내주변호텔목록조회
		case 7:
			URL += "/mweb/search/searchHotelNearbyList.gm";
			break;
//			8. 예약폼전문
		case 8:
		{
			if (_AppManager.m_SearchWorld)
			{
				URL += "/mweb/booking/bookingFormInt.gm";
			}
			else
			{
				URL += "/mweb/booking/bookingFormDom.gm";
			}
		}
			break;
			
//			9. 예약입력
		case 9:
		{
			if (_AppManager.m_SearchWorld)
			{
				URL += "/mweb/booking/bookingAddInt.gm";
			}
			else
			{
				URL += "/mweb/booking/bookingAddDom.gm";
			}
		}
			break;
//			10.결제화면
		case 10:
			URL += "/mweb/booking/payRequestForm.gm";
			break;
//			11.추가할인받기정보
		case 11:
			URL += "/mweb/booking/extraDiscountInfo.gm";
			break;
//			12.공지사항목록조회
		case 12:
			URL += "/mweb/board/noticeList.gm";
			break; 
//			13.공지사항조회
		case 13:
			URL += "/mweb/board/noticeDetail.gm";
			break;
//			14.문의목록조회
		case 14:
			URL += "/mweb/board/consultList.gm";
			break;
//			15.문의상세조회
		case 15:
			URL += "/mweb/board/consultDetail.gm";
			break;
//			16.문의등록		
		case 16:
			URL += "/mweb/board/consultAdd.gm";
			break;
//			17.이용후기목록조회
		case 17:
			URL += "/mweb/board/reviewList.gm";
			break; 
//			18.이용후기상세조회	
		case 18:
			URL += "/mweb/board/reviewDetail.gm";
			break;
//			19.이용후기댓글목록조회
		case 19:
			URL += "/mweb/board/reviewDetailReplies.gm";
			break;
//			20.이용후기추천
		case 20:
			URL += "/mweb/board/reviewAddRecommend.gm";
			break;
//			21.이용후기댓글등록			
		case 21:
			URL += "/mweb/board/reviewDetailAddReply.gm";
			break;
//			22.이용후기댓글삭제			
		case 22:
			URL += "/mweb/board/reviewDetailDeleteReply.gm";
			break;
//			23.여행일지목록조회
		case 23:
			URL += "/mweb/board/diaryList.gm";
			break;
////////////////////////////////////////
			
//			24.여행일지상세조회
		case 24:
			URL += "/mweb/board/diaryDetail.gm";
			break;
//			25.여행일지댓글목록조회
		case 25:
			URL += "/mweb/board/diaryReplyList.gm";
			break;
//			26.여행일지등록
		case 26:
			URL += "/mweb/board/diaryAdd.gm";
			break;
//			27.여행일지등록 국가코드목록
		case 27:
			URL += "/mweb/board/diaryNationCodeList.gm";
			break;
//			28.여행일지등록 도시코드목록
		case 28:
			URL += "/mweb/board/diaryCityCodeList.gm";
			break;
//			29.여행일지추천
		case 29:
			URL += "/mweb/board/diaryAddRecommend.gm";
			break;
//			30.여행일지수정
		case 30:
			URL += "/mweb/board/diaryUpdate.gm";
			break;
//			31.여행일지삭제
		case 31:
			URL += "/mweb/board/diaryDelete.gm";
			break;
//			32.여행일지댓글등록
		case 32:
			URL += "/mweb/board/diaryReplyAdd.gm";
			break;
//			33.여행일지댓글삭제
		case 33:
			URL += "/mweb/board/diaryReplyDelete.gm";
			break;
//			34.이벤트목록조회
		case 34:
			URL += "/mweb/event/eventList.gm";
			break;
//			35.메인이벤트이미지조회
		case 35:
			URL += "/mweb/event/eventMainImage.gm";
			break;
//			36.이벤트상세조회
		case 36:
			URL += "/mweb/event/eventDetail.gm";
			break;
//			37.이벤트베너목록조회
		case 37:
			URL += "/mweb/event/eventBannerList.gm";
			break;
//			38.로그인
		case 38:
			URL += "/mweb/member/login.gm";
			break;
//			39.회원가입
		case 39:
			URL += "/mweb/member/register.gm";
			break;
//			40.회원가입 아이디체크
		case 40:
			URL += "/mweb/member/validateId.gm";
			break;
//			41.예약현황목록조회
		case 41:
			URL += "/mweb/booking/bookingList.gm";
			break;
//			42.예약현황상세조회
		case 42:
			URL += "/mweb/booking/bookingDetail.gm";
			break;
//			43.예약취소요청
		case 43:
			URL += "/mweb/booking/bookingCancelRequest.gm";
			break;
//			44.해피쿠폰받기
		case 44:
			URL += "/mweb/member/happyCouponAccept.gm";
			break;
//			45.쿠폰등록
		case 45:
			URL += "/mweb/member/couponAccept.gm";
			break;
//			46.나의할인혜택정보조회
		case 46:
			URL += "/mweb/member/myBenefitsInfo.gm";
			break;
//			47.적립금내역조회
		case 47:
			URL += "/mweb/member/myMileageList.gm";
			break;
//			48.쿠폰내역조회
		case 48:
			URL += "/mweb/member/myCouponList.gm";
			break;
//			49.상품권내역조회
		case 49:
			URL += "/mweb/member/myGiftList.gm";
			break;
//			50.선호지역조회
		case 50:
			URL += "/mweb/member/myPreferCity.gm";
			break;
//			51.선호지역등록
		case 51:
			URL += "/mweb/member/myPreferCityAdd.gm";
			break;
//			52.선호지역삭제
		case 52:
			URL += "/mweb/member/myPreferCityDel.gm";
			break;
//			53.선호호텔조회
		case 53:
			URL += "/mweb/member/myPreferHotel.gm";
			break;
//			54.선호호텔등록
		case 54:
			URL += "/mweb/member/myPreferHotelAdd.gm";
			break;
//			55.선호호텔삭제
		case 55:
			URL += "/mweb/member/myPreferHotelDel.gm";
			break;
//			56.찜 BOX 목록 조회
		case 56:
			URL += "/mweb/bookmark/bookmarkList.gm";
			break;
//			57.찜 BOX 등록
		case 57:
			URL += "/mweb/bookmark/bookmarkAdd.gm";
			break;
//			58.찜 BOX 삭제
		case 58:
			URL += "/mweb/bookmark/bookmarkDelete.gm";
			break;
			
		
		}

		final String finalURL = URL;
		ConnectThread = new Thread(new Runnable()
		{

			public void run() 
			{
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(finalURL, _AppManager.ParamData);
				_AppManager.ParseString = strJSON;
				handler.sendEmptyMessage(0);
			}
		});
		ConnectThread.start();
		


	}
	
	@Override
	public void onBackPressed() 
	{
		// 스레드가 살아 있음에도 종료를 하고자 할때 들어옴. 
		if ( bFinish == false )
		{
			bFinish = true;
	    	this.setResult(-1);
	    	finish();			
		}

	}
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{

			switch(msg.what)
			{
				case 0:
				{
					// 무슨 메세지를 받았던간에 화면을 종료 시키고 다음 화면으로 이동 시킨다. 
					callFinish();
				}
				break;
			}
		}
	};
    	
    private void callFinish()
    {
    	// 스레드가 살아있는데 여기 들어왔다는건 전송 도중에 back 키를 눌렀다는 것 
    	// 팝업이 종료되어도 스레드는 죽지 않기에, 메세지를 보내지 않도록 해야 한다. 
    	if ( bFinish == false )
		{
    		bFinish = true;
        	Intent i =  new Intent();
        	i.putExtra("return", APINumber);
        	setResult(10, i);
        	
        	finish();
		}


    	
    }
//	void setGlobalFont(ViewGroup root) {
//        for (int i = 0; i < root.getChildCount(); i++) {
//            View child = root.getChildAt(i);
//            if (child instanceof TextView)
//            {
//                ((TextView)child).setTypeface(mTypeface);
//                ((TextView)child).setTypeface(mTypefaceBold, Typeface.BOLD);
//            }
//           /* else if  (child instanceof EditText) 
//            {
//            	((EditText)child).setTypeface(mTypeface);
//                ((EditText)child).setTypeface(mTypefaceBold, Typeface.BOLD);
//            }*/
//            else if (child instanceof ViewGroup)
//                setGlobalFont((ViewGroup)child);
//            
//            
//            
//        }
//    }
	

}

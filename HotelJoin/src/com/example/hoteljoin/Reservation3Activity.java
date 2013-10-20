package com.example.hoteljoin;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.example.hoteljoin.HotelSearchListActivity.SearchList_Adapter;
import com.slidingmenu.lib.SlidingMenu;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Reservation3Activity extends HotelJoinBaseActivity implements OnClickListener{
	Reservation3Activity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 


	 Integer day;
	 Integer month;
	 Integer year;
	 Calendar calendar = Calendar.getInstance();
	 
	 Boolean checkbox1 = false;
	 Boolean checkbox2 = false;
	 Boolean checkbox3 = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rese_korea_main);
		
		

		// 비하인드 뷰 설정
		setBehindContentView(R.layout.main_menu_1);
		
		self = this;
		

	    // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
		
		// 슬라이딩 뷰
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.RIGHT);
		
		{
			Display defaultDisplay = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    	
			int width;
			@SuppressWarnings("deprecation")
			int windowswidth = defaultDisplay.getWidth();
			@SuppressWarnings("deprecation")
			int windowsheight= defaultDisplay.getHeight();
			int originwidth = 480;
			

			width = (int) ( (float) 340 * ( (float)(windowswidth)/ (float)(originwidth) )  );

			
			menu.setBehindOffset(windowswidth - width );
		}
		
		menu.setFadeDegree(0.35f);
		
		GetInputData();
		
		AfterCreate(7);
		
		{
			{
				AppManagement _AppManager = (AppManagement) getApplication();
				DateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
				 Date tempDate = null;
				try {
					tempDate = sdFormat.parse(_AppManager.m_CheckInDay);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				calendar.setTime(tempDate);
				
				year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH) + 1;
				day = calendar.get(Calendar.DAY_OF_MONTH);
				StringBuffer sbDate=new StringBuffer ( );
				sbDate.append ( year );
				sbDate.append ("/");
				if ( month < 10 ) sbDate.append ( "0" );
				sbDate.append ( month );
				sbDate.append ("/");
				if ( day < 10 ) sbDate.append ( "0" );
				sbDate.append ( day );
				
				
				
				
				((TextView)findViewById(R.id.roomday)).setText(sbDate + "~" + getDate(Integer.parseInt(_AppManager.m_Duration)) );

				
			}
			
			AppManagement _AppManager = (AppManagement) getApplication();
			((TextView)findViewById(R.id.roomname)).setText(_AppManager.m_RoomDetailData.roomName);
			((TextView)findViewById(R.id.roomcount)).setText(_AppManager.m_NumRoom );
			
			if ( _AppManager.m_NumChild.equals("0") )
			{
				((TextView)findViewById(R.id.roomman)).setText(_AppManager.m_NumPer + "[성인]");
			}
			else
			{
				((TextView)findViewById(R.id.roomman)).setText(_AppManager.m_NumPer + "/" +_AppManager.m_NumChild + "[성인/어린이]");
			}
			
			
			String tempString = "";
			for ( int i = 0 ; i < _AppManager.m_RoomOptionData.size() ; i++ )
			{
				if( _AppManager.m_RoomOptionData.get(i).optionMethodType.equals("Y"))
				{
					tempString += _AppManager.m_RoomOptionData.get(i).optionName;
					tempString += "  ";
				}
			}
			
			((TextView)findViewById(R.id.roomtype)).setText(tempString);
			tempString = "";
			for ( int i = 0 ; i < _AppManager.m_RoomOptionData.size() ; i++ )
			{
				if( _AppManager.m_RoomOptionData.get(i).optionMethodType.equals("S"))
				{
					tempString += _AppManager.m_RoomOptionData.get(i).optionName;
					tempString += "  ";
				}
			}
			
			((TextView)findViewById(R.id.roomreop)).setText(tempString);
			tempString = "";
			
			tempString = "";
			for ( int i = 0 ; i < _AppManager.m_RoomOptionData.size() ; i++ )
			{
				if( _AppManager.m_RoomOptionData.get(i).optionMethodType.equals("N"))
				{
					tempString += _AppManager.m_RoomOptionData.get(i).optionName;
					tempString += "  ";
				}
			}
			
			((TextView)findViewById(R.id.roomseop)).setText(tempString);
			
			
		}
		
		BtnEvent(R.id.login_btn);
		BtnEvent(R.id.reservation_btn);
		BtnEvent(R.id.detail_btn_2);
		BtnEvent(R.id.detail_btn_1);
		BtnEvent(R.id.checkbox_3);
		BtnEvent(R.id.checkbox_2);
		BtnEvent(R.id.checkbox_1);
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			if ( _AppManager.m_LoginCheck == true)
			{
				
				((EditText)findViewById(R.id.name1)).setText(_AppManager.m_Name);
				((EditText)findViewById(R.id.tel2)).setText(_AppManager.m_Mobile);
				((EditText)findViewById(R.id.mail)).setText(_AppManager.m_Email);
			}
			
			{
				DecimalFormat df = new DecimalFormat("#,##0");

				((TextView)findViewById(R.id.main_row_9_day)).setText("￦ " + df.format(Double.parseDouble(_AppManager.m_TotalPrice)) );
			}

		}
		

		

		

	}
	
	public String getDate ( int iDay )
	{
		calendar.set(year, month -1, day);
		StringBuffer sbDate=new StringBuffer ( );
		calendar.add ( Calendar.DAY_OF_MONTH, iDay );
		int nYear = calendar.get ( Calendar.YEAR );
		int nMonth = calendar.get ( Calendar.MONTH ) + 1;
		int nDay = calendar.get ( Calendar.DAY_OF_MONTH );
		sbDate.append ( nYear );
		sbDate.append ("/");
		if ( nMonth < 10 ) sbDate.append ( "0" );
		sbDate.append ( nMonth );
		sbDate.append ("/");
		if ( nDay < 10 ) sbDate.append ( "0" );
		sbDate.append ( nDay );

		return sbDate.toString ( );
	}
	
	

	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	public void BtnEvent( int id )
    {
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
    }

	
	public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	public void ImageBtnResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	
	public void GetInputData()
	{

		mProgress.show();
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				Map<String, String> data = new HashMap  <String, String>();

				data.put("hotelCode", _AppManager.m_HotelCode);
				data.put("supplyCode", "700");
				
				data.put("checkinDay", _AppManager.m_CheckInDay);
				data.put("duration", _AppManager.m_Duration);
				data.put("roomCount1", _AppManager.m_NumRoom);
				data.put("numAdults1", _AppManager.m_NumPer);
				
				data.put("numChild", _AppManager.m_NumChild);
				
				data.put("roomCode", _AppManager.m_RoomDetailData.roomCode);
				
				
				for ( Integer i = 0 ; i < _AppManager.m_RoomOptionData.size(); i++ )
				{
					data.put("optionCode"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionCode);
					data.put("optionName"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionName);
					data.put("optionSendPrice"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionSendPrice);
					data.put("optionPrice"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionPrice);
					data.put("optionPriceType"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionPriceType);
					data.put("optionMethodType"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionMethodType);
					
				}
				

				String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mweb/booking/bookingFormDom.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						
						
						_AppManager.m_RoomReserData.roomName = 	json.getString("roomName");
						_AppManager.m_RoomReserData.roomCode = 	json.getString("roomCode");
						_AppManager.m_RoomReserData.roomPrice = 	json.getString("roomPrice");
						_AppManager.m_RoomReserData.totalPrice = 	json.getString("totalPrice");
						_AppManager.m_RoomReserData.numRooms = 	json.getString("numRooms");
						_AppManager.m_RoomReserData.lguCstMid = 	json.getString("lguCstMid");
						_AppManager.m_RoomReserData.nonRefundableYn = 	json.getString("nonRefundableYn");
						//_AppManager.m_RoomReserData.priceFormula = 	json.getString("priceFormula");
						//_AppManager.m_RoomReserData.currencyCode = 	json.getString("currencyCode");
						if( _AppManager.m_SearchWorld == true )
						{
							_AppManager.m_RoomReserData.nativeRoomPrice = 	json.getString("nativeRoomPrice");
							_AppManager.m_RoomReserData.nativeCurrencyCode = 	json.getString("nativeCurrencyCode");
							_AppManager.m_RoomReserData.promoYn = 	json.getString("promoYn");
							_AppManager.m_RoomReserData.promoCode = 	json.getString("promoCode");
							_AppManager.m_RoomReserData.promoDesc = 	json.getString("promoDesc");
							_AppManager.m_RoomReserData.breakfastYn = 	json.getString("breakfastYn");
							_AppManager.m_RoomReserData.bookRequestKey = 	json.getString("bookRequestKey");
							_AppManager.m_RoomReserData.nonResvInfoUpdateYn = 	json.getString("nonResvInfoUpdateYn");
						}



						
						{
							_AppManager.m_RoomCancel.clear();
							JSONArray usageList = (JSONArray)json.get("chargeConditions");
							for(int i = 0; i < usageList.length(); i++)
							{
								CancelPolicyList item = new CancelPolicyList();
								JSONObject list = (JSONObject)usageList.get(i);
								
								if ( list.optString("chargeYn").equals("1") )
								{
									item.cancelDay = list.optString("fromDate");
									item.cancelPrice = list.optString("currencyCode");
									item.cancelPrice = list.optString("chargeAmount");
									_AppManager.m_RoomCancel.add(item);
								}
								
							}
						}
						
						
			
						

						handler.sendEmptyMessage(0);
					}
					else 
					{
						// 에러 메세지를 전송한다. 
						handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.getString("errorMsg")) ));
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler.sendMessage(handler.obtainMessage(1,"json Data Error \n" + e.getMessage() ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
			{
				RefreshUI();

			}
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				break;
			default:
				break;
			}

		}
    	
	};	
	
	

	
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		AppManagement _AppManager = (AppManagement) getApplication();
		switch(v.getId())
		{
		case R.id.login_btn:
		{
			if ( _AppManager.m_LoginCheck == true)
			{
				self.ShowAlertDialLog( self ,"에러" , "로그인 되어 있습니다.");
			}
			else
			{
				Intent intent;
	            intent = new Intent().setClass(baseself, LoginActivity.class);
	            startActivity( intent );
			}
		}
			break;
		case R.id.detail_btn_2:
		{
			{
				_AppManager.m_URL = "http://m.hoteljoin.com/mweb/noMemberPolicy.html";
				_AppManager.m_URLTitle = "개인정보 취급방침";
				Intent intent;
	            intent = new Intent().setClass(baseself, WebActivity.class);
	            startActivity( intent ); 
			}
			
		}
			break;
		case R.id.detail_btn_1:
		{
			
			{
				String temp = "";
				// 취소 규정 
				
				/*_AppManager.m_URL = "http://m.hoteljoin.com/mweb/noMemberPolicy.html";
				_AppManager.m_URLTitle = "개인정보 취급방침";
				Intent intent;
	            intent = new Intent().setClass(baseself, WebActivity.class);
	            startActivity( intent );*/ 
				
				for ( int i = 0 ; i < _AppManager.m_RoomCancel.size() ; i++ )
				{
					temp += _AppManager.m_RoomCancel.get(i).cancelDay;
					temp += " ";
					temp +=  _AppManager.m_RoomCancel.get(i).cancelPrice;
					temp += "% 의 위약금이 발생";
					temp += "\n";
				}
				self.ShowAlertDialLog( self ,"취소 규정" , temp );
			}
		}
			break;
		case R.id.checkbox_3:
		{
			checkbox3 = !checkbox3;
			if ( checkbox3 == true )
			{
				
				((ImageView)findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.checkbox_2);
			}
			else
			{
				
				((ImageView)findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.checkbox_1);
			}
		}
			break;
		case R.id.checkbox_2:
		{
			checkbox2 = !checkbox2;
			if ( checkbox2 == true )
			{
				
				((ImageView)findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.checkbox_2);
			}
			else
			{
				
				((ImageView)findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.checkbox_1);
			}
		}
			break;
		case R.id.checkbox_1:
		{
			checkbox1 = !checkbox1;
			if ( checkbox1 == true )
			{
				((EditText)findViewById(R.id.name2)).setText(((EditText)findViewById(R.id.name1)).getText().toString());
				((ImageView)findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.checkbox_2);
			}
			else
			{
				((EditText)findViewById(R.id.name2)).setText("");
				((ImageView)findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.checkbox_1);
			}
			
			
		}
			break;
		case R.id.reservation_btn:
		{
			EditText name1 = (EditText)findViewById(R.id.name1);
			EditText tel1 = (EditText)findViewById(R.id.tel1);
			EditText tel2 = (EditText)findViewById(R.id.tel2);
			EditText mail = (EditText)findViewById(R.id.mail);
			EditText write_text = (EditText)findViewById(R.id.write_text);
			EditText name2 = (EditText)findViewById(R.id.name2);
			
			
			if (name1.getText().toString().equals(""))
			{
				self.ShowAlertDialLog( self ,"에러" , "이름을 입력해주세요");
				return;
				
			}
			else if (tel1.getText().toString().equals("") && tel2.getText().toString().equals(""))
			{
				self.ShowAlertDialLog( self ,"에러" , "연락처나 핸드폰번호를 입력해주세요");
				return;
				
			}

			
			else if (!mail.getText().toString().equals(mail.getText().toString()))
			{
				self.ShowAlertDialLog( self ,"에러" , "메일주소를 입력해주세요");
				return;
				
			}
			
			else if (!isEmailPattern(mail.getText().toString()) )
			{
				self.ShowAlertDialLog( self ,"에러" , "올바르지 않은 메일주소입니다. ");
				return;
			}
			else if (name2.getText().toString().equals(""))
			{
				self.ShowAlertDialLog( self ,"에러" , "투숙자명을 입력해주세요");
				return;
				
			}

			/*else if ( checkbox1== false )
			{
				self.ShowAlertDialLog( self ,"에러" , "약관에 동의해주세요");
				return;
			}*/
			else if ( checkbox2== false )
			{

				self.ShowAlertDialLog( self ,"에러" , "약관에 동의해주세요");
				return;
			}
			
			else if ( checkbox3== false )
			{
				self.ShowAlertDialLog( self ,"에러" , "약관에 동의해주세요");
				return;
			}
			
			
			_AppManager.m_lodgeName = name2.getText().toString();
			_AppManager.m_resvName = name1.getText().toString();
			_AppManager.m_resvEmail = mail.getText().toString();
			_AppManager.m_resvTel = tel1.getText().toString();
			_AppManager.m_resvMobile = tel2.getText().toString();
			_AppManager.m_resvPasswd = "";
			_AppManager.m_resvIyagi = write_text.getText().toString();
			
			Intent intent;
            intent = new Intent().setClass(baseself, ReservationConfirmActivity.class);
            startActivity( intent );
			

		}
			break;

		}
		

		
	}

    

}

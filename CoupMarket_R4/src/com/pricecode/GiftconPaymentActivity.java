package com.pricecode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;

public class GiftconPaymentActivity extends CMActivity implements OnClickListener
{
	TextView m_tvGiftconName, m_tvGiftconExplain, m_tvCoopName, m_tvExpireDate, m_tvTimeLimit;
	
	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_giftcon_payment);
	    
	    setTitleBarText("구매");
	    
	    m_tvGiftconName = (TextView) findViewById(R.id.textView_product_name);
	    m_tvGiftconExplain = (TextView) findViewById(R.id.textView_product_explain);
	    m_tvCoopName = (TextView) findViewById(R.id.textView_coop_name);
	    m_tvExpireDate = (TextView) findViewById(R.id.textView_expiry_date);
	    m_tvTimeLimit = (TextView) findViewById(R.id.textView_time_limit);
	    
	    findViewById(R.id.imageButton_payment).setOnClickListener(this);
	    
	    updateGiftconInfo();
    }

	private void updateGiftconInfo()
    {
		@SuppressWarnings("unchecked")
        HashMap<String, String> infoMap = (HashMap<String, String>) getIntent().getSerializableExtra("item_info");
	    if(infoMap == null)
	    {
	    	showToast("상품정보가 없습니다.");
	    	return;
	    }
	    
		String strCoopName = null, strProductName = null, strExpireDate = null, strTimeLimit = null;
		strCoopName = infoMap.get("comp_name");
		strProductName = infoMap.get("coupon_name");
		strExpireDate = infoMap.get("use_term");

		if( strCoopName == null )
			strCoopName = "";
		if( strProductName == null )
			strProductName = "";
		if( strExpireDate == null )
			strExpireDate = "";

		m_tvGiftconName.setText("<" + strProductName + ">");
		m_tvCoopName.setText(strCoopName);
		m_tvExpireDate.setText("구입일로부터 " + strExpireDate + "일까지");
		
		try
		{
    		int nExpireDate = Integer.parseInt(strExpireDate);
    		if(nExpireDate > 0)
    		{
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, nExpireDate);
                Date limitDate = cal.getTime();
                SimpleDateFormat sdfLimit = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                strTimeLimit = sdfLimit.format(limitDate);
        		m_tvTimeLimit.setText(strTimeLimit);
    		}
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
		
		String strText, strExplain = "";
		strText = infoMap.get("use_area");
		if(strText != null && strText.length() > 0)
			strExplain += strText + "\n\n";
		strText = infoMap.get("use_limit");
		if(strText != null && strText.length() > 0)
			strExplain += strText + "\n\n";
		strText = infoMap.get("use_note");
		if(strText != null && strText.length() > 0)
			strExplain += strText + "\n\n";
		m_tvGiftconExplain.setText(strExplain);
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
				updateGiftconInfo();
				return;
			}
			else if(msg.what == XHandler.RESULT_TIME_OUT)
	        {
	        	cancelWait();
	        	toastCheckInternet();
	        	return;
	        }
	        else if(msg.what == CMHttpConn.TYPE_REQ_BUY_GIFTCON)
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
								showAlertAndFinish("구매가 완료되었습니다.");
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
        }
	};

	@Override
    public void onClick( View v )
    {
	    if(v.getId() == R.id.imageButton_payment)
	    {
	    	@SuppressWarnings("unchecked")
	        HashMap<String, String> infoMap = (HashMap<String, String>) getIntent().getSerializableExtra("item_info");
		    if(infoMap == null)
		    {
		    	showToast("상품정보가 없습니다.");
		    	return;
		    }
		    
		    String strId = null, strPassword = null, strMobile = null, strDBNo = null, strGiftconCode = null;
			strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
			strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");
			strMobile = CMManager.getMyInfo("hphone");
			strDBNo = infoMap.get("no");
			strGiftconCode = infoMap.get("coupon_code");
			
			if( strId == null || strId.length() == 0 || strPassword == null || strPassword.length() == 0 )
			{
				showAlert("로그인정보가 없습니다.");
				return;
			}
			if( strMobile == null || strMobile.length() == 0 )
			{
				showAlert("휴대폰 연락처 정보가 없습니다.");
				return;
			}
			if( strDBNo == null || strDBNo.length() == 0 )
			{
				showAlert("기프티콘의 인덱스 정보가 없습니다.");
				return;
			}
			if( strGiftconCode == null || strGiftconCode.length() == 0 )
			{
				showAlert("기프티콘의 코드 정보가 없습니다.");
				return;
			}
		    
	    	if(CMHttpConn.reqBuyGiftcon(m_Handler, strId, strPassword, strMobile, strDBNo, strGiftconCode) == null)
	    		toastCheckInternet();
	    	else
	    		showWait(null);
	    }
    }
}

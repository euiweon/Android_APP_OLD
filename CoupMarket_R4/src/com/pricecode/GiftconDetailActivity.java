package com.pricecode;

import java.io.File;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pricecode.*;
import com.rangboq.xutil.XFileDownloader;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;

public class GiftconDetailActivity extends CMActivity implements OnClickListener
{
	boolean m_bIsResendUI = false;
	
	LinearLayout m_layoutBuy, m_layoutResend;
	TextView m_tvGiftconName1, m_tvGiftconName2, m_tvCoopName, m_tvPrice;
	TextView m_tvBuyDate, m_tvEndDate, m_tvStatus;
	ImageView m_ivPhoto;
	
	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_giftcon_detail);
	    
	    setTitleBarText("기프트콘");
	    
	    m_layoutBuy = (LinearLayout) findViewById(R.id.linearLayout_buy);
	    m_layoutResend = (LinearLayout) findViewById(R.id.linearLayout_resend);
	    m_ivPhoto = (ImageView) findViewById(R.id.imageView_photo);
	    m_tvGiftconName1 = (TextView) findViewById(R.id.textView_giftcon_name_1);
	    m_tvCoopName = (TextView) findViewById(R.id.textView_coop_name);
	    m_tvGiftconName2 = (TextView) findViewById(R.id.textView_giftcon_name_2);
	    m_tvPrice = (TextView) findViewById(R.id.textView_price);
	    m_tvBuyDate = (TextView) findViewById(R.id.textView_buy_date);
	    m_tvEndDate = (TextView) findViewById(R.id.textView_use_date);
	    m_tvStatus = (TextView) findViewById(R.id.textView_status);

	    findViewById(R.id.imageButton_buy).setOnClickListener(this);
	    findViewById(R.id.imageButton_resend).setOnClickListener(this);
	    
	    Intent curIntent = getIntent();
	    if(curIntent != null)
	    	m_bIsResendUI = curIntent.getBooleanExtra("isResendUI", false);
	    
	    if(m_bIsResendUI)
	    	m_layoutBuy.setVisibility(View.GONE);
	    else
	    	m_layoutResend.setVisibility(View.GONE);
	    
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
	    
		String strCoopName = null, strProductName = null, strRealPrice = null;
		if(m_bIsResendUI)
			strCoopName = infoMap.get("coupon_comp");
		else
			strCoopName = infoMap.get("comp_name");
		strProductName = infoMap.get("coupon_name");
		strRealPrice = infoMap.get("real_sel_price");

		if( strCoopName == null )
			strCoopName = "";
		if( strProductName == null )
			strProductName = "";
		if( strRealPrice == null )
			strRealPrice = "";

		m_tvCoopName.setText(strCoopName);
		m_tvGiftconName1.setText("<" + strProductName + ">");
		m_tvGiftconName2.setText(strProductName);
		m_tvPrice.setText(strRealPrice + "원");

		Bitmap photoBitmap = null;
		String strDetailUrl = infoMap.get("detail_img");
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
		
		if(m_bIsResendUI)
		{
			String strBuyDate = null, strEndDate = null, strStatus = null;
			strBuyDate = infoMap.get("reg_date");
			strEndDate = infoMap.get("use_end");
			strStatus = infoMap.get("coupon_status");

			if( strBuyDate == null )
				strBuyDate = "";
			else
			{
				int nFound = strBuyDate.indexOf(" ");
				if(nFound > 0)
					strBuyDate = strBuyDate.substring(0, nFound);
			}
			if( strEndDate == null )
				strEndDate = "";
			if( strStatus == null )
				strStatus = "";
			
			m_tvBuyDate.setText(strBuyDate);
            m_tvEndDate.setText(strEndDate);
			if(strStatus.equals("Y"))
				m_tvStatus.setText("사용");
			else if(strStatus.equals("N"))
				m_tvStatus.setText("미사용");
			else if(strStatus.equals("C"))
				m_tvStatus.setText("취소");
			else
				m_tvStatus.setText("?");
		}
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
	        else if(msg.what == CMHttpConn.TYPE_REQ_RESEND_GIFTCON)
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
								showAlert("재전송 요청이 완료되었습니다.");
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
	    if(v.getId() == R.id.imageButton_buy)
	    {
	    	@SuppressWarnings("unchecked")
	        HashMap<String, String> infoMap = (HashMap<String, String>) getIntent().getSerializableExtra("item_info");
		    if(infoMap == null)
		    {
		    	showToast("상품정보가 없습니다.");
		    	return;
		    }
		    
			Intent intent = CMManager.getIntent(GiftconDetailActivity.this, GiftconPaymentActivity.class);
			intent.putExtra("item_info", infoMap);
			startActivity(intent);
	    }
	    else if(v.getId() == R.id.imageButton_resend)
	    {
	    	@SuppressWarnings("unchecked")
	        HashMap<String, String> infoMap = (HashMap<String, String>) getIntent().getSerializableExtra("item_info");
		    if(infoMap == null)
		    {
		    	showToast("상품정보가 없습니다.");
		    	return;
		    }
		    
		    String strId = null, strPassword = null, strMobile = null, strDBNo = null, strGiftconCode = null, strGiftconNumber = null;
			strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
			strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");
			strMobile = CMManager.getMyInfo("hphone");
			strDBNo = infoMap.get("no");
			strGiftconCode = infoMap.get("coupon_code");
			strGiftconNumber = infoMap.get("coupon_number");
			
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
			if( strGiftconNumber == null || strGiftconNumber.length() == 0 )
			{
				showAlert("기프티콘의 번호 정보가 없습니다.");
				return;
			}
		    
	    	if(CMHttpConn.reqResendGiftcon(m_Handler, strId, strPassword, strMobile, strDBNo, strGiftconCode, strGiftconNumber) == null)
	    		toastCheckInternet();
	    	else
	    		showWait(null);
	    }
   }
}

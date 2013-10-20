package com.pricecode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XUtilFunc;

public class ShowBenefitActivity extends CMActivity
{
	WebView m_WebView;
	String m_strUrl = "http://www.coupmarket.com/interx/image_viewer.php";
	String m_strParam = "";


	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_show_full_image);
	    
	    setTitleBarText("혜택보기");

		m_WebView = (WebView) findViewById(R.id.webView_body);

		String strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
		String strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");
		if( strId.length() == 0 || strPassword.length() == 0 )
		{
			showAlert("로그인 정보가 없습니다.");
			return;
		}

		if(CMHttpConn.reqBenefitInfo(m_Handler, strId, strPassword) == null)
			toastCheckInternet();
		else
			showWait(null);
    }

	@Override
    protected void onDestroy()
    {
	    m_Handler = null;
	    super.onDestroy();
    }
	
	XHandler m_Handler = new XHandler()
	{
		@Override
        public void handleMessage( Message msg )
        {
			removeTimeoutMsg();
			if(m_Handler == null)
				return;
			
	        if(msg.what == XHandler.RESULT_TIME_OUT)
	        {
	        	cancelWait();
	        	toastCheckInternet();
	        	return;
	        }
	        else if(msg.what == CMHttpConn.TYPE_REQ_BENEFIT_INFO)
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
								String strLink = resultList.get(0).get("benefit_img");
								openWebView(strLink);
								return;
							}
							else
							{
								String strError = resultList.get(0).get("errorMsg");
								if(strError != null && strError.length() > 0)
									showToast(strError);
								else
									toastRequestFail();
							}
							return;
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

	String m_strLink = "";
	private void openWebView(String strLink)
	{
		setWebViewProperty(m_WebView);

		if( CMManager.enableHttps == false )
			m_strUrl = m_strUrl.replace("https://", "http://");

		try
		{
			String strCmdMode = "openImageViewer";
			String strHashData = XUtilFunc.getMD5Hex(strCmdMode + "coupmarket");

			if(strLink != null && strLink.length() > 0)
				m_strLink = strLink;
			m_strParam = String.format("cmd_mode=%s&img_url=%s&hashdata=%s", 
			                           strCmdMode, m_strLink, strHashData);

			m_WebView.clearCache(true);
			m_WebView.clearHistory();
			m_WebView.clearFormData();
			m_WebView.postUrl(m_strUrl, m_strParam.getBytes("UTF-8"));
		}
		catch( UnsupportedEncodingException e )
		{
			e.printStackTrace();
			finish();
		}
		catch( NullPointerException e )
		{
			e.printStackTrace();
			finish();
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setWebViewProperty( WebView webView )
	{
		webView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
		                                                      LayoutParams.FILL_PARENT));
		webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);

		WebSettings set = webView.getSettings();
		set.setJavaScriptEnabled(true);
		set.setJavaScriptCanOpenWindowsAutomatically(true);
		set.setPluginsEnabled(true);
		set.setPluginState(PluginState.ON);
		set.setSaveFormData(true);
		set.setUseWideViewPort(true);
		set.setSupportZoom(true);
		set.setBuiltInZoomControls(true);
		set.setSaveFormData(true);
		set.setAllowFileAccess(true);
		set.setCacheMode(WebSettings.LOAD_DEFAULT);
		set.setLoadsImagesAutomatically(true);
		set.setUserAgentString("coupMarket");
		set.setAppCacheEnabled(true);
		set.setSupportMultipleWindows(true);
		set.setLoadWithOverviewMode(true);

		webView.setWebViewClient(new ThisWebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
	}


	private class ThisWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading( WebView view, String url )
		{
			String strDecodedUrl = url;
			
			try
			{
				strDecodedUrl = URLDecoder.decode(url, "utf-8");
			}
			catch( UnsupportedEncodingException e )
			{
				e.printStackTrace();
			}

			if( strDecodedUrl.startsWith("tel:") )
			{
				try
				{
					Uri uri = Uri.parse(strDecodedUrl);
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
					return true;
				}
				catch( ActivityNotFoundException e )
				{
					e.printStackTrace();
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}

			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted( WebView view, String url, Bitmap favicon )
		{
			super.onPageStarted(view, url, favicon);
			showWait(null);
		}

		@Override
		public void onPageFinished( WebView view, String url )
		{
			super.onPageFinished(view, url);
			cancelWait();
		}
	}
}

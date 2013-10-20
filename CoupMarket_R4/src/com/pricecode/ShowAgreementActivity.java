package com.pricecode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.pricecode.*;
import com.rangboq.xutil.XUtilFunc;

public class ShowAgreementActivity extends CMActivity
{
	WebView m_WebView;
	String m_strUrl = "http://www.coupmarket.com/interx/privacy_useterms.php";
	String m_strParam = "";

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_agreement);
		
		setTitleBarText("개인정보보호정책 및 이용약관");

		m_WebView = (WebView) findViewById(R.id.webView_body);

		openWebView();
	}

	private void openWebView()
	{
		setWebViewProperty(m_WebView);

		if( CMManager.enableHttps == false )
			m_strUrl = m_strUrl.replace("https://", "http://");

		try
		{
			String strCmdMode = "getUseTerm";
			String strHashData = XUtilFunc.getMD5Hex(strCmdMode + "coupmarket");

			m_strParam = String.format("cmd_mode=%s&hashdata=%s", strCmdMode, strHashData);

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

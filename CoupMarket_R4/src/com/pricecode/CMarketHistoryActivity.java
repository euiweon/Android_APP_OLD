package com.pricecode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebView.WebViewTransport;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.pricecode.*;
import com.rangboq.xutil.XUtilFunc;

public class CMarketHistoryActivity extends CMActivity
{
	WebView m_WebView;
	String m_strUrl = "https://coupmarket.com/m/goods/pre_index.php";
	String m_strParam = "";

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cmarket_history);
		
		setTitleBarText("C ¸¶ÄÏ");

		m_WebView = (WebView) findViewById(R.id.webView_body);

		openWebView();
	}
	
	@Override
    protected void onDestroy()
    {
		m_WebView = null;
	    super.onDestroy();
    }

	@Override
    public void refreshView()
    {
	    super.refreshView();
	    openWebView();
    }

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )
	{
		if( (keyCode == KeyEvent.KEYCODE_BACK) )
		{
			if( m_WebView != null && m_WebView.canGoBack() == true )
			{
				m_WebView.goBack();
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}


	private void openWebView()
	{
		setWebViewProperty(m_WebView);

		if( CMManager.enableHttps == false )
			m_strUrl = m_strUrl.replace("https://", "http://");

		try
		{
			String strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
			String strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");

			String strCmdMode = "goToCmarketBuyList";
			String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + "coupmarket");

			m_strParam = String.format("cmd_mode=%s&access_device=android&uid=%s&passwd=%s&hashdata=%s", 
			                           strCmdMode, strId, strPassword, strHashData);

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
		webView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		                                                      LayoutParams.MATCH_PARENT));
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
		set.setAllowFileAccess(true);
		set.setCacheMode(WebSettings.LOAD_DEFAULT);
		set.setLoadsImagesAutomatically(true);
		set.setUserAgentString("coupMarket");
		set.setAppCacheEnabled(true);
		set.setSupportMultipleWindows(true);
		set.setLoadWithOverviewMode(true);

		ThisWebViewClient viewClient = new ThisWebViewClient();
		webView.setWebViewClient(viewClient);
		ThisWebCromeClient crome = new ThisWebCromeClient();
		webView.setWebChromeClient(crome);

		webView.addJavascriptInterface(new CMWebViewBridge(), "CMWebViewBridge");
	}

	private class ThisWebCromeClient extends WebChromeClient
	{
		@Override
		public boolean onJsConfirm( WebView view, String url, String message, final JsResult result )
		{
			try
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(CMarketHistoryActivity.this);
				
				builder.setMessage(message)
				       .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				       {
					       public void onClick( DialogInterface dialog, int which )
					       {
						       result.confirm();
					       }
				       })
				       .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
				       {
					       public void onClick( DialogInterface dialog, int which )
					       {
						       result.cancel();
					       }
				       });
				
				AlertDialog alert = builder.create();
				alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alert.show();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			return true;
		}

		@Override
		public boolean onCreateWindow( WebView view, final boolean dialog, boolean userGesture,
		                               final Message resultMsg )
		{
			((WebViewTransport) resultMsg.obj).setWebView(view);
			resultMsg.sendToTarget();

			return true;
		}

		@Override
		public void onCloseWindow( WebView window )
		{
		}

		@Override
		public void onProgressChanged( WebView view, int newProgress )
		{
		}

		@Override
		public void onReceivedTitle( WebView view, String title )
		{
		}

		@Override
		public void onReceivedIcon( WebView view, Bitmap icon )
		{
		}
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

	private class CMWebViewBridge
	{
		@SuppressWarnings("unused")
		public void redirectToUrl( final String strUrl )
		{
			runOnUiThread(new Runnable()
			{
				public void run()
				{
					m_WebView.loadUrl(strUrl);
				}
			});
		}

		@SuppressWarnings("unused")
		public void replaceToUrl( final String strUrl )
		{
			runOnUiThread(new Runnable()
			{
				public void run()
				{
					m_WebView.loadUrl(strUrl);
				}
			});
		}
	}

}

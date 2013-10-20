package com.etoos.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etoos.data.EventData;
import com.google.zxing.client.android.integration.IntentIntegrator;
import com.google.zxing.client.android.integration.IntentResult;

import android.net.Uri;
import android.os.Bundle;

import android.webkit.JsResult;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ResultWebActivity extends EtoosBaseActivity {

	private WebView mWebView;
	
	private final Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		
		// 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
		
		AfterCreate();
		
		mWebView = (WebView)findViewById(R.id.webView);
		
		IninWebView();
		
		// 초기 웹페이지 열기 
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			
			OpenWebView(_AppManager.WEB_URL);
		}

	}

	
	
	/// 안드로이드 메세지  웹 -> 안드로이드 
	private class AndroidBridge 
	{
        public void setMessage(final String arg) { // must be final
        	handler.post(new Runnable()
        	{
                public void run() 
                {
                	Log.v("Web Message", arg);
                    if (arg.equals("fail"))
                    {

                    }
                    else
                    {

                    }
                }
            });
        }
    }
	
	
	public void IninWebView()
	{

		mWebView.getSettings().setJavaScriptEnabled(true);  
		// Bridge 인스턴스 등록
        mWebView.addJavascriptInterface(new AndroidBridge(), "android"); 




        WebViewClientClass viewClient = new WebViewClientClass();
        mWebView.setWebViewClient(viewClient);

        ThisWebCromeClient cromeclient = new ThisWebCromeClient();
		mWebView.setWebChromeClient( cromeclient );

	}
	
	
	
	
	public void OpenWebView( String URL )
	{
		mWebView.loadUrl(URL);
	}
	
	
	
	private class ThisWebCromeClient extends WebChromeClient
	{
		final Context myApp = baseself;
		 @Override
		 public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
		    {
		        new AlertDialog.Builder(myApp)
		           // .setTitle("AlertDialog")
		            .setMessage(message)
		            .setPositiveButton(android.R.string.ok,
		                    new AlertDialog.OnClickListener()
		                    {
		                        public void onClick(DialogInterface dialog, int which)
		                        {
		                            result.confirm();
		                        }
		                    })
		            .setCancelable(false)
		            .create()
		            .show();

		        return true;
		    };
		    
		    
		    /**
		     * 페이지를 로딩하는 현재 진행 상황을 전해줍니다.
		     * newProgress  현재 페이지 로딩 진행 상황, 0과 100 사이의 정수로 표현.(0% ~ 100%)
		     */
		    @Override
		    public void onProgressChanged(WebView view, int newProgress) 
		    {
		        Log.i("WebView", "Progress: " + String.valueOf(newProgress)); 
		        super.onProgressChanged(view, newProgress);
		        mProgress.setMessage("로딩중 " +  String.valueOf(newProgress) + "%");
		        if ( newProgress > 98 )
		        {
		        	if ( mProgress.isShowing() == true)
		        		mProgress.hide();
		        }
		        else 
		        {
		        	if ( mProgress.isShowing() == false)
		        		mProgress.show();
		        }
		        
		    }

		    
		    /**
		     * 문서 제목에 변경이 있다고 알립니다.
		     * title  문서의 새로운 타이틀이 들어있는 문자열  
		     */
		    @Override
		    public void onReceivedTitle(WebView view, String title)
		    {
		        super.onReceivedTitle(view, title);
		    }
		    /*  아래처럼 title 태그 사이의 값을 가져옵니다.
			<title> LG텔레콤 전자결제 서비스 </title>
		     */


		    @Override
		    public Bitmap getDefaultVideoPoster() {
		        return super.getDefaultVideoPoster();
		    }


		    @Override
		    public View getVideoLoadingProgressView() {
		        return super.getVideoLoadingProgressView();
		    }


		    @Override
		    public void getVisitedHistory(ValueCallback<String[]> callback) {
		        super.getVisitedHistory(callback);
		    }


		    @Override
		    public void onCloseWindow(WebView window) {
		         super.onCloseWindow(window);
		    }


		    @Override
		    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		        super.onConsoleMessage(message, lineNumber, sourceID);
		    }


		    @Override
		    public boolean onCreateWindow(WebView view, boolean dialog,
		            boolean userGesture, Message resultMsg) {
		         return super.onCreateWindow(view, dialog, userGesture, resultMsg);
		    }

		    @Override
		    public void onGeolocationPermissionsHidePrompt() {
		         super.onGeolocationPermissionsHidePrompt();
		    }


		   

		    @Override
		    public void onHideCustomView() {
		        super.onHideCustomView();
		    }


		
		    
		    /**
		     * 현재 페이지에서 나와 탐색을 확정하는 대화 상자를 디스플레이한다고 클라이언트에게 
		     * 알려줍니다. 이것은 자바 스크립트 이벤트 onbeforeunload()의 결과입니다. 클라이언트가 
		     * true를 반환하는 경우, WebView는 클라이언트가 대화 상자를 처리하고 적절한 JsResult 
		     * 메쏘드를 호출할 것이라고 여깁니다. 클라이언트가 false를 반환하는 경우, true의 기본값은 
		     * 현재 페이지에서 나와 탐색하기를 수락하기 위한 자바 스크립트를 반환하게 될 것입니다. 
		     * 기본 동작은 false를 반환하는 것입니다. JsResult를 true로 설정한 것은 현재 페이지에서 나와 
		     * 탐색할 것이고 false로 설정한 것은 탐색을 취소할 것입니다.
		     * */
		    @Override
		    public boolean onJsBeforeUnload(WebView view, String url,
		            String message, JsResult result) {
		        return super.onJsBeforeUnload(view, url, message, result);
		    }
		 
		    /**
		     * 사용자에게 확인 대화 상자를 디스플레이한다고 클라이언트에게 알려줍니다. 클라이언트가 
		     * true를 반환하는 경우, WebView는 클라이언트가 확인 대화 상자를 처리하고 적절한 
		     * JsResult 메쏘드를 호출할 수 있다고 여깁니다. 클라이언트가 false를 반환하는 경우 false의 
		     * 기본값은 자바 스크립트로 반환될 것 입니다. 기본 동작은 false를 반환하는 것입니다.
		     */
		    @Override
		    public boolean onJsConfirm(WebView view, String url, String message,
		            JsResult result) {
		         return super.onJsConfirm(view, url, message, result);
		    }
		 
		   
		 
		    /**
		     * 자바 스크립트 실행 제한 시간을 초과했다고 클라이언트에게 알려줍니다. 그리고 
		     * 클라이언트가 실행을 중단할지 여부를 결정할 수 있습니다. 클라이언트가 true를 반환하는
		     * 경우, 자바 스크립트가 중단됩니다. 클라이언트가 false를 반환하는 경우, 계속 실행됩니다. 
		     * 참고로 지속적인 실행 상태에서는 제한 시간 카운터가 재설정되고  스크립트가 다음 체크 
		     * 포인트에서 완료되지 않을 경우 계속 콜백되어질 것집니다.
		     */
		    @Override
		    public boolean onJsTimeout() {
		        return super.onJsTimeout();
		    }

		    @Override
		    public void onReceivedTouchIconUrl(WebView view, String url,
		            boolean precomposed) {
		        super.onReceivedTouchIconUrl(view, url, precomposed);
		    }


		    @Override
		    public void onRequestFocus(WebView view) {
		        super.onRequestFocus(view);
		    }


		    @Override
		    public void onShowCustomView(View view, CustomViewCallback callback) {
		        super.onShowCustomView(view, callback);
		    }
	}
	

	
	
	// 뒤로가기 
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )
	{
		/*if( (keyCode == KeyEvent.KEYCODE_BACK) )
		{
			if( mWebView != null && mWebView.canGoBack() == true )
			{
				mWebView.goBack();
				return true;
			}

		}*/
		

		return super.onKeyDown(keyCode, event);
	}
	
	private class WebViewClientClass extends WebViewClient 
	{      
		// 특정 웹페이지 주소를 호출할때 들어오는 함수. 
		@Override        
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{             
			String strDecodedUrl = url;
			
			
			/*{
				CookieManager cookieManager = CookieManager.getInstance();
				cookieManager.setAcceptCookie(true);
				
				String cookie = cookieManager.getCookie("http://xx.xxx.xxx.com");

			    Log.d("Cookie", "cookie ------>"+cookie);
			}*/
			try
			{
				strDecodedUrl = URLDecoder.decode(url, "utf-8");
			}
			catch( UnsupportedEncodingException e )
			{
				e.printStackTrace();
			}

			if (url.startsWith("etoos://login"))
			{
				InitCookie();
				
				// 쿠키 매니져로 부터 쿠키를 가져온다. 
				CookieManager cookieManager = CookieManager.getInstance();
				cookieManager.setAcceptCookie(true);
				
				CookieSyncManager.getInstance().sync();


			    String cookie = cookieManager.getCookie(url);
			    
			    if ( cookie != null)
			    {
				    Log.d("Cookie", "cookie ------>"+cookie);

				    // 웹뷰에서 가져온 쿠키를 httpclient 에 입력해준다. 
				    SaveCookie(url);

					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					CookieSyncManager.getInstance().sync();
			    }

				
				
				
				{
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.ParamData.clear();
					
					
					{
						
					
						String value = url.replace("etoos://login?", "");
						{
							String  id  = value.split("&")[0].split("=")[1];
							String uid = value.split("&")[1].split("=")[1];
							
							_AppManager.ParamData.put("email", id);
							_AppManager.ParamData.put("uid", uid);
							_AppManager.ParamData.put("ostype", "android");
							_AppManager.ParamData.put("token", "1");
						}

					}


					Intent intent = new Intent(baseself, NetPopup.class);
					intent.putExtra("API", 1);
					startActivityForResult(intent , 1);
				}
				
	    		return true;
			}
			return super.shouldOverrideUrlLoading(view, url);             
		}  
		
		// 페이지 로딩 시작 
		@Override
		public void onPageStarted( WebView view, String url, Bitmap favicon )
		{
			super.onPageStarted(view, url, favicon);
			
		}

		// 페이지 로딩 종료. 
		@Override
		public void onPageFinished( WebView view, String url )
		{
			super.onPageFinished(view, url);
			
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
		AppManagement _AppManager = (AppManagement) getApplication();
		if ( requestCode == 1 )
		{
			if ( resultCode == 10 )
			{
				_AppManager.m_LoginCheck = false;
				_AppManager.LogInDataParsing();
				
				if ( _AppManager.m_ResultCode.equals("success"))
				{
					_AppManager.m_LoginCheck = true;
					
					// 로그인 완료 뒤에는 뒤로 간다. 
					onBackPressed();
				}
				
				else
				{
					
					baseself.ShowAlertDialLog(baseself, _AppManager.m_ResultMsg);
					
				}
				
				
			}
		}
		else
		{
			// QR코드/바코드를 스캔한 결과 값을 가져옵니다.
			IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

			if ( result.getContents() != null)
			{
				OpenWebView(result.getContents());
			}
			else
			{
				baseself.ShowAlertDialLog(baseself, "QR 코드 인식에 실패 했습니다.");
			}
		}

		

	}

	
	

	

}

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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebActivity extends EtoosBaseActivity {

	private WebView mWebView;
	
	private final Handler handler = new Handler();
	 ValueCallback<Uri>  mUploadMessage  ;
	
	final int FILECHOOSER_RESULTCODE = 2000;
	
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
		
		{
			mWebView.requestFocus(); 
			mWebView.setFocusable(true); 
			mWebView.setFocusableInTouchMode(true); 
			mWebView.setOnTouchListener(new View.OnTouchListener() { 
				@Override 
				public boolean onTouch(View v, MotionEvent event) { 
					switch (event.getAction()) { 
					case MotionEvent.ACTION_DOWN: 
					case MotionEvent.ACTION_UP: 
						if (!v.hasFocus()) { 
							v.requestFocus(); 
						} 
						break; 
					} 
					return false; 
				} 
			});




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
	
	
	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
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
		
		public void openFileChooser( ValueCallback<Uri> uploadMsg ){

	        openFileChooser( uploadMsg, "" );

	    }
		// For Android 4.1+

	    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

	        openFileChooser( uploadMsg, "" );

	    }


		public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType )   //  For Android  3.0+ (허니콤)
		{  

			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
			i.addCategory(Intent.CATEGORY_OPENABLE);  
			//i.setType("*/*");
			i.setType("image/*");
			baseself.startActivityForResult(
					Intent.createChooser(i, "이미지 파일 선택"),
					FILECHOOSER_RESULTCODE);


		}  
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
		        	{
		        		mProgress.hide();
		        		mWebView.setVisibility(View.VISIBLE);
		        	}
		        		
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
		if( (keyCode == KeyEvent.KEYCODE_BACK) )
		{
			if( mWebView != null && mWebView.canGoBack() == true )
			{
				mWebView.goBack();
				return true;
			}

		}

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
			else if (url.endsWith("pricecode://goods_detail_backbutton"))
			{
				
				//self.HideWebview();
				return true;
			}
			
			else if (url.startsWith("etoos://login"))
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
						String value1 = url.replace("etoos://login", "");
						if ( value1.equals(""))
						{
							new AlertDialog.Builder(baseself)
							.setTitle("로그인 에러")
							.setMessage("비정상적인 로그인 정보가 전달 되어서 어플리케이션을 종료합니다.") 
							.setPositiveButton("확인", new DialogInterface.OnClickListener() 
							{
								public void onClick(DialogInterface dialog, int whichButton)
								{   
									baseself.ExitApp();
								}
							})
							.show();
						}
						else	
						{
							String  id  = value.split("&")[0].split("=")[1];
							String uid = value.split("&")[1].split("=")[1];
							
							_AppManager.ParamData.put("email", id);
							_AppManager.ParamData.put("uid", uid);
							_AppManager.ParamData.put("ostype", "android");
							_AppManager.ParamData.put("token", "1");
							
							_AppManager.uid = uid;
						}

					}


					Intent intent = new Intent(baseself, NetPopup.class);
					intent.putExtra("API", 1);
					startActivityForResult(intent , 1);
				}
				
	    		return true;
			}
			else if  (url.startsWith("etoos://quickomr?"))
			{
				final AppManagement _AppManager = (AppManagement) getApplication();
				
				if ( _AppManager.m_LoginCheck == false)
				{
					new AlertDialog.Builder(baseself)
					.setTitle("로그인 확인")
					.setMessage("비 로그인 상태로 응시 시 응시 정보가 저장되지 않습니다. 로그인 하시겠습니까?") 
					.setPositiveButton("로그인 함", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int whichButton)
						{   
							_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/login/login.jsp";
							OpenWebView(_AppManager.WEB_URL);
						}
					})
					.setNegativeButton("로그인 안함", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int whichButton) 
						{
							if (_AppManager.CheckTest())
							{
								Intent intent;
					            intent = new Intent().setClass(baseself, ContinueActivity2.class);
					            startActivity( intent );;

							}
							else
							{
								
								_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/Quick.jsp";
								OpenWebView(_AppManager.WEB_URL);
									
								_AppManager.FastTestLoad  =false;

							}
						}
					})
					.show();
				}
				
				else
				{
					if (_AppManager.CheckTest())
					{
						Intent intent;
			            intent = new Intent().setClass(baseself, ContinueActivity2.class);
			            startActivity( intent );;
						return true; 
					}
					else
					{
						_AppManager.FastTestLoad  =false;
						_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/Quick.jsp";
						OpenWebView(_AppManager.WEB_URL);
			            return true;
					}
				}
				
				return true;



				
			}
			
			else if  (url.startsWith("etoos://qrcode?"))
			{
				/*Intent intent;
	            intent = new Intent().setClass(baseself, QRCodeTest.class);
	            startActivity( intent );*/
				IntentIntegrator.initiateScan(baseself);
	            return true;
			}
			
			
			else if  (url.startsWith("etoos://quicksubject?"))
			{
				
				AppManagement _AppManager = (AppManagement) getApplication();
				_AppManager.m_FastSelectIndex = 0;
				{
					
					
					String value = url.replace("etoos://quicksubject?", "");
					{
						String  id  = value.split("=")[1];
						_AppManager.m_FastSelectIndex = Integer.parseInt(id) -1;
					}

				}
				Intent intent;
	            intent = new Intent().setClass(baseself, FastTestSelect2.class);
	            startActivity( intent ); 
	            
	            return true;
			}
			
			else if  (url.startsWith("etoos://omring?"))
			{
				AppManagement _AppManager = (AppManagement) getApplication();

				JSONObject rootobject = new JSONObject();
				JSONArray array = new JSONArray();

				try
				{

					for ( int i = 0; i < _AppManager.m_OMRMaingIDList.size() ;i++  )
					{
						JSONObject object = new JSONObject();


						object.put("gid", _AppManager.m_OMRMaingIDList.get(i).toString());


						array.put(object);
					}
					rootobject.put("omrlist", array);

				} 
				catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				
				
				OpenWebView("http://etoos.hubweb.net:8080/SMART_OMR/examinee/OMRsearch3.jsp?list=" + rootobject);
				return true;
			}
			
			else if  (url.startsWith("etoos://omr?"))
			{
				AppManagement _AppManager = (AppManagement) getApplication();
				

				
				if ( _AppManager.m_LoginCheck )
				{
					
					String value = url.replace("etoos://omr?", "");
					{
						_AppManager.ParamData.clear();
						String  id  = value.split("=")[1];
						_AppManager.ParamData.put("gID", id);
						_AppManager.gid = id;

					}
					Intent intent = new Intent(baseself, NetPopup.class);
					intent.putExtra("API", 2);
					startActivityForResult(intent , 2);
				}
				else
				{
					baseself.ShowAlertDialLog(baseself, "로그인 상태에서만 온라인 시험에 응시 할수 있습니다. ");
				}

				
				
	            return true;
			}
			
			else if  (url.startsWith("etoos://main?"))
			{
				Intent intent;
	            intent = new Intent().setClass(baseself, MainActivity.class);
	            startActivity( intent );
				
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
			
			
			if  (url.startsWith("http://etoos.hubweb.net:8080/SMART_OMR/setting/setting.jsp"))
			//if ( url.matches("setting.jsp"))
			{
				String ver;
				try {
					ver = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
					mWebView.loadUrl("javascript:setAppVersion('" + ver + "')");
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else if (url.startsWith("http://etoos.hubweb.net:8080/SMART_OMR/setting/version.jsp"))
			{
				String ver;
				try {
					ver = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
					mWebView.loadUrl("javascript:setAppVersion('" + ver + "')");
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////
	// 웹뷰만 데이터 끊겼을때 따로 처리 해준다 
	// 웹뷰를 숨기고, 이전에 로드했던 URL을 다시 로드 할때까지 웹뷰를 숨긴다. 
	public void DataConnect()
	{

		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_Connect = true;; 

			ConnectView.setVisibility(View.GONE);
			OpenWebView(_AppManager.WEB_URL);
		}

	}

	public void DataDisconnect()
	{

		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_Connect = false; 
			ConnectView.setVisibility(View.VISIBLE);
			mWebView.setVisibility(View.GONE);
		}

	}
	    
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
		AppManagement _AppManager = (AppManagement) getApplication();
		
	    if(requestCode==FILECHOOSER_RESULTCODE)  
	    {  
	     
	    	//Toast.makeText(this,"FILECHOOSER_RESULTCODE 진입", Toast.LENGTH_SHORT).show(); 
	    	if (null == mUploadMessage) 
	    		return; 

	    	Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();  
	    	mUploadMessage.onReceiveValue(result);  
	    	mUploadMessage = null;  
	                
	    }
	    
	    else if ( requestCode == 1 )
		{
			if ( resultCode == 10 )
			{
				_AppManager.m_LoginCheck = false;
				
				_AppManager.LogInDataParsing();
				
				if ( _AppManager.m_ResultCode.equals("success"))
				{
					_AppManager.m_LoginCheck = true;
					OpenWebView("http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp");
				}
				
				else
				{
					
					baseself.ShowAlertDialLog(baseself, _AppManager.m_ResultMsg);
					
				}
				
				
			}
		}
		else if (requestCode == 2 )
		{
			if ( resultCode == 10 )
			{
				_AppManager.DefaultOMRData();
				Intent intent;
	            intent = new Intent().setClass(baseself, OMRMain.class);
	            startActivity( intent );
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

package com.example.hoteljoin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.example.hoteljoin.RoomDetailData;


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
import android.graphics.Bitmap;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class ReservationWebActivity extends HotelJoinBaseActivity  {
	ReservationWebActivity  self;
	 SlidingMenu menu ;

	 private WebView mWebView;
	 
	 private final Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reser_web);
		
		

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
		
		AfterCreate(7);
		

		mWebView = (WebView)findViewById(R.id.pay_web);

		RefreshUI();
	}


	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	
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
                    	new AlertDialog.Builder(self)
	   					 .setTitle("결제 실패")
	   					 .setMessage("결제를 실패 하였습니다.\n 결제를 처음부터 진행하겠습니다.  ") 
	   					 .setPositiveButton("확인", new DialogInterface.OnClickListener() 
	   					 {
	   					     public void onClick(DialogInterface dialog, int whichButton)
	   					     {   
	   					    	RefreshUI();
	   					     }
	   					 })
	   					 .show();
                    }
                    else if (arg.equals("success"))
                    {
                    	new AlertDialog.Builder(self)
	   					 .setTitle("결제 성공")
	   					 .setMessage("결제에 성공하여 예약이 완료되었습니다  ") 
	   					 .setPositiveButton("확인", new DialogInterface.OnClickListener() 
	   					 {
	   					     public void onClick(DialogInterface dialog, int whichButton)
	   					     {   
	   					    	 Intent intent;
	   				          // Create an Intent to launch an Activity for the tab (to be reused)
		   				          intent = new Intent().setClass(self, MainActivity.class);
		   				          
		   				          
		   				          startActivity( intent ); 
	   					     }
	   					 })
	   					 .show();
                    }
                    else
                    {
                    	new AlertDialog.Builder(self)
	   					 .setTitle("비정상적인 메세지 발견")
	   					 .setMessage("비정상적인 메세지가 발견되었습니다. \n 결제가 실패하였습니다. \n 자세한 사항을 확인 하기 위해 아래의 전화번호로 전화해주시길 바랍니다.  ") 
	   					 .setPositiveButton("확인", new DialogInterface.OnClickListener() 
	   					 {
	   					     public void onClick(DialogInterface dialog, int whichButton)
	   					     {   
	   					    	RefreshUI();
	   					     }
	   					 })
	   					 .show();
                    }
                }
            });
        }
    }



/*	public void BtnEvent( int id )
    {
		View imageview = (View)findViewById(id);
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
	
	*/
	
	public void RefreshUI()
	{
		final AppManagement _AppManager = (AppManagement) getApplication();

		// 웹뷰에서 자바스크립트실행가능        
		/*mWebView.getSettings().setJavaScriptEnabled(true); 
		// 구글홈페이지 지정        
		mWebView.loadUrl(_AppManager.m_PayURL);
		// WebViewClient 지정        
		mWebView.setWebViewClient(new WebViewClientClass());  
*/
		
		mWebView.getSettings().setJavaScriptEnabled(true);  //javascript 사용 가능하게 한다
		// Bridge 인스턴스 등록
        mWebView.addJavascriptInterface(new AndroidBridge(), "android"); 



		final Context myApp = this;

		mWebView.setWebChromeClient(new WebChromeClient() {
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
		    public void onProgressChanged(WebView view, int newProgress) {
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
		    public void onReceivedTitle(WebView view, String title) {
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


		});
		mWebView.loadUrl(_AppManager.m_PayURL);
		
		/*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((_AppManager.m_PayURL)));

		startActivity(intent);*/

	}
	
	private class WebViewClientClass extends WebViewClient 
	{         
		@Override        
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{             
			view.loadUrl(url);             
			return true;        
		}     
	}
	


	
	
	


    

}

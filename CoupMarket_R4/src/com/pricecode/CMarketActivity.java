package com.pricecode;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebView.WebViewTransport;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultAdapter;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XResultMap;
import com.rangboq.xutil.XUtilFunc;

public class CMarketActivity extends CMActivity
{
	
	enum CURR_TOP_MENU_XML_STATE
	{
		CURR_XML_NO,
		CURR_XML_IMAGE,
		CURR_XML_SUBJECT,
		CURR_XML_ADD_SUBJECT,
		CURR_XML_MONEY_SUBJECT,
		CURR_XML_NEWMONEY_SUBJECT,
		CURR_XML_PERCENT,
		CURR_XML_RESULT,
		
		

		CURR_XML_UNKNOWN,
	}
	class MainListData
	{
		String title;
		String desc;
		String newvalue;
		String oldvalue = "500원";
		String imageURL;
		String number;
		String percent;
		String LinkURL ="";
	}
	WebView m_WebView;
	String m_strUrl = "https://coupmarket.com/m/goods/pre_index.php";
	String m_strParam = "";
	
	String m_strUrl2 = "https://www.coupmarket.com/interx/req_info.php";
	String m_strParam2 = "";
	
	Spinner m_spArea1, m_spArea2;
	String m_strArea1 = "", m_strArea2 = "";
	
	ArrayList<String> m_List = new ArrayList<String>();
	ArrayList<Integer> m_ListIDX = new ArrayList<Integer>();

	ViewPager vp_main = null;	//ViewPager
	CustomPagerAdapter cpa = null;	//커스텀 어댑터
	
	
	Boolean m_HomeMenu = true;;
	
	
	String m_ViewPaperURL;
	
	int URLCount = 0;
	
	
	
	ArrayList<MainListData>	m_BottomBestList = new ArrayList<MainListData>();	// 리스트 

	
	protected int m_nBarButtonId2 = 0;
	
	int m_curViewPosition = 0;
	
	CMarketActivity self;
	
	boolean m_bWebView = false;
	
	public CookieHTTP		m_Cookie ;
	
	public ProgressDialog mProgress;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cmarket);
		
		self = this;
		
		m_Cookie = new CookieHTTP();
		
		initTopMainBar(R.id.top_imageView_cmarket);
		initBottomTabBar(0);
		
		initTopTabBar2(R.id.imageView_category1);

		m_spArea1 = (Spinner) findViewById(R.id.spinner_area1);
		m_spArea2 = (Spinner) findViewById(R.id.spinner_area2);
		m_WebView = (WebView) findViewById(R.id.webView_body);
		
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }

		
		
		
		if ( CMManager.getPrefString(CMManager.PREF_USER_ID, "") == null || CMManager.getPrefString(CMManager.PREF_USER_ID, "").equals(""))
		{
			
		}
		else
		{

					
		}

		
		{
			m_BottomBestList = new ArrayList< MainListData >();
			m_BottomBestList.clear();
			

		}
		
		vp_main = (ViewPager) findViewById(R.id.market_img);
		cpa = new CustomPagerAdapter();
		
		
		vp_main.setAdapter(cpa);
		
		 //ViewPage 페이지 변경 리스너
		vp_main.setOnPageChangeListener(new OnPageChangeListener()
        {

			@Override
			public void onPageScrollStateChanged(int state) {}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

			@Override
			public void onPageSelected(int position) 
			{
				selectViewPaper( position );
				 
				//페이지가 변경될때 변경되는 페이지 포지션에 대한 체크(버튼을 활성화/비활성화 시켜줌)
				//pageCheck(position);		
				
				/* if(position < m_List.size())        //1번째 아이템에서 마지막 아이템으로 이동하면
					 vp_main.setCurrentItem(position+ m_List.size(), false); //이동 애니메이션을 제거 해야 한다
	                else if(position >=  m_List.size()*2)     //마지막 아이템에서 1번째 아이템으로 이동하면
	                	vp_main.setCurrentItem(position -  m_List.size(), false);*/
			}
        	
        });
		
		vp_main.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				
				final int index = vp_main.getCurrentItem();
				
				openWebView(index);

			}
			
		});

		HideWebview();
		
		openWebView2();
		
		getThumData();
		
		
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
		
		//openWebView();
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
			else if ( m_bWebView == true)
			{
				
				
				//	리스트 뷰와 ViewPaper 보이도록 설정한다.  
				
				HideWebview();
				//
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	
	public void ShowWebview()
	{
		m_bWebView = true;
		
		((View)findViewById(R.id.data_view_scroll)).setVisibility(View.GONE);
		
		((LinearLayout)findViewById(R.id.view_header)).setVisibility(View.GONE);
		((LinearLayout)findViewById(R.id.listview_header)).setVisibility(View.GONE);
		((WebView)findViewById(R.id.webView_body)).setVisibility(View.VISIBLE);
	}
	
	private void HideWebview()
	{
		m_bWebView = false;
		
		if ( m_HomeMenu )
			((LinearLayout)findViewById(R.id.view_header)).setVisibility(View.VISIBLE);
		((View)findViewById(R.id.data_view_scroll)).setVisibility(View.VISIBLE);
		((LinearLayout)findViewById(R.id.listview_header)).setVisibility(View.GONE);
		((WebView)findViewById(R.id.webView_body)).setVisibility(View.GONE);
	}
	
	

	public void openWebView( final int index )
	{
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{


				Map<String, String> data = new HashMap  <String, String>();
				
				
				String strId = CMManager.getMyInfo("uid");


				String strJSON ="";
				

				strJSON = m_Cookie.GetHTTPData("http://www.coupmarket.com/interx/req_info.php?key=coupmarket"+
						"&cmd_mode=getGoodsViewDo"+
						"&nno=" + index + "&hashdata=" + getMD5Hash ("getGoodsViewDo" + "coupmarket")
						, data); 
				
				CURR_TOP_MENU_XML_STATE estate = CURR_TOP_MENU_XML_STATE.CURR_XML_UNKNOWN;
				
				// XML 파서를 초기화
				XmlPullParserFactory xmlpf = null;

				try {
					xmlpf = XmlPullParserFactory.newInstance();
				} catch (XmlPullParserException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
				XmlPullParser xmlp = null;
				try {
					xmlp = xmlpf.newPullParser();
				} catch (XmlPullParserException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				try {
					xmlp.setInput(new StringReader(strJSON));
				} catch (XmlPullParserException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				// 파싱에 발생하는 event?를 처리하기 위한 메소드
	            int eventType = 0;
				try {
					eventType = xmlp.getEventType();
				} catch (XmlPullParserException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


				while( eventType != XmlPullParser.END_DOCUMENT )
	            {


	                switch ( eventType ) 
	                {
	                case XmlPullParser.START_DOCUMENT: // 문서 시작 태그를 만난 경우
	                case XmlPullParser.END_DOCUMENT: // 문서 끝 태그를 만난 경우
	                    break;
	                case XmlPullParser.START_TAG: // 쌍으로 구성된 태그의 시작을 만난 경우
	                    // 요소명 체크
	                    if ( xmlp.getName().equals("no") )
	                    { 
	                        estate = CURR_TOP_MENU_XML_STATE.CURR_XML_NO;
	                    }
	                    else if ( xmlp.getName().equals("viewdo")   )
	                    {
	                    	estate = CURR_TOP_MENU_XML_STATE.CURR_XML_IMAGE;

	                    }

	                case XmlPullParser.END_TAG: // 쌍으로 구성된 태그의 끝을 만난 경우
	                    break;
	                case XmlPullParser.TEXT: // TEXT로 이루어진 내용을 만난 경우
	                    switch ( estate )
	                    {

	                    case CURR_XML_IMAGE:
	                    {
	                    	m_ViewPaperURL = xmlp.getText();
	                    }
	                    break;
						default:
							break;


	                    }
	                    break;
	                    
	                } // switch end
	                
	                // 다음 내용을 읽어옵니다
	                try
	                {
						try {
							eventType = xmlp.next();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
	                catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                
	            } // while end
				handler2.sendEmptyMessage(200);
				
			}
		});
		thread.start();	
	}
	public  void openWebView()
	{
		setWebViewProperty(m_WebView);

		if( CMManager.enableHttps == false )
			m_strUrl = m_strUrl.replace("https://", "http://");

		try
		{
			String strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
			String strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");

			String strCmdMode = "goToCmarket";
			String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + "coupmarket");

			m_strParam = String.format("cmd_mode=%s&access_device=android&id=notice1&uid=%s&passwd=%s&area1=%s&area2=%s&hashdata=%s", 
			                           strCmdMode, strId, strPassword, m_strArea1, m_strArea2, strHashData);

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

	public  void openWebView2()
	{
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		
		String strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
		if ( strId == null || strId.equals("") )
		{
			return;
		}
		setWebViewProperty(m_WebView);

		if( CMManager.enableHttps == false )
			m_strUrl2 = m_strUrl2.replace("https://", "http://");

		try
		{
			String strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");

			String strCmdMode = "login";
			String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + "coupmarket");

			m_strParam2= String.format("cmd_mode=%s&uid=%s&passwd=%s&hashdata=%s", strCmdMode, strId,
                    strPassword, strHashData);

			m_WebView.clearCache(true);
			m_WebView.clearHistory();
			m_WebView.clearFormData();
			m_WebView.postUrl(m_strUrl2, m_strParam2.getBytes("UTF-8"));
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
	
	
	public  void openWebView(String URL)
	{
		setWebViewProperty(m_WebView);

		if( CMManager.enableHttps == false )
			m_strUrl = m_strUrl.replace("https://", "http://");

		try
		{
			String strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
			String strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");
			String strHashData;
			if ( strId== null || strId.equals("") )
			{
				String strCmdMode = "goToCmarket";
				strHashData = XUtilFunc.getMD5Hex( strCmdMode + "coupmarket");

				m_strParam = String.format(URL );
			}
			else
			{
				String strCmdMode = "goToCmarket";
				strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + "coupmarket");

				m_strParam = String.format(URL +  "&hashdata=" + strHashData);
			}


			m_strParam = String.format(URL +  "&hashdata=" + strHashData);

			m_WebView.clearCache(true);
			m_WebView.clearHistory();
			m_WebView.clearFormData();
			m_WebView.loadUrl(URL);
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
				AlertDialog.Builder builder = new AlertDialog.Builder(CMarketActivity.this);
				
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
			else if (url.endsWith("pricecode://goods_detail_backbutton"))
			{
				
				self.HideWebview();
				return true;
			}
			
			else if (url.startsWith("pricecodelogin://"))
			{
				new AlertDialog.Builder(self)
				.setTitle("에러")
	    		.setMessage("로그인시에만 진행가능한 페이지입니다. 로그인을 하시겠습니까?")
	    		.setPositiveButton("예", new DialogInterface.OnClickListener()
	    		{
	    			public void onClick(DialogInterface dialog, int which)
	    			{
	    				Intent intent = null;
	    				intent = CMManager.getIntent(baseself, LoginActivity.class);
	    				if(intent != null)
	    			    	startActivity(intent);
	    				dialog.dismiss();
	    			}
	    		}
	    		)
	    		.setNegativeButton("아니요", new DialogInterface.OnClickListener()
	    		{
	    			public void onClick(DialogInterface dialog, int which)
	    			{

	    				dialog.dismiss();
	    			}
	    		})
	    		.show();
	    		return true;
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

	
	XResultList m_AreaResultList = null, m_AreaList1 = null, m_AreaList2 = null;
	boolean updateAreaList( XResultList resultList )
	{
		m_AreaResultList = resultList;
		if( m_AreaResultList == null )
			m_AreaResultList = new XResultList();
		
		m_AreaList1 = new XResultList();
		XResultMap defaultMap = new XResultMap();
		defaultMap.put("subject", "전체지역");
		m_AreaList1.add(defaultMap);
		for( XResultMap result : m_AreaResultList )
		{
			String strPNo = result.get("pno");
			if( strPNo == null || strPNo.length() == 0 )
				continue;

			if( strPNo.equals("0") )
				m_AreaList1.add(result);
		}
		
		XResultAdapter adapter1 = new XResultAdapter(this, android.R.layout.simple_spinner_item,
		                                             android.R.id.text1, "subject", m_AreaList1);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item,
		                                 android.R.id.text1);
		m_spArea1.setAdapter(adapter1);
		m_spArea1.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int pos, long id )
			{
				if( m_AreaList1 == null )
					return;

				XResultMap result1 = m_AreaList1.get(pos);
				if( result1 == null )
					return;

				String strNo1 = result1.get("no");
				if( strNo1 == null )
					strNo1 = "";

				m_AreaList2 = new XResultList();
				XResultMap defaultMap = new XResultMap();
				defaultMap.put("subject", "전체지역");
				m_AreaList2.add(defaultMap);
				if( strNo1.length() > 0 )
				{
					m_spArea2.setEnabled(true);

					for( XResultMap resultOfAll : m_AreaResultList )
					{
						String strPNo = resultOfAll.get("pno");
						if( strPNo == null || strPNo.length() == 0 )
							continue;

						if( strPNo.equals(strNo1) )
							m_AreaList2.add(resultOfAll);
					}
				}
				else
				{
					m_spArea2.setEnabled(false);
				}

				XResultAdapter adapter2 = new XResultAdapter(CMarketActivity.this,
				                                             android.R.layout.simple_spinner_item,
				                                             android.R.id.text1, "subject", m_AreaList2);
				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item,
				                                 android.R.id.text1);
				m_spArea2.setAdapter(adapter2);
				m_spArea2.setOnItemSelectedListener(new OnItemSelectedListener()
				{
					@Override
                    public void onItemSelected( AdapterView<?> arg0, View arg1, int arg2, long arg3 )
                    {
						applyArea();
                    }

					@Override
                    public void onNothingSelected( AdapterView<?> arg0 )
                    {
                    }
				});
				
				int nArea2Index = 0;
				for( int nIndex=0; nIndex<m_AreaList2.size(); nIndex++ )
				{
					XResultMap result2 = m_AreaList2.get(nIndex);
					String strNo2 = result2.get("no");
					if( strNo2 == null || strNo2.length() == 0 )
						continue;

					if( strNo2.equals(m_strArea2) )
					{
						nArea2Index = nIndex;
						break;
					}
				}
				m_spArea2.setSelection(nArea2Index);
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent )
			{
				m_spArea2.setAdapter(null);
			}
		});

		int nArea1Index = 0;
		for( int nIndex=0; nIndex<m_AreaList1.size(); nIndex++ )
		{
			XResultMap result = m_AreaList1.get(nIndex);
			String strNo = result.get("no");
			if( strNo == null || strNo.length() == 0 )
				continue;

			if( strNo.equals(m_strArea1) )
			{
				nArea1Index = nIndex;
				break;
			}
		}
		m_spArea1.setSelection(nArea1Index);

		return true;
	}
	
	void applyArea()
	{
    	if(m_AreaList1 == null || m_AreaList1.size() == 0)
    		return;
    	if(m_AreaList2 == null || m_AreaList2.size() == 0)
    		return;

    	int nArea1 = m_spArea1.getSelectedItemPosition();
    	int nArea2 = m_spArea2.getSelectedItemPosition();
    	if(m_AreaList1.size() <= nArea1 || m_AreaList2.size() <= nArea2)
    		return;
    	
    	XResultMap result1 = m_AreaList1.get(nArea1);
    	XResultMap result2 = m_AreaList2.get(nArea2);
    	if(result1 == null || result2 == null)
    		return;
    	
    	String strArea1 = result1.get("no");
    	String strArea2 = result2.get("no");
    	if(strArea1 == null)
    	{
    		strArea1 = "";
    		strArea2 = "";
    	}
    	
    	if(strArea2 == null)
    		strArea2 = "";
    	
    	m_strArea1 = strArea1;
    	m_strArea2 = strArea2;
    	openWebView();
	}

	
	protected void initTopTabBar2(int nMyId)
	{
		int itemIds[] = {
						R.id.imageView_category1,
		                 R.id.imageView_category2,
		                 R.id.imageView_category3,
		                 R.id.imageView_category4,
		                 R.id.imageView_category5,
		};
		m_nBarButtonId2 = R.id.imageView_category1;
		ImageView ivButton = null;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);
			
			if(nId == nMyId)
			{
				m_nBarButtonId2 = nMyId;
				ivButton.setSelected(true);
			}
			else
			{
				ivButton.setSelected(false);
			}
		
			ivButton.setOnClickListener( new OnClickListener()
			{

				@Override
				public void onClick(View v) 
				{
					selectTopTabBar2(v.getId());
				}
			
			});
		}
		
		
	}
	
	@SuppressWarnings("null")
	protected void selectTopTabBar2(int nMyId)
	{
		int itemIds[] = {
						R.id.imageView_category1,
		                 R.id.imageView_category2,
		                 R.id.imageView_category3,
		                 R.id.imageView_category4,
		                 R.id.imageView_category5,
		};
		
		ImageView ivButton = null;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);
			if(nId == nMyId)
			{
				
				
				if ( m_bWebView )
				{
					
					HideWebview();

				}
				
				m_nBarButtonId2 = nMyId;
				ivButton.setSelected(true);
				
				if ( R.id.imageView_category1 == nMyId)
				{
					m_HomeMenu = true;
					((LinearLayout)findViewById(R.id.view_header)).setVisibility(View.VISIBLE);
					getThumData();
				}
				else
				{
					m_HomeMenu = false;
					((LinearLayout)findViewById(R.id.view_header)).setVisibility(View.GONE);
					getListData();
				}
				
			}
			else
			{
				ivButton.setSelected(false);
			}
		}
	}
	
	private void initViewPaperDot( int count)
	{
		int itemIds[] = {
				R.id.imageView_dot_1,
                 R.id.imageView_dot_2,
                 R.id.imageView_dot_3,
                 R.id.imageView_dot_4,
                 R.id.imageView_dot_5,
                 R.id.imageView_dot_6,
                 R.id.imageView_dot_7,
                 R.id.imageView_dot_8,
                 R.id.imageView_dot_9,
                 R.id.imageView_dot_10,
                 
		};
		
		ImageView ivButton = null;
		int i = 0;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);
			
			if (i < count)
			{
				ivButton.setVisibility(View.VISIBLE);
			}
			else
			{
				ivButton.setVisibility(View.GONE);
			}
			i++;
		}
	}
	
	
	protected void selectViewPaper(int position)
	{
		int itemIds[] = {
				R.id.imageView_dot_1,
                R.id.imageView_dot_2,
                R.id.imageView_dot_3,
                R.id.imageView_dot_4,
                R.id.imageView_dot_5,
                R.id.imageView_dot_6,
                R.id.imageView_dot_7,
                R.id.imageView_dot_8,
                R.id.imageView_dot_9,
                R.id.imageView_dot_10,
		};
		int nMyId = itemIds[position];
		ImageView ivButton = null;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);
			if(nId == nMyId)
			{
				ivButton.setSelected(true);
			}
			else
			{
				ivButton.setSelected(false);
			}
		}
		
		vp_main.setCurrentItem(position);
	}
	
	public void SetCenterLineTextView( TextView view )
	{
		view.setPaintFlags(view.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
		
	}
	
	

	public void getThumData()
	{

		m_ListIDX.clear();
		m_List.clear();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{


				Map<String, String> data = new HashMap  <String, String>();
				
			
				String strId = CMManager.getMyInfo("uid");


				String strCmdMode = "getGoodsTopMenuKind";
				//String strCmdMode = "getGoodsKind";
				String key = "coupmarket";
				data.put("key", key);
				data.put("cmd_mode", strCmdMode);
				data.put("topmenukind", "191");
				

				//String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + key);
				String strHashData = getMD5Hash (strCmdMode + key);
				data.put("hashdata", strHashData);
				
				
				String strJSON = m_Cookie.PostHTTPData( "http://www.coupmarket.com/interx/req_info.php", data); 
				
				// XML 파서를 초기화
				XmlPullParserFactory xmlpf = null;

				try {
					xmlpf = XmlPullParserFactory.newInstance();
				} catch (XmlPullParserException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
				XmlPullParser xmlp = null;
				try {
					xmlp = xmlpf.newPullParser();
				} catch (XmlPullParserException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				try {
					xmlp.setInput(new StringReader(strJSON));
				} catch (XmlPullParserException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				// 파싱에 발생하는 event?를 처리하기 위한 메소드
	            int eventType = 0;
				try {
					eventType = xmlp.getEventType();
				} catch (XmlPullParserException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				CURR_TOP_MENU_XML_STATE estate = CURR_TOP_MENU_XML_STATE.CURR_XML_UNKNOWN;
				String temp= "";
				
				
				while( eventType != XmlPullParser.END_DOCUMENT )
	            {


	                switch ( eventType ) 
	                {
	                case XmlPullParser.START_DOCUMENT: // 문서 시작 태그를 만난 경우
	                case XmlPullParser.END_DOCUMENT: // 문서 끝 태그를 만난 경우
	                    break;
	                case XmlPullParser.START_TAG: // 쌍으로 구성된 태그의 시작을 만난 경우
	                    // 요소명 체크
	                    if ( xmlp.getName().equals("no") )
	                    { 
	                        estate = CURR_TOP_MENU_XML_STATE.CURR_XML_NO;
	                    }
	                    else if ( xmlp.getName().equals("subjectimage")   )
	                    {
	                    	estate = CURR_TOP_MENU_XML_STATE.CURR_XML_IMAGE;

	                    }
	                    else if ( xmlp.getName().equals("result")   )
	                    {
	                    	estate = CURR_TOP_MENU_XML_STATE.CURR_XML_RESULT;
	                    }
	                    else
	                    {
	                    	estate = CURR_TOP_MENU_XML_STATE.CURR_XML_UNKNOWN;
	                    }

	                case XmlPullParser.END_TAG: // 쌍으로 구성된 태그의 끝을 만난 경우
	                    break;
	                case XmlPullParser.TEXT: // TEXT로 이루어진 내용을 만난 경우
	                    switch ( estate )
	                    {
	                    case CURR_XML_NO:
	                    {
	                    	m_ListIDX.add(Integer.parseInt(xmlp.getText()));
	                    }
	                    	break;
	                    case CURR_XML_IMAGE:
	                    {
	                    	m_List.add(xmlp.getText());
	                    }
	                    	break;


	                    case CURR_XML_RESULT:
	                    {
	                    	temp = xmlp.getText();
	                    	
	                    	if ( !temp.equals("true") )
	                    	{
	                    		handler2.sendEmptyMessage(17);
	                    	}
	                    }
	                    	break;
	                    case CURR_XML_UNKNOWN:
	                    	break;


	                    }
	                    break;
	                } // switch end
	                
	                // 다음 내용을 읽어옵니다
	                try
	                {
						try {
							eventType = xmlp.next();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
	                catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                
	            } // while end
				
				
				
				handler2.sendEmptyMessage(10);
			}
		});
		thread.start();
	
	}
	
	public void getListData()
	{
		((LinearLayout)findViewById(R.id.data_view_2)).removeAllViews();
		m_BottomBestList.clear();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{


				Map<String, String> data = new HashMap  <String, String>();
				
				
				String strId = CMManager.getMyInfo("uid");


				String strCmdMode = "getGoodsTopBest";
				//String strCmdMode = "getGoodsKind";
				String key = "coupmarket";
				data.put("key", key);
				data.put("cmd_mode", strCmdMode);
				
				if ( m_nBarButtonId2 == R.id.imageView_category1)
				{
					data.put("topmenukind", "191");
				}
				else if ( m_nBarButtonId2 == R.id.imageView_category2)
				{
					data.put("topmenukind", "192");
				}
				else if ( m_nBarButtonId2 == R.id.imageView_category3)
				{
					data.put("topmenukind", "193");
				}
				else if ( m_nBarButtonId2 == R.id.imageView_category4)
				{
					data.put("topmenukind", "194");
				}
				else if ( m_nBarButtonId2 == R.id.imageView_category5)
				{
					data.put("topmenukind", "195");
				}
				
				
				

				

				//String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + key);
				String strHashData = getMD5Hash (strCmdMode + key);
				data.put("hashdata", strHashData);
				
				String strJSON = m_Cookie.GetHTTPData( "http://www.coupmarket.com/interx/req_info.php", data); 
				
				// XML 파서를 초기화
				XmlPullParserFactory xmlpf = null;

				try {
					xmlpf = XmlPullParserFactory.newInstance();
				} catch (XmlPullParserException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
				XmlPullParser xmlp = null;
				try {
					xmlp = xmlpf.newPullParser();
				} catch (XmlPullParserException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				try {
					xmlp.setInput(new StringReader(strJSON));
				} catch (XmlPullParserException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				// 파싱에 발생하는 event?를 처리하기 위한 메소드
	            int eventType = 0;
				try {
					eventType = xmlp.getEventType();
				} catch (XmlPullParserException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				CURR_TOP_MENU_XML_STATE estate = CURR_TOP_MENU_XML_STATE.CURR_XML_UNKNOWN;
				String temp= "";
				
				MainListData bestData = null;
				while( eventType != XmlPullParser.END_DOCUMENT )
	            {


	                switch ( eventType ) 
	                {
	                case XmlPullParser.START_DOCUMENT: // 문서 시작 태그를 만난 경우
	                case XmlPullParser.END_DOCUMENT: // 문서 끝 태그를 만난 경우
	                    break;
	                case XmlPullParser.START_TAG: // 쌍으로 구성된 태그의 시작을 만난 경우
	                    // 요소명 체크
	                    if ( xmlp.getName().equals("no") )
	                    { 
	                        estate = CURR_TOP_MENU_XML_STATE.CURR_XML_NO;
	                    }
	                    else if ( xmlp.getName().equals("subjectimage")   )
	                    {
	                    	estate = CURR_TOP_MENU_XML_STATE.CURR_XML_IMAGE;

	                    }
	                    else if ( xmlp.getName().equals("subject")   )
	                    {
	                    	estate = CURR_TOP_MENU_XML_STATE.CURR_XML_SUBJECT;
	                    }
	                   
	                    else if ( xmlp.getName().equals("add_subject")   )
	                    {
	                    	estate = CURR_TOP_MENU_XML_STATE.CURR_XML_ADD_SUBJECT;

	                    }
	                    else if ( xmlp.getName().equals("ori_money")   )
	                    {
	                    	estate = CURR_TOP_MENU_XML_STATE.CURR_XML_MONEY_SUBJECT;
	                    }
	                    else if (  xmlp.getName().equals("money") )
	                    {
	                    	estate = CURR_TOP_MENU_XML_STATE.CURR_XML_NEWMONEY_SUBJECT;
	                    }
	                    else if ( xmlp.getName().equals("result")   )
	                    {
	                    	estate = CURR_TOP_MENU_XML_STATE.CURR_XML_RESULT;
	                    }
	                    else if ( xmlp.getName().equals("percent")   )
	                    {
	                    	estate = CURR_TOP_MENU_XML_STATE.CURR_XML_PERCENT;
	                    }

	                case XmlPullParser.END_TAG: // 쌍으로 구성된 태그의 끝을 만난 경우
	                    break;
	                case XmlPullParser.TEXT: // TEXT로 이루어진 내용을 만난 경우
	                    switch ( estate )
	                    {
	                    case CURR_XML_NO:
	                    {
	                    	bestData = new MainListData();
	                    	bestData.number = xmlp.getText();
	                    }
	                    	break;
	                    case CURR_XML_IMAGE:
	                    {
	                    	bestData.imageURL = xmlp.getText();
	                    }
	                    	break;
	                    case CURR_XML_SUBJECT:
	                    {
	                    	bestData.title = xmlp.getText();
	                    	
	                    }
	                    	break;
	                    case CURR_XML_ADD_SUBJECT:
	                    {
	                    	bestData.desc = xmlp.getText();
	                    }
	                    	break;
	                    case CURR_XML_MONEY_SUBJECT:
	                    {
	                    	bestData.oldvalue = xmlp.getText() +"원";
	                    	
	                    }
	                    	break;
	                    case CURR_XML_NEWMONEY_SUBJECT:
	                    {
	                    	bestData.newvalue = xmlp.getText() +"원";
	                    	
	                    }
	                    	break;
	                    case CURR_XML_PERCENT:
	                    {
	                    	bestData.newvalue = bestData.newvalue  + " ( "+ xmlp.getText() + "%의 할인 )";
	                    	m_BottomBestList.add(bestData);
	                    }
	                    	break;
	                    case CURR_XML_RESULT:
	                    {
	                    	temp = xmlp.getText();
	                    	
	                    	if ( !temp.equals("true") )
	                    	{
	                    		handler2.sendEmptyMessage(17);
	                    	}
	                    }
	                    	break;


	                    }
	                    break;
	                } // switch end
	                
	                // 다음 내용을 읽어옵니다
	                try
	                {
						try {
							eventType = xmlp.next();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
	                catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                
	            } // while end
				
					
				
				handler2.sendEmptyMessage(11);
			}
		});
		thread.start();
	
	}
	
	
	
	final Handler handler2 = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			
			switch(msg.what)
			{
			case 0:
				break;
			case 10:
				initViewPaperDot( m_List.size() );	
				selectViewPaper( 0 );
				getListData();
				break;
			case 11:
				((TextView)findViewById(R.id.textView_count)).setText(Integer.toString(m_BottomBestList.size()));
				
				{
					
					mProgress.show();
					handler2.sendEmptyMessage(30);
				}
				
				break;
			case 17:
				break; 
			case 20:
				break;
			case 30:
			{
				for ( int i = 0 ; i <m_BottomBestList.size() ; i++ )
				{
					MarketItem item = new MarketItem(self);
					
					item.SetData(m_BottomBestList.get(i).title, m_BottomBestList.get(i).imageURL, m_BottomBestList.get(i).desc, m_BottomBestList.get(i).newvalue, m_BottomBestList.get(i).oldvalue
							, m_BottomBestList.get(i).LinkURL , Integer.parseInt( m_BottomBestList.get(i).number));

					
					((LinearLayout)findViewById(R.id.data_view_2)).addView(item);
				}

			}
				break;
			case 200:
				openWebView(m_ViewPaperURL);
				break; 
			}
		}
	};
	
	
	 private class CustomPagerAdapter extends PagerAdapter{

	        
	        @Override
	        public int getCount() {
	                return m_List.size() ;
	        }

	        /**
	         * 각 페이지 정의
	         */
	        @Override
	        public Object instantiateItem(View collection, int position)
	        {

	        	

	        	ImageView img = new ImageView(self); //this is a variable that stores the context of the activity
	            //set properties for the image like width, height, gravity etc...
	        	
	        	//position %= m_List.size();

	        	
	        	final int pos = position;
	            int resId = 0;
	            /*switch (position) {
	            case 0:
	                resId = R.drawable.a;
	                break;
	            case 1:
	                resId = R.drawable.b;
	                break;
	            case 2:
	                resId = R.drawable.c;
	                break;
	            case 3:
	                resId = R.drawable.d;
	                break;
	            }

	            img.setBackgroundResource(resId);*/
	            //img.setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));

	            img.setTag( m_List.get(pos));
				BitmapManager.INSTANCE.loadBitmap_2(m_List.get(pos), img);
				
				
				OnClickListener ol  = new OnClickListener()
	            {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ShowWebview();
						openWebView(m_ListIDX.get(pos));
					}            
	            };
	            img.setOnClickListener(ol);

	            ((ViewPager) collection).addView(img,0);  

	            return img;

	        }

	        @Override
	        public void destroyItem(View collection, int position, Object view) {
	                ((ViewPager) collection).removeView((View) view);
	        }

	        
	        
	        @Override
	        public boolean isViewFromObject(View view, Object object) {
	                return view==((View)object);
	        }

	        
	        @Override
	        public void finishUpdate(View v) {
	        }
	        

	        @Override
	        public void restoreState(Parcelable pc, ClassLoader cl) {
	        }

	        @Override
	        public Parcelable saveState() {
	                return null;
	        }

	        @Override
	        public void startUpdate(View v) {
	        }

	    }
	 
	
		
	
}

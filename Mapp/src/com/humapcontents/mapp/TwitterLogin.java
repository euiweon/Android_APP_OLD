package com.humapcontents.mapp;



import com.humapcontents.mapp.data.TwitterConstant;

import android.app.*;
import android.content.*;
import android.os.*;
import android.webkit.*;

public class TwitterLogin extends Activity {

     Intent mIntent;
 
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.twitter_login);
 
         WebView webView = (WebView) findViewById(R.id.webView1);
 
         // 화면 전환시 WebView에서 화면 전환하도록한다.
         // 이렇게하지 않으면 표준 브라우저가 열려 버린다.
         webView.setWebViewClient(new WebViewClient() {
             public void onPageFinished(WebView view, String url) {
                 // page 렌더링이 완료되면 호출됨.
                 super.onPageFinished(view, url);
 
                 // 로그아웃을 처리한후에는 바로 Activity를 종료시킨다.
                 if (url != null && url.equals("http://mobile.twitter.com/")) {
                     finish();
                 } else if (url != null && url.startsWith(TwitterConstant.TWITTER_CALLBACK_URL)) {
                     String[] urlParameters = url.split("\\?")[1].split("&");
                     String oauthToken = "";
                     String oauthVerifier = "";
 
                     try {
                         if (urlParameters[0].startsWith("oauth_token")) {
                             oauthToken = urlParameters[0].split("=")[1];
                         } else if (urlParameters[1].startsWith("oauth_token")) {
                             oauthToken = urlParameters[1].split("=")[1];
                         }
 
                         if (urlParameters[0].startsWith("oauth_verifier")) {
                             oauthVerifier = urlParameters[0].split("=")[1];
                         } else if (urlParameters[1].startsWith("oauth_verifier")) {
                             oauthVerifier = urlParameters[1].split("=")[1];
                         }
 
                         mIntent.putExtra("oauth_token", oauthToken);
                         mIntent.putExtra("oauth_verifier", oauthVerifier);
 
                         setResult(RESULT_OK, mIntent);
                         finish();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             }
         });
         mIntent = getIntent();
         String url1 = mIntent.getStringExtra("auth_url");
         webView.loadUrl(url1);
     }
 }
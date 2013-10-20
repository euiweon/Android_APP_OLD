package com.pricecode;

import com.pricecode.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class OptionActivity extends CMActivity implements OnClickListener
{
	LinearLayout m_layoutBefore, m_layoutAfter;
	
	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_option);
		
		initTopMainBar(0);
		initBottomTabBar(R.id.tab_imageView_option);
	    
	    m_layoutBefore = (LinearLayout) findViewById(R.id.linearLayout_before_login);
	    m_layoutAfter = (LinearLayout) findViewById(R.id.linearLayout_after_login);
	    
	    int nButtonIds[] = {
	                      R.id.imageButton_login,
	                      R.id.imageButton_join,
	                      R.id.imageButton_logout,
	                      R.id.imageButton_edit_my_info,
	                      R.id.imageButton_notice,
	                      R.id.imageButton_inquiry,
	                      R.id.imageButton_select_area,
	    };
	    for(int nButtonId : nButtonIds)
	    	findViewById(nButtonId).setOnClickListener(this);
	    
	    if(CMManager.isOnline())
	    	m_layoutBefore.setVisibility(View.GONE);
	    else
	    	m_layoutAfter.setVisibility(View.GONE);
    }

	@Override
    protected void onDestroy()
    {
	    super.onDestroy();
    }

	@Override
    public void onClick( View v )
    {
		int nId = v.getId();
		
		Intent intent = null;
	    switch(nId)
	    {
	    	case R.id.imageButton_login:
	    		intent = CMManager.getIntent(this, LoginActivity.class);
	    		break;
	    	case R.id.imageButton_join:
	    		intent = CMManager.getIntent(this, MemberInfoActivity.class);
	    		intent.putExtra("isJoinUI", true);
	    		break;
	    	case R.id.imageButton_logout:
	    		CMManager.setPrefBoolean(CMManager.PREF_AUTO_LOGIN, false);
	    		CMManager.setPrefString(CMManager.PREF_USER_ID, "");
	    		CMManager.setPrefString(CMManager.PREF_USER_PASSWORD, "");
	    		intent = CMManager.getIntent(this, CMarketActivity.class);
	    		break;
	    	case R.id.imageButton_edit_my_info:
	    		intent = CMManager.getIntent(this, MemberInfoActivity.class);
	    		intent.putExtra("isJoinUI", false);
	    		break;
	    	case R.id.imageButton_notice:
	    		intent = CMManager.getIntent(this, NoticeActivity.class);
	    		break;
	    	case R.id.imageButton_inquiry:
	    		intent = CMManager.getIntent(this, InquiryActivity.class);
	    		break;
	    	case R.id.imageButton_select_area:
	    		intent = CMManager.getIntent(this, SelectAreaActivity.class);
	    		break;
	    }
	    
	    if(intent == null)
	    	return;
	    
	    if(nId == R.id.imageButton_join)
	    	startActivityForResult(intent, REQUEST_FOR_JOIN);
	    else
	    	startActivity(intent);
	    
	    if(nId == R.id.imageButton_login || nId == R.id.imageButton_logout)
	    	finish();
    }
	
	static final int REQUEST_FOR_JOIN = 1;
	@Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
	    // TODO Auto-generated method stub
	    super.onActivityResult(requestCode, resultCode, data);
	    if(resultCode != RESULT_OK || data == null)
	    	return;
	    
	    String strId = data.getStringExtra("id");
	    String strPassword = data.getStringExtra("password");
	    if(strId == null || strId.length() == 0)
	    	return;
	    if(strPassword == null || strPassword.length() == 0)
	    	return;
	    
	    Intent intent = CMManager.getIntent(this, LoginActivity.class);
	    intent.putExtra("id", strId);
	    intent.putExtra("password", strPassword);
	    startActivity(intent);
	    finish();
    }
}

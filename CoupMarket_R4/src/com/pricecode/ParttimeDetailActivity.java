package com.pricecode;

import java.util.HashMap;

import com.pricecode.*;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class ParttimeDetailActivity extends CMActivity
{
	TextView m_tvSubject, m_tvDate, m_tvContent;

	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_parttime_detail);
		
		setTitleBarText("알바정보");
	    
		m_tvSubject = (TextView) findViewById(R.id.textView_subject);
		m_tvDate = (TextView) findViewById(R.id.textView_date);
		m_tvContent = (TextView) findViewById(R.id.textView_content);

		@SuppressWarnings("unchecked")
        HashMap<String, String> infoMap = (HashMap<String, String>) getIntent().getSerializableExtra("item_info");
	    if(infoMap == null)
	    {
	    	showToast("알바정보가 없습니다.");
	    	return;
	    }
	    
		String strSubject = null, strDate = null, strContent = null;
		strSubject = infoMap.get("subject");
		strDate = infoMap.get("rdate");
		strContent = infoMap.get("content");

		if( strSubject == null )
			strSubject = "";
		if( strDate == null )
			strDate = "";
		if( strContent == null )
			strContent = "";

		m_tvSubject.setText(strSubject);
		m_tvDate.setText(strDate);
		m_tvContent.setText(Html.fromHtml(strContent));
    }

	@Override
    protected void onDestroy()
    {
	    super.onDestroy();
    }
}

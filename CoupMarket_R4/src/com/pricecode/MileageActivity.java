package com.pricecode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;

public class MileageActivity extends CMActivity implements OnClickListener
{
	
	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_mileage);
		
		initTopMainBar(0);
		initBottomTabBar(R.id.tab_imageView_my_page);
	    
	    findViewById(R.id.imageButton_my_mileage_info).setOnClickListener(this);
	    findViewById(R.id.imageButton_show_benefit).setOnClickListener(this);
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
			
			cancelWait();
        }
	};

	@Override
    public void onClick( View v )
    {
	    Intent intent = null;
	    if(v.getId() == R.id.imageButton_my_mileage_info)
	    	intent = CMManager.getIntent(this, MyMileageInfoActivity.class);
	    else if(v.getId() == R.id.imageButton_show_benefit)
	    	intent = CMManager.getIntent(this, ShowBenefitActivity.class);
	    if(intent == null)
	    	return;
	    
	    startActivity(intent);
    }

}

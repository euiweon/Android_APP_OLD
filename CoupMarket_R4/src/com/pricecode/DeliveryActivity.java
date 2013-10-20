package com.pricecode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;

public class DeliveryActivity extends CMActivity implements OnClickListener
{

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery);

		initTopMainBar(R.id.top_imageView_delivery);
		initBottomTabBar(0);

		int buttonIds[] = { R.id.imageButton_chicken, R.id.imageButton_pizza, R.id.imageButton_china,
		                   R.id.imageButton_korea, R.id.imageButton_pig, R.id.imageButton_etc, };
		for(int nId : buttonIds)
			findViewById(nId).setOnClickListener(this);
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
			if( m_Handler == null )
				return;

			cancelWait();
		}
	};

	@Override
    public void onClick( View v )
    {
	    int nId = v.getId();
	    
	    String strCategory = "";
	    int nCategory = 0;
	    switch(nId)
	    {
	    	case R.id.imageButton_chicken:
	    		strCategory = "치킨";
	    		nCategory = 1;
	    		break;
	    	case R.id.imageButton_pizza:
	    		strCategory = "피자";
	    		nCategory = 2;
	    		break;
	    	case R.id.imageButton_china:
	    		strCategory = "중국집";
	    		nCategory = 3;
	    		break;
	    	case R.id.imageButton_korea:
	    		strCategory = "한식/분식";
	    		nCategory = 4;
	    		break;
	    	case R.id.imageButton_pig:
	    		strCategory = "족발/보쌈";
	    		nCategory = 5;
	    		break;
	    	case R.id.imageButton_etc:
	    		strCategory = "기타";
	    		nCategory = 6;
	    		break;
	    	default:
	    		return;
	    }
	    
	    Intent intent = CMManager.getIntent(this, DeliveryListActivity.class);
	    intent.putExtra("category_name", strCategory);
	    intent.putExtra("category_no", nCategory);
	    startActivity(intent);
    }

}

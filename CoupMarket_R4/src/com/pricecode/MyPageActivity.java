package com.pricecode;

import com.pricecode.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MyPageActivity extends CMActivity implements OnClickListener
{
	ImageButton m_btnCMarketHistory, m_btnGiftconHistory, m_btnMiliage;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_page);
		
		initTopMainBar(0);
		initBottomTabBar(R.id.tab_imageView_my_page);

		m_btnCMarketHistory = (ImageButton) findViewById(R.id.imageButton_cmarket_history);
		m_btnGiftconHistory = (ImageButton) findViewById(R.id.imageButton_giftcon_history);
		m_btnMiliage= (ImageButton) findViewById(R.id.imageButton_mileage);
		m_btnCMarketHistory.setOnClickListener(this);
		m_btnGiftconHistory.setOnClickListener(this);
		m_btnMiliage.setOnClickListener(this);
	}
	
	@Override
    protected void onDestroy()
    {
	    super.onDestroy();
    }

	@Override
	public void onClick( View v )
	{
		Intent intent = null;
		switch( v.getId() )
		{
			case R.id.imageButton_cmarket_history:
				intent = CMManager.getIntent(MyPageActivity.this, CMarketHistoryActivity.class);
				break;

			case R.id.imageButton_giftcon_history:
				intent = CMManager.getIntent(MyPageActivity.this, GiftconHistoryActivity.class);
				break;
				
			case R.id.imageButton_mileage:
				intent = CMManager.getIntent(MyPageActivity.this, MileageActivity.class);
				break;
				
		}

		if( intent == null )
			return;

		startActivity(intent);
	}
}

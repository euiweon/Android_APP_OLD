package com.pricecode;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.pricecode.*;
import com.rangboq.xutil.XActivity;

public class CMActivity extends XActivity
{
	CMActivity baseself = null;
	public void setTitleBarText(String strTitle)
	{
		TextView tvTitle = (TextView) findViewById(R.id.top_textView_title);
		if(tvTitle == null)
			return;
		tvTitle.setText(strTitle);
	}
	
	protected int m_nBarButtonId = 0;
	protected void initTopMainBar(int nMyId)
	{
		int itemIds[] = {
		                // R.id.top_imageView_giftcon,
		                 R.id.top_imageView_delivery,
		                 R.id.top_imageView_cmarket,
		};
		
		ImageView ivButton = null;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);
			ivButton.setOnClickListener(m_OnBarClickListener);
			if(nId == nMyId)
			{
				m_nBarButtonId = nMyId;
				ivButton.setSelected(true);
			}
		}
	}

	protected void initBottomTabBar(int nMyId)
	{
		int itemIds[] = {
						R.id.tab_imageView_gift,
		                 R.id.tab_imageView_parttime,
		                 R.id.tab_imageView_my_page,
		                 //R.id.tab_imageView_mileage,
		                 R.id.tab_imageView_option,
		};
		
		ImageView ivButton = null;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);
			ivButton.setOnClickListener(m_OnBarClickListener);
			if(nId == nMyId)
			{
				m_nBarButtonId = nMyId;
				ivButton.setSelected(true);
			}
		}
	}
	
	public void refreshView()
	{
	}
	
	private OnClickListener m_OnBarClickListener = new OnClickListener()
	{
		@Override
		public void onClick( View v )
		{
			onBarButtonClick(v);
		}
	};

    public void onBarButtonClick( View v )
    {
	    int nId = v.getId();
	    if(nId == m_nBarButtonId)
	    {
	    	if(m_nBarButtonId == R.id.top_imageView_cmarket)
	    		refreshView();
	    	return;
	    }
	    String id = CMManager.getPrefString(CMManager.PREF_USER_ID, "") ;
	    Intent intent = null;
	    switch(nId)
	    {
	    	/*case R.id.top_imageView_giftcon:
	    		intent = CMManager.getIntent(this, GiftconActivity.class);
	    		break;-*/
		    case R.id.tab_imageView_gift:
		    	
		    	if((this instanceof CMarketActivity || this instanceof ParttimeActivity ||  this instanceof  ParttimeDetailActivity)&&(id == null || id.equals("")) ) 
		    	{
		    		new AlertDialog.Builder(this)

		    		
		    		.setTitle("에러")
		    		.setMessage("로그인을 하시겠습니까?")
		    		.setPositiveButton("확인", new DialogInterface.OnClickListener()
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
		    		return;
		    	}
		    	else
		    	{
		    		intent = CMManager.getIntent(this, GiftconActivity.class);
		    	}
		    	
		    	
		    	break;
	    	case R.id.top_imageView_delivery:
	    		if((this instanceof CMarketActivity || this instanceof ParttimeActivity ||  this instanceof  ParttimeDetailActivity)&&(id == null || id.equals("")) ) 
		    	{
	    			new AlertDialog.Builder(this)

		    		
		    		.setTitle("에러")
		    		.setMessage("로그인을 하시겠습니까?")
		    		.setPositiveButton("확인", new DialogInterface.OnClickListener()
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
		    		return;
		    	}
		    	else
		    	{
		    		intent = CMManager.getIntent(this, DeliveryActivity.class);
		    	}

	    		break;
	    	case R.id.top_imageView_cmarket:
	    		
	    		if(( this instanceof ParttimeActivity ||  this instanceof  ParttimeDetailActivity)&&(id == null || id.equals("")) ) 
		    	{
	    			intent = CMManager.getIntent(this, CMarketActivity.class);
		    	}
		    	else
		    	{
		    		intent = CMManager.getIntent(this, CMarketActivity.class);
		    	}
	    		
	    		break;

	    	case R.id.tab_imageView_parttime:
	    		intent = CMManager.getIntent(this, ParttimeActivity.class);
	    		break;
	    	case R.id.tab_imageView_my_page:
	    		if((this instanceof CMarketActivity || this instanceof ParttimeActivity ||  this instanceof  ParttimeDetailActivity)&&(id == null || id.equals("")) ) 
		    	{
	    			new AlertDialog.Builder(this)

		    		
		    		.setTitle("에러")
		    		.setMessage("로그인을 하시겠습니까?")
		    		.setPositiveButton("확인", new DialogInterface.OnClickListener()
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
		    		return;
		    	}
		    	else
		    	{
		    		intent = CMManager.getIntent(this, MyPageActivity.class);
		    	}
	    		
	    		break;
	    	/*case R.id.tab_imageView_mileage:
	    		intent = CMManager.getIntent(this, MileageActivity.class);
	    		break;*/
	    	case R.id.tab_imageView_option:
	    		if((this instanceof CMarketActivity || this instanceof ParttimeActivity ||  this instanceof  ParttimeDetailActivity)&&(id == null || id.equals("")) ) 
		    	{
		    		
	    			new AlertDialog.Builder(this)

		    		
		    		.setTitle("에러")
		    		.setMessage("로그인을 하시겠습니까?")
		    		.setPositiveButton("확인", new DialogInterface.OnClickListener()
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
		    		return;
		    	}
		    	else
		    	{
		    		intent = CMManager.getIntent(this, OptionActivity.class);
		    	}
	    		
	    		break;
	    }
	    
	    if(intent != null)
	    	startActivity(intent);
    }

	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    CMManager.setContext(this);
	    
	    baseself = this;
	    
	    if(this instanceof LoginActivity)
	    	return;
	    if(this instanceof IntroActivity)
	    	return;
	    if(this instanceof MemberInfoActivity)
	    	return;
	    if(this instanceof SearchIdPasswordActivity)
	    	return;
	    if(this instanceof SearchPostcodeActivity)
	    	return;
	    if(this instanceof ShowAgreementActivity)
	    	return;
	    
	    //CMManager.checkOnline(this, true);
    }
	
	public static String getMD5Hash(String s) {
        MessageDigest m = null;
        String hash = null;
   	
        try {
       	 m = MessageDigest.getInstance("MD5");
       	 m.update(s.getBytes(),0,s.length());
   	     hash = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
       	 e.printStackTrace();
        }
   	
        return hash;
   }
	

	@Override
    protected void onDestroy()
    {
	    // TODO Auto-generated method stub
	    super.onDestroy();
    }

	@Override
    protected void onPause()
    {
	    // TODO Auto-generated method stub
	    super.onPause();
    }

	@Override
    protected void onResume()
    {
	    // TODO Auto-generated method stub
	    super.onResume();
    }

	void toastCheckInternet()
	{
		showToast("인터넷 연결을 확인하여 주십시오.");
	}
}

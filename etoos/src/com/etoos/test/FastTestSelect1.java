package com.etoos.test;

import java.util.ArrayList;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FastTestSelect1 extends EtoosBaseActivity  implements OnClickListener {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fastselect);


		AfterCreate();
		BtnEvent(R.id.main_tab_1);
		BtnEvent(R.id.main_tab_2);
		BtnEvent(R.id.main_tab_3);
		BtnEvent(R.id.main_tab_4);
		BtnEvent(R.id.main_tab_5);
		BtnEvent(R.id.main_tab_6);
		BtnEvent(R.id.main_tab_7);
		BtnEvent(R.id.title_icon);
		BtnEvent(R.id.title_icon2);
	}


	public void BtnEvent( int id )
	{
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		AppManagement _AppManager = (AppManagement) getApplication();
		switch(arg0.getId())
		{
		case R.id.title_icon:
		{
			onBackPressed();
		}

		break;
		case R.id.title_icon2:
		{
			_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examiner/examiner.jsp";
			Intent intent;
			intent = new Intent().setClass(baseself, WebActivity.class);
			startActivity( intent ); 

		}
		break;
		case R.id.main_tab_1:
		{

			_AppManager.m_FastSelectIndex = 0;
			Intent intent;
			intent = new Intent().setClass(baseself, FastTestSelect2.class);
			startActivity( intent ); 


		}
		break; 
		case R.id.main_tab_2:
		{
			_AppManager.m_FastSelectIndex = 1;
			Intent intent;
			intent = new Intent().setClass(baseself, FastTestSelect2.class);
			startActivity( intent ); 
		}
		break;
		case R.id.main_tab_3:
		{
			_AppManager.m_FastSelectIndex = 2;
			Intent intent;
			intent = new Intent().setClass(baseself, FastTestSelect2.class);
			startActivity( intent ); 
		}
		break;
		case R.id.main_tab_4:
		{
			_AppManager.m_FastSelectIndex = 3;
			Intent intent;
			intent = new Intent().setClass(baseself, FastTestSelect2.class);
			startActivity( intent ); 
		}
		break;
		case R.id.main_tab_5:
		{
			_AppManager.m_FastSelectIndex = 4;
			Intent intent;
			intent = new Intent().setClass(baseself, FastTestSelect2.class);
			startActivity( intent ); 
		}
		break;
		case R.id.main_tab_6:
		{
			_AppManager.m_FastSelectIndex = 5;
			Intent intent;
			intent = new Intent().setClass(baseself, FastTestSelect2.class);
			startActivity( intent ); 
		}
		break;
		case R.id.main_tab_7:
		{
			_AppManager.m_FastSelectIndex = 6;
			Intent intent;
			intent = new Intent().setClass(baseself, FastTestSelect2.class);
			startActivity( intent ); 
		}
		break;
		}

		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("test1", Activity.MODE_PRIVATE);

		SharedPreferences.Editor sharedEditor = sharedPref.edit();
		sharedEditor.putInt("test", _AppManager.m_FastSelectIndex);

		sharedEditor.commit();

	}



}

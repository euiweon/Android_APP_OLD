package com.etoos.test;

import android.os.Bundle;






import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etoos.data.OMRContent;
import com.euiweonjeong.base.BitmapManager;

import com.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContinueActivity2 extends EtoosBaseActivity implements OnClickListener {

	ContinueActivity2 self;

	ArrayList< OMRContent > m_ListData = new ArrayList< OMRContent >();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.continue_test);

		AfterCreate();
		self = this;


		BtnEvent(R.id.bottom_1);
		BtnEvent(R.id.bottom_3);
		
		{
			SharedPreferences sharedPref;
			sharedPref = getSharedPreferences("test1", Activity.MODE_PRIVATE);
			
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_FastSelectIndex= sharedPref.getInt("sub", 0);
			
			((TextView)findViewById(R.id.test_name)).setText(sharedPref.getString("title", ""));
			((TextView)findViewById(R.id.answer_time)).setText(sharedPref.getString("time", "10:00"));
			
			
			
			m_ListData.clear();
			String strJSON = sharedPref.getString("sdata", "");
			try 
			{
				JSONObject json = new JSONObject(strJSON);
				JSONArray usageList = (JSONArray)json.get("data");
				for(int i = 0; i < usageList.length(); i++)
				{
					OMRContent content = new OMRContent();
					JSONObject list = (JSONObject)usageList.get(i);
					content.Number = list.optInt("Number");
					content.AnswerString = list.optString("AnswerString");
					content.QuestionType = list.optInt("QuestionType");
					content.AnswerCount = list.optInt("AnswerCount");
					
					JSONArray usageList2 = (JSONArray)list.get("Answer");
					
					for ( int j =0 ; j < content.Answer.length; j++)
					{
						content.Answer[j] = usageList2.getInt(j);
					}
					
					
					content.Later = list.optBoolean("Later");
					content.refQuestion = list.optInt("refQuestion");
					content.Timer = list.optString ("Timer");
					
					JSONArray usageList3 = (JSONArray)list.get("AnswerCheck");
					for ( int j =0 ; j < content.AnswerCheck.length; j++)
					{
						content.AnswerCheck[j] = usageList3.getInt(j);
					}
					content.Score = list.optInt ("Score");
					
					m_ListData.add(content);
				}
				
				
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block

				e.printStackTrace();
			} 
			
			
			
			int noblackcount = 0;
			int blackcount = 0;
			for( int i = 0 ; i < m_ListData.size(); i++ )
			{
				boolean check = false; 

				if (  m_ListData.get(i).QuestionType == 0)
				{
					for ( int j = 0 ;  j< m_ListData.get(i).Answer.length ; j++  )
					{
						if ( m_ListData.get(i).Answer[j] >= 2 )
						{
							check = true;
						}
					}
					if ( !check )
					{
						noblackcount++;
					}
					else
					{
						blackcount++;
					}
				}

				// 주관식일 경우 풀었는지....
				else
				{
					if ( m_ListData.get(i).AnswerString.equals("") )
					{
						noblackcount++;
					}
					else
					{
						blackcount++;
					}
				}

			}
			
			((TextView)findViewById(R.id.answer_count)).setText(Integer.toString(noblackcount));
		}
	}


	public void BtnEvent( int id )
	{
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
	}

	@Override
	public void onResume()
	{
		super.onResume();

	}




	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch( arg0.getId())
		{
		case R.id.bottom_1:
		{
			new AlertDialog.Builder(this)
			.setMessage("이어서 보기를 취소하고, 새로운 시험을 보시겠습니까?") //줄였음
			.setPositiveButton("예", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{   
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/Quick.jsp";
					Intent intent;
					intent = new Intent().setClass(baseself, WebActivity.class);
					startActivity( intent ); 
					

				}
			})
			.setNegativeButton("아니요", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					//...할일
				}
			})
			.show();
		}
		break;

		case R.id.bottom_3:
		{
			new AlertDialog.Builder(this)
			.setMessage("시험보기를 계속 하시겠습니까") //줄였음
			.setPositiveButton("예", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{   
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.FastTestLoad  =true;
					Intent intent;
					intent = new Intent().setClass(baseself, FastTestMain.class);
					startActivity( intent );
				}
			})
			.setNegativeButton("아니요", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{

				}
			})
			.show();
		}
		break;

		}

	}

}

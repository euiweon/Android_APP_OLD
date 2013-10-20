package com.etoos.test;



import java.util.ArrayList;

import com.etoos.data.OMRHistoryContent;
import com.etoos.view.FastResultItem_0;
import com.etoos.view.FastResultItem_0_2;
import com.etoos.view.FastResultItem_0_3;
import com.etoos.view.FastResultItem_1;
import com.etoos.view.FastResultItem_2;
import com.etoos.view.FastResultItem_3;
import com.etoos.view.FastResultItem_4;
import com.etoos.view.FastResultItem_4_1;
import com.etoos.view.FastResultItem_dummy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;



public class FastTestResult2 extends EtoosBaseActivity implements OnClickListener{

	FastTestResult2 self;

	ArrayList <View>	m_ExpainList = new ArrayList <View>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fastresult_2);

		self = this;


		AfterCreate();

		RefreshUI();

		BtnEvent(R.id.title_icon);
		BtnEvent(R.id.bottom_1);
		BtnEvent(R.id.bottom_3);
	}


	public void RefreshUI()
	{
		m_ExpainList.clear();
		TestInfomation();
		{
			FastResultItem_0_3 item = new FastResultItem_0_3(self);
			item.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					FastResultItem_0_3 item = (FastResultItem_0_3)(arg0);
					item.HideView();

					for ( int i = 0 ; i < m_ExpainList.size() ; i ++ )
					{
						if ( item.m_Hide )
						{
							m_ExpainList.get(i).setVisibility(View.GONE);
						}
						else
						{
							m_ExpainList.get(i).setVisibility(View.VISIBLE);
						}
					}
				}

			});
			((LinearLayout)findViewById(R.id.data_view)).addView(item);
		}

		// History 체크 
		TestCheckHistory();



		{
			FastResultItem_dummy item = new FastResultItem_dummy(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);
		}


		// 각 문항별로 History 
		TestHistoryDetail();

		{
			FastResultItem_dummy item = new FastResultItem_dummy(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);

		}

		SetFont();
	}

	public void TestInfomation()
	{
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("test1", Activity.MODE_PRIVATE);
		Integer Index = sharedPref.getInt("sub", 0);

		{
			AppManagement _AppManager = (AppManagement) getApplication();
			String Title = "["+ _AppManager.FastTestGrade + "]";
			String Subject = "";

			
			


			switch (Index)
			{
			case 0:				Subject = ("국어 A형");				break;
			case 1:				Subject = ("국어 B형");				break;
			case 2:				Subject = ("수학 A형");				break;
			case 3:				Subject = ("수학 B형");				break;
			case 4:				Subject = ("영어 A형");				break;
			case 5:				Subject = ("영어 B형");				break;
			case 6:				Subject = ("사회탐구 / 과학탐구 ");	break;
			}

			FastResultItem_0 item = new FastResultItem_0(self);

			item.SetData(Title , Subject , "");


			((LinearLayout)findViewById(R.id.data_view)).addView(item);
		}

		{
			FastResultItem_dummy item = new FastResultItem_dummy(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);
		}

		{
			AppManagement _AppManager = (AppManagement) getApplication();
			FastResultItem_0_2 item = new FastResultItem_0_2(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);

			String Time = "" ;

			{
				String[] totalTime = _AppManager.FastTimer.split(":");
				if (  Integer.parseInt(totalTime[0])/60 != 0)
				{
					Time  = Integer.toString(  Integer.parseInt(totalTime[0])/60);
					Time += "시간 ";
				}
				
				{
					Integer Min=  Integer.parseInt(totalTime[0])%60;
					Time += Min.toString();
					Time += "분 / ";
				}
				
			}
			switch (Index)
			{
			case 0:				Time += ("1시간 20분");				break;
			case 1:				Time += ("1시간 20분");				break;
			case 2:				Time += ("1시간 40분");				break;
			case 3:				Time += ("1시간 40분");				break;
			case 4:				Time += ("1시간 10분");				break;
			case 5:				Time += ("1시간 10분");				break;
			case 6:				Time += ("20분");				break;
			}


			item.SetData("응시시간",  Time);



		}

		{

			FastResultItem_0_2 item = new FastResultItem_0_2(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);

			AppManagement _AppManager = (AppManagement) getApplication();
			String Making = _AppManager.FastTestmakingCount + "문항 /";



			switch (_AppManager.m_FastSelectIndex)
			{
			case 0:				Making += "45문항";				break;
			case 1:				Making += "45문항";				break;
			case 2:				Making += "30문항";				break;
			case 3:				Making += "30문항";				break;
			case 4:				Making += "50문항";				break;
			case 5:				Making += "50문항";				break;
			case 6:				Making += "20문항";				break;
			}
			item.SetData("마킹문항",  Making);
		}

		{
			Integer Total = 0;
			FastResultItem_0_2 item = new FastResultItem_0_2(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);

			AppManagement _AppManager = (AppManagement) getApplication();

			String[] totalTime1= _AppManager.FastTimer.split(":");

			Total = Integer.parseInt(totalTime1[0])  *60 ;
			Total += Integer.parseInt(totalTime1[1])  ;
			Integer Output= 0;
			if ( _AppManager.FastTestmakingCount != 0 )
			{
				Output = (Total / _AppManager.FastTestmakingCount);
			}
			
			item.SetData("문항평균 풀이시간", Output.toString()+ "초");
		}
	}
	public void TestCheckHistory()
	{


		{
			FastResultItem_dummy item = new FastResultItem_dummy(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);
			m_ExpainList.add(item);
		}


		AppManagement _AppManager = (AppManagement) getApplication();

		int counter  = 0;
		ArrayList< OMRHistoryContent > HistoryListData = null ;
		for ( int i = 0 ; i < _AppManager.m_FastHistoryListData.size() ; i ++ )
		{
			if ( counter == 0  )
				HistoryListData = new ArrayList< OMRHistoryContent >();
			
			// 객관식일 경우만 추가 해준다. 
			if ( _AppManager.m_FastHistoryListData.get(i).AnswerString.equals(""))
			{
				HistoryListData.add(  _AppManager.m_FastHistoryListData.get(i) );
				counter++;
			}
			

			if ( HistoryListData.size() >= 9 )
			{
				{
					FastResultItem_1 item = new FastResultItem_1(self);
					((LinearLayout)findViewById(R.id.data_view)).addView(item);

					item.SetData(HistoryListData);
					m_ExpainList.add(item);
				}

				{
					FastResultItem_2 item = new FastResultItem_2(self);
					((LinearLayout)findViewById(R.id.data_view)).addView(item);


					item.SetData(HistoryListData);

					m_ExpainList.add(item);
				}

				counter = 0; 
			}


		}

		if ( HistoryListData.size() < 9 &&  HistoryListData.size() != 0 )
		{
			{
				FastResultItem_1 item = new FastResultItem_1(self);
				((LinearLayout)findViewById(R.id.data_view)).addView(item);
				item.SetData(HistoryListData);
				m_ExpainList.add(item);
			}

			{
				FastResultItem_2 item = new FastResultItem_2(self);
				((LinearLayout)findViewById(R.id.data_view)).addView(item);
				item.SetData(HistoryListData);

				m_ExpainList.add(item);
			}
		}
	}

	public void TestHistoryDetail()
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		{
			FastResultItem_3 item = new FastResultItem_3(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);
		}
		{


			// 루프를 돌면서 번호 순서대로 정렬 해서 리스트에 넣어준다. 
			for ( int j = 0  ; j  < _AppManager.m_FastListData.size() ;  j++)
			{
				ArrayList< OMRHistoryContent > tempList = new ArrayList< OMRHistoryContent > ();
				tempList.clear();
				for ( int  i = 0 ; i < _AppManager.m_FastHistoryListData.size() ; i++)
				{
					if ( j+ 1 == _AppManager.m_FastHistoryListData.get(i).Number)
					{
						// 객관식일 경우만 추가 
						if ( _AppManager.m_FastHistoryListData.get(i).AnswerString.equals(""))
							tempList.add(_AppManager.m_FastHistoryListData.get(i));
					}
				}

				if ( tempList.size() != 0)
				{

					String tempString = "";
					for ( int c = 0 ; c <  _AppManager.m_FastListData.get(j).Answer.length ; c++)
					{
						if (_AppManager.m_FastListData.get(j).Answer[c] > 1)
						{
							tempString +=_AppManager.m_FastListData.get(j).Answer[c] + " ";
						}
						
					}
					
					
					if ( !tempString.equals(""))
					{
						FastResultItem_4 item = new FastResultItem_4(self);
						((LinearLayout)findViewById(R.id.data_view)).addView(item);


						ArrayList<View> viewList = new ArrayList<View>();


						// 4개씩 리스트를 짤라서.. 
						ArrayList< OMRHistoryContent > tempList2 = new ArrayList< OMRHistoryContent > ();

						int counter = 0;
						for ( int k = 0; k < tempList.size() ; k++ )
						{
							tempList2.add(tempList.get(k));
							counter++;

							if ( counter > 3)
							{

								FastResultItem_4_1 item1 = new FastResultItem_4_1(self);
								item1.setVisibility(View.GONE);
								((LinearLayout)findViewById(R.id.data_view)).addView(item1);
								item1.SetData(tempList2);
								viewList.add(item1);



								counter = 0; 
							}

						}
						if ( counter != 0)
						{
							FastResultItem_4_1 item1 = new FastResultItem_4_1(self);
							item1.setVisibility(View.GONE);
							((LinearLayout)findViewById(R.id.data_view)).addView(item1);
							item1.SetData(tempList2);
							viewList.add(item1);
						}

						item.SetData(_AppManager.m_FastListData.get(j).Number , _AppManager.m_FastListData.get(j).Answer, viewList, tempList);
						item.setOnClickListener(new OnClickListener()
						{

							@Override
							public void onClick(View arg0) {
								FastResultItem_4 item = (FastResultItem_4)(arg0);
								item.ShowDetail();

							}

						});
					}
					
					


				}

			}


			/*ArrayList<View> viewList = new ArrayList<View>();
			for ( int i = 0 ; i < 3  ; i++)
			{
				FastResultItem_4_1 item1 = new FastResultItem_4_1(self);
				item1.setVisibility(View.GONE);
				((LinearLayout)findViewById(R.id.data_view)).addView(item1);
				viewList.add(item1);
			}
			item.SetData("test", "Index", "Name", viewList);


			item.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					FastResultItem_4 item = (FastResultItem_4)(arg0);
					item.ShowDetail();

				}

			});*/
		}
	}

	@Override
	public void onBackPressed() 
	{

		new AlertDialog.Builder(this)
		.setTitle("채점 확인")
		.setMessage("나중에 채점 하시겠습니까?") 
		.setPositiveButton("예", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{   
				AppManagement _AppManager = (AppManagement) getApplication();
				_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
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

	public void BtnEvent( int id )
	{
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch( arg0.getId())
		{
		case R.id.title_icon:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
			Intent intent;
			intent = new Intent().setClass(baseself, WebActivity.class);
			startActivity( intent ); 
		}
		break; 
		case R.id.bottom_1:
		{

			AppManagement _AppManager = (AppManagement) getApplication();
			if (_AppManager.m_LoginCheck)
			{
				new AlertDialog.Builder(this)

				.setMessage("빠른 응시 결과가 저장되었습니다. \nMy보관함>성적표에서 채점 가능합니다") 
				.setPositiveButton("확인", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
						Intent intent;
						intent = new Intent().setClass(baseself, WebActivity.class);
						startActivity( intent ); 
					}
				})

				.show();
			}
			else
			{
				new AlertDialog.Builder(this)
				.setMessage("빠른 응시 결과를 저장하려면 로그인해야 합니다.") 
				.setPositiveButton("로그인", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/login/login.jsp";
						Intent intent;
						intent = new Intent().setClass(baseself, ResultWebActivity.class);
						startActivity( intent ); 
					}
				})
				.setNegativeButton("무시", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton) 
					{
						//...할일
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
						Intent intent;
						intent = new Intent().setClass(baseself, WebActivity.class);
						startActivity( intent ); 
					}
				})
				.show();
			}



		}
		break;
		case R.id.bottom_3:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			if (_AppManager.m_LoginCheck)
			{
				Intent intent;
				intent = new Intent().setClass(baseself, FastTestResult1.class);
				startActivity( intent ); 
			}
			else
			{
				new AlertDialog.Builder(this)
				.setTitle("채점 확인")
				.setMessage("응시결과를 저장하고 채점을 하려면 로그인해야 합니다.") 
				.setPositiveButton("로그인 함", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/login/login.jsp";
						Intent intent;
						intent = new Intent().setClass(baseself, ResultWebActivity.class);
						startActivity( intent ); 
					}
				})
				.setNegativeButton("저장/채점 안함", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton) 
					{
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
						Intent intent;
						intent = new Intent().setClass(baseself, WebActivity.class);
						startActivity( intent ); 
					}
				})
				.show();
			}

		}
		break;

		}

	}



}

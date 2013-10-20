package com.etoos.test;

import java.util.ArrayList;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FastTestSelect2 extends EtoosBaseActivity  implements OnClickListener {


	String TestName;
	String TesterName;
	Integer TesterSex = 0 ;
	String TesterSchool;
	String TesterInstitute;
	String TesterGrade = "";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fastselect2);


		AfterCreate();

		BtnEvent(R.id.select_male);
		BtnEvent(R.id.name_3_1);
		BtnEvent(R.id.name_3_2);
		BtnEvent(R.id.select_female);
		BtnEvent(R.id.test_grade);
		BtnEvent(R.id.ok_btn);

		BtnEvent(R.id.title_icon);

		((TextView)findViewById(R.id.test_grade)).setText("고1");

		{
			AppManagement _AppManager = (AppManagement) getApplication();
			if (_AppManager.m_LoginCheck)
			{
				TesterGrade = _AppManager.m_LoginData.gname;
				((EditText)findViewById(R.id.tester_name)).setText(_AppManager.m_LoginData.name);
				if ( _AppManager.m_LoginData.gname.equals(""))
				{
					((TextView)findViewById(R.id.test_grade)).setText("고1");
				}
				else
				{
					((TextView)findViewById(R.id.test_grade)).setText(_AppManager.m_LoginData.gname);
				}
				
				((EditText)findViewById(R.id.tester_school)).setText(_AppManager.m_LoginData.shname);
				((EditText)findViewById(R.id.tester_institute)).setText(_AppManager.m_LoginData.eduinstitute1);

				if ( _AppManager.m_LoginData.gender.equals("1"))
				{
					TesterSex = 1;
					((ImageView)findViewById(R.id.select_female)).setBackgroundResource(R.drawable.r_1_bt);
					((ImageView)findViewById(R.id.select_male)).setBackgroundResource(R.drawable.r_2_bt);
				}
				else if ( _AppManager.m_LoginData.gender.equals("2"))
				{
					TesterSex = 2;
					((ImageView)findViewById(R.id.select_female)).setBackgroundResource(R.drawable.r_2_bt);
					((ImageView)findViewById(R.id.select_male)).setBackgroundResource(R.drawable.r_1_bt);
				}
				else
				{
					TesterSex = 0;
					((ImageView)findViewById(R.id.select_female)).setBackgroundResource(R.drawable.r_1_bt);
					((ImageView)findViewById(R.id.select_male)).setBackgroundResource(R.drawable.r_1_bt);
				}
			}
		}

	}


	public void BtnEvent( int id )
	{
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		switch(arg0.getId())
		{
		case R.id.title_icon:
		{
			onBackPressed();
		}

		break;

		case R.id.name_3_1:
		case R.id.select_male:
		{
			TesterSex = 1;
			((ImageView)findViewById(R.id.select_female)).setBackgroundResource(R.drawable.r_1_bt);
			((ImageView)findViewById(R.id.select_male)).setBackgroundResource(R.drawable.r_2_bt);
			
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_institute)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.test_name)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_name)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_school)).getWindowToken(), 0);
		}
		break; 
		case R.id.name_3_2:
		case R.id.select_female:
		{
			TesterSex = 2;
			((ImageView)findViewById(R.id.select_female)).setBackgroundResource(R.drawable.r_2_bt);
			((ImageView)findViewById(R.id.select_male)).setBackgroundResource(R.drawable.r_1_bt);
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_institute)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.test_name)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_name)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_school)).getWindowToken(), 0);
		}
		break;
		case R.id.test_grade:
		{
			TesterSchool =((EditText)findViewById(R.id.tester_school)).getText().toString();
			
			if ( TesterSchool.indexOf("여중")>= 0 )
			{
				final String[] Grade = {"중1", "중2", "중3","기타" };
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				alt_bld.setTitle("맞는 학년을 선택해주세요");
				alt_bld.setSingleChoiceItems(Grade, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						TesterGrade =Grade[item];
						((TextView)findViewById(R.id.test_grade)).setText(TesterGrade);
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.show();
			}
			else if (TesterSchool.indexOf("여고")>= 0|| TesterSchool.indexOf("고등학교")>= 0|| 
					 TesterSchool.indexOf("고교")>= 0|| TesterSchool.indexOf("외고")>= 0||
					 TesterSchool.indexOf("과학고")>= 0|| TesterSchool.indexOf("예고")>= 0||
					 TesterSchool.indexOf("공고")>= 0|| TesterSchool.indexOf("상고")>= 0||
					 TesterSchool.indexOf("인터넷고")>= 0)
			{
				final String[] Grade = {"고1", "고2", "고3","재수생", "삼수이상", "기타" };
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				alt_bld.setTitle("맞는 학년을 선택해주세요");
				alt_bld.setSingleChoiceItems(Grade, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						TesterGrade =Grade[item];
						((TextView)findViewById(R.id.test_grade)).setText(TesterGrade);
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.show();
			}
			else if (TesterSchool.indexOf("중학")>= 0)
			{
				final String[] Grade = {"중1", "중2", "중3","기타" };
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				alt_bld.setTitle("맞는 학년을 선택해주세요");
				alt_bld.setSingleChoiceItems(Grade, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						TesterGrade =Grade[item];
						((TextView)findViewById(R.id.test_grade)).setText(TesterGrade);
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.show();
			}
			else if (TesterSchool.indexOf("고등")>= 0)
			{
				final String[] Grade = {"고1", "고2", "고3", "재수생", "삼수이상", "기타" };
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				alt_bld.setTitle("맞는 학년을 선택해주세요");
				alt_bld.setSingleChoiceItems(Grade, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						TesterGrade =Grade[item];
						((TextView)findViewById(R.id.test_grade)).setText(TesterGrade);
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.show();
			}
			else
			{
				final String[] Grade = {"고1", "고2", "고3","중1", "중2", "중3", "재수생", "삼수이상", "기타" };
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				alt_bld.setTitle("맞는 학년을 선택해주세요");
				alt_bld.setSingleChoiceItems(Grade, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						TesterGrade =Grade[item];
						((TextView)findViewById(R.id.test_grade)).setText(TesterGrade);
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.show();
			}
			
			

		}
		break;
		case R.id.ok_btn:
		{
			TestName = ((EditText)findViewById(R.id.test_name)).getText().toString();
			TesterName = ((EditText)findViewById(R.id.tester_name)).getText().toString();
			TesterSchool = ((EditText)findViewById(R.id.tester_school)).getText().toString();
			TesterInstitute = ((EditText)findViewById(R.id.tester_institute)).getText().toString();

			if ( TestName == null || TestName.equals(""))
			{
				baseself.ShowAlertDialLog(baseself, "시험명을 입력해주세요. ");
			}
			else
			{
				String ResultString  = "시험명 : " +  TestName;
				ResultString  += "\n이름 : " +  TesterName;
				ResultString  += "\n성별 : ";
				if ( TesterSex ==  0 )
				{
					ResultString += "선택하지 않음\n";
				}
				else if ( TesterSex ==  1 )
				{
					ResultString += "남자\n";
				}
				else if ( TesterSex ==  2 )
				{
					ResultString  += "여자\n";
				}
				ResultString  += "학년 : ";

				if ( ((TextView)findViewById(R.id.test_grade)).getText().equals("선택하세요")) 
				{
					ResultString  += "선택하지 않음\n";
				}
				else
				{
					ResultString  +=((TextView)findViewById(R.id.test_grade)).getText() + "\n";
				}
				ResultString += "학교명 : " +TesterSchool;
				ResultString += "\n학원명 : " +TesterInstitute;

				ResultString += "\n\n입력하신 정보가 맞습니까? "; 


				new AlertDialog.Builder(baseself)
				.setTitle("입력 확인")
				.setMessage(ResultString) //줄였음
				.setPositiveButton("예", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						Intent intent;
						intent = new Intent().setClass(baseself, FastTestMain.class);
						startActivity( intent ); 

						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.FastTestTitle = TestName;
						_AppManager.FastTestName = TesterName;
						_AppManager.FastTestSex = TesterSex;
						_AppManager.FastTestGrade = (String) ((TextView)findViewById(R.id.test_grade)).getText();
						_AppManager.FastTestSchool = TesterSchool;
						_AppManager.FastTestInstute = TesterInstitute;



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




		}
		break;

		}

	}



}

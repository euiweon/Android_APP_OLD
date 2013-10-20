package com.etoos.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.etoos.data.OMRContent;
import com.etoos.data.OMRHistoryContent;
import com.etoos.view.NumberPickerDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FastTestResult1 extends EtoosBaseActivity implements OnClickListener{


	FastTestResult1 self;
	ArrayList< OMRContent > m_ListData;
	private ListView m_ListView;


	// 마킹 히스토리 
	ArrayList< OMRHistoryContent > m_HistoryListData;


	private OMR_Adapter m_Adapter;


	private Integer m_CurrentTab = 0; 



	private Integer PreTimeCount = 0;	// 이전에 마킹을 했던 시간 ...


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fastresult_1);
		self = this;


		// 시험 시간에는 화면 꺼지는걸 방지 한다. 
		//super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



		m_HistoryListData =  new ArrayList< OMRHistoryContent >();
		m_HistoryListData.clear();

		m_ListView = ((ListView)findViewById(R.id.test_list));
		// 이전에 입력했던 데이터를 다시 입력해준다. 
		{

			AppManagement _AppManager = (AppManagement) getApplication();
			m_HistoryListData = _AppManager.m_FastHistoryListData ;
			m_ListData = _AppManager.m_FastListData ;

		}
				
		m_Adapter = new OMR_Adapter(this, R.layout.test_result_row, m_ListData);

		m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);



		


		m_Adapter.notifyDataSetChanged();



		BtnEvent(R.id.bottom_1);

		
		AppManagement _AppManager = (AppManagement) getApplication();
		
		{
			((TextView)findViewById(R.id.result_title_1)).setText("["+ _AppManager.FastTestGrade + "]");
			((TextView)findViewById(R.id.result_title_3)).setText( _AppManager.FastTestTitle);

			
			
			switch (_AppManager.m_FastSelectIndex)
			{
			case 0:				((TextView)findViewById(R.id.result_title_2)).setText("국어 A형");				break;
			case 1:				((TextView)findViewById(R.id.result_title_2)).setText("국어 B형");				break;
			case 2:				((TextView)findViewById(R.id.result_title_2)).setText("수학 A형");				break;
			case 3:				((TextView)findViewById(R.id.result_title_2)).setText("수학 B형");				break;
			case 4:				((TextView)findViewById(R.id.result_title_2)).setText("영어 A형");				break;
			case 5:				((TextView)findViewById(R.id.result_title_2)).setText("영어 B형");				break;
			case 6:				((TextView)findViewById(R.id.result_title_2)).setText("사회탐구 / 과학탐구 ");	break;
			}

		}




		AfterCreate();


	}


	public void LoadMarkingData()
	{
		// 임시저장된 마킹 데이터를 불러온다 

	}

	public void ListRefresh()
	{
		m_Adapter.notifyDataSetChanged();
	}
	public void BtnEvent( int id )
	{
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{

		case R.id.bottom_1:

		{
			new AlertDialog.Builder(self)
			.setMessage("채점을  끝냅니까?") 
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
		break;
	
		}
	}



	public void ButtonCheck( int number , int answer)
	{
		for( int i = 0 ; i < m_ListData.size(); i++ )
		{
			if ( m_ListData.get(i).Number == number) 
			{
				// 채점 갯수가 2개이상일 경우 체크 안 되도록..
				int checkcount = 0 ; 
				for ( int j = 0 ; j < m_ListData.get(i).AnswerCheck.length ; j++ )
				{
					if (m_ListData.get(i).AnswerCheck[j] >= 1)
					{
						checkcount++;
					}
				}
				if ( checkcount <= 2 )
				{
					if (m_ListData.get(i).AnswerCheck[answer-1] == 1)
					{
						m_ListData.get(i).AnswerCheck[answer-1] = 0;
					}
					else
					{
						m_ListData.get(i).AnswerCheck[answer-1] = 1;
					}
				}

			}
		}
		m_Adapter.notifyDataSetChanged();

	}



	public class OMR_Adapter extends ArrayAdapter<OMRContent>
	{
		private Context mContext;
		private int mResource;
		private ArrayList<OMRContent> mList;
		private LayoutInflater mInflater;
		public OMR_Adapter(Context context, int layoutResource, ArrayList<OMRContent> mTweetList)
		{
			super(context, layoutResource, mTweetList);
			this.mContext = context;
			this.mResource = layoutResource;
			this.mList = mTweetList;
			this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		private void SetImage( ImageView view , int value , int state)
		{
			if ( state == 1 )
			{
				switch(value)
				{
				case 0:	view.setImageResource(R.drawable.black_0);				break;
				case 1:	view.setImageResource(R.drawable.black_1);				break;
				case 2:	view.setImageResource(R.drawable.black_2);				break;
				case 3:	view.setImageResource(R.drawable.black_3);				break;
				case 4:	view.setImageResource(R.drawable.black_4);				break;
				case 5:	view.setImageResource(R.drawable.black_5);				break;
				case 6:	view.setImageResource(R.drawable.black_6);				break;
				case 7:	view.setImageResource(R.drawable.black_7);				break;
				case 8:	view.setImageResource(R.drawable.black_8);				break;
				case 9:	view.setImageResource(R.drawable.black_9);				break;
			
				}
			}
			else if ( state == 0 )
			{
				switch(value)
				{
				case 0:	view.setImageResource(R.drawable.pencil_mark_0);				break;
				case 1:	view.setImageResource(R.drawable.pencil_mark_1);				break;
				case 2:	view.setImageResource(R.drawable.pencil_mark_2);				break;
				case 3:	view.setImageResource(R.drawable.pencil_mark_3);				break;
				case 4:	view.setImageResource(R.drawable.pencil_mark_4);				break;
				case 5:	view.setImageResource(R.drawable.pencil_mark_5);				break;
				case 6:	view.setImageResource(R.drawable.pencil_mark_6);				break;
				case 7:	view.setImageResource(R.drawable.pencil_mark_7);				break;
				case 8:	view.setImageResource(R.drawable.pencil_mark_8);				break;
				case 9:	view.setImageResource(R.drawable.pencil_mark_9);				break;
			
				}
			}
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final OMRContent mBar = mList.get(position);
			final  AppManagement _AppManager = (AppManagement) getApplication();
			
			

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);;
			}
			
			if (mBar.QuestionType == 0 )
			{
				convertView = mInflater.inflate(R.layout.test_result_row, null);;
				final View convertView2 = convertView;

				if(mBar != null) 
				{
					
					((TextView)convertView.findViewById(R.id.number)).setText(mBar.Number.toString());  

					switch(mBar.Answer[0])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.basic_1); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.pencil_mark_1); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.color_black_1); break;
					}

					switch(mBar.Answer[1])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.basic_2); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.pencil_mark_2); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.color_black_2); break;
					}


					switch(mBar.Answer[2])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_3)).setImageResource(R.drawable.basic_3); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_3)).setImageResource(R.drawable.pencil_mark_3); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_3)).setImageResource(R.drawable.color_black_3); break;
					}


					switch(mBar.Answer[3])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_4)).setImageResource(R.drawable.basic_4); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_4)).setImageResource(R.drawable.pencil_mark_4); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_4)).setImageResource(R.drawable.color_black_4); break;
					}


					switch(mBar.Answer[4])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_5)).setImageResource(R.drawable.basic_5); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_5)).setImageResource(R.drawable.pencil_mark_5); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_5)).setImageResource(R.drawable.color_black_5); break;
					}


					
					switch(mBar.AnswerCheck[0])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_1_1)).setVisibility(View.GONE); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_1_1)).setVisibility(View.VISIBLE);break;
					}

					switch(mBar.AnswerCheck[1])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_2_1)).setVisibility(View.GONE); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_2_1)).setVisibility(View.VISIBLE);break;

					}


					switch(mBar.AnswerCheck[2])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_3_1)).setVisibility(View.GONE); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_3_1)).setVisibility(View.VISIBLE);break;
					}


					switch(mBar.AnswerCheck[3])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_4_1)).setVisibility(View.GONE); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_4_1)).setVisibility(View.VISIBLE);break;
					}


					switch(mBar.AnswerCheck[4])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_5_1)).setVisibility(View.GONE); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_5_1)).setVisibility(View.VISIBLE);break;
					}

					((ImageView)convertView.findViewById(R.id.sol_plus)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							mBar.Score++;
							
							if ( m_ListData.size() == 30 )
							{
								if ( mBar.Score > 3 )
									mBar.Score = 3;
							}
							else
							{
								if ( mBar.Score > 3 )
									mBar.Score = 3;
							}
							
							((TextView)convertView2.findViewById(R.id.result_number1111)).setText(mBar.Score.toString());

						}
					});
					TextView temp = ((TextView)convertView.findViewById(R.id.result_number1111));
					temp.setText(mBar.Score.toString());
					
					
					
					
					((ImageView)convertView.findViewById(R.id.sol_minus)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							mBar.Score--;

							
							if ( m_ListData.size() == 30 )
							{
								if ( mBar.Score < 2 )
									mBar.Score = 2;
							}
							else
							{
								if ( mBar.Score < 1 )
									mBar.Score = 1;
							}
							
							((TextView)convertView2.findViewById(R.id.result_number1111)).setText(mBar.Score.toString());

						}
					});



					((ImageView)convertView.findViewById(R.id.number_1)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ButtonCheck(mBar.Number, 1);

						}
					});

					((ImageView)convertView.findViewById(R.id.number_2)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ButtonCheck(mBar.Number, 2);

						}
					});

					((ImageView)convertView.findViewById(R.id.number_3)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ButtonCheck(mBar.Number, 3);

						}
					});
					((ImageView)convertView.findViewById(R.id.number_4)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ButtonCheck(mBar.Number, 4);

						}
					});
					((ImageView)convertView.findViewById(R.id.number_5)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ButtonCheck(mBar.Number, 5);

						}
					});

				}
				
				
				
			}
			
			else if ( mBar.QuestionType == 1  )
			{
				
				
				convertView = mInflater.inflate(R.layout.test_result_row3, null);;
				final View convertView2 = convertView;
				((TextView)convertView.findViewById(R.id.number)).setText(mBar.Number.toString());  

				/*View.OnClickListener onclick =   new View.OnClickListener()
				{

					public void onClick(View v) 
					{
						self.ShowAlertDialLog(baseself, "현재 주관식 채점 기능은 지원하지 않습니다. ");
						
					}
				};
				((ImageView)convertView.findViewById(R.id.number_1)).setOnClickListener(onclick);
				((ImageView)convertView.findViewById(R.id.number_2)).setOnClickListener(onclick);
				((ImageView)convertView.findViewById(R.id.number_3)).setOnClickListener(onclick);
				*/
				View.OnClickListener onclick =   new View.OnClickListener()
				{

					public void onClick(View v) 
					{
						if (mBar.AnswerCheck2 == 0)
						{
							mBar.AnswerCheck2 =1;
						}
						else
						{
							mBar.AnswerCheck2 =0;
						}
						ListRefresh();
					}
				};
				
				((ImageView)convertView.findViewById(R.id.number_5)).setOnClickListener(onclick);
				
				if (mBar.AnswerCheck2 == 0 )
				{
					((ImageView)convertView.findViewById(R.id.number_5)).setImageResource(R.drawable.x_box);
				}
				else
				{
					((ImageView)convertView.findViewById(R.id.number_5)).setImageResource(R.drawable.o_box);
				}

				((ImageView)convertView.findViewById(R.id.sol_plus)).setOnClickListener(new View.OnClickListener() 
				{

					public void onClick(View v) 
					{
						mBar.Score++;
						
						if ( m_ListData.size() == 30 )
						{
							if ( mBar.Score > 3 )
								mBar.Score = 3;
						}
						else
						{
							if ( mBar.Score > 3 )
								mBar.Score = 3;
						}
						
						((TextView)convertView2.findViewById(R.id.result_number1111)).setText(mBar.Score.toString());

					}
				});
				TextView temp = ((TextView)convertView.findViewById(R.id.result_number1111));
				temp.setText(mBar.Score.toString());
				
				
				
				
				((ImageView)convertView.findViewById(R.id.sol_minus)).setOnClickListener(new View.OnClickListener() 
				{

					public void onClick(View v) 
					{
						mBar.Score--;

						
						if ( m_ListData.size() == 30 )
						{
							if ( mBar.Score < 2 )
								mBar.Score = 2;
						}
						else
						{
							if ( mBar.Score < 1 )
								mBar.Score = 1;
						}
						
						((TextView)convertView2.findViewById(R.id.result_number1111)).setText(mBar.Score.toString());

					}
				});
				
				
				if ( mBar.AnswerString.equals(""))
				{
					((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.basic_0);
					((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.basic_0);
					((ImageView)convertView.findViewById(R.id.number_3)).setImageResource(R.drawable.basic_0);
				}
				else
				{				
					
					if ( Integer.parseInt( mBar.AnswerString)/100 == 0 )
					{
						((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.basic_0);
					}
					else
					{
						SetImage(((ImageView)convertView.findViewById(R.id.number_1)), Integer.parseInt( mBar.AnswerString)/100, mBar.AnswerStringState);
					}
					
					if ( Integer.parseInt( mBar.AnswerString)/100 == 0 &&  (Integer.parseInt( mBar.AnswerString)/10)%10 == 0)
					{
						((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.basic_0);
					}
					else
					{
						SetImage(((ImageView)convertView.findViewById(R.id.number_2)), (Integer.parseInt( mBar.AnswerString)/10)%10, mBar.AnswerStringState);
					}
					
					
					SetImage(((ImageView)convertView.findViewById(R.id.number_3)), Integer.parseInt( mBar.AnswerString)%10, mBar.AnswerStringState);
					
					

				}
				
				
			}
			
			return convertView;
		}
	}



}


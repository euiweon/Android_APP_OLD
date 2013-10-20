package com.etoos.view;


import java.util.ArrayList;

import com.etoos.data.OMRHistoryContent;
import com.etoos.test.R;
import com.etoos.test.R.layout;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class FastResultItem_2  extends LinearLayout {

	Context mContext = null;
	TextView TilteView = null;
	TextView TilteView2 = null;
	TextView TilteView3 = null;


	public FastResultItem_2(Context context) 
	{
		super(context);
		initView(context);
	}
	public FastResultItem_2(Context context, AttributeSet attrs) {

		super(context, attrs);
		initView(context);
	}


	void initView(Context context)
	{
		mContext = context;

		String infService = Context.LAYOUT_INFLATER_SERVICE;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.result_data_2, this, false);
		addView(v);   

	}

	public void SetData(ArrayList< OMRHistoryContent > list  )
	{
		// 일단 모든 데이터 Hide 시킴
		((ImageView)findViewById(R.id.number1)).setVisibility(View.GONE);
		((ImageView)findViewById(R.id.number2)).setVisibility(View.GONE);
		((ImageView)findViewById(R.id.number3)).setVisibility(View.GONE);
		((ImageView)findViewById(R.id.number4)).setVisibility(View.GONE);
		((ImageView)findViewById(R.id.number5)).setVisibility(View.GONE);
		((ImageView)findViewById(R.id.number6)).setVisibility(View.GONE);
		((ImageView)findViewById(R.id.number7)).setVisibility(View.GONE);
		((ImageView)findViewById(R.id.number8)).setVisibility(View.GONE);
		((ImageView)findViewById(R.id.number9)).setVisibility(View.GONE);


		// 해당하는 데이터만 Visible 시키고 데이터를 입력해준다. 


		ImageView temp;
		for ( int  i = 0 ; i < list.size() ; i++  )
		{
			switch( i)
			{
			case 0 :
			{
				temp = ((ImageView)findViewById(R.id.number1));
				switch( list.get(i).QuestionType)
				{
				case 0:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_white_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_white_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_white_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_white_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_white_5 );						break;
					}
				}
				break;
				case 1:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.mark_show_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.mark_show_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.mark_show_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.mark_show_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.mark_show_5 );						break;
					}
				}
				break;
				case 2:
				case 3:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_black_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_black_2);						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_black_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_black_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_black_5 );						break;
					}
				}
				break;
				}
				((ImageView)findViewById(R.id.number1)).setVisibility(View.VISIBLE);
			}
			break;
			case 1 :
			{
				temp = ((ImageView)findViewById(R.id.number2));
				switch( list.get(i).QuestionType)
				{
				case 0:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_white_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_white_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_white_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_white_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_white_5 );						break;
					}
				}
				break;
				case 1:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.mark_show_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.mark_show_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.mark_show_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.mark_show_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.mark_show_5 );						break;
					}
				}
				break;
				case 2:
				case 3:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_black_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_black_2);						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_black_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_black_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_black_5 );						break;
					}
				}
				break;
				}
				((ImageView)findViewById(R.id.number2)).setVisibility(View.VISIBLE);
			}
			break;
			case 2 :
			{
				temp = ((ImageView)findViewById(R.id.number3));
				switch( list.get(i).QuestionType)
				{
				case 0:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.mark_show_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.mark_show_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.mark_show_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.mark_show_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.mark_show_5 );						break;
					}
				}
				break;
				case 1:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.mark_show_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.mark_show_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.mark_show_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.mark_show_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.mark_show_5 );						break;
					}
				}
				break;
				case 2:
				case 3:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_black_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_black_2);						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_black_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_black_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_black_5 );						break;
					}
				}
				break;
				}
				((ImageView)findViewById(R.id.number3)).setVisibility(View.VISIBLE);
			}
			break;
			case 3 :
			{
				temp = ((ImageView)findViewById(R.id.number4));
				switch( list.get(i).QuestionType)
				{
				case 0:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_white_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_white_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_white_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_white_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_white_5 );						break;
					}
				}
				break;
				case 1:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.mark_show_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.mark_show_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.mark_show_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.mark_show_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.mark_show_5 );						break;
					}
				}
				break;
				case 2:
				case 3:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_black_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_black_2);						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_black_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_black_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_black_5 );						break;
					}
				}
				break;

				}
				((ImageView)findViewById(R.id.number4)).setVisibility(View.VISIBLE);
			}
			break;
			case 4 :
			{
				temp = ((ImageView)findViewById(R.id.number5));
				switch( list.get(i).QuestionType)
				{
				case 0:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_white_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_white_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_white_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_white_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_white_5 );						break;
					}
				}
				break;
				case 1:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.mark_show_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.mark_show_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.mark_show_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.mark_show_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.mark_show_5 );						break;
					}
				}
				break;
				case 2:
				case 3:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_black_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_black_2);						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_black_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_black_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_black_5 );						break;
					}
				}
				break;

				}
				((ImageView)findViewById(R.id.number5)).setVisibility(View.VISIBLE);
			}
			break;
			case 5 :
			{
				temp = ((ImageView)findViewById(R.id.number6));
				switch( list.get(i).QuestionType)
				{
				case 0:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_white_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_white_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_white_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_white_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_white_5 );						break;
					}
				}
				break;
				case 1:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.mark_show_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.mark_show_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.mark_show_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.mark_show_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.mark_show_5 );						break;
					}
				}
				break;
				case 2:
				case 3:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_black_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_black_2);						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_black_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_black_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_black_5 );						break;
					}
				}
				break;
				}
				((ImageView)findViewById(R.id.number6)).setVisibility(View.VISIBLE);
			}
			break;
			case 6 :
			{
				temp = ((ImageView)findViewById(R.id.number7));
				switch( list.get(i).QuestionType)
				{
				case 0:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_white_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_white_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_white_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_white_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_white_5 );						break;
					}
				}
				break;
				case 1:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.mark_show_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.mark_show_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.mark_show_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.mark_show_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.mark_show_5 );						break;
					}
				}
				break;
				case 2:
				case 3:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_black_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_black_2);						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_black_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_black_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_black_5 );						break;
					}
				}
				break;
				}
				((ImageView)findViewById(R.id.number7)).setVisibility(View.VISIBLE);
			}
			break;

			case 7 :
			{
				temp = ((ImageView)findViewById(R.id.number8));
				switch( list.get(i).QuestionType)
				{
				case 0:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_white_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_white_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_white_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_white_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_white_5 );						break;
					}
				}
				break;
				case 1:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.mark_show_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.mark_show_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.mark_show_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.mark_show_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.mark_show_5 );						break;
					}
				}
				break;
				case 2:
				case 3:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_black_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_black_2);						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_black_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_black_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_black_5 );						break;
					}
				}
				break;
				}
				((ImageView)findViewById(R.id.number8)).setVisibility(View.VISIBLE);
			}
			break;
			case 8 :
			{
				temp = ((ImageView)findViewById(R.id.number9));
				switch( list.get(i).QuestionType)
				{
				case 0:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_white_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_white_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_white_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_white_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_white_5 );						break;
					}
				}
				break;
				case 1:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.mark_show_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.mark_show_2 );						break;
					case 2 : temp.setBackgroundResource(R.drawable.mark_show_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.mark_show_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.mark_show_5 );						break;
					}
				}
				break;
				case 2:
				case 3:
				{
					switch( list.get(i).Answer - 1 )
					{
					case 0 : temp.setBackgroundResource(R.drawable.a_black_1 );						break;
					case 1 : temp.setBackgroundResource(R.drawable.a_black_2);						break;
					case 2 : temp.setBackgroundResource(R.drawable.a_black_3 );						break;
					case 3 : temp.setBackgroundResource(R.drawable.a_black_4 );						break;
					case 4 : temp.setBackgroundResource(R.drawable.a_black_5 );						break;
					}
				}
				break;
				}
				((ImageView)findViewById(R.id.number9)).setVisibility(View.VISIBLE);
			}
			break;

			}
		}

	}
}
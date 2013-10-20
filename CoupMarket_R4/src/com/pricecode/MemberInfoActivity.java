package com.pricecode;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultAdapter;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XResultMap;

public class MemberInfoActivity extends CMActivity implements OnClickListener, OnTouchListener,
                                                  OnFocusChangeListener
{
	boolean m_bIsJoinUI = false;

	EditText m_etId, m_etName, m_etPassword1, m_etPassword2, m_etBirthday;
	EditText m_etEmail, m_etMobile1, m_etMobile2, m_etMobile3;
	EditText m_etContact1, m_etContact2, m_etContact3, m_etPost1, m_etPost2;
	EditText m_etAddress1, m_etAddress2;
	ImageButton m_btnSearchPost, m_btnJoin, m_btnUpdate;
	Spinner m_spDayType, m_spGender, m_spFavorArea1, m_spFavorArea2, m_spFavorCategory;
	RadioGroup m_radioEmail, m_radioSms;
	CheckBox m_btnAgreement;
	TextView m_tvShowAgreement;

	LinearLayout m_layoutJoin, m_layoutUpdate;

	String[] m_arrayDayType = { "양력", "음력" };
	String[] m_arrayGender = { "남성", "여성" };

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_info);

		Intent curIntent = getIntent();
		m_bIsJoinUI = curIntent.getBooleanExtra("isJoinUI", false);
		
		if(m_bIsJoinUI)
			setTitleBarText("회원가입");
		else
			setTitleBarText("회원정보수정");

		m_etId = (EditText) findViewById(R.id.editText_id);
		m_etName = (EditText) findViewById(R.id.editText_name);
		m_etPassword1 = (EditText) findViewById(R.id.editText_password_1);
		m_etPassword2 = (EditText) findViewById(R.id.editText_password_2);
		m_etBirthday = (EditText) findViewById(R.id.editText_birthday);
		m_etEmail = (EditText) findViewById(R.id.editText_email);
		m_etMobile1 = (EditText) findViewById(R.id.editText_mobile_number_1);
		m_etMobile2 = (EditText) findViewById(R.id.editText_mobile_number_2);
		m_etMobile3 = (EditText) findViewById(R.id.editText_mobile_number_3);
		m_etContact1 = (EditText) findViewById(R.id.editText_contact_number_1);
		m_etContact2 = (EditText) findViewById(R.id.editText_contact_number_2);
		m_etContact3 = (EditText) findViewById(R.id.editText_contact_number_3);
		m_etPost1 = (EditText) findViewById(R.id.editText_postcode_1);
		m_etPost2 = (EditText) findViewById(R.id.editText_postcode_2);
		m_etAddress1 = (EditText) findViewById(R.id.editText_address_1);
		m_etAddress2 = (EditText) findViewById(R.id.editText_address_2);

		m_btnSearchPost = (ImageButton) findViewById(R.id.imageButton_search_postcode);
		m_btnJoin = (ImageButton) findViewById(R.id.imageButton_join);
		m_btnUpdate = (ImageButton) findViewById(R.id.imageButton_update);

		m_spDayType = (Spinner) findViewById(R.id.spinner_day_type);
		m_spGender = (Spinner) findViewById(R.id.spinner_sex);
		m_spFavorArea1 = (Spinner) findViewById(R.id.spinner_favor_area_1);
		m_spFavorArea2 = (Spinner) findViewById(R.id.spinner_favor_area_2);
		m_spFavorCategory = (Spinner) findViewById(R.id.spinner_favor_category);

		m_radioEmail = (RadioGroup) findViewById(R.id.radioGroup_email);
		m_radioSms = (RadioGroup) findViewById(R.id.radioGroup_sms);

		m_btnAgreement = (CheckBox) findViewById(R.id.checkBox_agreement);
		m_tvShowAgreement = (TextView) findViewById(R.id.textView_show_agreement);

		m_btnSearchPost.setOnClickListener(this);
		m_btnJoin.setOnClickListener(this);
		m_btnUpdate.setOnClickListener(this);
		m_btnAgreement.setOnClickListener(this);
		m_tvShowAgreement.setOnClickListener(this);
		m_etBirthday.setOnTouchListener(this);
		m_etBirthday.setOnFocusChangeListener(this);

		m_layoutJoin = (LinearLayout) findViewById(R.id.linearLayout_join);
		m_layoutUpdate = (LinearLayout) findViewById(R.id.linearLayout_update);

		m_etPost1.setEnabled(false);
		m_etPost2.setEnabled(false);
		m_etAddress1.setEnabled(false);

		if( m_bIsJoinUI )
		{
			m_layoutUpdate.setVisibility(View.GONE);
			m_etId.requestFocus();
		}
		else
		{
			m_layoutJoin.setVisibility(View.GONE);
			m_etPassword1.requestFocus();
			
			CMManager.checkOnline(this, true);
		}

		// ///
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		                                                        android.R.layout.simple_spinner_item,
		                                                        m_arrayDayType);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		m_spDayType.setAdapter(adapter);
		m_spDayType.setSelection(0);

		// ///
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_arrayGender);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		m_spGender.setAdapter(adapter);
		m_spGender.setSelection(0);

		// ///
		m_radioEmail.check(R.id.radioButton_email_yes);
		m_radioSms.check(R.id.radioButton_sms_yes);

		// ///
		if( CMHttpConn.reqAreaList(m_Handler) == null )
			toastCheckInternet();
		else
			showWait(null);
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	static final int REQUEST_FOR_SHOW_AGREEMENT = 1;
	static final int REQUEST_FOR_SEARCH_POSTCODE = 2;

	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult(requestCode, resultCode, data);
		if( resultCode != RESULT_OK || data == null )
			return;

		if( requestCode == REQUEST_FOR_SEARCH_POSTCODE )
		{
			String strPostcode = data.getStringExtra("postcode");
			String strAddress = data.getStringExtra("address");

			if( strPostcode.length() != 7 )
				return;

			String strPostcode1 = strPostcode.substring(0, 3);
			String strPostcode2 = strPostcode.substring(4);

			m_etPost1.setText(strPostcode1);
			m_etPost2.setText(strPostcode2);
			m_etAddress1.setText(strAddress);
//			m_etPost1.setEnabled(false);
//			m_etPost2.setEnabled(false);
//			m_etAddress1.setEnabled(false);
			m_etAddress2.requestFocus();
		}
	}

	@Override
	public void onClick( View v )
	{
		switch( v.getId() )
		{
			case R.id.imageButton_join:
			case R.id.imageButton_update:
				tryJoinOrUpdate();
				break;

			case R.id.imageButton_search_postcode:
				searchPostCode();
				break;

			case R.id.textView_show_agreement:
			{
				Intent intent = CMManager.getIntent(this, ShowAgreementActivity.class);
				startActivityForResult(intent, REQUEST_FOR_SHOW_AGREEMENT);
				break;
			}
		}
	}

	private void searchPostCode()
	{
		Intent intent = CMManager.getIntent(this, SearchPostcodeActivity.class);
		startActivityForResult(intent, REQUEST_FOR_SEARCH_POSTCODE);
	}

	void tryJoinOrUpdate()
	{
		String strId = m_etId.getText().toString().trim();
		String strName = m_etName.getText().toString().trim();
		String strPass1 = m_etPassword1.getText().toString().trim();
		String strPass2 = m_etPassword2.getText().toString().trim();
		String strBirth = m_etBirthday.getText().toString().trim();
		String strEmail = m_etEmail.getText().toString().trim();
		String strMobile1 = m_etMobile1.getText().toString().trim();
		String strMobile2 = m_etMobile2.getText().toString().trim();
		String strMobile3 = m_etMobile3.getText().toString().trim();
		String strContact1 = m_etContact1.getText().toString().trim();
		String strContact2 = m_etContact2.getText().toString().trim();
		String strContact3 = m_etContact3.getText().toString().trim();
		String strPost1 = m_etPost1.getText().toString().trim();
		String strPost2 = m_etPost2.getText().toString().trim();
		String strAddress1 = m_etAddress1.getText().toString().trim();
		String strAddress2 = m_etAddress2.getText().toString().trim();

		String strDateType = (String) m_spDayType.getSelectedItem();
		String strGender = (String) m_spGender.getSelectedItem();
		String strFavorArea1 = null, strFavorArea2 = null, strFaverCategory = null;
		int nPos = m_spFavorArea1.getSelectedItemPosition();
		if( nPos > 0 )
			strFavorArea1 = m_AreaList1.get(nPos).get("no");
		nPos = m_spFavorArea2.getSelectedItemPosition();
		if( nPos > 0 )
			strFavorArea2 = m_AreaList2.get(nPos).get("no");
		nPos = m_spFavorCategory.getSelectedItemPosition();
		if( nPos > 0 )
			strFaverCategory = m_FavorCategoryList.get(nPos).get("no");

		String strEmailYes = "yes", strSmsYes = "yes";
		int nCheckedId = m_radioEmail.getCheckedRadioButtonId();
		if( nCheckedId != R.id.radioButton_email_yes )
			strEmailYes = "no";
		nCheckedId = m_radioSms.getCheckedRadioButtonId();
		if( nCheckedId != R.id.radioButton_sms_yes )
			strSmsYes = "no";

		
		if ( strId.length() != 0  )
		{
			Character cr = null;
			cr = new Character(strId.charAt(0));
			String temp = cr.toString();
			if (isNumber(temp) )
			{
				showAlert("아이디의 첫 글자는 숫자를 사용할수 없습니다.");
				return;
			}

		}
		if( strId.length() == 0 || strName.length() == 0 || strPass1.length()  < 4
		    || strPass2.length() == 0 || strBirth.length() == 0 || strEmail.length() == 0
		    || strMobile1.length() == 0 || strMobile2.length() == 0 || strMobile3.length() == 0
		    || strContact1.length() == 0 || strContact2.length() == 0 || strContact3.length() == 0
		    || strDateType == null || strDateType.length() == 0 || strGender == null
		    || strGender.length() == 0 || strFavorArea1 == null || strFavorArea1.length() == 0
		    || strFavorArea2 == null || strFavorArea2.length() == 0 || strFaverCategory == null
		    || strFaverCategory.length() == 0 )
		{
			if( strId.length() == 0 )
				showAlert("아이디를 입력하여 주십시오.\n주소이외의 다른 정보들은 필수 입력사항입니다.");
			else if( strName.length() == 0 )
				showAlert("이름을 입력하여 주십시오.\n주소이외의 다른 정보들은 필수 입력사항입니다.");
			else if( strPass1.length() == 0 || strPass2.length() == 0 )
				showAlert("비밀번호를 입력하여 주십시오.\n주소이외의 다른 정보들은 필수 입력사항입니다.");
			else if( strPass1.length() < 4  )
				showAlert("비밀번호는 4자 이상이여야 가입이 가능합니다.");
			
			else if( strMobile1.length() == 0 || strMobile2.length() == 0 || strMobile3.length() == 0 )
				showAlert("휴대폰번호를 입력하여 주십시오.\n주소이외의 다른 정보들은 필수 입력사항입니다.");
			else if( strContact1.length() == 0 || strContact2.length() == 0 || strContact3.length() == 0 )
				showAlert("전화번호를 입력하여 주십시오.\n주소이외의 다른 정보들은 필수 입력사항입니다.");
			else if( strBirth.length() == 0 || strDateType == null || strDateType.length() == 0 )
				showAlert("생년월일을 입력하여 주십시오.\n주소이외의 다른 정보들은 필수 입력사항입니다.");
			else if( strGender == null || strGender.length() == 0 )
				showAlert("성별을 선택하여 주십시오.\n주소이외의 다른 정보들은 필수 입력사항입니다.");
			else if( strFavorArea1 == null || strFavorArea1.length() == 0 || strFavorArea2 == null
			         || strFavorArea2.length() == 0 )
				showAlert("관심지역을 선택하여 주십시오.\n주소이외의 다른 정보들은 필수 입력사항입니다.");
			else if( strFaverCategory == null || strFaverCategory.length() == 0 )
				showAlert("관심상품종류를 선택하여 주십시오.\n주소이외의 다른 정보들은 필수 입력사항입니다.");
			return;
		}

		if( strPass1.equals(strPass2) == false )
		{
			showAlert("비밀번호를 확인하여 주십시오.");
			return;
		}

		if( m_bIsJoinUI && m_btnAgreement.isChecked() == false )
		{
			showAlert("개인정보보호정책 및 이용약관에 동의하여주십시오.");
			return;
		}

		if( strGender.equals("여성") )
			strGender = "female";
		else
			strGender = "male";
		if( strDateType.equals("음력") )
			strDateType = "lunar";
		else
			strDateType = "solar";

		String strMobile = strMobile1 + "-" + strMobile2 + "-" + strMobile3;
		String strContact = strContact1 + "-" + strContact2 + "-" + strContact3;
		String strFavorArea = strFavorArea1 + "-" + strFavorArea2;
		String strPost = strPost1 + "-" + strPost2;
		String strAddress = strAddress1 + "||" + strAddress2;

		int nReqType = 0;
		if(m_bIsJoinUI)
			nReqType = CMHttpConn.TYPE_REQ_JOIN_MEMBER;
		else
			nReqType = CMHttpConn.TYPE_REQ_UPDATE_MEMBER_INFO;
		
		if( CMHttpConn.reqJoinOrUpdate(nReqType, m_Handler, strId, strName, strPass1, strBirth, strDateType, strGender,
		                       strEmail, strMobile, strContact, strFavorArea, strFaverCategory,
		                       strEmailYes, strSmsYes, strPost, strAddress) == null )
			toastCheckInternet();
		else
			showWait(null);
	}
	
	public static boolean isNumber(String str) {  
		boolean check = true;
		for(int i = 0; i < str.length(); i++) {
			if(!Character.isDigit(str.charAt(i)))


			{
				check = false;
				break; 
			}// end if
		} //end for
		return check;  
	} //isNumber 


	XHandler m_Handler = new XHandler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			removeTimeoutMsg();
			if( m_Handler == null )
				return;

			if( msg.what == XHandler.RESULT_TIME_OUT )
			{
				cancelWait();
				toastCheckInternet();
				finish();
				return;
			}
			else
			{
				cancelWait();
				if( msg.what == CMHttpConn.TYPE_REQ_JOIN_MEMBER )
				{
					if( msg.obj != null && msg.obj instanceof CMHttpConn )
					{
						CMHttpConn conn = (CMHttpConn) msg.obj;
						if( conn.m_nWebOpenResult == CMHttpConn.XHTTP_200_OK )
						{
							XResultList resultList = conn.m_XmlParser.getResultList();
							if( resultList != null && resultList.size() > 0 )
							{
								String strResult = resultList.get(0).get("result");
								if( strResult != null && strResult.equals("true") )
								{
									showToast("회원에 가입되었습니다.");

									Intent resultData = new Intent();
									resultData.putExtra("id", m_etId.getText().toString().trim());
									resultData.putExtra("password", m_etPassword1.getText().toString()
									                                             .trim());
									setResult(RESULT_OK, resultData);
									finish();
									return;
								}
								else
								{
									String strError = resultList.get(0).get("errorMsg");
									if( strError != null && strError.length() > 0 )
									{
										showAlert(strError);
										return;
									}
								}

							}

							toastRequestFail();
							return;
						}
					}

					toastCheckInternet();
					return;
				}
				else if( msg.what == CMHttpConn.TYPE_REQ_UPDATE_MEMBER_INFO )
				{
					if( msg.obj != null && msg.obj instanceof CMHttpConn )
					{
						CMHttpConn conn = (CMHttpConn) msg.obj;
						if( conn.m_nWebOpenResult == CMHttpConn.XHTTP_200_OK )
						{
							XResultList resultList = conn.m_XmlParser.getResultList();
							if( resultList != null && resultList.size() > 0 )
							{
								String strResult = resultList.get(0).get("result");
								if( strResult != null && strResult.equals("true") )
								{
									showToast("수정된 정보가 저장되었습니다.");
									return;
								}
								else
								{
									String strError = resultList.get(0).get("errorMsg");
									if( strError != null && strError.length() > 0 )
									{
										showAlert(strError);
										return;
									}
								}

							}

							toastRequestFail();
							return;
						}
					}

					toastCheckInternet();
					return;
				}

				if( msg.obj != null && msg.obj instanceof CMHttpConn )
				{
					CMHttpConn conn = (CMHttpConn) msg.obj;
					if( conn.m_nWebOpenResult == CMHttpConn.XHTTP_200_OK )
					{
						XResultList resultList = conn.m_XmlParser.getResultList();
						if( resultList != null && resultList.size() > 0 )
						{
							if( msg.what == CMHttpConn.TYPE_REQ_AREA_LIST )
							{
								if( updateAreaList(resultList) )
								{
									CMManager.setAreaList(resultList);
									if( CMHttpConn.reqFavorCategory(m_Handler) == null )
										toastCheckInternet();
									else
										showWait(null);
									return;
								}
							}
							else if( msg.what == CMHttpConn.TYPE_REQ_FAVOR_CATEGORY_LIST )
							{
								if( updateFavorCategoryList(resultList) )
								{
									if( m_bIsJoinUI == false )
									{
										String strId = CMManager.getPrefString(CMManager.PREF_USER_ID,
										                                       "");
										String strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD,
										                                              "");
										if( CMHttpConn.reqMemberInfo(m_Handler, strId, strPassword) == null )
											toastCheckInternet();
										else
											showWait(null);
									}
									return;
								}
							}
							else if( msg.what == CMHttpConn.TYPE_REQ_MEMBER_INFO )
							{
								String strResult = resultList.get(0).get("result");
								if( strResult != null && strResult.equals("true") )
								{
									if(updateUIWithReceivedInfo(resultList))
									{
										CMManager.setMyInfoList(resultList);
										return;
									}
								}
							}

							toastRequestFail();
							finish();
							return;
						}

						toastResultIsEmpty();
						finish();
						return;
					}
				}

				toastCheckInternet();
				finish();
				return;
			}
		}
	};

	XResultList m_AreaResultList = null, m_AreaList1 = null, m_AreaList2 = null;
	boolean updateAreaList( XResultList resultList )
	{
		m_AreaResultList = resultList;
		if( m_AreaResultList == null )
			m_AreaResultList = new XResultList();

		XResultMap defaultMap = null;

		m_AreaList1 = new XResultList();
		defaultMap = new XResultMap();
		defaultMap.put("subject", "1차지역");
		m_AreaList1.add(defaultMap);

		String strPNo;
		for( XResultMap result : m_AreaResultList )
		{
			strPNo = result.get("pno");
			if( strPNo == null || strPNo.length() == 0 )
				continue;

			if( strPNo.equals("0") )
				m_AreaList1.add(result);
		}

		XResultAdapter adapter1 = new XResultAdapter(this, android.R.layout.simple_spinner_item,
		                                             android.R.id.text1, "subject", m_AreaList1);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item,
		                                 android.R.id.text1);
		m_spFavorArea1.setAdapter(adapter1);
		m_spFavorArea1.setSelection(Spinner.NO_ID);
		m_spFavorArea1.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int pos, long id )
			{
				if( m_AreaList1 == null )
					return;

				XResultMap result = m_AreaList1.get(pos);
				if( result == null )
					return;

				String strNo = result.get("no");
				if( strNo == null )
					strNo = "";

				m_AreaList2 = new XResultList();
				XResultMap defaultMap = new XResultMap();
				defaultMap.put("subject", "2차지역");
				m_AreaList2.add(defaultMap);

				if( strNo.length() > 0 )
				{
					String strPNo;
					for( XResultMap resultOfAll : m_AreaResultList )
					{
						strPNo = resultOfAll.get("pno");
						if( strPNo == null || strPNo.length() == 0 )
							continue;

						if( strPNo.equals(strNo) )
							m_AreaList2.add(resultOfAll);
					}
				}

				XResultAdapter adapter2 = new XResultAdapter(MemberInfoActivity.this,
				                                             android.R.layout.simple_spinner_item,
				                                             android.R.id.text1, "subject", m_AreaList2);
				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item,
				                                 android.R.id.text1);
				m_spFavorArea2.setAdapter(adapter2);
				m_spFavorArea2.setOnItemSelectedListener(new OnItemSelectedListener()
				{
					@Override
					public void onItemSelected( AdapterView<?> parent, View view, int pos, long id )
					{
						if( pos == 0 )
							return;
					}

					@Override
					public void onNothingSelected( AdapterView<?> parent )
					{
					}
				});

				int nArea2Index = Spinner.NO_ID;
				if(CMManager.isOnline())
				{
    				String strArea = CMManager.getMyInfo("area");
    				if( strArea != null && strArea.length() > 0 )
    				{
    					String strArea2 = "";
    					int nFound = -1;
    					nFound = strArea.indexOf("-", 0);
    					if( nFound != -1 )
    					{
    						strArea2 = strArea.substring(nFound + 1);
    						if( strArea2 != null && strArea2.length() > 0 && m_AreaList2 != null
    							    && m_AreaList2.size() > 0 )
    							{
    								int nFoundIndex = -1;
    								for( int nIndex = 0; nIndex < m_AreaList2.size(); nIndex++ )
    								{
    									XResultMap area2Result = m_AreaList2.get(nIndex);
    									if( area2Result == null )
    										continue;
    									String strArea2No = area2Result.get("no");
    									if( strArea2No == null || strArea2No.length() == 0 )
    										continue;
    									if( strArea2No.equals(strArea2) == false )
    										continue;
    									nFoundIndex = nIndex;
    									break;
    								}
    								if( nFoundIndex != -1 )
    									nArea2Index = nFoundIndex;
    							}
    					}
    				}
				}
				m_spFavorArea2.setSelection(nArea2Index);
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent )
			{
				m_spFavorArea2.setAdapter(null);
			}
		});

		// ////////
		m_AreaList2 = new XResultList();
		defaultMap = new XResultMap();
		defaultMap.put("subject", "2차지역");
		m_AreaList2.add(defaultMap);

		XResultAdapter adapter2 = new XResultAdapter(MemberInfoActivity.this,
		                                             android.R.layout.simple_spinner_item,
		                                             android.R.id.text1, "subject", m_AreaList2);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item,
		                                 android.R.id.text1);
		m_spFavorArea2.setAdapter(adapter2);
		m_spFavorArea2.setSelection(0);

		return true;
	}

	XResultList m_FavorCategoryList = null;

	protected boolean updateFavorCategoryList( XResultList resultList )
	{
		m_FavorCategoryList = resultList;
		if( m_FavorCategoryList == null )
			m_FavorCategoryList = new XResultList();

		XResultMap defaultMap = new XResultMap();
		defaultMap.put("subject", "상품종류");
		m_FavorCategoryList.add(0, defaultMap);

		XResultAdapter adapter = new XResultAdapter(MemberInfoActivity.this,
		                                            android.R.layout.simple_spinner_item,
		                                            android.R.id.text1, "subject", m_FavorCategoryList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item,
		                                android.R.id.text1);
		m_spFavorCategory.setAdapter(adapter);
		m_spFavorCategory.setSelection(0);
		m_spFavorCategory.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int pos, long id )
			{
				if( pos == 0 )
					return;
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent )
			{
			}
		});

		return true;
	}

	protected boolean updateUIWithReceivedInfo( XResultList resultList )
	{
		if( resultList == null || resultList.size() == 0 )
			return false;
		XResultMap result = resultList.get(0);
		if( result == null || result.size() == 0 )
			return false;

		String strId = result.get("uid");
		if( strId != null && strId.length() > 0 )
		{
			m_etId.setText(strId);
			m_etId.setEnabled(false);
		}
		String strName = result.get("name");
		if( strName != null && strName.length() > 0 )
		{
			m_etName.setText(strName);
			m_etName.setEnabled(false);
		}
		String strPassword = result.get("passwd");
		if( strPassword != null && strPassword.length() > 0 )
		{
			m_etPassword1.setText(strPassword);
			m_etPassword2.setText(strPassword);
		}
		String strBirth = result.get("birth");
		if( strBirth != null && strBirth.length() > 0 )
			m_etBirthday.setText(strBirth);
		String strDayType = result.get("calendar_code");
		if( strDayType != null && strDayType.length() > 0 )
		{
			if( strDayType.equals("lunar") )
				m_spDayType.setSelection(1);
			else
				m_spDayType.setSelection(0);
		}
		String strGender = result.get("gender");
		if( strGender != null && strGender.length() > 0 )
		{
			if( strGender.equals("female") )
				m_spGender.setSelection(1);
			else
				m_spGender.setSelection(0);
		}
		String strEmail = result.get("email");
		if( strEmail != null && strEmail.length() > 0 )
		{
			m_etEmail.setText(strEmail);
			m_etEmail.setEnabled(false);
		}
		String strMibile = result.get("hphone");
		if( strMibile != null && strMibile.length() > 0 )
		{
			String strSub = "";
			int nFound = -1, nStart = 0;
			nFound = strMibile.indexOf("-", (nStart = nFound + 1));
			if( nFound != -1 )
			{
				strSub = strMibile.substring(nStart, nFound);
				m_etMobile1.setText(strSub);

				nFound = strMibile.indexOf("-", (nStart = nFound + 1));
				if( nFound != -1 )
				{
					strSub = strMibile.substring(nStart, nFound);
					m_etMobile2.setText(strSub);

					strSub = strMibile.substring(nFound + 1);
					m_etMobile3.setText(strSub);
				}
			}
		}
		String strContact = result.get("phone");
		if( strContact != null && strContact.length() > 0 )
		{
			String strSub = "";
			int nFound = -1, nStart = 0;
			nFound = strContact.indexOf("-", (nStart = nFound + 1));
			if( nFound != -1 )
			{
				strSub = strContact.substring(nStart, nFound);
				m_etContact1.setText(strSub);

				nFound = strContact.indexOf("-", (nStart = nFound + 1));
				if( nFound != -1 )
				{
					strSub = strContact.substring(nStart, nFound);
					m_etContact2.setText(strSub);

					strSub = strContact.substring(nFound + 1);
					m_etContact3.setText(strSub);
				}
			}
		}
		String strPost = result.get("post");
		if( strPost != null && strPost.length() > 0 )
		{
			String strSub = "";
			int nFound = -1;
			nFound = strPost.indexOf("-", 0);
			if( nFound != -1 )
			{
				strSub = strPost.substring(0, nFound);
				m_etPost1.setText(strSub);

				strSub = strPost.substring(nFound + 1);
				m_etPost2.setText(strSub);
			}
		}
		String strAddress = result.get("address");
		if( strAddress != null && strAddress.length() > 0 )
		{
			String strSub = "";
			int nFound = -1;
			nFound = strAddress.indexOf("||");
			if( nFound != -1 )
			{
				strSub = strAddress.substring(0, nFound);
				m_etAddress1.setText(strSub);

				strSub = strAddress.substring(nFound + 2);
				m_etAddress2.setText(strSub);
			}
		}
		String strArea = result.get("area");
		if( strArea != null && strArea.length() > 0 )
		{
			String strArea1 = "";
			int nFound = -1;
			nFound = strArea.indexOf("-", 0);
			if( nFound != -1 )
			{
				strArea1 = strArea.substring(0, nFound);
				if( strArea1 != null && strArea1.length() > 0 && m_AreaList1 != null
				    && m_AreaList1.size() > 0 )
				{
					int nFoundIndex = -1;
					for( int nIndex = 0; nIndex < m_AreaList1.size(); nIndex++ )
					{
						XResultMap areaResult = m_AreaList1.get(nIndex);
						if( areaResult == null )
							continue;
						String strNo = areaResult.get("no");
						if( strNo == null || strNo.length() == 0 )
							continue;
						if( strNo.equals(strArea1) == false )
							continue;
						nFoundIndex = nIndex;
						break;
					}
					if( nFoundIndex != -1 )
						m_spFavorArea1.setSelection(nFoundIndex);
				}
			}
		}
		String strFavorCategory = result.get("goods_kind");
		if( strFavorCategory != null && strFavorCategory.length() > 0 )
		{
			if( m_FavorCategoryList != null && m_FavorCategoryList.size() > 0 )
			{
				int nFoundIndex = -1;
				for( int nIndex = 0; nIndex < m_FavorCategoryList.size(); nIndex++ )
				{
					XResultMap categoryResult = m_FavorCategoryList.get(nIndex);
					if( categoryResult == null )
						continue;
					String strNo = categoryResult.get("no");
					if( strNo == null || strNo.length() == 0 )
						continue;
					if( strNo.equals(strFavorCategory) == false )
						continue;
					nFoundIndex = nIndex;
					break;
				}
				if( nFoundIndex != -1 )
					m_spFavorCategory.setSelection(nFoundIndex);
			}
		}
		String strUseMailing = result.get("mailing");
		if( strUseMailing != null && strUseMailing.length() > 0 )
		{
			if(strUseMailing.equals("no"))
				m_radioEmail.check(R.id.radioButton_email_no);
			else
				m_radioEmail.check(R.id.radioButton_email_yes);
		}
		String strUseSms = result.get("use_sms");
		if( strUseSms != null && strUseSms.length() > 0 )
		{
			if(strUseSms.equals("no"))
				m_radioSms.check(R.id.radioButton_sms_no);
			else
				m_radioSms.check(R.id.radioButton_sms_yes);
		}

		return true;
	}

	@Override
	public boolean onTouch( View v, MotionEvent event )
	{
		if( v.getId() == R.id.editText_birthday )
		{
			showDialog(DATE_DIALOG_ID);
		}
		return false;
	}

	@Override
	public void onFocusChange( View v, boolean hasFocus )
	{
		if( v.getId() == R.id.editText_birthday && hasFocus )
			showDialog(DATE_DIALOG_ID);
	}

	private static final int DATE_DIALOG_ID = 2000;

	protected Dialog onCreateDialog( int id )
	{
		switch( id )
		{
			case DATE_DIALOG_ID:
			{
				int nYear = 1997, nMonth = 0, nDay = 1;
				String strBirth = m_etBirthday.getText().toString();
				if(strBirth != null && strBirth.length() > 0)
				{
					try
					{
    					String strSub = "";
    					int nFound = -1, nStart = 0;
    					nFound = strBirth.indexOf("-", (nStart = nFound + 1));
    					if( nFound != -1 )
    					{
    						strSub = strBirth.substring(nStart, nFound);
    						nYear = Integer.parseInt(strSub);
    
    						nFound = strBirth.indexOf("-", (nStart = nFound + 1));
    						if( nFound != -1 )
    						{
    							strSub = strBirth.substring(nStart, nFound);
    							nMonth = Integer.parseInt(strSub) - 1;
    
    							strSub = strBirth.substring(nFound + 1);
    							nDay = Integer.parseInt(strSub);
    						}
    					}
					}
					catch(NumberFormatException e)
					{
						e.printStackTrace();
					}
				}
				return new DatePickerDialog(this, m_DateSetListener, nYear, nMonth, nDay);
			}
		}

		return null;
	}

	@SuppressLint("DefaultLocale")
	private DatePickerDialog.OnDateSetListener m_DateSetListener = new DatePickerDialog.OnDateSetListener()
	{
        @Override
		public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth )
		{
			String strDate = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
			m_etBirthday.setText(strDate);
			m_spDayType.requestFocus();
		}

	};
}

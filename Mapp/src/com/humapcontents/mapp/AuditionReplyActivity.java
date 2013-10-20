package com.humapcontents.mapp;



import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.humapcontents.mapp.AuditionDetailActivity.ReplyData;
import com.humapcontents.mapp.data.HomeWeekLyRank;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class AuditionReplyActivity extends MappBaseActivity implements OnClickListener {


	
	private class AuditionReply
	{
		int  idx;
		String  id;
		String  nick;
		String  date;
		String  text;

	}
	

	
	private AuditionReplyActivity self;
	
	int m_SelectStageIndex = 0;

	
	
	ArrayList< AuditionReply > 	m_ListADData;
	private AuditionReply_Adapter m_Adapter;
	
    Integer m_Offset  = -1; 
    Integer m_SortIndex = 0;
    Boolean m_Refresh = true;
    Integer m_TextConut = 0;

	private boolean mLockListView; 
	
	public ProgressDialog mProgress2;
	private LayoutInflater mInflater;
	private ListView m_ListView;
	private View m_Footer;
	
	public Boolean m_bFooter = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auditionlist_detail_reply_layout);  // 인트로 레이아웃 출력     

        self = this;

        mLockListView = true; 
        
        // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        {
        	mProgress2 = new ProgressDialog(this);
        	mProgress2.setMessage("문제가 생겨 리스트를 갱신중입니다. ");
        	mProgress2.setIndeterminate(true);
        	mProgress2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        	mProgress2.setCancelable(false);
        }
		
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	
        	((TextView)findViewById(R.id.title_name)).setText(_AppManager.m_ReplyTitle);
            ((TextView)findViewById(R.id.title_desc)).setText(_AppManager.m_ReplyDesc);
        }
        
        ImageResize(R.id.main_layout);
        ImageResize(R.id.audition_info_listview);
        {
        	OnScrollListener listen = new OnScrollListener() 
        	{
      		  
     		   public void onScrollStateChanged(AbsListView view, int scrollState) {}
     		  
     		   // 첫번째 아이템이 안 보이면 하단 바를 꺼준다. 
     		   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
     		   {
	        		    
	        		    
	        		    // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이 
	        		    // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다. 
	        		    int count = totalItemCount - visibleItemCount; 

	        		    if(firstVisibleItem >= count && totalItemCount != 0   && mLockListView == false) 
	        		    { 
	        		    	      // 추가
	        		    	
	        		    	if (m_bFooter == true )
	        		    	{
	        		    		GetMoreAudiotionList();
	        		    	}

	        		    		
	        		    } 
     		   }
        	};
        	m_ListView = ((ListView)findViewById(R.id.audition_info_listview));
        	
        	m_ListView.setOnScrollListener(listen);
        	
        }
        
        {
        	
        	
        	FrameLayout imageview = (FrameLayout)findViewById(R.id.title_bar);
            imageview.setOnClickListener(this);
            ((TextView)findViewById(R.id.title_desc)).setOnClickListener(this);
            ((TextView)findViewById(R.id.title_name)).setOnClickListener(this);
        }


    	m_ListADData = new ArrayList< AuditionReply >();
    	
    	m_ListADData.clear();
    	m_Adapter = new AuditionReply_Adapter(this, R.layout.audition_reply_row, m_ListADData);
    	
    	// 푸터를 등록합니다. setAdapter 이전에 해야 합니다. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Footer  = mInflater.inflate(R.layout.footer, null);
        m_ListView.addFooterView(m_Footer);
        
    	m_ListView.setAdapter(m_Adapter);
		//m_ListView.setDivider(null); 
        BottomMenuDown(true);
        AfterCreate( 1 );
        
        ImageResize(R.id.reply_bar);
        
        

        ImageResize(R.id.reply_text);
        ImageBtnResize(R.id.reply_send);
        
        
        
        
        {
        	EditText searchWord = (EditText) findViewById(R.id.reply_text);
        	searchWord.setOnKeyListener(new View.OnKeyListener(){
        	    public boolean onKey(View v, int keyCode, KeyEvent event) {
        	        // 개행은 체크하지 않도록
        	        if (event.getAction() == KeyEvent.ACTION_DOWN) {
        	            return true;
        	        }
        	        // 키입력 체크
        	        if (keyCode ==  KeyEvent.KEYCODE_ENTER) {
        	            EditText word = (EditText) findViewById(R.id.reply_text);
        	            m_TextConut = word.length();
        	            if (word != null && word.length() != 0) 
        	            {
        	                // 뭔가 처리....
        	            	SetWrite();
        	            }  
        	            else if ( word != null && word.length() != 0 )
        	            {
        	            	
        	            }
        	            return true;
        	        }
        	        return false;
        	    }


        	});
        }

        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	
        	if ( _AppManager.m_bLogin == false )
        	{
        		ImageBtnResize(R.id.reply_bar2);
        		
        		View view =  (View)findViewById(R.id.reply_text);
        		view.setVisibility(View.GONE);
        	}
        	else
        	{
        		ImageResize(R.id.reply_bar2);
        	}
        }
		

        GetReplyData();
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	ImageView imageview = (ImageView)findViewById(R.id.title_icon);
           
            imageview.setOnClickListener(this);
        }
    }
    
    public void FooterHide()
    {
    	if (m_bFooter == true)
    	{
    		m_bFooter = false;
    		
    		(m_Footer).setVisibility(View.GONE);
 /*   		 m_ListView.removeFooterView(mInflater.inflate(R.layout.footer, null));
    		m_ListView.setAdapter(m_Adapter);*/
    	}
    }
    public void FooterShow()
    {
    	if (m_bFooter == false)
    	{
    		m_bFooter = true;
    		(m_Footer).setVisibility(View.VISIBLE);
 /*   	    m_ListView.addFooterView(mInflater.inflate(R.layout.footer, null));
    		m_ListView.setAdapter(m_Adapter);*/
    	}
    }
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }




	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		
		switch(arg0.getId() )
		{
		case R.id.title_icon2:
		{

		}
			break;
		case R.id.title_bar:
			break;
		case R.id.title_name:
		case R.id.title_desc:
			
		{

		}
			
			break;
		case	R.id.reply_send:
		{
			EditText word = (EditText) findViewById(R.id.reply_text);
            m_TextConut = word.length();
			if ( m_TextConut != 0)
				SetWrite();
		}
		break;
		case R.id.reply_bar2:
		{
			Toast.makeText(self
                    .getApplicationContext(), 
                    "로그인이 되어 있지 않습니다. 설정화면으로 이동합니다.",
                    Toast.LENGTH_LONG).show();
        	
        	Intent intent;
            intent = new Intent().setClass(baseself, SettingMainActivity.class);
            startActivity( intent ); 
		}
			break;
			
		case R.id.title_icon:
		{
			onBackPressed();
		}
		break;
		
		}
		
	}
	
	@Override
    public void  onResume()
    {
    	super.onResume();
    	{
        	AppManagement _AppManager = (AppManagement) getApplication();
        	
        	if ( _AppManager.m_bLogin == false )
        	{
        		
        		View view =  (View)findViewById(R.id.reply_text);
        		view.setVisibility(View.GONE);
        	}
        	else
        	{
        		View view =  (View)findViewById(R.id.reply_text);
        		view.setVisibility(View.VISIBLE);
        	}

        }
    }
	
	public void RefreshUI()
	{
		
		{
        	AppManagement _AppManager = (AppManagement) getApplication();
        	
        	if ( _AppManager.m_bLogin == false )
        	{
        		
        		View view =  (View)findViewById(R.id.reply_text);
        		view.setVisibility(View.GONE);
        	}
        	else
        	{
        		View view =  (View)findViewById(R.id.reply_text);
        		view.setVisibility(View.VISIBLE);
        	}

        }
		
		
		if (  m_ListADData.size() > 19 )
		{
			FooterShow();
		}
		else
		{
			FooterHide();
		}
		int adcount = 0;
		int listcount = 0;
		


		
		m_Adapter.notifyDataSetChanged();
		
		mLockListView = false;  

	}
	
	public void GetReplyData()
	{
		mLockListView = true;
		{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					Map<String, String> data = new HashMap  <String, String>();

					data.put("idx", _AppManager.m_ReplyIndex.toString());
					data.put("range", "20" );
					data.put("offset", m_Offset.toString() );
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Audition/Detail/Reply/Read", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{
							
							JSONArray usageList = (JSONArray)json.get("reply");
							
							for(int i = 0; i < usageList.length(); i++)
							{
								AuditionReply item = new AuditionReply();
								JSONObject list = (JSONObject)usageList.get(i);
								
								item.idx =  Integer.parseInt(list.optString("idx"));
								item.id = (list.optString("id"));
								item.nick =  (list.optString("nick"));
								item.date =  (list.optString("date"));
								item.text =  (list.optString("text"));
								
								
								m_ListADData.add(item);
							}
							
							
							handler.sendEmptyMessage(0);
						}
						else 
						{
							// 에러 메세지를 전송한다. 
							handler.sendMessage(handler.obtainMessage(1,_AppManager.GetError(json.optInt("errcode")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler.sendMessage(handler.obtainMessage(1,"Error" ));
						e.printStackTrace();
					} 
				}
			});
			thread.start();
		}
	}
	
	
	
	
	public void GetMoreAudiotionList()
	{
		m_Offset = m_ListADData.get(m_ListADData.size() -1 ).idx;
		GetReplyData();
		
		
	}
	

	public void SetWrite( )
	{
	  	{
				final  AppManagement _AppManager = (AppManagement) getApplication();
				mProgress.show();
				Thread thread = new Thread(new Runnable()
				{

					public void run() 
					{

						EditText text = (EditText)findViewById(R.id.reply_text);
						Map<String, String> data = new HashMap  <String, String>();

						data.put("idx", _AppManager.m_ReplyIndex.toString());
						data.put("text", _AppManager.GetHttpManager().EncodeString(text.getText().toString()));
						data.put("id", _AppManager.m_LoginID);
						data.put("skey", _AppManager.m_MappToken);
						
						
						String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Audition/Detail/Reply/Write", data);

						try 
						{
							JSONObject json = new JSONObject(strJSON);
							if(json.optString("errcode").equals("0"))
							{
								handler.sendEmptyMessage(50);
							}
							else if ( json.optString("errcode").equals("1101"))
							{
								handler.sendEmptyMessage(77);
							}
							else 
							{
								// 에러 메세지를 전송한다. 
								handler.sendMessage(handler.obtainMessage(1,_AppManager.GetError(json.optInt("errcode")) ));
								return ;
							}
						} catch (JSONException e) 
						{
							// TODO Auto-generated catch block
							handler.sendMessage(handler.obtainMessage(1,"Error" ));
							e.printStackTrace();
						}
					}
				});
				thread.start();
			}
	}
	
	
	public void SetDelete( final Integer idx)
	{
	  	{
				final  AppManagement _AppManager = (AppManagement) getApplication();
				mProgress.show();
				Thread thread = new Thread(new Runnable()
				{

					public void run() 
					{

						Map<String, String> data = new HashMap  <String, String>();

						data.put("idx", idx.toString());
						data.put("id", _AppManager.m_LoginID);
						data.put("skey", _AppManager.m_MappToken);
						
						
						String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Audition/Detail/Reply/Delete", data);

						try 
						{
							JSONObject json = new JSONObject(strJSON);
							if(json.optString("errcode").equals("0"))
							{
								handler.sendEmptyMessage(40);
							}
							else 
							{
								// 에러 메세지를 전송한다. 
								handler.sendMessage(handler.obtainMessage(1,_AppManager.GetError(json.optInt("errcode")) ));
								return ;
							}
						} catch (JSONException e) 
						{
							// TODO Auto-generated catch block
							handler.sendMessage(handler.obtainMessage(1,"Error" ));
							e.printStackTrace();
						}
					}
				});
				thread.start();
			}
	}
	
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				RefreshUI();
				
				break;
			case 1:
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 2:
				
				break;
				
			case 3:
				GetReplyData();
				break;

			case 20:
				GetReplyData();
				
				break;
			case 40:
			{
				m_ListADData.clear();
				GetReplyData();
				
			}
				break;
			case 50:
			{
				m_ListADData.clear();
				GetReplyData();
				EditText word = (EditText) findViewById(R.id.reply_text);
				word.setText("");
				
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(word.getWindowToken(), 0);
				
				word.clearFocus();
				
			}
			case 77:
			{
				AppManagement _AppManager = (AppManagement) getApplication();
				_AppManager.m_MappToken= "";
				_AppManager.m_bLogin = false;
				_AppManager.m_LoginID = "";
				
	       	 
		        SharedPreferences preferences = getSharedPreferences( "login" ,MODE_PRIVATE);
		        SharedPreferences.Editor editor = preferences.edit();
		        editor.putString("id", _AppManager.m_LoginID ); //키값, 저장값
		        editor.putString("skey",_AppManager.m_MappToken );
		        editor.commit();
		        
				Toast.makeText(self
	                    .getApplicationContext(), 
	                    "로그인이 되어 있지 않습니다. 설정화면으로 이동합니다.",
	                    Toast.LENGTH_LONG).show();
	        	
	        	Intent intent;
	            intent = new Intent().setClass(baseself, SettingMainActivity.class);
	            startActivity( intent ); 
			}
				break;
			default:
				break;
			}

		}
    	
	};
	public class AuditionReply_Adapter extends ArrayAdapter<AuditionReply>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<AuditionReply> mList;
		private LayoutInflater mInflater;
		
    	public AuditionReply_Adapter(Context context, int layoutResource, ArrayList<AuditionReply> mTweetList)
		{
			super(context, layoutResource, mTweetList);
			this.mContext = context;
			this.mResource = layoutResource;
			this.mList = mTweetList;
			this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
    	
    	@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
    		final AuditionReply mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}
			
			LinearLayout frameBar4 = (LinearLayout)convertView.findViewById(R.id.audition_deteil_row_3);
			
			((TextView)convertView.findViewById(R.id.audition_deteil_row_reply_nick)).setText(mBar.nick);
			((TextView)convertView.findViewById(R.id.audition_deteil_row_reply_time)).setText(mBar.date);
			((TextView)convertView.findViewById(R.id.audition_deteil_row_reply)).setText(mBar.text);
			
			
			frameBar4.setOnLongClickListener(new View.OnLongClickListener() 
			{



				public boolean onLongClick(View v) 
				{
					// TODO Auto-generated method stub
					/*if (  _AppManager.m_LoginID.equals(mBar.id) )
					{
						new AlertDialog.Builder(self)
						 .setTitle("경고")
						 .setMessage("리플을 지우시겠습니까?") //줄였음
						 .setPositiveButton(self.getResources().optString(R.string.yes), new DialogInterface.OnClickListener() 
						 {
						     public void onClick(DialogInterface dialog, int whichButton)
						     {   

						    	 // 삭제 
						    	 self.SetDelete( mBar.idx);
						    	 
						     }
						 })
						 .setNegativeButton(self.getResources().optString(R.string.no), new DialogInterface.OnClickListener() 
						 {
						     public void onClick(DialogInterface dialog, int whichButton) 
						     {
						         //...할일
						     }
						 })
						 .show();
					}*/
					return true;
				}
			});

		
			return convertView;
		}
    }
	
	

	
}

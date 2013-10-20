package com.pricecode;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.pricecode.*;
import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XResultList;
import com.rangboq.xutil.XResultMap;

public class SelectAreaActivity extends CMActivity
{
	ExpandableListView m_ListView;

	@Override
    protected void onCreate( Bundle savedInstanceState )
    {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_select_area);
	    
	    setTitleBarText("지역설정");
	    
	    m_ListView = (ExpandableListView) findViewById(R.id.expandableListView_area);
	    
	    XResultList areaList = CMManager.getAreaList();
	    if(areaList == null || areaList.size() == 0)
	    {
        	if(CMHttpConn.reqAreaList(m_Handler) == null)
        		toastRequestFail();
        	else
        		showWait(null);
	    }
	    else
	    {
	    	updateResultList(areaList);
	    }
    }

	@Override
    protected void onDestroy()
    {
		m_Handler = null;
		m_ResultList = null;
		m_AreaList = null;
	    super.onDestroy();
    }

	XHandler m_Handler = new XHandler()
	{
		@Override
        public void handleMessage( Message msg )
        {
			removeTimeoutMsg();
			if(m_Handler == null)
				return;
			
	        if(msg.what == XHandler.RESULT_TIME_OUT)
	        {
	        	cancelWait();
	        	toastCheckInternet();
	        	return;
	        }
	        else if(msg.what == CMHttpConn.TYPE_REQ_AREA_LIST)
			{
	        	cancelWait();
				if( msg.obj != null && msg.obj instanceof CMHttpConn )
				{
					CMHttpConn conn = (CMHttpConn) msg.obj;
					if( conn.m_nWebOpenResult == CMHttpConn.XHTTP_200_OK )
					{
						XResultList resultList = conn.m_XmlParser.getResultList();
						if( resultList != null && resultList.size() > 0 )
						{
							String strResult = resultList.getOtherValue("result");
							if( strResult != null && strResult.equals("true") )
							{
								CMManager.setAreaList(resultList);
    						
								updateResultList(resultList);
    							return;
							}
						}
						
						toastResultIsEmpty();
						return;
					}
				}

				toastCheckInternet();
				return;
			}
	        else if(msg.what == CMHttpConn.TYPE_REQ_SET_MY_AREA)
			{
	        	cancelWait();
				if( msg.obj != null && msg.obj instanceof CMHttpConn )
				{
					CMHttpConn conn = (CMHttpConn) msg.obj;
					if( conn.m_nWebOpenResult == CMHttpConn.XHTTP_200_OK )
					{
						XResultList resultList = conn.m_XmlParser.getResultList();
						if( resultList != null && resultList.size() > 0 )
						{
							XResultMap result = resultList.get(0);
							String strResult = result.get("result");
							if(strResult != null && strResult.equals("true"))
							{
								showAlertAndFinish("지역설정이 변경되었습니다.");
								return;
							}
							
							String strError = result.get("errorMsg");
							if(strError != null)
								showToast(strError);
							else
								toastRequestFail();
							return;
						}
						
						toastRequestFail();
						return;
					}
				}

				toastCheckInternet();
				return;
			}
        }
	};
	
	public class AreaGroupItem extends XResultMap
	{
		private static final long serialVersionUID = -2583948172407191836L;
		
		private XResultList m_ChildList = new XResultList();
		public String m_strNo = "";
		
		public AreaGroupItem(XResultMap parentItem)
		{
			super.putAll(parentItem);
			m_strNo = parentItem.get("no");
			if(m_strNo == null)
				m_strNo = "";
		}
		
		public void addChild(XResultMap childItem)
		{
			if(m_ChildList == null)
				m_ChildList = new XResultList();
			m_ChildList.add(childItem);
		}
		
		public boolean removeChild(XResultMap childItem)
		{
			if(m_ChildList == null)
				return true;
			return m_ChildList.remove(childItem);
		}
		
		public XResultMap removeChild(int nIndex)
		{
			if(m_ChildList == null)
				return null;
			return m_ChildList.remove(nIndex);
		}
		
		public XResultMap getChildAt(int nIndex)
		{
			if(m_ChildList == null || m_ChildList.size() <= nIndex)
				return null;
			return m_ChildList.get(nIndex);
		}

		public int getChildCount()
        {
	        if(m_ChildList == null)
	        	return 0;
	        return m_ChildList.size();
        }
	}
	
	public class AreaGroupList extends ArrayList<AreaGroupItem>
	{
        private static final long serialVersionUID = -7955912643818016754L;
		public AreaGroupList()
		{
		}
	}
	
	XResultList m_ResultList = null;
	AreaGroupList m_AreaList = null;
	void updateResultList( XResultList resultList )
    {
		if( resultList == null || resultList.size() == 0 )
		{
			toastResultIsEmpty();
			return;
		}
		m_ResultList = resultList;
		
		m_AreaList = new AreaGroupList();
		for(XResultMap curMap : resultList)
		{
			String strPNo = curMap.get("pno");
			if( strPNo == null || strPNo.length() == 0 )
				continue;

			if( strPNo.equals("0") )
				m_AreaList.add( new AreaGroupItem(curMap) );
		}
		
		for(XResultMap curMap : resultList)
		{
			if(curMap == null || curMap.size() == 0)
				continue;
			String strPNo = curMap.get("pno");
			if( strPNo == null || strPNo.length() == 0 )
				continue;
			if( strPNo.equals("0") )
				continue;
			
			for(AreaGroupItem curGroup : m_AreaList)
			{
				if(strPNo.equals(curGroup.m_strNo))
				{
					curGroup.addChild(curMap);
					break;
				}
			}
		}
		
		m_AreaAdapter = new SelectAreaAdapter(this, m_AreaList);
		m_ListView.setAdapter(m_AreaAdapter);
		m_ListView.setOnChildClickListener(new OnChildClickListener()
		{
			@Override
			public boolean onChildClick( ExpandableListView parent, View v, int groupPosition,
			                             int childPosition, long id )
			{
				AreaGroupItem groupItem = m_AreaAdapter.getGroup(groupPosition);
				if(groupItem == null || groupItem.size() <= childPosition)
					return false;
				
				XResultMap childItem = groupItem.getChildAt(childPosition);
				if(childItem == null)
					return false;
				
				String strGroupNo = groupItem.get("no");
				String strChildNo = childItem.get("no");
				if( strGroupNo == null || strGroupNo.length() == 0 
					|| strChildNo == null || strChildNo.length() == 0 )
					return false;
				
				String strArea = strGroupNo + "-" + strChildNo;
				String strId = CMManager.getPrefString(CMManager.PREF_USER_ID, "");
				String strPassword = CMManager.getPrefStringS(CMManager.PREF_USER_PASSWORD, "");
				if( strId.length() == 0 || strPassword.length() == 0 )
				{
					showAlert("로그인 정보가 없습니다.");
					return true;
				}
				
				if(CMHttpConn.reqSetMyArea(m_Handler, strId, strPassword, strArea) == null)
					toastCheckInternet();
				else
					showWait(null);
				return true;
			}
		});
     }
	
	SelectAreaAdapter m_AreaAdapter = null;
}

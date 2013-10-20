package com.pricecode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.pricecode.*;
import com.pricecode.SelectAreaActivity.AreaGroupItem;
import com.pricecode.SelectAreaActivity.AreaGroupList;
import com.rangboq.xutil.XResultMap;

public class SelectAreaAdapter extends BaseExpandableListAdapter
{
	SelectAreaActivity m_Parent = null;
	LayoutInflater m_layoutIflater = null;
	AreaGroupList m_AreaList = null;
	LayoutInflater m_Inflater = null;
	
	static final int MAX_CHILD_COUNT = 1000;
	
	public SelectAreaAdapter(SelectAreaActivity parent, AreaGroupList areaList)
	{
		m_Parent = parent;
		m_layoutIflater = m_Parent.getLayoutInflater();
		m_AreaList = areaList;
		m_Inflater = m_Parent.getLayoutInflater();
	}

	@Override
	public XResultMap getChild( int groupPosition, int childPosition )
	{
		if(m_AreaList == null || m_AreaList.size() <= groupPosition)
			return null;
		AreaGroupItem groupItem = m_AreaList.get(groupPosition);
		if(groupItem == null || groupItem.getChildCount() <= childPosition)
			return null;
		return groupItem.getChildAt(childPosition);
	}

	@Override
	public long getChildId( int groupPosition, int childPosition )
	{
		return (groupPosition * MAX_CHILD_COUNT + childPosition);
	}

	@Override
	public int getChildrenCount( int groupPosition )
	{
		if(m_AreaList == null || m_AreaList.size() <= groupPosition)
			return 0;
		AreaGroupItem groupItem = m_AreaList.get(groupPosition);
		if(groupItem == null)
			return 0;
		return groupItem.getChildCount();
	}

	@Override
	public AreaGroupItem getGroup( int groupPosition )
	{
		if(m_AreaList == null || m_AreaList.size() <= groupPosition)
			return null;
		return m_AreaList.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		if(m_AreaList == null)
			return 0;
		return m_AreaList.size();
	}

	@Override
	public long getGroupId( int groupPosition )
	{
		return (groupPosition * MAX_CHILD_COUNT);
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public boolean isChildSelectable( int groupPosition, int childPosition )
	{
		return true;
	}


	
	
	@Override
	public View getGroupView( int groupPosition, boolean isExpanded, View convertView, ViewGroup parent )
	{
		if(convertView == null)
			convertView = m_Inflater.inflate(R.layout.list_item_area_group, null);
		if( convertView == null || m_AreaList == null )
			return null;

		AreaGroupItem groupItem = getGroup(groupPosition);
		if(groupItem == null)
			return convertView;
		
		TextView tvName = (TextView) convertView.findViewById(R.id.textView_area_group);
		if(tvName == null)
			return convertView;
		
		String strSubject = groupItem.get("subject");
		if(strSubject == null)
			strSubject = "";
		tvName.setText(strSubject);

		return convertView;
	}

	@Override
	public View getChildView( int groupPosition, int childPosition, boolean isLastChild,
	                          View convertView, ViewGroup parent )
	{
		if(convertView == null)
			convertView = m_Inflater.inflate(R.layout.list_item_area_child, null);
		if( convertView == null || m_AreaList == null )
			return null;

		XResultMap childItem = getChild(groupPosition, childPosition);
		if(childItem == null)
			return convertView;
		
		TextView tvName = (TextView) convertView.findViewById(R.id.textView_area_child);
		if(tvName == null)
			return convertView;
		
		String strSubject = childItem.get("subject");
		if(strSubject == null)
			strSubject = "";
		tvName.setText(strSubject);

		return convertView;
	}
}

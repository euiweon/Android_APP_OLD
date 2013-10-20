package com.hoteljoin.data;

import java.util.ArrayList;

// 이벤트 배너 
public class EventBannerList {

	public String totalCount;
	
	public ArrayList< eventListData> eventList =new ArrayList< eventListData> ();
	
	public static class eventListData
	{
		public String imageUrl;
		public String imageWidth;
		public String imageHeight;
		public String linkTypeCode;
		public String linkCode;

	}
}

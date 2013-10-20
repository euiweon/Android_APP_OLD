package com.hoteljoin.data;

import java.util.ArrayList;



public class EventList {
	public String currPage;
	public String totalPage;
	public String numPerPage;
	
	public ArrayList<eventListData> eventList = new  ArrayList<eventListData>();
	public static  class eventListData
	{
		public String eventNum;
		public String subject;
		public String summary;
		public String bgnDay;
		public String endDay;
		public String imageUrl;
	}
}

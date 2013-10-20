package com.hoteljoin.data;

import java.util.ArrayList;



// 공지사항 
public class NoticeList {
	public String currPage;
	public String totalPage;
	public String numPerPage;
	
	public ArrayList<noticeListData> noticeList = new  ArrayList<noticeListData>();
	public static class noticeListData
	{
		public String wid;
		public String subject;
		public String name;
		public String regDay;
		public String newYn;

	}

}

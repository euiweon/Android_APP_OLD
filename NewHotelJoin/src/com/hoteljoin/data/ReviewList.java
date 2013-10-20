package com.hoteljoin.data;

import java.util.ArrayList;



public class ReviewList {
	public String currPage;
	public String totalPage;
	public String numPerPage;
	
	public ArrayList<boardListData> boardList = new  ArrayList<boardListData>();
	public static class boardListData
	{
		public String boardNum;
		public String subject;
		public String writerName;
		public String regDay;
		public String rating;
		public String recommendCount;
		public String hitCount;
		public String replyCount;
	}

}

package com.hoteljoin.data;

import java.util.ArrayList;


public class DiaryReplyList {
	
	public String currPage;
	public String totalPage;
	public String numPerPage;

	public ArrayList<replyListData> replyList = new  ArrayList<replyListData>();
	public static class replyListData
	{
		public String replyNum;
		public String memberId;
		public String writerName;
		public String contents;
		public String regDay;

	}
}

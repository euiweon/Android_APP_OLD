package com.hoteljoin.data;

import java.util.ArrayList;


public class ReviewDetailReplies {
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

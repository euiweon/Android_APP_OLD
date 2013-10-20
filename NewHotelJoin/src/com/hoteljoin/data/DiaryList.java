package com.hoteljoin.data;

import java.util.ArrayList;


// 여행일지 목록
public class DiaryList {

	public String currPage;
	public String totalPage;
	public String numPerPage;
	
	public static class tourDiaryListData
	{

		public String diaryNum;
		public String subject;
		public String contents;
		public String imageUrl;
		public String writerName;
		public String regDay;
		public String nationCode;
		public String cityCode;
		public String hotelCode;
		public String hotelName;

		public String recommendCount;
		public String hitCount;
		public String replyCount;

	}
	
	public ArrayList< tourDiaryListData > tourDiaryList = new ArrayList< tourDiaryListData > ();
	
}

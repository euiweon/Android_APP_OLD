package com.hoteljoin.data;

import java.util.ArrayList;


// 이용후기 상세 조회
public class ReviewDetail
{
	public boardInfoData boardInfo = new boardInfoData(); 
	public ArrayList<ratingListData> ratingList = new ArrayList<ratingListData>();
	public ArrayList<String >  imageList = new ArrayList<String >();
	public static class boardInfoData
	{
		public String prevBoardNum;
		public String nextBoardNum;
		public String subject;
		public String contents;
		public String writerName;
		public String regDay;
		public String hitCount;
		public String recommendCount;
		public String replyCount;
		public String rating;
		public String hotelCode;
		public String hotelName;
		public String nationCode;
		public String nationName;
		public String cityCode;
		public String cityName;
	}
	
	public static class ratingListData
	{
		public String rating;
		public String typeCode;
		public String typeName;

	}
}

package com.hoteljoin.data;

import java.util.ArrayList;


// 4. 호텔 상세정보 
public class HotelDetail {
	
	public String hotelCode;
	public String checkinDay;
	public String duration;
	public String numOfRooms;
	public String numPerRoom;

	public hotelInfoData hotelInfo = new hotelInfoData();
	public ArrayList <imageListData > imageList = new  ArrayList <imageListData >();
	public ArrayList <facilityListData > facilityList = new  ArrayList <facilityListData >();
	 
	public class hotelInfoData
	{
		public hotelInfoData()
		{
			
		}

			
		public String hotelCode;
		public String hotelName;
		public String hotelNameEn;
		public String nationCode;
		public String nationName;
		public String nationNameEn;
		public String cityCode;
		public String cityName;
		public String cityNameEn;
		public String starRating;
		public String checkinTime;
		public String checkoutTime;
		public String roomCount;
		public String latitude;
		public String longitude;
		public String address;
		public String price;
		public String numOfDiary;
		public String numOfReview;
		public String rating;
		public String description;
		public String traffic;
		public String event;
	}
	
	public static class imageListData
	{
		public String primaryYn;
		public String smallImageUrl;
		public String middleImageUrl;
	}
	
	public static class facilityListData
	{
		public String typeCode;
		public String facilityCode;
		public String facilityName;

	}
}

package com.hoteljoin.data;

import java.util.ArrayList;

public class EventDetail {

	public eventInfoData eventInfo = new eventInfoData();
	public ArrayList<domHotelListData> domHotelList = new ArrayList<domHotelListData>();
	public ArrayList<intHotelListData> intHotelList = new ArrayList<intHotelListData>();
	public class eventInfoData
	{
		public String eventNum;
		public String subject;
		public String summary;
		public String 	bgnDay;
		public String bgnTime;
		public String endDay;
		public String endTime;
		public String imageUrl;
		public String imageWidth;
		public String imageHeight;
		public String pageTypeCode;
		public String hbrdTypeCode;
		public String linkHotelCode;
		public String linkCouponId;
		public String buyCount;

	}
	public static class domHotelListData
	{
		public String hotelCode;
		public String hotelName;
		public String imageUrl;
		public String starRating;
		public String starRatingName;
		public String hotelAddrs;
		public String hotelPrice;

	}
	public static class intHotelListData
	{
		public String hotelCode;
		public String hotelName;
		public String imageUrl;
		public String starRating;
		public String starRatingName;
		public String hotelAddrs;
		public String hotelPrice;

	}
}

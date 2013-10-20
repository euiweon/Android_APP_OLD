package com.hoteljoin.data;

import java.util.ArrayList;


// 7. 내주면 호텔목록 조회
public class HotelNearbyList {

	public String cityCode;
	public String cityName;
	public ArrayList < hotelListData > hotelList = new  ArrayList < hotelListData >();
	public static class hotelListData
	{
		public String hotelCode;
		public String hotelName;
		public String starRating;
		public String latitude;
		public String longitude;
		public String price;
	}
	
}

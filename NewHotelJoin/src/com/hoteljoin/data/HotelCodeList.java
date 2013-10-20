package com.hoteljoin.data;

import java.util.ArrayList;


// 2. 호텔코드목록 
public class HotelCodeList {
	
	public ArrayList<hotelListData> hotelList = new ArrayList<hotelListData>();
	
	public static class hotelListData
	{
		public String hotelName;
		public String hotelCode;
	}


}

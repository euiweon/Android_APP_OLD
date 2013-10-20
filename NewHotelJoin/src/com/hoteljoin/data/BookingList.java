package com.hoteljoin.data;

import java.util.ArrayList;




// 예약현황조회 
public class BookingList {
	public static  class resvListData
	{

			
		public String resvNum;
		public String hotelName;
		public String nationCode;
		public String resvStatusCode;
		public String resvStatusName;

	}
	public ArrayList< resvListData > resvList =new  ArrayList< resvListData >();
}

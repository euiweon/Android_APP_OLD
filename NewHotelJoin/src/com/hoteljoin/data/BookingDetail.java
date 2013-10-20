package com.hoteljoin.data;

import java.util.ArrayList;

public class BookingDetail {

	public resvInfoData resvInfo = new resvInfoData();
	public String chargeOnCancelYn;
	public ArrayList<chargeConditionsData> chargeConditions  = new ArrayList<chargeConditionsData>();
	public ArrayList<String> optionList = new ArrayList<String>();
	
	public class resvInfoData
	{
		public String hotelCode="";
		public String hotelName="";
		public String checkinDay="";
		public String checkoutDay="";
		public String duration="";
		public String roomName="";
		public String roomCount="";
		public String hotelAddrs;
		public String hotelTel;
		public String resvName;
		public String guestName;

		public String adultCount="";
		public String childCount="";
		public String resvStatusCode="";
		public String resvStatusName="";
		public String payStatusCode="";
		public String payStatusName="";
		public String breakfastYn="";
		public String addInfo="";
		public String cityCode="";
		public String cityName="";
		public String nationCode="";
		public String nationName="";
	}
	
	
	
	
	public static class chargeConditionsData
	{
		public String chargeYn;
		public String fromDate;
		public String currencyCode;
		public String chargeAmount;

	}
	
}

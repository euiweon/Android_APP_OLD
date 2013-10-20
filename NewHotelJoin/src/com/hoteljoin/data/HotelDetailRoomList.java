package com.hoteljoin.data;

import java.util.ArrayList;

// 5. 객실 가격목록
public class HotelDetailRoomList {
	
	public String hotelCode;
	public String checkinDay;
	public String duration;
	public String numOfRooms;
	public String numPerRoom;
	public String nationCode;
	public String cityCode;

	
	public ArrayList<roomPriceListData> roomPriceList = new ArrayList<roomPriceListData>();
	
	public static class roomPriceListData

	{
		public roomPriceListData()
		{
					
		}
		public String roomCode;
		public String roomName;
		public String roomIncInfo;
		public String roomPrice;
		public String breakfastYn;
		public String availableCode;
		public String promoYn;
		public String promoDesc;
		public String continueType;
		public String continueDay;
		public String supplyCode;
		public String roomRequestKey;
	}

}

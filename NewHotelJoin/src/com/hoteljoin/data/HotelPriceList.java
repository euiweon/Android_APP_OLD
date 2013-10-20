package com.hoteljoin.data;

import java.util.ArrayList;

// 3. 호텔가격목록
public class HotelPriceList {

	public String currPage;
	public String totalPage;
	
	public String totalCount;
	public String numPerPage;
	public String distanceFilterYn;
	public ArrayList<hotelPriceListData> hotelPriceList = new ArrayList<hotelPriceListData>();
	public ArrayList<poiInfoData> poiInfo = new  ArrayList<poiInfoData>();;
	
	public static class hotelPriceListData
	{
		public String supplyCode;
		public String nationCode;
		public String cityCode;
		public String hotelCode;
		public String hotelName;
		public String hotelNameEn;
		public String starRating;
		public String starRatingName;
		public String bestYn;
		public String bestSortSeq;
		public String breakfastYn;
		public String availableCode;
		public String promoYn;
		public String promoDesc;
		public String roomPrice;
		public String rating;
		public String latitude;
		public String longitude;
		public String distance;
		public String distanceText;
		public String thumbNailUrl;
	}
	
	public static class poiInfoData
	{
		public String poiName;
		public String poiLatitude;
		public String poiLongitude;

	}

	
}

package com.hoteljoin.data;

import java.util.ArrayList;

import com.hoteljoin.data.HotelDetailRoomOptionList.optionListData;

// 8. ¿¹¾àÆû Àü¹® 
public class BookingForm 
{
	public String roomName;	
	public String roomCode	;
	public String roomPrice	;
	public String totalPrice	;
	public String numRooms;	
	public String lguCstMid;	
	public String nonRefundableYn;	
	public String priceFormula;	
	public String currencyCode;	
	public String nativeRoomPrice;	
	public String nativeCurrencyCode;	
	public String promoYn;	
	public String promoCode;	
	public String promoDesc;	
	public String breakfastYn;	
	public String bookRequestKey;	
	public String nonResvInfoUpdateYn;	
	public String mileage;
	public String giftMoney;
	public String couponCount;
	
	public ArrayList<chargeConditionsData > chargeConditions = new ArrayList<chargeConditionsData >();
	public ArrayList<ArrayList<roomPersonListData > > roomPersonList = new ArrayList<ArrayList<roomPersonListData > >();
	public ArrayList<roomPersonListData > personList = new ArrayList<roomPersonListData >();
	public ArrayList<remarkListData > remarkList = new ArrayList<remarkListData >();
	public ArrayList<useAlertListData > useAlertList = new ArrayList<useAlertListData >();
	public reqInfoData reqInfo = new reqInfoData();
	public ArrayList<optionListData> optionList = new  ArrayList<optionListData>();
	public static class chargeConditionsData
	{
		public String chargeYn;
		public String fromDate;
		public String currencyCode;
		public String chargeAmount;
		


	}

	public static  class roomPersonListData
	{
		public String numAdultName;
		public String bedTypeCode;
		public String roomTypeDetailCode;
	}
	
	public static class remarkListData 
	{
		public String remarkCode;
		public String remarkName;
	}
	
	public static class useAlertListData 
	{
		public String alertTitle;
		public String alertContents;
	}
	
	public static class reqInfoData 
	{
		public String supplyCode;
		public String hotelCode;
		public String checkinDay;
		public String duration;
		public String roomCount1;
		public String numAdults1;
		public String roomCode;
		public String roomRequestKey;
		public String closeDate;
		public String toDate;
		public String guestCountAdult;
		public String guestCountChild;

	}
	
	
	public static class optionListData
	{
		public optionListData()
		{
					
		}
		public String optionCode;
		public String optionName;
		public String optionSendPrice;
		public String optionPrice;
		public String optionPriceType;
		public String optionMethodType;
	}
	



	
}

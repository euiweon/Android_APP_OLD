package com.hoteljoin.data;

import java.util.ArrayList;

// 11. 추가 할인 받기 정보 
public class ExtraDiscountInfo {

	public String totalPrice;
	public String residueMileage;
	public ArrayList<String>  mileageList = new ArrayList<String> ();
	public ArrayList<couponListData>  couponList = new ArrayList<couponListData> ();
	public ArrayList<giftListData>  giftList = new ArrayList<giftListData> ();
	
	
	public static  class couponListData
	{
		public String couponId;
		public String couponIssueNo;
		public String couponDcAmount;
		public String couponName;

	}
	
	public static class giftListData
	{
		public String ticketSheetId;
		public String ticketName;
		public String ticketPrice;
		public String remainder;
		public String unitCodeRate;
		public String payCardPeeRate;
		public String deliveryCostRate;
	}

}

package com.hoteljoin.data;

import java.util.ArrayList;

// 6.  按角 可记格废(惫郴)
public class HotelDetailRoomOptionList {

	public ArrayList<optionListData> optionList = new  ArrayList<optionListData>();
	public static class optionListData
	{

		public String optionCode;
		public String optionName;
		public String optionSendPrice;
		public String optionPrice;
		public String optionPriceType;
		public String optionMethodType;
	}
}

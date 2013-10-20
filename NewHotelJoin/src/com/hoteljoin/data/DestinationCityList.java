package com.hoteljoin.data;

import java.util.ArrayList;


// 1. 목적지 도시 목록
public class DestinationCityList {
public ArrayList<destinationListData> destinationList = new ArrayList<destinationListData>();
	
	public static class destinationListData
	{
		public String name;
		public String code;
	}

}

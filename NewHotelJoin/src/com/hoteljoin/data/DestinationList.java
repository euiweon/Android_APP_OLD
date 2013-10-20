package com.hoteljoin.data;

import java.util.ArrayList;

// 목적지 조회 
public class DestinationList 
{
	public ArrayList<destinationListData> destinationList = new ArrayList<destinationListData>();
	
	public static class destinationListData
	{
		public String name;
		public String code;
	}



}

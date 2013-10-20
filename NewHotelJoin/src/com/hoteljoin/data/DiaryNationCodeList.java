package com.hoteljoin.data;

import java.util.ArrayList;

// 여행일지 국가코드 
public class DiaryNationCodeList {

	public ArrayList < nationListData >  nationList = new  ArrayList < nationListData >();
	
	public static class  nationListData
	{
		public String nationNameEn;
		public String nationName;
		public String nationCode;

	}
}

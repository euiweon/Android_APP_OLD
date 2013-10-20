package com.hoteljoin.data;

import java.util.ArrayList;

// 문의목록
public class ConsultList {
	public String currPage;
	public String totalPage;
	public String numPerPage;
	public ArrayList<consultListData> consultList = new  ArrayList<consultListData>();
	public static class consultListData
	{
		public String consultId;
		public String subject;
		public String status;
		public String statusName;
		public String regDay;

	}
}

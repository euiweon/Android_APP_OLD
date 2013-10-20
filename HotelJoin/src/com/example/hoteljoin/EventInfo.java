package com.example.hoteljoin;

import com.google.android.gms.maps.model.LatLng;

public class EventInfo 
{
	EventInfo ()
	{
		
	}
	EventInfo(LatLng pos1 ,String title1 , String id )
	{
		pos= pos1;
		title = title1;
		ID = id;
	}
	public LatLng pos;
	public String title;
	public String ID;
	
	// 내주변 목록 호텔코드 
	 public String hotelCode;
	 public String hotelName;
	 public String starRating;
	 public String latitude;
	 public String longitude;
	 public String price;
	 
}

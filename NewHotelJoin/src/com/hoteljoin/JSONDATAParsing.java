package com.hoteljoin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.hoteljoin.data.*;
import com.hoteljoin.data.BookingForm.roomPersonListData;


public class JSONDATAParsing {

	AppManagement _AppManager;
	public JSONDATAParsing(AppManagement app)
	{
		_AppManager = app;
	}
	
	
	// 목적지 목록 조회 
	public void SearchDestinationListParsing( String strJSON )
	{
		

		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				JSONArray usageList = (JSONArray)json.get("destinationList");
				_AppManager.PParsingData.destinationList.destinationList.clear();
				// 검색 정보를 얻는다. 
				for(int i = 0; i < usageList.length(); i++)
				{
					DestinationList.destinationListData item = new DestinationList.destinationListData();
					JSONObject list = (JSONObject)usageList.get(i);
					
					item.name = (list.optString("name"));
					item.code =  (list.optString("code"));
					_AppManager.PParsingData.destinationList.destinationList.add(item);
				}
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else 
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	// 1. 목적지 도시목록 
	public void SearchDestinationCityListParsing( String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				JSONArray usageList = (JSONArray)json.get("destinationList");
				
				_AppManager.PParsingData.destinationCityList.destinationList.clear();
				// 검색 정보를 얻는다. 
				for(int i = 0; i < usageList.length(); i++)
				{
					DestinationCityList.destinationListData item = new DestinationCityList.destinationListData();
					JSONObject list = (JSONObject)usageList.get(i);
					
					item.name = (list.optString("name"));
					item.code =  (list.optString("code"));
					_AppManager.PParsingData.destinationCityList.destinationList.add(item);
				}
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else 
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	// 2. 호텔코드목록 조회 
	public void SearcHotelCodeListParsing( String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				JSONArray usageList = (JSONArray)json.get("hotelList");
				
				_AppManager.PParsingData.hotelCodeList.hotelList.clear();
				// 검색 정보를 얻는다. 
				for(int i = 0; i < usageList.length(); i++)
				{
					HotelCodeList.hotelListData item = new HotelCodeList.hotelListData();
					JSONObject list = (JSONObject)usageList.get(i);
					
					item.hotelCode = (list.optString("hotelCode"));
					item.hotelName = (list.optString("hotelName"));
					_AppManager.PParsingData.hotelCodeList.hotelList.add(item);
				}
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else 
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	// 3
	public void SearchHotelPriceListParsing (String strJSON , boolean bFirstPage  )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.optString("errorCode").equals("0"))
			{
				if ( bFirstPage)
				{
					_AppManager.PParsingData.hotelPriceList.poiInfo.clear();
					_AppManager.PParsingData.hotelPriceList.hotelPriceList.clear();					
				}
				
				_AppManager.PParsingData.hotelPriceList.currPage = json.optString("currPage");
				_AppManager.PParsingData.hotelPriceList.distanceFilterYn = json.optString("distanceFilterYn");
				_AppManager.PParsingData.hotelPriceList.numPerPage = json.optString("numPerPage");
				_AppManager.PParsingData.hotelPriceList.totalCount = json.optString("totalCount");
				_AppManager.PParsingData.hotelPriceList.totalPage = json.optString("totalPage");
				
				
				{
					_AppManager.PParsingData.hotelPriceList.hotelPriceList.clear();
					JSONArray usageList = (JSONArray)json.optJSONArray("hotelPriceList");
					
					// 검색 정보를 얻는다. 
					for(int i = 0; i < usageList.length(); i++)
					{
						HotelPriceList.hotelPriceListData item = new HotelPriceList.hotelPriceListData();
						JSONObject list = (JSONObject)usageList.get(i);
						item.supplyCode =  (list.optString("supplyCode"));
						item.nationCode =  (list.optString("nationCode"));				
						item.cityCode =  (list.optString("cityCode"));
						item.hotelCode =  (list.optString("hotelCode"));
						item.hotelName =  (list.optString("hotelName"));
						item.hotelNameEn =  (list.optString("hotelNameEn"));
						item.starRating =  (list.optString("starRating"));
						item.starRatingName =  (list.optString("starRatingName"));
						item.bestYn =  (list.optString("bestYn"));
						item.bestSortSeq =  (list.optString("bestSortSeq"));
						item.breakfastYn =  (list.optString("breakfastYn"));
						item.availableCode =  (list.optString("availableCode"));
						item.promoYn =  (list.optString("promoYn"));
						item.promoDesc =  (list.optString("promoDesc"));
						item.roomPrice =  (list.optString("roomPrice"));
						item.rating =  (list.optString("rating"));
						item.latitude =  (list.optString("latitude"));
						item.longitude =  (list.optString("longitude"));
						item.distance =  (list.optString("distance"));
						item.distanceText =  (list.optString("distanceText"));
						item.thumbNailUrl =  (list.optString("thumbNailUrl"));

						_AppManager.PParsingData.hotelPriceList.hotelPriceList.add(item);
						
					}
				}
				
				{
					_AppManager.PParsingData.hotelPriceList.poiInfo.clear();
					JSONArray usageList = (JSONArray)json.optJSONArray("poiInfo");
					
					if ( usageList != null )
					{
						for(int i = 0; i < usageList.length(); i++)
						{
							HotelPriceList.poiInfoData item = new HotelPriceList.poiInfoData();
							JSONObject list = (JSONObject)usageList.get(i);
							item.poiName =  (list.optString("poiName"));
							item.poiLatitude =  (list.optString("poiLatitude"));				
							item.poiLongitude =  (list.optString("poiLongitude"));


							_AppManager.PParsingData.hotelPriceList.poiInfo.add(item);
							
						}
					}
					
				}
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";

				
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.optString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	// 4. 호텔상세정보
	public void SearchHotelDetail (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{


				_AppManager.PParsingData.hotelDetail.hotelCode = json.optString("hotelCode");
				_AppManager.PParsingData.hotelDetail.checkinDay = json.optString("checkinDay");
				_AppManager.PParsingData.hotelDetail.duration = json.optString("duration");
				_AppManager.PParsingData.hotelDetail.numOfRooms = json.optString("numOfRooms");
				_AppManager.PParsingData.hotelDetail.numPerRoom = json.optString("numPerRoom");
				
				{
					JSONObject usageList = (JSONObject)json.get("hotelInfo");
					_AppManager.PParsingData.hotelDetail.hotelInfo.address = usageList.optString("address");
					_AppManager.PParsingData.hotelDetail.hotelInfo.checkinTime = usageList.optString("checkinTime");
					_AppManager.PParsingData.hotelDetail.hotelInfo.checkoutTime = usageList.optString("checkoutTime");
					_AppManager.PParsingData.hotelDetail.hotelInfo.cityCode = usageList.optString("cityCode");
					_AppManager.PParsingData.hotelDetail.hotelInfo.cityName = usageList.optString("cityName");
					_AppManager.PParsingData.hotelDetail.hotelInfo.cityNameEn = usageList.optString("cityNameEn");
					_AppManager.PParsingData.hotelDetail.hotelInfo.description = usageList.optString("description");
					_AppManager.PParsingData.hotelDetail.hotelInfo.event = usageList.optString("event");
					_AppManager.PParsingData.hotelDetail.hotelInfo.hotelCode = usageList.optString("hotelCode");
					_AppManager.PParsingData.hotelDetail.hotelInfo.hotelName = usageList.optString("hotelName");
					_AppManager.PParsingData.hotelDetail.hotelInfo.hotelNameEn = usageList.optString("hotelNameEn");
					_AppManager.PParsingData.hotelDetail.hotelInfo.latitude = usageList.optString("latitude");
					_AppManager.PParsingData.hotelDetail.hotelInfo.longitude = usageList.optString("longitude");
					_AppManager.PParsingData.hotelDetail.hotelInfo.nationCode = usageList.optString("nationCode");
					_AppManager.PParsingData.hotelDetail.hotelInfo.nationName = usageList.optString("nationName");
					_AppManager.PParsingData.hotelDetail.hotelInfo.nationNameEn = usageList.optString("nationNameEn");
					_AppManager.PParsingData.hotelDetail.hotelInfo.numOfDiary = usageList.optString("numOfDiary");
					_AppManager.PParsingData.hotelDetail.hotelInfo.numOfReview = usageList.optString("numOfReview");
					_AppManager.PParsingData.hotelDetail.hotelInfo.price = usageList.optString("price");
					_AppManager.PParsingData.hotelDetail.hotelInfo.rating = usageList.optString("rating");
					_AppManager.PParsingData.hotelDetail.hotelInfo.roomCount = usageList.optString("roomCount");
					_AppManager.PParsingData.hotelDetail.hotelInfo.starRating = usageList.optString("starRating");
					_AppManager.PParsingData.hotelDetail.hotelInfo.traffic = usageList.optString("traffic");
				}
				
				
				{
					_AppManager.PParsingData.hotelDetail.imageList.clear();
					JSONArray usageList = (JSONArray)json.get("imageList");
					
					// 검색 정보를 얻는다. 
					for(int i = 0; i < usageList.length(); i++)
					{
						HotelDetail.imageListData item = new HotelDetail.imageListData();
						JSONObject list = (JSONObject)usageList.get(i);
						item.primaryYn =  (list.optString("primaryYn"));
						item.middleImageUrl =  (list.optString("middleImageUrl"));				
						item.smallImageUrl =  (list.optString("smallImageUrl"));


						_AppManager.PParsingData.hotelDetail.imageList.add(item);
						
					}
				}
				
				{
					_AppManager.PParsingData.hotelDetail.facilityList.clear();
					JSONArray usageList = (JSONArray)json.get("facilityList");
					
					// 검색 정보를 얻는다. 
					for(int i = 0; i < usageList.length(); i++)
					{
						HotelDetail.facilityListData item = new HotelDetail.facilityListData();
						JSONObject list = (JSONObject)usageList.get(i);
						item.typeCode =  (list.optString("typeCode"));
						item.facilityCode =  (list.optString("facilityCode"));				
						item.facilityName =  (list.optString("facilityName"));


						_AppManager.PParsingData.hotelDetail.facilityList.add(item);
						
					}
				}
				
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";

			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	// 5. 객실 가격목록
	public void SearchHotelDetailRoomList (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PParsingData.hotelDetailRoomList.hotelCode = json.optString("hotelCode");
				_AppManager.PParsingData.hotelDetailRoomList.checkinDay = json.optString("checkinDay");
				_AppManager.PParsingData.hotelDetailRoomList.duration = json.optString("duration");
				_AppManager.PParsingData.hotelDetailRoomList.numOfRooms = json.optString("numOfRooms");
				_AppManager.PParsingData.hotelDetailRoomList.numPerRoom = json.optString("numPerRoom");
				_AppManager.PParsingData.hotelDetailRoomList.cityCode = json.optString("cityCode");
				_AppManager.PParsingData.hotelDetailRoomList.nationCode = json.optString("nationCode");
				
				
				{
					_AppManager.PParsingData.hotelDetailRoomList.roomPriceList.clear();
					JSONArray usageList = (JSONArray)json.get("roomPriceList");
					
					// 검색 정보를 얻는다. 
					for(int i = 0; i < usageList.length(); i++)
					{
						HotelDetailRoomList.roomPriceListData item = new HotelDetailRoomList.roomPriceListData();
						JSONObject list = (JSONObject)usageList.get(i);
						
						item.roomCode =  (list.optString("roomCode"));
						item.roomName =  (list.optString("roomName"));				
						item.roomIncInfo =  (list.optString("roomIncInfo"));
						
						item.roomCode =  (list.optString("roomPrice"));
						item.roomName =  (list.optString("breakfastYn"));				
						item.roomIncInfo =  (list.optString("availableCode"));
						item.roomCode =  (list.optString("promoYn"));
						item.roomName =  (list.optString("promoDesc"));				
						item.roomIncInfo =  (list.optString("continueType"));
						item.roomCode =  (list.optString("continueDay"));
						item.roomName =  (list.optString("supplyCode"));				
						item.roomIncInfo =  (list.optString("roomRequestKey"));
						item.roomCode =  (list.optString("roomCode"));



						_AppManager.PParsingData.hotelDetailRoomList.roomPriceList.add(item);
						
					}
				}
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
				
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}

	// 6. 객실 옵션목록
	public void SearchHotelDetailRoomOptionList (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				{
					
					_AppManager.PParsingData.hotelDetailRoomOptionList.optionList.clear();
					JSONArray usageList = (JSONArray)json.get("optionList");
					
					// 검색 정보를 얻는다. 
					for(int i = 0; i < usageList.length(); i++)
					{
						HotelDetailRoomOptionList.optionListData item = new HotelDetailRoomOptionList.optionListData();
						JSONObject list = (JSONObject)usageList.get(i);	
						item.optionCode =  (list.optString("optionCode"));
						item.optionName =  (list.optString("optionName"));				
						item.optionSendPrice =  (list.optString("optionSendPrice"));				
						item.optionPrice =  (list.optString("optionPrice"));
						item.optionPriceType =  (list.optString("optionPriceType"));				
						item.optionMethodType =  (list.optString("optionMethodType"));

						_AppManager.PParsingData.hotelDetailRoomOptionList.optionList.add(item);
						
					}
				}
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	// 7. 내주변 호텔목록 조회 
	public void SearchHotelNearbyList (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{


				_AppManager.PParsingData.hotelNearbyList.cityCode = json.optString("cityCode");
				_AppManager.PParsingData.hotelNearbyList.cityName = json.optString("cityName");

				
				{
					
					_AppManager.PParsingData.hotelNearbyList.hotelList.clear();
					JSONArray usageList = (JSONArray)json.get("hotelList");


					// 검색 정보를 얻는다. 
					for(int i = 0; i < usageList.length(); i++)
					{
						HotelNearbyList.hotelListData item = new HotelNearbyList.hotelListData();
						JSONObject list = (JSONObject)usageList.get(i);	
						item.hotelCode =  (list.optString("hotelCode"));
						item.hotelName =  (list.optString("hotelName"));				
						item.starRating =  (list.optString("starRating"));				
						item.latitude =  (list.optString("latitude"));
						item.longitude =  (list.optString("longitude"));				
						item.price =  (list.optString("price"));

						_AppManager.PParsingData.hotelNearbyList.hotelList.add(item);
						
					}
				}
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	// 8. 예약폼 전문 
	public void BookingFormParsing (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PParsingData.bookingForm.roomName = json.optString("roomName");
				_AppManager.PParsingData.bookingForm.roomCode = json.optString("roomCode");
				_AppManager.PParsingData.bookingForm.roomPrice = json.optString("roomPrice");
				_AppManager.PParsingData.bookingForm.totalPrice = json.optString("totalPrice");
				_AppManager.PParsingData.bookingForm.numRooms = json.optString("numRooms");
				_AppManager.PParsingData.bookingForm.lguCstMid = json.optString("lguCstMid");
				_AppManager.PParsingData.bookingForm.nonRefundableYn = json.optString("nonRefundableYn");
				_AppManager.PParsingData.bookingForm.priceFormula = json.optString("priceFormula");
				_AppManager.PParsingData.bookingForm.currencyCode = json.optString("currencyCode");
				_AppManager.PParsingData.bookingForm.nativeRoomPrice = json.optString("nativeRoomPrice");
				_AppManager.PParsingData.bookingForm.nativeCurrencyCode = json.optString("nativeCurrencyCode");
				_AppManager.PParsingData.bookingForm.promoYn = json.optString("promoYn");
				_AppManager.PParsingData.bookingForm.promoCode = json.optString("promoCode");
				_AppManager.PParsingData.bookingForm.promoDesc = json.optString("promoDesc");
				_AppManager.PParsingData.bookingForm.breakfastYn = json.optString("breakfastYn");
				_AppManager.PParsingData.bookingForm.bookRequestKey = json.optString("bookRequestKey");
				_AppManager.PParsingData.bookingForm.nonResvInfoUpdateYn = json.optString("nonResvInfoUpdateYn");
				_AppManager.PParsingData.bookingForm.mileage = json.optString("mileage");
				_AppManager.PParsingData.bookingForm.giftMoney = json.optString("giftMoney");
				_AppManager.PParsingData.bookingForm.couponCount = json.optString("couponCount");
		
				

				{
					// 일단 이 정보는 보류 
//					JSONObject usageList = (JSONObject)json.get("hotelInfo");
//					_AppManager.PParsingData.bookingForm.hotelInfo.nationCode = usageList.optString("nationCode");
//					_AppManager.PParsingData.bookingForm.hotelInfo.nationName = usageList.optString("nationName");
//					_AppManager.PParsingData.bookingForm.hotelInfo.cityCode = usageList.optString("cityCode");
//					_AppManager.PParsingData.bookingForm.hotelInfo.cityName = usageList.optString("cityName");
//					_AppManager.PParsingData.bookingForm.hotelInfo.hotelCode = usageList.optString("hotelCode");
//					_AppManager.PParsingData.bookingForm.hotelInfo.hotelNameEn = usageList.optString("hotelNameEn");
//					_AppManager.PParsingData.bookingForm.hotelInfo.starRating = usageList.optString("starRating");
//					_AppManager.PParsingData.bookingForm.hotelInfo.starRatingName = usageList.optString("starRatingName");
				}
				
				{
					
					_AppManager.PParsingData.bookingForm.chargeConditions.clear();
					JSONArray usageList = (JSONArray)json.get("chargeConditions");


					// 검색 정보를 얻는다. 
					for(int i = 0; i < usageList.length(); i++)
					{
						
						BookingForm.chargeConditionsData item = new BookingForm.chargeConditionsData();
						JSONObject list = (JSONObject)usageList.get(i);	
						item.chargeYn =  (list.optString("chargeYn"));
						item.fromDate =  (list.optString("fromDate"));				
						item.currencyCode =  (list.optString("currencyCode"));				
						item.chargeAmount =  (list.optString("chargeAmount"));

						_AppManager.PParsingData.bookingForm.chargeConditions.add(item);
						
					}
				}
				
				{
					
					_AppManager.PParsingData.bookingForm.roomPersonList.clear();
					JSONArray usageList = (JSONArray)json.get("roomPersonList");

					// 검색 정보를 얻는다. 
					for(int i = 0; i < usageList.length(); i++)
					{
						ArrayList<BookingForm.roomPersonListData > item = new ArrayList<BookingForm.roomPersonListData >();
						JSONObject list = (JSONObject)usageList.get(i);	
						JSONArray usageList2 = (JSONArray)list.get("roomPersonList");
						
						for(int j = 0; j < usageList2.length(); j++)
						{
							roomPersonListData data = new roomPersonListData();
							JSONObject list2 = (JSONObject)usageList2.get(j);	

							data.numAdultName =  (list2.optString("numAdultName"));				
							data.bedTypeCode =  (list2.optString("bedTypeCode"));				
							data.roomTypeDetailCode =  (list2.optString("roomTypeDetailCode"));

							item.add(data);
						}

						_AppManager.PParsingData.bookingForm.roomPersonList.add(item);
						
					}
				}
				
				{
					_AppManager.PParsingData.bookingForm.remarkList.clear();
					JSONArray usageList = (JSONArray)json.get("remarkList");
					for(int i = 0; i < usageList.length(); i++)
					{
						
						BookingForm.remarkListData item = new BookingForm.remarkListData();
						JSONObject list = (JSONObject)usageList.get(i);	
						item.remarkCode =  (list.optString("remarkCode"));
						item.remarkName =  (list.optString("remarkName"));				
						_AppManager.PParsingData.bookingForm.remarkList.add(item);
						
					}
				}
				
				{
					
					_AppManager.PParsingData.bookingForm.useAlertList.clear();
					JSONArray usageList = (JSONArray)json.get("useAlertList");
					for(int i = 0; i < usageList.length(); i++)
					{
						
						BookingForm.useAlertListData item = new BookingForm.useAlertListData();
						JSONObject list = (JSONObject)usageList.get(i);	
						item.alertTitle =  (list.optString("alertTitle"));
						item.alertContents =  (list.optString("alertContents"));				
						_AppManager.PParsingData.bookingForm.useAlertList.add(item);
						
					}
				}
				
				{

					JSONObject usageList = (JSONObject)json.opt("reqInfo");
					_AppManager.PParsingData.bookingForm.reqInfo.supplyCode = usageList.optString("supplyCode");
					_AppManager.PParsingData.bookingForm.reqInfo.hotelCode = usageList.optString("hotelCode");
					_AppManager.PParsingData.bookingForm.reqInfo.checkinDay = usageList.optString("checkinDay");
					_AppManager.PParsingData.bookingForm.reqInfo.duration = usageList.optString("duration");
					_AppManager.PParsingData.bookingForm.reqInfo.roomCount1 = usageList.optString("roomCount1");
					_AppManager.PParsingData.bookingForm.reqInfo.numAdults1 = usageList.optString("numAdults1");
					_AppManager.PParsingData.bookingForm.reqInfo.roomCode = usageList.optString("roomCode");
					_AppManager.PParsingData.bookingForm.reqInfo.roomRequestKey = usageList.optString("roomRequestKey");
					_AppManager.PParsingData.bookingForm.reqInfo.closeDate = usageList.optString("closeDate");
					_AppManager.PParsingData.bookingForm.reqInfo.toDate = usageList.optString("toDate");
					_AppManager.PParsingData.bookingForm.reqInfo.guestCountAdult = usageList.optString("guestCountAdult");
					_AppManager.PParsingData.bookingForm.reqInfo.guestCountChild = usageList.optString("guestCountChild");
					
				}
				
				{
					
					_AppManager.PParsingData.bookingForm.optionList.clear();
					JSONArray usageList = (JSONArray)json.get("optionList");
					for(int i = 0; i < usageList.length(); i++)
					{
						
						BookingForm.optionListData item = new BookingForm.optionListData();
						JSONObject list = (JSONObject)usageList.get(i);	
						item.optionCode =  (list.optString("optionCode"));
						item.optionName =  (list.optString("optionName"));	
						item.optionSendPrice =  (list.optString("optionSendPrice"));
						item.optionPrice =  (list.optString("optionPrice"));	
						item.optionCode =  (list.optString("optionCode"));
						item.optionMethodType =  (list.optString("optionMethodType"));	
						_AppManager.PParsingData.bookingForm.optionList.add(item);
						
					}

				}
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
				
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}

	// 9. 예약 입력 
	public void BookingAddParsing (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PParsingData.bookingAdd.resvNum = json.optString("resvNum");
				_AppManager.PParsingData.bookingAdd.lguCstMid = json.optString("lguCstMid");
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	
	// 11. 추가할인 받기
	public void ExtraDiscountInfoParsing (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PParsingData.extraDiscountInfo.totalPrice = json.optString("totalPrice");
				_AppManager.PParsingData.extraDiscountInfo.residueMileage = json.optString("residueMileage");
				
				{
					_AppManager.PParsingData.extraDiscountInfo.mileageList.clear();
					JSONArray usageList = (JSONArray)json.get("mileageList");
					for(int i = 0; i < usageList.length(); i++)
					{
						JSONObject list = (JSONObject)usageList.get(i);	
						_AppManager.PParsingData.extraDiscountInfo.mileageList.add(list.optString("useMileage"));

					}
				}
				
				{
					_AppManager.PParsingData.extraDiscountInfo.couponList.clear();
					JSONArray usageList = (JSONArray)json.get("couponList");
					for(int i = 0; i < usageList.length(); i++)
					{
						
						ExtraDiscountInfo.couponListData item = new ExtraDiscountInfo.couponListData ();
						JSONObject list = (JSONObject)usageList.get(i);	

						item.couponId =  (list.optString("couponId"));	
						item.couponIssueNo =  (list.optString("couponIssueNo"));	
						item.couponDcAmount =  (list.optString("couponDcAmount"));	
						item.couponName =  (list.optString("couponName"));	
						
						_AppManager.PParsingData.extraDiscountInfo.couponList.add(item);

					}
				}
				
				{
					_AppManager.PParsingData.extraDiscountInfo.giftList.clear();
					JSONArray usageList = (JSONArray)json.get("giftList");
					for(int i = 0; i < usageList.length(); i++)
					{
						
						ExtraDiscountInfo.giftListData item = new ExtraDiscountInfo.giftListData ();
						JSONObject list = (JSONObject)usageList.get(i);	
						item.ticketSheetId =  (list.optString("ticketSheetId"));	
						item.ticketName =  (list.optString("ticketName"));	
						item.ticketPrice =  (list.optString("ticketPrice"));	
						item.remainder =  (list.optString("remainder"));	
						item.unitCodeRate =  (list.optString("unitCodeRate"));	
						item.payCardPeeRate =  (list.optString("payCardPeeRate"));	
						item.deliveryCostRate =  (list.optString("deliveryCostRate"));
						_AppManager.PParsingData.extraDiscountInfo.giftList.add(item);

					}
				}

				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
				
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	// 공지사항 목록 
	public void NoticeListParsing (String strJSON ,boolean bFirst )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				
				_AppManager.PParsingData.noticeList.currPage = json.optString("currPage");
				_AppManager.PParsingData.noticeList.totalPage = json.optString("totalPage");
				_AppManager.PParsingData.noticeList.numPerPage = json.optString("numPerPage");

				{
					if (bFirst)
						_AppManager.PParsingData.noticeList.noticeList.clear();
					
					JSONArray usageList = (JSONArray)json.get("noticeList");
					for(int i = 0; i < usageList.length(); i++)
					{
						NoticeList.noticeListData item = new NoticeList.noticeListData ();
						JSONObject list = (JSONObject)usageList.get(i);	
						item.wid =  (list.optString("wid"));	
						item.subject =  (list.optString("subject"));	
						item.name =  (list.optString("name"));	
						item.regDay =  (list.optString("regDay"));	
						item.newYn =  (list.optString("newYn"));	
						_AppManager.PParsingData.noticeList.noticeList.add(item);

					}
				}
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	public void NoticeInfoParsing (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PParsingData.noticeDetail.noticeInfo.wid = json.optString("resvNum");
				_AppManager.PParsingData.noticeDetail.noticeInfo.subject = json.optString("subject");
				_AppManager.PParsingData.noticeDetail.noticeInfo.contents = json.optString("contents");
				_AppManager.PParsingData.noticeDetail.noticeInfo.name = json.optString("name");
				_AppManager.PParsingData.noticeDetail.noticeInfo.regDay = json.optString("regDay");
				_AppManager.PParsingData.noticeDetail.noticeInfo.newYn = json.optString("newYn");

				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	// 문의 목록 
	public void ConsultListParsing (String strJSON ,boolean bFirst )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				
				_AppManager.PParsingData.consultList.currPage = json.optString("currPage");
				_AppManager.PParsingData.consultList.totalPage = json.optString("totalPage");
				_AppManager.PParsingData.consultList.numPerPage = json.optString("numPerPage");

				{
					if (bFirst)
						_AppManager.PParsingData.consultList.consultList.clear();
					
					JSONArray usageList = (JSONArray)json.get("consultList");
					for(int i = 0; i < usageList.length(); i++)
					{
						ConsultList.consultListData item = new ConsultList.consultListData ();
						JSONObject list = (JSONObject)usageList.get(i);	
						item.consultId =  (list.optString("consultId"));	
						item.subject =  (list.optString("subject"));	
						item.status =  (list.optString("status"));	
						item.statusName =  (list.optString("statusName"));	
						item.regDay =  (list.optString("regDay"));	
						_AppManager.PParsingData.consultList.consultList.add(item);

					}
				}
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		}
	}
	
	
	public void ConsultInfoParsing (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PParsingData.consultDetail.consultInfo.consultId = json.optString("consultId");
				_AppManager.PParsingData.consultDetail.consultInfo.contents = json.optString("contents");
				_AppManager.PParsingData.consultDetail.consultInfo.answer = json.optString("answer");
				_AppManager.PParsingData.consultDetail.consultInfo.regDay = json.optString("regDay");
				_AppManager.PParsingData.consultDetail.consultInfo.ansDay = json.optString("ansDay");
				_AppManager.PParsingData.consultDetail.consultInfo.status = json.optString("status");

				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	public void ReviewList(String strJSON  ,boolean bFirst)
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				
				_AppManager.PParsingData.reviewList.currPage = json.optString("currPage");
				_AppManager.PParsingData.reviewList.totalPage = json.optString("totalPage");
				_AppManager.PParsingData.reviewList.numPerPage = json.optString("numPerPage");

				{
					if (bFirst)
						_AppManager.PParsingData.reviewList.boardList.clear();
					
					JSONArray usageList = (JSONArray)json.get("boardList");
					for(int i = 0; i < usageList.length(); i++)
					{
						ReviewList.boardListData item = new ReviewList.boardListData ();
						JSONObject list = (JSONObject)usageList.get(i);	

						item.boardNum =  (list.optString("boardNum"));	
						item.subject =  (list.optString("subject"));	
						item.writerName =  (list.optString("writerName"));	
						item.regDay =  (list.optString("regDay"));	
						item.rating =  (list.optString("rating"));	
						item.recommendCount =  (list.optString("recommendCount"));	
						item.hitCount =  (list.optString("hitCount"));	
						item.replyCount =  (list.optString("replyCount"));	
						
						

						_AppManager.PParsingData.reviewList.boardList.add(item);

					}
				}
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	
	public void ReviewInfoParsing (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				{
					JSONObject list = (JSONObject)json.get("boardInfo");
					
					_AppManager.PParsingData.reviewDetail.boardInfo.prevBoardNum = list.optString("prevBoardNum");
					_AppManager.PParsingData.reviewDetail.boardInfo.nextBoardNum = list.optString("nextBoardNum");
					_AppManager.PParsingData.reviewDetail.boardInfo.subject = list.optString("subject");
					_AppManager.PParsingData.reviewDetail.boardInfo.contents = list.optString("contents");
					_AppManager.PParsingData.reviewDetail.boardInfo.writerName = list.optString("writerName");
					_AppManager.PParsingData.reviewDetail.boardInfo.regDay = list.optString("regDay");
					_AppManager.PParsingData.reviewDetail.boardInfo.hitCount = list.optString("hitCount");
					_AppManager.PParsingData.reviewDetail.boardInfo.recommendCount = list.optString("recommendCount");
					_AppManager.PParsingData.reviewDetail.boardInfo.replyCount = list.optString("replyCount");
					_AppManager.PParsingData.reviewDetail.boardInfo.rating = list.optString("rating");
					_AppManager.PParsingData.reviewDetail.boardInfo.hotelCode = list.optString("hotelCode");
					_AppManager.PParsingData.reviewDetail.boardInfo.hotelName = list.optString("hotelName");
					_AppManager.PParsingData.reviewDetail.boardInfo.nationCode = list.optString("nationCode");
					_AppManager.PParsingData.reviewDetail.boardInfo.nationName = list.optString("nationName");
					_AppManager.PParsingData.reviewDetail.boardInfo.cityCode = list.optString("cityCode");
					_AppManager.PParsingData.reviewDetail.boardInfo.cityName = list.optString("cityName");
				}
				
				{
					_AppManager.PParsingData.reviewDetail.ratingList.clear();
					JSONArray usageList = (JSONArray)json.get("ratingList");
					for(int i = 0; i < usageList.length(); i++)
					{
						ReviewDetail.ratingListData item = new ReviewDetail.ratingListData ();
						JSONObject list = (JSONObject)usageList.get(i);	

						item.rating =  (list.optString("rating"));	
						item.typeCode =  (list.optString("typeCode"));	
						item.typeName =  (list.optString("typeName"));	
						_AppManager.PParsingData.reviewDetail.ratingList.add(item);

					}
				}
				
				{
					_AppManager.PParsingData.reviewDetail.imageList.clear();
					JSONArray usageList = (JSONArray)json.get("imageList");
					for(int i = 0; i < usageList.length(); i++)
					{
						JSONObject list = (JSONObject)usageList.get(i);
	
						_AppManager.PParsingData.reviewDetail.imageList.add(list.optString("middleImageUrl"));

					}
				}

				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		}
	}
	
	
	public void ReviewInfoReplyParsing (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				
				{
					_AppManager.PParsingData.reviewDetailReplies.replyList.clear();
					JSONArray usageList = (JSONArray)json.get("replyList");
					for(int i = 0; i < usageList.length(); i++)
					{
						ReviewDetailReplies.replyListData item = new ReviewDetailReplies.replyListData ();
						JSONObject list = (JSONObject)usageList.get(i);	
						
						item.replyNum =  (list.optString("replyNum"));	
						item.memberId =  (list.optString("memberId"));	
						item.writerName =  (list.optString("writerName"));	
						item.contents =  (list.optString("contents"));	
						item.regDay =  (list.optString("regDay"));	
						_AppManager.PParsingData.reviewDetailReplies.replyList.add(item);

					}
				}
				


				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		}
	}
	
	
	public void DiaryListParsing(String strJSON  ,boolean bFirst)
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				
				_AppManager.PParsingData.diaryList.currPage = json.optString("currPage");
				_AppManager.PParsingData.diaryList.totalPage = json.optString("totalPage");
				_AppManager.PParsingData.diaryList.numPerPage = json.optString("numPerPage");

				{
					if (bFirst)
						_AppManager.PParsingData.diaryList.tourDiaryList.clear();
					
					JSONArray usageList = (JSONArray)json.get("tourDiaryList");
					for(int i = 0; i < usageList.length(); i++)
					{
						DiaryList.tourDiaryListData item = new DiaryList.tourDiaryListData ();
						JSONObject list = (JSONObject)usageList.get(i);	

						item.diaryNum =  (list.optString("diaryNum"));	
						item.subject =  (list.optString("subject"));	
						item.contents =  (list.optString("contents"));	
						item.imageUrl =  (list.optString("imageUrl"));	
						item.writerName =  (list.optString("writerName"));	
						item.regDay =  (list.optString("regDay"));	
						item.nationCode =  (list.optString("nationCode"));	
						item.cityCode =  (list.optString("cityCode"));	
						item.hotelCode =  (list.optString("hotelCode"));	
						item.hotelName =  (list.optString("hotelName"));

						item.hotelName =  (list.optString("recommendCount"));
						item.hotelName =  (list.optString("hitCount"));
						item.hotelName =  (list.optString("replyCount"));

						_AppManager.PParsingData.diaryList.tourDiaryList.add(item);

					}
				}
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	public void DiaryInfoParsing (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				{
					JSONObject list = (JSONObject)json.get("tourDiaryInfo");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.diaryNum = list.optString("diaryNum");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.prevDiaryNum = list.optString("prevDiaryNum");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.nextDiaryNum = list.optString("nextDiaryNum");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.subject = list.optString("subject");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.mamberId = list.optString("mamberId");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.writerName = list.optString("writerName");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.hitCount = list.optString("hitCount");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.recommendCount = list.optString("recommendCount");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.regDay = list.optString("regDay");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.hotelCode = list.optString("hotelCode");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.hotelName = list.optString("hotelName");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.cityCode = list.optString("cityCode");
					_AppManager.PParsingData.diaryDetail.tourDiaryInfo.nationCode = list.optString("nationCode");

				}
				
				{
					_AppManager.PParsingData.diaryDetail.subDiaryList.clear();
					JSONArray usageList = (JSONArray)json.get("subDiaryList");
					for(int i = 0; i < usageList.length(); i++)
					{
						DiaryDetail.subDiaryListData item = new DiaryDetail.subDiaryListData ();
						JSONObject list = (JSONObject)usageList.get(i);	
						item.cntntNum =  (list.optString("cntntNum"));	
						item.contents =  (list.optString("contents"));	
						item.regDay =  (list.optString("regDay"));	
						item.imageUrl =  (list.optString("imageUrl"));	
						_AppManager.PParsingData.diaryDetail.subDiaryList.add(item);

					}
				}
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		}
	}
	
	// 여행일지 댓글 목록 
	public void DiaryInfoReplyParsing (String strJSON , boolean bFirst)
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				
				_AppManager.PParsingData.diaryReplyList.currPage = json.optString("currPage");
				_AppManager.PParsingData.diaryReplyList.totalPage = json.optString("totalPage");
				_AppManager.PParsingData.diaryReplyList.numPerPage = json.optString("numPerPage");
				{
					if ( bFirst )
						_AppManager.PParsingData.diaryReplyList.replyList.clear();
					JSONArray usageList = (JSONArray)json.get("replyList");
					for(int i = 0; i < usageList.length(); i++)
					{
						DiaryReplyList.replyListData item = new DiaryReplyList.replyListData ();
						JSONObject list = (JSONObject)usageList.get(i);	
						
						item.replyNum =  (list.optString("replyNum"));	
						item.memberId =  (list.optString("memberId"));	
						item.writerName =  (list.optString("writerName"));	
						item.contents =  (list.optString("contents"));	
						item.regDay =  (list.optString("regDay"));	
						_AppManager.PParsingData.diaryReplyList.replyList.add(item);

					}
				}
				


				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		}
	}
	
	public void DiaryNationCodeListParsing( String strJSON )
	{
		

		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				JSONArray usageList = (JSONArray)json.get("nationList");
				_AppManager.PParsingData.diaryNationCodeList.nationList.clear();
				// 검색 정보를 얻는다. 
				for(int i = 0; i < usageList.length(); i++)
				{
					DiaryNationCodeList.nationListData item = new DiaryNationCodeList.nationListData();
					JSONObject list = (JSONObject)usageList.get(i);
					


					
					item.nationNameEn = (list.optString("nationNameEn"));
					item.nationName =  (list.optString("nationName"));
					item.nationCode =  (list.optString("nationCode"));
					
					_AppManager.PParsingData.diaryNationCodeList.nationList.add(item);
				}
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else 
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	
	public void DiaryCityCodeListParsing( String strJSON )
	{
		

		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				JSONArray usageList = (JSONArray)json.get("cityList");
				_AppManager.PParsingData.diaryCityCodeList.cityList.clear();
				// 검색 정보를 얻는다. 
				for(int i = 0; i < usageList.length(); i++)
				{
					DiaryCityCodeList.cityListData item = new DiaryCityCodeList.cityListData();
					JSONObject list = (JSONObject)usageList.get(i);
					


					
					item.cityNameEn = (list.optString("cityNameEn"));
					item.cityName =  (list.optString("cityName"));
					item.cityCode =  (list.optString("cityCode"));
					
					_AppManager.PParsingData.diaryCityCodeList.cityList.add(item);
				}
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else 
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
		
	public void EventList(String strJSON, boolean bFirst )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				
				_AppManager.PParsingData.eventList.currPage = json.optString("currPage");
				_AppManager.PParsingData.eventList.numPerPage = json.optString("numPerPage");
				_AppManager.PParsingData.eventList.totalPage = json.optString("totalPage");
				
				if ( bFirst )
					_AppManager.PParsingData.eventList.eventList.clear();
				
				JSONArray usageList = (JSONArray)json.get("eventList");
				for(int i = 0; i < usageList.length(); i++)
				{
					EventList.eventListData item = new EventList.eventListData();
					JSONObject list = (JSONObject)usageList.get(i);
					


					
					item.eventNum = (list.optString("eventNum"));
					item.subject =  (list.optString("subject"));
					item.summary =  (list.optString("summary"));
					item.bgnDay =  (list.optString("bgnDay"));
					item.endDay =  (list.optString("endDay"));
					item.imageUrl =  (list.optString("imageUrl"));
					
					_AppManager.PParsingData.eventList.eventList.add(item);
				}
				
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	public void EventMainImageParsing( String strJSON)
	{
		
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{

				JSONArray usageList = (JSONArray)json.get("eventList");
					
				// 검색 정보를 얻는다. 
				for(int i = 0; i < usageList.length(); i++)
				{
					EventMainImage.eventListData item = new EventMainImage.eventListData();
					JSONObject list = (JSONObject)usageList.get(i);

					item.imageUrl =  (list.optString("imageUrl"));
					item.eventNum =  (list.optString("eventNum"));

					_AppManager.PParsingData.eventMainImage.eventList.add(item);
					
				}
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else 
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		} catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	public void EventDetailParsing( String strJSON)
	{
		
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			
			
			if(json.getString("errorCode").equals("0"))
			{

				{
					JSONObject list = (JSONObject)json.get("eventInfo");
					_AppManager.PParsingData.eventDetail.eventInfo.eventNum = list.optString("eventNum");
					_AppManager.PParsingData.eventDetail.eventInfo.subject = list.optString("subject");
					_AppManager.PParsingData.eventDetail.eventInfo.summary = list.optString("summary");
					_AppManager.PParsingData.eventDetail.eventInfo.bgnDay = list.optString("bgnDay");
					_AppManager.PParsingData.eventDetail.eventInfo.bgnTime = list.optString("bgnTime");
					_AppManager.PParsingData.eventDetail.eventInfo.endDay = list.optString("endDay");
					_AppManager.PParsingData.eventDetail.eventInfo.endTime = list.optString("endTime");
					_AppManager.PParsingData.eventDetail.eventInfo.imageUrl = list.optString("imageUrl");
					_AppManager.PParsingData.eventDetail.eventInfo.imageWidth = list.optString("imageWidth");
					_AppManager.PParsingData.eventDetail.eventInfo.imageHeight = list.optString("imageHeight");
					_AppManager.PParsingData.eventDetail.eventInfo.pageTypeCode = list.optString("pageTypeCode");
					_AppManager.PParsingData.eventDetail.eventInfo.hbrdTypeCode = list.optString("hbrdTypeCode");
					_AppManager.PParsingData.eventDetail.eventInfo.linkHotelCode = list.optString("linkHotelCode");
					_AppManager.PParsingData.eventDetail.eventInfo.linkCouponId = list.optString("linkCouponId");
					_AppManager.PParsingData.eventDetail.eventInfo.buyCount = list.optString("buyCount");

				}
				
				{
					_AppManager.PParsingData.eventDetail.domHotelList.clear();
					JSONArray usageList = (JSONArray)json.optJSONArray("domHotelList");
					if ( usageList != null)
					{
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList.length(); i++)
						{
							EventDetail.domHotelListData item = new EventDetail.domHotelListData();
							JSONObject list = (JSONObject)usageList.get(i);
							
							item.hotelCode = list.optString("hotelCode");
							item.hotelName = list.optString("hotelName");
							item.imageUrl = list.optString("imageUrl");
							item.starRating = list.optString("starRating");
							item.starRatingName = list.optString("starRatingName");
							item.hotelAddrs = list.optString("hotelAddrs");
							item.hotelPrice = list.optString("hotelPrice");

							_AppManager.PParsingData.eventDetail.domHotelList.add(item);
							
						}
					}
				}
				
				{
					_AppManager.PParsingData.eventDetail.intHotelList.clear();
					JSONArray usageList = (JSONArray)json.optJSONArray("intHotelList");
					if ( usageList != null)
					{
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList.length(); i++)
						{
							EventDetail.intHotelListData item = new EventDetail.intHotelListData();
							JSONObject list = (JSONObject)usageList.get(i);
							
							item.hotelCode = list.optString("hotelCode");
							item.hotelName = list.optString("hotelName");
							item.imageUrl = list.optString("imageUrl");
							item.starRating = list.optString("starRating");
							item.starRatingName = list.optString("starRatingName");
							item.hotelAddrs = list.optString("hotelAddrs");
							item.hotelPrice = list.optString("hotelPrice");

							_AppManager.PParsingData.eventDetail.intHotelList.add(item);
							
						}
					}
				}

				
				
					

				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else 
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		} catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	
	public void EventBannerList(String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				
				_AppManager.PParsingData.eventBannerList.totalCount = json.optString("totalCount");

				

				_AppManager.PParsingData.eventBannerList.eventList.clear();
				JSONArray usageList = (JSONArray)json.get("eventList");
				for(int i = 0; i < usageList.length(); i++)
				{
					EventBannerList.eventListData item = new EventBannerList.eventListData();
					JSONObject list = (JSONObject)usageList.get(i);

					
					item.imageWidth = (list.optString("imageWidth"));
					item.imageHeight =  (list.optString("imageHeight"));
					item.linkTypeCode =  (list.optString("linkTypeCode"));
					item.linkCode =  (list.optString("linkCode"));
					item.imageUrl =  (list.optString("imageUrl"));
					
					_AppManager.PParsingData.eventBannerList.eventList.add(item);
				}
				
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	
	// 로그인 파싱은 여기서 하지만, 아이디와 패스워드는 저장하지 않는다. 
	// 이 함수를 호출한곳에서 아이디와 패스워드를 저장한다. 
	public void LoginParsing(String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.m_LoginCheck = true;

				_AppManager.PParsingData.login.nickname = (json.getString("nickname"));
				_AppManager.PParsingData.login.name = (json.getString("name"));
				_AppManager.PParsingData.login.email = (json.getString("email"));
				_AppManager.PParsingData.login.mobile =(json.getString("mobile"));
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else 
			{
				// 에러 메세지를 전송한다.
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	public void BookingList(String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				
				

				_AppManager.PParsingData.bookingList.resvList.clear();
				JSONArray usageList = (JSONArray)json.get("resvList");
				for(int i = 0; i < usageList.length(); i++)
				{
					BookingList.resvListData item = new BookingList.resvListData();
					JSONObject list = (JSONObject)usageList.get(i);

					item.resvNum = (list.optString("resvNum"));
					item.hotelName =  (list.optString("hotelName"));
					item.nationCode =  (list.optString("nationCode"));
					item.resvStatusCode =  (list.optString("resvStatusCode"));
					item.resvStatusName =  (list.optString("resvStatusName"));
					
					_AppManager.PParsingData.bookingList.resvList.add(item);
				}
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	
	public void BookingDetail(String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				
				_AppManager.PParsingData.bookingDetail.chargeOnCancelYn = json.optString ("chargeOnCancelYn");

				{
					JSONObject list = (JSONObject)json.get("resvInfo");
					

					_AppManager.PParsingData.bookingDetail.resvInfo.hotelCode = list.optString("hotelCode");
					_AppManager.PParsingData.bookingDetail.resvInfo.hotelName = list.optString("hotelName");
					_AppManager.PParsingData.bookingDetail.resvInfo.checkinDay = list.optString("checkinDay");
					_AppManager.PParsingData.bookingDetail.resvInfo.checkoutDay = list.optString("checkoutDay");
					_AppManager.PParsingData.bookingDetail.resvInfo.duration = list.optString("duration");
					_AppManager.PParsingData.bookingDetail.resvInfo.roomName = list.optString("roomName");
					_AppManager.PParsingData.bookingDetail.resvInfo.roomCount = list.optString("roomCount");
					_AppManager.PParsingData.bookingDetail.resvInfo.hotelAddrs = list.optString("hotelAddrs");
					_AppManager.PParsingData.bookingDetail.resvInfo.hotelTel = list.optString("hotelTel");
					_AppManager.PParsingData.bookingDetail.resvInfo.resvName = list.optString("resvName");
					_AppManager.PParsingData.bookingDetail.resvInfo.guestName = list.optString("guestName");
					_AppManager.PParsingData.bookingDetail.resvInfo.adultCount = list.optString("adultCount");
					
					_AppManager.PParsingData.bookingDetail.resvInfo.childCount = list.optString("childCount");
					_AppManager.PParsingData.bookingDetail.resvInfo.resvStatusCode = list.optString("resvStatusCode");
					_AppManager.PParsingData.bookingDetail.resvInfo.resvStatusName = list.optString("resvStatusName");
					_AppManager.PParsingData.bookingDetail.resvInfo.payStatusCode = list.optString("payStatusCode");
					_AppManager.PParsingData.bookingDetail.resvInfo.breakfastYn = list.optString("breakfastYn");
					_AppManager.PParsingData.bookingDetail.resvInfo.addInfo = list.optString("addInfo");
					_AppManager.PParsingData.bookingDetail.resvInfo.cityCode = list.optString("cityCode");
					
					
					_AppManager.PParsingData.bookingDetail.resvInfo.cityName = list.optString("cityName");
					_AppManager.PParsingData.bookingDetail.resvInfo.nationCode = list.optString("nationCode");
					_AppManager.PParsingData.bookingDetail.resvInfo.nationName = list.optString("nationName");
					
					
				}
				
				{
					JSONArray usageList = (JSONArray)json.get("chargeConditions");
					_AppManager.PParsingData.bookingDetail.chargeConditions.clear();
					for(int i = 0; i < usageList.length(); i++)
					{
						BookingDetail.chargeConditionsData item = new BookingDetail.chargeConditionsData();
						JSONObject list = (JSONObject)usageList.get(i);


						item.chargeYn = (list.optString("chargeYn"));
						item.fromDate =  (list.optString("fromDate"));
						item.currencyCode =  (list.optString("currencyCode"));
						item.chargeAmount =  (list.optString("chargeAmount"));

						
						_AppManager.PParsingData.bookingDetail.chargeConditions.add(item);
					}
				}
				
				{
					JSONArray usageList = (JSONArray)json.get("optionList");
					_AppManager.PParsingData.bookingDetail.optionList.clear();
					for(int i = 0; i < usageList.length(); i++)
					{
						JSONObject list = (JSONObject)usageList.get(i);
						_AppManager.PParsingData.bookingDetail.optionList.add(list.optString("optionName"));
					}
				}
				
				
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	
	
	
	public void MyBenefitsInfo (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
				
				
				_AppManager.PParsingData.myBenefitsInfo.mileage = json.optString("mileage");
				_AppManager.PParsingData.myBenefitsInfo.giftMoney = json.optString("giftMoney");
				_AppManager.PParsingData.myBenefitsInfo.couponCount = json.optString("couponCount");
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	
	public void MyMileageList (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
				
				
				_AppManager.PParsingData.myMileageList.totalMileage = json.optString("totalMileage");
				{
					JSONArray usageList = (JSONArray)json.get("mileageList");
					_AppManager.PParsingData.myMileageList.mileageList.clear();
					for(int i = 0; i < usageList.length(); i++)
					{
						MyMileageList.mileageListData item = new MyMileageList.mileageListData();
						JSONObject list = (JSONObject)usageList.get(i);
						item.workDay = (list.optString("workDay"));
						item.itemName =  (list.optString("itemName"));
						item.mileage =  (list.optString("mileage"));
						_AppManager.PParsingData.myMileageList.mileageList.add(item);
					}
				}
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	
	public void MyCouponList (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";

				{
					JSONArray usageList = (JSONArray)json.get("couponList");
					_AppManager.PParsingData.myCouponList.couponList.clear();
					for(int i = 0; i < usageList.length(); i++)
					{

						MyCouponList.couponListData item = new MyCouponList.couponListData();
						JSONObject list = (JSONObject)usageList.get(i);
						item.putDay = (list.optString("putDay"));
						item.couponName =  (list.optString("couponName"));
						item.appEndDay =  (list.optString("appEndDay"));
						_AppManager.PParsingData.myCouponList.couponList.add(item);
					}
				}
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	
	public void MyGiftList (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";

				_AppManager.PParsingData.myGiftList.totalRemainder = json.optString("totalRemainder");
				{
					JSONArray usageList = (JSONArray)json.get("couponList");
					_AppManager.PParsingData.myGiftList.mileageList.clear();
					for(int i = 0; i < usageList.length(); i++)
					{

						MyGiftList.mileageListData item = new MyGiftList.mileageListData();
						JSONObject list = (JSONObject)usageList.get(i);
						item.putDay = (list.optString("putDay"));
						item.giftName =  (list.optString("giftName"));
						item.remainder =  (list.optString("remainder"));
						_AppManager.PParsingData.myGiftList.mileageList.add(item);
					}
				}
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	public void MyPreferCity (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";

				{
					JSONObject usageList = (JSONObject)json.get("cityInfo");
					
					_AppManager.PParsingData.myPreferCity.cityInfo.cityCode1 = usageList.optString("cityCode1");
					_AppManager.PParsingData.myPreferCity.cityInfo.cityName1 = usageList.optString("cityName1");
					_AppManager.PParsingData.myPreferCity.cityInfo.cityCode2 = usageList.optString("cityCode2");
					_AppManager.PParsingData.myPreferCity.cityInfo.cityName2 = usageList.optString("cityName2");
					_AppManager.PParsingData.myPreferCity.cityInfo.cityCode3 = usageList.optString("cityCode3");
					_AppManager.PParsingData.myPreferCity.cityInfo.cityName3 = usageList.optString("cityName3");
				}
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	public void MyPreferHotel (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";

				{
					JSONObject usageList = (JSONObject)json.get("hotelInfo");
					
					_AppManager.PParsingData.myPreferHotel.hotelInfo.hotelCode1 = usageList.optString("hotelCode1");
					_AppManager.PParsingData.myPreferHotel.hotelInfo.hotelName1 = usageList.optString("hotelName1");
					_AppManager.PParsingData.myPreferHotel.hotelInfo.hotelCode2 = usageList.optString("hotelCode2");
					_AppManager.PParsingData.myPreferHotel.hotelInfo.hotelName2 = usageList.optString("hotelName2");
					_AppManager.PParsingData.myPreferHotel.hotelInfo.hotelCode3 = usageList.optString("hotelCode3");
					_AppManager.PParsingData.myPreferHotel.hotelInfo.hotelName3 = usageList.optString("hotelName3");
				}
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	
	
	
	public void BookmarkList (String strJSON )
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";

				
				{
					JSONArray usageList = (JSONArray)json.get("hotelList");
					_AppManager.PParsingData.bookmarkList.hotelList.clear();
					for(int i = 0; i < usageList.length(); i++)
					{


						BookmarkList.hotelListData item = new BookmarkList.hotelListData();
						JSONObject list = (JSONObject)usageList.get(i);
						item.hotelCode = (list.optString("hotelCode"));
						item.hotelName =  (list.optString("hotelName"));
						item.nationCode =  (list.optString("nationCode"));
						_AppManager.PParsingData.bookmarkList.hotelList.add(item);
					}
				}
			}
			else
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		}
		catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
	

//	public void BASE (String strJSON )
//	{
//		try 
//		{
//			JSONObject json = new JSONObject(strJSON);
//			if(json.getString("errorCode").equals("0"))
//			{
//				_AppManager.PErrorCode = 0;
//				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
//			}
//			else
//			{
//				// 에러 메세지를 전송한다. 
//				_AppManager.PErrorCode = -1;
//				_AppManager.PErrorMsg = json.getString("errorMsg");
//				return ;
//			}
//		}
//		catch (JSONException e) 
//		{
//			_AppManager.PErrorCode = -1;
//			_AppManager.PErrorMsg = "JSON Parsing error";
//			e.printStackTrace();
//		} 
//	}

	
	
	
	public void CheckError(String strJSON)
	{
		try 
		{
			JSONObject json = new JSONObject(strJSON);
			if(json.getString("errorCode").equals("0"))
			{
				_AppManager.PErrorCode = 0;
				_AppManager.PErrorMsg = "JSON Parsing 성공 ";
			}
			else 
			{
				// 에러 메세지를 전송한다. 
				_AppManager.PErrorCode = -1;
				_AppManager.PErrorMsg = json.getString("errorMsg");
				return ;
			}
		} catch (JSONException e) 
		{
			_AppManager.PErrorCode = -1;
			_AppManager.PErrorMsg = "JSON Parsing error";
			e.printStackTrace();
		} 
	}
}

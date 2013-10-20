package com.pricecode;

import android.annotation.SuppressLint;

import com.rangboq.xutil.XHandler;
import com.rangboq.xutil.XHttpConn;
import com.rangboq.xutil.XUtilFunc;

@SuppressLint("DefaultLocale")
public class CMHttpConn extends XHttpConn
{
	static final String DEFAULT_HASH_KEY = "coupmarket";

	static final int TYPE_REQ_POSTCODE_LIST = 1002;
	static final int TYPE_TRY_LOGIN = 1001;
	static final int TYPE_REQ_AREA_LIST = 1003;
	static final int TYPE_REQ_FAVOR_CATEGORY_LIST = 1004;
	static final int TYPE_REQ_JOIN_MEMBER = 1005;
	static final int TYPE_REQ_UPDATE_MEMBER_INFO = 1006;
	static final int TYPE_REQ_SEARCH_ID = 1007;
	static final int TYPE_REQ_SEARCH_PASSWORD = 1008;
	static final int TYPE_REQ_MY_MILEAGE_INFO = 1009;
	static final int TYPE_REQ_MEMBER_INFO = 1010;
	static final int TYPE_REQ_NOTICE_LIST = 1011;
	static final int TYPE_REQ_INQUIRY = 1012;
	static final int TYPE_REQ_GIFTCON_LIST = 1013;
	static final int TYPE_REQ_BUY_GIFTCON = 1014;
	static final int TYPE_REQ_SET_MY_AREA = 1015;
	static final int TYPE_REQ_GIFTCON_CATEGORY_LIST = 1016;
	static final int TYPE_REQ_BENEFIT_INFO = 1017;
	static final int TYPE_REQ_GIFTCON_BUY_LIST = 1018;
	static final int TYPE_REQ_RESEND_GIFTCON = 1019;
	static final int TYPE_REQ_PARTTIME_LIST = 1020;
	static final int TYPE_REQ_DELIVERY_LIST = 1021;
	static final int TYPE_REQ_CALL_FOR_DELIVERY = 1022;
	static final int TYPE_REQ_QUERY_MAP_POS = 1023;
	
	
	static final int DEFAULT_REQ_COUNT = 10;

	public boolean openUrl( XHandler handler, String rootName, String strUrl, String strParam )
	{
		if( CMManager.enableHttps == false )
			strUrl = strUrl.replace("https://", "http://");
		return super.openUrl(handler, rootName, strUrl, strParam);
	}

	public static CMHttpConn tryLogin( XHandler resultHandler, String strId, String strPassword )
	{
		if( strId == null || strId.length() == 0 || strPassword == null || strPassword.length() == 0 )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "login";
		String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&uid=%s&passwd=%s&hashdata=%s", strCmdMode, strId,
		                                strPassword, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_TRY_LOGIN;
		boolean bTryOpen = webConn.openUrl(resultHandler, "data", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqPostcodeList( XHandler resultHandler, String strDong )
	{
		if( strDong == null || strDong.length() == 0 )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getAddressInfo";
		String strHashData = XUtilFunc.getMD5Hex(strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&msg=%s&hashdata=%s", strCmdMode, strDong,
		                                strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_POSTCODE_LIST;
		boolean bTryOpen = webConn.openUrl(resultHandler, "address", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqAreaList( XHandler resultHandler )
	{
		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getAreaInfo";
		String strHashData = XUtilFunc.getMD5Hex(strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&hashdata=%s", strCmdMode, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_AREA_LIST;
		boolean bTryOpen = webConn.openUrl(resultHandler, "area", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqFavorCategory( XHandler resultHandler )
	{
		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getGoodsKind";
		String strHashData = XUtilFunc.getMD5Hex(strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&hashdata=%s", strCmdMode, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_FAVOR_CATEGORY_LIST;
		boolean bTryOpen = webConn.openUrl(resultHandler, "goods_kind", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqJoinOrUpdate( int nReqType, XHandler resultHandler, String strId,
	                                          String strName, String strPass, String strBirth,
	                                          String strDateType, String strSex, String strEmail,
	                                          String strMobile, String strContact, String strFavorArea,
	                                          String strFaverCategory, String strEmailYes,
	                                          String strSmsYes, String strPost, String strAddress )
	{
		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = null;
		if( nReqType == TYPE_REQ_JOIN_MEMBER )
			strCmdMode = "coupMemberRegister";
		else if( nReqType == TYPE_REQ_UPDATE_MEMBER_INFO )
			strCmdMode = "updateMyInfo";
		else
			return null;
		String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + DEFAULT_HASH_KEY);

		String strParam;
		strParam = String.format("cmd_mode=%s&uid=%s&name=%s&passwd=%s&birth=%s&gender=%s&calendar_code=%s&email=%s&hphone=%s&phone=%s&post=%s&address=%s&area=%s&goods_kind=%s&mailing=%s&use_sms=%s&hashdata=%s",
		                         strCmdMode, strId, strName, strPass, strBirth, strSex, strDateType,
		                         strEmail, strMobile, strContact, strPost, strAddress, strFavorArea,
		                         strFaverCategory, strEmailYes, strSmsYes, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = nReqType;
		boolean bTryOpen = webConn.openUrl(resultHandler, "data", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqSearchId( XHandler resultHandler, String strEmail )
	{
		if( strEmail == null || strEmail.length() == 0 || strEmail.contains("@") == false )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "findUid";
		String strHashData = XUtilFunc.getMD5Hex(strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&email=%s&hashdata=%s", strCmdMode, strEmail,
		                                strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_SEARCH_ID;
		boolean bTryOpen = webConn.openUrl(resultHandler, "data", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqSearchPassword( XHandler resultHandler, String strId, String strEmail )
	{
		if( strId == null || strId.length() == 0 || strEmail == null || strEmail.length() == 0
		    || strEmail.contains("@") == false )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "findMyPwd";
		String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&uid=%s&email=%s&hashdata=%s", strCmdMode, strId,
		                                strEmail, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_SEARCH_PASSWORD;
		boolean bTryOpen = webConn.openUrl(resultHandler, "data", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqMyMileageInfo( XHandler resultHandler, String strId, String strPassword )
	{
		if( strId == null || strId.length() == 0 || strPassword == null || strPassword.length() == 0 )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getMyPoint";
		String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&uid=%s&passwd=%s&hashdata=%s", strCmdMode, strId,
		                                strPassword, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_MY_MILEAGE_INFO;
		boolean bTryOpen = webConn.openUrl(resultHandler, "point_list", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqMemberInfo( XHandler resultHandler, String strId, String strPassword )
	{
		if( strId == null || strId.length() == 0 || strPassword == null || strPassword.length() == 0 )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getMyProfile";
		String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&uid=%s&passwd=%s&hashdata=%s", strCmdMode, strId,
		                                strPassword, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_MEMBER_INFO;
		boolean bTryOpen = webConn.openUrl(resultHandler, "data", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqNoticeList( XHandler resultHandler, int nLastNo, int nReqCount )
	{
		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getNoticeInfo";
		String strHashData = XUtilFunc.getMD5Hex(strCmdMode + DEFAULT_HASH_KEY);

		if( nReqCount <= 0 )
			nReqCount = DEFAULT_REQ_COUNT;

		String strParam = null;
		if( nLastNo > 0 )
			strParam = String.format("cmd_mode=%s&last_no=%d&req_count=%d&hashdata=%s", strCmdMode,
			                         nLastNo, nReqCount, strHashData);
		else
			strParam = String.format("cmd_mode=%s&req_count=%d&hashdata=%s", strCmdMode, nReqCount,
			                         strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_NOTICE_LIST;
		boolean bTryOpen = webConn.openUrl(resultHandler, "notice_info", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqInquiry( XHandler resultHandler, String strId, String strPassword,
	                                     String strEmail, String strSubject, String strContent )
	{
		if( strEmail == null || strEmail.length() == 0 || strSubject == null || strSubject.length() == 0
		    || strContent == null || strContent.length() == 0 )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "reqMyQuestion";
		String strHashData = XUtilFunc.getMD5Hex(strCmdMode + DEFAULT_HASH_KEY);

		String strParam = null;
		if( strId != null && strId.length() > 0 && strPassword != null && strPassword.length() > 0 )
			strParam = String.format("cmd_mode=%s&uid=%s&passwd=%s&email=%s&subject=%s&hashdata=%s&content=%s",
			                         strCmdMode, strId, strPassword, strEmail, strSubject, strHashData,
			                         strContent);
		else
			strParam = String.format("cmd_mode=%s&email=%s&subject=%s&hashdata=%s&content=%s",
			                         strCmdMode, strEmail, strSubject, strHashData, strContent);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_INQUIRY;
		boolean bTryOpen = webConn.openUrl(resultHandler, "data", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqGiftconList( XHandler resultHandler, int nCategory, int nLastNo, int nReqCount )
	{
		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getGiftiConInfo";
		String strHashData = XUtilFunc.getMD5Hex(strCmdMode + DEFAULT_HASH_KEY);

		String strParam = "cmd_mode=" + strCmdMode;
		if(nCategory > 0)
			strParam += ("&category_num=" + nCategory);
		if( nLastNo > 0 )
			strParam += ("&last_no=" + nLastNo);
		if( nReqCount <= 0 )
			nReqCount = DEFAULT_REQ_COUNT;
		strParam += String.format("&req_count=%d&hashdata=%s", nReqCount, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_GIFTCON_LIST;
		boolean bTryOpen = webConn.openUrl(resultHandler, "coupon_info", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqBuyGiftcon( XHandler resultHandler, String strId, String strPassword,
	                                        String strMobile, String strDBNo, String strGiftconCode )
	{
		if( strId == null || strId.length() == 0 || strPassword == null || strPassword.length() == 0
		    || strMobile == null || strMobile.length() == 0 || strDBNo == null || strDBNo.length() == 0
		    || strGiftconCode == null || strGiftconCode.length() == 0 )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "buyGiftiCon";
		String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + DEFAULT_HASH_KEY);

		String strParam = null;
		strParam = String.format("cmd_mode=%s&uid=%s&passwd=%s&hp=%s&no=%s&coupon_code=%s&hashdata=%s",
		                         strCmdMode, strId, strPassword, strMobile, strDBNo, strGiftconCode,
		                         strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_BUY_GIFTCON;
		boolean bTryOpen = webConn.openUrl(resultHandler, "data", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqSetMyArea( XHandler resultHandler, String strId, String strPassword, String strArea )
	{
		if( strId == null || strId.length() == 0 || strPassword == null || strPassword.length() == 0
			|| strArea == null || strArea.length() == 0 || strArea.contains("-") == false )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "setMyArea";
		String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&uid=%s&passwd=%s&area=%s&hashdata=%s", 
		                                strCmdMode, strId, strPassword, strArea, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_SET_MY_AREA;
		boolean bTryOpen = webConn.openUrl(resultHandler, "data", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqBenefitInfo( XHandler resultHandler, String strId, String strPassword )
	{
		if( strId == null || strId.length() == 0 || strPassword == null || strPassword.length() == 0 )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getBenefitInfo";
		String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&uid=%s&passwd=%s&hashdata=%s", strCmdMode, strId,
		                                strPassword, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_BENEFIT_INFO;
		boolean bTryOpen = webConn.openUrl(resultHandler, "data", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqGiftconCategoryList( XHandler resultHandler )
	{
		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getGiftiConCategoryNameList";
		String strHashData = XUtilFunc.getMD5Hex(strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&hashdata=%s", strCmdMode, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_GIFTCON_CATEGORY_LIST;
		boolean bTryOpen = webConn.openUrl(resultHandler, "category_info", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqGiftconBuyList( XHandler resultHandler, String strId, String strPassword, int nLastNo, int nReqCount )
	{
		if( strId == null || strId.length() == 0 || strPassword == null || strPassword.length() == 0 )
				return null;
		
		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getGiftiConBuyList";
		String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + DEFAULT_HASH_KEY);

		if( nReqCount <= 0 )
			nReqCount = DEFAULT_REQ_COUNT;

		String strParam = null;
		if( nLastNo > 0 )
			strParam = String.format("cmd_mode=%s&uid=%s&passwd=%s&last_no=%d&req_count=%d&hashdata=%s", 
			                         strCmdMode, strId, strPassword, nLastNo, nReqCount, strHashData);
		else
			strParam = String.format("cmd_mode=%s&uid=%s&passwd=%s&req_count=%d&hashdata=%s", 
			                         strCmdMode, strId, strPassword, nReqCount, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_GIFTCON_BUY_LIST;
		boolean bTryOpen = webConn.openUrl(resultHandler, "coupon_info", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqResendGiftcon( XHandler resultHandler, String strId, String strPassword,
	                                        String strMobile, String strDBNo, String strGiftconCode, String strGiftconNumber )
	{
		if( strId == null || strId.length() == 0 || strPassword == null || strPassword.length() == 0
		    || strMobile == null || strMobile.length() == 0 || strDBNo == null || strDBNo.length() == 0
		    || strGiftconCode == null || strGiftconCode.length() == 0 )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "reSendCoupon";
		String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + DEFAULT_HASH_KEY);

		String strParam = null;
		strParam = String.format("cmd_mode=%s&uid=%s&passwd=%s&hp=%s&no=%s&coupon_code=%s&coupon_number=%s&hashdata=%s",
		                         strCmdMode, strId, strPassword, strMobile, strDBNo, strGiftconCode,
		                         strGiftconNumber, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_RESEND_GIFTCON;
		boolean bTryOpen = webConn.openUrl(resultHandler, "data", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqParttimeList( XHandler resultHandler, int nLastNo, int nReqCount )
	{
		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getAlbaInfo";
		String strHashData = XUtilFunc.getMD5Hex(strCmdMode + DEFAULT_HASH_KEY);

		if( nReqCount <= 0 )
			nReqCount = DEFAULT_REQ_COUNT;

		String strParam = null;
		if( nLastNo > 0 )
			strParam = String.format("cmd_mode=%s&last_no=%d&req_count=%d&hashdata=%s", strCmdMode,
			                         nLastNo, nReqCount, strHashData);
		else
			strParam = String.format("cmd_mode=%s&req_count=%d&hashdata=%s", strCmdMode, nReqCount,
			                         strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_PARTTIME_LIST;
		boolean bTryOpen = webConn.openUrl(resultHandler, "alba_info", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqDeliveryList( XHandler resultHandler, String strArea, int nCategory, int nLastNo, int nReqCount )
	{
		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "getDeliveryGoodsList";
		String strHashData = XUtilFunc.getMD5Hex(strCmdMode + DEFAULT_HASH_KEY);

		String strParam = "cmd_mode=" + strCmdMode;
		if(strArea != null && strArea.length() > 0)
			strParam += "&area=" + strArea;
		if(nCategory > 0)
			strParam += ("&req_category_num=" + nCategory);
		if( nLastNo > 0 )
			strParam += ("&last_no=" + nLastNo);
		if( nReqCount <= 0 )
			nReqCount = DEFAULT_REQ_COUNT;
		strParam += String.format("&req_count=%d&hashdata=%s", nReqCount, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_DELIVERY_LIST;
		boolean bTryOpen = webConn.openUrl(resultHandler, "delivery_goods_info", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqCallForDelivery( XHandler resultHandler, String strId, String strName, String strAddress, String strMobile, String strNo )
	{
		if( strId == null || strId.length() == 0 || strName == null || strName.length() == 0 )
			return null;

		String strUrl = "https://www.coupmarket.com/interx/req_info.php";
		String strCmdMode = "callForDelivery";
		String strHashData = XUtilFunc.getMD5Hex(strId + strCmdMode + DEFAULT_HASH_KEY);

		String strParam = String.format("cmd_mode=%s&uid=%s&name=%s&address=%s&hp=%s&no=%s&hashdata=%s", 
		                                strCmdMode, strId, strName, strAddress, strMobile, strNo, strHashData);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_CALL_FOR_DELIVERY;
		boolean bTryOpen = webConn.openUrl(resultHandler, "data", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

	public static CMHttpConn reqQueryMapPos( XHandler resultHandler, String strAddress )
	{
		if( strAddress == null || strAddress.length() == 0 )
			return null;
		
		String strUrl = "http://apis.daum.net/local/geo/addr2coord";
		String strParam = String.format("q=%s&apikey=%s&output=xml", 
		                                strAddress, CMManager.LOCAL_API_KEY);

		CMHttpConn webConn = new CMHttpConn();
		webConn.m_nRequestType = TYPE_REQ_QUERY_MAP_POS;
		webConn.m_bIsPostMethod = false;
		boolean bTryOpen = webConn.openUrl(resultHandler, "item", strUrl, strParam);
		if( bTryOpen == false )
			webConn = null;
		else if( resultHandler != null )
			resultHandler.postTimeoutMsg();
		return webConn;
	}

}

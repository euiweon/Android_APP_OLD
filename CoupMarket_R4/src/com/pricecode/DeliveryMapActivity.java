package com.pricecode;

import java.util.HashMap;

import com.pricecode.*;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapView.CurrentLocationEventListener;
import net.daum.mf.map.api.MapView.MapViewEventListener;
import net.daum.mf.map.api.MapView.OpenAPIKeyAuthenticationResultListener;
import net.daum.mf.map.api.MapView.POIItemEventListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class DeliveryMapActivity extends CMActivity implements OpenAPIKeyAuthenticationResultListener, MapViewEventListener, CurrentLocationEventListener, POIItemEventListener
{
	LinearLayout m_layoutMap;
	MapView m_MapView;

	String m_strShopName = "배달";
	HashMap<String, String> m_infoMap;

    @Override
	protected void onCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery_map);

		Intent curIntent = getIntent();
		@SuppressWarnings("unchecked")
		HashMap<String, String> infoMap = (HashMap<String, String>) curIntent.getSerializableExtra("item_info");
		if(infoMap == null)
		{
			finish();
			return;
		}
		
		m_infoMap = infoMap;
		
		m_strShopName = m_infoMap.get("comp_name");
		if( m_strShopName == null )
		{
			m_strShopName = curIntent.getStringExtra("category_name");
    		if( m_strShopName == null )
    			m_strShopName = "배달";
		}

		setTitleBarText(m_strShopName);

		m_layoutMap = (LinearLayout) findViewById(R.id.linearLayout_map);

		String strApiKey = CMManager.MAP_API_KEY;
		m_MapView = new MapView(this);
		m_MapView.setDaumMapApiKey(strApiKey);
		m_MapView.setOpenAPIKeyAuthenticationResultListener(this);
		m_MapView.setMapViewEventListener(this);
		m_MapView.setCurrentLocationEventListener(this);
		m_MapView.setPOIItemEventListener(this);

		m_MapView.setMapType(MapView.MapType.Standard);
		m_layoutMap.addView(m_MapView);

		m_MapView.setClickable(true);	
	}

	@Override
    public void onDaumMapOpenAPIKeyAuthenticationResult( MapView mapView, int resultCode, String resultMessage )
    {
    }

	@Override
    public void onMapViewInitialized( MapView mapView )
    {
		String strLat = m_infoMap.get("lat");
		String strLng = m_infoMap.get("lng");
		if(strLat == null || strLat.length() == 0 || strLng == null || strLng.length() == 0)
			return;
		
		double dblLat = Double.valueOf(strLat);
		double dblLng = Double.valueOf(strLng);
		MapPoint pos = MapPoint.mapPointWithGeoCoord(dblLat, dblLng);

		mapView.removeAllPOIItems();
		
		MapPOIItem poiItem = new MapPOIItem();
		poiItem.setItemName(m_strShopName);
		poiItem.setMapPoint(pos);
		poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
		poiItem.setShowAnimationType(MapPOIItem.ShowAnimationType.NoAnimation);
		mapView.addPOIItem(poiItem);
		
		mapView.setMapCenterPointAndZoomLevel(pos, 1, true);
    }

	@Override
    public void onMapViewCenterPointMoved( MapView arg0, MapPoint arg1 )
    {
    }

	@Override
    public void onMapViewDoubleTapped( MapView arg0, MapPoint arg1 )
    {
    }

	@Override
    public void onMapViewLongPressed( MapView arg0, MapPoint arg1 )
    {
    }

	@Override
    public void onMapViewSingleTapped( MapView arg0, MapPoint arg1 )
    {
    }

	@Override
    public void onMapViewZoomLevelChanged( MapView arg0, int arg1 )
    {
    }

	@Override
    public void onCurrentLocationDeviceHeadingUpdate( MapView arg0, float arg1 )
    {
    }

	@Override
    public void onCurrentLocationUpdate( MapView arg0, MapPoint arg1, float arg2 )
    {
    }

	@Override
    public void onCurrentLocationUpdateCancelled( MapView arg0 )
    {
    }

	@Override
    public void onCurrentLocationUpdateFailed( MapView arg0 )
    {
    }

	@Override
    public void onCalloutBalloonOfPOIItemTouched( MapView arg0, MapPOIItem arg1 )
    {
    }

	@Override
    public void onDraggablePOIItemMoved( MapView arg0, MapPOIItem arg1, MapPoint arg2 )
    {
    }

	@Override
    public void onPOIItemSelected( MapView arg0, MapPOIItem arg1 )
    {
    }

	// ///////////////////////////////////////////

}

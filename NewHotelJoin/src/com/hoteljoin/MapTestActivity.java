package com.hoteljoin;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;




public class MapTestActivity extends FragmentActivity    implements LocationListener, LocationSource { 
public static boolean locatingMe=true;
public GoogleMap mappa;
public MapView mapView;
private OnLocationChangedListener onLocationChangedListener;
private LocationManager locationManager;
 
 
 
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_maptest);
	 
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.onCreate(savedInstanceState);
	 
	 
	        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	 
	    //You may want to pass a different provider in as the first arg here
	    //depending on the location accuracy that you desire
	    //see LocationManager.getBestProvider()
	    Criteria locationCriteria = new Criteria();
	    locationCriteria.setAccuracy(Criteria.NO_REQUIREMENT);
	    locationManager.requestLocationUpdates(locationManager.getBestProvider(locationCriteria, true), 1L, 2F, this);
	 
	    if (mappa == null) {
	        mappa=mapView.getMap();
	        //This is how you register the LocationSource
	        mappa.setLocationSource(this);
	        mappa.getUiSettings().setMyLocationButtonEnabled(false);
	        mappa.setMyLocationEnabled(true);
	    }
	    
	    try 
		{
	        MapsInitializer.initialize(this);
	    } 
		catch (GooglePlayServicesNotAvailableException impossible) 
	    {
	        /* Impossible */
	    }
	    
	    CameraPosition sydney = new CameraPosition.Builder().target(new LatLng(37.5666091, 126.978371)).zoom(15.5f)
				.bearing(0).tilt(0).build();
	    mappa.animateCamera(CameraUpdateFactory.newCameraPosition(sydney));
	 
	 
	}
	@Override
	public void onPause()
	{
	    if(locationManager != null)
	    {
	        locationManager.removeUpdates(this);
	    }
	    mapView.onPause();
	    super.onPause();
	}
	 
	@Override
	public void onResume()
	{
	      mapView.onResume();
	      super.onResume();
	}
	@Override
	protected void onDestroy() {
	    mapView.onDestroy();
	    super.onDestroy();
	}
	 
	@Override
	public void onLowMemory() {
	    super.onLowMemory();
	    mapView.onLowMemory();
	}
	 
	@Override
	public void activate(OnLocationChangedListener listener) 
	{
	    onLocationChangedListener = listener;
	}
	 
	@Override
	public void deactivate() 
	{
	    onLocationChangedListener = null;
	}
	 
	@Override
	public void onLocationChanged(Location location) 
	{
	    if( onLocationChangedListener != null )
	    {
	        onLocationChangedListener.onLocationChanged( location );
	 
	        //Move the camera to the user's location once it's available!
	        //only if locatingMe is true
	        if (locatingMe) {
	            if (mappa!=null){
	                CameraUpdate pino= CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
	                        mappa.animateCamera(pino);
	 
	            }
	 
	        }
	 
	    }
	}
	 
	@Override
	public void onProviderDisabled(String provider) 
	{
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
	}
	 
	@Override
	public void onProviderEnabled(String provider) 
	{
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
	}
	 
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "status changed", Toast.LENGTH_SHORT).show();
	}
	 
}
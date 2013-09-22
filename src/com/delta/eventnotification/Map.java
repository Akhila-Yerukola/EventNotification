package com.delta.eventnotification;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends android.support.v4.app.FragmentActivity {
	
	private GoogleMap mMap;
	GoogleMapOptions options;
	SupportMapFragment myMap;
	LocationManager lm;
	Location location;
	double longitude, latitude;
	Geocoder geocoder;
	String bestProvider;
	List<Address> user = null;
	String rlat, rlng;
	double lat;
	double lng,latOfDest,lngOfDest;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		Intent getDetails =getIntent();
		rlat=getDetails.getStringExtra("lat");
		rlng=getDetails.getStringExtra("lng");
		latOfDest=Double.parseDouble(rlat);
		lngOfDest=Double.parseDouble(rlng);
		
		android.support.v4.app.FragmentManager myFragmentManager = getSupportFragmentManager();
		SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager
				.findFragmentById(R.id.map);

		mMap = mySupportMapFragment.getMap();
		if (mMap != null)
			mMap.setMyLocationEnabled(true);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		// longitude = location.getLongitude();
		// latitude = location.getLatitude();

		Criteria criteria = new Criteria();
		bestProvider = lm.getBestProvider(criteria, false);
		Location location = lm.getLastKnownLocation(bestProvider);

		if (location == null) {
			Toast.makeText(this, "Location Not found", Toast.LENGTH_LONG)
					.show();
		} else {
			geocoder = new Geocoder(this);
			try {
				user = geocoder.getFromLocation(location.getLatitude(),
						location.getLongitude(), 1);
				lat = (double) user.get(0).getLatitude();
				lng = (double) user.get(0).getLongitude();
				System.out.println(" DDD lat: " + lat + ",  longitude: " + lng);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		LatLng latLng = new LatLng(latOfDest, lngOfDest);
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.addMarker(new MarkerOptions()
				.position(latLng)
				.title("My Spot")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

		String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+lat+","+lng+"&daddr="+latOfDest+","+lngOfDest;
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
		intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		startActivity(intent);
		
	}
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			Log.e("Check", "Map");
		}
		// Check if we were successful in obtaining the map.
		if (mMap != null) {
			// The Map is verified. It is now safe to manipulate the map.

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}

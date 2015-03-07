package br.com.apuri.utils;

import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ApuriMapUtils {

	private static final int DEFAULT_ZOOM = 15;

	public static void defaultZoom(GoogleMap map, LatLng location){
		CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM);
		map.animateCamera(cam);
		map.addMarker(new MarkerOptions().position(location));		
	}

	public static SupportMapFragment mapWithLocation(final LatLng location) {
		final CameraPosition position = new CameraPosition(location, DEFAULT_ZOOM, 0, 0);
		SupportMapFragment fragment = new SupportMapFragment(){
			public void onActivityCreated(android.os.Bundle savedInstanceState) {
				super.onActivityCreated(savedInstanceState);
				GoogleMap map = getMap();
				if(map != null){
					map.addMarker(new MarkerOptions().position(location));
					CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
					map.animateCamera(update);
				}
			};
		};
		return fragment;//SupportMapFragment.newInstance(options);
	}

	public static SupportMapFragment fixedMapWithLocation(final LatLng location){
		SupportMapFragment fragment = new SupportMapFragment(){
			public void onActivityCreated(android.os.Bundle savedInstanceState) {
				super.onActivityCreated(savedInstanceState);
				GoogleMap map = getMap();

				map.addMarker(new MarkerOptions().position(location));
				map.getUiSettings().setAllGesturesEnabled(false);
				map.getUiSettings().setZoomControlsEnabled(false);
				ApuriMapUtils.configureFixedMapWithDirectionsIntent(this, location);
				
				getView().setMinimumHeight(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 185, getResources().getDisplayMetrics())));
			};
			
		
		};
		
		return fragment;
	}

	public static Intent directionsIntentTo(LatLng location) {

		String direct = "http://maps.google.com/maps?daddr=%f,%f";
		Uri uri = Uri.parse(String.format(Locale.US,direct,location.latitude,location.longitude));

		return new Intent(Intent.ACTION_VIEW,uri);
	}

	public static void configureFixedMapWithDirectionsIntent(final SupportMapFragment map,
			final LatLng location) {
		map.getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				map.getActivity().startActivity(ApuriMapUtils.directionsIntentTo(location));
			}
		});
		ApuriMapUtils.defaultZoom(map.getMap(), location);			
	}

}

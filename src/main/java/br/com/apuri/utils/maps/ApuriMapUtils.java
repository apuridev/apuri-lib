/*
 * Copyright  2015 apuri Developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.apuri.utils.maps;

import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ApuriMapUtils {

    /**
     * 15
     */
	private static final int DEFAULT_ZOOM = 15;

    /**
     * Apply the specified zoom level at the location of the map
     * @param map - the map
     * @param location - the location in the map
     * @param zoom - the zoom level 1 - 21
     */
	public static void applyZoom(GoogleMap map, LatLng location,int zoom){
		CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(location, zoom);
		map.animateCamera(cam);
		map.addMarker(new MarkerOptions().position(location));		
	}


    /**
     * Creates a {@link com.google.android.gms.maps.SupportMapFragment} centered at the provided location at the zoom level
     * of {@code DEFAULT_ZOOM}
     *
     * @param location - the location to center the map
     * @return a SupportMapFragment
     * @throws java.lang.IllegalArgumentException when the location is null
     */
	public static SupportMapFragment mapWithLocation(final LatLng location) {
        if(location == null)
            throw new IllegalArgumentException("The location can't be null");
		final CameraPosition position = new CameraPosition(location, DEFAULT_ZOOM, 0, 0);
		SupportMapFragment fragment = new SupportMapFragment(){
			public void onActivityCreated(android.os.Bundle savedInstanceState) {
				super.onActivityCreated(savedInstanceState);
				GoogleMap map = getMap();
				if(map != null){
//					map.addMarker(new MarkerOptions().position(location));
					CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
					map.animateCamera(update);
				}
			};
		};
		return fragment;//SupportMapFragment.newInstance(options);
	}

    /**
     * Calls {@link #fixedMapWithLocation(com.google.android.gms.maps.model.LatLng, float)} with a minHeight
     * of 185 DIP
     * @param location
     * @return
     */
	public static SupportMapFragment fixedMapWithLocation(final LatLng location){
        return  fixedMapWithLocation(location,185);
	}

    /**
     * Creates a {@link com.google.android.gms.maps.SupportMapFragment} fixed at the provided location with
     * the provided minHeight.
     * The map location can not be changed by user interaction
     * @param location  the location
     * @param minHeight the minimum height of the map view. This height will be used as parameter for
     *                  {@link android.util.TypedValue#applyDimension(int, float, android.util.DisplayMetrics)}
     * @return a SupportMapFragment
     * @throws java.lang.IllegalArgumentException if location is null
     */
    public static SupportMapFragment fixedMapWithLocation(final LatLng location, final float minHeight) {
        if(location == null)
            throw new IllegalArgumentException("The location can't be null");

        SupportMapFragment fragment = new SupportMapFragment(){

            public void onActivityCreated(android.os.Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                GoogleMap map = getMap();
                getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap map) {
                        map.getUiSettings().setAllGesturesEnabled(false);
                        map.getUiSettings().setZoomControlsEnabled(false);
                        ApuriMapUtils.applyZoom(map, location,DEFAULT_ZOOM);
                    }

                });
                getView().setMinimumHeight(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minHeight, getResources().getDisplayMetrics())));
            };
        };
        return fragment;
    }

        /**
         * Creates a directions intent for the provided location
         * @param location the location to be used for the {@link android.content.Intent}
         * @return a directions intent for the provided location
         * @throws java.lang.IllegalArgumentException if the location is null
         */
	public static Intent directionsIntentTo(LatLng location) {
        if(location == null)
            throw new IllegalArgumentException("The location can't be null");
		String direct = "http://maps.google.com/maps?daddr=%f,%f";
		Uri uri = Uri.parse(String.format(Locale.US,direct,location.latitude,location.longitude));

		return new Intent(Intent.ACTION_VIEW,uri);
	}

    /**
     * Add a direction intent to the provided location in response to the user touching the map.
     * @param map the map
     * @param location the location to ask for directions
     * @throws  java.lang.IllegalArgumentException if the map or the location are null
     */
	public static void configureMapWithDirectionsIntent(final SupportMapFragment map,
			final LatLng location) {
        if(map == null)
            throw new IllegalArgumentException("The map can not be null");
        else if (location == null)
            throw new IllegalArgumentException("The location can not be null");

		map.getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                map.getActivity().startActivity(ApuriMapUtils.directionsIntentTo(location));
            }
        });
	}

}

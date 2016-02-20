package com.eer.getirt.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Ergun on 20.02.2016.
 */
public class MapClickListener implements GoogleMap.OnMapClickListener {
    GoogleMap map;
    Marker marker;

    public MapClickListener(GoogleMap map, Marker marker){
        this.map = map;
        this.marker = marker;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        marker.setPosition(latLng);
    }
}

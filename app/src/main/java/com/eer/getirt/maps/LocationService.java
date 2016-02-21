package com.eer.getirt.maps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.eer.getirt.R;
import com.eer.getirt.activities.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Ergun on 20.02.2016.
 */
public class LocationService{

    static Context context;

    public LocationService(Context context){
        this.context = context;
    }

    //only works in MainActivity.
    public static void requestLocationPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            Snackbar.make(((Activity)context).findViewById(R.id.drawer_layout),
                    "Konum için izin gerekli", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Tamam", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions((Activity)context,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    0);
                        }
                    })
                    .show();
        }else{
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
    }

}

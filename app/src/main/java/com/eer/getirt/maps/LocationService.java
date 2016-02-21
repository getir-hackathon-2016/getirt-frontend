package com.eer.getirt.maps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;


/**
 * Created by Ergun on 20.02.2016.
 */
public class LocationService{

    static Context context;

    public LocationService(Context context){
        this.context = context;
    }

    /**
     * Makes a request ACCESS_FINE_LOCATION permission in runtime.
     */
    public static void requestLocationPermission(int viewId){
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            Snackbar.make(((Activity)context).findViewById(viewId),
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

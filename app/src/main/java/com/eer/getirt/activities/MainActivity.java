package com.eer.getirt.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.eer.getirt.R;
import com.eer.getirt.maps.LocationService;
import com.eer.getirt.maps.MapClickListener;
import com.eer.getirt.utils.SessionController;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Holds the main elements of the application. All navigations start from here.
 * The GoogleMap in it is used for prediction of the time that an order goes.
 * It contains the categories recycler view which can navigate the app to the
 * ProductsActivity.
 *
 * It implements OnMapReadyCallback to sync with google map
 * implements ConnectionCallBacks and ConnectionFailedListener to easily connect to
 * the GoogleApiClient.
 * It also implements LocationListener to act with the change in the location which is
 * got by LocationServices.FusedLocationApi
 * Created by Ergun on 20.02.2016.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    protected GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle
                (this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        ImageButton imageButtonBasket = (ImageButton)findViewById(R.id.main_image_button_basket);
        imageButtonBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, BasketActivity.class);
                startActivity(i);
            }
        });

        NavigationView navigationView = (NavigationView)findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_logout:{
                        SessionController sessionController = new SessionController(MainActivity.this);
                        sessionController.deleteSession();
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
                return false;
            }
        });

        mapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

        initGoogleApiClient();
    }

    /**
     * When the map is ready, it pins a marker to the location of the device
     * when the device moves, the pin also changes location in map.
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        Marker marker;
        mCurrLocationMarker = map.addMarker(new MarkerOptions()
                .position(new LatLng(41.085170, 29.050868))
                .title("Senin adresin"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(41.085170, 29.050868), 10));
        map.setIndoorEnabled(false);

        map.setOnMapClickListener(new MapClickListener(map, mCurrLocationMarker));
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }else{
            LocationService.requestLocationPermission(this, R.id.drawer_layout);
        }

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (mGoogleApiClient != null) {
                    LocationRequest locationRequest = new LocationRequest();

                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, MainActivity.this);
                    }else{
                        LocationService.requestLocationPermission(MainActivity.this, R.id.drawer_layout);
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }


    public void initGoogleApiClient(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    /**
     * Used to change the location of the marker when the location of the device changes.
     * @param location
     */
    @Override
    public void onLocationChanged(final Location location){
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Şimdiki pozisyonun");

                mCurrLocationMarker = googleMap.addMarker(markerOptions);

                googleMap.setOnMapClickListener(new MapClickListener(googleMap, mCurrLocationMarker));
                //move map camera
                googleMap.moveCamera(CameraUpdateFactory.
                        newLatLng(latLng));

            }
        });

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

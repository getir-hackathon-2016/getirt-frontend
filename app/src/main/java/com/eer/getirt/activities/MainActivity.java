package com.eer.getirt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.eer.getirt.R;
import com.eer.getirt.maps.MapClickListener;
import com.eer.getirt.utils.SessionController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by Ergun on 20.02.2016.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
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


        SupportMapFragment mapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap map) {

        Marker marker;
        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(-33.86997, 151.2089))
                .title("Senin adresin"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(-33.86997, 151.2089), 16));
        map.setIndoorEnabled(false);
        map.setOnMapClickListener(new MapClickListener(map, marker));
    }

}

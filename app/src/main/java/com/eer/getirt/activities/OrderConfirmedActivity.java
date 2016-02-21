package com.eer.getirt.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.eer.getirt.R;
import com.eer.getirt.connections.ConnectionManager;
import com.eer.getirt.utils.SessionController;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


/**
 * holds a map that shows the static location of the user order and gets dynamic locations
 * of messenger with using a socket then pin that location on the map.
 * Created by Ergun on 21.02.2016.
 */
public class OrderConfirmedActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://46.101.247.248:8000/");
        } catch (URISyntaxException e) {}
    }
    SupportMapFragment mapFragment;
    Marker messengerMarker;
    GoogleApiClient mGoogleApiClient;
    String sessiodId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);

        Toolbar toolbar = (Toolbar)findViewById(R.id.order_confirmed_toolbar);
        toolbar.setTitle("Getirt!");
        toolbar.setSubtitle("Sipariş Takip");

        SessionController sessionController = new SessionController(this);
        sessiodId = sessionController.getSessionId();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i = getIntent();
        final String address = i.getStringExtra("address");
        final double lat = i.getDoubleExtra("lat", 41.085170);
        final double lng = i.getDoubleExtra("lng", 29.050868);

        new MaterialDialog.Builder(this)
                .title("Kuryeniz yola çıkmaya hazır")
                .content(address + " adresine olan siparişinizi onaylıyor musunuz?")
                .theme(Theme.LIGHT)
                .positiveText("Evet")
                .negativeText("Hayır")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new ConfirmAsyncTask(address, lat, lng).execute();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent(OrderConfirmedActivity.this, BasketActivity.class);
                        startActivity(i);
                    }
                })
                .show();

        mapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_order_confirmed));
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Marker marker;
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title("Senin adresin"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(lat, lng)));
                googleMap.setIndoorEnabled(false);
            }
        }); //initially location of the user added.
        initGoogleApiClient();
        mSocket.connect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivityForResult(myIntent, 0);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class ConfirmAsyncTask extends AsyncTask<Void, Void, JSONObject>{

        String address;
        double lat;
        double lng;
        ProgressDialog progress;

        ConfirmAsyncTask(String address, double lat, double lng){
            this.address = address;
            this.lat = lat;
            this.lng = lng;
            progress = new ProgressDialog(OrderConfirmedActivity.this);
        }

        @Override
        protected void onPreExecute(){
            progress.setMessage("Onaylanıyor.");
            progress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return ConnectionManager.confirmBasket(OrderConfirmedActivity.this, address, lat, lng);
        }

        @Override
        protected void onPostExecute(JSONObject jsonResult){
            boolean result;
            String message;
            try{
                result = jsonResult.getBoolean("result");
                message = jsonResult.getString("message");

                Snackbar.make(findViewById(R.id.order_confirmed_layout), message, Snackbar.LENGTH_LONG)
                        .show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progress.dismiss();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("sessionCode", sessiodId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.on("messenger position", onNewLocation);
            mSocket.emit("configuration", jsonObject);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("messenger position", onNewLocation);
    }

    @Override
    public void onStart(){
        super.onStart();
        mGoogleApiClient.connect();

    }

    private Emitter.Listener onNewLocation = new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
            /* this code block runs in a ui thread because it changes something on a view. */
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    try {
                        final double lat = data.getDouble("x");
                        final double lng = data.getDouble("y");
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                if(messengerMarker == null) {
                                    messengerMarker = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lat, lng))
                                            .title("Kurye adresi"));
                                }else{
                                    messengerMarker.setPosition(new LatLng(lat, lng));
                                }
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                                        new LatLng(lat, lng)));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}

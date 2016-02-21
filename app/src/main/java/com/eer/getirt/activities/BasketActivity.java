package com.eer.getirt.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.eer.getirt.R;
import com.eer.getirt.adapters.RVBasketAdapter;
import com.eer.getirt.connections.ConnectionManager;
import com.eer.getirt.models.BasketProduct;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Basket (Cart) activity; shows user his cart, enables it to change/delete
 * the cart items. When the cart is confirmed it takes an address with
 * Google Places API and sends cart information to the server.
 * Created by Ergun on 20.02.2016.
 */
public class BasketActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private String address;
    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        Toolbar toolbar = (Toolbar)findViewById(R.id.basket_toolbar);
        toolbar.setTitle("Getirt!");
        toolbar.setSubtitle("Sepet");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        RecyclerView rv = (RecyclerView)findViewById(R.id.basket_rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        RVBasketAdapter rvBasketAdapter = new RVBasketAdapter(BasketProduct.getDummyBasketData());
        rv.setAdapter(rvBasketAdapter);

        Button button = (Button)findViewById(R.id.header_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickAPlace();
            }
        });

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        new GetBasketProductsAsyncTask(rvBasketAdapter).execute();

    }

    @Override
    public void onStart(){
        super.onStart();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivityForResult(myIntent, 0);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    /**
     * Goes to PlacePicker intent, choses an address and returns with same
     * request code. Then the Intent result is handled in OnActivityResult
     */
    public void pickAPlace(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    /**
     * Handles the intent result which contains a Place data then sends server the basket data
     * and place address.
     * @param requestCode - intent request code
     * @param resultCode - intent result code
     * @param data - intent data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("BASKET_GOOGLE", "failed");
    }

    /**
     * AsyncTask class which asynchronously gets BasketProduct data after getting the data
     * it changes the adapter of the RecyclerView in BasketActivity
     */
    class GetBasketProductsAsyncTask extends AsyncTask<Void, Void, JSONObject>{

        RVBasketAdapter rvBasketAdapter;

        GetBasketProductsAsyncTask(RVBasketAdapter rvBasketAdapter){
            this.rvBasketAdapter = rvBasketAdapter;
        }

        ProgressDialog progressDialog = new ProgressDialog(BasketActivity.this);
        @Override
        protected void onPreExecute(){
            progressDialog.setMessage("Sepetiniz yükleniyor.");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return ConnectionManager.getBasketProducts(BasketActivity.this);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject){
            boolean result;
            String message = "";
            ArrayList<BasketProduct> basketProducts = new ArrayList<>();
            try{
                result = jsonObject.getBoolean("result");
                if(!result){

                    message = jsonObject.getString("message");
                    View v = (BasketActivity.this).findViewById(R.id.basket_rv);
                    Snackbar
                            .make(v, message, Snackbar.LENGTH_SHORT)
                            .show();
                }else{
                    JSONArray jsonBasketProductsArray = jsonObject.getJSONArray("basketProducts");
                    //String basketProduct = jsonObject.getString("basketProducts");
                    for(int i = 0; i < jsonBasketProductsArray.length(); i++){
                        JSONObject jsonBasketProduct = jsonBasketProductsArray.getJSONObject(i);
                        String name = jsonBasketProduct.getString("name");
                        String price = jsonBasketProduct.getString("price");
                        String quantity = jsonBasketProduct.getString("number");
                        String productId = jsonBasketProduct.getString("_id");

                        BasketProduct basketProduct = new BasketProduct(name, price + " TL", quantity, productId);
                        basketProducts.add(basketProduct);
                    }
                    //Log.d("Basket products : ", basketProduct);
                    rvBasketAdapter.changeDataList(basketProducts);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }

}

package com.eer.getirt.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.eer.getirt.R;
import com.eer.getirt.adapters.RVBasketAdapter;
import com.eer.getirt.adapters.RVCategoryAdapter;
import com.eer.getirt.connections.ConnectionManager;
import com.eer.getirt.models.BasketProduct;
import com.eer.getirt.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ergun on 20.02.2016.
 */
public class BasketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        Toolbar toolbar = (Toolbar)findViewById(R.id.basket_toolbar);
        toolbar.setTitle("Getirt!");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        RecyclerView rv = (RecyclerView)findViewById(R.id.basket_rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        RVBasketAdapter rvBasketAdapter = new RVBasketAdapter(BasketProduct.getDummyBasketData());
        rv.setAdapter(rvBasketAdapter);

        new GetBasketProductsAsyncTask(rvBasketAdapter).execute();

    }

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

                        BasketProduct basketProduct = new BasketProduct(name, price, quantity, productId);
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

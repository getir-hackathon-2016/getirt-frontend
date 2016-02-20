package com.eer.getirt.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.eer.getirt.R;
import com.eer.getirt.adapters.RVProductsAdapter;
import com.eer.getirt.connections.ConnectionManager;
import com.eer.getirt.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ergun on 20.02.2016.
 */
public class ProductsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Toolbar toolbar = (Toolbar) findViewById(R.id.products_toolbar);
        toolbar.setTitle("Getirt!");
        toolbar.setSubtitle("Ürünler");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        RecyclerView rv =(RecyclerView)findViewById(R.id.products_rv);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rv.setLayoutManager(gridLayoutManager);

        RVProductsAdapter rvProductsAdapter = new RVProductsAdapter(Product.getDummyData());
        rv.setAdapter(rvProductsAdapter);

        Intent intent = getIntent();
        String categoryId = intent.getStringExtra("category_id");

        new GetProductsAsyncTask(categoryId, 10, 0, rvProductsAdapter).execute();



    }

    class GetProductsAsyncTask extends AsyncTask<Void, Void, JSONObject> {


        RVProductsAdapter rvProductsAdapter;
        String categoryId;
        int limitNumber = 10;
        int skipNumber = 0;

        public GetProductsAsyncTask(String categoryId, int limitNumber, int skipNumber, RVProductsAdapter rvProductsAdapter){
            this.categoryId = categoryId;
            this.limitNumber = limitNumber;
            this.skipNumber = skipNumber;
            this.rvProductsAdapter = rvProductsAdapter;
        }

        ProgressDialog progressDialog = new ProgressDialog(ProductsActivity.this);

        @Override
        protected void onPreExecute(){
            progressDialog.setMessage("Ürünler alınıyor.");
            progressDialog.show();
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            return ConnectionManager.getProducts(categoryId, limitNumber, skipNumber);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject){
            boolean result;
            ArrayList<Product> products = new ArrayList<>();
            try{
                result = jsonObject.getBoolean("result");
                Log.d("result from connection", Boolean.toString(result));
                if(!result){
                    String message = jsonObject.getString("message");
                    View v = (ProductsActivity.this).findViewById(R.id.products_rv);
                    Snackbar
                            .make(v, message, Snackbar.LENGTH_SHORT)
                            .show();
                }else{
                    JSONArray jsonProductsArray = jsonObject.getJSONArray("urunler");
                    for(int i = 0; i < jsonProductsArray.length(); i++){
                        JSONObject productObject = jsonProductsArray.getJSONObject(i);
                        Product p = new Product(productObject.getString("urunAdi"), productObject.getString("fiyat"));
                        products.add(p);
                    }
                    rvProductsAdapter.changeDataList(products);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }
}

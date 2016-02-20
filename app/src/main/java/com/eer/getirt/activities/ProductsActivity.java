package com.eer.getirt.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.eer.getirt.R;
import com.eer.getirt.adapters.RVProductsAdapter;
import com.eer.getirt.connections.ConnectionManager;
import com.eer.getirt.models.Product;
import com.eer.getirt.utils.RecyclerItemClickListener;

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
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        RecyclerView rv =(RecyclerView)findViewById(R.id.products_rv);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rv.setLayoutManager(gridLayoutManager);

        final RVProductsAdapter rvProductsAdapter = new RVProductsAdapter(Product.getDummyData(), this);
        rv.setAdapter(rvProductsAdapter);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Product product = rvProductsAdapter.getDataList().get(position);
                new AddToBasketAsyncTask(product.getProductId()).execute();
            }
        }));

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.products_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);

                builder.setTitle("Ürün öner");

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        Intent intent = getIntent();
        String categoryId = intent.getStringExtra("category_id");
        String categoryName = intent.getStringExtra("category_name");
        toolbar.setSubtitle(categoryName);

        new GetProductsAsyncTask(categoryId, 10, 0, rvProductsAdapter).execute();

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
            return ConnectionManager.getProducts(categoryId, limitNumber, skipNumber, ProductsActivity.this);
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
                    final Snackbar snackBar = Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE);
                    snackBar.setAction("Kapat", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackBar.dismiss();
                        }
                    });
                    snackBar.show();
                }else{
                    JSONArray jsonProductsArray = jsonObject.getJSONArray("products");
                    for(int i = 0; i < jsonProductsArray.length(); i++){
                        JSONObject productObject = jsonProductsArray.getJSONObject(i);
                        Product p = new Product(productObject.getString("name"),
                                productObject.getString("price"), productObject.getString("_id"));
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

    class AddToBasketAsyncTask extends AsyncTask<Void, Void, JSONObject>{

        String productId;

        public AddToBasketAsyncTask(String productId){
            this.productId = productId;
        }

        ProgressDialog progressDialog = new ProgressDialog(ProductsActivity.this);

        @Override
        protected void onPreExecute(){
            progressDialog.setMessage("Sepete ekleniyor.");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return ConnectionManager.addToBasket(ProductsActivity.this, productId);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject){
            progressDialog.dismiss();
            boolean result = false;
            try {
                Snackbar.make(ProductsActivity.this.findViewById(R.id.products_layout),
                        jsonObject.getString("message"), Snackbar.LENGTH_LONG)
                        .show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}

package com.eer.getirt.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.eer.getirt.R;
import com.eer.getirt.adapters.RVBasketAdapter;
import com.eer.getirt.models.BasketProduct;

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

    }

}

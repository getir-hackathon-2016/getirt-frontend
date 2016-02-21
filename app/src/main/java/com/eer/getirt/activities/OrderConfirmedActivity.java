package com.eer.getirt.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eer.getirt.R;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

/**
 * Created by Ergun on 21.02.2016.
 */
public class OrderConfirmedActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);

        MaterialStyledDialog dialog = new MaterialStyledDialog(this)
                .setTitle("title")
                .setDescription("descr");
        dialog.show();
    }
}

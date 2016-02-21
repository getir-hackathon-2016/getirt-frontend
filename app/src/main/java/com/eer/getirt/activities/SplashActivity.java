package com.eer.getirt.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eer.getirt.R;
import com.eer.getirt.connections.ConnectionManager;
import com.eer.getirt.utils.SessionController;

/**
 * Created by Ergun on 21.02.2016.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);

        boolean networkStatus = ConnectionManager.isNetworkConnected(this);
        if(networkStatus){
            SessionController sessionController = new SessionController(this);
            boolean sessionStatus = sessionController.isSessionOn();
            if(sessionStatus){
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }else{
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            }
        }else{
            Snackbar.
                    make(findViewById(R.id.splash_layout), "İnternet bağlantısı bulunamadı", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Yeniden Dene", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SplashActivity.this.recreate();
                        }
                    })
                    .dismiss();
        }

    }
}

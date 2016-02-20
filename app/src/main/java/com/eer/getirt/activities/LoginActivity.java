package com.eer.getirt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eer.getirt.R;
import com.eer.getirt.utils.LoginUtils;

/**
 * A simple login activity.
 * @author Ergun Erdogmus
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editTextEmail = (EditText)findViewById(R.id.login_edit_text_email);
        EditText editTextPassword = (EditText)findViewById(R.id.login_edit_text_password);
        Button buttonLogin = (Button)findViewById(R.id.login_button);

        buttonLogin.setOnClickListener(new LoginClickListener(editTextEmail, editTextPassword));

        TextView textViewRegister = (TextView)findViewById(R.id.login_text_view_register);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public class LoginClickListener implements View.OnClickListener{

        EditText editTextEmail;
        EditText editTextPassword;

        public LoginClickListener(EditText editTextEmail, EditText editTextPassword){
            this.editTextEmail = editTextEmail;
            this.editTextPassword = editTextPassword;
        }

        @Override
        public void onClick(View view) {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            LoginUtils.LoginAsyncTask loginAsyncTask = new LoginUtils().new LoginAsyncTask(email, password, LoginActivity.this);
            loginAsyncTask.execute();
        }
    }
}

package com.eer.getirt.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.eer.getirt.R;
import com.eer.getirt.utils.RegisterUtils;

/**
 * A simple register activity.
 * Created by Ergun on 19.02.2016.
 */
public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText editTextUsername = (EditText)findViewById(R.id.register_edit_text_username);
        EditText editTextPassword = (EditText)findViewById(R.id.register_edit_text_password);
        EditText editTextEmail = (EditText)findViewById(R.id.register_edit_text_email);

        Button buttonRegister = (Button)findViewById(R.id.register_button);

        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        buttonRegister.setOnClickListener(new RegisterClickListener(username, password, email));

    }

    public class RegisterClickListener implements View.OnClickListener {
        String username, password, email;
        public RegisterClickListener(String username, String password, String email){
            this.username = username;
            this.password = password;
            this.email = email;
        }
        @Override
        public void onClick(View view) {
            RegisterUtils.RegisterAsyncTask registerAsyncTask = new RegisterUtils().
                    new RegisterAsyncTask(username, password, email, RegisterActivity.this);
            registerAsyncTask.execute();
        }
    }
}

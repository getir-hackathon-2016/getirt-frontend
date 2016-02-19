package com.eer.getirt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        buttonRegister.setOnClickListener(new RegisterClickListener(editTextUsername, editTextPassword, editTextEmail));

        TextView textViewAlreadyAUser = (TextView)findViewById(R.id.already_a_user);
        textViewAlreadyAUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    /**
     * An OnClickListener implementation to get EditText views dynamically.
     */
    public class RegisterClickListener implements View.OnClickListener {
        EditText editTextUsername, editTextPassword, editTextEmail;

        public RegisterClickListener(EditText editTextUsername, EditText editTextPassword, EditText editTextEmail){
            this.editTextUsername = editTextUsername;
            this.editTextPassword = editTextPassword;
            this.editTextEmail = editTextEmail;
        }

        @Override
        public void onClick(View view) {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();

            RegisterUtils.RegisterAsyncTask registerAsyncTask = new RegisterUtils().
                    new RegisterAsyncTask(username, password, email, RegisterActivity.this);
            registerAsyncTask.execute();
        }
    }
}

package com.eer.getirt.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.eer.getirt.R;
import com.eer.getirt.activities.LoginActivity;
import com.eer.getirt.activities.MainActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Holds the LoginAsyncTask class and login helper methods
 * LoginAsyncTask class makes connection with server and checks whether
 * the login process is successfully completed.
 * Created by Ergun on 19.02.2016.
 */
public class LoginUtils{

    /**
     * Sends a get request to the server to login.
     * @param email
     * @param password
     * @return result - result from the server, whether login is successful or not
     */

    public JSONObject attemptLogin(String email, String password){

        String requestUrl = Constants.serverUrl + "/login";
        Log.d("Request url : ", requestUrl);

        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password);
            jsonObject.put("email", email);
        }catch(JSONException e){
            Log.d("Exception ocurred : ", e.getMessage());
        }

        RequestBody requestBody = RequestBody.create(Constants.JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .header("appsecret", Constants.appSecret)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        }catch(IOException ex){
            Log.d("login exception : ", ex.getMessage());
        }catch(JSONException ex){
            Log.d("Json exception : ", ex.getMessage());
        }

        JSONObject resultObject = new JSONObject();

        try{
            resultObject.put("result", false);
            resultObject.put("message", "Bir hata oluştu :(");
        }catch(JSONException ex){
            Log.d("json exception : ", ex.getMessage());
        }

        return resultObject;
    }

    /**
     * AsnycTask class to send the http request asynchronously.
     */
    public class LoginAsyncTask extends AsyncTask<Void, Void, JSONObject>{

        ProgressDialog progress;

        String email, password;
        Context context;

        public LoginAsyncTask(String email, String password, Context context){

            this.email = email;
            this.password = password;
            this.context = context;
            progress = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute(){
            progress.setMessage("Giriş yapılıyor.");
            progress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return attemptLogin(email, password);
        }

        @Override
        protected void onPostExecute(JSONObject resultObject){

            boolean result = false;
            String message = "Bir hata oluştu :(";

            try{
                result = resultObject.getBoolean("result");
                message = resultObject.getString("message");
            }catch (JSONException ex){
                Log.d("json exception : ", ex.getMessage());
            }

            if(!result){
                View v = ((Activity)context).findViewById(R.id.login_layout);
                Snackbar
                        .make(v, message, Snackbar.LENGTH_SHORT)
                        .show();
            }else{
                SessionController sessionController = new SessionController((Activity)context);
                sessionController.createSession(message);

                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
                ((Activity)context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out);

            }

            progress.dismiss();
        }
    }

}

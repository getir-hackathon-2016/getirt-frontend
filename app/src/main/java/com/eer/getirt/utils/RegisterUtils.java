package com.eer.getirt.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.eer.getirt.R;
import com.eer.getirt.activities.LoginActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOError;
import java.io.IOException;

/**
 * Holds the RegisterAsyncTask class and register helper methods
 * RegisterAsyncTask class makes connection with server and checks whether
 * the register process is successfully finished.
 * Created by Ergun on 19.02.2016.
 */
public class RegisterUtils{

    /**
     * Sends a post request to the server to register a user.
     * @param username
     * @param password
     * @param email
     * @return result - result from the server, whether register is successful or not
     */

    public JSONObject attemptRegister(String username, String password, String email){

        String requestUrl = Constants.serverUrl + "/kaydol";
        Log.d("Request url : ", requestUrl);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ad", username);
            jsonObject.put("parola", password);
            jsonObject.put("email", email);
        }catch(JSONException e){
            Log.d("Exception ocurred : ", e.getMessage());
        }

        RequestBody requestBody = RequestBody.create(Constants.JSON, jsonObject.toString());
        Log.d("Request body : ", jsonObject.toString());
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .header("Content-Type", "application/json")
                .header("appsecret", Constants.appSecret)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        }catch(IOException ex){
            Log.d("register exception : ", ex.getMessage());
        }catch(JSONException ex){
            Log.d("json expection :", ex.getMessage());
        }

        JSONObject jsonResult = new JSONObject();
        try {
            jsonResult.put("result", false);
            jsonResult.put("message", "Başarısız");
        }catch(JSONException ex){
            Log.d("json exception : ", ex.getMessage());
        }

        return jsonResult;
    }

    /**
     * AsnycTask class to send the http request asynchronously.
     */
    public class RegisterAsyncTask extends AsyncTask<Void, Void, JSONObject>{

        ProgressDialog progress;

        String username, password, email;
        Context context;

        public RegisterAsyncTask(String username, String password, String email, Context context){
            this.username = username;
            this.password = password;
            this.email = email;
            this.context = context;
            progress = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute(){
            progress.setMessage("Üye olunuyor.");
            progress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return attemptRegister(username, password, email);
        }

        @Override
        protected void onPostExecute(JSONObject resultObject){
            boolean result = false;
            String message = "Bir hata oluştu.";

            try{
                result = resultObject.getBoolean("result");
                message = resultObject.getString("message");
            }catch(JSONException e){
                Log.d("json exception : ", e.getMessage());
            }

            if(!result){
                View v = ((Activity)context).findViewById(R.id.register_layout);
                Snackbar
                        .make(v, message, Snackbar.LENGTH_SHORT)
                        .show();
            }else{
                Intent i = new Intent(context, LoginActivity.class);
                context.startActivity(i);
            }

            progress.dismiss();
            Log.d("Kayit durumu : ", message);
        }
    }

}

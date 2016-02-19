package com.eer.getirt.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.eer.getirt.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOError;
import java.io.IOException;

/**
 * Holds the RegisterAsyncTask class and register helper methods
 * RegisterAsyncTask class makes connection with server and checks whether
 * the register process is successfully finished.
 * Created by Ergun on 19.02.2016.
 */
public class LoginUtils{

    /**
     * Sends a get request to the server to login.
     * @param email
     * @param password
     * @return result - result from the server, whether login is successful or not
     */

    public String attemptLogin(String email, String password){

        String requestUrl = Constants.serverUrl + "/login/" + email + "/" + password;
        Log.d("Request url : ", requestUrl);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(requestUrl)
                .header("appsecret", Constants.appSecret)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        }catch(IOException ex){
            Log.d("login exception : ", ex.getMessage());
        }

        return "false:basarisiz";
    }

    /**
     * AsnycTask class to send the http request asynchronously.
     */
    public class LoginAsyncTask extends AsyncTask<Void, Void, String>{

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
        protected String doInBackground(Void... voids) {
            return attemptLogin(email, password);
        }

        @Override
        protected void onPostExecute(String result){
            try {
                String registerResult = result.split(":")[0]; //servers sends like "true:basariyla üye oldunuz"
                String registerMessage = result.split(":")[1];
                if(registerResult.equals("false")) {
                    View v = ((Activity)context).findViewById(R.id.register_layout);
                    Snackbar
                            .make(v, registerMessage, Snackbar.LENGTH_SHORT)
                            .show();
                }else{
                    //intent ile main activitye gönderilecek, dönen değer kayıt edilecek filan.
                }
            }catch (Exception e){
                View v = ((Activity)context).findViewById(R.id.register_layout);
                Snackbar
                        .make(v, "Bir hata oluştu :(", Snackbar.LENGTH_SHORT)
                        .show();
            }

            progress.dismiss();
            Log.d("Giris durumu : ", result);
        }
    }

}

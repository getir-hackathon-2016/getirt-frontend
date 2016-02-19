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
public class RegisterUtils{

    /**
     * Sends a get request to the server to register a user.
     * @param username
     * @param password
     * @param email
     * @return result - result from the server, whether register is successful or not
     */

    public String attemptRegister(String username, String password, String email){

        String requestUrl = Constants.serverUrl + "/register/" + username + "/" + email + "/" + password;
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
            Log.d("register exception : ", ex.getMessage());
        }

        return "false:basarisiz";
    }

    /**
     * AsnycTask class to send the http request asynchronously.
     */
    public class RegisterAsyncTask extends AsyncTask<Void, Void, String>{

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
        protected String doInBackground(Void... voids) {
            return attemptRegister(username, password, email);
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
            Log.d("Kayit durumu : ", result);
        }
    }

}

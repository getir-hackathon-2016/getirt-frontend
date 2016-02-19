package com.eer.getirt.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

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
     * @return result - result from the server, whether register successful or not
     */

    public String attemptRegister(String username, String password, String email){
        String requestUrl = Constants.serverUrl + "/register/" + username + "/" + email + "/" + password;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(requestUrl)
                .header("appSecret", Constants.appSecret)
                .build();

        return request.body().toString();
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
            progress.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return attemptRegister(username, password, email);
        }

        @Override
        protected void onPostExecute(String result){
            String registerResult = result.split(":")[0]; //servers sends like "true:basariyla üye oldunuz"
            String registerMessage = result.split(":")[1];

            Log.d("Kayit durumu : ", registerMessage);
        }
    }

}

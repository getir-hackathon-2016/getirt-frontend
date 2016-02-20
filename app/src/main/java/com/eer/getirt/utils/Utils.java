package com.eer.getirt.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Ergun on 20.02.2016.
 */
public class Utils {

    public static void showSnackbar(View v, String message){
        Snackbar
                .make(v, message, Snackbar.LENGTH_SHORT)
                .show();
    }

    public static JSONObject makeRequest(String requestUrl){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("appsecret", Constants.appSecret)
                .url(requestUrl)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("result", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

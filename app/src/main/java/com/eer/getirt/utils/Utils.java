package com.eer.getirt.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    public static JSONObject makeRequest(String requestUrl, Context context){

        SessionController sessionController = new SessionController((Activity)context);
        String sessionId = sessionController.getSessionId();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("appsecret", Constants.appSecret)
                .header("sessioncode", sessionId)
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
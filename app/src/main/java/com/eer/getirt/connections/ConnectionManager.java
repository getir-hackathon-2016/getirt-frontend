package com.eer.getirt.connections;

import com.eer.getirt.models.Category;
import com.eer.getirt.utils.Constants;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ergun on 20.02.2016.
 */
public class ConnectionManager {

    public static JSONObject getAllCategories(){
        String requestUrl = Constants.serverUrl + "/kategoriler";

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

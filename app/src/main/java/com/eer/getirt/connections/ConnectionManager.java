package com.eer.getirt.connections;

import com.eer.getirt.models.Category;
import com.eer.getirt.utils.Constants;
import com.eer.getirt.utils.Utils;
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
        return Utils.makeRequest(requestUrl);
    }

    public static JSONObject getProducts(String categoryId, int limitNumber, int skipNumber){
        String requestUrl = Constants.serverUrl + "/kategoriUrunleri/" + categoryId +
                "/" + skipNumber +
                "/" + limitNumber;
        return Utils.makeRequest(requestUrl);
    }

}

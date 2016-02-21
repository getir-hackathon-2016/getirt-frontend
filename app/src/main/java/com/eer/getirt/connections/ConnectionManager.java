package com.eer.getirt.connections;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.eer.getirt.utils.Constants;
import com.eer.getirt.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ergun on 20.02.2016.
 */
public class ConnectionManager {

    public static JSONObject getAllCategories(Context context){
        String requestUrl = Constants.serverUrl + "/categories";
        return Utils.makeGetRequest(requestUrl, context);
    }

    public static JSONObject getProducts(String categoryId, int limitNumber, int skipNumber, Context context){
        String requestUrl = Constants.serverUrl + "/productsofcategory/" + categoryId +
                "/" + skipNumber +
                "/" + limitNumber;
        return Utils.makeGetRequest(requestUrl, context);
    }

    public static JSONObject getBasketProducts(Context context){
        String requestUrl = Constants.serverUrl + "/orderproducts";
        return Utils.makeGetRequest(requestUrl, context);
    }

    public static JSONObject addToBasket(Context context, String productId){
        String requestUrl = Constants.serverUrl + "/updateProducts";
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("productNo", productId);
            jsonObject.put("number", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Utils.makePostRequest(requestUrl, jsonObject, context);
    }

    public static boolean isNetworkConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}

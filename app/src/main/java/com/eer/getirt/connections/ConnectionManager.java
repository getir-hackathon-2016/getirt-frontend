package com.eer.getirt.connections;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.eer.getirt.utils.Constants;
import com.eer.getirt.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Holds some functions for Connections.
 * Created by Ergun on 20.02.2016.
 */
public class ConnectionManager {

    /**
     * It makes a get request to the server to get all categories and returns that categories
     * in JSON format.
     * @param context
     * @return jsonObject
     */
    public static JSONObject getAllCategories(Context context){
        String requestUrl = Constants.serverUrl + "/categories";
        return Utils.makeGetRequest(requestUrl, context);
    }

    /**
     * It makes a get request to the server to get products by categoryId
     * and skips some and limits the number of products that will be returned. Then returns
     * the products in JSON format.
     * @param categoryId - unique category id
     * @param limitNumber
     * @param skipNumber
     * @param context
     * @return
     */
    public static JSONObject getProducts(String categoryId, int limitNumber, int skipNumber, Context context){
        String requestUrl = Constants.serverUrl + "/productsofcategory/" + categoryId +
                "/" + skipNumber +
                "/" + limitNumber;
        return Utils.makeGetRequest(requestUrl, context);
    }

    /**
     * Gets the logged user's basket(cart) products.
     * @param context
     * @return
     */
    public static JSONObject getBasketProducts(Context context){
        String requestUrl = Constants.serverUrl + "/orderproducts";
        return Utils.makeGetRequest(requestUrl, context);
    }

    /**
     * Makes a post request to the server to add a product to the basket(cart) then returns a
     * JSONObject result
     * @param context
     * @param productId - unique product id
     * @return
     */
    public static JSONObject addToBasket(Context context, String productId){
        String requestUrl = Constants.serverUrl + "/updateProducts";
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("productNo", productId);
            jsonObject.put("number", "1");
            jsonObject.put("doo", "increase");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Utils.makePostRequest(requestUrl, jsonObject, context);
    }

    /**
     * returns whether the device has a network connection.
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}

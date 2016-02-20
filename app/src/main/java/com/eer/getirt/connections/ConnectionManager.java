package com.eer.getirt.connections;

import android.content.Context;

import com.eer.getirt.utils.Constants;
import com.eer.getirt.utils.Utils;
import org.json.JSONObject;

/**
 * Created by Ergun on 20.02.2016.
 */
public class ConnectionManager {

    public static JSONObject getAllCategories(Context context){
        String requestUrl = Constants.serverUrl + "/categories";
        return Utils.makeRequest(requestUrl, context);
    }

    public static JSONObject getProducts(String categoryId, int limitNumber, int skipNumber, Context context){
        String requestUrl = Constants.serverUrl + "/productsofcategory/" + categoryId +
                "/" + skipNumber +
                "/" + limitNumber;
        return Utils.makeRequest(requestUrl, context);
    }

    public static JSONObject getBasketProducts(Context context){
        String requestUrl = Constants.serverUrl + "/orderproducts";
        return Utils.makeRequest(requestUrl, context);
    }

}

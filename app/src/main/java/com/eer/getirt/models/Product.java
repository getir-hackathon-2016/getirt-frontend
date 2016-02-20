package com.eer.getirt.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Ergun on 20.02.2016.
 */
public class Product {
    String productName;
    String productPriceStr;
    String categoryName;


    boolean isOnBasket;

    public Product(String productName, String productPriceStr, boolean isOnBasket){
        this.productName = productName;
        this.productPriceStr = productPriceStr;
        this.isOnBasket = isOnBasket;
    }


    public boolean isOnBasket() {
        return isOnBasket;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPriceStr() {
        return productPriceStr;
    }

    public static ArrayList<Product> getDummyData(){
        ArrayList<Product> products = new ArrayList<Product>();
        for(int i = 0; i < 10; i++){
            products.add(new Product("Product name " + i, "5,00", true));
        }
        return products;
    }
}

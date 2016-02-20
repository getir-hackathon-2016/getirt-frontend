package com.eer.getirt.models;

import java.util.ArrayList;

/**
 * Created by Ergun on 20.02.2016.
 */
public class BasketProduct extends Product {

    String productQuantity;

    public BasketProduct(String productName, String productPriceStr, String productQuantity) {
        super(productName, productPriceStr, true);
        this.productQuantity = productQuantity;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public static ArrayList<BasketProduct> getDummyBasketData(){
        ArrayList<BasketProduct> products = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            products.add(new BasketProduct("Name " + i, "" + i, "" + (i + 3)));
        }
        return products;
    }


}

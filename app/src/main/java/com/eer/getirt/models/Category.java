package com.eer.getirt.models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by Ergun on 20.02.2016.
 */
public class Category {
    String name;
    Drawable image;
    Category(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static ArrayList<Category> getDummyData(){
        ArrayList<Category> categories = new ArrayList<Category>();
        for(int i = 0; i < 10; i++){
            categories.add(new Category("Category " + i));
        }
        return categories;
    }

}

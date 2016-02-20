package com.eer.getirt.models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by Ergun on 20.02.2016.
 */
public class Category {
    String id;
    String name;
    Drawable image;
    public Category(String id, String name){
        this.name = name;
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public String getId(){ return id; }

    public static ArrayList<Category> getDummyData(){
        ArrayList<Category> categories = new ArrayList<Category>();
        for(int i = 0; i < 10; i++){
            categories.add(new Category("Category " + i, "asd"));
        }
        return categories;
    }

}

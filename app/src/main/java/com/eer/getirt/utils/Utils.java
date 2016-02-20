package com.eer.getirt.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Ergun on 20.02.2016.
 */
public class Utils {
    public static void showSnackbar(View v, String message){
        Snackbar
                .make(v, message, Snackbar.LENGTH_SHORT)
                .show();
    }
}

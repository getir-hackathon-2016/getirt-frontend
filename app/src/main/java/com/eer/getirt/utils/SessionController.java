package com.eer.getirt.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ergun on 20.02.2016.
 */
public class SessionController {

    Activity activity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public SessionController(Activity activity){
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("session", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        editor = sharedPreferences.edit();
    }

    public void deleteSession(){
        editor.putString("session_id", "");
        editor.commit();
    }

    public void createSession(String sessionId){
        editor.putString("session_id", sessionId);
        editor.commit();
    }

    public String getSessionId(){
        return(sharedPreferences.getString("session_id", ""));
    }


}

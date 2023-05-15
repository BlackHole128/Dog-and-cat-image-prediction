package com.example.dogimagepredection;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManeger {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManeger(Context context){
        sharedPreferences =context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }
    public void saveSession(UserS user){
        String id = user.getId();

        editor.putInt(SESSION_KEY, Integer.parseInt(id)).commit();
    }
    public int getSession(){
        return sharedPreferences.getInt(SESSION_KEY,-1);
    }
    public void remmoveSesssion(){
        editor.putInt(SESSION_KEY,-1).commit();
    }
}

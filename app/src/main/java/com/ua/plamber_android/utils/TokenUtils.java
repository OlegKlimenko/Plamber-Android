package com.ua.plamber_android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ua.plamber_android.activitys.LoginActivity;

public class TokenUtils {

    public final static String TOKEN = "Token";

    Context context;

    public TokenUtils(Context context) {
        this.context = context;
    }

    public boolean checkUserToken() {
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.contains(TOKEN);
    }

    public void writeToken(String token) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public void removeToken() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public String readToken() {
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(LoginActivity.TOKEN, "default");
    }
}

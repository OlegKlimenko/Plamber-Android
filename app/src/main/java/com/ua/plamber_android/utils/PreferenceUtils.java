package com.ua.plamber_android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TokenUtils {

    private final static String TOKEN = "Token";
    private final static String VERSION = "Version";
    private final static String USER_NAME = "UserName";
    private final static String EMAIL = "Email0";

    private Context context;

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
        editor.apply();
    }

    public void removeToken() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public String readToken() {
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(TOKEN, "default");
    }
}

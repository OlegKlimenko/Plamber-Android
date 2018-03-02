package com.ua.plamber_android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public final static String TOKEN = "Token";
    public final static String USER_NAME = "User_name";
    public final static String USER_EMAIL = "User_email";
    public final static String USER_PHOTO = "User_photo";
    public final static String OFFLINE_MODE = "Offline_mode";
    public final static String TOOLBAR_HIDE = "Toolbar_hide";
    public final static String STATUS_BAR_HIDE = "Status_bar_hide";

    private Context context;

    public PreferenceUtils(Context context) {
        this.context = context;
    }

    public boolean checkPreference(String preference) {
        return getPreference().contains(preference);
    }

    public void writePreference(String preference, String data) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(preference, data);
        editor.apply();
    }

    public void removePreference() {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.clear();
        editor.apply();
    }

    public String readPreference(String preference) {
        return getPreference().getString(preference, "default");
    }

    public void writeLogic(String preference, boolean status) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putBoolean(preference, status);
        editor.apply();
    }

    public boolean readLogic(String preference) {
        return getPreference().getBoolean(preference, false);
    }

    private SharedPreferences getPreference() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}

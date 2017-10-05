package com.ua.plamber_android.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtils {

    public PlamberAPI initializePlamberAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PlamberAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PlamberAPI plamberAPI = retrofit.create(PlamberAPI.class);

        return plamberAPI;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}

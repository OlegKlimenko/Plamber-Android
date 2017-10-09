package com.ua.plamber_android.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.ua.plamber_android.api.interfaces.PlamberAPI;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtils {

    private Context context;

    public APIUtils(Context context) {
        this.context = context;
    }

    public PlamberAPI initializePlamberAPI() {
        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PlamberAPI.ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PlamberAPI plamberAPI = retrofit.create(PlamberAPI.class);

        return plamberAPI;
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "Please check yor interner connection", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

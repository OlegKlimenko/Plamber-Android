package com.ua.plamber_android.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import com.ua.plamber_android.BuildConfig;
import com.ua.plamber_android.R;
import com.ua.plamber_android.utils.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
                //.addInterceptor(getLogger())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.END_POINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(PlamberAPI.class);
    }

    public boolean isOnline(View view) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Utils.messageSnack(view, context.getString(R.string.no_internet_connection));
            return false;
        }
    }

    private HttpLoggingInterceptor getLogger() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}

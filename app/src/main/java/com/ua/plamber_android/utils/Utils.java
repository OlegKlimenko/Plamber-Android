package com.ua.plamber_android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ua.plamber_android.R;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    private Context context;
    private static final String datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public Utils(Context context) {
        this.context = context;
    }

    public void initBackgroundImage(ImageView background) {
        Glide.with(context).load(R.drawable.main_background)
                .apply(new RequestOptions().transform(new CenterCrop())).into(background);
    }

    public String getPlamberPath() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            String path = getRootDirectory() + File.separator + "Plamber" + File.separator;
            File plamberDirectory = new File(path);
            if (!plamberDirectory.exists()) {
                plamberDirectory.mkdir();
            }
            return path;
        } else {
            return context.getFilesDir().getPath();
        }
    }

    public File getRootDirectory() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory();
        } else {
            return context.getFilesDir();
        }
    }

    public float getHeightDeviceDP() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels / displayMetrics.density;
    }

    public float getWidthDeviceDP() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels / displayMetrics.density;
    }

    public String getFullFileName(String bookName) {
        return getPlamberPath() + bookName + ".pdf";
    }

    public String getNamePDF(String bookName) {
        return bookName + ".pdf";
    }

    public static void messageSnack(View view, String mes) {
        final Snackbar snackbar = Snackbar.make(view, mes, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static String dateUpload(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat plamberFormat = new SimpleDateFormat("dd.MM.yy", Locale.US);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return plamberFormat.format(newDate);
    }

    public static String getCurrentTime() {
        DateFormat format = new SimpleDateFormat(datePattern, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date());
    }

    public static Date convertStringToDate(String date) {
        DateFormat format = new SimpleDateFormat(datePattern, Locale.US);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static String convertDateToString(Date date) {
        DateFormat format = new SimpleDateFormat(datePattern, Locale.US);
        return format.format(date);
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

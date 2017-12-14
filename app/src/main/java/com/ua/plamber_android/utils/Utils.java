package com.ua.plamber_android.utils;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ua.plamber_android.R;

import java.io.File;

public class Utils {

    private Context context;

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
            String path = Environment.getExternalStorageDirectory() + File.separator + "Plamber" + File.separator;
            File plamberDirectory = new File(path);
            if (!plamberDirectory.exists()) {
                plamberDirectory.mkdir();
            }
            return path;
        } else {
            return context.getFilesDir().getPath();
        }
    }

    public String getRootDirectory() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            return null;
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
}

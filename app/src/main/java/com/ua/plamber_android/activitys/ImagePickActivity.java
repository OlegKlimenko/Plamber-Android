package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;

import com.ua.plamber_android.R;

public class ImagePickActivity extends FilePickActivity {

    public static final String[] IMAGE_FORMAT = {"png", "jpeg", "gif", "bmp", "jpg"};

    @Override
    public String[] searchFileType() {
        return IMAGE_FORMAT;
    }

    @Override
    public int setToolbarTitle() {
        return R.string.select_image_file;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getmMessageNoFile().setText(R.string.no_image_file);
    }

    public static Intent startImagePickActivity(Context context) {
        return new Intent(context, ImagePickActivity.class);
    }
}

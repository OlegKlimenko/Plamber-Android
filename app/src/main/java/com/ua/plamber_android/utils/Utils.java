package com.ua.plamber_android.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ua.plamber_android.R;

public class Utils {


    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public void initBackgroundImage(ImageView background) {
        Glide.with(context).load(R.drawable.main_background)
                .apply(new RequestOptions().transform(new CenterCrop())).into(background);
    }
}

package com.ua.plamber_android.activitys;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ua.plamber_android.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initBackgroundImage(SignUpActivity.this);
    }

    private void initBackgroundImage(Context context) {
        ImageView background = (ImageView) findViewById(R.id.iv_singup_background);
        Glide.with(context).load(R.drawable.main_background).centerCrop().into(background);
    }
}

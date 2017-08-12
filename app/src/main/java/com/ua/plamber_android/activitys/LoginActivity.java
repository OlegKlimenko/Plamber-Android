package com.ua.plamber_android.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ua.plamber_android.R;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginButton;
    private Button mSignButton;
    private EditText mEmailLoginEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = (Button) findViewById(R.id.btn_login);
        mSignButton = (Button) findViewById(R.id.btn_login_signup);
        mEmailLoginEdit = (EditText) findViewById(R.id.et_login_email);

        initBackgroundImage();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailLoginEdit.setError("Please enter email");
            }
        });

        mSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.iv_login_background);
        Glide.with(this).load(R.drawable.main_background).centerCrop().into(background);
    }
}

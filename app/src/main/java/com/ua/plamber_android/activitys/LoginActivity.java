package com.ua.plamber_android.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ua.plamber_android.R;
import com.ua.plamber_android.utils.Validate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_login_email) EditText mEmailLoginEdit;
    @BindView(R.id.et_login_password) EditText mPasswordLoginEdit;
    @BindView(R.id.btn_login) Button mLoginButton;

    private static long timeExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initBackgroundImage();
    }

    @Override
    public void onBackPressed() {
        if (timeExit + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            String mess = getString(R.string.press_once_to_exit);
            Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
        }
        timeExit = System.currentTimeMillis();
    }

    @OnClick(R.id.btn_login)
    public void loginButton() {
        mLoginButton.setEnabled(false);

        Validate valid = new Validate(this);

        if (!valid.userNameValidate(mEmailLoginEdit) | !valid.passwordValidate(mPasswordLoginEdit)) {
            mLoginButton.setEnabled(true);
        }
    }

    @OnClick(R.id.btn_login_signup)
    public void signUpButton() {
        finish();
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.iv_login_background);
        Glide.with(this).load(R.drawable.main_background).centerCrop().into(background);
    }
}

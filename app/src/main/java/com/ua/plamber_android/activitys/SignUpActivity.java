package com.ua.plamber_android.activitys;

import android.content.Context;
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

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.et_sing_up_username) EditText mUserNameSingUpEdit;
    @BindView(R.id.et_sing_up_email) EditText mEmailSingUpEdit;
    @BindView(R.id.et_sing_up_password) EditText mPasswordSingUpEdit;
    @BindView(R.id.et_sing_up_password_again) EditText mPasswordAgainSingUpEdit;
    @BindView(R.id.btn_sing_up_connected) Button mConnectedSingUpButton;

    private static long timeExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        initBackgroundImage(SignUpActivity.this);
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

    @OnClick(R.id.btn_sing_up_connected)
    public void connectedButton() {
        mConnectedSingUpButton.setEnabled(false);
        Validate valid = new Validate(this);
        if (!valid.userNameValidate(mUserNameSingUpEdit) | !valid.emailValidate(mEmailSingUpEdit)
                | !valid.passwordAgainValidate(mPasswordSingUpEdit, mPasswordAgainSingUpEdit)) {
            mConnectedSingUpButton.setEnabled(true);

        }
    }

    @OnClick(R.id.tv_sing_up_back_login)
    public void backToLogin() {
        finish();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void initBackgroundImage(Context context) {
        ImageView background = (ImageView) findViewById(R.id.iv_singup_background);
        Glide.with(context).load(R.drawable.main_background).centerCrop().into(background);
    }
}

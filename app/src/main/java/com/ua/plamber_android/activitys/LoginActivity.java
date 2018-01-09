package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.model.User;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;
import com.ua.plamber_android.utils.Validate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity";


    @BindView(R.id.iv_login_background)
    ImageView mBackgroundLogin;
    @BindView(R.id.login_progress_bar)
    LinearLayout mLoginProgressBar;
    @BindView(R.id.et_login_email)
    EditText mUsername;
    @BindView(R.id.et_login_password)
    EditText mPasswordLoginEdit;
    @BindView(R.id.til_login_email)
    TextInputLayout mTilUsername;
    @BindView(R.id.til_login_password)
    TextInputLayout mTilPasswordLoginEdit;
    @BindView(R.id.parent_login_layout)
    RelativeLayout mParentLayout;

    private static long timeExit;


    PreferenceUtils preferenceUtils;
    APIUtils apiUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Utils utils = new Utils(getApplicationContext());
        preferenceUtils = new PreferenceUtils(getApplicationContext());
        apiUtils = new APIUtils(getApplicationContext());

        utils.initBackgroundImage(mBackgroundLogin);

        if (preferenceUtils.checkPreference(PreferenceUtils.TOKEN)) {
            Intent intent = LibraryActivity.startLibraryActivity(this);
            startActivity(intent);
            finish();
        }
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
        if (apiUtils.isOnline()) {
            Validate valid = new Validate(getApplicationContext());
            if (valid.userNameValidate(mUsername, mTilUsername) & valid.passwordValidate(mPasswordLoginEdit, mTilPasswordLoginEdit)) {
                userLoginInSystem();
            }
        } else {
            Utils.messageSnack(mParentLayout, getString(R.string.no_internet_connection));
        }
    }

    @OnClick(R.id.btn_login_signup)
    public void signUpButton() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_restore_account)
    public void restoreAccountButton() {
        Intent intent = RestoreAccountActivity.startRestoreActivity(getApplicationContext());
        startActivity(intent);
    }

    public static Intent startLoginActivity(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    private void userLoginInSystem() {
        User.UserRequest userRequest =
                new User.UserRequest(mUsername.getText().toString().trim(),
                        mPasswordLoginEdit.getText().toString().trim());

        visibleProgressBar(true);
        Call<User.UserRespond> request = apiUtils.initializePlamberAPI().login(userRequest);
        request.enqueue(new Callback<User.UserRespond>() {
            @Override
            public void onResponse(Call<User.UserRespond> call, Response<User.UserRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        preferenceUtils.writePreference(PreferenceUtils.TOKEN, response.body().getData().getToken());
                        visibleProgressBar(false);
                        Intent intent = LibraryActivity.startLibraryActivity(getApplicationContext());
                        startActivity(intent);
                        finish();
                    } else {
                        Utils.messageSnack(mParentLayout, getString(R.string.password_or_account_is_incorrect));
                        visibleProgressBar(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<User.UserRespond> call, Throwable t) {
                Utils.messageSnack(mParentLayout, "Connection error");
                visibleProgressBar(false);
            }
        });
    }

    private void visibleProgressBar(boolean status) {
        if (status) {
            mLoginProgressBar.setVisibility(LinearLayout.VISIBLE);
        } else {
            mLoginProgressBar.setVisibility(LinearLayout.INVISIBLE);
        }
    }
}


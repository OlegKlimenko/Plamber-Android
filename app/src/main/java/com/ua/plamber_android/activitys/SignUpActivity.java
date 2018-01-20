package com.ua.plamber_android.activitys;

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
import com.ua.plamber_android.interfaces.callbacks.AccountCallback;
import com.ua.plamber_android.model.Account;
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

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.iv_singup_background)
    ImageView backgroundSing;
    @BindView(R.id.singup_progress_bar)
    LinearLayout mSingUpProgressBar;
    @BindView(R.id.et_sing_up_password_again)
    EditText mPasswordAgainSingUpEdit;
    @BindView(R.id.et_sing_up_username)
    EditText mUserNameSingUpEdit;
    @BindView(R.id.et_sing_up_email)
    EditText mEmailSingUpEdit;
    @BindView(R.id.et_sing_up_password)
    EditText mPasswordSingUpEdit;
    @BindView(R.id.til_sing_up_username)
    TextInputLayout mTilUserNameSingUpEdit;
    @BindView(R.id.til_sing_up_email)
    TextInputLayout mTilEmailSingUpEdit;
    @BindView(R.id.til_sing_up_password)
    TextInputLayout mTilPasswordSingUpEdit;
    @BindView(R.id.til_sing_up_password_again)
    TextInputLayout mTilPasswordAgainSingUpEdit;
    @BindView(R.id.sing_up_parent_layout)
    RelativeLayout mParentLayout;

    private final static String TAG = "SignUpActivity";
    APIUtils apiUtils;
    PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        apiUtils = new APIUtils(getApplicationContext());
        preferenceUtils = new PreferenceUtils(getApplicationContext());
        Utils utils = new Utils(getApplicationContext());
        utils.initBackgroundImage(backgroundSing);
    }

    @OnClick(R.id.btn_sing_up_connected)
    public void connectedButton() {
        if (apiUtils.isOnline(mParentLayout) && checkFieldsValid()) {
            visibleProgressBar(true);
            checkUserData();
        }
    }

    private boolean checkFieldsValid() {
        Validate valid = new Validate(getApplicationContext());
        return valid.userNameValidate(mUserNameSingUpEdit, mTilUserNameSingUpEdit) & valid.emailValidate(mEmailSingUpEdit, mTilEmailSingUpEdit)
                & valid.passwordAgainValidate(mPasswordSingUpEdit, mPasswordAgainSingUpEdit, mTilPasswordSingUpEdit, mTilPasswordAgainSingUpEdit);
    }

    private void checkUserData() {
        checkUserName(new AccountCallback() {
            @Override
            public void onSuccess(@NonNull boolean isCreate) {
                if (!isCreate) {
                    checkUserEmail(new AccountCallback() {
                        @Override
                        public void onSuccess(@NonNull boolean isCreate) {
                            if (!isCreate) {
                                registerUser();
                                visibleProgressBar(false);
                            } else {
                                mTilEmailSingUpEdit.setError(getString(R.string.email_already_use));
                                visibleProgressBar(false);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            messageError();
                        }
                    });
                } else {
                    mTilUserNameSingUpEdit.setError(getString(R.string.login_already_use));
                    visibleProgressBar(false);
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                messageError();
            }
        });
    }

    @OnClick(R.id.tv_sing_up_back_login)
    public void backToLogin() {
        finish();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void checkUserName(final AccountCallback callback) {
        if (callback != null) {
            Account.LoginRequest userLogin =
                    new Account.LoginRequest(getString(R.string.app_key), mUserNameSingUpEdit.getText().toString().trim());
            Call<Account.LoginRespond> loginRequest = apiUtils.initializePlamberAPI().checkLogin(userLogin);
            loginRequest.enqueue(new Callback<Account.LoginRespond>() {
                @Override
                public void onResponse(Call<Account.LoginRespond> call, Response<Account.LoginRespond> response) {
                    boolean isCreated = true;
                    if (response.isSuccessful()) {
                        isCreated = response.body().getData().isLoginStatus();
                    }
                    callback.onSuccess(isCreated);
                }

                @Override
                public void onFailure(Call<Account.LoginRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

    private void checkUserEmail(final AccountCallback callback) {
        if (callback != null) {
            Account.EmailRequest userEmail =
                    new Account.EmailRequest(getString(R.string.app_key), mEmailSingUpEdit.getText().toString().trim());
            Call<Account.EmailRespond> emailRequest = apiUtils.initializePlamberAPI().checkEmail(userEmail);
            emailRequest.enqueue(new Callback<Account.EmailRespond>() {
                @Override
                public void onResponse(Call<Account.EmailRespond> call, Response<Account.EmailRespond> response) {
                    boolean isCreate = true;
                    if (response.isSuccessful()) {
                        isCreate = response.body().getData().isEmailStatus();
                    }
                    callback.onSuccess(isCreate);
                }

                @Override
                public void onFailure(Call<Account.EmailRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

    private void registerUser() {
        String name = mUserNameSingUpEdit.getText().toString().trim();
        String email = mEmailSingUpEdit.getText().toString().trim();
        String password = mPasswordAgainSingUpEdit.getText().toString().trim();
        Account.RegisterRequest registerUser =
                new Account.RegisterRequest(getString(R.string.app_key), name, email, password);
        Call<User.UserRespond> request = apiUtils.initializePlamberAPI().registerUser(registerUser);
        request.enqueue(new Callback<User.UserRespond>() {
            @Override
            public void onResponse(Call<User.UserRespond> call, Response<User.UserRespond> response) {
                if (response.isSuccessful()) {
                    preferenceUtils.removePreference();
                    preferenceUtils.writePreference(PreferenceUtils.TOKEN, response.body().getData().getToken());
                    Intent intent = LibraryActivity.startLibraryActivity(getApplicationContext());
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User.UserRespond> call, Throwable t) {
                messageError();
            }
        });
    }

    private void visibleProgressBar(boolean status) {
        if (status) {
            mSingUpProgressBar.setVisibility(LinearLayout.VISIBLE);
        } else {

            mSingUpProgressBar.setVisibility(LinearLayout.INVISIBLE);
        }
        Utils.hideKeyboard(mParentLayout);
    }

    private void messageError() {
        Utils.messageSnack(mParentLayout, getString(R.string.an_error_has_occurred));
        visibleProgressBar(false);
    }

}

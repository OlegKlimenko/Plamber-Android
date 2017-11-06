package com.ua.plamber_android.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.api.interfaces.AccountCallback;
import com.ua.plamber_android.model.Account;
import com.ua.plamber_android.model.User;
import com.ua.plamber_android.utils.TokenUtils;
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

    private final static String TAG = "SignUpActivity";
    APIUtils apiUtils;
    TokenUtils tokenUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        apiUtils = new APIUtils(getApplicationContext());
        tokenUtils = new TokenUtils(getApplicationContext());
        Utils utils = new Utils(getApplicationContext());
        utils.initBackgroundImage(backgroundSing);
    }

    @OnClick(R.id.btn_sing_up_connected)
    public void connectedButton() {

        if (apiUtils.isOnline()) {
            Validate valid = new Validate(getApplicationContext());
            if (valid.userNameValidate(mUserNameSingUpEdit, mTilUserNameSingUpEdit) & valid.emailValidate(mEmailSingUpEdit, mTilEmailSingUpEdit)
                    & valid.passwordAgainValidate(mPasswordSingUpEdit, mPasswordAgainSingUpEdit, mTilPasswordSingUpEdit, mTilPasswordAgainSingUpEdit)) {
                visibleProgressBar(true);
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
                                        mEmailSingUpEdit.setError("Email already use");
                                        visibleProgressBar(false);
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable t) {
                                    errorMessage(t);
                                    visibleProgressBar(false);
                                }
                            });
                        } else {
                            mUserNameSingUpEdit.setError("Login already use");
                            visibleProgressBar(false);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        errorMessage(t);
                    }
                });
            }
        }
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
                    new Account.LoginRequest(mUserNameSingUpEdit.getText().toString().trim());
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
                    new Account.EmailRequest(mEmailSingUpEdit.getText().toString().trim());
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
                new Account.RegisterRequest(name, email, password);
        Call<User.UserRespond> request = apiUtils.initializePlamberAPI().registerUser(registerUser);
        request.enqueue(new Callback<User.UserRespond>() {
            @Override
            public void onResponse(Call<User.UserRespond> call, Response<User.UserRespond> response) {
                if (response.isSuccessful()) {
                    tokenUtils.removeToken();
                    tokenUtils.writeToken(response.body().getData().getToken());
                    Intent intent = LibraryActivity.startLibraryActivity(getApplicationContext());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User.UserRespond> call, Throwable t) {
                errorMessage(t);
            }
        });
    }

    private void visibleProgressBar(boolean status) {
        if (status) {
            mSingUpProgressBar.setVisibility(LinearLayout.VISIBLE);
        } else {
            mSingUpProgressBar.setVisibility(LinearLayout.INVISIBLE);
        }
    }

    private void errorMessage(Throwable t) {
        Toast.makeText(getApplicationContext(),
                t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}

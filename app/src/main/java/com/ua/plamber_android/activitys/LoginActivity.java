package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.model.User;
import com.ua.plamber_android.utils.TokenUtils;
import com.ua.plamber_android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity";
    public final static String TOKEN = "Token";
    public final static String TOKENSTATUS = "TokenStatus";

    @BindView(R.id.et_login_email) EditText mEmailLoginEdit;
    @BindView(R.id.et_login_password) EditText mPasswordLoginEdit;
    @BindView(R.id.btn_login) Button mLoginButton;
    @BindView(R.id.iv_login_background) ImageView backgroundLogin;

    private static long timeExit;

    TokenUtils tokenUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Utils utils = new Utils(getApplicationContext());
        tokenUtils = new TokenUtils(getApplicationContext());

        utils.initBackgroundImage(backgroundLogin);


        if (tokenUtils.checkUserToken()) {
            Intent intent = LibraryActivity.startLibraryActivity(this);
            startActivity(intent);
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

//        ***Disable validate***
//        mLoginButton.setEnabled(false);
//        Validate valid = new Validate(this);
//        if (!valid.userNameValidate(mEmailLoginEdit) | !valid.passwordValidate(mPasswordLoginEdit)) {
//            mLoginButton.setEnabled(true);
//        }
//        Intent intent = LibraryActivity.startLibraryActivity(this);
//        startActivity(intent);


        final APIUtils apiUtils = new APIUtils();

        if (apiUtils.isOnline(getApplicationContext())) {
            User.UserRequest userRequest =
                    new User.UserRequest(mEmailLoginEdit.getText().toString().trim(),
                            mPasswordLoginEdit.getText().toString().trim());
            Call<User.UserRespond> request = apiUtils.initializePlamberAPI().login(userRequest);
            request.enqueue(new Callback<User.UserRespond>() {
                @Override
                public void onResponse(Call<User.UserRespond> call, Response<User.UserRespond> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            tokenUtils.writeToken(response.body().getData().getToken());
                            Intent intent = LibraryActivity.startLibraryActivity(getApplicationContext());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getBaseContext(), "Incorrect!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User.UserRespond> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),
                            t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }





//        final List<Book.BookData> books = new ArrayList<>();
//
//        APIUtils apiUtils = new APIUtils();
//        final Book.BookRequest book = new Book.BookRequest(TOKEN);
//        Call<Book.BookRespond> request = apiUtils.initializePlamberAPI().getUserBook(book);
//        request.enqueue(new Callback<Book.BookRespond>() {
//            @Override
//            public void onResponse(Call<Book.BookRespond> call, Response<Book.BookRespond> response) {
//                Log.i(TAG, "Status " + response.body().getStatus());
//                books.addAll(response.body().getBookData());
//                for (Book.BookData bookData : books) {
//                    Log.i(TAG, bookData.getBookName());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Book.BookRespond> call, Throwable t) {
//
//            }
//        });
//
    }


    @OnClick(R.id.btn_login_signup)
    public void signUpButton() {
        finish();
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public static Intent startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

}


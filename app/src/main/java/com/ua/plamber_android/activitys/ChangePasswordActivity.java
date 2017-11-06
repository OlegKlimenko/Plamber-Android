package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.model.Password;
import com.ua.plamber_android.utils.TokenUtils;
import com.ua.plamber_android.utils.Validate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_change_old_password)
    EditText mChangeOldPassword;
    @BindView(R.id.et_change_new_password)
    EditText mChangeNewPassword;
    @BindView(R.id.et_change_again_password)
    EditText mChangeAgainPassword;

    @BindView(R.id.til_change_old_password)
    TextInputLayout mTilChangeOldPassword;
    @BindView(R.id.til_change_new_password)
    TextInputLayout mTilChangeNewPassword;
    @BindView(R.id.til_change_again_password)
    TextInputLayout mTilChangeAgainPassword;

    TokenUtils tokenUtils;
    APIUtils apiUtils;

    private static final String TAG = "ChangePasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(10);
        getSupportActionBar().setTitle(getResources().getString(R.string.change_password));
        tokenUtils = new TokenUtils(this);
        apiUtils = new APIUtils(this);
    }

    @OnClick(R.id.btn_change_password)
    public void changePassword() {
        Validate validate = new Validate(this);
        if (validate.passwordValidate(mChangeOldPassword, mTilChangeOldPassword) & validate.passwordAgainValidate(mChangeNewPassword, mChangeAgainPassword, mTilChangeNewPassword, mTilChangeAgainPassword) & validate.passwordNoClone(mChangeOldPassword, mChangeNewPassword, mTilChangeOldPassword, mTilChangeNewPassword)) {
            changeCurrentPassword();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeCurrentPassword() {
        String oldPassword = mChangeOldPassword.getText().toString().trim();
        String newPassword = mChangeNewPassword.getText().toString().trim();
        Password.PasswordRequest password = new Password.PasswordRequest(tokenUtils.readToken(), oldPassword, newPassword);
        Call<Password.PasswordRespond> request = apiUtils.initializePlamberAPI().changePassword(password);
        request.enqueue(new Callback<Password.PasswordRespond>() {
            @Override
            public void onResponse(Call<Password.PasswordRespond> call, Response<Password.PasswordRespond> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getDetail().equals("successful")) {
                       mTilChangeOldPassword.setError("Current password is incorrect");
                    } else {
                        Toast.makeText(getApplicationContext(), "Password was change", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onFailure(Call<Password.PasswordRespond> call, Throwable t) {
              Toast.makeText(getApplicationContext(), t.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static Intent startChangePasswordActivity(Context context) {
        return new Intent(context, ChangePasswordActivity.class);
    }
}

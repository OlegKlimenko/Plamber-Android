package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.model.Account;
import com.ua.plamber_android.utils.Utils;
import com.ua.plamber_android.utils.Validate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestoreAccountActivity extends AppCompatActivity {

    @BindView(R.id.restore_progress_bar)
    LinearLayout mRestoreProgressBar;
    @BindView(R.id.et_email_restore)
    EditText mRestoreAccountEdit;
    @BindView(R.id.til_email_restore)
    TextInputLayout mTilRestoreAccountEdit;
    @BindView(R.id.restore_account_parent)
    RelativeLayout mRestoreParentLayout;
    @BindView(R.id.iv_restore_login)
    ImageView mRestoreLogo;

    APIUtils apiUtils;

    private final static String TAG = "RestoreAccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_account);
        ButterKnife.bind(this);
        apiUtils = new APIUtils(getApplicationContext());
        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.main_background));
        Glide.with(getApplicationContext()).load(R.drawable.plamber_logo_mini).into(mRestoreLogo);
    }

    @OnClick(R.id.btn_restore_account)
    public void restoreAccountButton() {
        if (apiUtils.isOnline(mRestoreParentLayout)) {
            Validate validate = new Validate(getApplicationContext());
            if (validate.emailValidate(mRestoreAccountEdit, mTilRestoreAccountEdit)) {
                restoreAccount();
            }
        }
    }

    @OnClick(R.id.tv_restore_back_login)
    public void backToLoginPage() {
        finish();
        Intent intent = LoginActivity.startLoginActivity(getApplicationContext());
        startActivity(intent);
    }

    private void restoreAccount() {
        String email = mRestoreAccountEdit.getText().toString().trim();
        Account.EmailRequest emailRequest = new Account.EmailRequest(getString(R.string.app_key), email);
        visibleProgressBar(true);
        Call<Account.EmailRespond> request = apiUtils.initializePlamberAPI().restoreAccount(emailRequest);
        request.enqueue(new Callback<Account.EmailRespond>() {
            @Override
            public void onResponse(Call<Account.EmailRespond> call, Response<Account.EmailRespond> response) {
                if (response.isSuccessful()) {
                    Utils.messageSnack(mRestoreParentLayout, getString(R.string.instruction_was_send_to_your_email));
                    visibleProgressBar(false);
                } else {
                    Utils.messageSnack(mRestoreParentLayout, getString(R.string.this_email_not_register));
                    visibleProgressBar(false);
                }
            }

            @Override
            public void onFailure(Call<Account.EmailRespond> call, Throwable t) {
                Log.i(TAG, t.getLocalizedMessage());
                Utils.messageSnack(mRestoreParentLayout, getString(R.string.connetion_error_title));
                visibleProgressBar(false);
            }
        });
    }

    private void visibleProgressBar(boolean status) {
        if (status) {
            mRestoreProgressBar.setVisibility(LinearLayout.VISIBLE);
        } else {
            mRestoreProgressBar.setVisibility(LinearLayout.INVISIBLE);
        }
        Utils.hideKeyboard(mRestoreParentLayout);
    }

    public static Intent startRestoreActivity(Context context) {
        return new Intent(context, RestoreAccountActivity.class);
    }
}

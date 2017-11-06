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
import android.widget.Toast;

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

    @BindView(R.id.iv_restore_background)
    ImageView mBackgroundRestore;
    @BindView(R.id.restore_progress_bar)
    LinearLayout mRestoreProgressBar;
    @BindView(R.id.et_email_restore)
    EditText mRestoreAccountEdit;
    @BindView(R.id.til_email_restore)
    TextInputLayout mTilRestoreAccountEdit;

    APIUtils apiUtils;

    private final static String TAG = "RestoreAccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_account);
        ButterKnife.bind(this);
        apiUtils = new APIUtils(getApplicationContext());
        Utils utils = new Utils(getApplicationContext());
        utils.initBackgroundImage(mBackgroundRestore);

    }

    @OnClick(R.id.btn_restore_account)
    public void restoreAccountButton() {
        if (apiUtils.isOnline()) {
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
        Account.EmailRequest emailRequest = new Account.EmailRequest(email);
        visibleProgressBar(true);
        Call<Account.EmailRespond> request = apiUtils.initializePlamberAPI().restoreAccount(emailRequest);
        request.enqueue(new Callback<Account.EmailRespond>() {
            @Override
            public void onResponse(Call<Account.EmailRespond> call, Response<Account.EmailRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        Toast.makeText(getApplicationContext(),
                                "Instruction was send to your email", Toast.LENGTH_LONG).show();
                        visibleProgressBar(false);
                    } else if (response.body().getStatus() == 404) {
                        Toast.makeText(getApplicationContext(),
                                "This email don`t register", Toast.LENGTH_LONG).show();
                        visibleProgressBar(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<Account.EmailRespond> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
    }

    public static Intent startRestoreActivity(Context context) {
        return new Intent(context, RestoreAccountActivity.class);
    }
}

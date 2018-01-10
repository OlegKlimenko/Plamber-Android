package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.SupportActivity;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.interfaces.callbacks.StatusCallback;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;
import com.ua.plamber_android.utils.Validate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SupportFragment extends Fragment {

    PreferenceUtils preferenceUtils;
    WorkAPI workAPI;
    Validate validate;

    @BindView(R.id.support_user_email)
    EditText mEmail;
    @BindView(R.id.support_text)
    EditText mSupportText;
    @BindView(R.id.til_support_user_email)
    TextInputLayout mTilEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtils = new PreferenceUtils(getActivity());
        workAPI = new WorkAPI(getActivity());
        validate= new Validate(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_support, container, false);
        ButterKnife.bind(this, view);
        setUserEmail();
        return view;
    }

    private void setUserEmail() {
        if (preferenceUtils.checkPreference(PreferenceUtils.USER_EMAIL))
            mEmail.setText(preferenceUtils.readPreference(PreferenceUtils.USER_EMAIL));
    }

    @OnClick(R.id.send_support_message)
    public void sendSupportMessage() {
        if (validate.emailValidate(mEmail, mTilEmail)) {
            if (!TextUtils.isEmpty(mSupportText.getText())) {
                workAPI.sendSupportMessage(new StatusCallback() {
                    @Override
                    public void onSuccess(@NonNull int status) {
                        if (status == 200) {
                            Toast.makeText(getActivity(), R.string.thanks_for_your_message, Toast.LENGTH_SHORT).show();
                            getSupportActivity().finish();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {

                    }
                }, mEmail.getText().toString().trim(), mSupportText.getText().toString().trim());
            } else {
                Utils.messageSnack(getView(), getString(R.string.the_support_message_not_empty));
            }
        }
    }

    private SupportActivity getSupportActivity() {
        return ((SupportActivity) getActivity());
    }
}

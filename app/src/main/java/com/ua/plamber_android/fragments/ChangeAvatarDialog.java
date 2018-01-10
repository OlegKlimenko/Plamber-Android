package com.ua.plamber_android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.ImagePickActivity;
import com.ua.plamber_android.activitys.SettingActivity;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeAvatarDialog extends DialogFragment {
    public static final String TAG = "ChangeAvatarDialog";

    PreferenceUtils preferenceUtils;

    @BindView(R.id.current_avatar)
    ImageView mCurrentAvatar;
    @BindView(R.id.current_user_name)
    TextView mCurrentName;
    @BindView(R.id.current_user_email)
    TextView mCurrentEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtils = new PreferenceUtils(getActivity());
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);
        super.onDestroyView();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View v = inflate.inflate(R.layout.change_avatar_dialog, null);
        ButterKnife.bind(this, v);
        loadData(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setNegativeButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                }).setPositiveButton(R.string.change_avatar_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = ImagePickActivity.startImagePickActivity(getActivity());
                getActivity().startActivityForResult(intent, SettingActivity.REQUEST_SELECT_IMAGE);
            }
        });
        return builder.create();
    }

    private void loadData(View v) {
        if (preferenceUtils.checkPreference(PreferenceUtils.USER_PHOTO)) {
            updateAvatar(v);
        } else {
            mCurrentAvatar.setColorFilter(getResources().getColor(R.color.colorAccent));
            Glide.with(v).load(R.drawable.ic_account_circle_black_48dp).into(mCurrentAvatar);
        }

        mCurrentName.setText(preferenceUtils.readPreference(PreferenceUtils.USER_NAME));
        mCurrentEmail.setText(preferenceUtils.readPreference(PreferenceUtils.USER_EMAIL));
    }

    private void updateAvatar(View v) {
        String url = PlamberAPI.ENDPOINT;
        String currentUrl = url.substring(0, url.length() - 1) + preferenceUtils.readPreference(PreferenceUtils.USER_PHOTO);
        Glide.with(v).load(currentUrl).into(mCurrentAvatar);
        mCurrentAvatar.setColorFilter(null);
    }
}

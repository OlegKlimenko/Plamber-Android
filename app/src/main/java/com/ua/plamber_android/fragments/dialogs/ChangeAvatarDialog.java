package com.ua.plamber_android.fragments.dialogs;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.ImagePickActivity;
import com.ua.plamber_android.activitys.SettingActivity;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

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
    @BindView(R.id.change_avatar_dialog_parent)
    LinearLayout mParentLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtils = new PreferenceUtils(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View v = inflate.inflate(R.layout.change_avatar_dialog, null);
        ButterKnife.bind(this, v);
        loadData();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setNegativeButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                }).setPositiveButton(R.string.change_avatar_btn, null);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!preferenceUtils.readLogic(PreferenceUtils.OFFLINE_MODE)) {
                    Intent intent = ImagePickActivity.startImagePickActivity(getActivity());
                    if (getActivity() != null)
                    getActivity().startActivityForResult(intent, SettingActivity.REQUEST_SELECT_IMAGE);
                } else {
                    Utils.messageSnack(mParentLayout, getString(R.string.upload_not_available_in_offline_mode));
                }
            }
        });
    }

    private void loadData() {
        updateAvatar();
        mCurrentName.setText(preferenceUtils.readPreference(PreferenceUtils.USER_NAME));
        mCurrentEmail.setText(preferenceUtils.readPreference(PreferenceUtils.USER_EMAIL));
    }

    public void updateAvatar() {
        String url = PlamberAPI.ENDPOINT;
        String currentUrl = url.substring(0, url.length() - 1) + preferenceUtils.readPreference(PreferenceUtils.USER_PHOTO);
        if (getActivity() != null)
        Glide.with(getActivity()).load(currentUrl).into(mCurrentAvatar);
    }
}

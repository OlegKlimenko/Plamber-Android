package com.ua.plamber_android.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.BaseDrawerActivity;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.model.Upload;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadAvatarDialog extends DialogFragment {
    public static final String UPLOAD_AVATAR = "UPLOAD_AVATAR";
    public static final String TAG = "UploadAvatarDialog";

    PreferenceUtils preferenceUtils;
    APIUtils apiUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtils = new PreferenceUtils(getActivity());
        apiUtils = new APIUtils(getActivity());
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
        View v = inflate.inflate(R.layout.simple_progress_dialog, null);
        File file = new File(getArguments().getString(UPLOAD_AVATAR));
        uploadAvatar(file);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setTitle(R.string.upload_avatar_title)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return builder.create();
    }


    private void uploadAvatar(File file) {
        RequestBody requestToken = createRequest(preferenceUtils.readPreference(PreferenceUtils.TOKEN));
        RequestBody requestAvatar = RequestBody.create(MultipartBody.FORM, file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestAvatar);
        Call<Upload.UploadAvatarRespond> request = apiUtils.initializePlamberAPI().uploadAvatar(requestToken, body);
        request.enqueue(new Callback<Upload.UploadAvatarRespond>() {
            @Override
            public void onResponse(Call<Upload.UploadAvatarRespond> call, Response<Upload.UploadAvatarRespond> response) {
                if (response.isSuccessful()) {
                    preferenceUtils.writePreference(PreferenceUtils.USER_PHOTO, response.body().getData().getAvatarUrl() + "?" + String.valueOf(System.currentTimeMillis()));
                    if (getActivity() instanceof BaseDrawerActivity) {
                        getBaseDrawerActivty().updateAvatar();
                        Utils.messageSnack(getBaseDrawerActivty().getDrawerLayout(), getString(R.string.avatar_uploaded_message));
                    } else {
                        Utils.messageSnack(getActivity().getCurrentFocus(), getString(R.string.avatar_uploaded_message));
                    }
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<Upload.UploadAvatarRespond> call, Throwable t) {
                dismiss();
                Utils.messageSnack(getActivity().getCurrentFocus(), getString(R.string.error_update_avatar));
                Log.i(TAG, t.getLocalizedMessage());
            }
        });
    }

    private RequestBody createRequest(String data) {
        return RequestBody.create(MultipartBody.FORM, data.trim());
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(this, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private BaseDrawerActivity getBaseDrawerActivty() {
        return ((BaseDrawerActivity) getActivity());
    }
}

package com.ua.plamber_android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.model.Upload;
import com.ua.plamber_android.utils.FileUploadProgress;
import com.ua.plamber_android.utils.TokenUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDialogFragment extends DialogFragment {

    @BindView(R.id.tv_loading_file)
    TextView titleLoad;
    @BindView(R.id.pb_dialog_download)
    ProgressBar progressDownload;
    @BindView(R.id.tv_percent_dialog_download)
    TextView percentDownload;
    public static final String UPLOAD_BOOK = "UPLOAD_BOOK";

    private Upload.UploadRequest uploadData;

    Utils utils;
    TokenUtils tokenUtils;
    APIUtils apiUtils;
    Call<Upload.UploadRespond> request;

    private static final String TAG = "UploadDialogFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(getActivity());
        tokenUtils = new TokenUtils(getActivity());
        apiUtils = new APIUtils(getActivity());
        uploadData = new Gson().fromJson(getArguments().getString(UPLOAD_BOOK), Upload.UploadRequest.class);
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
        View v = inflate.inflate(R.layout.download_fragment_dialog, null);
        ButterKnife.bind(this, v);

        if (request == null || request.isCanceled())
        uploadFileToServer();
        titleLoad.setText(R.string.upload_progress);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setTitle("Upload book")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (request != null)
                        request.cancel();
                    }
                });

        return builder.create();
    }

    private void uploadFileToServer() {
        File file = new File(uploadData.getBookPath());

        MultipartBody.Part fileBody = prepareFilePart(file);

        Log.i(TAG, uploadData.getLanguageBook());

        request = apiUtils.initializePlamberAPI().uploadFile(createRequest(uploadData.getUserToken()), createRequest(uploadData.getBookName()), createRequest(uploadData.getAuthorName()), createRequest(uploadData.getCategoryName()), createRequest(uploadData.getAboutBook()), createRequest(uploadData.getLanguageBook()), uploadData.isPrivateBook(), fileBody);

        request.enqueue(new Callback<Upload.UploadRespond>() {
            @Override
            public void onResponse(Call<Upload.UploadRespond> call, Response<Upload.UploadRespond> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    BaseViewBookFragment.isUpdate = true;
                    dismiss();

                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    dismiss();
                    getActivity().finish();
                    Log.i(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<Upload.UploadRespond> call, Throwable t) {
                dismiss();
                Log.i(TAG, t.getLocalizedMessage());
            }
        });
    }

    private RequestBody createRequest(String data) {
        return RequestBody.create(MultipartBody.FORM, data.trim());
    }

    private void setProgress(int progress) {
        progressDownload.setProgress(progress);
        percentDownload.setText(new StringBuilder().append(progress).append("%").toString());
    }

    private MultipartBody.Part prepareFilePart(File file) {
        RequestBody requestFile = new FileUploadProgress(file, new FileUploadProgress.ProgressListener() {
            @Override
            public void onUploadProgress(final int progressInPercent, long totalBytes) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setProgress(progressInPercent);
                        }
                    });
                }

            }
        });

        return MultipartBody.Part.createFormData("book_file", file.getName(), requestFile);
    }

}

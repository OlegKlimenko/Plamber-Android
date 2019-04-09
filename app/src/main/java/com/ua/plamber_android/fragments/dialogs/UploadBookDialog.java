package com.ua.plamber_android.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.fragments.UploadFileFragment;
import com.ua.plamber_android.model.Upload;
import com.ua.plamber_android.utils.FileUploadProgress;
import com.ua.plamber_android.utils.FileHelper;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadBookDialog extends DialogFragment {

    @BindView(R.id.tv_loading_file)
    TextView titleLoad;
    @BindView(R.id.pb_dialog_download)
    ProgressBar progressDownload;
    @BindView(R.id.tv_percent_dialog_download)
    TextView percentDownload;

    @BindView(R.id.linear_progress)
    LinearLayout linearProgress;

    @BindView(R.id.linear_wait)
    LinearLayout linearWait;

    public static final String UPLOAD_BOOK = "UPLOAD_BOOK";
    public static final String TAG = "UploadBookDialog";
    private Upload.UploadBookRequest uploadData;

    Utils utils;
    PreferenceUtils preferenceUtils;
    APIUtils apiUtils;
    UploadFile uploadFile;
    Call<Upload.UploadBookRespond> request;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(getActivity());
        preferenceUtils = new PreferenceUtils(getActivity());
        apiUtils = new APIUtils(getActivity());
        uploadData = new Gson().fromJson(getArguments().getString(UPLOAD_BOOK), Upload.UploadBookRequest.class);
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
        View v = inflate.inflate(R.layout.progress_fragment_dialog, null);
        ButterKnife.bind(this, v);
        File file = new File(uploadData.getBookPath());
        if (uploadFile == null || uploadFile.isCancelled()) {
            uploadFile = new UploadFile();
            uploadFile.execute(file);
        }
        titleLoad.setText(R.string.upload_progress);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setTitle(R.string.upload_book_title)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (uploadFile != null && request != null) {
                            uploadFile.cancel(true);
                            request.cancel();
                            Utils.messageSnack(getActivity().getCurrentFocus(), getString(R.string.upload_was_interrupted));
                        }

                    }
                });

        return builder.create();
    }

    private void uploadFileToServer(final File file, MultipartBody.Part photo) {

        MultipartBody.Part fileBody = prepareFilePart(file);
        request = apiUtils.initializePlamberAPI().uploadFile(createRequest(getString(R.string.app_key)),
                createRequest(uploadData.getUserToken()), createRequest(uploadData.getBookName()),
                createRequest(uploadData.getAuthorName()), createRequest(uploadData.getCategoryName()),
                createRequest(uploadData.getAboutBook()),
                createRequest(uploadData.getLanguageBook()), uploadData.isPrivateBook(), fileBody, photo);

        request.enqueue(new Callback<Upload.UploadBookRespond>() {
            @Override
            public void onResponse(Call<Upload.UploadBookRespond> call, Response<Upload.UploadBookRespond> response) {
                if (response.isSuccessful()) {
                    getUploadFileFragamnt().updateOfflineBook(response.body().getData().getBookData());
                    Toast.makeText(getActivity(), R.string.book_uploaded_success, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    dismiss();

                } else {
                    dismiss();
                    Toast.makeText(getActivity(), R.string.error_has_occurred, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    Log.i(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<Upload.UploadBookRespond> call, Throwable t) {
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
                    getActivity().runOnUiThread(() -> setProgress(progressInPercent));
                }

            }
        });
        return MultipartBody.Part.createFormData("book_file", Utils.getTimeMillis() + FileHelper.getFileType(file), requestFile);
    }


    private class UploadFile extends AsyncTask<File, Void, MultipartBody.Part> {
        private File file;

        @Override
        protected MultipartBody.Part doInBackground(File... files) {
            file = files[0];
            RequestBody requestCover = RequestBody.create(MultipartBody.FORM, utils.getFirstPageAsByte(files[0]));
            return MultipartBody.Part.createFormData("photo", Utils.getTimeMillis() + ".png", requestCover);
        }

        @Override
        protected void onPreExecute() {
            visibilityProgress(false);
        }

        @Override
        protected void onPostExecute(MultipartBody.Part part) {
            visibilityProgress(true);
            uploadFileToServer(file, part);
        }
    }

    private void visibilityProgress(boolean status) {
        if (status) {
            linearProgress.setVisibility(View.VISIBLE);
            linearWait.setVisibility(View.GONE);
        } else {
            linearProgress.setVisibility(View.GONE);
            linearWait.setVisibility(View.VISIBLE);
        }
    }
    private UploadFileFragment getUploadFileFragamnt() {
        return ((UploadFileFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container));
    }
}

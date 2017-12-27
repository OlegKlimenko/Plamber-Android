package com.ua.plamber_android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.model.Upload;
import com.ua.plamber_android.utils.FileUploadProgress;
import com.ua.plamber_android.utils.FileUtils;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

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

    @BindView(R.id.linear_progress)
    LinearLayout linearProgress;

    @BindView(R.id.linear_wait)
    LinearLayout linearWait;

    public static final String UPLOAD_BOOK = "UPLOAD_BOOK";

    private Upload.UploadRequest uploadData;

    Utils utils;
    PreferenceUtils preferenceUtils;
    APIUtils apiUtils;
    UploadFile uploadFile;

    private static final String TAG = "UploadDialogFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(getActivity());
        preferenceUtils = new PreferenceUtils(getActivity());
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
                .setTitle("Upload book")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (uploadFile != null)
                            uploadFile.cancel(true);
                    }
                });

        return builder.create();
    }

    private void uploadFileToServer(final File file, MultipartBody.Part photo) {

        MultipartBody.Part fileBody = prepareFilePart(file);
        Call<Upload.UploadRespond> request = apiUtils.initializePlamberAPI().uploadFile(createRequest(uploadData.getUserToken()), createRequest(uploadData.getBookName()), createRequest(uploadData.getAuthorName()), createRequest(uploadData.getCategoryName()), createRequest(uploadData.getAboutBook()), createRequest(uploadData.getLanguageBook()), uploadData.isPrivateBook(), fileBody, photo);

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

    private byte[] getFirstPageAsByte(File pdfFileUrl) {
        PdfiumCore pdfiumCore = null;
        PdfDocument pdfDocument = null;
        try {
            ParcelFileDescriptor fd = ParcelFileDescriptor.open(pdfFileUrl, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfiumCore = new PdfiumCore(getActivity());
            pdfDocument = pdfiumCore.newDocument(fd);
        } catch (IOException e) {
            Log.i(TAG, e.getLocalizedMessage());
        }
        pdfiumCore.openPage(pdfDocument, 0);

        int width = pdfiumCore.getPageWidthPoint(pdfDocument, 0);
        int height = pdfiumCore.getPageHeightPoint(pdfDocument, 0);

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        pdfiumCore.renderPageBitmap(pdfDocument, bitmap, 0, 0, 0,
                width, height);
        pdfiumCore.closeDocument(pdfDocument);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private class UploadFile extends AsyncTask<File, Void, MultipartBody.Part> {
        File file;

        @Override
        protected MultipartBody.Part doInBackground(File... files) {
            file = files[0];
            RequestBody requestCover = RequestBody.create(MultipartBody.FORM, getFirstPageAsByte(files[0]));
            return MultipartBody.Part.createFormData("photo", FileUtils.removeSymbol(files[0].getName(), 4) + ".png", requestCover);
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

}

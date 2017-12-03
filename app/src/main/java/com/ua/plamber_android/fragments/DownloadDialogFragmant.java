package com.ua.plamber_android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.DetailBookActivity;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadDialogFragmant extends DialogFragment {

    public static final String DOWNLOADBOOK = "DOWNLOADBOOK";
    private APIUtils apiUtils;
    private AsyncDownloadBook asyncDownload;

    ProgressBar progressDownload;
    TextView percentDownload;

    Book.BookData bookData;
    private Utils utils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookData = new Gson().fromJson(getArguments().getString(DOWNLOADBOOK), Book.BookData.class);
        apiUtils = new APIUtils(getActivity());
        utils = new Utils(getActivity());
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
        progressDownload = v.findViewById(R.id.pb_dialog_download);
        percentDownload = v.findViewById(R.id.tv_percent_dialog_download);

        final File file = new File(utils.getBooksPath() + Utils.getFileName(bookData));
        if (asyncDownload == null || asyncDownload.getStatus() != AsyncTask.Status.RUNNING)
        downloadBook(file);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setTitle("Download book")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (asyncDownload != null) {
                            asyncDownload.cancel(true);
                        }
                        file.delete();
                        Toast.makeText(getActivity(), "Download was interrupted", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }

    private void downloadBook(final File file) {
        final Call<ResponseBody> request = apiUtils.initializePlamberAPI().downloadFile(bookData.getBookFile());
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                   asyncDownload = new AsyncDownloadBook(file);
                   asyncDownload.execute(response);
                } else {
                    Log.d("BookReaderActivity", "Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setProgress(int progress) {
        progressDownload.setProgress(progress);
        percentDownload.setText(new StringBuilder().append(progress).append("%").toString());
    }

    private class AsyncDownloadBook extends AsyncTask<Response<ResponseBody>, Integer, Boolean> {

        private File file;

        public AsyncDownloadBook(File file) {
            this.file = file;
        }

        @SafeVarargs
        @Override
        protected final Boolean doInBackground(Response<ResponseBody>... response) {
            boolean checkFile = false;
            try {
                float fileSize = response[0].body().contentLength();
                FileOutputStream stream = new FileOutputStream(file);
                BufferedInputStream inputStream = new BufferedInputStream(response[0].body().byteStream());
                byte[] data = new byte[8192];
                float total = 0;
                int readBytes;

                while ((readBytes = inputStream.read(data)) != -1) {
                    total += readBytes;
                    stream.write(data, 0, readBytes);
                    publishProgress((int) ((total / fileSize) * 100));
                }
                if (fileSize == total) {
                    checkFile = true;
                }
            } catch (IOException e) {
                Log.i("BookReaderActivity", e.getLocalizedMessage());
            }
            return checkFile;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean status) {
            dismiss();
            if (status) {
                Toast.makeText(getActivity(), "Download complete", Toast.LENGTH_SHORT).show();
                ((DetailBookActivity)getActivity()).checkBook();
            } else {
                file.delete();
                Toast.makeText(getActivity(), "Download error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

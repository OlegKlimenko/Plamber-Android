package com.ua.plamber_android.fragments.dialogs;

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
import com.ua.plamber_android.fragments.DetailBookFragment;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadDialogFragmant extends DialogFragment {

    public static final String DOWNLOADBOOK = "DOWNLOADBOOK";
    private APIUtils apiUtils;
    private AsyncDownloadBook asyncDownload;

    @BindView(R.id.pb_dialog_download)
    ProgressBar progressDownload;
    @BindView(R.id.tv_percent_dialog_download)
    TextView percentDownload;

    Book.BookData bookData;
    private String bookId;
    private Utils utils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookData = new Gson().fromJson(getArguments().getString(DOWNLOADBOOK), Book.BookData.class);
        apiUtils = new APIUtils(getActivity());
        utils = new Utils(getActivity());
        bookId = Utils.generateIdBook();
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

        final File file = new File(utils.getPdfFileWithPath(bookId));
        if (asyncDownload == null || asyncDownload.isCancelled())
        downloadBook(file);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setTitle(R.string.download_book_title)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (asyncDownload != null) {
                            asyncDownload.cancel(true);
                        }
                        file.delete();
                        Utils.messageSnack(getDetailBookFragment().getView(), getString(R.string.download_was_interrupted));
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
                    Log.d("BookReaderActivity", "Download error");
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
            try {
                return writeResponse(file, response[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean status) {
            dismiss();
            if (status) {
                getDetailBookFragment().writeBookToDB(bookId);
                getDetailBookFragment().startReadBook();
                //Utils.messageSnack(getDetailBookFragment().getView(), getString(R.string.download_complete_message));
            } else {
                file.delete();
                Utils.messageSnack(getDetailBookFragment().getView(), getString(R.string.download_error_message));
            }
        }

        private boolean writeResponse(File file, Response<ResponseBody> response) throws IOException {
            boolean checkFile = false;
            FileOutputStream stream = null;
            BufferedInputStream inputStream = null;
            try {
                float fileSize = response.body().contentLength();
                stream = new FileOutputStream(file);
                inputStream = new BufferedInputStream(response.body().byteStream());
                byte[] data = new byte[8192];
                float total = 0;
                int readBytes;
                int progress = 0;

                while ((readBytes = inputStream.read(data)) != -1) {
                    int download = (int)((total / fileSize) * 100);
                    if (progress + 1 <= download) {
                        progress = download;
                        publishProgress(download);
                    }
                    total += readBytes;
                    stream.write(data, 0, readBytes);

                }
                if (fileSize == total) {
                    checkFile = true;
                }
            } finally {
                if (stream != null)
                    stream.close();

                if (inputStream != null)
                    inputStream.close();
            }
            return checkFile;
        }
    }

    private DetailBookFragment getDetailBookFragment() {
        return ((DetailBookFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container));
    }

}
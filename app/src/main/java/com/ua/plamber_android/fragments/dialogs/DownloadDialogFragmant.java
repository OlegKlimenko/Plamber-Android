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
    public static final String TAG = "DownloadDialogFragmant";

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
        bookId = Utils.generateId();
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
        File bookFile = new File(utils.getPdfFileWithPath(bookId));
        File bookCover = new File(utils.getPngFileWithPath(bookId));
        if (asyncDownload == null || asyncDownload.isCancelled()) {
            downloadBook(bookFile);
            downloadCover(bookCover);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setTitle(R.string.download_book_title)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (asyncDownload != null) {
                            asyncDownload.cancel(true);
                        }
                        bookFile.delete();
                        Utils.messageSnack(getDetailBookFragment().getView(), getString(R.string.download_was_interrupted));
                    }
                });
        return builder.create();
    }

    private void downloadBook(final File file) {
        final Call<ResponseBody> request = apiUtils.initializePlamberAPI().downloadBigFile(bookData.getBookFile());
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                   asyncDownload = new AsyncDownloadBook(file);
                   asyncDownload.execute(response);
                } else {
                    Log.d(TAG, "Download error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadCover(File file) {
        Call<ResponseBody> request = apiUtils.initializePlamberAPI().downloadFile(bookData.getPhoto());
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful())
                    return;
                saveBookCover(file,response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "Download error");
            }
        });
    }


    private void setProgress(int progress) {
        progressDownload.setProgress(progress);
        percentDownload.setText(String.valueOf(progress) + "%");
    }

    private class AsyncDownloadBook extends AsyncTask<Response<ResponseBody>, Integer, Boolean> {

        private File file;

        private AsyncDownloadBook(File file) {
            this.file = file;
        }

        @SafeVarargs
        @Override
        protected final Boolean doInBackground(Response<ResponseBody>... response) {
            return writeResponse(file, response[0]);
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
            } else {
                file.delete();
                Utils.messageSnack(getDetailBookFragment().getView(), getString(R.string.download_error_message));
            }
        }

        private boolean writeResponse(File file, Response<ResponseBody> response) {
            try (FileOutputStream stream = new FileOutputStream(file)) {
                try (BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream())) {
                    float fileSize = response.body().contentLength();
                    byte[] data = new byte[8192];
                    float total = 0;
                    int readBytes;
                    int progress = 0;

                    while ((readBytes = inputStream.read(data)) != -1) {
                        int download = (int) ((total / fileSize) * 100);
                        if (progress + 1 <= download) {
                            progress = download;
                            publishProgress(download);
                        }
                        total += readBytes;
                        stream.write(data, 0, readBytes);
                    }
                }
            } catch (IOException e) {
                Log.i(TAG, e.getLocalizedMessage());
            }
            return file.exists();
        }
    }

    private DetailBookFragment getDetailBookFragment() {
        return ((DetailBookFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container));
    }

    private void saveBookCover(File file, Response<ResponseBody> response) {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            try (BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream())) {
                byte[] data = new byte[8192];
                int readBytes;
                while ((readBytes = inputStream.read(data)) != -1) {
                    stream.write(data, 0, readBytes);
                }
            }
        } catch (IOException e) {
            Log.i(TAG, e.getLocalizedMessage());
        }
    }

}

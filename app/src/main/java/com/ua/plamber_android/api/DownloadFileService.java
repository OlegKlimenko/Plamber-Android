package com.ua.plamber_android.api;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.ua.plamber_android.R;

import com.google.gson.Gson;
import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class DownloadFileService extends IntentService {
    public static final String TAG = DownloadFileService.class.getSimpleName();
    public static final String BOOK_DATA = "BOOK_DATA";
    public static final String DOWNLOAD_PROGRESS_FILE = "DOWNLOAD_PROGRESS_FILE";

    private Book.BookDetailData bookData;
    private String bookId;
    private APIUtils apiUtils;
    private BookUtilsDB bookUtilsDB;

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public DownloadFileService() {
        super("Download book start");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null)
            return;
        bookData = new Gson().fromJson(intent.getStringExtra(BOOK_DATA), Book.BookDetailData.class);
        apiUtils = new APIUtils();
        bookId = Utils.generateId();
        bookUtilsDB = new BookUtilsDB(getApplicationContext());

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this, "Download")
                .setSmallIcon(R.drawable.pdf_book)
                .setContentText("Downloading File")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        try {
            downloadCover();
            downloadBook();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bookUtilsDB.writeBookToDataBase(bookId, bookData.getBookData());
    }

    private void downloadCover() throws IOException {
        Call<ResponseBody> request = apiUtils.initializePlamberAPI().downloadFile(bookData.getBookData().getPhoto());
        saveBookCover(new File(new Utils(getApplicationContext()).getPngFileWithPath(bookId)), request.execute());
        Log.i(TAG, "Save book cover");
    }

    private void downloadBook() throws IOException {
        Call<ResponseBody> request = apiUtils.initializePlamberAPI().downloadBigFile(bookData.getBookData().getBookFile());
        saveBookFile(new File(new Utils(getApplicationContext()).getPdfFileWithPath(bookId)), request.execute());
        Log.i(TAG, "Save book file");
    }

    private void saveBookFile(File file, Response<ResponseBody> response) {
        if (!response.isSuccessful())
            return;
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
                        //publishProgress(download);
                        sendNotification(progress, total / 1000000, fileSize / 1000000);
                    }
                    total += readBytes;
                    stream.write(data, 0, readBytes);
                }
            }
        } catch (IOException e) {
            Log.i(TAG, e.getLocalizedMessage());
        }
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

    private void sendNotification(int progress, float currentSize, float totalSize) {

        //sendIntent(download);
        notificationBuilder.setProgress(100, progress, false);
        notificationBuilder.setContentText("Downloading file " + currentSize + "/" + totalSize + " MB");
        notificationBuilder.setContentTitle("Downloading file " + progress);
        notificationManager.notify(0, notificationBuilder.build());
    }
}

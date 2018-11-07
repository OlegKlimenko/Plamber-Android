package com.ua.plamber_android.api.download;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.download.interfaces.FastDownloadListener;
import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.model.Book;

import java.util.List;

public class DownloadService extends IntentService {
    public static final String TAG = DownloadService.class.getSimpleName();
    public static final String FILE_DATA = "FILE_DATA";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public DownloadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "Download")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Downloading File")
                .setAutoCancel(false)
                .setOngoing(true);

        FastDownload fast = new FastDownload();
        fast.setFastListener(new FastDownloadListener() {
            @Override
            public void startDownload() {

            }

            @Override
            public void progressUpdate(List<FastFileData> filesInQueue, int totalSize, int percent, float currentSize, FastFileData currentFile) {
                if (filesInQueue.get(0).equals(currentFile))
                    sendNotification(percent, filesInQueue.size(), currentFile);
            }

            @Override
            public void finishDownload(FastFileData file, int count) {
                BookUtilsDB db = new BookUtilsDB(getApplicationContext());
                db.writeBookToDataBase(file.getId(), file.getBookData().getBookData());
                if (count == 0)
                    notificationManager.cancelAll();
            }
        });
        Log.i(TAG, (new Gson().fromJson(intent.getStringExtra(FILE_DATA), FastFileData.class).getBookData().getBookData().getBookFile()));
        fast.addFileForDownload(new Gson().fromJson(intent.getStringExtra(FILE_DATA), FastFileData.class));
    }

    public void sendNotification(int progress, int fileCount, FastFileData data) {
        notificationBuilder.setProgress(100, progress, false);
        notificationBuilder.setContentText("Downloading file " + progress + " %");
        if (fileCount > 1)
            notificationBuilder.setContentTitle(fileCount + " is now downloading");
        else
            notificationBuilder.setContentTitle(data.getFileName());
        notificationManager.notify(0, notificationBuilder.build());
    }
}

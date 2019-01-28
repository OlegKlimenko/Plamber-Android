package com.ua.plamber_android.api.download;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.download.interfaces.FastDownloadListener;
import com.ua.plamber_android.api.download.type.FastCalculateHelper;
import com.ua.plamber_android.api.download.type.FastTimeLeft;
import com.ua.plamber_android.api.download.type.enums.FastTimeEnum;
import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.fragments.DetailBookFragment;

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
        FileCreateHelper file = new Gson().fromJson(intent.getStringExtra(FILE_DATA), FileCreateHelper.class);
        new FastDownload().addFileToDownload(file, new FastDownloadListener() {
            @Override
            public void startDownload() {
                sendIndeterminateNotification(file.getFileName());
            }

            @Override
            public void progressUpdate(List filesInQueue, FastDownloadedFile file) {
                if (filesInQueue.get(0).equals(file.getFile())) {
                    sendNotification(file.getPersent(), filesInQueue.size(), file);
                }
            }

            @Override
            public void finishDownload(FileCreateHelper file, int filesInQueue) {
                BookUtilsDB utilsDB = new BookUtilsDB(getApplicationContext());
                utilsDB.writeBookToDataBase(file.getDetailData().getBookData().getIdBook(), file.getDetailData().getBookData());
                Intent intent = new Intent(DetailBookFragment.STOP_DOWNLOAD);
                intent.putExtra(DetailBookFragment.SERVER_BOOK_ID, file.getDetailData().getBookData().getIdServerBook());
                sendBroadcast(intent);
                if (filesInQueue == 0)
                    notificationManager.cancelAll();
            }
        });
    }

    public void sendNotification(int progress, int fileCount, FastDownloadedFile data) {
        notificationBuilder.setProgress(100, progress, false);
        FastTimeLeft helper = FastCalculateHelper.getLeftTime(data.getFileSize(), data.getCurrentSize(), data.getStartTime());
        notificationBuilder.setContentText(getString(R.string.time_left) + " " + helper.getLeftTime() + " " + convertType(helper.getType()));
        notificationBuilder.setContentInfo(progress + " %");
        if (fileCount > 1)
            notificationBuilder.setContentTitle(fileCount + getString(R.string.now_downloading));
        else
            notificationBuilder.setContentTitle(data.getFile().getFileName());
        notificationManager.notify(0, notificationBuilder.build());
    }

    public void sendIndeterminateNotification(String name) {
        notificationBuilder.setProgress(100,0, true);
        notificationBuilder.setContentTitle(name);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private String convertType(FastTimeEnum type) {
        if (type.compareTo(FastTimeEnum.SECONDS) == 0) {
            return getString(R.string.seconds);
        } else if (type.compareTo(FastTimeEnum.MINUTES) == 0) {
            return getString(R.string.minutes);
        } else {
            return getString(R.string.hours);
        }
    }
}

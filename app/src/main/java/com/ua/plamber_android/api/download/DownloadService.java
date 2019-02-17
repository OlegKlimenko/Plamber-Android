package com.ua.plamber_android.api.download;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

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
    private static final String CHANNEL_ID = "12";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public static final String CANCEL_DOWNLOAD = "com.ua.plamber_android.api.download.cancel.download";
    public static final String CANCEL_DOWNLOAD_STATUS = "CANCEL_DOWNLOAD_STATUS";

    private FastDownload fastDownload;

    public DownloadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initNotificationChannel();
        iniBroadcast();
        Intent i = new Intent(CANCEL_DOWNLOAD);
        i.putExtra(CANCEL_DOWNLOAD_STATUS, 0);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Downloading File")
                .addAction(R.drawable.baseline_close_24, getString(R.string.cancel), pi)
                .setAutoCancel(false)
                .setOngoing(true);
        FileCreateHelper file = new Gson().fromJson(intent.getStringExtra(FILE_DATA), FileCreateHelper.class);
        fastDownload = new FastDownload();
        fastDownload.addFileToDownload(file, new FastDownloadListener() {
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

            @Override
            public void errorDownloading() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> Toast.makeText(getApplicationContext(), getString(R.string.connetion_error_title), Toast.LENGTH_SHORT).show());
                cancelDownload();
            }
        });
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Download",
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void iniBroadcast() {
        BroadcastReceiver stopBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null)
                    return;
                if (intent.getIntExtra(CANCEL_DOWNLOAD_STATUS, 1) == 0)
                    cancelDownload();

            }
        };
        IntentFilter intentFilter = new IntentFilter(CANCEL_DOWNLOAD);
        getApplicationContext().registerReceiver(stopBroadcast, intentFilter);
    }

    private void cancelDownload() {
        fastDownload.stopDownload();
        notificationManager.cancelAll();
        Intent i = new Intent(CANCEL_DOWNLOAD);
        sendBroadcast(i);
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

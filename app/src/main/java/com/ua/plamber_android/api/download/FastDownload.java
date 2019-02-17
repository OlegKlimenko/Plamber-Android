package com.ua.plamber_android.api.download;

import android.os.AsyncTask;
import android.util.Log;

import com.ua.plamber_android.api.download.interfaces.FastDownloadListener;

import java.io.File;
import java.util.List;

public class FastDownload {
    private static final String TAG = FastDownload.class.getSimpleName();

    private FastDownloadFile request;

    public void addFileToDownload(FileCreateHelper fileData, FastDownloadListener listener) {
        if (request == null)
            request = new FastDownloadFile(listener);
        FileCreateHelper file = new FileCreateHelper(fileData);
        request.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, file);
    }

    public void stopDownload() {
        FastDownloadFile.getPoolFiles().clear();
        request.cancel(true);
    }
}

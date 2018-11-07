package com.ua.plamber_android.api.download;

import android.os.AsyncTask;
import android.util.Log;

import com.ua.plamber_android.api.download.interfaces.FastDownloadListener;

import java.io.File;
import java.util.List;

public class FastDownload {
    private static final String TAG = FastDownload.class.getSimpleName();

    private FastDownloadListener fastListener;

    public void addFileForDownload(File saveAs, String url, String fileName) {
        if (fastListener == null)
            fastListener = getDefaultListener();
        FastDownloadFile request = new FastDownloadFile(fastListener);
        FastFileData file = new FastFileData(saveAs, url, fileName);
        request.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, file);
    }

    public void addFileForDownload(FastFileData fileData) {
        if (fastListener == null)
            fastListener = getDefaultListener();
        FastDownloadFile request = new FastDownloadFile(fastListener);
        FastFileData file = new FastFileData(fileData.getFile(), fileData.getFileURL(), fileData.getFileName());
        file.setBookData(fileData.getBookData());
        file.setId(fileData.getId());
        request.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, file);
    }

    private FastDownloadListener getDefaultListener() {
        return new FastDownloadListener() {
            @Override
            public void startDownload() {
                Log.i(TAG, "startDownload: ");
            }

            @Override
            public void progressUpdate(List<FastFileData> queueFiles, int totalSize, int percent, float currentSize, FastFileData currentFile) {
                Log.i(TAG, "progressUpdate: " + "queueFiles " + queueFiles.size() + "totalSize " + totalSize);
            }

            @Override
            public void finishDownload(FastFileData file, int count) {
                Log.i(TAG, "finishDownload: ");
            }
        };
    }

    public void setFastListener(FastDownloadListener fastListener) {
        this.fastListener = fastListener;
    }
}

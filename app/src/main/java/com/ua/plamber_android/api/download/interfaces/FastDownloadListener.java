package com.ua.plamber_android.api.download.interfaces;

import com.ua.plamber_android.api.download.FastDownloadedFile;
import com.ua.plamber_android.api.download.FileCreateHelper;

import java.util.List;

public interface FastDownloadListener {
    void startDownload();

    void progressUpdate(List<FileCreateHelper> filesInQueue, FastDownloadedFile file);

    void finishDownload(FileCreateHelper file, int filesInQueue);
}

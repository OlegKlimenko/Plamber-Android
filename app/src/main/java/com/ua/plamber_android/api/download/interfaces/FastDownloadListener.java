package com.ua.plamber_android.api.download.interfaces;

import com.ua.plamber_android.api.download.FastFileData;

import java.io.File;
import java.util.List;

public interface FastDownloadListener {
    void startDownload();

    void progressUpdate(List<FastFileData> filesInQueue, int totalSize, int percent, float currentSize, FastFileData currentFile);

    void finishDownload(FastFileData file, int filesInQueue);
}

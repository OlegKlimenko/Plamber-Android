package com.ua.plamber_android.utils;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class FileUploadProgress extends RequestBody {

    private File file;
    private ProgressListener listener;

    public FileUploadProgress(File file, ProgressListener listener) {
        this.file = file;
        this.listener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MultipartBody.FORM;
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long totalBytes = file.length();
        FileInputStream in = new FileInputStream(file);
        try {
            byte[] buffer = new byte[4096];
            long uploadedBytes = 0;
            int readBytes;
            int progress = 0;

            while ((readBytes = in.read(buffer)) != -1) {
                int upload = (int) ((uploadedBytes * 100) / totalBytes);
                if (progress + 1 <= upload) {
                    progress = upload;
                    listener.onUploadProgress(upload, totalBytes);
                }

                uploadedBytes += readBytes;
                sink.write(buffer, 0, readBytes);
            }
        } finally {
            in.close();
        }

        listener.onUploadProgress(100, totalBytes);
    }

    public interface ProgressListener {
        void onUploadProgress(int progressInPercent, long totalBytes);
    }
}

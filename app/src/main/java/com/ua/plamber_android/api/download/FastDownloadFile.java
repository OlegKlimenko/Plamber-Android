package com.ua.plamber_android.api.download;

import android.os.AsyncTask;
import android.util.Log;

import com.ua.plamber_android.api.download.interfaces.FastDownloadListener;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FastDownloadFile extends AsyncTask<FastFileData, Void, FastFileData> {
    private static final String TAG = FastDownloadFile.class.getSimpleName();
    private FastDownloadListener listener;
    private static List<FastFileData> poolFiles = new ArrayList<>();

    public FastDownloadFile(FastDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.startDownload();
    }

    @Override
    protected FastFileData doInBackground(FastFileData... fastFileData) {
        return saveFile(fastFileData[0]);
    }

    @Override
    protected void onPostExecute(FastFileData file) {
        super.onPostExecute(file);
        poolFiles.remove(file);
        listener.finishDownload(file, poolFiles.size());
    }

    private FastFileData saveFile(FastFileData file) {
        poolFiles.add(file);
        try (FileOutputStream stream = new FileOutputStream(file.getFile())) {
            URL url = new URL(file.getFileURL());
            URLConnection connection = url.openConnection();
            int fileSize = connection.getContentLength();
            try (BufferedInputStream inputStream = new BufferedInputStream(url.openStream())) {
                byte[] data = new byte[8192];
                float total = 0;
                int readBytes;
                int progress = 0;
                while ((readBytes = inputStream.read(data)) != -1) {
                    int download = (int) ((total / fileSize) * 100);
                    if (progress + 1 <= download) {
                        progress = download;
                        listener.progressUpdate(poolFiles, fileSize, download, total, file);
                    }
                    total += readBytes;
                    stream.write(data, 0, readBytes);
                }
            }
        } catch (MalformedURLException e) {
            Log.i(TAG, e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
        }
        return file;
    }
}

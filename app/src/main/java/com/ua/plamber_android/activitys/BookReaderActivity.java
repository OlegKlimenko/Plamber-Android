package com.ua.plamber_android.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.api.interfaces.PlamberAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.jar.Manifest;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class BookReaderActivity extends AppCompatActivity {

    @BindView(R.id.pdf_view)
    PDFView mPdfView;

    String currentUrl;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);
        ButterKnife.bind(this);

        String url = PlamberAPI.ENDPOINT;
        final String pdfUrl = getIntent().getStringExtra(DetailBookActivity.PDFURL);
        currentUrl = url.substring(0, url.length() - 1) + pdfUrl;
        APIUtils apiUtils = new APIUtils(this);
        checkPermissions();
        File file = new File(Environment.getExternalStorageDirectory()
                + "/Plamber/1.pdf");
        if (file.exists()) {
            Log.i("BookReaderActivity",file.getAbsolutePath());
            mPdfView.fromFile(file).load();;
        }



//        Call<ResponseBody> request = apiUtils.initializePlamberAPI().downloadFileWithDynamicUrlAsync(currentUrl);
//        request.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    new AsyncTask<Void, Void, Void>() {
//                        @Override
//                        protected Void doInBackground(Void... voids) {
//                            try {
//                            File file = new File(getFilesDir(), "book1.pdf");
//                                FileOutputStream stream = new FileOutputStream(file);
//                                stream.write(response.body().bytes());
//                                stream.close();
//                                Log.d("BookReaderActivity", "Finish");
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            return null;
//                        }
//                    }.execute();
//                } else {
//                    Log.d("BookReaderActivity", "Error");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            }
            }
    }

    public static Intent startBookActivity(Context context) {
        Intent intent = new Intent(context, BookReaderActivity.class);
        return intent;
    }
}

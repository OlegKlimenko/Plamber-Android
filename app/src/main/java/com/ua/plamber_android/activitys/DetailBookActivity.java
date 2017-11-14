package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.adapters.RecyclerBookAdapter;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.api.interfaces.PlamberAPI;
import com.ua.plamber_android.fragments.DownloadDialogFragmant;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailBookActivity extends AppCompatActivity {


    public static final String PDFPATH = "PDFPATH";
    public static final String TAG = "DetailBookActivity";
    private static final int REQUEST_WRITE_STORAGE = 101;

    @BindView(R.id.iv_detail_book_image)
    ImageView mImageBook;
    @BindView(R.id.tv_detail_book_name)
    TextView mBookName;
    @BindView(R.id.tv_detail_author_book)
    TextView mAuthorBook;
    @BindView(R.id.tv_detail_language)
    TextView mLanguageBook;
    @BindView(R.id.tv_detail_genre_book)
    TextView mGenreBook;
    @BindView(R.id.tv_detail_about_book)
    TextView mAboutBook;
    @BindView(R.id.toolbar)
    Toolbar mToolbarDeatil;
    @BindView(R.id.btn_detail_download_book)
    Button mDetailButton;

    Book.BookData bookData;
    DownloadDialogFragmant dialogFragmant;
    private APIUtils apiUtils;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        ButterKnife.bind(this);

        bookData = new Gson().fromJson(getIntent().getStringExtra(RecyclerBookAdapter.BOOKKEY),
                Book.BookData.class);

        String url = PlamberAPI.ENDPOINT;
        String currentUrl = url.substring(0, url.length() - 1) + bookData.getPhoto();
        Glide.with(this).load(currentUrl).into(mImageBook);

        dialogFragmant = new DownloadDialogFragmant();
        apiUtils = new APIUtils(this);
        utils = new Utils(this);

        mBookName.setText(bookData.getBookName());
        mAuthorBook.setText(bookData.getIdAuthor());
        mLanguageBook.setText(bookData.getLanguage());
        mGenreBook.setText(bookData.getIdCategory());
        mAboutBook.setText(bookData.getDescription());
        mDetailButton.setTag("Download");

        setSupportActionBar(mToolbarDeatil);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(bookData.getBookName());
            getSupportActionBar().setElevation(10);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkFileExist();
    }

    @OnClick(R.id.btn_detail_download_book)
    public void downloadBookButton() {
        if (checkPermission()) {
            if (mDetailButton.getTag() == "Download") {
                if (apiUtils.isOnline())
                    runDownloadDialog();
            } else {
                Intent intent = BookReaderActivity.startReaderActivity(this);
                intent.putExtra(PDFPATH, utils.getBooksPath() + dialogFragmant.getFileName(bookData));
                startActivity(intent);
            }
        } else {
            runQuestionPermissions();
        }
    }

    private void runQuestionPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Don`t have write permission", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkFileExist() {
        File file = new File(utils.getBooksPath(), dialogFragmant.getFileName(bookData));
        if (file.exists()) {
            mDetailButton.setText("Read Book");
            mDetailButton.setTag("Read");
        } else {
            mDetailButton.setText("Download Book");
            mDetailButton.setTag("Download");
        }
    }

    public static Intent startDetailActivity(Context context) {
        Intent intent = new Intent(context, DetailBookActivity.class);
        return intent;
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void runDownloadDialog() {
        Bundle args = new Bundle();
        args.putString(DownloadDialogFragmant.DOWNLOADBOOK, new Gson().toJson(bookData));
        dialogFragmant.setArguments(args);
        dialogFragmant.setCancelable(false);
        dialogFragmant.show(getSupportFragmentManager(), "DownloadDialog");
    }
}

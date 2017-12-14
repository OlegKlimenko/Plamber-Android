package com.ua.plamber_android.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.fragments.UploadDialogFragment;
import com.ua.plamber_android.utils.FileUtils;
import com.ua.plamber_android.utils.TokenUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.upload_book_name)
    TextInputLayout mTilBookName;
    @BindView(R.id.et_upload_book_name)
    EditText mBookName;
    @BindView(R.id.upload_book_author)
    TextInputLayout mTilBookAuthor;
    @BindView(R.id.et_upload_book_author)
    EditText mBookAuthor;
    @BindView(R.id.et_upload_book_category)
    EditText mBookCategory;
    @BindView(R.id.et_upload_book_file)
    EditText mBookFile;
    @BindView(R.id.et_upload_book_language)
    EditText mBookLanguage;
    @BindView(R.id.et_upload_book_about)
    EditText mBookAbout;
    @BindView(R.id.upload_is_private)
    CheckBox mBookIsPrivate;

    @BindString(R.string.upload_book_activity)
    String title;

    Utils utils;
    TokenUtils tokenUtils;
    APIUtils apiUtils;
    File mFile;
    private static final String TAG = "UploadActivity";
    private static final int REQUEST_SELECT_FILE = 205;
    public static final String FILE_PATH = "FILEPATH";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        utils = new Utils(this);
        tokenUtils = new TokenUtils(this);
        apiUtils = new APIUtils(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(10);
            getSupportActionBar().setTitle(title);
        }
    }

    @OnClick(R.id.btn_upload_file)
    public void uploadFile() {
        UploadDialogFragment uploadDialogFragment = new UploadDialogFragment();
        uploadDialogFragment.setCancelable(false);
        uploadDialogFragment.show(getSupportFragmentManager(), "UploadDialog");
    }

    @OnClick(R.id.btn_upload_select_file)
    public void selectBookFile() {
        startActivityForResult(BookFilePickActivity.startBookFilePickActivity(this), REQUEST_SELECT_FILE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_FILE) {
            if (resultCode == Activity.RESULT_OK) {
                mFile = new File(data.getStringExtra(FILE_PATH));
                mBookFile.setText(mFile.getName());
                Log.i(TAG, mFile.getPath());
            }
        }
    }

    public static Intent startUploadActivity(Context context) {
        return new Intent(context, UploadActivity.class);
    }

    private void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

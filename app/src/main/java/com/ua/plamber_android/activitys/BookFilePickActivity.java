package com.ua.plamber_android.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.ua.plamber_android.R;
import com.ua.plamber_android.adapters.RecyclerFileAdapter;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.utils.FileUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookFilePickActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.file_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.message_no_pdf_file)
    TextView mMessageNoFile;
    RecyclerFileAdapter mAdapter;
    List<File> mFiles;
    File currentFolder;
    Utils utils;
    boolean isHidden;
    public static final String TAG = "BookFilePickActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_file_pick);
        ButterKnife.bind(this);
        utils = new Utils(getApplicationContext());
        mFiles = new ArrayList<>();
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(10);
            getSupportActionBar().setTitle("Select PDF file");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        currentFolder = new File(utils.getRootDirectory());
        mFiles = FileUtils.getPdfFileInDirectory(currentFolder.getPath(), isHidden);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration((new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)));

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (!mFiles.get(position).isFile()) {
                    openCurrentFolder(position);
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(UploadActivity.FILE_PATH, mFiles.get(position).getPath());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        };
        mAdapter = new RecyclerFileAdapter(mFiles, listener);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static Intent startBookFilePickActivity(Context context) {
        return new Intent(context, BookFilePickActivity.class);
    }

    private void openCurrentFolder(int position) {
        currentFolder = mFiles.get(position);
        String path = currentFolder.getPath();
        if (searchPdfFile(FileUtils.getPdfFileInDirectory(path, isHidden)))
        mAdapter.updateList(FileUtils.getPdfFileInDirectory(path, isHidden));
    }

    private void backDirectory() {
        if (currentFolder.getPath().equals(utils.getRootDirectory())) {
            finish();
        } else {
            String path = currentFolder.getParent();
            if (searchPdfFile(FileUtils.getPdfFileInDirectory(path, isHidden)))
            mAdapter.updateList(FileUtils.getPdfFileInDirectory(path, isHidden));
            currentFolder = new File(path);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backDirectory();
                break;
            case R.id.item_close_picker:
                finish();
                break;
            case R.id.item_show_hidden:
                isHidden = !isHidden;
                mAdapter.updateList((FileUtils.getPdfFileInDirectory(currentFolder.getPath(), isHidden)));

        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isHidden) {
            menu.findItem(R.id.item_show_hidden).setTitle(R.string.no_show_hidden_file);
        } else {
            menu.findItem(R.id.item_show_hidden).setTitle(R.string.show_hidden_file);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        backDirectory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_picker, menu);
        return true;
    }

    private boolean searchPdfFile(List<File> files) {
        if (files.size() == 0) {
            mMessageNoFile.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            return false;
        } else {
            mMessageNoFile.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            return true;
        }
    }
}

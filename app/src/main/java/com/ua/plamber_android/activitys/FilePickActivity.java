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

public class FilePickActivity extends AppCompatActivity {

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
    public static final String TAG = "FilePickActivity";
    public static final String FILE_PATH = "FILEPATH";
    public static final String[] BOOK_FORMAT = {"pdf"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_file_pick);
        ButterKnife.bind(this);
        utils = new Utils(getApplicationContext());
        mFiles = new ArrayList<>();
        initToolbar();

        currentFolder = utils.getRootDirectory();
        mFiles = searchFileInDirectory(currentFolder.getPath());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration((new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)));

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (!mFiles.get(position).isFile()) {
                    openCurrentFolder(position);
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(FILE_PATH, mFiles.get(position).getPath());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        };
        mAdapter = new RecyclerFileAdapter(mFiles, listener);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(10);
            getSupportActionBar().setTitle(setToolbarTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public int setToolbarTitle() {
        return R.string.select_pdf_file;
    }

    public static Intent startBookFilePickActivity(Context context) {
        return new Intent(context, FilePickActivity.class);
    }

    private void openCurrentFolder(int position) {
        currentFolder = mFiles.get(position);
        String path = currentFolder.getPath();
        if (checkEmptyFiles(searchFileInDirectory(path)))
        mAdapter.updateList(searchFileInDirectory(path));
        mRecyclerView.scrollToPosition(0);
    }

    private void backDirectory() {
        if (currentFolder.equals(utils.getRootDirectory())) {
            finish();
        } else {
            String path = currentFolder.getParent();
            if (checkEmptyFiles(searchFileInDirectory(path)))
            mAdapter.updateList(searchFileInDirectory(path));
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
                mAdapter.updateList((searchFileInDirectory(currentFolder.getPath())));
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

    private boolean checkEmptyFiles(List<File> files) {
        if (files.isEmpty()) {
            mMessageNoFile.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            return false;
        } else {
            mMessageNoFile.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private List<File> searchFileInDirectory(String path) {
        return FileUtils.getFileInDirectory(path, isHidden, searchFileType());
    }

    public String[] searchFileType() {
        return BOOK_FORMAT;
    }

    public TextView getmMessageNoFile() {
        return mMessageNoFile;
    }
}

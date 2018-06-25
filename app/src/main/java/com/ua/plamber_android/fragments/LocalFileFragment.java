package com.ua.plamber_android.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.BookReaderLocalActivity;
import com.ua.plamber_android.activitys.FilePickActivity;
import com.ua.plamber_android.adapters.RecyclerLocalBookAdapter;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.utils.FileUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocalFileFragment extends Fragment {

    public static final String TAG = "LocalFileFragment";
    public static final String FILE_LIST = "FILE_LIST";

    @BindView(R.id.local_refresh_layout)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.local_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.local_progress_load)
    ProgressBar mProgressLoad;
    @BindView(R.id.tv_local_message)
    TextView mMessage;

    private volatile List<File> listFile;
    private Utils utils;
    private LoadFiles loadFiles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(getActivity());
        listFile = new ArrayList<>();
        if (savedInstanceState != null)
            listFile = new Gson().fromJson(savedInstanceState.getString(FILE_LIST), new TypeToken<List<File>>() {
            }.getType());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.local_file_fragment, container, false);
        ButterKnife.bind(this, v);
        if (listFile.isEmpty()) {
            loadFiles = new LoadFiles();
            loadFiles.execute(FilePickActivity.BOOK_FORMAT);
        } else {
            setAdapter();
        }
        setSwipe();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(FILE_LIST, new Gson().toJson(listFile));
        super.onSaveInstanceState(outState);
    }

    private void setSwipe() {
        mSwipeRefresh.setOnRefreshListener(() -> {
            if (loadFiles != null && !loadFiles.isCancelled())
                loadFiles.cancel(true);
            loadFiles = new LoadFiles();
            loadFiles.execute(FilePickActivity.BOOK_FORMAT);
        });
    }

    public class LoadFiles extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            getAllFilesInDevise(utils.getUsersDirectory(), strings);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listFile.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!listFile.isEmpty()){
                setAdapter();
            }
            else{
                visible(mProgressLoad, false);
                visible(mRecyclerView, false);
                visible(mMessage, true);
                mSwipeRefresh.setRefreshing(false);
            }
        }
    }

    private void getAllFilesInDevise(File dir, String[] types) {
        File[] files = dir.listFiles();
        if (files == null || files.length <= 0)
            return;
        for (File file : files) {
            if (file.isDirectory()) {
                getAllFilesInDevise(file, types);
            } else if (FileUtils.isCorrectType(file, types)) {
                listFile.add(file);
            }
        }
    }

    private void setAdapter() {
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = BookReaderLocalActivity.startLocalReaderActivity(getActivity());
                intent.putExtra(BookReaderLocalActivity.LOCAL_BOOK_FILE, listFile.get(position).getAbsolutePath());
                intent.putExtra(BookReaderLocalActivity.LOCAL_BOOK_NAME, listFile.get(position).getName());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        };
        if (getActivity() != null) {
            deleteDuplicate();
            RecyclerLocalBookAdapter adapter = new RecyclerLocalBookAdapter(listFile, listener);
            mRecyclerView.setAdapter(adapter);
        }
        visible(mProgressLoad, false);
        visible(mRecyclerView, true);
        visible(mMessage, false);
        mSwipeRefresh.setRefreshing(false);

    }

    private void deleteDuplicate() {
        Set<File> set = new HashSet<>(listFile);
        listFile.clear();
        listFile.addAll(set);
    }

    public void visible(View v, boolean status) {
        if (status) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }
}

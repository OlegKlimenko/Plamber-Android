package com.ua.plamber_android.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.BookReaderLocalActivity;
import com.ua.plamber_android.activitys.FilePickActivity;
import com.ua.plamber_android.adapters.RecyclerLocalBookAdapter;
import com.ua.plamber_android.database.model.LocalBookDB;
import com.ua.plamber_android.database.utils.LocalBookUtils;
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

    private List<LocalBookDB> listFile;
    private Utils utils;
    private LoadFiles loadFiles;
    private RecyclerLocalBookAdapter adapter;
    private LocalBookUtils bookUtilsDB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(getActivity());
        bookUtilsDB = new LocalBookUtils(getActivity());
        listFile = new ArrayList<>();
        if (savedInstanceState != null)
            listFile = new Gson().fromJson(savedInstanceState.getString(FILE_LIST), new TypeToken<List<File>>() {}.getType());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.local_file_fragment, container, false);
        ButterKnife.bind(this, v);
        setSwipe();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listFile.isEmpty()) {
            loadFiles = new LoadFiles();
            loadFiles.execute(FilePickActivity.BOOK_FORMAT);
        } else {
            setAdapter(listFile);
        }
        //findAllPDF();
    }

    private void findAllPDF() {
        if (getActivity() == null)
            return;
        Log.i(TAG, String.valueOf(System.currentTimeMillis()));
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        String[] types = new String[]{mimeType};
        Cursor cursor = getActivity().getContentResolver()
                .query(MediaStore.Files.getContentUri("external"), null, selectionMimeType, types, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.i(TAG, cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i(TAG, String.valueOf(System.currentTimeMillis()));
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
            Log.i(TAG, String.valueOf(System.currentTimeMillis()));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i(TAG, String.valueOf(System.currentTimeMillis()));
            if (!listFile.isEmpty()) {
                setAdapter(listFile);
            } else {
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
                LocalBookDB book = new LocalBookDB();
                book.setBookPath(file.getAbsolutePath());
                book.setBookName(file.getName());
                book.setLastReadDate(bookUtilsDB.getDate(file.getAbsolutePath()));
                listFile.add(book);
            }
        }
    }

    private void setAdapter(List<LocalBookDB> books) {
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = BookReaderLocalActivity.startLocalReaderActivity(getActivity());
                intent.putExtra(BookReaderLocalActivity.LOCAL_BOOK_FILE, listFile.get(position).getBookPath());
                intent.putExtra(BookReaderLocalActivity.LOCAL_BOOK_NAME, listFile.get(position).getBookName());
                long time = System.currentTimeMillis();
                bookUtilsDB.updateDate(listFile.get(position).getBookPath(), time);
                listFile.get(position).setLastReadDate(time);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        };
        if (getActivity() == null)
            return;

        //deleteDuplicate();

        if (mRecyclerView.getAdapter() == null) {
            adapter = new RecyclerLocalBookAdapter(books, listener);
            mRecyclerView.setAdapter(adapter);
        } else
            adapter.updateLocalBooks(books);
        hideFindFileProgress();
    }

    private void hideFindFileProgress() {

        visible(mProgressLoad, false);
        visible(mRecyclerView, true);
        visible(mMessage, false);
        mSwipeRefresh.setRefreshing(false);
    }

    private void deleteDuplicate() {
        Set<LocalBookDB> set = new HashSet<>(listFile);
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

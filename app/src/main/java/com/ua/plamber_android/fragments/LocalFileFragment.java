package com.ua.plamber_android.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.BookReaderLocalActivity;
import com.ua.plamber_android.activitys.FilePickActivity;
import com.ua.plamber_android.adapters.RecyclerLocalBookAdapter;
import com.ua.plamber_android.database.model.LocalBookDB;
import com.ua.plamber_android.database.utils.LocalBookUtils;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocalFileFragment extends Fragment {

    public static final String TAG = "LocalFileFragment";
    public static final String FILE_LIST = "FILE_LIST";

    @BindView(R.id.local_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.local_progress_load)
    ProgressBar mProgressLoad;
    @BindView(R.id.tv_local_message)
    TextView mMessage;

    private RecyclerLocalBookAdapter adapter;
    private LocalBookUtils bookUtilsDB;
    private List<LocalBookDB> localBooks;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.local_file_fragment, container, false);
        ButterKnife.bind(this, v);
        bookUtilsDB = new LocalBookUtils(getActivity());
        localBooks = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        localBooks = new ArrayList<>();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadLocalBooks().execute();
    }

    private void findAllFiles() {
        if (getActivity() == null)
            return;

        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String[] mimeType = new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension(FilePickActivity.BOOK_FORMAT[0])};
        Cursor cursor = getActivity().getContentResolver()
                .query(MediaStore.Files.getContentUri("external"), null, selectionMimeType, mimeType, null);

        if (cursor == null)
            return;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocalBookDB book = new LocalBookDB();
            File file = new File(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
            book.setBookPath(file.getAbsolutePath());
            book.setBookName(file.getName());
            book.setLastReadDate(bookUtilsDB.getDate(file.getAbsolutePath()));
            localBooks.add(book);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private class LoadLocalBooks extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            localBooks.clear();
        }

        @Override
        protected Void doInBackground(Void... v) {
            findAllFiles();
            return null;
        }


        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (localBooks.isEmpty()) {
                noFindBookMessage();
                return;
            }
            sortList(localBooks);
            setAdapter();
            hideFindFileProgress();
        }
    }


    private void setAdapter() {
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = BookReaderLocalActivity.startLocalReaderActivity(getActivity());
                intent.putExtra(BookReaderLocalActivity.LOCAL_BOOK_FILE, localBooks.get(position).getBookPath());
                intent.putExtra(BookReaderLocalActivity.LOCAL_BOOK_NAME, localBooks.get(position).getBookName());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        };
        if (mRecyclerView.getAdapter() == null) {
            adapter = new RecyclerLocalBookAdapter(mRecyclerView, localBooks, listener);
            mRecyclerView.setAdapter(adapter);
            return;
        }

        adapter.updateLocalBooks(localBooks);
    }

    private void hideFindFileProgress() {
        visible(mProgressLoad, false);
        visible(mRecyclerView, true);
        visible(mMessage, false);
    }

    private void noFindBookMessage() {
        visible(mProgressLoad, false);
        visible(mRecyclerView, false);
        visible(mMessage, true);
    }

    public void visible(View v, boolean status) {
        if (status) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public void sortList(List<LocalBookDB> list) {
        Collections.sort(list, (o1, o2) -> Long.compare(o2.getLastReadDate(), o1.getLastReadDate()));
    }
}

package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.adapters.RecyclerBookAdapter;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.api.interfaces.callbacks.BooksCallback;
import com.ua.plamber_android.api.interfaces.callbacks.CurrentCategoryCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.CategoryBook;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseViewBookFragment extends Fragment {

    public RecyclerView recyclerView;
    public RecyclerBookAdapter mAdapter;
    public ProgressBar mUserBookProgress;
    public TextView mMessageAgain;
    public SwipeRefreshLayout mSwipeRefresh;
    public WorkAPI workAPI;

    public abstract String getBookAPI();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workAPI = new WorkAPI(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.base_view_book_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.user_book_recycler);
        mUserBookProgress = (ProgressBar) v.findViewById(R.id.user_book_progress);
        mMessageAgain = (TextView) v.findViewById(R.id.tv_user_book_again);
        mSwipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.user_book_refresh_layout);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewUserBook();
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            viewUserBook();

        return v;
    }


    public void viewUserBook() {
        workAPI.getUserBook(new BooksCallback() {
            @Override
            public void onSuccess(@NonNull List<Book.BookData> books) {
               viewBookFromList(books);
            }

            @Override
            public void onError(@NonNull Throwable t) {
               errorViewBook(t);
            }
        }, getBookAPI());
    }

    public void errorViewBook(Throwable t) {
        visibleProgress(recyclerView, false);
        visibleProgress(mUserBookProgress, false);
        visibleProgress(mMessageAgain, true);
        mSwipeRefresh.setRefreshing(false);
        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void viewBookFromList(List<Book.BookData> books) {
        visibleProgress(mMessageAgain, false);
        visibleProgress(mUserBookProgress, false);
        visibleProgress(recyclerView, true);
        mSwipeRefresh.setRefreshing(false);
        if (recyclerView.getAdapter() == null) {
            List<Book.BookData> oldBooks = new ArrayList<>();
            oldBooks.addAll(books);
            if (mAdapter == null) {
                mAdapter = new RecyclerBookAdapter(recyclerView, oldBooks);
            }
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.updateList(books);
        }
    }

    private void visibleProgress(View v, boolean status) {
        if (status) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }
}

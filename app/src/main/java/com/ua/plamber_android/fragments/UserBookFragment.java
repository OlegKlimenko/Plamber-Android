package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.api.interfaces.BooksCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.TokenUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBookFragment extends Fragment {

    private final static String TAG = "UserBookFragment";
    private final static String HOMEBOOKAPI = "api/v1/home/";

    private RecyclerView recyclerView;
    private RecyclerBookAdapter mAdapter;
    private ProgressBar mUserBookProgress;
    private TextView mMessageAgain;
    private SwipeRefreshLayout mSwipeRefresh;
    private WorkAPI workAPI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workAPI = new WorkAPI(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_books, container, false);
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

    private void viewUserBook() {
        workAPI.getUserBook(new BooksCallback() {
            @Override
            public void onSuccess(@NonNull List<Book.BookData> books) {
                visibleProgress(mMessageAgain, false);
                visibleProgress(mUserBookProgress, false);
                visibleProgress(recyclerView, true);
                mSwipeRefresh.setRefreshing(false);
                if (recyclerView.getAdapter() == null) {
                    List<Book.BookData> oldBooks = new ArrayList<>();
                    oldBooks.addAll(books);
                    mAdapter = new RecyclerBookAdapter(oldBooks);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.updateList(books);
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                visibleProgress(recyclerView, false);
                visibleProgress(mUserBookProgress, false);
                visibleProgress(mMessageAgain, true);
                mSwipeRefresh.setRefreshing(false);
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }, HOMEBOOKAPI);
    }


    private void visibleProgress(View v, boolean status) {
        if (status) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(v.GONE);
        }
    }
}


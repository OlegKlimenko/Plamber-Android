package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.adapters.RecyclerBookAdapter;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.api.interfaces.BooksCallback;
import com.ua.plamber_android.model.Book;

import java.util.ArrayList;
import java.util.List;

public class RecommendedFragmnet extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerBookAdapter mBookAdapter;
    private WorkAPI workAPI;

    private final static String RECOMMENDEDBOOKAPI = "api/v1/recommend/";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workAPI = new WorkAPI(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommended, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recommended_book_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        viewRecommendedBooks();
        return v;
    }

    private void viewRecommendedBooks() {
        workAPI.getUserBook(new BooksCallback() {
            @Override
            public void onSuccess(@NonNull List<Book.BookData> books) {
                if (mRecyclerView.getAdapter() == null) {
                    List<Book.BookData> oldBooks = new ArrayList<>();
                    oldBooks.addAll(books);
                    mBookAdapter = new RecyclerBookAdapter(oldBooks);
                    mRecyclerView.setAdapter(mBookAdapter);
                } else {
                    mBookAdapter.updateList(books);
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }, RECOMMENDEDBOOKAPI);
    }
}

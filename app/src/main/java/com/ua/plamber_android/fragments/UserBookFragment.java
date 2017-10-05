package com.ua.plamber_android.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.LoginActivity;
import com.ua.plamber_android.adapters.RecyclerBookAdapter;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.TokenUtils;

import java.util.ArrayList;
import java.util.List;

public class UserBookFragment extends Fragment {

    private final static String TAG = "UserBookFragment";

    private TokenUtils tokenUtils;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Book> books;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenUtils = new TokenUtils(getActivity());

        Log.i(TAG, tokenUtils.readToken());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_books, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.user_book_recycler);
        books = new ArrayList<>();

        mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerBookAdapter(books);
        recyclerView.setAdapter(mAdapter);

        return v;
    }
}

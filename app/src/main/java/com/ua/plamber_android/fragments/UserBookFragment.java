package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.interfaces.callbacks.BooksCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import java.util.List;

public class UserBookFragment extends BaseViewBookFragment {

    private final static String TAG = "UserBookFragment";
    private final static String HOME_BOOK_API = "api/v1/home/";
    BookUtilsDB bookUtilsDB;
    PreferenceUtils preferenceUtils;
    Utils utils;

    @Override
    public String getBookAPI() {
        return HOME_BOOK_API;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookUtilsDB = new BookUtilsDB(getActivity());
        preferenceUtils = new PreferenceUtils(getActivity());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideFloatingButton();
    }

    @Override
    public void viewUserBook() {
        getWorkAPI().getUserBook(new BooksCallback() {
            @Override
            public void onSuccess(@NonNull List<Book.BookData> books) {
                initAdapter(addOfflineBook(books));
            }

            @Override
            public void onError(@NonNull Throwable t) {
                errorViewBook(t);
            }
        }, getBookAPI());
    }

    @Override
    public void viewBookOffline() {
        initAdapter(bookUtilsDB.getListBookFromDB());
    }
}


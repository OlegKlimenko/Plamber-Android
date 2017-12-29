package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ua.plamber_android.R;
import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.utils.PreferenceUtils;

public class UserBookFragment extends BaseViewBookFragment {

    private final static String TAG = "UserBookFragment";
    private final static String HOME_BOOK_API = "api/v1/home/";
    BookUtilsDB bookUtilsDB;
    PreferenceUtils preferenceUtils;

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideFloatingButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferenceUtils.readStatusOffline())
            viewBookOffline();
        else
            viewUserBook();
    }

    @Override
    public void viewBookOffline() {
        offlineMessage();
        initAdapter(bookUtilsDB.getListBookFromDB());
    }
}


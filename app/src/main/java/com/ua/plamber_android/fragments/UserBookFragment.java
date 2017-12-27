package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.BookDataBase;
import com.ua.plamber_android.utils.DataBaseUtils;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class UserBookFragment extends BaseViewBookFragment {

    private final static String TAG = "UserBookFragment";
    private final static String HOME_BOOK_API = "api/v1/home/";
    DataBaseUtils dataBaseUtils;
    PreferenceUtils preferenceUtils;

    @Override
    public String getBookAPI() {
        return HOME_BOOK_API;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseUtils = new DataBaseUtils(getActivity());
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
        initAdapter(dataBaseUtils.getBookDataList());
    }
}


package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.LibraryActivity;
import com.ua.plamber_android.utils.PreferenceUtils;

public class UploadFragment extends BaseViewBookFragment {

    private final static String UPLOAD_BOOK_API = "api/v1/uploaded/";
    private PreferenceUtils preferenceUtils;

    @Override
    public String getBookAPI() {
        return UPLOAD_BOOK_API;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtils = new PreferenceUtils(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideFloatingButton();
    }

    @Override
    public void viewMessageListEmpty(String message) {
        super.viewMessageListEmpty(getString(R.string.no_upload_book_in_library));
    }
}

package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class UserBookFragment extends BaseViewBookFragment {

    private final static String TAG = "UserBookFragment";
    private final static String HOME_BOOK_API = "api/v1/home/";

    @Override
    public String getBookAPI() {
        return HOME_BOOK_API;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideFloatingButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isUpdate) {
            viewUserBook();
            isUpdate = false;
        }
    }

    @Override
    public void viewBookOffline() {
        offlineMessage();
        mMessageAgain.setText("Books will be here");
    }
}


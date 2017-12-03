package com.ua.plamber_android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class UserBookFragment extends BaseViewBookFragment {

    private final static String TAG = "UserBookFragment";
    private final static String HOMEBOOKAPI = "api/v1/home/";

    @Override
    public String getBookAPI() {
        return HOMEBOOKAPI;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isUpdate) {
            viewUserBook();
        }
    }
}


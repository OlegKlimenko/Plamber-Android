package com.ua.plamber_android.fragments;

public class UserBookFragment extends BaseViewBookFragment {

    private final static String TAG = "UserBookFragment";
    private final static String HOMEBOOKAPI = "api/v1/home/";

    @Override
    public String getBookAPI() {
        return HOMEBOOKAPI;
    }
}


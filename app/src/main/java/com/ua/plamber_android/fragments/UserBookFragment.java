package com.ua.plamber_android.fragments;

public class UserBookFragment extends BaseViewBookFragment {

    private final static String TAG = "UserBookFragment";
    private final static String HOME_BOOK_API = "api/v1/home/";

    @Override
    public String getBookAPI() {
        return HOME_BOOK_API;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isUpdate) {
            viewUserBook();
        }
    }
}


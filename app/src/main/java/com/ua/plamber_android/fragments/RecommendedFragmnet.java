package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ua.plamber_android.utils.PreferenceUtils;

public class RecommendedFragmnet extends BaseViewBookFragment {

    private final static String RECOMMENDED_BOOK_API = "api/v1/recommend/";

    @Override
    public String getBookAPI() {
        return RECOMMENDED_BOOK_API;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

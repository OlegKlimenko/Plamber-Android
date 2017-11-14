package com.ua.plamber_android.fragments;

public class RecommendedFragmnet extends BaseViewBookFragment {

    private final static String RECOMMENDEDBOOKAPI = "api/v1/recommend/";

    @Override
    public String getBookAPI() {
        return RECOMMENDEDBOOKAPI;
    }
}

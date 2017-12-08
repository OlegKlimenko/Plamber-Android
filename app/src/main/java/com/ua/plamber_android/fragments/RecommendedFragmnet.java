package com.ua.plamber_android.fragments;

public class RecommendedFragmnet extends BaseViewBookFragment {

    private final static String RECOMMENDED_BOOK_API = "api/v1/recommend/";

    @Override
    public String getBookAPI() {
        return RECOMMENDED_BOOK_API;
    }
}

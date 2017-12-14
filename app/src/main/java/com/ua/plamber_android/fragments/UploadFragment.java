package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

import com.ua.plamber_android.R;

public class UploadFragment extends BaseViewBookFragment {

    private final static String UPLOAD_BOOK_API = "api/v1/uploaded/";
    public static int currentPosition;

    @Override
    public String getBookAPI() {
        return UPLOAD_BOOK_API;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_upload);
        getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fab.isShown()) {
                    fab.hide();
                } else if (currentPosition == 3){
                    fab.show();
                }
            }
        });
    }
}

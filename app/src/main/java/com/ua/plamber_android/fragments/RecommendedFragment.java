package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ua.plamber_android.interfaces.callbacks.BooksCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.PreferenceUtils;

import java.util.List;

public class RecommendedFragment extends BaseViewBookFragment {

    private final static String RECOMMENDED_BOOK_API = "api/v1/recommend/";
    private boolean isUpdate;

    @Override
    public String getBookAPI() {
        return RECOMMENDED_BOOK_API;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isUpdate = true;
    }

    @Override
    public void setBooks() {
        if (isUpdate) {
            if (getPreferenceUtils().readLogic(PreferenceUtils.OFFLINE_MODE))
                viewBookOffline();
            else
                viewUserBook();
            isUpdate = false;
        }
    }

    @Override
    public void viewUserBook() {
        getWorkAPI().getUserBook(new BooksCallback() {
            @Override
            public void onSuccess(@NonNull List<Book.BookData> books) {
                initAdapter(books);
                getRecyclerView().scrollToPosition(0);
            }

            @Override
            public void onError(@NonNull Throwable t) {
                errorViewBook(t);
            }
        }, getBookAPI());
    }
}

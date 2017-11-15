package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ua.plamber_android.api.interfaces.callbacks.CurrentCategoryCallback;
import com.ua.plamber_android.model.CategoryBook;

public class CategoryFragment extends BaseViewBookFragment {

    public static final String TAG = "CategoryFragment";

    private Long idCategory;
    private int currentPage = 1;

    @Override
    public String getBookAPI() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idCategory = getArguments().getLong(LibraryFragment.IDCATEGORI);
    }

    @Override
    public void viewUserBook() {
        workAPI.getBooksFromCategory(new CurrentCategoryCallback() {
            @Override
            public void onSuccess(@NonNull CategoryBook.CategoryBookData data) {
                viewBookFromList(data.getBookData());
                if (data.getNextPageNumber() > 1) {
                    currentPage++;
                }
                Log.i(TAG, String.valueOf(data.getNextPageNumber()));
            }

            @Override
            public void onError(@NonNull Throwable t) {
                errorViewBook(t);
            }
        }, currentPage, idCategory);
    }
}

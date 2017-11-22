package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ua.plamber_android.api.interfaces.OnLoadMoreListener;
import com.ua.plamber_android.api.interfaces.callbacks.CurrentCategoryCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.CategoryBook;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends BaseViewBookFragment {

    public static final String TAG = "CategoryFragment";

    private Long idCategory;
    private int page = 1;

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
        loadCategoryBook();
    }

    private void loadCategoryBook() {
        if (page != 0) {
            getWorkAPI().getBooksFromCategory(new CurrentCategoryCallback() {
                @Override
                public void onSuccess(@NonNull final CategoryBook.CategoryBookData data) {
                    final List<Book.BookData> books = new ArrayList<>();
                    books.addAll(data.getBookData());
                    page = data.getNextPageNumber();
                    initAdapter(books);
                    getmAdapter().setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            if (page != 0) {
                                books.add(null);
                                getmAdapter().notifyItemInserted(books.size() - 1);
                                getWorkAPI().getBooksFromCategory(new CurrentCategoryCallback() {
                                    @Override
                                    public void onSuccess(@NonNull CategoryBook.CategoryBookData data) {
                                        books.remove(books.size() - 1);
                                        getmAdapter().notifyItemRemoved(books.size());
                                        getmAdapter().stopLoading();
                                        books.addAll(data.getBookData());
                                        page = data.getNextPageNumber();
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable t) {

                                    }
                                }, page, idCategory);
                            }
                        }
                    });
                }

                @Override
                public void onError(@NonNull Throwable t) {
                    errorViewBook(t);
                }
            }, 1, idCategory);
        } else {
            page = 1;
            viewUserBook();
        }
        if (getmAdapter() != null) {
            getmAdapter().stopLoading();
        }
    }
}


package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    private List<Book.BookData> books;

    @Override
    public String getBookAPI() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idCategory = getArguments().getLong(LibraryFragment.IDCATEGORI);
        books = new ArrayList<>();
    }

    @Override
    public void fullUpdate() {
       loadCategoryBook(true);
    }

    @Override
    public void viewUserBook() {
        loadCategoryBook(false);
    }

    private void loadCategoryBook(boolean isFullUpdate) {
        if (isFullUpdate)
            page = 0;

        if (page != 0) {
            getWorkAPI().getBooksFromCategory(new CurrentCategoryCallback() {
                @Override
                public void onSuccess(@NonNull final CategoryBook.CategoryBookData data) {
                    books.addAll(data.getBookData());
                    page = data.getNextPageNumber();
                    viewBookFromList(books);
                    getmAdapter().setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            if (page != 0) {
                                viewUserBook();
                            }
                        }
                    });
                }

                @Override
                public void onError(@NonNull Throwable t) {
                    errorViewBook(t);
                }
            }, page, idCategory);
        } else {
            books.clear();
            page = 1;
            viewUserBook();
        }
        if (getmAdapter() != null) {
            getmAdapter().setLoading(false);
        }
    }
}


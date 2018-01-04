package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ua.plamber_android.R;
import com.ua.plamber_android.interfaces.OnLoadMoreListener;
import com.ua.plamber_android.interfaces.callbacks.LoadMoreCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.LoadMoreBook;

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
    public void viewMessageListEmpty(String message) {
        super.viewMessageListEmpty(getString(R.string.no_book_in_category_message));
    }

    @Override
    public void viewUserBook() {
        loadCategoryBook();
    }

    private void loadCategoryBook() {
        if (page != 0) {
            getWorkAPI().getBooksFromCategory(new LoadMoreCallback() {
                @Override
                public void onSuccess(@NonNull final LoadMoreBook.LoadMoreBookData data) {
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
                                getWorkAPI().getBooksFromCategory(new LoadMoreCallback() {
                                    @Override
                                    public void onSuccess(@NonNull LoadMoreBook.LoadMoreBookData data) {
                                        books.remove(books.size() - 1);
                                        getmAdapter().notifyItemRemoved(books.size());
                                        getmAdapter().stopLoading();
                                        books.addAll(data.getBookData());
                                        page = data.getNextPageNumber();
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable t) {
                                        errorViewBook(t);
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


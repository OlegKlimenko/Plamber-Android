package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.SearchActivity;
import com.ua.plamber_android.interfaces.OnLoadMoreListener;
import com.ua.plamber_android.interfaces.callbacks.LoadMoreCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.LoadMoreBook;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseViewBookFragment {

    private int page = 1;
    private String term = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        term = getArguments().getString(SearchActivity.SEARCH_KEY);
    }

    @Override
    public String getBookAPI() {
        return null;
    }

    @Override
    public void viewUserBook() {
        getmSwipeRefresh().setEnabled(false);
        searchBook();
    }

    private void searchBook() {
        if (page != 0) {
            getWorkAPI().searchBook(new LoadMoreCallback() {
                @Override
                public void onSuccess(@NonNull LoadMoreBook.LoadMoreBookData data) {
                    final List<Book.BookData> books = new ArrayList<>();
                    books.addAll(data.getBookData());
                    if (!books.isEmpty()) {
                        page = data.getNextPageNumber();
                        initAdapter(books);
                        getmAdapter().setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                if (page != 0) {
                                    books.add(null);
                                    getmAdapter().notifyItemInserted(books.size() - 1);
                                    getWorkAPI().searchBook(new LoadMoreCallback() {
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
                                    }, page, term);
                                }
                            }
                        });
                    } else {
                        visible(getRecyclerView(), false);
                        visible(getmUserBookProgress(), false);
                        visible(getmMessageAgain(), true);
                        getmMessageAgain().setText(R.string.nothing_found);
                    }
                }

                @Override
                public void onError(@NonNull Throwable t) {
                    errorViewBook(t);
                }
            }, page, term);
        } else {
            page = 1;
            viewUserBook();
        }
        if (getmAdapter() != null) {
            getmAdapter().stopLoading();
        }
    }
}

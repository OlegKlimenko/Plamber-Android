package com.ua.plamber_android.api.interfaces;

import android.support.annotation.NonNull;

import com.ua.plamber_android.model.Book;

import java.util.List;

public interface BooksCallback {
    void onSuccess(@NonNull List<Book.BookData> books);

    void onError(@NonNull Throwable t);
}

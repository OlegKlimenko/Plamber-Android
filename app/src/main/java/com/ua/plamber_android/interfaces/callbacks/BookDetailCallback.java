package com.ua.plamber_android.interfaces.callbacks;

import android.support.annotation.NonNull;

import com.ua.plamber_android.model.Book;

public interface BookDetailCallback {
    void onSuccess(@NonNull Book.BookDetailRespond bookDetail);

    void onError(@NonNull Throwable t);
}

package com.ua.plamber_android.interfaces.callbacks;

import android.support.annotation.NonNull;

import com.ua.plamber_android.model.LoadMoreBook;

public interface LoadMoreCallback {
    void onSuccess(@NonNull LoadMoreBook.LoadMoreBookData data);

    void onError(@NonNull Throwable t);
}

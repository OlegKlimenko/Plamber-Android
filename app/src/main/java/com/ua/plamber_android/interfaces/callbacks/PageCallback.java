package com.ua.plamber_android.interfaces.callbacks;

import android.support.annotation.NonNull;

import com.ua.plamber_android.model.Page;

public interface PageCallback {
    void onSuccess(@NonNull Page.PageData page);

    void onError(@NonNull Throwable t);
}

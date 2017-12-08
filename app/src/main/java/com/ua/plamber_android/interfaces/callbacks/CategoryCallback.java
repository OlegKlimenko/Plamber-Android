package com.ua.plamber_android.interfaces.callbacks;

import android.support.annotation.NonNull;

import com.ua.plamber_android.model.Library;

import java.util.List;

public interface CategoryCallback {
    void onSuccess(@NonNull List<Library.LibraryData> categories);

    void onError(@NonNull Throwable t);
}

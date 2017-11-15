package com.ua.plamber_android.api.interfaces.callbacks;

import android.support.annotation.NonNull;

import com.ua.plamber_android.model.CategoryBook;
import com.ua.plamber_android.model.Library;

import java.util.List;

public interface CurrentCategoryCallback {
    void onSuccess(@NonNull CategoryBook.CategoryBookData data);

    void onError(@NonNull Throwable t);
}

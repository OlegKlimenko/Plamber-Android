package com.ua.plamber_android.interfaces.callbacks;

import android.support.annotation.NonNull;

public interface PageCallback {
    void onSuccess(@NonNull int status, @NonNull int page);

    void onError(@NonNull Throwable t);
}

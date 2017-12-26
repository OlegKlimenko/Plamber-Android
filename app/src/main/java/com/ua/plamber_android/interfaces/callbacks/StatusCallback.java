package com.ua.plamber_android.interfaces.callbacks;

import android.support.annotation.NonNull;

import java.util.List;

public interface StatusCallback {
    void onSuccess(@NonNull int status);

    void onError(@NonNull Throwable t);
}

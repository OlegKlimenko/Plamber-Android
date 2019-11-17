package com.ua.plamber_android.interfaces.callbacks;

import android.support.annotation.NonNull;

public interface ManageBookCallback {
    void onSuccess(@NonNull boolean result, int code);

    void onError(@NonNull Throwable t);
}

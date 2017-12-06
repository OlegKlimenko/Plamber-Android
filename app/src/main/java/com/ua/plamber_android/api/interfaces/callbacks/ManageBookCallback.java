package com.ua.plamber_android.api.interfaces.callbacks;

import android.support.annotation.NonNull;

public interface ManageBookCallback {
    void onSuccess(@NonNull boolean result);

    void onError(@NonNull Throwable t);
}

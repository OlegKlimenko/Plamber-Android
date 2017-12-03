package com.ua.plamber_android.api.interfaces.callbacks;

import android.support.annotation.NonNull;

public interface AccountCallback {
    void onSuccess(@NonNull boolean isCreate);

    void onError(@NonNull Throwable t);
}

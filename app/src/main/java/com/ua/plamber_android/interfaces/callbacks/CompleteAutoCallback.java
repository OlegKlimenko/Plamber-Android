package com.ua.plamber_android.interfaces.callbacks;

import android.support.annotation.NonNull;

import java.util.List;

public interface CompleteAutoCallback {
    void onSuccess(@NonNull List<String> stringsList);

    void onError(@NonNull Throwable t);
}

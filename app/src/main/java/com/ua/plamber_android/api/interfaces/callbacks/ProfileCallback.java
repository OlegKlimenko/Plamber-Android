package com.ua.plamber_android.api.interfaces.callbacks;

import android.support.annotation.NonNull;

import com.ua.plamber_android.model.User;

public interface ProfileCallback {
    void onSuccess(@NonNull User.ProfileData profileData);

    void onError(@NonNull Throwable t);
}

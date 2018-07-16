package com.ua.plamber_android.interfaces.callbacks;

import android.support.annotation.NonNull;

import com.ua.plamber_android.model.ReminderList;

import java.util.List;

public interface ReminderCallback {
    void onSuccess(@NonNull List<ReminderList.Data> reminders);

    void onError(@NonNull Throwable t);
}

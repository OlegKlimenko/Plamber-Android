package com.ua.plamber_android.interfaces.callbacks;

import android.support.v4.app.DialogFragment;

import com.ua.plamber_android.model.ReminderList;

public interface ReminderListenerDialog {
    void onPositiveClick(DialogFragment dialog, ReminderList.Data data);
    void onNegativeClick(DialogFragment dialog);
    void onNeutralClick(DialogFragment dialog);
}

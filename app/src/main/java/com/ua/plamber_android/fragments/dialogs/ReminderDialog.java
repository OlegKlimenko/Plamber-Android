package com.ua.plamber_android.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.interfaces.callbacks.ReminderListenerDialog;
import com.ua.plamber_android.model.ReminderList;
import com.ua.plamber_android.utils.Reminder;

public class ReminderDialog extends DialogFragment {
    public static final String TAG = ReminderDialog.class.getSimpleName();
    public static final String REMINDER_DATA = "REMINDER_DATA";

    private ReminderList.Data reminder;
    private ReminderListenerDialog mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = new Reminder();
        } catch (ClassCastException e) {
            Log.i(TAG, e.getLocalizedMessage() + "Calling fragment must implement ReminderListenerDialog");
        }
        if (getArguments() != null)
            reminder = new Gson().fromJson(getArguments().getString(REMINDER_DATA), ReminderList.Data.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int positiveTitle = R.string.follow_btn;
        int title = R.string.follow_title_dialog;
        if (reminder.getNameId().equals(ReminderList.APP_RATE)) {
            positiveTitle = R.string.rate_app_btn;
            title = R.string.rate_title_dialog;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(reminder.getMessage())
                .setPositiveButton(positiveTitle, null)
                .setNegativeButton(R.string.later_btn, null)
                .setNeutralButton(R.string.never_btn, null);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v ->
        {
            dismiss();
            mListener.onPositiveClick(ReminderDialog.this, reminder);
        });
        getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v ->
        {
            dismiss();
            mListener.onNegativeClick(ReminderDialog.this);
        });
        getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v ->
        {
            dismiss();
            mListener.onNeutralClick(ReminderDialog.this);
        });

    }

    private Button getButton(int button) {
        AlertDialog dialog = (AlertDialog) getDialog();
        return dialog.getButton(button);
    }
}

package com.ua.plamber_android.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.gson.Gson;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.fragments.dialogs.ReminderDialog;
import com.ua.plamber_android.interfaces.callbacks.ReminderCallback;
import com.ua.plamber_android.interfaces.callbacks.ReminderListenerDialog;
import com.ua.plamber_android.interfaces.callbacks.StatusCallback;
import com.ua.plamber_android.model.ReminderList;

import java.util.List;
import java.util.Random;

public class Reminder implements ReminderListenerDialog {
    private static final String TAG = Reminder.class.getSimpleName();

    public static void init(Context context, FragmentManager fm, int count) {
        showIfAvailable(context, fm, count);
    }

    private static void showIfAvailable(Context context, FragmentManager fm, int count) {
        PreferenceUtils preferenceUtils = new PreferenceUtils(context);
        if (preferenceUtils.readReminderCount() >= count) {
            new Reminder().getReminderDate(context, fm);
            return;
        }

        preferenceUtils.incrementReminderCount();
    }

    private void getReminderDate(Context context, FragmentManager fm) {
        WorkAPI workAPI = new WorkAPI(context);
        workAPI.getAllReminder(new ReminderCallback() {
            @Override
            public void onSuccess(@NonNull List<ReminderList.Data> reminders) {
                if (reminders.isEmpty()) {
                    disableAllReminder(context);
                    return;
                }
                showDialog(getRandomElement(reminders), fm);
            }

            @Override
            public void onError(@NonNull Throwable t) {
                Log.i(TAG, t.getLocalizedMessage());
            }
        });

    }

    private void showDialog(ReminderList.Data date, FragmentManager fm) {
        if (fm == null)
            return;
        Bundle args = new Bundle();
        args.putString(ReminderDialog.REMINDER_DATA, new Gson().toJson(date));
        ReminderDialog reminderDialog = new ReminderDialog();
        reminderDialog.setCancelable(false);
        reminderDialog.setArguments(args);
        reminderDialog.show(fm, ReminderDialog.TAG);
    }

    @Override
    public void onPositiveClick(DialogFragment dialog, ReminderList.Data data) {
        if (dialog == null || dialog.getContext() == null)
            return;

        new PreferenceUtils(dialog.getContext()).reminderReset();

        WorkAPI workAPI = new WorkAPI(dialog.getContext());
        workAPI.disableReminder(new StatusCallback() {
            @Override
            public void onSuccess(@NonNull int status) {
                Log.i(TAG, data.getNameId() + " is disabled");
            }

            @Override
            public void onError(@NonNull Throwable t) {
                Log.i(TAG, t.getLocalizedMessage());
            }
        }, data.getNameId());

        if (data.getNameId().equals(ReminderList.APP_RATE)) {
            openRateApp(dialog.getContext());
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrl()));
        dialog.getContext().startActivity(intent);
    }

    @Override
    public void onNegativeClick(DialogFragment dialog) {
        if (dialog == null || dialog.getContext() == null)
            return;
        new PreferenceUtils(dialog.getContext()).reminderReset();
    }

    @Override
    public void onNeutralClick(DialogFragment dialog) {
        if (dialog == null || dialog.getContext() == null)
            return;
        disableAllReminder(dialog.getContext());
        new PreferenceUtils(dialog.getContext()).writeLogic(PreferenceUtils.DISABLE_SHOW_REMINDER, true);
    }

    private ReminderList.Data getRandomElement(List<ReminderList.Data> reminders) {
        int index = new Random().nextInt(reminders.size());
        return reminders.get(index);
    }

    private void disableAllReminder(Context context) {
        WorkAPI workAPI = new WorkAPI(context);
        workAPI.disableReminder(new StatusCallback() {
            @Override
            public void onSuccess(@NonNull int status) {
                Log.i(TAG, "Reminder disabled");
            }

            @Override
            public void onError(@NonNull Throwable t) {
                Log.i(TAG, t.getLocalizedMessage());
            }
        }, ReminderList.DISABLE_ALL);
    }

    private void openRateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
}

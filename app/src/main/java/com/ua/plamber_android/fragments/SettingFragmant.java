package com.ua.plamber_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ua.plamber_android.BuildConfig;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.ChangePasswordActivity;
import com.ua.plamber_android.activitys.SupportActivity;
import com.ua.plamber_android.fragments.dialogs.ChangeAvatarDialog;
import com.ua.plamber_android.utils.PlamberAnalytics;

public class SettingFragmant extends PreferenceFragmentCompat {

    public static final String TAG = "Setting";

    Tracker mTracker;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        setSupport();
        setChangePassword();
        setAvatar();
        setAppVersion();
        initGoogleAnalytics();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.view_activity))
                .setAction(TAG).build());
    }

    private void initGoogleAnalytics() {
        if (getActivity() != null) {
            PlamberAnalytics plamberAnalytics = (PlamberAnalytics) getActivity().getApplication();
            mTracker = plamberAnalytics.getTracker();
            mTracker.setScreenName(TAG);
        }
    }

    private void setAvatar() {
        getPreference("change_avatar").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ChangeAvatarDialog changeAvatarDialog = new ChangeAvatarDialog();
                changeAvatarDialog.setCancelable(false);
                if (getFragmentManager() != null)
                changeAvatarDialog.show(getFragmentManager(), ChangeAvatarDialog.TAG);
                return true;
            }
        });

    }

    private void setSupport() {
        getPreference("support").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = SupportActivity.startSupportActivity(getActivity());
                startActivity(intent);
                return true;
            }
        });
    }

    private void setChangePassword() {
        getPreference("change_password").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = ChangePasswordActivity.startChangePasswordActivity(getActivity());
                startActivity(intent);
                return true;
            }
        });
    }

    private void setAppVersion() {
        getPreference("version").setSummary(BuildConfig.VERSION_NAME);
    }

    private  Preference getPreference(String key) {
        return findPreference(key);
    }
}

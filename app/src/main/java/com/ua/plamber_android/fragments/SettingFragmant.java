package com.ua.plamber_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.ChangePasswordActivity;
import com.ua.plamber_android.activitys.LoginActivity;
import com.ua.plamber_android.activitys.SupportActivity;

public class SettingFragmant extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreference("change_avatar").setWidgetLayoutResource(R.layout.avatar_change);
        setSupport();
        setChangePassword();
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

    private Preference getPreference(String key) {
        return findPreference(key);
    }
}

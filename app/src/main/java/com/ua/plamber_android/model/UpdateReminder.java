package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class UpdateReminder {
    public static class UpdateReminderRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("token")
        private String token;

        @SerializedName("field")
        private String nameReminder;

        @SerializedName("value")
        private boolean isEnable;

        public UpdateReminderRequest(String appKey, String token, String nameReminder, boolean isEnable) {
            this.appKey = appKey;
            this.token = token;
            this.nameReminder = nameReminder;
            this.isEnable = isEnable;
        }
    }
}

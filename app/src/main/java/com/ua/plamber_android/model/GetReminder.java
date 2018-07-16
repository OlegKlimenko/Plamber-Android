package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class GetReminder {
    public static class GetReminderRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("user_token")
        private String userToken;

        public GetReminderRequest(String appKey, String userToken) {
            this.appKey = appKey;
            this.userToken = userToken;
        }
    }

    public static class GetReminderRespond {
        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private GetReminderData data;

        public String getDetail() {
            return detail;
        }

        public GetReminderData getData() {
            return data;
        }
    }

    public static class GetReminderData {
        @SerializedName("vk")
        private boolean vk;

        @SerializedName("twitter")
        private boolean twitter;

        @SerializedName("fb_page")
        private boolean fbPage;

        @SerializedName("app_rate")
        private boolean appRate;

        @SerializedName("fb_group")
        private boolean fbGroup;

        @SerializedName("disabled_all")
        private boolean disabelAll;

        public boolean isVk() {
            return vk;
        }

        public boolean isTwitter() {
            return twitter;
        }

        public boolean isFbPage() {
            return fbPage;
        }

        public boolean isAppRate() {
            return appRate;
        }

        public boolean isFbGroup() {
            return fbGroup;
        }

        public boolean isDisabelAll() {
            return disabelAll;
        }
    }

}

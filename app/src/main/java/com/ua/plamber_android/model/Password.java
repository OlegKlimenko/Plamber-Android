package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Password {
    public static class PasswordRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("user_token")
        private String userToken;

        @SerializedName("prev_password")
        private String prevPassword;

        @SerializedName("new_password")
        private String newPassword;

        public PasswordRequest(String appKey, String userToken, String prevPassword, String newPassword) {
            this.appKey = appKey;
            this.userToken = userToken;
            this.prevPassword = prevPassword;
            this.newPassword = newPassword;
        }
    }

    public class PasswordRespond {

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private PasswordData data;

        public String getDetail() {
            return detail;
        }

        public PasswordData getData() {
            return data;
        }
    }

    private class PasswordData {

    }

}

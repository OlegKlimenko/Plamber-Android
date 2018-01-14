package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Password {
    public static class PasswordRequest {
        @SerializedName("user_token")
        private String userToken;

        @SerializedName("prev_password")
        private String prevPassword;

        @SerializedName("new_password")
        private String newPassword;

        public PasswordRequest(String userToken, String prevPassword, String newPassword) {
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

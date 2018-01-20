package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Support {
    public static class SupportRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("email")
        private String email;
        @SerializedName("text")
        private String message;

        public SupportRequest(String appKey, String email, String message) {
            this.appKey = appKey;
            this.email = email;
            this.message = message;
        }
    }

    public static class SupportRespond {

        @SerializedName("detail")
        String detail;

        @SerializedName("data")
        SupportData data;

        public String getDetail() {
            return detail;
        }

        public SupportData getData() {
            return data;
        }
    }

    public class SupportData {

    }
}

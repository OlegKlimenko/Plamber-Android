package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Support {
    public static class SupportRequest {
        @SerializedName("email")
        private String email;
        @SerializedName("text")
        private String message;

        public SupportRequest(String email, String message) {
            this.email = email;
            this.message = message;
        }
    }

    public static class SupportRespond {
        @SerializedName("status")
        int status;

        @SerializedName("detail")
        String detail;

        @SerializedName("data")
        SupportData data;

        public int getStatus() {
            return status;
        }

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

package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Language {
    public static class LanguageRequest {
        @SerializedName("user_token")
        private String userToken;

        public LanguageRequest(String userToken) {
            this.userToken = userToken;
        }
    }

    public static class LanguageRespond {
        @SerializedName("status")
        private int status;

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private List<String> data;

        public int getStatus() {
            return status;
        }

        public String getDetail() {
            return detail;
        }

        public List<String> getData() {
            return data;
        }
    }
}

package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Language {
    public static class LanguageRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("user_token")
        private String userToken;

        public LanguageRequest(String appKey, String userToken) {
            this.appKey = appKey;
            this.userToken = userToken;
        }
    }

    public static class LanguageRespond {

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private List<String> data;

        public String getDetail() {
            return detail;
        }

        public List<String> getData() {
            return data;
        }
    }
}

package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoComplete {
    public static class AuthorRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("user_token")
        private String userToken;

        @SerializedName("author_part")
        private String authorPart;

        public AuthorRequest(String appKey, String userToken, String authorPart) {
            this.appKey = appKey;
            this.userToken = userToken;
            this.authorPart = authorPart;
        }
    }

    public static class BookRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("user_token")
        private String userToken;

        @SerializedName("book_part")
        private String bookPart;

        public BookRequest(String appKey, String userToken, String bookPart) {
            this.appKey = appKey;
            this.userToken = userToken;
            this.bookPart = bookPart;
        }
    }


    public static class AuthorRespond {
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

package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoComplete {
    public static class AuthorRequest {
        @SerializedName("user_token")
        private String userToken;

        @SerializedName("author_part")
        private String authorPart;

        public AuthorRequest(String userToken, String authorPart) {
            this.userToken = userToken;
            this.authorPart = authorPart;
        }
    }

    public static class BookRequest {
        @SerializedName("user_token")
        private String userToken;

        @SerializedName("book_part")
        private String bookPart;

        public BookRequest(String userToken, String bookPart) {
            this.userToken = userToken;
            this.bookPart = bookPart;
        }
    }



    public static class AuthorRespond {
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

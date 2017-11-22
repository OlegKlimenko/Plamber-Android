package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Page {
    public static class SetPageRequest {
        @SerializedName("user_token")
        private String userToken;

        @SerializedName("book_id")
        private long book_id;

        @SerializedName("current_page")
        private int currentPage;

        public SetPageRequest(String userToken, long book_id, int currentPage) {
            this.userToken = userToken;
            this.book_id = book_id;
            this.currentPage = currentPage;
        }
    }

    public static class GetPageRequest {
        @SerializedName("user_token")
        private String userToken;

        @SerializedName("book_id")
        private long book_id;

        public GetPageRequest(String userToken, long book_id) {
            this.userToken = userToken;
            this.book_id = book_id;
        }
    }

    public static class SetPageRespond {
        @SerializedName("status")
        private int status;

        @SerializedName("detail")
        private String detail;

        public int getStatus() {
            return status;
        }

        public String getDetail() {
            return detail;
        }
    }

    public static class GetPageRespond {
        @SerializedName("status")
        private int status;

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        GetPageData data;

        public int getStatus() {
            return status;
        }

        public String getDetail() {
            return detail;
        }

        public GetPageData getData() {
            return data;
        }
    }

    public class GetPageData {
        @SerializedName("last_page")
        private int lastPage;

        public int getLastPage() {
            return lastPage;
        }
    }

}

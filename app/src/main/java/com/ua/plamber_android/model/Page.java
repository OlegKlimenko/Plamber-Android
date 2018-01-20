package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Page {
    public static class SetPageRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("user_token")
        private String userToken;

        @SerializedName("book_id")
        private long book_id;

        @SerializedName("current_page")
        private int currentPage;

        public SetPageRequest(String appKey, String userToken, long book_id, int currentPage) {
            this.appKey = appKey;
            this.userToken = userToken;
            this.book_id = book_id;
            this.currentPage = currentPage;
        }
    }

    public static class GetPageRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("user_token")
        private String userToken;

        @SerializedName("book_id")
        private long book_id;

        public GetPageRequest(String appKey, String userToken, long book_id) {
            this.appKey = appKey;
            this.userToken = userToken;
            this.book_id = book_id;
        }
    }

    public static class SetPageRespond {
        @SerializedName("detail")
        private String detail;


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
        PageData data;

        public int getStatus() {
            return status;
        }

        public String getDetail() {
            return detail;
        }

        public PageData getData() {
            return data;
        }
    }

    public class PageData {
        @SerializedName("last_page")
        private int lastPage;

        @SerializedName("last_read")
        private String lastReadData;

        public int getLastPage() {
            return lastPage;
        }

        public String getLastReadData() {
            return lastReadData;
        }
    }

}

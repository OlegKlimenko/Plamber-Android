package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoadMoreBook {

    public static class LoadMoreRequestCategory {

        @SerializedName("user_token")
        private String userToken;

        @SerializedName("page")
        private int pageNumber;

        @SerializedName("category_id")
        private long categoryID;

        public LoadMoreRequestCategory(String userToken, int pageNumber, long categoryID) {
            this.userToken = userToken;
            this.pageNumber = pageNumber;
            this.categoryID = categoryID;
        }
    }

    public static class LoadMoreRequestSearch {

        @SerializedName("user_token")
        private String userToken;

        @SerializedName("search_term")
        private String term;

        @SerializedName("page")
        private int page;

        public LoadMoreRequestSearch (String userToken, String term, int page) {
            this.userToken = userToken;
            this.term = term;
            this.page = page;
        }
    }

    public class LoadMoreBookRespond {
        @SerializedName("status")
        private int status;

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private LoadMoreBookData data;

        public int getStatus() {
            return status;
        }

        public String getDetail() {
            return detail;
        }

        public LoadMoreBookData getData() {
            return data;
        }
    }

    public class LoadMoreBookData {

        @SerializedName("books")
        private List<Book.BookData> bookData;

        @SerializedName("next_page")
        private int nextPageNumber;

        public List<Book.BookData> getBookData() {
            return bookData;
        }

        public int getNextPageNumber() {
            return nextPageNumber;
        }
    }
}

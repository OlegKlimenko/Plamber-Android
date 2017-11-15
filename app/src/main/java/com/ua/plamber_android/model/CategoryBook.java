package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryBook {

    public static class CategoryBookRequest {

        @SerializedName("user_token")
        private String userToken;

        @SerializedName("page")
        private int pageNumber;

        @SerializedName("category_id")
        private long categoryID;

        public CategoryBookRequest(String userToken, int pageNumber, long categoryID) {
            this.userToken = userToken;
            this.pageNumber = pageNumber;
            this.categoryID = categoryID;
        }
    }

    public class CategoryBookRespond {
        @SerializedName("status")
        private int status;

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private CategoryBookData data;

        public int getStatus() {
            return status;
        }

        public String getDetail() {
            return detail;
        }

        public CategoryBookData getData() {
            return data;
        }
    }

    public class CategoryBookData {

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

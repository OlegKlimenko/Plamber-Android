package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Library {

    public static class LibraryRequest {
        @SerializedName("user_token")
        private String userToken;

        public LibraryRequest(String userToken) {
            this.userToken = userToken;
        }
    }

    public static class LibraryRespond {
        @SerializedName("status")
        private int status;

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private List<LibraryData> libraryData;

        public int getStatus() {
            return status;
        }

        public String getDetail() {
            return detail;
        }

        public List<LibraryData> getLibraryData() {
            return libraryData;
        }
    }

    public class LibraryData {
        @SerializedName("id")
        private long id;

        @SerializedName("category_name")
        private String categoryName;

        @SerializedName("url")
        private String url;

        public long getId() {
            return id;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getUrl() {
            return url;
        }
    }

}
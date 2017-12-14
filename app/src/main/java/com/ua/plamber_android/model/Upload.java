package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Upload {

    public static class UploadRequest {
        @SerializedName("user_token")
        String userToken;

        @SerializedName("book_name")
        String bookName;

        @SerializedName("author")
        String authorName;

        @SerializedName("category")
        String categoryName;

        @SerializedName("about")
        String aboutBook;

        @SerializedName("language")
        String languageBook;

        @SerializedName("private_book")
        boolean isPrivateBook;

        public String getUserToken() {
            return userToken;
        }

        public String getBookName() {
            return bookName;
        }

        public String getAuthorName() {
            return authorName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getAboutBook() {
            return aboutBook;
        }

        public String getLanguageBook() {
            return languageBook;
        }

        public boolean isPrivateBook() {
            return isPrivateBook;
        }
    }

    public static class UploadRespond {
        @SerializedName("status")
        int status;

        @SerializedName("detail")
        String detail;

        @SerializedName("data")
       UploadData data;

        public int getStatus() {
            return status;
        }

        public String getDetail() {
            return detail;
        }

        public UploadData getData() {
            return data;
        }
    }

    private class UploadData {

    }


}

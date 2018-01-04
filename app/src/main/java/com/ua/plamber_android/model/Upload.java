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

        @SerializedName("category_name")
        String categoryName;

        @SerializedName("book_path")
        String bookPath;

        @SerializedName("book_about")
        String bookAbout;

        @SerializedName("book_language")
        String bookLanguage;

        @SerializedName("private_book")
        boolean isPrivateBook;

        public UploadRequest(String userToken, String bookName, String authorName, String categoryName, String bookPath, String aboutBook, String languageBook, boolean isPrivateBook) {
            this.userToken = userToken;
            this.bookName = bookName;
            this.authorName = authorName;
            this.categoryName = categoryName;
            this.bookPath = bookPath;
            this.bookAbout = aboutBook;
            this.bookLanguage = languageBook;
            this.isPrivateBook = isPrivateBook;
        }

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

        public String getBookPath() {
            return bookPath;
        }

        public String getAboutBook() {
            return bookAbout;
        }

        public String getLanguageBook() {
            return bookLanguage;
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

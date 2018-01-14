package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Upload {

    public static class UploadBookRequest {
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

        public UploadBookRequest(String userToken, String bookName, String authorName, String categoryName, String bookPath, String aboutBook, String languageBook, boolean isPrivateBook) {
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

    public static class UploadBookRespond {

        @SerializedName("detail")
        String detail;

        @SerializedName("data")
        UploadBookData data;

        public String getDetail() {
            return detail;
        }

        public UploadBookData getData() {
            return data;
        }
    }

    private class UploadBookData {

    }

    public static class UploadAvatarRequest {
        @SerializedName("user_token")
        String userToken;
    }

    public static class UploadAvatarRespond {
        @SerializedName("detail")
        String detail;

        @SerializedName("data")
        UploadAvatarData data;


        public String getDetail() {
            return detail;
        }

        public UploadAvatarData getData() {
            return data;
        }
    }

    public class UploadAvatarData {
        @SerializedName("profile_image")
        String avatarUrl;

        public String getAvatarUrl() {
            return avatarUrl;
        }
    }

}

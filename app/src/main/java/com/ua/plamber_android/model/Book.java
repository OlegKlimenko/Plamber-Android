package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Book {

    public static class BookRequest {
        @SerializedName("user_token")
        private String userToken;

        public BookRequest(String userToken) {
            this.userToken = userToken;
        }
    }


    public class BookRespond {
        @SerializedName("status")
        private String status;

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private List<BookData> bookData;

        public String getStatus() {
            return status;
        }

        public String getDetail() {
            return detail;
        }

        public List<BookData> getBookData() {
            return bookData;
        }
    }

    public class BookData {
        @SerializedName("book_name")
        private String bookName;

        @SerializedName("id_author")
        private String idAuthor;

        @SerializedName("id_category")
        private String idCategory;

        @SerializedName("description")
        private String description;

        @SerializedName("language")
        private String language;

        @SerializedName("photo")
        private String photo;

        @SerializedName("book_file")
        private String bookFile;

        @SerializedName("who_added")
        private String whoAdded;

        @SerializedName("upload_date")
        private String uploadDate;

        @SerializedName("private_book")
        private boolean isprivatBeook;

        public String getBookName() {
            return bookName;
        }

        public String getIdAuthor() {
            return idAuthor;
        }

        public String getIdCategory() {
            return idCategory;
        }

        public String getDescription() {
            return description;
        }

        public String getLanguage() {
            return language;
        }

        public String getPhoto() {
            return photo;
        }

        public String getBookFile() {
            return bookFile;
        }

        public String getWhoAdded() {
            return whoAdded;
        }

        public String getUploadDate() {
            return uploadDate;
        }

        public boolean isprivatBeook() {
            return isprivatBeook;
        }
    }
}

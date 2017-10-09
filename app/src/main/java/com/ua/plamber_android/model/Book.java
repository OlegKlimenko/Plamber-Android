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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BookData bookData = (BookData) o;

            if (isprivatBeook != bookData.isprivatBeook) return false;
            if (bookName != null ? !bookName.equals(bookData.bookName) : bookData.bookName != null)
                return false;
            if (idAuthor != null ? !idAuthor.equals(bookData.idAuthor) : bookData.idAuthor != null)
                return false;
            if (idCategory != null ? !idCategory.equals(bookData.idCategory) : bookData.idCategory != null)
                return false;
            if (description != null ? !description.equals(bookData.description) : bookData.description != null)
                return false;
            if (language != null ? !language.equals(bookData.language) : bookData.language != null)
                return false;
            if (photo != null ? !photo.equals(bookData.photo) : bookData.photo != null)
                return false;
            if (bookFile != null ? !bookFile.equals(bookData.bookFile) : bookData.bookFile != null)
                return false;
            if (whoAdded != null ? !whoAdded.equals(bookData.whoAdded) : bookData.whoAdded != null)
                return false;
            return uploadDate != null ? uploadDate.equals(bookData.uploadDate) : bookData.uploadDate == null;

        }

        @Override
        public int hashCode() {
            int result = bookName != null ? bookName.hashCode() : 0;
            result = 31 * result + (idAuthor != null ? idAuthor.hashCode() : 0);
            result = 31 * result + (idCategory != null ? idCategory.hashCode() : 0);
            result = 31 * result + (description != null ? description.hashCode() : 0);
            result = 31 * result + (language != null ? language.hashCode() : 0);
            result = 31 * result + (photo != null ? photo.hashCode() : 0);
            result = 31 * result + (bookFile != null ? bookFile.hashCode() : 0);
            result = 31 * result + (whoAdded != null ? whoAdded.hashCode() : 0);
            result = 31 * result + (uploadDate != null ? uploadDate.hashCode() : 0);
            result = 31 * result + (isprivatBeook ? 1 : 0);
            return result;
        }
    }
}

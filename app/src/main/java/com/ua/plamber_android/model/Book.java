package com.ua.plamber_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Book {

    public static class BookRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("user_token")
        private String userToken;

        public BookRequest(String appKey, String userToken) {
            this.appKey = appKey;
            this.userToken = userToken;
        }
    }


    public class BookRespond {

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private List<BookData> bookData;

        public String getDetail() {
            return detail;
        }

        public List<BookData> getBookData() {
            return bookData;
        }
    }

    public static class BookDetailRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("user_token")
        private String userToken;

        @SerializedName("book_id")
        private long idBook;

        public BookDetailRequest(String appKey, String userToken, long idBook) {
            this.appKey = appKey;
            this.userToken = userToken;
            this.idBook = idBook;
        }
    }

    public class BookDetailRespond {
        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private BookDetailData data;

        public String getDetail() {
            return detail;
        }

        public BookDetailData getData() {
            return data;
        }
    }

    public static class BookDetailData {
        @SerializedName("book_rating")
        private double bookRating;

        @SerializedName("is_added_book")
        private boolean isAddedBook;

        @SerializedName("book_rated_count")
        private int countBookRated;

        @SerializedName("comments")
        private List<Comment.CommentData> commentData;

        @SerializedName("book")
        private BookData bookData;

        @SerializedName("user_reading_count")
        private int countUserReading;

        public double getBookRating() {
            return bookRating;
        }

        public boolean isAddedBook() {
            return isAddedBook;
        }

        public int getCountBookRated() {
            return countBookRated;
        }

        public List<Comment.CommentData> getCommentData() {
            return commentData;
        }

        public BookData getBookData() {
            return bookData;
        }

        public int getCountUserReading() {
            return countUserReading;
        }

        public void setAddedBook(boolean addedBook) {
            isAddedBook = addedBook;
        }
    }

    public static class BookData {
        @Expose(serialize = false)
        private String idBook;

        @SerializedName("id")
        private long idServerBook;

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
        private boolean isPrivatBook;

        @SerializedName("url")
        private String bookUrl;

        @Expose
        private boolean isOfflineBook;

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

        public String getBookUrl() {
            return bookUrl;
        }

        public long getIdServerBook() {
            return idServerBook;
        }

        public boolean isPrivatBook() {
            return isPrivatBook;
        }

        public void setIdServerBook(long idServerBook) {
            this.idServerBook = idServerBook;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public void setIdAuthor(String idAuthor) {
            this.idAuthor = idAuthor;
        }

        public void setIdCategory(String idCategory) {
            this.idCategory = idCategory;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setBookFile(String bookFile) {
            this.bookFile = bookFile;
        }

        public void setWhoAdded(String whoAdded) {
            this.whoAdded = whoAdded;
        }

        public void setUploadDate(String uploadDate) {
            this.uploadDate = uploadDate;
        }

        public void setPrivatBook(boolean privatBook) {
            isPrivatBook = privatBook;
        }

        public void setBookUrl(String bookUrl) {
            this.bookUrl = bookUrl;
        }

        public String getIdBook() {
            return idBook;
        }

        public void setIdBook(String idBook) {
            this.idBook = idBook;
        }

        public boolean isOfflineBook() {
            return isOfflineBook;
        }

        public void setOfflineBook(boolean offlineBook) {
            isOfflineBook = offlineBook;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BookData bookData = (BookData) o;

            if (idServerBook != bookData.idServerBook) return false;
            if (isPrivatBook != bookData.isPrivatBook) return false;
            if (isOfflineBook != bookData.isOfflineBook) return false;
            if (idBook != null ? !idBook.equals(bookData.idBook) : bookData.idBook != null)
                return false;
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
            if (uploadDate != null ? !uploadDate.equals(bookData.uploadDate) : bookData.uploadDate != null)
                return false;
            return bookUrl != null ? bookUrl.equals(bookData.bookUrl) : bookData.bookUrl == null;
        }

        @Override
        public int hashCode() {
            int result = idBook != null ? idBook.hashCode() : 0;
            result = 31 * result + (int) (idServerBook ^ (idServerBook >>> 32));
            result = 31 * result + (bookName != null ? bookName.hashCode() : 0);
            result = 31 * result + (idAuthor != null ? idAuthor.hashCode() : 0);
            result = 31 * result + (idCategory != null ? idCategory.hashCode() : 0);
            result = 31 * result + (description != null ? description.hashCode() : 0);
            result = 31 * result + (language != null ? language.hashCode() : 0);
            result = 31 * result + (photo != null ? photo.hashCode() : 0);
            result = 31 * result + (bookFile != null ? bookFile.hashCode() : 0);
            result = 31 * result + (whoAdded != null ? whoAdded.hashCode() : 0);
            result = 31 * result + (uploadDate != null ? uploadDate.hashCode() : 0);
            result = 31 * result + (isPrivatBook ? 1 : 0);
            result = 31 * result + (bookUrl != null ? bookUrl.hashCode() : 0);
            result = 31 * result + (isOfflineBook ? 1 : 0);
            return result;
        }
    }
}

package com.ua.plamber_android.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BookDB extends RealmObject {
    @PrimaryKey
    private String idBook;

    private long idServerBook;

    private String bookName;

    private String idAuthor;

    private String idCategory;

    private String description;

    private String language;

    private String photo;

    private String whoAdded;

    private String uploadDate;

    private int bookPage;

    private String lastReadDate;

    private boolean isOfflineBook;

    public String getIdBook() {
        return idBook;
    }

    public void setIdBook(String idBook) {
        this.idBook = idBook;
    }

    public long getIdServerBook() {
        return idServerBook;
    }

    public void setIdServerBook(long idServerBook) {
        this.idServerBook = idServerBook;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(String idAuthor) {
        this.idAuthor = idAuthor;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getWhoAdded() {
        return whoAdded;
    }

    public void setWhoAdded(String whoAdded) {
        this.whoAdded = whoAdded;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean isOfflineBook() {
        return isOfflineBook;
    }

    public void setOfflineBook(boolean offlineBook) {
        isOfflineBook = offlineBook;
    }

    public int getBookPage() {
        return bookPage;
    }

    public void setBookPage(int bookPage) {
        this.bookPage = bookPage;
    }

    public String getLastReadDate() {
        return lastReadDate;
    }

    public void setLastReadDate(String lastReadDate) {
        this.lastReadDate = lastReadDate;
    }
}

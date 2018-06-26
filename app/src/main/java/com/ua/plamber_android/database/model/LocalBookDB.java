package com.ua.plamber_android.database.model;

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LocalBookDB extends RealmObject {
    @PrimaryKey
    private String idBook;

    private String bookName;

    private String bookPath;

    private String bookAvatar;

    private int lastPage;

    private long lastReadDate;

    public long getLastReadDate() {
        return lastReadDate;
    }

    public void setLastReadDate(long lastReadDate) {
        this.lastReadDate = lastReadDate;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public String getBookAvatar() {
        return bookAvatar;
    }

    public void setBookAvatar(String bookAvatar) {
        this.bookAvatar = bookAvatar;
    }

    public String getIdBook() {
        return idBook;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }
}

package com.ua.plamber_android.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PageDB extends RealmObject {
    @PrimaryKey
    private long idBook;

    private int bookPage;

    private String lastRead;

    public long getIdBook() {
        return idBook;
    }

    public void setIdBook(long idBook) {
        this.idBook = idBook;
    }

    public int getBookPage() {
        return bookPage;
    }

    public void setBookPage(int bookPage) {
        this.bookPage = bookPage;
    }

    public String getLastRead() {
        return lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }
}

package com.ua.plamber_android.api.download;

import android.net.Uri;

import com.ua.plamber_android.model.Book;

import java.io.File;

public class FastFileData {
    private File file;
    private String fileURL;
    private String fileName;
    private Book.BookDetailData bookData;
    private String id;

    public FastFileData(File file, String fileURL, String fileName) {
        this.file = file;
        this.fileURL = fileURL;
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Book.BookDetailData getBookData() {
        return bookData;
    }

    public void setBookData(Book.BookDetailData bookData) {
        this.bookData = bookData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

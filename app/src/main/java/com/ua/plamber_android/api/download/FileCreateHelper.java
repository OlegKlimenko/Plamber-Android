package com.ua.plamber_android.api.download;

import com.ua.plamber_android.model.Book;

import java.io.File;

public class FileCreateHelper {
    private File file;
    private String fileURL;
    private String fileName;
    private Book.BookDetailData bookDetailData;

    public FileCreateHelper(File file, String fileURL, String fileName) {
        this.file = file;
        this.fileURL = fileURL;
        this.fileName = fileName;
    }

    public FileCreateHelper(File file, String fileURL, String fileName, Book.BookDetailData userFile) {
        this.file = file;
        this.fileURL = fileURL;
        this.fileName = fileName;
        this.bookDetailData = userFile;
    }

    public FileCreateHelper(FileCreateHelper file) {
        this.file = file.getFile();
        this.fileURL = file.getFileURL();
        this.fileName = file.getFileName();
        this.bookDetailData = file.bookDetailData;
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


    public Book.BookDetailData getDetailData() {
        return bookDetailData;
    }
}

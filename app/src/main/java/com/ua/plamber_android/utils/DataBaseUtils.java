package com.ua.plamber_android.utils;

import android.content.Context;
import android.util.Log;

import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.BookDataBase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DataBaseUtils {
    Context context;
    Utils utils;
    public static final String TAG = "DataBaseUtils";

    public DataBaseUtils(Context context) {
        this.context = context;
        Realm.init(context);
        utils = new Utils(context);
    }

    public void writeBookToDataBase(Book.BookData book) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDataBase data = realm.createObject(BookDataBase.class, book.getIdBook());
        data.setBookName(book.getBookName());
        data.setIdAuthor(book.getIdAuthor());
        data.setIdCategory(book.getIdCategory());
        data.setDescription(book.getDescription());
        data.setLanguage(book.getLanguage());
        data.setPhoto(book.getPhoto());
        data.setWhoAdded(book.getWhoAdded());
        data.setUploadDate(book.getUploadDate());
        realm.commitTransaction();
    }

    public Book.BookData readBookFromDataBase(long id) {
        BookDataBase book;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<BookDataBase> results = realm.where(BookDataBase.class).equalTo("idBook", id).findAll();
        book = results.first();
        realm.commitTransaction();
        return convertOffline(book);
    }

    public void removeFromDatabase(long id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<BookDataBase> results = realm.where(BookDataBase.class).equalTo("idBook", id).findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public List<Book.BookData> getBookDataList() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<BookDataBase> offlineBooks = new ArrayList<>();
        offlineBooks.addAll(realm.where(BookDataBase.class).findAll());
        realm.commitTransaction();
        return deleteExistBooks(convertOfflineList(offlineBooks));
    }

    private List<Book.BookData> convertOfflineList(List<BookDataBase> books) {
        List<Book.BookData> modelBooks = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            modelBooks.add(convertOffline(books.get(i)));
        }
        return modelBooks;
    }

    private Book.BookData convertOffline(BookDataBase bookDataBase) {
        Book.BookData bookData = new Book.BookData();
        bookData.setIdBook(bookDataBase.getIdBook());
        bookData.setBookName(bookDataBase.getBookName());
        bookData.setIdAuthor(bookDataBase.getIdAuthor());
        bookData.setIdCategory(bookDataBase.getIdCategory());
        bookData.setDescription(bookDataBase.getDescription());
        bookData.setLanguage(bookDataBase.getLanguage());
        bookData.setPhoto(bookDataBase.getPhoto());
        bookData.setWhoAdded(bookDataBase.getWhoAdded());
        bookData.setUploadDate(bookDataBase.getUploadDate());
        return bookData;
    }

    private List<Book.BookData> deleteExistBooks(List<Book.BookData> books) {
        List<Book.BookData> booksBase = new ArrayList<>(books);
        for (int i = 0; i < booksBase.size(); i++) {
            File file = new File(utils.getFullFileName(booksBase.get(i).getBookName()));
            if (!file.exists()) {
                removeFromDatabase(booksBase.get(i).getIdBook());
                booksBase.remove(i);
            }
        }
        deleteFile(books);
        return booksBase;
    }

    private void deleteFile(List<Book.BookData> books) {
        File plamberPath = new File(utils.getPlamberPath() + File.separator);
        File[] files = plamberPath.listFiles();
        List<String> bookName = new ArrayList<>();
        for (Book.BookData book : books) {
            bookName.add(utils.getNamePDF(book.getBookName()));
        }
        if (books.size() == 0 && files.length != 0) {
            for (File file : files) {
                file.delete();
            }
        } else {
            for (File file : files) {
                if (!bookName.contains(file.getName())) {
                    file.delete();
                }
            }
        }
    }

}


package com.ua.plamber_android.database.utils;

import android.content.Context;

import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.database.model.BookDB;
import com.ua.plamber_android.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class BookUtilsDB {
    private Context context;
    private Utils utils;
    public static final String TAG = "BookUtilsDB";

    public BookUtilsDB(Context context) {
        this.context = context;
        Realm.init(context);
        utils = new Utils(context);
    }

    public void writeBookToDataBase(Book.BookData book) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDB data = realm.createObject(BookDB.class, book.getIdBook());
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

    public Book.BookData readBookFromDB(long id) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<BookDB> results = realm.where(BookDB.class).equalTo("idBook", id).findAll();
        BookDB book = results.first();
        realm.commitTransaction();
        return convertBookForDB(book);
    }

    public void removeBookFromDatabase(long id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<BookDB> results = realm.where(BookDB.class).equalTo("idBook", id).findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public List<Book.BookData> getListBookFromDB() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<BookDB> offlineBooks = new ArrayList<>();
        offlineBooks.addAll(realm.where(BookDB.class).findAll());
        realm.commitTransaction();
        return deleteBookFromDBFileExist(convertBookListForDB(offlineBooks));
    }

    private List<Book.BookData> convertBookListForDB(List<BookDB> books) {
        List<Book.BookData> modelBooks = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            modelBooks.add(convertBookForDB(books.get(i)));
        }
        return modelBooks;
    }

    private Book.BookData convertBookForDB(BookDB bookDB) {
        Book.BookData bookData = new Book.BookData();
        bookData.setIdBook(bookDB.getIdBook());
        bookData.setBookName(bookDB.getBookName());
        bookData.setIdAuthor(bookDB.getIdAuthor());
        bookData.setIdCategory(bookDB.getIdCategory());
        bookData.setDescription(bookDB.getDescription());
        bookData.setLanguage(bookDB.getLanguage());
        bookData.setPhoto(bookDB.getPhoto());
        bookData.setWhoAdded(bookDB.getWhoAdded());
        bookData.setUploadDate(bookDB.getUploadDate());
        return bookData;
    }

    private List<Book.BookData> deleteBookFromDBFileExist(List<Book.BookData> books) {
        List<Book.BookData> booksBase = new ArrayList<>(books);
        for (int i = 0; i < booksBase.size(); i++) {
            File file = new File(utils.getFullFileName(booksBase.get(i).getBookName()));
            if (!file.exists()) {
                removeBookFromDatabase(booksBase.get(i).getIdBook());
                booksBase.remove(i);
            }
        }
        deleteFileFromFolderExistDB(books);
        return booksBase;
    }

    private void deleteFileFromFolderExistDB(List<Book.BookData> books) {
        File plamberPath = new File(utils.getPlamberPath() + File.separator);
        File[] files = plamberPath.listFiles();
        List<String> bookName = new ArrayList<>();
        for (Book.BookData book : books) {
            bookName.add(utils.getNamePDF(book.getBookName()));
        }
        if (books.isEmpty() && files.length != 0) {
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


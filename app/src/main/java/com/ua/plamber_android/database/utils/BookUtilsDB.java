package com.ua.plamber_android.database.utils;

import android.content.Context;

import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.database.model.BookDB;
import com.ua.plamber_android.utils.Utils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class BookUtilsDB {
    private Utils utils;
    public static final String TAG = "BookUtilsDB";
    private static final String datePattern = "yyyy-MM-dd'T'HH:mm:ss";

    public BookUtilsDB(Context context) {
        Realm.init(context);
        utils = new Utils(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(RealmConst.REALM_VERSION)
                .migration(new MigrationLocalBook())
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public int readLastPage(String id) {
        int page = 0;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDB result = realm.where(BookDB.class).equalTo("idBook", id).findFirst();
        if (result != null)
            page = result.getBookPage();
        realm.commitTransaction();
        return page;
    }

    public boolean isBookSaveDB(long id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDB result = realm.where(BookDB.class).equalTo("idServerBook", id).findFirst();
        realm.commitTransaction();
        return result != null;
    }

    public String readLastDate(String id) {
        String date = null;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDB result = realm.where(BookDB.class).equalTo("idBook", id).findFirst();
        if (result != null)
            date = result.getLastReadDate();
        realm.commitTransaction();
        return date;
    }

    public void updatePage(String id, int page) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDB result = realm.where(BookDB.class).equalTo("idBook", id).findFirst();
        if (result != null)
            result.setBookPage(page);
        realm.commitTransaction();
    }

    public void updateLastReadDate(String id, String date) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDB result = realm.where(BookDB.class).equalTo("idBook", id).findFirst();
        if (result != null)
            result.setLastReadDate(date);
        realm.commitTransaction();
    }

    public void writeBookToDataBase(String bookId, Book.BookData book) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDB data = realm.createObject(BookDB.class, bookId);
        data.setIdServerBook(book.getIdServerBook());
        data.setBookName(book.getBookName());
        data.setIdAuthor(book.getIdAuthor());
        data.setIdCategory(book.getIdCategory());
        data.setDescription(book.getDescription());
        data.setLanguage(book.getLanguage());
        data.setPhoto(book.getPhoto());
        data.setWhoAdded(book.getWhoAdded());
        data.setUploadDate(book.getUploadDate());
        data.setOfflineBook(false);
        realm.commitTransaction();
    }

    public void updateOfflineBook(String id, Book.BookData bookData) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDB result = realm.where(BookDB.class).equalTo("idBook", id).findFirst();
        if (result != null) {
            result.setIdServerBook(bookData.getIdServerBook());
            result.setBookName(bookData.getBookName());
            result.setIdAuthor(bookData.getIdAuthor());
            result.setIdCategory(bookData.getIdCategory());
            result.setDescription(bookData.getDescription());
            result.setLanguage(bookData.getLanguage());
            result.setPhoto(bookData.getPhoto());
            result.setWhoAdded(bookData.getWhoAdded());
            result.setUploadDate(bookData.getUploadDate());
            result.setOfflineBook(false);
        }
        realm.commitTransaction();
    }

    public void saveBookOffline(String bookId, String bookName) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDB data = realm.createObject(BookDB.class, bookId);
        data.setBookName(bookName);
        data.setOfflineBook(true);
        realm.commitTransaction();
    }

    public Book.BookData readBookFromDB(String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDB result = realm.where(BookDB.class).equalTo("idBook", id).findFirst();
        realm.commitTransaction();
        return convertBookForAdapter(result);
    }

    public void removeBookFromDatabase(String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<BookDB> results = realm.where(BookDB.class).equalTo("idBook", id).findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public String getBookPrimaryKey(long id) {
        String primaryKey = null;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookDB result = realm.where(BookDB.class).equalTo("idServerBook", id).findFirst();
        if (result != null)
            primaryKey = result.getIdBook();
        realm.commitTransaction();
        return primaryKey;
    }

    public List<Book.BookData> getListBookFromDB() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<BookDB> offlineBooks = new ArrayList<>(realm.where(BookDB.class).findAll());
        realm.commitTransaction();
        Collections.sort(offlineBooks, (o1, o2) -> o2.getLastReadDate().compareTo(o1.getLastReadDate()));
        return convertBookListForDB(offlineBooks);
    }

    private List<Book.BookData> convertBookListForDB(List<BookDB> books) {
        List<Book.BookData> modelBooks = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            modelBooks.add(convertBookForAdapter(books.get(i)));
        }
        return modelBooks;
    }

    private Book.BookData convertBookForAdapter(BookDB bookDB) {
        Book.BookData bookData = new Book.BookData();
        bookData.setIdBook(bookDB.getIdBook());
        bookData.setIdServerBook(bookDB.getIdServerBook());
        bookData.setBookName(bookDB.getBookName());
        bookData.setIdAuthor(bookDB.getIdAuthor());
        bookData.setIdCategory(bookDB.getIdCategory());
        bookData.setDescription(bookDB.getDescription());
        bookData.setLanguage(bookDB.getLanguage());
        bookData.setPhoto(bookDB.getPhoto());
        bookData.setWhoAdded(bookDB.getWhoAdded());
        bookData.setUploadDate(bookDB.getUploadDate());
        bookData.setOfflineBook(bookDB.isOfflineBook());
        return bookData;
    }

    private List<Book.BookData> deleteBookFromDBFileExist(List<Book.BookData> books) {
        List<Book.BookData> booksBase = new ArrayList<>(books);
        for (int i = 0; i < booksBase.size(); i++) {
            File file = new File(utils.getPdfFileWithPath(getBookPrimaryKey(booksBase.get(i).getIdServerBook())));
            if (!file.exists()) {
                removeBookFromDatabase(getBookPrimaryKey(booksBase.get(i).getIdServerBook()));
                booksBase.remove(i);
            }
        }
        //deleteFileFromFolderExistDB(books);
        return booksBase;
    }

    private void deleteFileFromFolderExistDB(List<Book.BookData> books) {
        File plamberPath = new File(utils.getStoragePdfPath());
        File[] files = plamberPath.listFiles();
        List<String> bookName = new ArrayList<>();
        for (Book.BookData book : books) {
            bookName.add(utils.getNameWithPDF(getBookPrimaryKey(book.getIdServerBook())));
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

    public String getCurrentTime() {
        DateFormat format = new SimpleDateFormat(datePattern, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date());
    }

    public Date convertStringToDate(String date) {
        DateFormat format = new SimpleDateFormat(datePattern, Locale.US);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public String convertDateToString(Date date) {
        DateFormat format = new SimpleDateFormat(datePattern, Locale.US);
        return format.format(date);
    }

    public void deleteAllFromDB() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<BookDB> results = realm.where(BookDB.class).findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }
}


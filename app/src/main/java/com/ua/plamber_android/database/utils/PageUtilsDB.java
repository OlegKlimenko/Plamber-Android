package com.ua.plamber_android.database.utils;

import android.content.Context;

import com.ua.plamber_android.database.model.PageDB;

import io.realm.Realm;
import io.realm.RealmResults;

public class PageUtilsDB {
    private Context context;
    private long bookId;
    public static final String TAG = "PageUtilsDB";

    public PageUtilsDB(Context context, long bookId) {
        this.context = context;
        this.bookId = bookId;
        Realm.init(context);
    }

    public void createPageData(int page) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        PageDB data = realm.createObject(PageDB.class, bookId);
        data.setBookPage(page);
        data.setReadOffline(false);
        realm.commitTransaction();
    }

    public PageDB readPageData() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<PageDB> result = realm.where(PageDB.class).equalTo("idBook", bookId).findAll();
        PageDB page = result.first();
        realm.commitTransaction();
        return page;
    }

    public void removePageData() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<PageDB> result = realm.where(PageDB.class).equalTo("idBook", bookId).findAll();
        result.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void updatePage(int page) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        PageDB result = realm.where(PageDB.class).equalTo("idBook", bookId).findFirst();
        if (result != null)
            result.setBookPage(page);
        realm.commitTransaction();
    }

    public void updateReadStatus(boolean status) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        PageDB result = realm.where(PageDB.class).equalTo("idBook", bookId).findFirst();
        if (result != null)
            result.setReadOffline(status);
        realm.commitTransaction();
    }

    public boolean isBookCreate() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        PageDB result = realm.where(PageDB.class).equalTo("idBook", bookId).findFirst();
        if (result == null) {
            realm.commitTransaction();
            return false;
        } else {
            realm.commitTransaction();
            return true;
        }
    }
}

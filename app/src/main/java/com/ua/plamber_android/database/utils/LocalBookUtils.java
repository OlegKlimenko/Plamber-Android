package com.ua.plamber_android.database.utils;

import android.content.Context;

import com.ua.plamber_android.database.model.LocalBookDB;
import com.ua.plamber_android.utils.Utils;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LocalBookUtils {

    public LocalBookUtils(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new MigrationLocalBook())
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public String getIdLocalBook(String path) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LocalBookDB result = realm.where(LocalBookDB.class).equalTo("bookPath", path).findFirst();
        realm.commitTransaction();
        return result != null ? result.getIdBook() : "";
    }

    public boolean isLocalBookSaveDB(String bookPath) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LocalBookDB result = realm.where(LocalBookDB.class).equalTo("bookPath", bookPath).findFirst();
        realm.commitTransaction();
        return result != null;
    }

    public int getLastLocalBookPage(String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LocalBookDB  result = realm.where(LocalBookDB.class).equalTo("idBook", id).findFirst();
        realm.commitTransaction();
        return result != null ? result.getLastPage() : 0;
    }

    public void updatePage(String id, int page) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LocalBookDB result = realm.where(LocalBookDB.class).equalTo("idBook", id).findFirst();
        if (result != null)
            result.setLastPage(page);
        realm.commitTransaction();
    }

    public String saveBookLocal(LocalBookDB bookDB) {
        String id = Utils.generateIdBook();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LocalBookDB data = realm.createObject(LocalBookDB.class, id);
        data.setBookName(bookDB.getBookName());
        data.setBookPath(bookDB.getBookPath());
        data.setBookAvatar(bookDB.getBookAvatar());
        realm.commitTransaction();
        return id;
    }
}

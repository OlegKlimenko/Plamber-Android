package com.ua.plamber_android.database.utils;

import android.content.Context;

import com.ua.plamber_android.database.model.CategoryDB;
import com.ua.plamber_android.model.Library;
import com.ua.plamber_android.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class CategoryDBUtils {

    public CategoryDBUtils(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(RealmConst.REALM_VERSION)
                .migration(new MigrationLocalBook())
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public void addCategoryToDB(List<Library.LibraryData> categories) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (Library.LibraryData category : categories) {
            CategoryDB cat = realm.createObject(CategoryDB.class, Utils.generateId());
            cat.setCategoryId(category.getId());
            cat.setCategoryName(category.getCategoryName());
            cat.setCategoryUrl(category.getUrl());
        }
        realm.commitTransaction();
    }

    public boolean categoryIsSave() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<CategoryDB> categoryDB = new ArrayList<>(realm.where(CategoryDB.class).findAll());
        realm.commitTransaction();
        return categoryDB.size() > 20;
    }

    public void removeAllCategory() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<CategoryDB> results = realm.where(CategoryDB.class).findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public List<Library.LibraryData> getCategoriesFromDB() {
        List<Library.LibraryData> categorises = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<CategoryDB> categoryDB = new ArrayList<>(realm.where(CategoryDB.class).findAll());
        realm.commitTransaction();
        for (CategoryDB db : categoryDB) {
            categorises.add(new Library.LibraryData(db.getCategoryId(), db.getCategoryName(), db.getCategoryUrl()));
        }
        return categorises;
    }
}

package com.ua.plamber_android.database.utils;

import android.content.Context;

import com.ua.plamber_android.database.model.CategoryDB;
import com.ua.plamber_android.database.model.LanguageDB;
import com.ua.plamber_android.model.Language;
import com.ua.plamber_android.model.Library;
import com.ua.plamber_android.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LanguageDBUtils {

    public LanguageDBUtils(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(RealmConst.REALM_VERSION)
                .migration(new MigrationLocalBook())
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public void addLanguageToDB(List<String> languages) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (String s : languages) {
             LanguageDB lang = realm.createObject(LanguageDB.class, Utils.generateId());
             lang.setLanguageName(s);
        }
        realm.commitTransaction();
    }

    public boolean languageIsSave() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<LanguageDB> language = new ArrayList<>(realm.where(LanguageDB.class).findAll());
        realm.commitTransaction();
        return language.size() > 10;
    }

    public List<String> getLanguageFromDB() {
        List<String> languages = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<LanguageDB> lang = new ArrayList<>(realm.where(LanguageDB.class).findAll());
        realm.commitTransaction();
        for (LanguageDB l : lang) {
            languages.add(l.getLanguageName());
        }
        return languages;
    }
}

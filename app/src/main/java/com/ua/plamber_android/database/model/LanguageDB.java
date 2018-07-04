package com.ua.plamber_android.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LanguageDB extends RealmObject {
    @PrimaryKey
    private String id;

    private String languageName;

    public String getId() {
        return id;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
}

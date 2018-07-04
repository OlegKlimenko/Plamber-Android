package com.ua.plamber_android.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CategoryDB extends RealmObject {
    @PrimaryKey
    private String id;

    private long categoryId;

    private String categoryName;

    private String categoryUrl;

    public String getId() {
        return id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }
}

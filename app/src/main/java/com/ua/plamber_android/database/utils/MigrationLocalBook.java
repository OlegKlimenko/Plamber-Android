package com.ua.plamber_android.database.utils;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MigrationLocalBook implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            schema.create("LocalBookDB")
                    .addField("idBook", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("bookName", String.class)
                    .addField("bookPath", String.class)
                    .addField("bookAvatar", String.class)
                    .addField("lastPage", int.class)
                    .addField("lastReadDate", long.class);
            addCategoryAndLanguage(schema);
            oldVersion++;
            return;
        }

        if (oldVersion == 1) {
            schema.get("LocalBookDB")
                    .addField("lastReadDate", long.class);
            addCategoryAndLanguage(schema);
            oldVersion++;
            return;
        }

        if (oldVersion == 2)
            addCategoryAndLanguage(schema);
    }

    private void addCategoryAndLanguage(RealmSchema schema) {
        schema.create("CategoryDB")
                .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                .addField("categoryId", long.class)
                .addField("categoryName", String.class)
                .addField("categoryUrl", String.class);
        schema.create("LanguageDB")
                .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                .addField("languageName", String.class);
    }

    @Override
    public int hashCode() {
        return 38;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof MigrationLocalBook);
    }

    }

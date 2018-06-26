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
            oldVersion++;
            return;
        }

        if (oldVersion == 1) {
            schema.get("LocalBookDB")
                    .addField("lastReadDate", long.class);
            oldVersion++;
        }


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

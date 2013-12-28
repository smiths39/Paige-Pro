/*
    Name:   DBBookmark.java
    Author: Sean Smith
    Date:   28 December 2013

    This implements the SQLite Database used for the storing of saved bookmarks.
*/

package com.project.paigepro;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class DBBookmark {

    private DBBookmarkSupport storageHelper;
    private SQLiteDatabase database;
    private final Context context;

    public DBBookmark(Context newContext) {

        context = newContext;
    }

    public DBBookmark open() throws SQLException {

        storageHelper = new DBBookmarkSupport(context);
        database = storageHelper.getWritableDatabase();

        return DBBookmark.this;
    }

    public void close() {

        storageHelper.close();
    }

    public long createEntry(String name, String url) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBBookmarkSupport.KEY_NAME, name);
        contentValues.put(DBBookmarkSupport.KEY_URL, url);

        return database.insert(DBBookmarkSupport.DATABASE_TABLE, null, contentValues);
    }
}

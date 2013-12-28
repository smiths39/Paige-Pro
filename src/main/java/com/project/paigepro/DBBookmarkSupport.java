/*
    Name:   DBBookmarkSupport.java
    Author: Sean Smith
    Date:   28 December 2013

    This implements the structure of the SQLite Database assigned for the storing of saved bookmarks.
*/

package com.project.paigepro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBBookmarkSupport extends SQLiteOpenHelper {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_URL = "url";

    private static final String DATABASE_NAME = "BookmarkStorage";
    public static final String DATABASE_TABLE = "urlTable";

    public DBBookmarkSupport(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
                                KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                KEY_NAME + " TEXT NOT NULL, " +
                               KEY_URL + " TEXT NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
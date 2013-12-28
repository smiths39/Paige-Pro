/*
    Name:   DBMemo.java
    Author: Sean Smith
    Date:   28 December 2013

    This implements the SQLite Database used for the storing of saved memos.
*/

package com.project.paigepro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBMemo {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "memoTitle";
    public static final String KEY_DATE = "memoDate";
    public static final String KEY_CONTENT = "memoContent";

    public static final String DATABASE_NAME = "memoStorage";
    public static final String TABLE_NAME = "memoTable";
    public static final int DATABASE_VERSION = 1;

    private Context context;

    private String [] memoDetails = { KEY_ROWID, KEY_TITLE, KEY_CONTENT, KEY_DATE };

    private DBMemoSupport dbMemoSupport;
    private SQLiteDatabase database;

    private static class DBMemoSupport extends SQLiteOpenHelper {

        DBMemoSupport (Context newContext) {

            super(newContext, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE "
                        + TABLE_NAME + "("
                        + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + KEY_TITLE + " TEXT NOT NULL, "
                        + KEY_CONTENT + " TEXT NOT NULL, "
                        + KEY_DATE + " TEXT NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public DBMemo(Context newContext) {

        this.context = newContext;
    }

    public DBMemo open() throws SQLException {

        dbMemoSupport = new DBMemoSupport(context);
        database = dbMemoSupport.getWritableDatabase();

        return DBMemo.this;
    }

    public long createMemo(String title, String content, String date) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_CONTENT, content);
        initialValues.put(KEY_DATE, date);

        return database.insert(TABLE_NAME, null, initialValues);
    }

    public boolean deleteMemo(long id) {

        return database.delete(TABLE_NAME, KEY_ROWID + "=" + id, null) != 0;
    }

    public Cursor retrieveMemos() {

        return database.query(TABLE_NAME, memoDetails, null, null, null, null, null);
    }

    public Cursor getMemo(long id) throws SQLException {

        Cursor mCursor = database.query(true, TABLE_NAME, memoDetails, KEY_ROWID + "=" + id, null, null, null, null, null);

        if (mCursor != null) {

            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateMemo(long id, String title, String content, String date) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_CONTENT, content);
        contentValues.put(KEY_DATE, date);

        return database.update(TABLE_NAME, contentValues, KEY_ROWID + "=" + id, null) != 0;
    }
}
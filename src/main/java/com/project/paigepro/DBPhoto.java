/*
    Name:   DBPhoto.java
    Author: Sean Smith
    Date:   28 December 2013

    This implements the SQLite Database used for the storing of saved photos.
*/

package com.project.paigepro;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBPhoto extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PhotoStorage";
    private static final String TABLE_NAME = "photoTable";
    private static final int DATABASE_VERSION = 1;

    private static final String KEY_ID = "photoID";
    private static final String KEY_NAME = "photoName";
    private static final String KEY_IMAGE = "photoImage";

    public DBPhoto(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "
                   + TABLE_NAME + "("
                   + KEY_ID + " INTEGER PRIMARY KEY,"
                   + KEY_NAME + " TEXT,"
                   + KEY_IMAGE + " BLOB" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public void addPhotoStorage(PhotoStorage newPhoto) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, newPhoto.storageName);
        contentValues.put(KEY_IMAGE, newPhoto.storageImage);

        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public List<PhotoStorage> getAllContacts() {

        ArrayList<PhotoStorage> photoList = new ArrayList<PhotoStorage>();

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT  * FROM " + TABLE_NAME + " ORDER BY " + KEY_NAME, null);

        if (cursor.moveToFirst()) {

            do {
                PhotoStorage photoStorage = new PhotoStorage();
                photoStorage.setPhotoID(Integer.parseInt(cursor.getString(0)));
                photoStorage.setPhotoName(cursor.getString(1));
                photoStorage.setPhotoImage(cursor.getBlob(2));

                photoList.add(photoStorage);
            } while (cursor.moveToNext());
        }

        database.close();
        return photoList;
    }

    public void deleteContact(PhotoStorage photo) {

        String [] photoIDValue = { String.valueOf(photo.getPhotoID()) };
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, KEY_ID + " = ?", photoIDValue);
        database.close();
    }
}
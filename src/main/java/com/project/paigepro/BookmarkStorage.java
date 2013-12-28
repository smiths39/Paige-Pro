/*
    Name:   BookmarkStorage.java
    Author: Sean Smith
    Date:   28 December 2013

    All bookmarks currently stored within the SQLite Database are displayed within an interactive listview.
*/

package com.project.paigepro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookmarkStorage extends Activity {

    private DBBookmarkSupport databaseBookmark;
    private static SQLiteDatabase database;

    private ArrayList<String> bookmarkID = new ArrayList<String>();
    private ArrayList<String> bookmarkTitle = new ArrayList<String>();
    private ArrayList<String> bookmarkURL = new ArrayList<String>();

    private ListView bookmarkList;
    private AlertDialog.Builder alertBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmarkview);

        bookmarkList = (ListView) findViewById(R.id.lvBookmarks);
        databaseBookmark = new DBBookmarkSupport(this);

        bookmarkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {

                Intent launchIntent = new Intent(getApplicationContext(), LinkNew.class);
                launchIntent.putExtra("BookmarkLaunch", bookmarkURL.get(index));
                startActivity(launchIntent);
            }
        });

        bookmarkList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int index, long l) {

                alertBuild = new AlertDialog.Builder(BookmarkStorage.this);
                alertBuild.setTitle("Delete " + bookmarkTitle.get(index) + " " + bookmarkURL.get(index));
                alertBuild.setMessage("Do you want to delete bookmark?");

                alertBuild.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {

                        Toast.makeText(getApplicationContext(), bookmarkTitle.get(index) + " is deleted.", 4000).show();
                        database.delete(DBBookmarkSupport.DATABASE_TABLE, DBBookmarkSupport.KEY_ROWID + "=" + bookmarkID.get(index), null);

                        displayData();
                        dialogInterface.cancel();
                    }
                });

                alertBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });

                AlertDialog alert = alertBuild.create();
                alert.show();

                return true;
            }
        });
    }


    private void displayData() {

            database = databaseBookmark.getWritableDatabase();

            Cursor cursor = database.rawQuery("SELECT * FROM " + databaseBookmark.DATABASE_TABLE, null);

            bookmarkID.clear();
            bookmarkTitle.clear();
            bookmarkURL.clear();

            if (cursor.moveToFirst()) {

                do {
                    bookmarkID.add(cursor.getString(cursor.getColumnIndex(DBBookmarkSupport.KEY_ROWID)));
                    bookmarkTitle.add(cursor.getString(cursor.getColumnIndex(DBBookmarkSupport.KEY_NAME)));
                    bookmarkURL.add(cursor.getString(cursor.getColumnIndex(DBBookmarkSupport.KEY_URL)));
                } while (cursor.moveToNext());
            }

            DisplayBookmarkAdapter bookmarkAdapter = new DisplayBookmarkAdapter(this, bookmarkID, bookmarkTitle, bookmarkURL);
            bookmarkList.setAdapter(bookmarkAdapter);
            cursor.close();

    }

    public static boolean bookmarkCurrentlyStored(String newBookmarkTitle) {

        Cursor cursor = database.rawQuery("SELECT * FROM " + DBBookmarkSupport.DATABASE_TABLE + " WHERE " + DBBookmarkSupport.KEY_NAME + "= '" + newBookmarkTitle + "'", null);

        int resultReturned = cursor.getCount();

        if (resultReturned == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {

        displayData();
        super.onResume();
    }
}

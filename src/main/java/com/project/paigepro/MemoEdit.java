/*
    Name:   MemoEdit.java
    Author: Sean Smith
    Date:   28 December 2013

    This page displays the interface used for the creation of memos within the application.
*/

package com.project.paigepro;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MemoEdit extends Activity{

    private EditText etTitle, etContent;
    private TextView tvDate;

    private Long rowID;
    private Date currentDate;
    private Cursor memoCursor;
    private DBMemo dbMemo;

    public static String recordDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.memoedit);

        etTitle = (EditText) findViewById(R.id.etMemoTitle);
        etContent = (EditText) findViewById(R.id.vContent);
        tvDate = (TextView) findViewById(R.id.tvMemoDate);

        dbMemo = new DBMemo(MemoEdit.this);
        dbMemo.open();

        setDate();

        if (savedInstanceState == null) {
            rowID = null;
        } else {
            rowID = (Long)savedInstanceState.getSerializable(DBMemo.KEY_ROWID);
        }

        if (rowID == null) {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                rowID = extras.getLong(DBMemo.KEY_ROWID);
            }
        }

        fillMemo();
    }

    private void setDate() {

        currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");

        recordDate = formatter.format(currentDate);
        tvDate.setText(recordDate);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {

        super.onSaveInstanceState(bundle);
        saveMemo();
        bundle.putSerializable(DBMemo.KEY_ROWID, rowID);
    }

    @Override
    protected void onPause() {

        super.onPause();
        saveMemo();
    }

    @Override
    protected void onResume() {

        super.onResume();
        fillMemo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.memomenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.memoDelete:
                if (memoCursor != null) {

                    memoCursor.close();
                    memoCursor = null;
                }

                if (rowID != null) {
                    dbMemo.deleteMemo(rowID);
                }

                finish();
                return true;

            case R.id.memoSave:
                saveMemo();

                finish();
                return true;
        }
        return false;
    }

    private void saveMemo() {

        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();

        if (rowID == null) {
            rowID = dbMemo.createMemo(title, content, recordDate);
        } else {
            dbMemo.updateMemo(rowID, title, content, recordDate);
        }
    }

    private void fillMemo() {

        if (rowID != null) {

            memoCursor = dbMemo.getMemo(rowID);

            etTitle.setText(memoCursor.getString(memoCursor.getColumnIndexOrThrow(DBMemo.KEY_TITLE)));
            etContent.setText(memoCursor.getString(memoCursor.getColumnIndexOrThrow(DBMemo.KEY_CONTENT)));
        }
    }
}

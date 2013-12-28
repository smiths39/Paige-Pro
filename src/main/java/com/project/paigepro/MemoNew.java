/*
    Name:   MemoNew.java
    Author: Sean Smith
    Date:   28 December 2013

    This page displays the interactive memo feature of the application.
*/

package com.project.paigepro;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MemoNew extends ListActivity {

    private static final int CREATE_MEMO = 0;
    private static final int EDIT_MEMO = 1;
    private static final int DELETE_MEMO = Menu.FIRST;

    private String [] memoSource = new String[] { DBMemo.KEY_TITLE, DBMemo.KEY_DATE };
    private int [] memoDestination = { R.id.tvTitleRow, R.id.tvDateRow };

    private DBMemo displayMemoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.memonew);

        displayMemoAdapter = new DBMemo(this);
        displayMemoAdapter.open();

        displayStoredMemo();
        registerForContextMenu(getListView());

        Button addMemo = (Button)findViewById(R.id.bAddMemo);

        addMemo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent memoIntent = new Intent(MemoNew.this, MemoEdit.class);
                startActivityForResult(memoIntent, CREATE_MEMO);
            }
        });
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {

        super.onListItemClick(listView, view, position, id);

        Intent editMemoIntent = new Intent(MemoNew.this, MemoEdit.class);
        editMemoIntent.putExtra(DBMemo.KEY_ROWID, id);
        startActivityForResult(editMemoIntent, EDIT_MEMO);
    }

    private void displayStoredMemo() {

        Cursor notesCursor = displayMemoAdapter.retrieveMemos();
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.memostyle, notesCursor, memoSource, memoDestination, 0);
        setListAdapter(notes);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.add(0, DELETE_MEMO, 0, "Delete Memo");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case DELETE_MEMO:
                AdapterContextMenuInfo adapter = (AdapterContextMenuInfo) item.getMenuInfo();
                displayMemoAdapter.deleteMemo(adapter.id);
                displayStoredMemo();

                return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        displayStoredMemo();
    }
}
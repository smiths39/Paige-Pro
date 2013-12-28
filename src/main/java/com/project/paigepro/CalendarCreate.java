/*
    Name:   CalendarCreate.java
    Author: Sean Smith
    Date:   28 December 2013

    This page permits a user to create a personal calendar for them to use to store future events.
*/

package com.project.paigepro;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarCreate extends Activity implements View.OnClickListener {

    private static Button buttonCalendarCreate;
    private static EditText etCalendarName, etAccountName, etAccountOwner;

    private static final Uri CALENDAR_URI = CalendarContract.Calendars.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendarcreate);

        buttonCalendarCreate = (Button) findViewById(R.id.bCalendarCreate);

        etCalendarName = (EditText) findViewById(R.id.etCalendarName);
        etAccountName = (EditText) findViewById(R.id.etAccountName);
        etAccountOwner = (EditText) findViewById(R.id.etAccountOwner);

        buttonCalendarCreate.setOnClickListener(CalendarCreate.this);
    }

    @Override
    public void onClick(View item) {

        switch (item.getId()) {

            case R.id.bCalendarCreate:

                if (allFieldsEntered()) {

                    createCalendar(CalendarCreate.this);
                    Toast.makeText(CalendarCreate.this, "Calendar created successfully", Toast.LENGTH_SHORT).show();
                } else {

                    Dialog dialog = new Dialog(CalendarCreate.this);
                    dialog.setTitle("Incomplete Input");

                    TextView text = new TextView(CalendarCreate.this);
                    text.setText("Please enter input for all available fields.");

                    dialog.setContentView(text);
                    dialog.show();
                }
                break;
        }
    }

    private boolean allFieldsEntered() {

        return (!etCalendarName.getText().toString().isEmpty() ||
                !etAccountName.getText().toString().isEmpty() ||
                !etAccountOwner.getText().toString().isEmpty());
    }

    public static ContentValues newCalendarValues(String calendarName, String accountName, String accountOwner) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
        contentValues.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        contentValues.put(CalendarContract.Calendars.NAME, calendarName);
        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, calendarName);
        contentValues.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xEA8561);
        contentValues.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        contentValues.put(CalendarContract.Calendars.OWNER_ACCOUNT, accountOwner);
        contentValues.put(CalendarContract.Calendars.VISIBLE, 1);
        contentValues.put(CalendarContract.Calendars.SYNC_EVENTS, 1);

        return contentValues;
    }

    private static Uri newCalendarURI(String accountName) {

        return CALENDAR_URI.buildUpon()
                           .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                           .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
                           .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                           .build();
    }

    public static void createCalendar(Context context) {

        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = newCalendarValues(etCalendarName.getText().toString(), etAccountName.getText().toString(), etAccountOwner.getText().toString());
        contentResolver.insert(newCalendarURI(etAccountName.getText().toString()), contentValues);
    }
}

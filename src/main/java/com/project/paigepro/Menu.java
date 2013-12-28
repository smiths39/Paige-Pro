/*
    Name:   Menu.java
    Author: Sean Smith
    Date:   28 December 2013

    This is the main page of the application that provides access to the following features within the application:
        1) Memo storage
        2) Photo storage
        3) Video storage
        4) Audio player
        5) Personal web browser / bookmark storage
        6) Calendar / Event storage
*/

package com.project.paigepro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Html;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Date;

public class Menu extends Activity implements View.OnClickListener {

    private TabHost tabHost;
    private TextView currentTime, currentTimeCalendar;
    private Button buttonMemo, buttonPhoto, buttonVideo, buttonAudio, buttonLink,
                   buttonViewCalendar, buttonCreateCalendar, buttonCreateEvent,
                   buttonDeleteEvent, buttonEditEvent;

    private AlertDialog.Builder alert;

    private long id = 0;

    private final int ID_INDEX = 0;
    private final String [] eventID = { CalendarContract.Events._ID };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        initialiseTabBar();

        buttonMemo = (Button) findViewById(R.id.bMemoNew);
        buttonPhoto = (Button) findViewById(R.id.bPhotoNew);
        buttonVideo = (Button) findViewById(R.id.bVideoNew);
        buttonAudio = (Button) findViewById(R.id.bAudioNew);
        buttonLink = (Button) findViewById(R.id.bLinkNew);

        buttonViewCalendar = (Button) findViewById(R.id.bViewCalendar);
        buttonCreateCalendar = (Button) findViewById(R.id.bCreateCalendar);
        buttonCreateEvent = (Button) findViewById(R.id.bCreateEvent);
        buttonDeleteEvent = (Button) findViewById(R.id.bDeleteEvent);
        buttonEditEvent = (Button) findViewById(R.id.bEditEvent);

        buttonMemo.setOnClickListener(Menu.this);
        buttonPhoto.setOnClickListener(Menu.this);
        buttonVideo.setOnClickListener(Menu.this);
        buttonLink.setOnClickListener(Menu.this);
        buttonAudio.setOnClickListener(Menu.this);

        buttonViewCalendar.setOnClickListener(Menu.this);
        buttonCreateCalendar.setOnClickListener(Menu.this);
        buttonCreateEvent.setOnClickListener(Menu.this);
        buttonDeleteEvent.setOnClickListener(Menu.this);
        buttonEditEvent.setOnClickListener(Menu.this);
        currentTime = (TextView) findViewById(R.id.currentTimeMenu);
        currentTimeCalendar = (TextView) findViewById(R.id.currentTimeCalendar);

        setCurrentTime(currentTime);
        setCurrentTime(currentTimeCalendar);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.optionalmenu, menu);

        return true;
    }

    @Override
    public void onClick(View selection) {

        switch (selection.getId()) {

            case R.id.bMemoNew:
                try{
                    Intent memoIntent = new Intent("com.project.paigepro.MEMONEW");
                    startActivity(memoIntent);
                } catch (Exception e) {
                    errorHandler(e);
                }

                break;

            case R.id.bPhotoNew:
                try{
                    Intent photoIntent = new Intent("com.project.paigepro.PHOTONEW");
                    startActivity(photoIntent);
                } catch (Exception e) {
                    errorHandler(e);
                }

                break;

            case R.id.bVideoNew:
                try{
                    Intent videoIntent = new Intent("com.project.paigepro.VIDEONEW");
                    startActivity(videoIntent);
                } catch (Exception e) {
                    errorHandler(e);
                }

                break;

            case R.id.bAudioNew:
                try{
                    Intent audioIntent = new Intent("com.project.paigepro.AUDIONEW");
                    startActivity(audioIntent);
                } catch (Exception e) {
                    errorHandler(e);
                }

                break;

            case R.id.bLinkNew:
                 try {
                     Intent linkIntent = new Intent("com.project.paigepro.LINKNEW");
                     startActivity(linkIntent);
                 } catch (Exception e) {
                     errorHandler(e);
                 }

                 break;

            case R.id.bViewCalendar:
                try {
                    launchViewCalendar();
                } catch (Exception e) {
                    errorHandler(e);
                }

                break;

            case R.id.bCreateCalendar:
                try {
                    Intent createCalendarIntent = new Intent("com.project.paigepro.CALENDARCREATE");
                    startActivity(createCalendarIntent);
                } catch (Exception e) {
                    errorHandler(e);
                }

                break;

            case R.id.bCreateEvent:
                try {
                    Intent createCalendarEventIntent = new Intent("com.project.paigepro.CALENDAREVENTCREATE");
                    startActivity(createCalendarEventIntent);
                } catch (Exception e) {
                    errorHandler(e);
                }

                break;

            case R.id.bDeleteEvent:
                try {
                    promptDeleteInput();
                } catch (Exception e) {
                    errorHandler(e);
                }

                break;

            case R.id.bEditEvent:
                try {
                    promptEditInput();
                } catch (Exception e) {
                    errorHandler(e);
                }

                break;
        }
    }

    private void errorHandler(Exception exception) {

        String errorMessage = exception.toString();

        Dialog dialog = new Dialog(Menu.this);
        dialog.setTitle("Error Occurred");

        TextView text = new TextView(Menu.this);
        text.setText(errorMessage);

        dialog.setContentView(text);
        dialog.show();
    }

    private void eventNotFound() {

        Dialog dialog = new Dialog(Menu.this);
        dialog.setTitle("Cannot Locate Event");

        TextView text = new TextView(Menu.this);
        text.setText("Event does not exist.");

        dialog.setContentView(text);
        dialog.show();
    }

    private void launchViewCalendar() {

        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, System.currentTimeMillis());
        Intent viewCalendarIntent = new Intent(Intent.ACTION_VIEW);
        viewCalendarIntent.setData(builder.build());
        startActivity(viewCalendarIntent);
    }

    private void promptDeleteInput() {

        alert = new AlertDialog.Builder(Menu.this, R.style.alert_dialog_theme);

        alert.setTitle("Delete Event");
        alert.setMessage("Enter Event Title");

        final EditText eventTitleInput = new EditText(Menu.this);
        eventTitleInput.setTextColor(Color.parseColor("#ffffff"));
        alert.setView(eventTitleInput);

        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                deleteEvent(Menu.this, eventTitleInput.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {}

        });

        alert.show();
    }

    private void deleteEvent(Context context, String eventTitle) {

        ContentResolver contentResolver = context.getContentResolver();
        String selection = "(" + CalendarContract.Events.TITLE + " = ?)";
        String[] selectionArgs = new String[] { eventTitle };

        Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, selection, selectionArgs, null);

        if (cursor.getCount() == 1) {

            contentResolver.delete(CalendarContract.Events.CONTENT_URI, selection, selectionArgs);
            Toast.makeText(Menu.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
        } else {

            eventNotFound();
        }
    }

    private void promptEditInput() {

        alert = new AlertDialog.Builder(Menu.this, R.style.alert_dialog_theme);

        alert.setTitle("Edit Event");
        alert.setMessage("Enter Event Title");

        final EditText etEventTitle = new EditText(Menu.this);
        etEventTitle.setTextColor(Color.parseColor("#ffffff"));
        alert.setView(etEventTitle);

        alert.setPositiveButton("Edit", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                String selection = "(" + CalendarContract.Events.TITLE + " = ?)";
                String[] selectionArgs = new String[] { etEventTitle.getText().toString() };

                Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, selection, selectionArgs, null);

                if (cursor.getCount() == 1) {

                    launchEditEvent(Menu.this, etEventTitle.getText().toString());
                } else {

                    eventNotFound();
               }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {}

        });

        alert.show();
    }

    private void launchEditEvent(Context context, String eventTitle) {

        ContentResolver contentResolver = context.getContentResolver();

        String selection = "(" + CalendarContract.Events.TITLE + " = ?)";
        String[] selectionArgs = new String[] { eventTitle };
        Cursor cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI, eventID, selection, selectionArgs, null);

        if (cursor.moveToFirst()) {
            id = cursor.getLong(ID_INDEX);
        }

        Uri eventUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
        Intent editIntent = new Intent(Intent.ACTION_EDIT).setData(eventUri);
        startActivity(editIntent);
    }

    private void initialiseTabBar() {

        tabHost.setup();

        TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.journalTab);
        tabSpec.setIndicator("Journal Entries");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.eventCalendarTab);
        tabSpec.setIndicator("Event Calendar");
        tabHost.addTab(tabSpec);
    }

    private void setCurrentTime(TextView currentTime) {

        currentTime.setTypeface(null, Typeface.BOLD);
        currentTime.setTypeface(null, Typeface.ITALIC);
        currentTime.setTypeface(null, Typeface.BOLD_ITALIC);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        currentTime.setText(Html.fromHtml("<i>" + currentDateTimeString + "</i>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.aboutus:
                Intent aboutUsIntent = new Intent("com.project.paigepro.ABOUTUS");
                startActivity(aboutUsIntent);

                break;

            case R.id.settings:
                Intent settingsIntent = new Intent("com.project.paigepro.SETTINGS");
                startActivity(settingsIntent);

                break;

            case R.id.exit:
                finish();

                break;
        }

        return false;
    }
}
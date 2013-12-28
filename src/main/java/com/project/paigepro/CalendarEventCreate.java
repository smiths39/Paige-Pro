/*
    Name:   CalendarEventCreate.java
    Author: Sean Smith
    Date:   28 December 2013

    This page provides a user with the capability of creating an event within their desired calendar.
    When all input fields are entered, a calendar event will be populated and the user can choose to implement the event.
*/

package com.project.paigepro;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class CalendarEventCreate extends Activity implements View.OnClickListener {

    private Button buttonCreateEvent;
    private EditText etEventTitle, etEventDescription, etEventLocation, etEventStartDateDay, etEventStartDateMonth, etEventStartDateYear,
                     etEventEndDateDay, etEventEndDateMonth, etEventEndDateYear, etEventStartTimeHour, etEventStartTimeMinute,
                     etEventEndTimeHour, etEventEndTimeMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendareventcreate);

        etEventTitle = (EditText) findViewById(R.id.etEventTitle);
        etEventDescription = (EditText) findViewById(R.id.etEventDescription);
        etEventLocation = (EditText) findViewById(R.id.etEventLocation);
        etEventStartDateDay = (EditText) findViewById(R.id.etEventStartDateDay);
        etEventStartDateMonth = (EditText) findViewById(R.id.etEventStartDateMonth);
        etEventStartDateYear = (EditText) findViewById(R.id.etEventStartDateYear);
        etEventEndDateDay = (EditText) findViewById(R.id.etEventEndDateDay);
        etEventEndDateMonth = (EditText) findViewById(R.id.etEventEndDateMonth);
        etEventEndDateYear = (EditText) findViewById(R.id.etEventEndDateYear);
        etEventStartTimeHour = (EditText) findViewById(R.id.etEventStartTimeHour);
        etEventStartTimeMinute = (EditText) findViewById(R.id.etEventStartTimeMinute);
        etEventEndTimeHour = (EditText) findViewById(R.id.etEventEndTimeHour);
        etEventEndTimeMinute = (EditText) findViewById(R.id.etEventEndTimeMinute);

        buttonCreateEvent = (Button) findViewById(R.id.bCalendarEvent);

        buttonCreateEvent.setOnClickListener(CalendarEventCreate.this);
    }

    public void populateEventDetails() {

        Calendar eventStartTime = Calendar.getInstance();
        Calendar eventEndTime = Calendar.getInstance();

        eventStartTime.set(Integer.parseInt(etEventStartDateYear.getText().toString()),
                           Integer.parseInt(etEventStartDateMonth.getText().toString()) - 1,
                           Integer.parseInt(etEventStartDateDay.getText().toString()),
                           Integer.parseInt(etEventStartTimeHour.getText().toString()),
                           Integer.parseInt(etEventStartTimeMinute.getText().toString()), 0);

        eventEndTime.set(Integer.parseInt(etEventEndDateYear.getText().toString()),
                         Integer.parseInt(etEventEndDateMonth.getText().toString()) - 1,
                         Integer.parseInt(etEventEndDateDay.getText().toString()),
                         Integer.parseInt(etEventEndTimeHour.getText().toString()),
                         Integer.parseInt(etEventEndTimeMinute.getText().toString()), 0);

        Intent createEventIntent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventStartTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventEndTime.getTimeInMillis())
                        .putExtra(CalendarContract.ACTION_EVENT_REMINDER, true)
                        .putExtra(CalendarContract.Events.TITLE, etEventTitle.getText().toString())
                        .putExtra(CalendarContract.Events.DESCRIPTION, etEventDescription.getText().toString())
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, etEventLocation.getText().toString())
                        .putExtra(CalendarContract.Events.EVENT_TIMEZONE, "Europe/London")
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        startActivity(createEventIntent);
    }

    private boolean allFieldsEntered() {

        return (!etEventTitle.getText().toString().isEmpty() ||
                !etEventDescription.getText().toString().isEmpty() ||
                !etEventLocation.getText().toString().isEmpty() ||
                !etEventStartDateDay.getText().toString().isEmpty() ||
                !etEventStartDateMonth.getText().toString().isEmpty() ||
                !etEventStartDateYear.getText().toString().isEmpty() ||
                !etEventEndDateDay.getText().toString().isEmpty() ||
                !etEventEndDateMonth.getText().toString().isEmpty() ||
                !etEventEndDateYear.getText().toString().isEmpty() ||
                !etEventStartTimeHour.getText().toString().isEmpty() ||
                !etEventStartTimeMinute.getText().toString().isEmpty() ||
                !etEventEndTimeHour.getText().toString().isEmpty() ||
                !etEventEndTimeMinute.getText().toString().isEmpty());
    }

    @Override
    public void onClick(View item) {

        switch (item.getId()) {

            case R.id.bCalendarEvent:

                if (allFieldsEntered()) {

                    populateEventDetails();
                } else {

                    Dialog dialog = new Dialog(CalendarEventCreate.this);
                    dialog.setTitle("Incomplete Input");

                    TextView text = new TextView(CalendarEventCreate.this);
                    text.setText("Please enter input for all available fields.");

                    dialog.setContentView(text);
                    dialog.show();
                }

                break;
        }
    }
}

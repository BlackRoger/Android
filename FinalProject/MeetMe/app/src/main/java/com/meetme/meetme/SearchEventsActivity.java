package com.meetme.meetme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.meetme.meetme.DataManagers.DataBase.EventInfo;
import com.meetme.meetme.DataManagers.DataManager;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SearchEventsActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    Date ToDate;
    Calendar mCalendar = Calendar.getInstance();
    private DateFormat mTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    View tmpView;
    Date RelevantDate;
    boolean IgnoreNextEventSelection;
    boolean IsUpdatingEvent;

    EditText txtName;
    CheckBox chkName;
    CheckBox chkLocation;
    CheckBox chkEventType;
    CheckBox chkSubType;
    Spinner spnEventTypeSpinner;
    Spinner spnEventSubtypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Date NewEventDate = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_events);

        IgnoreNextEventSelection = true;
        mCalendar.setTimeZone(TimeZone.getDefault());

        // initialize the spinners!
        txtName                 = (EditText)findViewById(R.id.edit_text_event_name);
        spnEventSubtypeSpinner  = (Spinner) findViewById(R.id.spinner_event_sub_type);
        spnEventTypeSpinner     = (Spinner) findViewById(R.id.spinner_event_type);
        CheckBox chkName        = (CheckBox)findViewById(R.id.check_box_name);
        CheckBox chkLocation    = (CheckBox)findViewById(R.id.check_box_near_me);
        CheckBox chkEventType   = (CheckBox)findViewById(R.id.check_box_event_type);
        CheckBox chkSubType     = (CheckBox)findViewById(R.id.check_box_event_sub_type);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> RecurrenceAdapter = ArrayAdapter.createFromResource(this,
                R.array.recurrence_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> EventTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.event_types_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        RecurrenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        EventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEventTypeSpinner.setAdapter(EventTypeAdapter);
        spnEventTypeSpinner.setSelection(0);
        spnEventTypeSpinner.setOnItemSelectedListener(this);

        InitViews();
    }

    void InitViews() {
        /*
        txtName.setText(ExistingEvent.Name);
        spnEventTypeSpinner.setSelection(ExistingEvent.EventType.ordinal());
        UpdateSubtypeSpinner(ExistingEvent.EventType);
        spnEventSubtypeSpinner.setSelection(ExistingEvent.EventSecondaryType);
        */
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // The first time this is called, its because we selected from code, so do nothing.
        if (IgnoreNextEventSelection) {
            IgnoreNextEventSelection = false;
            return;
        }

        // First thing first - is this the first spinner?
        View EventTypeView = findViewById(R.id.spinner_event_type);
        Integer ViewId = parent.getId();
        Integer EventViewId = EventTypeView.getId();

        if (ViewId.intValue() == EventViewId.intValue()) {
            EventInfo.eEventTypes types[] = EventInfo.eEventTypes.values();
            UpdateSubtypeSpinner(types[pos]);
        }
    }

    void UpdateSubtypeSpinner(EventInfo.eEventTypes MainType) {
        Spinner EventSubtypeSpinner = (Spinner) findViewById(R.id.spinner_event_sub_type);
        EventInfo.eEventTypes types[] = EventInfo.eEventTypes.values();
        ArrayAdapter<CharSequence> Adapter;

        // We need to know what is the event type to give a list of subtypes
        switch(MainType) {
            case EVENT_TYPE_SPORT:
                Adapter = ArrayAdapter.createFromResource(this,
                        R.array.sport_event_types_array, android.R.layout.simple_spinner_item);
                break;
            case EVENT_TYPE_FOOD:
                Adapter = ArrayAdapter.createFromResource(this,
                        R.array.food_event_types_array, android.R.layout.simple_spinner_item);
                break;
            case EVENT_TYPE_SOCIAL:
                Adapter = ArrayAdapter.createFromResource(this,
                        R.array.social_event_types_array, android.R.layout.simple_spinner_item);
                break;
            // For now, just keep it unclickable
            case EVENT_TYPE_OTHER:
            default:
                return;
        }

        // Specify the layout to use when the list of choices appears
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        EventSubtypeSpinner.setAdapter(Adapter);

        EventSubtypeSpinner.setEnabled(true);
        EventSubtypeSpinner.setClickable(true);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void button_search(View view) {
        //DataManager.getInstance(this).FindEventByFriend()
    }

    public void button_clear(View view) {
        chkEventType.setChecked(false);
        chkLocation.setChecked(false);
        chkName.setChecked(false);
        chkSubType.setChecked(false);
    }
}

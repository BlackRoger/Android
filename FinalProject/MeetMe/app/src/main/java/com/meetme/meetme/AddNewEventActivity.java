package com.meetme.meetme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.Profile;
import com.meetme.meetme.DataManagers.DataBase.EventInfo;
import com.meetme.meetme.DataManagers.DataManager;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AddNewEventActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    public static final String EVENT_INFO = "Event_Info";
    public static final String EVENT_DATE = "Event_Date";
    public static final String EVENT_FINAL = "Event_Final";
    public static final String EVENT_REMOVED = "Event_Removed";
    public static final int ADD_EVENT_REQUEST = 1;
    public static final int UPDATE_EVENT_REQUEST = 2;
    // public static final int JOIN_EVENT_REQUEST = 3;

    Date mFromDate;
    Date mToDate;
    EventInfo mOldEvent = null;
    Calendar mCalendar = Calendar.getInstance();
    private DateFormat mTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    View tmpView;
    Date RelevantDate;
    boolean IgnoreNextEventSelection;
    boolean IsUpdatingEvent;
    boolean IsJoinEvent = false;

    EditText txtName;
    EditText txtDescryption;
    EditText txtLocation;
    TextView txtFromDate;
    TextView txtFromTime;
    TextView txtToDate;
    TextView txtToTime;
    EditText txtParticipationCap;
    Spinner spnRecurrenceSpinner;
    Spinner spnEventTypeSpinner;
    Spinner spnEventSubtypeSpinner;
    SharedPreferences EventIdpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Date NewEventDate = null;
        super.onCreate(savedInstanceState);

        Serializable Input = getIntent().getExtras().getSerializable(EVENT_INFO);

        Bundle b = getIntent().getExtras();
        String requestType = b.getString("requestType");
        if (requestType == null)
            requestType = "";

        if (requestType.equals("Join")) {
            NewEventDate = new Date(getIntent().getExtras().getLong(EVENT_DATE));
            mOldEvent = (EventInfo) Input;
            mFromDate = new Date(((EventInfo) Input).StartDate.longValue());
            mToDate = new Date(((EventInfo) Input).EndDate.longValue());
            IsJoinEvent = true;
            IsUpdatingEvent = true;
            setContentView(R.layout.activity_join_event);
        } else {
            // Check if we were asked to modify an existing event
            if (Input != null) {
                mOldEvent = (EventInfo) Input;
                mFromDate = new Date(((EventInfo) Input).StartDate.longValue());
                mToDate = new Date(((EventInfo) Input).EndDate.longValue());
                IsUpdatingEvent = true;
                setContentView(R.layout.activity_modify_event);
            } else {
                NewEventDate = new Date(getIntent().getExtras().getLong(EVENT_DATE));
                mFromDate = new Date(NewEventDate.getTime());
                mToDate = new Date(NewEventDate.getTime());
                IsUpdatingEvent = false;
                setContentView(R.layout.activity_add_new_event);
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        IgnoreNextEventSelection = true;
        mCalendar.setTimeZone(TimeZone.getDefault());

        // initialize the spinners!
        txtName = (EditText) findViewById(R.id.edit_text_event_name);
        txtDescryption = (EditText) findViewById(R.id.edit_text_event_descryption);
        txtLocation = (EditText) findViewById(R.id.edit_text_event_location);
        spnEventSubtypeSpinner = (Spinner) findViewById(R.id.spinner_event_sub_type);
        spnRecurrenceSpinner = (Spinner) findViewById(R.id.spinner_recurrence);
        spnEventTypeSpinner = (Spinner) findViewById(R.id.spinner_event_type);
        txtFromDate = (TextView) findViewById(R.id.edit_text_from_date);
        txtFromTime = (TextView) findViewById(R.id.edit_text_from_time);
        txtToDate = (TextView) findViewById(R.id.edit_text_to_date);
        txtToTime = (TextView) findViewById(R.id.edit_text_to_time);
        txtParticipationCap = (EditText) findViewById(R.id.edit_text_event_participation_cap);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> RecurrenceAdapter = ArrayAdapter.createFromResource(this,
                R.array.recurrence_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> EventTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.event_types_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        RecurrenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRecurrenceSpinner.setAdapter(RecurrenceAdapter);
        spnRecurrenceSpinner.setSelection(0);

        EventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEventTypeSpinner.setAdapter(EventTypeAdapter);
        spnEventTypeSpinner.setSelection(0);
        spnEventTypeSpinner.setOnItemSelectedListener(this);

        if (IsUpdatingEvent) {
            InitExistingEvent(mOldEvent);
        } else {
            InitNewEvent(NewEventDate);
        }
    }

    void InitExistingEvent(EventInfo ExistingEvent) {
        txtName.setText(ExistingEvent.Name);
        txtDescryption.setText(ExistingEvent.Description);
        txtLocation.setText(ExistingEvent.Location);

        // Set the from date/time
        mFromDate = new Date(ExistingEvent.StartDate.longValue());
        txtFromDate.setText(mDateFormat.format(mFromDate));
        txtFromTime.setText(mTimeFormat.format(mFromDate));

        // Set the to date/time
        mToDate = new Date(ExistingEvent.EndDate.longValue());
        txtToDate.setText(mDateFormat.format(mToDate));
        txtToTime.setText(mTimeFormat.format(mToDate));
        txtParticipationCap.setText(String.valueOf(ExistingEvent.ParticipationCap));

        spnRecurrenceSpinner.setSelection(ExistingEvent.Recurrence.ordinal());
        spnEventTypeSpinner.setSelection(ExistingEvent.EventType.ordinal());
        UpdateSubtypeSpinner(ExistingEvent.EventType);
        spnEventSubtypeSpinner.setSelection(ExistingEvent.EventSecondaryType);
    }

    void InitNewEvent(Date EventDate) {
        // Set the from date/time
        mCalendar.setTime(EventDate);
        mFromDate = EventDate;
        txtFromDate.setText(mDateFormat.format(mFromDate));
        txtFromTime.setText(mTimeFormat.format(mFromDate));

        // Set the to date/time
        int Minute = mCalendar.get(Calendar.MINUTE);
        int Hour = mCalendar.get(Calendar.HOUR);
        if (Minute + 30 <= mCalendar.getActualMaximum(Calendar.MINUTE)) {
            Minute += 30;
        } else if (Hour == mCalendar.getActualMaximum(Calendar.HOUR)) {
            Minute = mCalendar.getActualMaximum(Calendar.MINUTE);
        } else {
            Minute = (Minute + 30) % 60;
            Hour++;
        }

        mCalendar.set(Calendar.MINUTE, Minute);
        mCalendar.set(Calendar.HOUR, Hour);
        mToDate = mCalendar.getTime();
        txtToDate.setText(mDateFormat.format(mToDate));
        txtToTime.setText(mTimeFormat.format(mToDate));

        txtParticipationCap.setText("0");

        // The subtypes are disabled until a type is chosen
        spnEventSubtypeSpinner.setEnabled(false);
        spnEventSubtypeSpinner.setClickable(false);

        // TODO: When we have a DayView set the start/end time to the time the user chose
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
        switch (MainType) {
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

    public void openDateDialog(View view) {
        DatePickerDialog DatePicker;
        tmpView = view;

        if (view.getId() == findViewById(R.id.edit_text_from_date).getId()) {
            RelevantDate = mFromDate;
            mCalendar.setTimeInMillis(mFromDate.getTime());
        } else {
            RelevantDate = mToDate;
            mCalendar.setTimeInMillis(mToDate.getTime());
        }

        DatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                mCalendar.set(selectedyear, selectedmonth, selectedday);
                ((TextView) tmpView).setText(mDateFormat.format(mCalendar.getTime()));

                RelevantDate.setTime(mCalendar.getTimeInMillis());
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE));
        DatePicker.setTitle("Select Date");
        DatePicker.show();
    }

    public void openTimeDialog(View view) {
        TimePickerDialog TimePicker;
        tmpView = view;

        if (view.getId() == findViewById(R.id.edit_text_from_time).getId()) {
            RelevantDate = mFromDate;
            mCalendar.setTimeInMillis(mFromDate.getTime());
        } else {
            RelevantDate = mToDate;
            mCalendar.setTimeInMillis(mToDate.getTime());
        }

        TimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DATE), selectedHour, selectedMinute);

                RelevantDate.setTime(mCalendar.getTimeInMillis());
                ((TextView) tmpView).setText(mTimeFormat.format(mCalendar.getTime()));
            }
        }, mCalendar.get(Calendar.HOUR), mCalendar.get(Calendar.MINUTE), true);
        TimePicker.setTitle("Select Time");
        TimePicker.show();
    }

    public void button_on_click_confirm(View view) {
        EventInfo NewEvent = new EventInfo();
        NewEvent.Id = (mOldEvent != null) ? mOldEvent.Id : null;
        NewEvent.Name = txtName.getText().toString();
        NewEvent.Description = txtDescryption.getText().toString();
        NewEvent.Location = txtLocation.getText().toString();
        NewEvent.ParticipationCap = Integer.valueOf(txtParticipationCap.getText().toString());
        NewEvent.EventType = EventInfo.eEventTypes.values()[spnEventTypeSpinner.getSelectedItemPosition()];
        NewEvent.EventSecondaryType = spnEventSubtypeSpinner.getSelectedItemPosition();
        NewEvent.StartDate = mFromDate.getTime();
        NewEvent.EndDate = mToDate.getTime();
        NewEvent.Recurrence = EventInfo.eReccurence.values()[spnRecurrenceSpinner.getSelectedItemPosition()];
        NewEvent.OrganizerId = Profile.getCurrentProfile().getId().toString();
        NewEvent.SecondaryId = "";


        if (IsJoinEvent) {
            DataManager.getInstance(this).AddEvent(NewEvent);
            Toast.makeText(getApplicationContext(),
                    "You have joined the event! ",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (IsUpdatingEvent) {
                DataManager.getInstance(this).UpdateEvent(NewEvent);
            } else {
                DataManager.getInstance(this).AddEvent(NewEvent);
            }
        }
        Intent ResultIntent = new Intent();
        ResultIntent.putExtra(EVENT_FINAL, NewEvent);
        ResultIntent.putExtra(EVENT_REMOVED, false);
        setResult(RESULT_OK, ResultIntent);

        finish();
    }

    public void button_on_click_cancel(View view) {
        setResult(RESULT_CANCELED, null);
        finish();
    }

    public void button_on_click_remove(View view) {
        DataManager.getInstance(this).RemoveEvent(mOldEvent);
        finish();

        Intent ResultIntent = new Intent();
        ResultIntent.putExtra(EVENT_REMOVED, true);
        setResult(RESULT_OK, ResultIntent);
    }
}

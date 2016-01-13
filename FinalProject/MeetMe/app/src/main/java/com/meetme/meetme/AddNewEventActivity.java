package com.meetme.meetme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class AddNewEventActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        IgnoreNextEventSelection = true;
        mCalendar.setTimeZone(TimeZone.getDefault());

        // initialize the spinners!
        txtName                 = (EditText)findViewById(R.id.edit_text_event_name);
        txtDescryption          = (EditText)findViewById(R.id.edit_text_event_descryption);
        txtLocation             = (EditText)findViewById(R.id.edit_text_event_location);
        spnEventSubtypeSpinner  = (Spinner) findViewById(R.id.spinner_event_sub_type);
        spnRecurrenceSpinner    = (Spinner) findViewById(R.id.spinner_recurrence);
        spnEventTypeSpinner     = (Spinner) findViewById(R.id.spinner_event_type);
        txtFromDate             = (TextView)findViewById(R.id.edit_text_from_date);
        txtFromTime             = (TextView)findViewById(R.id.edit_text_from_time);
        txtToDate               = (TextView)findViewById(R.id.edit_text_to_date);
        txtToTime               = (TextView)findViewById(R.id.edit_text_to_time);
        txtParticipationCap     = (EditText)findViewById(R.id.edit_text_event_participation_cap);

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

        Serializable Input = getIntent().getExtras().getSerializable(DayViewer.EVENT_INFO);

        // Check if we were asked to modify an existing event
        if (Input != null) {
            mOldEvent = (EventInfo)Input;
            mFromDate = new Date(((EventInfo) Input).StartDate.longValue());
            mToDate = new Date(((EventInfo) Input).EndDate.longValue());
            InitExistingEvent(mOldEvent);
            IsUpdatingEvent = true;
        } else {
            Date NewEventDate = new Date(getIntent().getExtras().getLong(DayViewer.EVENT_DATE));
            mFromDate = new Date(NewEventDate.getTime());
            mToDate = new Date(NewEventDate.getTime());

            InitNewEvent(NewEventDate);
            IsUpdatingEvent = false;
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
        if (Minute + 30 <= mCalendar.getActualMaximum(Calendar.MINUTE)){
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
                ((TextView)tmpView).setText(mDateFormat.format(mCalendar.getTime()));

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
        NewEvent.Name = txtName.getText().toString();
        NewEvent.Description = txtDescryption.getText().toString();
        NewEvent.Location = txtLocation.getText().toString();
        NewEvent.ParticipationCap = Integer.valueOf(txtParticipationCap.getText().toString());
        NewEvent.EventType = EventInfo.eEventTypes.values()[spnEventTypeSpinner.getSelectedItemPosition()];
        NewEvent.EventSecondaryType = spnEventTypeSpinner.getSelectedItemPosition();
        NewEvent.StartDate = mFromDate.getTime();
        NewEvent.EndDate = mToDate.getTime();
        NewEvent.Recurrence = EventInfo.eReccurence.values()[spnRecurrenceSpinner.getSelectedItemPosition()];
        NewEvent.OrganizerId = DataManager.getInstance(this).GetMyInfo().SecondaryId;
        NewEvent.SecondaryId = 0;

        if (IsUpdatingEvent) {
            DataManager.getInstance(this).UpdateEvent(mOldEvent, NewEvent);
        } else {
            DataManager.getInstance(this).AddEvent(NewEvent);
        }

        Intent ResultIntent = new Intent();
        ResultIntent.putExtra(DayViewer.EVENT_FINAL, NewEvent);
        setResult(RESULT_OK, ResultIntent);

        finish();
    }

    public void button_on_click_cancel(View view) {
        setResult(RESULT_CANCELED, null);
        finish();
    }
}

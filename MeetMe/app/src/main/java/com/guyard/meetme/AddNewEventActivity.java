package com.guyard.meetme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.guyard.meetme.DataManagers.DataBase.EventInfo;
import com.guyard.meetme.DataManagers.DataManager;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewEventActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    Date mFromDate;
    Date mToDate;
    Calendar mCalendar = Calendar.getInstance();
    private DateFormat mTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    View tmpView;

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
            EventInfo ExistingEvent = (EventInfo)Input;
            mFromDate = new Date(((EventInfo) Input).StartDate.longValue());
            mToDate = new Date(((EventInfo) Input).EndDate.longValue());
            InitExistingEvent(ExistingEvent);
        } else {
            Date NewEventDate = new Date(getIntent().getExtras().getLong(DayViewer.EVENT_INFO));
            mFromDate = new Date(NewEventDate.getTime());
            mToDate = new Date(NewEventDate.getTime());

            InitNewEvent(NewEventDate);
        }
    }

    void InitExistingEvent(EventInfo ExistingEvent) {
        UpdateSubtypeSpinner(ExistingEvent.EventType);

        txtName.setText(ExistingEvent.Name);
        txtDescryption.setText(ExistingEvent.Description);
        txtLocation.setText(ExistingEvent.Location);

        // Set the from date/time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ExistingEvent.StartDate);
        txtFromDate.setText(mDateFormat.format(mCalendar.getTime()));
        txtFromTime.setText(mTimeFormat.format(mCalendar.getTime()));

        // Set the to date/time
        calendar.setTimeInMillis(ExistingEvent.EndDate);
        txtToDate.setText(mDateFormat.format(mCalendar.getTime()));
        txtToTime.setText(mTimeFormat.format(mCalendar.getTime()));
        txtParticipationCap.setText(ExistingEvent.ParticipationCap);

        spnRecurrenceSpinner.setSelection(ExistingEvent.Recurrence.ordinal());
        spnEventTypeSpinner.setSelection(ExistingEvent.EventType.ordinal());
        spnEventSubtypeSpinner.setSelection(ExistingEvent.EventSecondaryType);
    }

    void InitNewEvent(Date EventDate) {
        // Set the from date/time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(EventDate.getTime());
        txtFromDate.setText(mDateFormat.format(mCalendar.getTime()));
        txtFromTime.setText(mTimeFormat.format(mCalendar.getTime()));

        // Set the to date/time
        int Minute = calendar.get(Calendar.MINUTE);
        int Hour = calendar.get(Calendar.HOUR);
        if (Minute + 30 <= calendar.getActualMaximum(Calendar.MINUTE)){
            Minute += 30;
        } else if (Hour == calendar.getActualMaximum(Calendar.HOUR)) {
            Minute = calendar.getActualMaximum(Calendar.MINUTE);
        } else {
            Minute = (Minute + 30) % 60;
            Hour++;
        }

        calendar.set(Calendar.MINUTE, Minute);
        calendar.set(Calendar.HOUR, Hour);
        txtToDate.setText(mDateFormat.format(mCalendar.getTime()));
        txtToTime.setText(mTimeFormat.format(mCalendar.getTime()));

        txtParticipationCap.setText("0");

        // The subtypes are disabled until a type is chosen
        spnEventSubtypeSpinner.setEnabled(false);
        spnEventSubtypeSpinner.setClickable(false);

        // TODO: When we have a DayView set the start/end time to the time the user chose
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
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
        Date RelevantDate;
        tmpView = view;

        if (view.getId() == R.id.edit_text_from_date) {
            mCalendar.setTimeInMillis(mFromDate.getTime());
        } else {
            mCalendar.setTimeInMillis(mToDate.getTime());
        }

        DatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                mCalendar.set(selectedyear, selectedmonth, selectedday);

                ((TextView)tmpView).setText(mDateFormat.format(mCalendar.getTime()));
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE));
        DatePicker.setTitle("Select Date");
        DatePicker.show();
    }

    private void openTimeDialog(View view) {
        TimePickerDialog TimePicker;
        Date RelevantDate;
        tmpView = view;

        if (view.getId() == R.id.edit_text_from_date) {
            mCalendar.setTimeInMillis(mFromDate.getTime());
        } else {
            mCalendar.setTimeInMillis(mToDate.getTime());
        }

        TimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DATE), selectedHour, selectedMinute);

                ((EditText) tmpView).setText(mTimeFormat.format(mCalendar.getTime()));
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
        NewEvent.StartDate = txtFromDate.getText();

        DataManager.getInstance(this).AddEvent(NewEvent);
    }

    public void button_on_click_cancel(View view) {
        finish();
    }
}

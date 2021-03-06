package com.meetme.meetme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.meetme.meetme.DataManagers.DataBase.EventInfo;
import com.meetme.meetme.DataManagers.DataManager;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventsCalendar extends AppCompatActivity {

    public static final String ARG_LONG_DATE = "date";
    CaldroidFragment caldroidFragment;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Date mModifiedDate = null;

    final CaldroidListener listener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {


            mModifiedDate = date;
            Intent ViewDayIntent = new Intent(getApplicationContext(), DayViewer.class);
            Long Time = date.getTime();
            ViewDayIntent.putExtra(ARG_LONG_DATE, Time);
            startActivity(ViewDayIntent);
        }

        @Override
        public void onChangeMonth(int month, int year) {
            String text = "month: " + month + " year: " + year;

        }

        @Override
        public void onLongClickDate(Date date, View view) {

        }

        @Override
        public void onCaldroidViewCreated() {

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(listener);

        // Now get all the events and color relevant dates.
        List<EventInfo> Events = DataManager.getInstance(this).GetMyEventsByMonth(new Date());

        for (EventInfo Event : Events) {
            Date StartDay = new Date(Event.StartDate);
            Date EndDay = new Date(Event.EndDate);
            Calendar StartDayCal = Calendar.getInstance();
            Calendar EndDayCal = Calendar.getInstance();

            StartDayCal.setTimeInMillis(Event.StartDate);
            EndDayCal.setTimeInMillis(Event.EndDate);

            int StartDayInMonth = StartDayCal.get(Calendar.DATE);
            int EndDayInMonth = EndDayCal.get(Calendar.DATE);

            // If the end date is in next month:
            if (EndDayCal.get(Calendar.MONTH) > StartDayCal.get(Calendar.MONTH)) {
                EndDayInMonth = StartDayCal.getActualMaximum(Calendar.DATE);
            }


            while (StartDayInMonth <= EndDayInMonth) {
                StartDayCal.set(Calendar.DATE, StartDayInMonth);
                SetDateColor(StartDayCal.getTime(), true);
                StartDayInMonth++;
                //caldroidFragment.refreshView();
            }
        }
        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar_fragment, caldroidFragment);
        t.commit();
    }

    void SetDateColor(Date date, boolean fHasEvents) {
        if (fHasEvents)
            caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_holo_blue_dark, date);
        else
            caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_black, date);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Check if the number of events in this day changed.
        if (mModifiedDate != null) {
            SetDateColor(mModifiedDate,
                    !DataManager.getInstance(this).GetEventsByDay(mModifiedDate).isEmpty());

            caldroidFragment.refreshView();
            mModifiedDate = null;
        }
    }
}

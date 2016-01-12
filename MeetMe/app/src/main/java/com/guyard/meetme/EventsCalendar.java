package com.guyard.meetme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventsCalendar extends AppCompatActivity {

    public static final String ARG_LONG_DATE = "date";
    CaldroidFragment caldroidFragment;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    final CaldroidListener listener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
            Toast.makeText(getApplicationContext(), dateFormat.format(date),
                    Toast.LENGTH_SHORT).show();

            // TODO: Check first if there are any other events
            Intent ViewDayIntent = new Intent(getApplicationContext(), DayViewer.class);
            ViewDayIntent.putExtra(ARG_LONG_DATE, Integer.valueOf((int)date.getTime()));
            startActivity(ViewDayIntent);

            /*
            Intent NewEventIntent = new Intent(getApplicationContext(), AddNewEventActivity.class);
            NewEventIntent.getExtras().putLong(ARG_LONG_DATE, date.getTime());
            startActivity(NewEventIntent);
            */

            //SetDateColor(date);
            //caldroidFragment.refreshView();
        }

        @Override
        public void onChangeMonth(int month, int year) {
            String text = "month: " + month + " year: " + year;
            Toast.makeText(getApplicationContext(), text,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLongClickDate(Date date, View view) {
            Toast.makeText(getApplicationContext(),
                    "Long click " + dateFormat.format(date),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCaldroidViewCreated() {
            Toast.makeText(getApplicationContext(),
                    "Caldroid view is created",
                    Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(listener);

        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar_fragment, caldroidFragment);
        t.commit();
    }

    void SetDateColor(Date date) {
        caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_holo_blue_dark, date);
        //caldroidFragment.setTextColorForDate(R.color.white, blueDate);

        //caldroidFragment.refreshView();
    }
}

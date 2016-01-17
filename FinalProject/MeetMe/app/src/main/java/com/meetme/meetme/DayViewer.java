package com.meetme.meetme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.meetme.meetme.DataManagers.DataBase.EventInfo;
import com.meetme.meetme.DataManagers.DataManager;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DayViewer extends AppCompatActivity {

    public static final String EVENT_INFO = "Event_Info";
    public static final String EVENT_DATE = "Event_Date";
    public static final String EVENT_FINAL = "Event_Final";
    public static final String EVENT_REMOVED = "Event_Removed";
    private static final int ADD_EVENT_REQUEST = 1;
    private static final int UPDATE_EVENT_REQUEST = 2;

    private Date CurrDate;
    private int mCurrPosition;
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private ArrayAdapter<EventInfo> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_viewer);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_new_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewEvent();
            }
        });

        // Get the events for today
        Long DateInMillis = getIntent().getExtras().getLong(EventsCalendar.ARG_LONG_DATE);
        CurrDate = new Date(DateInMillis.longValue());
        List<EventInfo> TodaysEvents = DataManager.getInstance(this).GetEventsByDay(CurrDate);

        final ListView listview = (ListView)findViewById(R.id.list_view_events);
        //registerForContextMenu(listview);

        mAdapter = new ArrayAdapter<EventInfo>(this,
                R.layout.event_list_element, TodaysEvents) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.event_list_element, parent, false);

                TextView txtName = (TextView) rowView.findViewById(R.id.text_view_event_name);
                TextView txtDescription = (TextView) rowView.findViewById(R.id.text_view_event_description);
                TextView txtTime = (TextView) rowView.findViewById(R.id.text_view_time);
                TextView txtParticipation = (TextView) rowView.findViewById(R.id.text_view_participation);

                // Set the texts in the row
                txtName.setText(this.getItem(position).Name);
                txtDescription.setText(this.getItem(position).Description);

                String Duration = timeFormat.format(this.getItem(position).StartDate) + " - " +
                        timeFormat.format(this.getItem(position).EndDate);
                txtTime.setText(Duration);
                // TODO: update participation count!
                txtParticipation.setText("0/" + String.valueOf(this.getItem(position).ParticipationCap));

                return rowView;
            }
        };

        listview.setAdapter(mAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                mCurrPosition = position;
                EditEvent(mAdapter.getItem(position));
            }

        });

        // If there are no current events, assume the user wants to add an event
        if (TodaysEvents.isEmpty()) {
            AddNewEvent();
        }
    }

    private void AddNewEvent() {
        // The floating action bar sends to add new event activity
        Intent AddEventIntent = new Intent(getApplicationContext(),
                AddNewEventActivity.class);
        AddEventIntent.putExtra(EVENT_DATE, CurrDate.getTime());
        startActivityForResult(AddEventIntent, ADD_EVENT_REQUEST);
    }

    private void EditEvent(EventInfo Event) {
        Intent EditEventIntent = new Intent(this, AddNewEventActivity.class);
        EditEventIntent.putExtra(EVENT_INFO, Event);
        startActivityForResult(EditEventIntent, UPDATE_EVENT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case (ADD_EVENT_REQUEST) :
                    AddEventToSchedule((EventInfo)data.getExtras().getSerializable(EVENT_FINAL));
                    break;
                case (UPDATE_EVENT_REQUEST) :
                    UpdateCurrentEvent((EventInfo)data.getExtras().getSerializable(EVENT_FINAL));
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void AddEventToSchedule(EventInfo Event) {
        mAdapter.add(Event);
        mAdapter.notifyDataSetChanged();
    }

    public void UpdateCurrentEvent(EventInfo NewEvent) {
        EventInfo OldEvent = mAdapter.getItem(mCurrPosition);
        mAdapter.remove(OldEvent);
        mAdapter.insert(NewEvent, mCurrPosition);
        mAdapter.notifyDataSetChanged();
    }
}

package com.guyard.meetme;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.guyard.meetme.DataManagers.DataBase.EventInfo;
import com.guyard.meetme.DataManagers.DataManager;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DayViewer extends AppCompatActivity {

    public static final String EVENT_INFO = "Event_Info";
    public static final String EVENT_DATE = "Event_Date";

    public Date CurrDate;
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_viewer);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_new_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The floating action bar sends to add new event activity
                Intent AddEventIntent = new Intent(getApplicationContext(),
                        AddNewEventActivity.class);
                AddEventIntent.putExtra(EVENT_DATE, CurrDate.getTime());
                startActivity(AddEventIntent);
            }
        });

        // Get the events for today
        CurrDate = new Date((long)getIntent().getExtras().getInt(EventsCalendar.ARG_LONG_DATE));
        List<EventInfo> TodaysEvents = DataManager.getInstance(this).GetEventsByDay(CurrDate);

        final ListView listview = (ListView)findViewById(R.id.list_view_events);
        registerForContextMenu(listview);

        final ArrayAdapter<EventInfo> adapter = new ArrayAdapter<EventInfo>(this,
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

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                EditEvent(adapter.getItem(position));
            }

        });
    }

    void EditEvent(EventInfo Event) {
        Intent EditEventIntent = new Intent(this, AddNewEventActivity.class);
        EditEventIntent.putExtra(EVENT_INFO, Event);
        startActivity(EditEventIntent);
    }
}

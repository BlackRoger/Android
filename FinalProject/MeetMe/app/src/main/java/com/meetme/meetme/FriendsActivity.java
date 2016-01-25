package com.meetme.meetme;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meetme.meetme.DataManagers.DataBase.EventInfo;
import com.meetme.meetme.DataManagers.DataBase.EventTable;
import com.meetme.meetme.DataManagers.DataManager;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FriendsActivity extends AppCompatActivity implements DataManager.EventsReady {

    public static final String INTENT_EXTRA_FRIEND_ID = "FRIEND_ID";
    public static final String INTENT_EXTRA_EVENT_TYPE = "EVENT_TYPE";
    public static final String INTENT_EXTRA_EVENT_SUB_TYPE = "EVENT_SUB_TYPE";
    public static final String INTENT_EXTRA_EVENT_NAME = "EVENT_NAME";

    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    SolventRecyclerViewAdapter rcAdapter;
    Integer mCurrItemPosition;
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    RecyclerView recyclerView;
    ProgressDialog dialog;
  //  private List<EventInfo> mReceivedEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        Bundle b = getIntent().getExtras();
        GetActivities(b.getString(INTENT_EXTRA_FRIEND_ID), b.getString(INTENT_EXTRA_EVENT_NAME),
                b.getInt(INTENT_EXTRA_EVENT_TYPE, -1), b.getInt(INTENT_EXTRA_EVENT_SUB_TYPE, -1));
        /*
        dialog = ProgressDialog.show(FriendsActivity.this, "",
                "Loading. Please wait...", true);
                */

    }

    private void GetActivities(String friend_id, String EventName, int EventType,
                               int EventSubType) {

        Object Filters[] = {null, null, null, null, null};
        String Columns[] = {"", "", "", "", ""};
        int Index = 0;

        if (friend_id != null) {
            Columns[Index] = EventTable.EventInfo.ORGANIZER;
            Filters[Index] = friend_id;
            Index++;
        }

        if (EventName != null) {
            Columns[Index] = EventTable.EventInfo.NAME;
            Filters[Index] = EventName;
            Index++;
        }

        if (EventType != -1) {
            Columns[Index] = EventTable.EventInfo.EVENT_TYPE;
            Filters[Index] = EventType;
            Index++;
        }

        if (EventSubType != -1) {
            Columns[Index] = EventTable.EventInfo.EVENT_SECONDARY_TYPE;
            Filters[Index] = EventSubType;
            Index++;
        }

        String FinalColumns[] = new String[Index];
        Object FinalFilters[] = new Object[Index];

        for (int col = 0; col < Index; col++) {
            FinalColumns[col] = Columns[col];
            FinalFilters[col] = Filters[col];
        }

        DataManager.getInstance(this).FindEventByFilter(FinalColumns, FinalFilters, this);
    }

    public void GetEvents(List<EventInfo> Events) {
        if (Events.size() == 0){
            TextView textView = (TextView) findViewById(R.id.message);
            textView.setText(R.string.noActivity);
        }
        rcAdapter = new SolventRecyclerViewAdapter(FriendsActivity.this, Events);
        recyclerView.setAdapter(rcAdapter);
    }

    public class SolventRecyclerViewAdapter extends RecyclerView.Adapter<SolventViewHolders> {

        private List<EventInfo> itemList;
        private Context context;

        public SolventRecyclerViewAdapter(Context context, List<EventInfo> itemList) {
            this.itemList = itemList;
            this.context = context;
        }

        public void insertNewItem(EventInfo NewEvent) {
            itemList.add(NewEvent);
        }

        public EventInfo getItemAtPosition(int position) {
            return itemList.get(position);
        }

        public void removeItemAtPosition(int position) {
            itemList.remove(position);
        }

        public void UpdateList(List<EventInfo> NewList) {
            itemList = NewList;
        }

        public void UpdateItemAtPosition(int position, EventInfo NewEvent) {
            itemList.remove(position);
            itemList.add(position, NewEvent);
        }

        @Override
        public SolventViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.solvent_list, null);
            SolventViewHolders rcv = new SolventViewHolders(layoutView);
            return rcv;
        }

        @Override
        public void onBindViewHolder(SolventViewHolders holder, int position) {

            EventInfo CurrEvent = itemList.get(position);

            holder.eventName.setText(CurrEvent.Name);
            // holder.eventDescription.setText(itemList.get(position).Description);
            EventInfo.eEventTypes EventType = itemList.get(position).EventType;
            Integer EventSecondaryType = itemList.get(position).EventSecondaryType;
            // getEventTypeImage(EventType ,EventSecondaryType);
            holder.eventTime.setText(dateFormat.format(CurrEvent.StartDate) + " - " +
                    timeFormat.format(CurrEvent.StartDate));

            holder.eventTypePhoto.setImageDrawable(getEventTypeImage(EventType, EventSecondaryType));
        }

        @Override
        public int getItemCount() {
            return this.itemList.size();
        }
    }



    public class SolventViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView eventName;
        public TextView eventDescription;
        public TextView eventTime;
        public ImageView eventTypePhoto;

        public SolventViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventDescription = (TextView) itemView.findViewById(R.id.text_view_event_description);
            eventTime = (TextView) itemView.findViewById(R.id.text_view_time);
            eventTypePhoto = (ImageView) itemView.findViewById(R.id.event_type);
        }

        @Override
        public void onClick(View view) {
            mCurrItemPosition = getPosition();
            EventInfo event = ((SolventRecyclerViewAdapter)
                    ((RecyclerView) view.getParent()).getAdapter()).getItemAtPosition(mCurrItemPosition);
            EditEvent(event);
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Drawable getEventTypeImage(EventInfo.eEventTypes eventType, Integer eventSecondaryType) {
        ImageView imageView = null;
        Drawable drawable = null;

        switch (eventType) {
            case EVENT_TYPE_SPORT:
                switch (EventInfo.eSportEventTypes.values()[eventSecondaryType]) {
                    case EVENT_TYPE_TENNIS:
                        drawable = getResources().getDrawable(R.drawable.tennis);
                        break;
                    case EVENT_TYPE_SWIMMING:
                        drawable = getResources().getDrawable(R.drawable.swimming);
                        break;
                    case EVENT_TYPE_SOCCER:
                        drawable = getResources().getDrawable(R.drawable.soccer);
                        break;
                    case EVENT_TYPE_GOLF:
                        drawable = getResources().getDrawable(R.drawable.golf);
                        break;
                    case EVENT_TYPE_FOOTBALL:
                        drawable = getResources().getDrawable(R.drawable.football);
                        break;
                    default:
                        drawable = getResources().getDrawable(R.drawable.meetme);
                        break;
                }
                break;
            case EVENT_TYPE_SOCIAL:
                switch (EventInfo.eSocialEventTypes.values()[eventSecondaryType]) {
                    case EVENT_TYPE_CLUB:
                        drawable = getResources().getDrawable(R.drawable.club);
                        break;
                    case EVENT_TYPE_HANGOUT:
                        drawable = getResources().getDrawable(R.drawable.hangout);
                        break;
                    case EVENT_TYPE_MEETING:
                        drawable = getResources().getDrawable(R.drawable.meeting);
                        break;
                    default:
                        drawable = getResources().getDrawable(R.drawable.meetme);
                        break;
                }
                break;
            case EVENT_TYPE_FOOD:
                switch (EventInfo.eFoodEventTypes.values()[eventSecondaryType]) {
                    case EVENT_TYPE_DINNER:
                        drawable = getResources().getDrawable(R.drawable.dinner);
                        break;
                    case EVENT_TYPE_LUNCH:
                        drawable = getResources().getDrawable(R.drawable.lunch);
                        break;
                    case EVENT_TYPE_BREAKFAST:
                        drawable = getResources().getDrawable(R.drawable.breakfast);
                        break;
                    default:
                        drawable = getResources().getDrawable(R.drawable.meetme);
                        break;
                }
                break;
            case EVENT_TYPE_OTHER:
                drawable = getResources().getDrawable(R.drawable.meetme);
                break;
            default:
                drawable = getResources().getDrawable(R.drawable.meetme);
                break;
        }
        return drawable;
    }


    private void EditEvent(EventInfo Event) {
        Intent EditEventIntent = new Intent(this, AddNewEventActivity.class);
        EditEventIntent.putExtra(AddNewEventActivity.EVENT_INFO, Event);
        Bundle b = new Bundle();
        b.putString("requestType", "Join");
       // EditEventIntent.putExtra(AddNewEventActivity.EVENT_INFO, Event);
        EditEventIntent.putExtras(b);
        startActivityForResult(EditEventIntent, AddNewEventActivity.ADD_EVENT_REQUEST);
    }
}

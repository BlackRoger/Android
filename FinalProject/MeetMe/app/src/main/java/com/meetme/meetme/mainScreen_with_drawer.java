package com.meetme.meetme;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.meetme.meetme.DataManagers.DataBase.EventInfo;
import com.meetme.meetme.DataManagers.DataManager;

public class mainScreen_with_drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DataManager.EventsReady {


    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    SolventRecyclerViewAdapter rcAdapter;
    Integer mCurrItemPosition;
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    ProgressDialog dialog;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_with_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displayWelcomeMessage(Profile.getCurrentProfile(), navigationView);

        /********************************************************/
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main_screen_add_new_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewEvent();
            }
        });

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        List<EventInfo> gaggeredList = DataManager.getInstance(this).GetMyEventsByMonth(new Date());

        rcAdapter = new SolventRecyclerViewAdapter(mainScreen_with_drawer.this, gaggeredList);
        recyclerView.setAdapter(rcAdapter);

        //String profile_id = Profile.getCurrentProfile().getId().toString();
        //GetActivities(profile_id);
        //dialog = ProgressDialog.show(mainScreen_with_drawer.this, "",
        //        "Loading. Please wait...", true);


        /******************************************************************/
    }

    public void GetEvents(List<EventInfo> Events) {
        if (Events.size() == 0){
            TextView textView = (TextView) findViewById(R.id.message);
            textView.setText(R.string.noActivity);
        }
        rcAdapter = new SolventRecyclerViewAdapter(mainScreen_with_drawer.this, Events);
        recyclerView.setAdapter(rcAdapter);
    }

    private void GetActivities(String friend_id) {
        DataManager.getInstance(getBaseContext()).FindEventByFriend(friend_id, this, true);
    }

    private void AddNewEvent() {
        // The floating action bar sends to add new event activity
        Intent AddEventIntent = new Intent(getApplicationContext(),
                AddNewEventActivity.class);
        AddEventIntent.putExtra(AddNewEventActivity.EVENT_DATE, new Date().getTime());
        startActivityForResult(AddEventIntent, AddNewEventActivity.ADD_EVENT_REQUEST);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen_with_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_friends) {
            //  startActivity(new Intent(this, FriendsScrollingActivity.class));
            startActivity(new Intent(this, FriendList.class));
        } else if (id == R.id.nav_my_callendar) {
            startActivity(new Intent(this, EventsCalendar.class));
        } else if (id == R.id.nav_search_events) {
            startActivity(new Intent(this, SearchEventsActivity.class));
        } else if (id == R.id.nav_Logout) {
            LoginManager.getInstance().logOut();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayWelcomeMessage(Profile profile, NavigationView navigationView) {
        if (profile != null) {

            // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            //  navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main_screen_with_drawer);

            Log.d("The Profile is:", profile.getName());
            ProfilePictureView profileImage = (ProfilePictureView) headerView.findViewById(R.id.profilePicture);
            if (profileImage != null)
                profileImage.setProfileId(profile.getId());

            TextView textView = (TextView) headerView.findViewById(R.id.userName);
            if (textView != null)
                textView.setText("Welcome " + profile.getName());


        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case (AddNewEventActivity.ADD_EVENT_REQUEST) :
                    rcAdapter.insertNewItem((EventInfo)data.getExtras().getSerializable(
                            AddNewEventActivity.EVENT_FINAL));
                    rcAdapter.notifyDataSetChanged();
                    break;
                case (AddNewEventActivity.UPDATE_EVENT_REQUEST) :
                    if (data.getExtras().getBoolean(AddNewEventActivity.EVENT_REMOVED)) {
                        rcAdapter.removeItemAtPosition(mCurrItemPosition);
                    } else {
                        rcAdapter.UpdateItemAtPosition(mCurrItemPosition,
                                (EventInfo) data.getExtras().getSerializable(AddNewEventActivity.EVENT_FINAL));
                    }

                    rcAdapter.notifyItemChanged(mCurrItemPosition);
                    break;
            }
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
        startActivityForResult(EditEventIntent, AddNewEventActivity.UPDATE_EVENT_REQUEST);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        List<EventInfo> gaggeredList = DataManager.getInstance(this).GetMyEventsByMonth(new Date());
        rcAdapter.UpdateList(gaggeredList);
        rcAdapter.notifyDataSetChanged();
   /*     String profile_id = Profile.getCurrentProfile().getId().toString();
        new GetActivities().execute(profile_id);
        dialog = ProgressDialog.show(mainScreen_with_drawer.this, "",
                "Loading. Please wait...", true);
*/
        // TODO: Make the list update on new events here
    }

}

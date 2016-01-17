package com.meetme.meetme;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import com.facebook.login.widget.ProfilePictureView;
import com.meetme.meetme.DataManagers.DataBase.EventInfo;
import com.meetme.meetme.DataManagers.DataManager;

public class mainScreen_with_drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        List<EventInfo> gaggeredList = DataManager.getInstance(this).GetMyEventsByMonth(new Date());
        //List<ItemObjects> gaggeredList = getListItemData();

        SolventRecyclerViewAdapter rcAdapter = new SolventRecyclerViewAdapter(mainScreen_with_drawer.this, gaggeredList);
        recyclerView.setAdapter(rcAdapter);

        /******************************************************************/
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
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_Logout) {
            //     startActivity(new Intent(this, LoginActivityFragment.class));
            finish();
        } else if (id == R.id.nav_send) {

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
                textView.setText("welcome " + profile.getName());


        }
    }

    public class SolventRecyclerViewAdapter extends RecyclerView.Adapter<SolventViewHolders> {

        private List<EventInfo> itemList;
        private Context context;

        public SolventRecyclerViewAdapter(Context context, List<EventInfo> itemList) {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public SolventViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.solvent_list, null);
            SolventViewHolders rcv = new SolventViewHolders(layoutView);
            return rcv;
        }

        @Override
        public void onBindViewHolder(SolventViewHolders holder, int position) {
            /*************************************/
            // Set the texts in the row
         //   txtName.setText(this.getItem(position).Name);
        //    txtDescription.setText(this.getItem(position).Description);

       //     String Duration = timeFormat.format(this.getItem(position).StartDate) + " - " +
       //             timeFormat.format(this.getItem(position).EndDate);
        //    txtTime.setText(Duration);
            /*************************************/
            holder.eventName.setText(itemList.get(position).Name);
           // holder.eventDescription.setText(itemList.get(position).Description);
            EventInfo.eEventTypes EventType = itemList.get(position).EventType;
            Integer EventSecondaryType = itemList.get(position).EventSecondaryType;
           // getEventTypeImage(EventType ,EventSecondaryType);
           // holder.eventTime.setText(itemList.get(position).getEventTime());

            holder.eventTypePhoto.setImageDrawable(getEventTypeImage(EventType, EventSecondaryType));
            //holder.eventTypePhoto= getEventTypeImage(EventType, EventSecondaryType);
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
            Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }


    private List<ItemObjects> getListItemData() {
        /****************/
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        // Now formattedDate have current date/time
        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
    //    List<EventInfo> list = DataManager.getInstance(this).GetMyEventsByMonth(new Date());
        // Date NewEventDate = new Date(DayViewer.EVENT_DATE);
        // Date mFromDate = new Date(NewEventDate.getTime());
        //    DataManager dataManager = new DataManager(null);
        //  Date date = new Date(c.DATE);
        //     dataManager.GetMyEventsByMonth(new Date());
        /*****************/
        List<ItemObjects> listViewItems = new ArrayList<ItemObjects>();
        listViewItems.add(new ItemObjects("Alkane", R.drawable.meetme));
        listViewItems.add(new ItemObjects("Ethane", R.drawable.meetme));
        listViewItems.add(new ItemObjects("Alkyne", R.drawable.meetme));
        listViewItems.add(new ItemObjects("Amide", R.drawable.meetme));

        return listViewItems;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Drawable getEventTypeImage(EventInfo.eEventTypes eventType, Integer eventSecondaryType) {
        ImageView imageView = null;
        Drawable drawable = null;

        switch (eventType) {
            case EVENT_TYPE_SPORT:
                switch (EventInfo.eSportEventTypes.values()[eventSecondaryType]) {
                    case EVENT_TYPE_TENNIS:
                        //imageView.setImageDrawable(R.drawable.tennis);
                        //imageView.setImageDrawable(getDrawable(R.drawable.tennis));
                        drawable = getDrawable(R.drawable.tennis);
                        break;
                    case EVENT_TYPE_SWIMMING:
                        break;
                    case EVENT_TYPE_SOCCER:
                       // imageView.setImageDrawable(getDrawable(R.drawable.soccer));
                        drawable = getDrawable(R.drawable.soccer);
                        break;
                    case EVENT_TYPE_GOLF:
                    //    imageView.setImageDrawable(getDrawable(R.drawable.golf));
                        drawable = getDrawable(R.drawable.golf);
                        break;
                    case EVENT_TYPE_FOOTBALL:
                        drawable = getDrawable(R.drawable.football);
                        break;
                }
                break;
            case EVENT_TYPE_SOCIAL:
                switch (EventInfo.eSocialEventTypes.values()[eventSecondaryType]) {
                    case EVENT_TYPE_CLUB:
                    //    imageView.setImageDrawable(getDrawable(R.drawable.meetme));
                        drawable = getDrawable(R.drawable.meetme);
                        break;
                    case EVENT_TYPE_HANGOUT:
                     //   imageView.setImageDrawable(getDrawable(R.drawable.meetme));
                        drawable = getDrawable(R.drawable.meetme);
                        break;
                    case EVENT_TYPE_MEETING:
                     //   imageView.setImageDrawable(getDrawable(R.drawable.meetme));
                        drawable = getDrawable(R.drawable.meetme);
                        break;
                }
                break;
            case EVENT_TYPE_OTHER:
                break;
            case EVENT_TYPE_FOOD:
                switch (EventInfo.eFoodEventTypes.values()[eventSecondaryType]) {
                    case EVENT_TYPE_DINNER:
                  //      imageView.setImageDrawable(getDrawable(R.drawable.meetme));
                        drawable = getDrawable(R.drawable.meetme);
                        break;
                    case EVENT_TYPE_LUNCH:
                  //      imageView.setImageDrawable(getDrawable(R.drawable.meetme));
                        drawable = getDrawable(R.drawable.meetme);
                        break;
                    case EVENT_TYPE_BREAKFAST:
                  //      imageView.setImageDrawable(getDrawable(R.drawable.meetme));
                        drawable = getDrawable(R.drawable.meetme);
                        break;
                }
                break;

        }
        return drawable;
    }
}

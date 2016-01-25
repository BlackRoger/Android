package com.meetme.meetme.DataManagers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.Profile;
import com.meetme.meetme.DataManagers.DataBase.DataBaseOp;
import com.meetme.meetme.DataManagers.DataBase.EventInfo;
import com.meetme.meetme.DataManagers.DataBase.EventTable;
import com.meetme.meetme.DataManagers.DataBase.UserInfo;
import com.meetme.meetme.FriendList;
import com.meetme.meetme.FriendsActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;


public class DataManager {
    private static DataManager mInstance;
    private DataBaseOp mDb;
    private EventInfo tmpEvent;
    private List<EventInfo> mReceivedEvents;

    public interface EventsReady {
        public void GetEvents(List<EventInfo> Events);
    }

    public DataManager(Context context) {
        mDb = new DataBaseOp(context);
        ParseObject.registerSubclass(EventInfo.class);
        // Add your initialization code here
        Parse.initialize(context);
    }

    public static DataManager getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new DataManager(context);
        }

        return mInstance;
    }

    public UserInfo GetMyInfo() {
        return mDb.GetMyInfo();
    }

    public void SetMyInfo(String iName) {
        // TODO: get real secondary id.
        mDb.AddUser(new UserInfo(iName, true, 1234));
    }

    public List<EventInfo> GetMyEventsByMonth(Date MonthDate) {
        Date DayStart;
        Date DayEnd;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(MonthDate);
        calendar.set(Calendar.HOUR, calendar.getActualMinimum(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        DayStart = calendar.getTime();

        calendar.set(Calendar.HOUR, calendar.getActualMaximum(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        DayEnd = calendar.getTime();

        return mDb.GetMyEventsInRange(DayStart, DayEnd, "");
    }

    public List<EventInfo> GetEventsByDay(Date DayDate) {
        Date DayStart;
        Date DayEnd;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DayDate);
        calendar.set(Calendar.HOUR, calendar.getActualMinimum(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        DayStart = calendar.getTime();

        calendar.set(Calendar.HOUR, calendar.getActualMaximum(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
        DayEnd = calendar.getTime();

        return mDb.GetMyEventsInRange(DayStart, DayEnd, "");
    }

    public void AddEvent(EventInfo event) {
        mDb.AddEvent(event);
        event.SaveEvent();
        tmpEvent = event;
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                tmpEvent.SecondaryId = tmpEvent.getObjectId();
                mDb.UpdateEvent(tmpEvent, tmpEvent);
            }
        });
    }

    public void UpdateEvent(EventInfo NewEvent) {
        EventInfo OldEvent = mDb.FindEventById(NewEvent.Id);
        NewEvent.SecondaryId = OldEvent.SecondaryId;
        mDb.UpdateEvent(OldEvent, NewEvent);

        NewEvent.setObjectId(OldEvent.SecondaryId);
        NewEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            }
        });
    }

    public void RemoveEvent(EventInfo Event) {
        // Our copy of the event may not have had its SecondaryId initialized, so get it
        EventInfo tmpEvent = mDb.FindEventById(Event.Id);

        mDb.RemoveEvent(Event.Id);
        tmpEvent.setObjectId(tmpEvent.SecondaryId);
        tmpEvent.deleteInBackground();
    }

    public void FindEventByFriend(String FriendId, final EventsReady Callback, boolean IncludesMe) {
        FindEventByFilter(new String[] {EventTable.EventInfo.ORGANIZER}, new Object[] {FriendId},
                Callback, IncludesMe);
    }

    public void FindEventByFilter(String Columns[], Object Values[], final EventsReady Callback,
                                  boolean IncludesMe) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(EventTable.EventInfo.TABLE_NAME);
        boolean WasFriendSpecified = false;

        for (int i = 0; i < Columns.length; i++) {
            // Was a specific friend specified?
            if (Columns[i] == EventTable.EventInfo.ORGANIZER)
                WasFriendSpecified = true;

            query.whereEqualTo(Columns[i], Values[i]);
        }

        // If a friend was not specified, show all events or all events but my own?
        if (!IncludesMe && !WasFriendSpecified) {
            query.whereNotEqualTo(EventTable.EventInfo.ORGANIZER,
                    Profile.getCurrentProfile().getId().toString());
        }

        mReceivedEvents = new ArrayList<EventInfo>();

        // Retrieve the object by id
        query.findInBackground(new FindCallback<ParseObject>() {  //retrieve serverID instead of object from parse
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject parseEvent : objects) {
                        EventInfo event = new EventInfo(parseEvent.getString(EventTable.EventInfo.NAME),
                                parseEvent.getString(EventTable.EventInfo.DESCRIPTION),
                                parseEvent.getString(EventTable.EventInfo.ORGANIZER),
                                Integer.valueOf(parseEvent.getInt(EventTable.EventInfo.PARTICIPATION_CAP)),
                                Long.valueOf(parseEvent.getLong(EventTable.EventInfo.START_DATE)),
                                Long.valueOf(parseEvent.getLong(EventTable.EventInfo.END_DATE)),
                                EventInfo.eEventTypes.values()[parseEvent.getInt(EventTable.EventInfo.EVENT_TYPE)],
                                Integer.valueOf(parseEvent.getInt(EventTable.EventInfo.EVENT_SECONDARY_TYPE)),
                                EventInfo.eReccurence.values()[parseEvent.getInt(EventTable.EventInfo.RECURRENCE)],
                                parseEvent.getString(EventTable.EventInfo.LOCATION),
                                parseEvent.getObjectId(),
                                Long.valueOf(0));

                        mReceivedEvents.add(event);
                    }
                }

                Callback.GetEvents(mReceivedEvents);
            }
        });
    }
}


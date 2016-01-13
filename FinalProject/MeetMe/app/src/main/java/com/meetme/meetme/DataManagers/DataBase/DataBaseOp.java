package com.meetme.meetme.DataManagers.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by azolotushko on 11/30/2015.
 */
public class DataBaseOp extends SQLiteOpenHelper {

    private static final String COMMA_SEP = ", ";
    private static final String AND_SEP = " AND ";
    private static final String OR_SEP = " OR ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";

    private static final String DATABASE_NAME = "Client_DB";
    public static final int database_version = 3;

    private static final String CREATE_EVENT_TABLE_QUERY = "create table " +
            EventTable.EventInfo.TABLE_NAME + "(" +
            EventTable.EventInfo._ID + " INTEGER PRIMARY KEY," +
            EventTable.EventInfo.NAME + TEXT_TYPE + COMMA_SEP +
            EventTable.EventInfo.DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            EventTable.EventInfo.EVENT_TYPE + INT_TYPE + COMMA_SEP +
            EventTable.EventInfo.EVENT_SECONDARY_TYPE + INT_TYPE + COMMA_SEP +
            EventTable.EventInfo.ORGANIZER + INT_TYPE + COMMA_SEP +
            EventTable.EventInfo.PARTICIPATION_CAP + INT_TYPE + COMMA_SEP +
            EventTable.EventInfo.RECURRENCE + INT_TYPE + COMMA_SEP +
            EventTable.EventInfo.START_DATE + INT_TYPE + COMMA_SEP +
            EventTable.EventInfo.END_DATE + INT_TYPE + COMMA_SEP +
            EventTable.EventInfo.LOCATION + TEXT_TYPE + COMMA_SEP +
            EventTable.EventInfo.SECONDARY_ID + INT_TYPE + ");";

    private static final String CREATE_USER_TABLE_QUERY = "create table " +
            UserTable.UserInfo.TABLE_NAME + "(" +
            UserTable.UserInfo._ID + " INTEGER PRIMARY KEY, " +
            UserTable.UserInfo.NAME + TEXT_TYPE + COMMA_SEP +
            UserTable.UserInfo.IS_ME + INT_TYPE + COMMA_SEP +
            UserTable.UserInfo.SECONDARY_ID + INT_TYPE + ");";

    private static final String CREATE_PARTICIPATION_TABLE_QUERY = "create table " +
            ParticipationTable.ParticipationInfo.TABLE_NAME + "(" +
            ParticipationTable.ParticipationInfo._ID + " INTEGER PRIMARY KEY," +
            ParticipationTable.ParticipationInfo.EVENT_ID + INT_TYPE + COMMA_SEP +
            ParticipationTable.ParticipationInfo.PARTICIPANT + INT_TYPE + COMMA_SEP +
            ParticipationTable.ParticipationInfo.SECONDARY_ID + INT_TYPE + ");";

    private static final String DELETE_USER_TABLE = "drop table " +
            UserTable.UserInfo.TABLE_NAME + ";";
    private static final String DELETE_EVENT_TABLE = "drop table " +
            EventTable.EventInfo.TABLE_NAME + ";";
    private static final String DELETE_PARTICIPATION_TABLE = "drop table " +
            ParticipationTable.ParticipationInfo.TABLE_NAME + ";";

    public DataBaseOp(Context context){
        super(context, DATABASE_NAME, null, database_version);
        Log.d(" dataBase operations", "dataBase created");
    }

    @Override
    public void onCreate(SQLiteDatabase sdb){
        Log.d(" dataBase operations", "Table created");
        sdb.execSQL(CREATE_USER_TABLE_QUERY);
        sdb.execSQL(CREATE_EVENT_TABLE_QUERY);
        sdb.execSQL(CREATE_PARTICIPATION_TABLE_QUERY);
    }

    @Override
    public void onUpgrade (SQLiteDatabase arg0, int arg1, int arg2){
        arg0.execSQL(DELETE_PARTICIPATION_TABLE);
        arg0.execSQL(DELETE_EVENT_TABLE);
        arg0.execSQL(DELETE_USER_TABLE);

        arg0.execSQL(CREATE_USER_TABLE_QUERY);
        arg0.execSQL(CREATE_EVENT_TABLE_QUERY);
        arg0.execSQL(CREATE_PARTICIPATION_TABLE_QUERY);
    }

    public void AddUser(UserInfo User){
        SQLiteDatabase SQ = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(UserTable.UserInfo.NAME, User.Name);
        cv.put(UserTable.UserInfo.IS_ME, new Integer(User.IsMe == Boolean.TRUE ? 1 : 0));
        cv.put(UserTable.UserInfo.SECONDARY_ID, User.SecondaryId);

        long k = SQ.insert(UserTable.UserInfo.TABLE_NAME, null, cv);
        if (k == -1) {
            Log.d(" dataBase operations", "Failed to insert row");
        } else {
            Log.d(" dataBase operations", "One raw inserted");
        }
    }

    public void AddEvent(EventInfo Event){
        SQLiteDatabase SQ = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EventTable.EventInfo.NAME, Event.Name);
        cv.put(EventTable.EventInfo.DESCRIPTION, Event.Description);
        cv.put(EventTable.EventInfo.EVENT_TYPE, Event.EventType.ordinal());
        cv.put(EventTable.EventInfo.EVENT_SECONDARY_TYPE, Event.EventSecondaryType);
        cv.put(EventTable.EventInfo.ORGANIZER, Event.OrganizerId);
        cv.put(EventTable.EventInfo.PARTICIPATION_CAP, Event.ParticipationCap);
        cv.put(EventTable.EventInfo.RECURRENCE, Event.Recurrence.ordinal());
        cv.put(EventTable.EventInfo.START_DATE, Event.StartDate);
        cv.put(EventTable.EventInfo.END_DATE, Event.EndDate);
        cv.put(EventTable.EventInfo.LOCATION, Event.Location);
        cv.put(EventTable.EventInfo.SECONDARY_ID, Event.SecondaryId);

        long k = SQ.insert(EventTable.EventInfo.TABLE_NAME, null, cv);
        if (k == -1) {
            Log.d(" dataBase operations", "Failed to insert row");
        } else {
            Log.d(" dataBase operations", "One raw inserted");
            Event.Id = k;
        }
    }

    public void AddParticipation(ParticipationInfo Participation){
        SQLiteDatabase SQ = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ParticipationTable.ParticipationInfo.PARTICIPANT, Participation.ParticipantId);
        cv.put(ParticipationTable.ParticipationInfo.EVENT_ID, Participation.EventId);
        cv.put(ParticipationTable.ParticipationInfo.SECONDARY_ID, Participation.SecondaryId);

        long k = SQ.insert(ParticipationTable.ParticipationInfo.TABLE_NAME, null, cv);
        if (k == -1) {
            Log.d(" dataBase operations", "Failed to insert row");
        } else {
            Log.d(" dataBase operations", "One raw inserted");
        }
    }

    public UserInfo GetMyInfo() {
        SQLiteDatabase SQ = this.getReadableDatabase();
        String[] columns = {UserTable.UserInfo.NAME, UserTable.UserInfo.SECONDARY_ID};
        Cursor CR = SQ.query(UserTable.UserInfo.TABLE_NAME, columns,
                UserTable.UserInfo.IS_ME + "=1 ", null, null, null, null);

        // Check if we found the user.
        if (!CR.moveToFirst())
            return null;

        return new UserInfo(CR.getString(0), true, CR.getInt(1));
    }

    public List<EventInfo> GetMyEventsInRange(Date RangeStart, Date RangeEnd,
                                              String AdditionalFilters) {
        String strRangeStartInMillis = String.valueOf(RangeStart.getTime());
        String strRangeEndInMillis = String.valueOf(RangeEnd.getTime());
        String NewFilter;

        // Check if there are additional filters
        if (AdditionalFilters != null && AdditionalFilters.compareTo("") != 0) {
            NewFilter = "(" + EventTable.EventInfo.END_DATE + ">= " + strRangeStartInMillis + AND_SEP +
                    EventTable.EventInfo.START_DATE + "<= " + strRangeEndInMillis + ")" + AND_SEP +
                    "(" + AdditionalFilters + ")";
        } else {
            NewFilter = EventTable.EventInfo.END_DATE + ">= " + strRangeStartInMillis + AND_SEP +
                    EventTable.EventInfo.START_DATE + "<= " + strRangeEndInMillis;
        }

        return GetMyEventsByFilter(NewFilter);
    }

    public List<EventInfo> GetMyEventsByFilter(String Filters) {
        SQLiteDatabase SQ = this.getReadableDatabase();
        String[] columns = {EventTable.EventInfo.NAME, EventTable.EventInfo.DESCRIPTION,
                EventTable.EventInfo.ORGANIZER, EventTable.EventInfo.PARTICIPATION_CAP,
                EventTable.EventInfo.START_DATE, EventTable.EventInfo.END_DATE,
                EventTable.EventInfo.EVENT_TYPE, EventTable.EventInfo.EVENT_SECONDARY_TYPE,
                EventTable.EventInfo.RECURRENCE, EventTable.EventInfo.LOCATION,
                EventTable.EventInfo.SECONDARY_ID, EventTable.EventInfo._ID};
        Cursor cr = SQ.query(EventTable.EventInfo.TABLE_NAME, columns, Filters,
                null, null, null, null);

        // Now transform the cursor to a normal event list
        cr.moveToFirst();
        ArrayList<EventInfo> EventsList = new ArrayList<EventInfo>();

        while (!cr.isAfterLast())
        {
            EventInfo currEvent = new EventInfo(cr.getString(0), cr.getString(1),
                    cr.getInt(2), cr.getInt(3),
                    cr.getLong(4), cr.getLong(5),
                    EventInfo.eEventTypes.values()[cr.getInt(6)], cr.getInt(7),
                    EventInfo.eReccurence.values()[cr.getInt(8)], cr.getString(9),
                    cr.getInt(10), cr.getLong(11));

            EventsList.add(currEvent);
            cr.moveToNext();
        }

        return EventsList;
    }

    public void UpdateEvent(EventInfo OldEvent, EventInfo NewEvent){
        String selection = EventTable.EventInfo._ID + "= ?";
        String args[] = {String.valueOf(OldEvent.Id)};
        SQLiteDatabase SQ = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(EventTable.EventInfo.NAME, NewEvent.Name);
        cv.put(EventTable.EventInfo.DESCRIPTION, NewEvent.Description);
        cv.put(EventTable.EventInfo.EVENT_TYPE, NewEvent.EventType.ordinal());
        cv.put(EventTable.EventInfo.EVENT_SECONDARY_TYPE, NewEvent.EventSecondaryType);
        cv.put(EventTable.EventInfo.ORGANIZER, NewEvent.OrganizerId);
        cv.put(EventTable.EventInfo.PARTICIPATION_CAP, NewEvent.ParticipationCap);
        cv.put(EventTable.EventInfo.RECURRENCE, NewEvent.Recurrence.ordinal());
        cv.put(EventTable.EventInfo.START_DATE, NewEvent.StartDate);
        cv.put(EventTable.EventInfo.END_DATE, NewEvent.EndDate);
        cv.put(EventTable.EventInfo.LOCATION, NewEvent.Location);
        cv.put(EventTable.EventInfo.SECONDARY_ID, NewEvent.SecondaryId);

        SQ.update(EventTable.EventInfo.TABLE_NAME, cv, selection, args);
    }

    public void RemoveEvent(Integer EventSecondaryId){
        String selection = EventTable.EventInfo.SECONDARY_ID + "= ?";
        String args[] = {EventSecondaryId.toString()};
        SQLiteDatabase SQ = this.getWritableDatabase();
        SQ.delete(EventTable.EventInfo.TABLE_NAME, selection, args);
    }
}

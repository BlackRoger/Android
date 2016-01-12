package com.meetme.meetme.DataManagers.DataBase;

import android.provider.BaseColumns;

/**
 * Created by Guy on 09-Jan-16.
 */
public class EventTable {

    public static abstract class EventInfo implements BaseColumns {
        public static final String TABLE_NAME = "Event_Table";
        public static final String NAME = "Name";
        public static final String DESCRIPTION = "Description";
        public static final String ORGANIZER = "Organizer";
        public static final String PARTICIPATION_CAP = "Participation_Cap";
        public static final String START_DATE = "Start_Date";
        public static final String END_DATE = "End_Date";
        public static final String EVENT_TYPE = "Event_Type";
        public static final String EVENT_SECONDARY_TYPE = "Event_Secondary_Type";
        public static final String RECURRENCE = "Recurrence";
        public static final String LOCATION = "Location";
        public static final String SECONDARY_ID = "Secondary_Id";
    }
}

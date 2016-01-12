package com.guyard.meetme.DataManagers.DataBase;

import android.provider.BaseColumns;

/**
 * Created by Guy on 09-Jan-16.
 */
public class ParticipationTable {

    public static abstract class ParticipationInfo implements BaseColumns {
        public static final String TABLE_NAME = "Participation_Table";
        public static final String PARTICIPANT = "Participant_Id";
        public static final String EVENT_ID = "Event_Id";
        public static final String SECONDARY_ID = "Secondary_Id";
    }
}

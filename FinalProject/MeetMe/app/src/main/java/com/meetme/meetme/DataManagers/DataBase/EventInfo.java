package com.meetme.meetme.DataManagers.DataBase;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.Serializable;

/**
 * Created by Guy on 09-Jan-16.
 */

@ParseClassName(EventTable.EventInfo.TABLE_NAME)
public class EventInfo extends ParseObject implements Serializable{

    public enum eReccurence {
        RECCURENCE_NONE,
        RECCURENCE_DAILY,
        RECCURENCE_WORK_DAY,
        RECCURENCE_WEEKLY,
        RECCURENCE_BI_WEEKLY,
        RECCURENCE_MONTHLY,
        RECCURENCE_YEARLY,
        RECCURENCE_NUM
    }

    public enum eEventTypes {
        EVENT_TYPE_OTHER,
        EVENT_TYPE_SPORT,
        EVENT_TYPE_FOOD,
        EVENT_TYPE_SOCIAL,
    }

    public enum eSportEventTypes {
        EVENT_TYPE_OTHER,
        EVENT_TYPE_FOOTBALL,
        EVENT_TYPE_SOCCER,
        EVENT_TYPE_TENNIS,
        EVENT_TYPE_GOLF,
        EVENT_TYPE_SWIMMING,
    }

    public enum eFoodEventTypes {
        EVENT_TYPE_OTHER,
        EVENT_TYPE_BREAKFAST,
        EVENT_TYPE_LUNCH,
        EVENT_TYPE_DINNER,
    }

    public enum eSocialEventTypes {
        EVENT_TYPE_OTHER,
        EVENT_TYPE_MEETING,
        EVENT_TYPE_HANGOUT,
        EVENT_TYPE_CLUB,
    }

    public Long Id;
    public String Name;
    public String Description;
    public String OrganizerId;
    public Integer ParticipationCap;
    public Long StartDate;
    public Long EndDate;
    public eEventTypes EventType;
    public Integer EventSecondaryType;
    public eReccurence Recurrence;
    public String Location;
    public String SecondaryId;

    public EventInfo() {
        super();
    }
    public EventInfo(String iName, String iDescription, String iOrganizerId,
                     Integer iParticipationCap, Long iStartDate, Long iEndDate,
                     eEventTypes iEventType, Integer iEventSecondaryType, eReccurence iRecurrence,
                     String iLocation, String iSecondaryId, Long iId) {
        super();
        Name = iName;
        Description = iDescription;
        OrganizerId = iOrganizerId;
        ParticipationCap = iParticipationCap;
        StartDate = iStartDate;
        EndDate = iEndDate;
        EventType = iEventType;
        EventSecondaryType = iEventSecondaryType;
        Recurrence = iRecurrence;
        Location = iLocation;
        SecondaryId = iSecondaryId;
        Id = iId;
    }

    public void SaveEvent(){
        put(EventTable.EventInfo.DESCRIPTION, Description);
        put(EventTable.EventInfo.NAME, Name);
        put(EventTable.EventInfo.ORGANIZER, OrganizerId);
        put(EventTable.EventInfo.PARTICIPATION_CAP, ParticipationCap);
        put(EventTable.EventInfo.START_DATE, StartDate);
        put(EventTable.EventInfo.END_DATE, EndDate);
        put(EventTable.EventInfo.EVENT_TYPE, EventType.ordinal());
        put(EventTable.EventInfo.EVENT_SECONDARY_TYPE, EventSecondaryType);
        put(EventTable.EventInfo.RECURRENCE, Recurrence.ordinal());
        put(EventTable.EventInfo.LOCATION, Location);
    }
}

package com.meetme.meetme.DataManagers.DataBase;

import java.io.Serializable;

/**
 * Created by Guy on 09-Jan-16.
 */
public class EventInfo extends Object implements Serializable{




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
    public Integer OrganizerId;
    public Integer ParticipationCap;
    public Long StartDate;
    public Long EndDate;
    public eEventTypes EventType;
    public Integer EventSecondaryType;
    public eReccurence Recurrence;
    public String Location;
    public Integer SecondaryId;

    public EventInfo() {}
    public EventInfo(String iName, String iDescription, Integer iOrganizerId,
                     Integer iParticipationCap, Long iStartDate, Long iEndDate,
                     eEventTypes iEventType, Integer iEventSecondaryType, eReccurence iRecurrence,
                     String iLocation, Integer iSecondaryId, Long iId) {
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
}

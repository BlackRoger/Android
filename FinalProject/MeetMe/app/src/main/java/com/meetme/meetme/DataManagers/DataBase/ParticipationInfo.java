package com.meetme.meetme.DataManagers.DataBase;

/**
 * Created by Guy on 09-Jan-16.
 */
public class ParticipationInfo {
    public Integer ParticipantId;
    public Integer EventId;
    public Integer SecondaryId;

    public ParticipationInfo() {}
    public ParticipationInfo(Integer iParticipationId, Integer iEventId, Integer iSecondaryId) {
        ParticipantId = iParticipationId;
        EventId = iEventId;
        SecondaryId = iSecondaryId;
    }
}

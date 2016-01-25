package com.meetme.meetme.DataManagers.DataBase;

import com.parse.ParseObject;


public class ParticipationInfo extends ParseObject {
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

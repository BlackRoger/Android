package com.meetme.meetme.DataManagers.DataBase;

import com.parse.ParseObject;

/**
 * Created by Guy on 09-Jan-16.
 */
public class UserInfo extends ParseObject {
    public String Name;
    public Boolean IsMe;
    public Integer SecondaryId;

    public UserInfo() {}
    public UserInfo(String iName, boolean iIsMe, Integer iSecondaryId) {
        Name = iName;
        IsMe = iIsMe;
        SecondaryId = iSecondaryId;
    }
}

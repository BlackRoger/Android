package com.meetme.meetme.DataManagers;

import android.content.Context;

import com.meetme.meetme.DataManagers.DataBase.DataBaseOp;
import com.meetme.meetme.DataManagers.DataBase.EventInfo;
import com.meetme.meetme.DataManagers.DataBase.UserInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by Guy on 09-Jan-16.
 */
public class DataManager {
    private static DataManager mInstance;
    private DataBaseOp mDb;

    private DataManager(Context context) {
        mDb = new DataBaseOp(context);
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
        return mDb.GetMyEventsByMonth(MonthDate);
    }

    public List<EventInfo> GetEventsByDay(Date DayDate) {
        return mDb.GetMyEventsByDay(DayDate);
    }

    public void AddEvent(EventInfo event) {
        mDb.AddEvent(event);
    }
}


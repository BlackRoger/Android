package com.meetme.meetme.DataManagers;

import android.content.Context;

import com.meetme.meetme.DataManagers.DataBase.DataBaseOp;
import com.meetme.meetme.DataManagers.DataBase.EventInfo;
import com.meetme.meetme.DataManagers.DataBase.UserInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Guy on 09-Jan-16.
 */
public class DataManager {
    private static DataManager mInstance;
    private DataBaseOp mDb;

    public DataManager(Context context) {
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
    }

    public void UpdateEvent(EventInfo OldEvent, EventInfo NewEvent) {
        mDb.UpdateEvent(OldEvent, NewEvent);
    }
}


package com.ex3.exersice3;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Guy on 15-Nov-15.
 */
public class JokeManager {
    public static final String PREFERENCE_FILE = "com.ex3.exercise3.prefs";
    public final String PREFERENCE_JOKE_TEXT = "com.ex3.exercise3.joke_text";
    public final String PREFERENCE_JOKE_AUTHOR = "com.ex3.exercise3.joke_author";
    public final String PREFERENCE_JOKE_DATE = "com.ex3.exercise3.joke_date";
    public final String PREFERENCE_JOKE_LIKE_STATE = "com.ex3.exercise3.joke_like_state";
    public final String PREFERENCE_JOKE_BACKGROUND_COLOR = "com.ex3.exercise3.joke_background_color";
    public final String PREFERENCE_JOKES_NUM = "com.ex3.exercise3.joke_num";

    public enum eJokeColor {
        JOKE_COLOR_RED,
        JOKE_COLOR_BLUE,
        JOKE_COLOR_GREEN,
        JOKE_COLOR_NUM
    }

    private static JokeManager ourInstance;
    private static Context myContext;
    public ArrayList<JokeData> list;
    public long JokesNum;
    private DataBaseOp db;

    public static JokeManager getInstance(Context context)
    {
        if (ourInstance == null)
        {
            ourInstance = new JokeManager(context);
        }

        return ourInstance;
    }

    public void ChangeBackgroundColor(eJokeColor NewColor)
    {
        SharedPreferences prefs = myContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(PREFERENCE_JOKE_BACKGROUND_COLOR, NewColor.ordinal());
        editor.commit();
    }

    public eJokeColor GetBackgroundColor()
    {
        SharedPreferences prefs = myContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        int colorOrdinal = prefs.getInt(PREFERENCE_JOKE_BACKGROUND_COLOR, eJokeColor.JOKE_COLOR_NUM.ordinal());
        return(eJokeColor.values()[colorOrdinal]);
    }

    public void AddToList(JokeData data)
    {
        list.add(data);
        db.InsertJoke(data.Author, data.Joke, data.LikeState.ordinal(), data.Creation_Date);

        SharedPreferences prefs = myContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREFERENCE_JOKES_NUM, list.size());
        editor.commit();
    }

    public void UpdateJokeText(int JokePosition, String NewJoke)
    {
        String OldJoke = list.get(JokePosition).Joke;
        list.get(JokePosition).Joke = NewJoke;
        db.UpdateJoke(OldJoke, list.get(JokePosition));
    }

    public void UpdateJokeLikeState(int JokePosition, JokeData.eLikeState LikeState)
    {
        list.get(JokePosition).LikeState = LikeState;
        db.UpdateJoke(list.get(JokePosition).Joke, list.get(JokePosition));
    }

    public void RemoveFromList(JokeData data)
    {
        list.remove(data);
        db.deleteJoke(data.Joke);

        SharedPreferences prefs = myContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREFERENCE_JOKES_NUM, list.size());
        editor.commit();
    }

    public void SaveList()
    {
        /*
        SharedPreferences prefs = myContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Integer index = 0;

        for (JokeData data : list)
        {
            editor.putString(PREFERENCE_JOKE_TEXT + index.toString(), data.Joke);
            editor.putString(PREFERENCE_JOKE_AUTHOR + index.toString(), data.Author);
            editor.putLong(PREFERENCE_JOKE_DATE + index.toString(), data.Creation_Date.getTime());
            editor.putInt(PREFERENCE_JOKE_LIKE_STATE + index.toString(), data.LikeState.ordinal());
            index++;
        }

        editor.putLong(PREFERENCE_JOKES_NUM, JokesNum);
        editor.commit();
        */
    }

    private ArrayList<JokeData> LoadList()
    {
        list.clear();
        Cursor cr = db.getJokes();
        cr.moveToFirst();

        while (!cr.isAfterLast())
        {
            String JokeText = cr.getString(1);
            String JokeAuthor = cr.getString(0);
            int LikeStateOrdinal = cr.getInt(2);
            JokeData.eLikeState JokeLinkState = JokeData.eLikeState.values()[LikeStateOrdinal];

            Date JokeDate;

            try {
                JokeDate = new Date();
                JokeDate.setDate(cr.getInt(3));
            } catch (Exception e) {
                System.out.println(e.toString());
                continue;
            }

            list.add(new JokeData(JokeText, JokeAuthor, JokeDate, JokeLinkState));
            cr.moveToNext();
        }

        return list;
    }

    private JokeManager(Context context) {
        myContext = context;
        list = new ArrayList<JokeData>();
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        db = new DataBaseOp(context);

        // Check if this is the first time starting this project.
        // If so create default jokes.
        JokesNum = prefs.getLong(PREFERENCE_JOKES_NUM, -1);

        // If so, we will create 3 jokes.
        if (JokesNum == -1)
        //if (true)
        {
            String[] Jokes = new String[] { "Knock knock", "A rabi, a priest and a mexican walk into a bar", "What is round and yellow?"};
            String[] Authors = new String[] { "Maya", "Ayalon", "Gandalf"};
            JokeData.eLikeState[] LikeStates = new JokeData.eLikeState[] {
                    JokeData.eLikeState.JOKE_LIKE, JokeData.eLikeState.JOKE_DISLIKE, JokeData.eLikeState.JOKE_UNDECIDED};
            Calendar calendar = Calendar.getInstance();

            list = new ArrayList<JokeData>();
            Date[] date = new Date[3];

            calendar.set(2000, 2, 1);
            date[0] = calendar.getTime();
            calendar.set(2010, 4, 5);
            date[1] = calendar.getTime();
            calendar.set(2013, 6, 6);
            date[2] = calendar.getTime();

            for (int i = 0; i < Jokes.length; ++i) {
                AddToList(new JokeData(Jokes[i], Authors[i], date[i], LikeStates[i]));
            }

            JokesNum = list.size();
            SaveList();
        }
        else {
            LoadList();
        }
    }
}

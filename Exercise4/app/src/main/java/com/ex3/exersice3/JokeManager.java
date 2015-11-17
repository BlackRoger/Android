package com.ex3.exersice3;

import android.content.Context;
import android.content.SharedPreferences;
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
    public final String PREFERENCE_JOKE_DAY = "com.ex3.exercise3.joke_day";
    public final String PREFERENCE_JOKE_MONTH = "com.ex3.exercise3.joke_month";
    public final String PREFERENCE_JOKE_YEAR = "com.ex3.exercise3.joke_year";
    public final String PREFERENCE_JOKES_NUM = "com.ex3.exercise3.joke_num";

    private static JokeManager ourInstance;
    private static Context myContext;
    public ArrayList<JokeData> list;
    public long JokesNum;

    public static JokeManager getInstance(Context context)
    {
        if (ourInstance == null)
        {
            ourInstance = new JokeManager(context);
        }

        return ourInstance;
    }

    public void SaveList()
    {
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
    }

    private ArrayList<JokeData> LoadList()
    {
        SharedPreferences prefs = myContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        list = new ArrayList<JokeData>();

        for (Integer index = 0; index < JokesNum; index++)
        {
            String JokeText = prefs.getString(PREFERENCE_JOKE_TEXT + index.toString(), "");
            String JokeAuthor = prefs.getString(PREFERENCE_JOKE_TEXT + index.toString(), "");
            int LikeStateOrdinal = prefs.getInt(PREFERENCE_JOKE_LIKE_STATE + index.toString(), 0);
            JokeData.eLikeState JokeLinkState = JokeData.eLikeState.values()[LikeStateOrdinal];

            Date JokeDate;

            try {
                JokeDate = new Date(prefs.getLong(PREFERENCE_JOKE_DATE + index.toString(), 0));
            } catch (Exception e) {
                System.out.println(e.toString());
                continue;
            }

            list.add(new JokeData(JokeText, JokeAuthor, JokeDate, JokeLinkState));
            index++;
        }

        return list;
    }

    private JokeManager(Context context) {
        myContext = context;
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);

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
                list.add(new JokeData(Jokes[i], Authors[i], date[i], LikeStates[i]));
            }

            JokesNum = list.size();
            SaveList();
        }
        else {
            LoadList();
        }
    }
}

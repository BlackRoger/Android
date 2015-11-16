package com.ex3.exersice3;

import java.util.Date;


/**
 * Created by Guy on 14-Nov-15.
 */
public class JokeData extends Object{
    String Joke;
    String Author;
    Date Creation_Date;

    public enum eLikeState {
        JOKE_LIKE,
        JOKE_DISLIKE,
        JOKE_UNDECIDED
    }

    eLikeState LikeState;

    public JokeData(String joke, String author, Date date, eLikeState likeState)
    {
        Joke = joke;
        Author = author;
        Creation_Date = date;
        LikeState = likeState;
    }

    @Override
    public String toString()
    {
        return Joke + "\r\n" + Author;
    }
}

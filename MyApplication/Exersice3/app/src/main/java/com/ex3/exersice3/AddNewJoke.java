package com.ex3.exersice3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;

public class AddNewJoke extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_joke);
    }

    /** Called when the user clicks the Send button */
    public void addJoke(View view) {
        String JokeText = ((EditText)findViewById(R.id.add_joke_field)).getText().toString();
        String JokeAuthor = ((EditText)findViewById(R.id.add_author_field)).getText().toString();
        Date JokeDate = new Date();
        /*
        try {
            JokeDate = DateFormat.getInstance().parse(((EditText) findViewById(R.id.add_date_field)).getText().toString());
        } catch (Exception e) {
            System.out.println(e.toString());
        }*/

        JokeData joke = new JokeData(JokeText, JokeAuthor, JokeDate, JokeData.eLikeState.JOKE_UNDECIDED);
        JokeManager.getInstance(getApplicationContext()).list.add(joke);
        JokeManager.getInstance(getApplicationContext()).JokesNum++;
        JokeManager.getInstance(getApplicationContext()).SaveList();
        finish();
    }
}

package com.ex3.exersice3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditJoke extends AppCompatActivity {

    JokeData joke;
    int position;

    /** Called when the user clicks the Send button */
    public void handleClick(View view) {
        switch (view.getId()) {
            case (R.id.like_button): {
                JokeManager.getInstance(getApplicationContext()).list.get(position).LikeState =
                        JokeData.eLikeState.JOKE_LIKE;
                break;
            }
            case (R.id.dislike_button): {
                JokeManager.getInstance(getApplicationContext()).list.get(position).LikeState =
                        JokeData.eLikeState.JOKE_DISLIKE;
                break;
            }
            case (R.id.delete_button): {
                JokeManager.getInstance(getApplicationContext()).list.remove(position);
                JokeManager.getInstance(getApplicationContext()).JokesNum--;
                JokeManager.getInstance(getApplicationContext()).SaveList();
                finish();
                break;
            }
            case (R.id.edit_done_button): {
                JokeManager.getInstance(getApplicationContext()).SaveList();
                finish();
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_joke);
        position = getIntent().getIntExtra(JokesList.INTENT_PARAM_JOKE_INDEX, 0);
        joke = JokeManager.getInstance(this).list.get(position);

        TextView text = (TextView) findViewById(R.id.edit_joke_text);
        text.setText(joke.Joke);

        // Now prepare the button listeners
        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case (R.id.like_button): {
                        joke.LikeState = JokeData.eLikeState.JOKE_LIKE;
                        break;
                    }
                    case (R.id.dislike_button): {
                        joke.LikeState = JokeData.eLikeState.JOKE_DISLIKE;
                        break;
                    }
                    case (R.id.delete_button): {
                        JokeManager.getInstance(getApplicationContext()).list.remove(position);
                        JokeManager.getInstance(getApplicationContext()).JokesNum--;
                        JokeManager.getInstance(getApplicationContext()).SaveList();
                        finishActivity(0);
                        break;
                    }
                    case (R.id.edit_done_button): {
                        JokeManager.getInstance(getApplicationContext()).SaveList();
                        finishActivity(0);
                        break;
                    }
                }
            }
        };
    }
}

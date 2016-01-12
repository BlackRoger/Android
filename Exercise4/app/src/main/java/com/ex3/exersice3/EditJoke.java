package com.ex3.exersice3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditJoke extends MenuActivity {

    JokeData joke;
    int position;
    Context CTX = this;
    DataBaseOp DOP = new DataBaseOp(CTX);
    String author, joke_text;


    /** Called when the user clicks the Send button */
    public void handleClick(View view) {
        switch (view.getId()) {
            case (R.id.like_button): {
                JokeManager.getInstance(getApplicationContext()).UpdateJokeLikeState(position, JokeData.eLikeState.JOKE_LIKE);

                break;
            }
            case (R.id.dislike_button): {
                JokeManager.getInstance(getApplicationContext()).UpdateJokeLikeState(position, JokeData.eLikeState.JOKE_DISLIKE);

                break;
            }
            case (R.id.delete_button): {
                joke_text = ((EditText)findViewById(R.id.edit_joke_text)).getText().toString();
               // author = ((EditText)findViewById(R.id.)).getText().toString();
             //   author = "art"; /* DEBUG */
                JokeManager.getInstance(this).RemoveFromList(joke);
                Toast.makeText(getBaseContext(), "Joke deleted",Toast.LENGTH_LONG ).show();
             //   JokeManager.getInstance(getApplicationContext()).list.remove(position);
              //  JokeManager.getInstance(getApplicationContext()).JokesNum--;
               // JokeManager.getInstance(getApplicationContext()).SaveList();
                finish();
                break;
            }
            case (R.id.edit_done_button): {
                finish();
                break;
            }
            case (R.id.edit_joke_button): {
                JokeManager.getInstance(getApplicationContext()).UpdateJokeText(position, ((EditText) findViewById(R.id.edit_joke_text)).getText().toString());
                Toast.makeText(EditJoke.this, "Text changed succesfully", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit_joke);
        position = getIntent().getIntExtra(JokesList.INTENT_PARAM_JOKE_INDEX, 0);
        joke = JokeManager.getInstance(this).list.get(position);

        TextView text = (TextView) findViewById(R.id.edit_joke_text);
        text.setText(joke.Joke);
        updateBackgroundColor(R.id.layout_joke_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_add_joke).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        updateBackgroundColor(R.id.layout_joke_edit);
        return true;
    }
}

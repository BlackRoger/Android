package com.ex3.exersice3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class AddNewJoke extends MenuActivity {

    final int MIN_CHARS_PER_JOKE = 10;
    public static JokeData newJoke;

   // EditText JOKE_AUTHOR, JOKE_TEXT;
    //String joke_author, joke_text;
    //Button AddJoke;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_joke);
        updateBackgroundColor(R.id.layout_joke_add);
    }

    private void saveJoke(JokeData data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        newJoke = data;

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_save_joke_message)
                .setTitle(R.string.dialog_save_joke_title);

        // If the user chooses to cancel.
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        // If the user chooses to exit.
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                JokeManager.getInstance(getApplicationContext()).AddToList(AddNewJoke.newJoke);
                finish();
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean validateJoke(JokeData data) {
        // Check if the joke is ok
        if (data.Joke.length() < MIN_CHARS_PER_JOKE) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.dialog_joke_too_short_message)
                    .setTitle(R.string.dialog_error_title);

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();

            return false;
        }

        return true;
    }

    /** Called when the user clicks the Send button */
    public void addJoke(View view) {
        String JokeText = ((EditText)findViewById(R.id.add_joke_field)).getText().toString();
        String JokeAuthor = ((EditText)findViewById(R.id.add_author_field)).getText().toString();
        Date JokeDate = new Date();

        JokeData joke = new JokeData(JokeText, JokeAuthor, JokeDate, JokeData.eLikeState.JOKE_UNDECIDED);
        if (validateJoke(joke)) {
            saveJoke(joke);
        }
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
        updateBackgroundColor(R.id.layout_joke_add);
        return true;
    }
}

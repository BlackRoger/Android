package com.ex3.exersice3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class JokesList extends AppCompatActivity {
    String[] Jokes = new String[] { "Knock knock", "A rabi, a priest and a mexican walk into a bar", "What is round and yellow?"};
    String[] Authors = new String[] { "Maya", "Ayalon", "Gandalf"};
    Calendar calendar = Calendar.getInstance();

    public static final String INTENT_PARAM_JOKE_INDEX = "com.ex3.exercise3.joke_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes_list);

        final ListView listview = (ListView) findViewById(R.id.jokeList);

        final ArrayAdapter<JokeData> adapter = new ArrayAdapter<JokeData>(this,
                R.layout.joke_list_element, JokeManager.getInstance(this).list) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.joke_list_element, parent, false);

                TextView JokeLine = (TextView) rowView.findViewById(R.id.firstLine);
                TextView AuthorLine = (TextView) rowView.findViewById(R.id.secondLine);
                ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

                if (this.getItem(position).LikeState == JokeData.eLikeState.JOKE_LIKE) {
                    imageView.setImageResource(R.drawable.like);
                }
                else if (this.getItem(position).LikeState == JokeData.eLikeState.JOKE_DISLIKE) {
                    imageView.setImageResource(R.drawable.dislike);
                }
                else if (this.getItem(position).LikeState == JokeData.eLikeState.JOKE_UNDECIDED) {
                    imageView.setImageResource(R.drawable.like_dislike);
                }

                JokeLine.setText(this.getItem(position).Joke);
                AuthorLine.setText(this.getItem(position).Author + ", " + this.getItem(position).Creation_Date.toString());

                return rowView;
            }
        };

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final JokeData item = (JokeData) parent.getItemAtPosition(position);

                Intent intent = new Intent(parent.getContext(), EditJoke.class);
                intent.putExtra(INTENT_PARAM_JOKE_INDEX, position);
                startActivity(intent);
            }

        });

        Button button = (Button)findViewById(R.id.addJoke);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewJoke.class);
                startActivity(intent);
            }
        });
    }

        @Override
    protected void onResume() {
        super.onResume();
        final ListView listview = (ListView) findViewById(R.id.jokeList);
        ArrayAdapter<JokeData> adapter = (ArrayAdapter<JokeData>)listview.getAdapter();
        adapter.notifyDataSetChanged();
        //listview.destroyDrawingCache();
    }
}

package com.ex3.exersice3;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class JokesList extends MenuActivity {
    String[] Jokes = new String[] { "Knock knock", "A rabi, a priest and a mexican walk into a bar", "What is round and yellow?"};
    String[] Authors = new String[] { "Maya", "Ayalon", "Gandalf"};
    Calendar calendar = Calendar.getInstance();
    Context CTX = this;

    public static final String INTENT_PARAM_JOKE_INDEX = "com.ex3.exercise3.joke_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes_list);

    // ADD FRAGMENT
        JokeFragment frag = new JokeFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout_joke_list, frag, "JokeFrag" );
        transaction.commit();



        updateBackgroundColor(R.id.layout_joke_list);

        final ListView listview = (ListView) findViewById(R.id.jokeList);
        registerForContextMenu(listview);


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
    }

        @Override
    protected void onResume() {
        super.onResume();
        final ListView listview = (ListView) findViewById(R.id.jokeList);
        ArrayAdapter<JokeData> adapter = (ArrayAdapter<JokeData>)listview.getAdapter();
            adapter.notifyDataSetChanged();
        //listview.destroyDrawingCache();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_menu)
            return true;

        super.onOptionsItemSelected(item);
        updateBackgroundColor(R.id.layout_joke_list);
        return true;
   /*   switch (item.getItemId()) {
            case R.id.menu_item_add_joke:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.menu_item_change_background:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
            case R.id.menu_item_exit:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_jokes_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_menu_edit:
                Intent intent = new Intent(getApplicationContext(), EditJoke.class);
                intent.putExtra(INTENT_PARAM_JOKE_INDEX, info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_delete:
                JokeManager manager = JokeManager.getInstance(getApplicationContext());
                manager.list.remove((int)info.id);
                manager.JokesNum--;
                manager.SaveList();

                final ListView listview = (ListView) findViewById(R.id.jokeList);
                ArrayAdapter<JokeData> adapter = (ArrayAdapter<JokeData>)listview.getAdapter();
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

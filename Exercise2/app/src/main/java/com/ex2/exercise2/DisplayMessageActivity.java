package com.ex2.exercise2;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {
    private final static String BUTTON_TEXT = "Next";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);
        setContentView(textView);

        Button button = new Button(this);
        button.setTextSize(40);
        button.setText(this.BUTTON_TEXT);
        button.setLayoutParams(new view.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        addContentView(button, new ViewGroup.LayoutParams());

    }

    /** Called when the user clicks the Next button */
    public void next(View view) {
        Intent intent = new Intent(this, activity3.class);
        //    EditText editText = (EditText) findViewById(R.id.edit_message);
        //  String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar
        // automatically handles clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {

        super.onStart();
        Log.d("Activity2", "OnStart");
    }

    @Override
    protected void onRestart() {

        super.onRestart();
        Log.d("Activity2", "OnRestart");
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.d("Activity2", "OnResume");
    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.d("Activity2", "OnResume");
    }

    @Override
    protected void onStop() {

        super.onStop();
        Log.d("Activity2", "OnStop");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.d("Activity2", "OnDestroy");
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_display_message,
                    container, false);
            return rootView;
        }
    }
    */

}

package com.meetme.meetme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.Profile;
import com.meetme.meetme.DataManagers.DataManager;
import com.parse.Parse;
import com.parse.ParseObject;

public class LoginActivity extends AppCompatActivity implements LoginActivityFragment.LoginListener {

    private Toolbar toolbar;
    boolean bFirstTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bFirstTime = true;

        /*
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
  //      toolbar = (Toolbar) findViewById(R.id.app_bar);
  //      setSupportActionBar(toolbar);

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.menu){
           // startActivity(new Intent(this, mainScreen_with_drawer.class));
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(bFirstTime && Profile.getCurrentProfile() != null) {
            startActivity(new Intent(this, mainScreen_with_drawer.class));
            bFirstTime = false;
        } else {
            addButton();

           /* Button btn= new Button(this);
            btn.setText("Submit");
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //your write code
                }
            }); */
        }
    }

    private void addButton() {

    }

    @Override
    public void SuccessfulLogin(Profile profile) {

        if (DataManager.getInstance(this).GetMyInfo() == null)
            DataManager.getInstance(this).SetMyInfo(profile.getName());

        startActivity(new Intent(this, mainScreen_with_drawer.class));
    }
}

package com.meetme.meetme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.Profile;

public class LoginActivity extends AppCompatActivity implements LoginActivityFragment.LoginListener {

    private Toolbar toolbar;
    boolean bFirstTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bFirstTime = true;
  //      toolbar = (Toolbar) findViewById(R.id.app_bar);
  //      setSupportActionBar(toolbar);
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
        if(bFirstTime) {
            startActivity(new Intent(this, mainScreen_with_drawer.class));
            bFirstTime = false;
        }
    }

    @Override
    public void SuccessfulLogin(Profile profile) {
        startActivity(new Intent(this, mainScreen_with_drawer.class));
    }
}

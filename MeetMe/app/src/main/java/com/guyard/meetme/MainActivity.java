package com.guyard.meetme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.guyard.meetme.DataManagers.DataBase.UserInfo;
import com.guyard.meetme.DataManagers.DataManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserInfo MyInfo = DataManager.getInstance(this).GetMyInfo();

        if (MyInfo == null) {
            DataManager.getInstance(this).SetMyInfo("Joshua Cohen");
        }

        Intent CallCalendar = new Intent(this, EventsCalendar.class);
        startActivity(CallCalendar);
    }
}

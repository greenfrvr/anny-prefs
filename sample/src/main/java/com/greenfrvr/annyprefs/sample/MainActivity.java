package com.greenfrvr.annyprefs.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.greenfrvr.annyprefs.compiled.ConfigPrefs;
import com.greenfrvr.annyprefs.compiled.PrefsAdapter;
import com.greenfrvr.annyprefs.compiled.RestoreConfig;
import com.greenfrvr.annyprefs.compiled.RestoreUser;
import com.greenfrvr.annyprefs.compiled.SaveUser;
import com.greenfrvr.annyprefs.compiled.UserPrefs;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SaveUser saveUser;
    RestoreUser restoreUser;

    RestoreConfig restoreConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ConfigPrefs config = PrefsAdapter.config(this);
        restoreConfig = config.restore();

        config.save()
                .lastVisit(new Date().getTime())
                .visits(config.restore().visits() + 1)
                .async();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        initUserPrefs();
        saveUserInfo();
    }

    private void initUserPrefs() {
        final UserPrefs prefs = PrefsAdapter.user(this);
        restoreUser = prefs.restore();
        saveUser = prefs.save();
    }

    private void saveUserInfo() {
        if(restoreUser.firstLaunch()){
            saveUser.username("beingericgreen")
                    .age(22)
                    .email("being.eric.green@gmail.com")
                    .firstLaunch(false)
                    .async();
        }
    }

    @Override
    public void onClick(View v) {
        System.out.println("SAVED: " + PrefsAdapter.config(this).save().subscribed(!PrefsAdapter.config(this).restore().subscribed()).sync());

        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        System.out.println("USER RESTORED: " + restoreUser.username() + " || " + restoreUser.email() + " || " + restoreUser.age());
        System.out.println("CONFIG RESTORED: " + restoreConfig.lastVisit() + " || " + restoreConfig.subscribed() + " || " + restoreConfig.visits() + " || " + restoreConfig.query());
    }
}

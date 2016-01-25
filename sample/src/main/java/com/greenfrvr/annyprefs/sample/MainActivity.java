package com.greenfrvr.annyprefs.sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.greenfrvr.annyprefs.AnnyPrefs;
import com.greenfrvr.annyprefs.ConfigPrefs;
import com.greenfrvr.annyprefs.RestoreConfig;
import com.greenfrvr.annyprefs.RestoreUser;
import com.greenfrvr.annyprefs.SaveUser;
import com.greenfrvr.annyprefs.UserPrefs;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    SaveUser saveUser;
    RestoreUser restoreUser;

    RestoreConfig restoreConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ConfigPrefs config = AnnyPrefs.config(this);
        restoreConfig = config.restore();

        config.save()
                .lastVisit(new Date())
                .launches(config.restore().launches() + 1)
                .async();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        initUserPrefs();
        saveUserInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnnyPrefs.user(this).registerListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnnyPrefs.user(this).unregisterListener(this);
    }

    private void initUserPrefs() {
        final UserPrefs prefs = AnnyPrefs.user(this);
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
        boolean isSubs = AnnyPrefs.config(this).restore().isSubscribed();
        AnnyPrefs.config(this).save().isSubscribed(!isSubs).sync();
        System.out.println("SAVED: " + !isSubs);
        if (!isSubs) {
            AnnyPrefs.config(this).remove().channels().sync();
            System.out.println(AnnyPrefs.config(this).restore().channels());

            Address m = new Address();
            m.streetName = "string";
            m.streetNum = 123;
            m.countryCode = 375;

            System.out.println("JSONJSONJSON: " + new Gson().toJson(m, Address.class));

            saveUser.address(m).async();
        } else {
            AnnyPrefs.config(this).save().channels(new HashSet<>(Arrays.asList("android", "github", "news")));
            System.out.println(AnnyPrefs.config(this).restore().channels());

            Address m = restoreUser.address();
            if (m != null) {
                System.out.println("DESERIALIZED: " + m.toString());
            } else {
                System.out.println("GOT NULL");
            }

            System.out.println("RESTORED MODEL ~~~ " + restoreUser.address());
        }

        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        System.out.println("USER RESTORED: " + restoreUser.username() + " || " + restoreUser.email() + " || " + restoreUser.age());
        System.out.println("CONFIG RESTORED: " + restoreConfig.lastVisit() + " || " + restoreConfig.isSubscribed() + " || " + restoreConfig.launches() + " || " + restoreConfig.lastQuery());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        System.out.println("PREFERENCE CHANGED: " + key);
    }
}

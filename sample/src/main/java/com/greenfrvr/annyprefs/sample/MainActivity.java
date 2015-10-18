package com.greenfrvr.annyprefs.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.greenfrvr.annyprefs.Prefs;
import com.greenfrvr.annyprefs.compiled.PrefsAdapter;
import com.greenfrvr.annyprefs.compiled.RestoreUser;
import com.greenfrvr.annyprefs.compiled.SaveUser;
import com.greenfrvr.annyprefs.compiled.UserPrefs;

public class MainActivity extends AppCompatActivity {

    SaveUser saveUser;
    RestoreUser restoreUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                System.out.println("RESTORED: " + restoreUser.username() + " || " + restoreUser.email() + " || " + restoreUser.age());
                System.out.println(User.class.getCanonicalName());
            }
        });

        PrefsAdapter.user(this);

        UserPrefs prefs = new UserPrefs(this);
        saveUser = prefs.save();
        restoreUser = prefs.restore();

        if(restoreUser.firstLaunch()){
            saveUser.username("beingericgreen");
            saveUser.email("being.eric.green@gmail.com");
            saveUser.age(22);
            saveUser.firstLaunch(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }
}

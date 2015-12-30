package com.greenfrvr.annyprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by greenfrvr
 */
public abstract class Prefs<S extends Save, R extends Restore> implements Preferences<S, R>, SaveMethod {

    private SharedPreferences.Editor editor;

    @Override
    public abstract S save();

    @Override
    public abstract R restore();

    @Override
    public abstract void clear();

    @Override
    public boolean sync() {
        boolean state = editor().commit();
        editor = null;
        return state;
    }

    @Override
    public void async() {
        editor().apply();
        editor = null;
    }

    protected SharedPreferences shared() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    protected SharedPreferences.Editor editor() {
        if (editor == null) {
            editor = shared().edit();
        }
        return editor;
    }

    protected abstract Context getContext();
}

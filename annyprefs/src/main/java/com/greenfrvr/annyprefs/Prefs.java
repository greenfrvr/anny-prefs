package com.greenfrvr.annyprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by greenfrvr
 */
public abstract class Prefs<S extends Save, R extends Restore> implements Preferences<S, R> {

    @Override
    public abstract S save();

    @Override
    public abstract S saveAsync();

    @Override
    public abstract R restore();

    @Override
    public abstract void clear();

    protected SharedPreferences shared() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    protected SharedPreferences.Editor editor() {
        return shared().edit();
    }

    protected abstract Context getContext();
}

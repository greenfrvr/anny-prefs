package com.greenfrvr.annyprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by greenfrvr
 */
abstract class Prefs<S extends Save, R extends Restore, D extends Remove> implements Preferences<S, R, D>, TransactionMethod {

    private Context context;
    private SharedPreferences.Editor editor;

    public Prefs(Context context) {
        this.context = context;
    }

    protected String name() {
        return "";
    }

    @Override
    public abstract S save();

    @Override
    public abstract R restore();

    @Override
    public abstract D remove();

    @Override
    public void clear() {
        editor().clear().apply();
        editor = null;
    }

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

    protected Context getContext() {
        return context;
    }

    protected SharedPreferences shared() {
        if (name().isEmpty()) {
            return PreferenceManager.getDefaultSharedPreferences(getContext());
        }
        return getContext().getSharedPreferences(getContext().getPackageName() + "_anny_" + name(), Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor editor() {
        if (editor == null) {
            editor = shared().edit();
        }
        return editor;
    }

    @Override
    public void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        shared().registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        shared().registerOnSharedPreferenceChangeListener(listener);
    }


}

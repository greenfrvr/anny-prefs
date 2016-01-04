package com.greenfrvr.annyprefs;

import android.content.SharedPreferences;

/**
 * Created by greenfrvr
 */
public interface Preferences<S extends Save, R extends Restore, D extends Remove> {

    String name();

    S save();

    R restore();

    D remove();

    void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener);

    void unregisterListener(SharedPreferences.OnSharedPreferenceChangeListener listener);

    void clear();

}

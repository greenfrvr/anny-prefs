package com.greenfrvr.annyprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by greenfrvr
 */
public abstract class Prefs<S extends Save, R extends Restore, D extends Remove> implements Preferences<S, R, D>, TransactionMethod {

    private Context context;
    private SharedPreferences.Editor editor;

    protected Prefs(Context context) {
        this.context = context;
    }

    protected String name() {
        return "";
    }

    /**
     * @return interface which provides saving SharedPreferences properties operation.
     */
    @Override
    public abstract S save();

    /**
     * @return interface which provides access to persisted SharedPreferences properties.
     */
    @Override
    public abstract R restore();

    /**
     * @return interface which allows to remove persisted SharedPreferences properties.
     */
    @Override
    public abstract D remove();

    /**
     * Clears all data of current SharedPreferences instance.
     */
    @Override
    public void clear() {
        editor().clear().apply();
        editor = null;
    }

    /**
     * Saving all applied changes. Works by using SharedPreferences.Editor.commit() method.
     * @return true if changes were successfully written to persistent storage.
     */
    @Override
    public boolean sync() {
        boolean state = editor().commit();
        editor = null;
        return state;
    }

    /**
     * Saving all applied changes. Works by using SharedPreferences.Editor.apply() method.
     */
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

    /**
     * Registers a callback to be invoked when a change happens to a preference.
     * @param listener - The callback that will run.
     */
    @Override
    public void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        shared().registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregisters a previous callback.
     * @param listener - The callback that should be unregistered.
     */
    @Override
    public void unregisterListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        shared().unregisterOnSharedPreferenceChangeListener(listener);
    }

}

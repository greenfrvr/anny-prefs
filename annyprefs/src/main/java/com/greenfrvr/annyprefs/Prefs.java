package com.greenfrvr.annyprefs;

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


}

package com.greenfrvr.annyprefs;

/**
 * Created by greenfrvr
 */
public interface Preferences<S extends Save, R extends Restore> {

    S save();

    S saveAsync();

    R restore();

    void clear();

}

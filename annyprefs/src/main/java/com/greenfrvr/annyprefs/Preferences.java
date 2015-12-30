package com.greenfrvr.annyprefs;

/**
 * Created by greenfrvr
 */
public interface Preferences<S extends Save, R extends Restore> {

    S save();

    R restore();

    void clear();

}

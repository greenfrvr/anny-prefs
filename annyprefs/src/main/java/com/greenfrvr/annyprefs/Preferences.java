package com.greenfrvr.annyprefs;

/**
 * Created by greenfrvr
 */
public interface Preferences<S extends Save, R extends Restore, D extends Remove> {

    String name();

    S save();

    R restore();

    D remove();

    void clear();

}

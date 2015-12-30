package com.greenfrvr.annyprefs;

/**
 * Created by greenfrvr on 12/30/15.
 */
public interface SaveMethod {
    boolean sync();

    void async();
}

package com.greenfrvr.annyprefs.compiler;

import com.greenfrvr.annyprefs.compiler.prefs.PrefField;

import java.util.List;

/**
 * Created by greenfrvr
 */
public interface DataSource {

    String name();

    String prefsName();

    String packageName();

    List<PrefField> prefs();

}

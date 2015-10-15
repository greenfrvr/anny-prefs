package com.greenfrvr.annyprefs.compiler.prefs;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public interface PrefField<T> {

    void setSource(Element element);

    String name();

    String key();

    T value();
}

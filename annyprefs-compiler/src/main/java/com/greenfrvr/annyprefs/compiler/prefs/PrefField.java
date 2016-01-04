package com.greenfrvr.annyprefs.compiler.prefs;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public interface PrefField<T> {

    void setSource(Element element);

    String name();

    String key();

    T value();

    TypeName fieldClass();

    String methodName();
}

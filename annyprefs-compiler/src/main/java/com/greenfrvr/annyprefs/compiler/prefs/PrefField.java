package com.greenfrvr.annyprefs.compiler.prefs;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public interface PrefField<T> {

    void setSource(Element element);

    String name();

    boolean hasResKey();

    String key();

    T value();

    TypeName fieldClass();

    String methodName();

    void putRestoreStatement(MethodSpec.Builder builder);

    void putSaveStatement(MethodSpec.Builder builder);

}

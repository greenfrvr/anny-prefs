package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.BoolPref;
import com.greenfrvr.annyprefs.annotation.StringPref;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class StringField implements PrefField<String> {

    private Element el;

    public StringField() {
    }

    @Override
    public void setSource(Element el) {
        this.el = el;
    }

    @Override
    public String name() {
        return el.getSimpleName().toString();
    }

    @Override
    public boolean hasResKey() {
        return el.getAnnotation(StringPref.class).keyRes() > 0;
    }

    @Override
    public String key() {
        int res = el.getAnnotation(StringPref.class).keyRes();
        if (res > 0) {
            return String.valueOf(res);
        }

        String key = el.getAnnotation(StringPref.class).key();
        if (key.isEmpty()) {
            key = name();
        }
        return key;
    }

    @Override
    public String value() {
        return el.getAnnotation(StringPref.class).value();
    }

    @Override
    public TypeName fieldClass() {
        return ClassName.get(String.class);
    }

    @Override
    public String methodName() {
        return Utils.STRING;
    }

    @Override
    public void putRestoreStatement(MethodSpec.Builder builder) {
        String statement = hasResKey() ? Utils.PREFS_RESTORE_STRING_VALUE_RES : Utils.PREFS_RESTORE_STRING_VALUE;
        builder.addStatement(statement, methodName(), key(), value());
    }

    @Override
    public void putSaveStatement(MethodSpec.Builder builder) {
        String statement = hasResKey() ? Utils.PREFS_PUT_VALUE_RES : Utils.PREFS_PUT_VALUE;
        builder.addParameter(fieldClass(), "value").addStatement(statement, methodName(), key());
    }

    @Override
    public String toString() {
        return "StringField{" +
                "name=" + name() +
                ", key=" + key() +
                ", value=" + value() +
                '}';
    }
}

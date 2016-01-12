package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.BoolPref;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class BooleanField implements PrefField<Boolean> {

    private Element el;

    public BooleanField() {
    }

    @Override
    public void setSource(Element element) {
        this.el = element;
    }

    @Override
    public String name() {
        return el.getSimpleName().toString();
    }

    @Override
    public boolean hasResKey() {
        return el.getAnnotation(BoolPref.class).keyRes() > 0;
    }

    @Override
    public String key() {
        int res = el.getAnnotation(BoolPref.class).keyRes();
        if (res > 0) {
            return String.valueOf(res);
        }

        String key = el.getAnnotation(BoolPref.class).key();
        if (key.isEmpty()) {
            key = name();
        }
        return key;
    }

    @Override
    public Boolean value() {
        return el.getAnnotation(BoolPref.class).value();
    }

    @Override
    public TypeName fieldClass() {
        return ClassName.get(Boolean.class);
    }

    @Override
    public String methodName() {
        return Utils.BOOLEAN;
    }

    @Override
    public void putRestoreStatement(MethodSpec.Builder builder) {
        String statement = hasResKey() ? Utils.PREFS_RESTORE_VALUE_RES : Utils.PREFS_RESTORE_VALUE;
        builder.addCode(statement, methodName(), key(), value());
    }

    @Override
    public void putSaveStatement(MethodSpec.Builder builder) {
        String statement = hasResKey() ? Utils.PREFS_PUT_VALUE_RES : Utils.PREFS_PUT_VALUE;
        builder.addParameter(fieldClass(), "value").addCode(statement, methodName(), key());
    }

    @Override
    public String toString() {
        return "BooleanField{" +
                "name=" + name() +
                ", key=" + key() +
                ", value=" + value() +
                '}';
    }
}

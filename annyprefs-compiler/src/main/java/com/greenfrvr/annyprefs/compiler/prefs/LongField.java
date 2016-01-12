package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.LongPref;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class LongField implements PrefField<Long> {

    private Element el;

    public LongField() {
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
        return el.getAnnotation(LongPref.class).keyRes() > 0;
    }

    @Override
    public String key() {
        int res = el.getAnnotation(LongPref.class).keyRes();
        if (res > 0) {
            return String.valueOf(res);
        }

        String key = el.getAnnotation(LongPref.class).key();
        if (key.isEmpty()) {
            key = name();
        }
        return key;
    }

    @Override
    public Long value() {
        return el.getAnnotation(LongPref.class).value();
    }

    @Override
    public TypeName fieldClass() {
        return ClassName.get(Long.class);
    }

    @Override
    public String methodName() {
        return Utils.LONG;
    }

    @Override
    public void putRestoreStatement(MethodSpec.Builder builder) {
        String statement = hasResKey() ? Utils.GET_LONG_VALUE_RES : Utils.GET_LONG_VALUE;
        builder.addCode(statement, methodName(), key(), value());
    }

    @Override
    public void putSaveStatement(MethodSpec.Builder builder) {
        String statement = hasResKey() ? Utils.PUT_VALUE_RES : Utils.PUT_VALUE;
        builder.addParameter(fieldClass(), "value").addCode(statement, methodName(), key());
    }

    @Override
    public String toString() {
        return "LongField{" +
                "name=" + name() +
                ", key=" + key() +
                ", value=" + value() +
                '}';
    }
}

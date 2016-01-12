package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.DatePref;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.Date;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class DateField implements PrefField<Long> {

    private Element el;

    public DateField() {
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
        return el.getAnnotation(DatePref.class).keyRes() > 0;
    }


    @Override
    public String key() {
        int res = el.getAnnotation(DatePref.class).keyRes();
        if (res > 0) {
            return String.valueOf(res);
        }

        String key = el.getAnnotation(DatePref.class).key();
        if (key.isEmpty()) {
            key = name();
        }
        return key;
    }

    @Override
    public Long value() {
        return el.getAnnotation(DatePref.class).value();
    }

    @Override
    public TypeName fieldClass() {
        return ClassName.get(Date.class);
    }

    @Override
    public String methodName() {
        return Utils.LONG;
    }

    @Override
    public void putRestoreStatement(MethodSpec.Builder builder) {
        String statement = hasResKey() ? Utils.GET_DATE_VALUE_RES : Utils.GET_DATE_VALUE;
        builder.addCode(statement, fieldClass(), methodName(), key(), value());
    }

    @Override
    public void putSaveStatement(MethodSpec.Builder builder) {
        String statement = hasResKey() ? Utils.PUT_DATE_VALUE_RES : Utils.PUT_DATE_VALUE;
        builder.addParameter(fieldClass(), "value").addCode(statement, methodName(), key());
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

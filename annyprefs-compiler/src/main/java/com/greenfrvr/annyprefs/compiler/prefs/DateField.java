package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.DatePref;
import com.greenfrvr.annyprefs.compiler.utils.GeneratorUtil;
import com.squareup.javapoet.ClassName;
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

    public DateField(Element el) {
        this.el = el;
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
    public String key() {
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
        return GeneratorUtil.LONG;
    }

    @Override
    public String putValueStatement() {
        return GeneratorUtil.PREFS_PUT_DATE_VALUE;
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

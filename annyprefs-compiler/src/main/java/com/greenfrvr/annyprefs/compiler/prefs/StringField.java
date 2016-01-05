package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.StringPref;
import com.greenfrvr.annyprefs.compiler.utils.GeneratorUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class StringField implements PrefField<String> {

    private Element el;

    public StringField() {
    }

    public StringField(Element el) {
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
        return GeneratorUtil.STRING;
    }

    @Override
    public String putValueStatement() {
        return GeneratorUtil.PREFS_PUT_VALUE;
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

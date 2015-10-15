package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.StringPref;

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
        return el.getAnnotation(StringPref.class).key();
    }

    @Override
    public String value() {
        return el.getAnnotation(StringPref.class).value();
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

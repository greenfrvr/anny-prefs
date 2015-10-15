package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.LongPref;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class LongField implements PrefField<Long> {

    private Element el;

    public LongField() {
    }

    public LongField(Element el) {
        this.el = el;
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
    public String key() {
        return el.getAnnotation(LongPref.class).key();
    }

    @Override
    public Long value() {
        return el.getAnnotation(LongPref.class).value();
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

package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.BoolPref;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class BooleanField implements PrefField<Boolean> {

    private Element el;

    public BooleanField() {
    }

    public BooleanField(Element el) {
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
        return el.getAnnotation(BoolPref.class).key();
    }

    @Override
    public Boolean value() {
        return el.getAnnotation(BoolPref.class).value();
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

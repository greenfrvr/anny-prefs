package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.FloatPref;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class FloatField implements PrefField<Float> {

    private Element el;

    public FloatField() {
    }

    public FloatField(Element el) {
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
        return el.getAnnotation(FloatPref.class).key();
    }

    @Override
    public Float value() {
        return el.getAnnotation(FloatPref.class).value();
    }

    @Override
    public String toString() {
        return "FloatField{" +
                "name=" + name() +
                ", key=" + key() +
                ", value=" + value() +
                '}';
    }
}

package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.FloatPref;
import com.greenfrvr.annyprefs.annotation.StringPref;
import com.greenfrvr.annyprefs.compiler.GeneratorUtil;

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
        String key = el.getAnnotation(FloatPref.class).key();
        if (key.isEmpty()) {
            key = name();
        }
        return key;
    }

    @Override
    public Float value() {
        return el.getAnnotation(FloatPref.class).value();
    }

    @Override
    public Class<Float> fieldClass() {
        return Float.class;
    }

    @Override
    public String methodName() {
        return GeneratorUtil.METHOD_FLOAT;
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

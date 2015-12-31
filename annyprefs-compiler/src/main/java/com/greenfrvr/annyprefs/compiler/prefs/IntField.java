package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.IntPref;
import com.greenfrvr.annyprefs.compiler.utils.GeneratorUtil;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class IntField implements PrefField<Integer> {

    private Element el;

    public IntField() {
    }

    public IntField(Element el) {
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
        String key = el.getAnnotation(IntPref.class).key();
        if (key.isEmpty()) {
            key = name();
        }
        return key;
    }

    @Override
    public Integer value() {
        return el.getAnnotation(IntPref.class).value();
    }

    @Override
    public Class<Integer> fieldClass() {
        return Integer.class;
    }

    @Override
    public String methodName() {
        return GeneratorUtil.INT;
    }

    @Override
    public String toString() {
        return "IntField{" +
                "name=" + name() +
                ", key=" + key() +
                ", value=" + value() +
                '}';
    }
}

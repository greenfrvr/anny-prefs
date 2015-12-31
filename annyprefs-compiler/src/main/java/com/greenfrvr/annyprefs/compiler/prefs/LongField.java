package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.LongPref;
import com.greenfrvr.annyprefs.compiler.utils.GeneratorUtil;

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
    public Class<Long> fieldClass() {
        return Long.class;
    }

    @Override
    public String methodName() {
        return GeneratorUtil.LONG;
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

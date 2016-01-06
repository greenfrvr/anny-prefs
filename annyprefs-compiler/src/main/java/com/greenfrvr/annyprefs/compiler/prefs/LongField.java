package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.LongPref;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class LongField implements PrefField<Long> {

    private Element el;

    public LongField() {
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
    public TypeName fieldClass() {
        return ClassName.get(Long.class);
    }

    @Override
    public String methodName() {
        return Utils.LONG;
    }

    @Override
    public void putRestoreStatement(MethodSpec.Builder builder) {
        builder.addStatement(Utils.PREFS_RESTORE_LONG_VALUE, methodName(), key(), value());
    }

    @Override
    public void putSaveStatement(MethodSpec.Builder builder) {
        builder.addParameter(fieldClass(), "value").addStatement(Utils.PREFS_PUT_VALUE, methodName(), key());
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

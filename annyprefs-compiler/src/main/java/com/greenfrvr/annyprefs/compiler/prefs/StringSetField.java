package com.greenfrvr.annyprefs.compiler.prefs;

import com.google.common.collect.Sets;
import com.greenfrvr.annyprefs.annotation.StringSetPref;
import com.greenfrvr.annyprefs.compiler.utils.GeneratorUtil;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class StringSetField implements PrefField<Set> {

    private Element el;

    public StringSetField() {
    }

    public StringSetField(Element el) {
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
        String key = el.getAnnotation(StringSetPref.class).key();
        if (key.isEmpty()) {
            key = name();
        }
        return key;
    }

    @Override
    public Set<String> value() {
        return Sets.newHashSet(el.getAnnotation(StringSetPref.class).value());
    }

    @Override
    public TypeName fieldClass() {
        return ParameterizedTypeName.get(Set.class, String.class);
    }

    @Override
    public String methodName() {
        return GeneratorUtil.STRING_SET;
    }

    @Override
    public void putRestoreStatement(MethodSpec.Builder builder) {
        if (value().isEmpty()) {
            builder.addStatement(GeneratorUtil.PREFS_RESTORE_SET_EMPTY_VALUE, methodName(), key(), null);
        } else {
            TypeName setType = ParameterizedTypeName.get(HashSet.class, String.class);
            builder.addStatement(GeneratorUtil.restoreSetStatement(this), methodName(), key(), setType, Arrays.class);
        }
    }

    @Override
    public void putSaveStatement(MethodSpec.Builder builder) {
        builder.addParameter(fieldClass(), "value").addStatement(GeneratorUtil.PREFS_PUT_VALUE, methodName(), key());
    }

    @Override
    public String toString() {
        return "StringSetField{" +
                "name=" + name() +
                ", key=" + key() +
                ", value=" + value() +
                '}';
    }
}

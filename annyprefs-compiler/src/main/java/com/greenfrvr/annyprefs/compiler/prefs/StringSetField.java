package com.greenfrvr.annyprefs.compiler.prefs;

import com.google.common.collect.Sets;
import com.greenfrvr.annyprefs.annotation.BoolPref;
import com.greenfrvr.annyprefs.annotation.StringPref;
import com.greenfrvr.annyprefs.annotation.StringSetPref;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

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

    @Override
    public void setSource(Element element) {
        this.el = element;
    }

    @Override
    public String name() {
        return el.getSimpleName().toString();
    }

    @Override
    public boolean hasResKey() {
        return el.getAnnotation(StringSetPref.class).keyRes() > 0;
    }

    @Override
    public String key() {
        int res = el.getAnnotation(StringSetPref.class).keyRes();
        if (res > 0) {
            return String.valueOf(res);
        }

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
        return Utils.STRING_SET;
    }

    @Override
    public void putRestoreStatement(MethodSpec.Builder builder) {
        if (value().isEmpty()) {
            String statement = hasResKey() ? Utils.PREFS_RESTORE_SET_EMPTY_VALUE_RES : Utils.PREFS_RESTORE_SET_EMPTY_VALUE;
            builder.addStatement(statement, methodName(), key(), null);
        } else {
            TypeName setType = ParameterizedTypeName.get(HashSet.class, String.class);
            builder.addStatement(Utils.restoreSetStatement(this, hasResKey()), methodName(), key(), setType, Arrays.class);
        }
    }

    @Override
    public void putSaveStatement(MethodSpec.Builder builder) {
        String statement = hasResKey() ? Utils.PREFS_PUT_VALUE_RES : Utils.PREFS_PUT_VALUE;
        builder.addParameter(fieldClass(), "value").addStatement(statement, methodName(), key());
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

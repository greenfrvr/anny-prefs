package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.ObjectPref;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.Map;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by greenfrvr
 */
public class ObjectField implements PrefField<String> {

    private Element el;

    public ObjectField() {
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
    public boolean hasResKey() {
        return el.getAnnotation(ObjectPref.class).keyRes() > 0;
    }

    @Override
    public String key() {
        int res = el.getAnnotation(ObjectPref.class).keyRes();
        if (res > 0) {
            return String.valueOf(res);
        }

        String key = el.getAnnotation(ObjectPref.class).key();
        if (key.isEmpty()) {
            key = name();
        }
        return key;
    }

    @Override
    public String value() {
        return "";
    }

    @Override
    public TypeName fieldClass() {
            Map<? extends ExecutableElement, ? extends AnnotationValue> map;
            map = el.getAnnotationMirrors().get(0).getElementValues();

            ExecutableElement ee = map.keySet().stream()
                    .filter(executableElement -> executableElement.getSimpleName().toString().equals("type"))
                    .findFirst().get();

            return ClassName.get((TypeMirror) map.get(ee).getValue());
    }

    @Override
    public String methodName() {
        return Utils.STRING;
    }

    @Override
    public void putRestoreStatement(MethodSpec.Builder builder) {
        String statement = hasResKey() ? Utils.PREFS_RESTORE_JSON_STRING_VALUE_RES : Utils.PREFS_RESTORE_JSON_STRING_VALUE;
        builder.addCode(statement, Utils.GSON_CLASS, methodName(), key(), "", fieldClass());
    }

    @Override
    public void putSaveStatement(MethodSpec.Builder builder) {
        String statement = hasResKey() ? Utils.PREFS_PUT_OBJECT_VALUE_RES : Utils.PREFS_PUT_OBJECT_VALUE;
        builder.addParameter(fieldClass(), "value").addCode(statement, Utils.GSON_CLASS, fieldClass(), key());
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

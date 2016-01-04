package com.greenfrvr.annyprefs.compiler.utils;

import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.greenfrvr.annyprefs.compiler.prefs.StringSetField;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;

import javax.lang.model.element.Modifier;

/**
 * Created by greenfrvr
 */
public class MethodsUtil {

    public static void makeSetRestoreMethod(MethodSpec.Builder b, StringSetField field) {
        if (field.value().isEmpty()) {
            b.addStatement(GeneratorUtil.PREFS_RESTORE_SET_EMPTY_VALUE, field.methodName(), field.key(), null);
        } else {
            TypeName setType = ParameterizedTypeName.get(HashSet.class, String.class);
            b.addStatement(GeneratorUtil.restoreSetStatement(field), field.methodName(), field.key(), setType, Arrays.class);
        }
    }

    public static MethodSpec saveMethodInstance(String name, PrefField field) {
        return builder(field.name(), GeneratorUtil.saveInterfaceClassName(name), true)
                .addParameter(field.fieldClass(), "value")
                .build();
    }

    public static MethodSpec restoreMethodInstance(PrefField field) {
        return builder(field.name(), field.fieldClass(), true).build();
    }

    public static MethodSpec removeMethodInstance(String name, PrefField field) {
        return builder(field.name(), GeneratorUtil.removeInterfaceClassName(name), true)
                .build();
    }

    public static MethodSpec syncMethod(String name) {
        return builder("sync", boolean.class, false)
                .addStatement("return $L.this.sync()", GeneratorUtil.prefsInstanceName(name))
                .build();
    }

    public static MethodSpec asyncMethod(String name) {
        return builder("async", void.class, false)
                .addStatement("$L.this.async()", GeneratorUtil.prefsInstanceName(name))
                .build();
    }

    public static MethodSpec.Builder builder(String name, Type returnType, boolean isAbstract) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(returnType);

        if (isAbstract)
            builder.addModifiers(Modifier.ABSTRACT);
        else
            builder.addAnnotation(Override.class);

        return builder;
    }

    public static MethodSpec.Builder builder(String name, TypeName returnType, boolean isAbstract) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(returnType);

        if (isAbstract)
            builder.addModifiers(Modifier.ABSTRACT);
        else
            builder.addAnnotation(Override.class);

        return builder;
    }

}

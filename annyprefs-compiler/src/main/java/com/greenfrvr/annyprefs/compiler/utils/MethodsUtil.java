package com.greenfrvr.annyprefs.compiler.utils;

import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.Type;

import javax.lang.model.element.Modifier;

/**
 * Created by greenfrvr
 */
public class MethodsUtil {

    public static MethodSpec saveMethodInstance(String name, PrefField field) {
        return builder(field.name(), Utils.saveClassName(name), true)
                .addParameter(field.fieldClass(), "value")
                .build();
    }

    public static MethodSpec restoreMethodInstance(PrefField field) {
        return builder(field.name(), field.fieldClass(), true).build();
    }

    public static MethodSpec removeMethodInstance(String name, PrefField field) {
        return builder(field.name(), Utils.removeClassName(name), true)
                .build();
    }

    public static MethodSpec syncMethod(String name) {
        return builder("sync", boolean.class, false)
                .addStatement("return $L.this.sync()", name.concat(Utils.PREFS))
                .build();
    }

    public static MethodSpec asyncMethod(String name) {
        return builder("async", void.class, false)
                .addStatement("$L.this.async()", name.concat(Utils.PREFS))
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

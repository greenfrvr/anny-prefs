package com.greenfrvr.annyprefs.compiler.utils;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by greenfrvr
 */
public class FieldsUtils {

    public static FieldSpec field(String name, TypeName typeName , TypeSpec.Builder builder) {
        return FieldSpec.builder(typeName, name, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$L", builder.build())
                .build();
    }

    public static FieldSpec field(String name, TypeName typeName , String arg) {
        return FieldSpec.builder(typeName, name, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", arg)
                .build();
    }

}

package com.greenfrvr.annyprefs.compiler.components;

import com.google.common.collect.Maps;
import com.greenfrvr.annyprefs.compiler.components.util.Generator;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Created by greenfrvr
 */
public class AdapterGenerator implements com.greenfrvr.annyprefs.compiler.components.util.Constructor, com.greenfrvr.annyprefs.compiler.components.util.Generator {

    private TypeSpec typeSpec;
    private Map<String, String> prefsMap;

    private AdapterGenerator() {
        prefsMap = Maps.newHashMap();
    }

    public static AdapterGenerator init() {
        return new AdapterGenerator();
    }

    public void add(String clsName, String prefClassName) {
        prefsMap.put(clsName, prefClassName);
    }

    private FieldSpec getClassesField() {
        TypeName type = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), Utils.PREFS_CLASS);
        return FieldSpec.builder(type, "PREFS")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T<>($L)", HashMap.class, prefsMap.size())
                .build();
    }

    @Override
    public Generator construct() {
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder("PrefsAdapter")
                .addModifiers(Modifier.PUBLIC)
                .addField(getClassesField());

        prefsMap.keySet().stream().forEach(key -> {
            ClassName className = Utils.className(prefsMap.get(key));

            MethodSpec method = MethodSpec.methodBuilder(key.toLowerCase())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(className)
                    .addParameter(Utils.CONTEXT_CLASS, "context")
                    .addCode(Utils.ADAPTER_PREFS_INSTANCE, prefsMap.get(key), prefsMap.get(key), className, className, prefsMap.get(key))
                    .build();
            typeBuilder.addMethod(method);
        });

        typeSpec = typeBuilder.build();

        return this;
    }

    @Override
    public void generate(Filer filer) throws IOException {
        JavaFile javaFile = JavaFile.builder(Utils.GENERATED_PACKAGE, typeSpec).build();
        javaFile.writeTo(filer);
    }
}

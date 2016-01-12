package com.greenfrvr.annyprefs.compiler.components;

import com.greenfrvr.annyprefs.compiler.DataSource;
import com.greenfrvr.annyprefs.compiler.components.inner.RemoveInnerInstance;
import com.greenfrvr.annyprefs.compiler.components.inner.RestoreInnerInstance;
import com.greenfrvr.annyprefs.compiler.components.inner.SaveInnerInstance;
import com.greenfrvr.annyprefs.compiler.components.util.Generator;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.greenfrvr.annyprefs.compiler.utils.MethodsUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Created by greenfrvr
 */
public class PrefInstanceGenerator implements com.greenfrvr.annyprefs.compiler.components.util.Constructor, com.greenfrvr.annyprefs.compiler.components.util.Generator {

    TypeSpec typeSpec;
    DataSource data;

    TypeName saveClassName;
    TypeName restoreClassName;
    TypeName removeClassName;

    private PrefInstanceGenerator(DataSource data) {
        this.data = data;
        this.saveClassName = prepareClassName(Utils.SAVE);
        this.restoreClassName = prepareClassName(Utils.RESTORE);
        this.removeClassName = prepareClassName(Utils.REMOVE);
    }

    public static PrefInstanceGenerator init(DataSource data) {
        return new PrefInstanceGenerator(data);
    }

    @Override
    public Generator construct() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(data.name().concat(Utils.PREFS))
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(Utils.PREFS_CLASS, saveClassName, restoreClassName, removeClassName))
                .addField(Utils.CONTEXT_CLASS, "context", Modifier.PRIVATE)
                .addField(staticKeyField());

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(Utils.CONTEXT_CLASS, "context")
                .addStatement(Utils.PREFS_CONSTRUCTOR, "context", "context")
                .build();

        builder.addMethod(constructor)
                .addMethod(instantiateMethod("save", saveClassName, "return save"))
                .addMethod(instantiateMethod("restore", restoreClassName, "return restore"))
                .addMethod(instantiateMethod("remove", removeClassName, "return remove"))
                .addMethod(instantiateMethod("getContext", Utils.CONTEXT_CLASS, "return context"))
                .addMethod(instantiateMethod("name", ClassName.get(String.class), String.format("return \"%s\"", data.prefsName())));

        builder.addField(SaveInnerInstance.init(data).construct())
                .addField(RestoreInnerInstance.init(data).construct())
                .addField(RemoveInnerInstance.init(data).construct());

        typeSpec = builder.build();

        return this;
    }

    private FieldSpec staticKeyField() {
        return FieldSpec.builder(ClassName.get(String.class), "KEY", Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", Utils.prefsKey(data.packageName(), data.name()))
                .build();
    }

    private MethodSpec instantiateMethod(String name, TypeName returnType, String... statements) {
        MethodSpec.Builder builder = MethodsUtil.builder(name, returnType, false);

        if (statements != null) {
            for (String statement : statements) {
                builder.addStatement(statement);
            }
        }
        return builder.build();
    }

    private ClassName prepareClassName(String s) {
        return ClassName.get(Utils.GENERATED_PACKAGE, s.concat(data.name()));
    }

    @Override
    public void generate(Filer filer) throws IOException {
        JavaFile javaFile = JavaFile.builder(Utils.GENERATED_PACKAGE, typeSpec).indent(Utils.INDENT).build();
        javaFile.writeTo(filer);
    }
}

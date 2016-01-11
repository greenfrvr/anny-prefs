package com.greenfrvr.annyprefs.compiler.components.interfaces;

import com.greenfrvr.annyprefs.compiler.DataSource;
import com.greenfrvr.annyprefs.compiler.components.util.Constructor;
import com.greenfrvr.annyprefs.compiler.components.util.Generator;
import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.function.Consumer;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Created by greenfrvr
 */
public abstract class InterfaceGenerator implements Constructor, Generator {

    DataSource data;
    TypeSpec typeSpec;

    abstract String className();

    abstract TypeName superInterface();

    abstract Consumer<PrefField> constructMethod(TypeSpec.Builder builder);

    @Override
    public InterfaceGenerator construct() {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(className())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(superInterface())
                .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "\"UnusedDeclaration\"").build());

        data.prefs().stream().forEach(constructMethod(builder));

        typeSpec = builder.build();
        return this;
    }

    @Override
    public void generate(Filer filer) throws IOException {
        JavaFile javaFile = JavaFile.builder(Utils.GENERATED_PACKAGE, typeSpec).build();
        javaFile.writeTo(filer);
    }

}

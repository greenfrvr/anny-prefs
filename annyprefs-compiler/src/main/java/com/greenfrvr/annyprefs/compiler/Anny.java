package com.greenfrvr.annyprefs.compiler;

import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.greenfrvr.annyprefs.compiler.prefs.PrefFieldFactory;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Created by greenfrvr
 */
public class Anny {

    private String name;
    private List<PrefField> prefs;

    public Anny(String name) {
        this.name = name;
        this.prefs = new ArrayList<>();
    }

    public void addElement(Element el, Class<? extends Annotation> cls) {
        PrefField field = PrefFieldFactory.build(cls);
        if (field != null) {
            field.setSource(el);
            prefs.add(field);
        }
    }

    public List<PrefField> getPrefs() {
        return prefs;
    }

    public void generateCode(Filer filer) throws IOException {
        generateSaveInterface(filer);
        generateRestoreInterface(filer);
        generatePrefsInstance(filer);
    }

    private void generateSaveInterface(Filer filer) throws IOException {
        String className = GeneratorUtil.saveInterfaceName(name);

        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(GeneratorUtil.SAVE_CLASS);

        for (PrefField field : prefs) {
            MethodSpec method = MethodSpec.methodBuilder(field.name())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(void.class)
                    .addParameter(field.fieldClass(), "value")
                    .build();

            interfaceBuilder.addMethod(method);
        }

        TypeSpec typeSpec = interfaceBuilder.build();

        JavaFile javaFile = JavaFile.builder(GeneratorUtil.GENERATED_PACKAGE, typeSpec).build();
        javaFile.writeTo(filer);
    }

    private void generateRestoreInterface(Filer filer) throws IOException {
        String className = GeneratorUtil.restoreInterfaceName(name);

        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(GeneratorUtil.RESTORE_CLASS);

        for (PrefField field : prefs) {
            MethodSpec method = MethodSpec.methodBuilder(field.name())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(field.fieldClass())
                    .build();

            interfaceBuilder.addMethod(method);
        }

        TypeSpec typeSpec = interfaceBuilder.build();

        JavaFile javaFile = JavaFile.builder(GeneratorUtil.GENERATED_PACKAGE, typeSpec).build();
        javaFile.writeTo(filer);
    }

    private void generatePrefsInstance(Filer filer) throws IOException {
        String className = GeneratorUtil.prefsInstanceName(name);
        TypeName saveClassName = ClassName.get(GeneratorUtil.GENERATED_PACKAGE, GeneratorUtil.saveInterfaceName(name));
        TypeName restoreClassName = ClassName.get(GeneratorUtil.GENERATED_PACKAGE, GeneratorUtil.restoreInterfaceName(name));

        TypeSpec.Builder prefsBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(GeneratorUtil.PREFS_CLASS, saveClassName, restoreClassName))
                .addField(GeneratorUtil.CONTEXT_CLASS, "context", Modifier.PRIVATE)
                .addField(innerSaveInstance(saveClassName))
                .addField(innerRestoreInstance(restoreClassName));

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(GeneratorUtil.CONTEXT_CLASS, "context")
                .addStatement("this.$N = $N", "context", "context")
                .build();

        MethodSpec save = MethodSpec.methodBuilder("save")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(saveClassName)
                .addStatement("return save")
                .build();

        MethodSpec saveAsync = MethodSpec.methodBuilder("saveAsync")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(saveClassName)
                .addStatement("return save")
                .build();

        MethodSpec restore = MethodSpec.methodBuilder("restore")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(restoreClassName)
                .addStatement("return restore")
                .build();

        MethodSpec clear = MethodSpec.methodBuilder("clear")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .build();

        MethodSpec context = MethodSpec.methodBuilder("getContext")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .returns(GeneratorUtil.CONTEXT_CLASS)
                .addStatement("return context")
                .build();

        prefsBuilder.addMethod(constructor);
        prefsBuilder.addMethod(save);
        prefsBuilder.addMethod(saveAsync);
        prefsBuilder.addMethod(restore);
        prefsBuilder.addMethod(clear);
        prefsBuilder.addMethod(context);

        TypeSpec typeSpec = prefsBuilder.build();

        JavaFile javaFile = JavaFile.builder(GeneratorUtil.GENERATED_PACKAGE, typeSpec).build();
        javaFile.writeTo(filer);
    }

    private FieldSpec innerSaveInstance(TypeName name) {
        TypeSpec.Builder innerClassBuilder = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(name);

        for (PrefField field : prefs) {
            MethodSpec method = MethodSpec.methodBuilder(field.name())
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(void.class)
                    .addParameter(field.fieldClass(), "value")
                    .addStatement("editor().put$N($S, value).apply()", field.methodName(), field.key())
                    .build();
            innerClassBuilder.addMethod(method);
        }

        return FieldSpec.builder(name, "save", Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$L", innerClassBuilder.build())
                .build();
    }

    private FieldSpec innerRestoreInstance(TypeName name) {
        TypeSpec.Builder innerClassBuilder = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(name);

        for (PrefField field : prefs) {
            MethodSpec method = MethodSpec.methodBuilder(field.name())
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(field.fieldClass())
                    .addStatement("return shared().get$N($S, $T.valueOf($S))", field.methodName(), field.key(), field.fieldClass(), field.value().toString())
                    .build();
            innerClassBuilder.addMethod(method);
        }

        return FieldSpec.builder(name, "restore", Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$L", innerClassBuilder.build())
                .build();
    }
}

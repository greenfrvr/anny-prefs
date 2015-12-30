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
    private String prefsName;
    private List<PrefField> prefs;

    public Anny(String name, String prefsName) {
        this.name = name;
        this.prefsName =prefsName;
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

    public String getPrefClassName() {
        return GeneratorUtil.prefsInstanceName(name);
    }

    private void generateSaveInterface(Filer filer) throws IOException {
        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(GeneratorUtil.saveInterfaceName(name))
                .addModifiers(Modifier.PUBLIC).addSuperinterface(GeneratorUtil.SAVE_CLASS);

        for (PrefField field : prefs) {
            interfaceBuilder.addMethod(GeneratorUtil.saveMethodInstance(field));
        }

        generate(filer, interfaceBuilder.build());
    }

    private void generateRestoreInterface(Filer filer) throws IOException {
        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(GeneratorUtil.restoreInterfaceName(name))
                .addModifiers(Modifier.PUBLIC).addSuperinterface(GeneratorUtil.RESTORE_CLASS);

        for (PrefField field : prefs) {
            interfaceBuilder.addMethod(GeneratorUtil.restoreMethodInstance(field));
        }

        generate(filer, interfaceBuilder.build());
    }

    private void generatePrefsInstance(Filer filer) throws IOException {
        TypeName saveClassName = ClassName.get(GeneratorUtil.GENERATED_PACKAGE, GeneratorUtil.saveInterfaceName(name));
        TypeName restoreClassName = ClassName.get(GeneratorUtil.GENERATED_PACKAGE, GeneratorUtil.restoreInterfaceName(name));

        TypeSpec.Builder prefsBuilder = TypeSpec.classBuilder(GeneratorUtil.prefsInstanceName(name))
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(GeneratorUtil.PREFS_CLASS, saveClassName, restoreClassName))
                .addField(prefsKeyField())
                .addField(GeneratorUtil.CONTEXT_CLASS, "context", Modifier.PRIVATE);

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(GeneratorUtil.CONTEXT_CLASS, "context")
                .addStatement("this.$N = $N.getApplicationContext()", "context", "context")
                .build();

        prefsBuilder.addMethod(constructor)
                .addMethod(instantiateMethod("save", saveClassName, "return save"))
                .addMethod(instantiateMethod("restore", restoreClassName, "return restore"))
                .addMethod(instantiateMethod("clear", TypeName.VOID))
                .addMethod(instantiateMethod("getContext", GeneratorUtil.CONTEXT_CLASS, "return context"))
                .addMethod(instantiateMethod("name", ClassName.get(String.class), "return \"" + prefsName + "\""));

        prefsBuilder.addField(innerSaveInstance(saveClassName))
                .addField(innerRestoreInstance(restoreClassName));

        generate(filer, prefsBuilder.build());
    }

    private FieldSpec prefsKeyField() {
        return FieldSpec.builder(String.class, "KEY", Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", name.toLowerCase() + "_prefs")
                .build();
    }

    private FieldSpec innerSaveInstance(TypeName typeName) {
        TypeSpec.Builder innerBuilder = TypeSpec.anonymousClassBuilder("").addSuperinterface(typeName);

        for (PrefField field : prefs) {
            MethodSpec method = MethodSpec.methodBuilder(field.name())
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(GeneratorUtil.SAVE_METHOD_INTERFACE)
                    .addParameter(field.fieldClass(), "value")
                    .addStatement("editor().put$N($S, value);\nreturn $L.this", field.methodName(), field.key(), GeneratorUtil.prefsInstanceName(name))
                    .build();
            innerBuilder.addMethod(method);
        }

        return FieldSpec.builder(typeName, "save", Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$L", innerBuilder.build())
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

    private MethodSpec instantiateMethod(String name, TypeName returnType, String... statements) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(returnType);

        if (statements != null)
            for (String statement : statements) {
                builder.addStatement(statement);
            }
        return builder.build();
    }

    private void generate(Filer filer, TypeSpec typeSpec) throws IOException {
        JavaFile javaFile = JavaFile.builder(GeneratorUtil.GENERATED_PACKAGE, typeSpec).build();
        javaFile.writeTo(filer);
    }
}

package com.greenfrvr.annyprefs.compiler;

import com.greenfrvr.annyprefs.annotation.StringSetPref;
import com.greenfrvr.annyprefs.compiler.prefs.DateField;
import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.greenfrvr.annyprefs.compiler.prefs.PrefFieldFactory;
import com.greenfrvr.annyprefs.compiler.prefs.StringSetField;
import com.greenfrvr.annyprefs.compiler.utils.FieldsUtils;
import com.greenfrvr.annyprefs.compiler.utils.GeneratorUtil;
import com.greenfrvr.annyprefs.compiler.utils.MethodsUtil;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Created by greenfrvr
 */
public class Anny {

    private String name;
    private String prefsName;
    private String packageName;
    private List<PrefField> prefs;

    public Anny(String name, String prefsName, String packageName) {
        this.name = name;
        this.prefsName = prefsName;
        this.packageName = packageName;
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
        generateRemoveInterface(filer);
        generatePrefsInstance(filer);
    }

    public String getPrefClassName() {
        return GeneratorUtil.prefsInstanceName(name);
    }

    private void generateSaveInterface(Filer filer) throws IOException {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(GeneratorUtil.saveInterfaceName(name))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(GeneratorUtil.SAVE_CLASS);

        for (PrefField field : prefs) {
            builder.addMethod(MethodsUtil.saveMethodInstance(name, field));
        }

        generate(filer, builder.build());
    }

    private void generateRestoreInterface(Filer filer) throws IOException {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(GeneratorUtil.restoreInterfaceName(name))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(GeneratorUtil.RESTORE_CLASS);

        for (PrefField field : prefs) {
            builder.addMethod(MethodsUtil.restoreMethodInstance(field));
        }

        generate(filer, builder.build());
    }

    private void generateRemoveInterface(Filer filer) throws IOException {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(GeneratorUtil.removeInterfaceName(name))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(GeneratorUtil.REMOVE_CLASS);

        for (PrefField field : prefs) {
            builder.addMethod(MethodsUtil.removeMethodInstance(name, field));
        }

        generate(filer, builder.build());
    }

    private void generatePrefsInstance(Filer filer) throws IOException {
        TypeName saveClassName = ClassName.get(GeneratorUtil.GENERATED_PACKAGE, GeneratorUtil.saveInterfaceName(name));
        TypeName restoreClassName = ClassName.get(GeneratorUtil.GENERATED_PACKAGE, GeneratorUtil.restoreInterfaceName(name));
        TypeName removeClassName = ClassName.get(GeneratorUtil.GENERATED_PACKAGE, GeneratorUtil.removeInterfaceName(name));

        TypeSpec.Builder builder = TypeSpec.classBuilder(GeneratorUtil.prefsInstanceName(name))
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(GeneratorUtil.PREFS_CLASS, saveClassName, restoreClassName, removeClassName))
                .addField(FieldsUtils.field("KEY", TypeName.get(String.class), GeneratorUtil.prefsKey(packageName, name)))
                .addField(GeneratorUtil.CONTEXT_CLASS, "context", Modifier.PRIVATE);

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(GeneratorUtil.CONTEXT_CLASS, "context")
                .addStatement(GeneratorUtil.PREFS_CONSTRUCTOR, "context", "context")
                .build();

        builder.addMethod(constructor)
                .addMethod(instantiateMethod("save", saveClassName, "return save"))
                .addMethod(instantiateMethod("restore", restoreClassName, "return restore"))
                .addMethod(instantiateMethod("remove", removeClassName, "return remove"))
                .addMethod(instantiateMethod("getContext", GeneratorUtil.CONTEXT_CLASS, "return context"))
                .addMethod(instantiateMethod("name", ClassName.get(String.class), "return \"" + prefsName + "\""));

        builder.addField(innerSaveInstance(saveClassName))
                .addField(innerRestoreInstance(restoreClassName))
                .addField(innerRemoveInstance(removeClassName));

        generate(filer, builder.build());
    }

    private FieldSpec innerSaveInstance(TypeName type) {
        TypeSpec.Builder builder = TypeSpec.anonymousClassBuilder("").addSuperinterface(type);

        for (PrefField field : prefs) {
            MethodSpec method = MethodsUtil.builder(field.name(), type, false)
                    .addParameter(field.fieldClass(), "value")
                    .addStatement(field.putValueStatement(), field.methodName(), field.key())
                    .build();
            builder.addMethod(method);
        }

        addTransactionMethods(builder);

        return FieldsUtils.field("save", type, builder);
    }

    private FieldSpec innerRestoreInstance(TypeName type) {
        TypeSpec.Builder builder = TypeSpec.anonymousClassBuilder("").addSuperinterface(type);

        for (PrefField field : prefs) {
            MethodSpec.Builder method = MethodsUtil.builder(field.name(), field.fieldClass(), false);
            if (field instanceof StringSetField) {
                MethodsUtil.makeSetRestoreMethod(method, (StringSetField) field);
            } else if (field instanceof DateField) {
                method.addStatement(GeneratorUtil.PREFS_RESTORE_DATE_VALUE, field.fieldClass(), field.methodName(), field.key(), field.value());
            } else {
                method.addStatement(GeneratorUtil.PREFS_RESTORE_VALUE, field.methodName(), field.key(), field.fieldClass(), field.value());
            }

            builder.addMethod(method.build());
        }

        return FieldsUtils.field("restore", type, builder);
    }

    private FieldSpec innerRemoveInstance(TypeName type) {
        TypeSpec.Builder builder = TypeSpec.anonymousClassBuilder("").addSuperinterface(type);

        for (PrefField field : prefs) {
            MethodSpec method = MethodsUtil.builder(field.name(), type, false)
                    .addStatement(GeneratorUtil.PREFS_REMOVE_VALUE, field.key())
                    .build();
            builder.addMethod(method);
        }

        addTransactionMethods(builder);

        return FieldsUtils.field("remove", type, builder);
    }

    private void addTransactionMethods(TypeSpec.Builder builder) {
        builder.addMethod(MethodsUtil.syncMethod(name));
        builder.addMethod(MethodsUtil.asyncMethod(name));
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

    private void generate(Filer filer, TypeSpec typeSpec) throws IOException {
        JavaFile javaFile = JavaFile.builder(GeneratorUtil.GENERATED_PACKAGE, typeSpec).build();
        javaFile.writeTo(filer);
    }
}

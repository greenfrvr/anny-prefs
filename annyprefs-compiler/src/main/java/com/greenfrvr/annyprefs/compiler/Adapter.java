package com.greenfrvr.annyprefs.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Created by greenfrvr
 */
public class Adapter {

    private Map<String, String> prefsMap;

    public Adapter() {
        prefsMap = new HashMap<>();
    }

    public void add(String clsName, String prefClassName) {
        prefsMap.put(clsName, prefClassName);
    }

    public void generateCode(Filer filer) throws IOException {
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder("PrefsAdapter")
                .addModifiers(Modifier.PUBLIC)
                .addField(getClassesField());

        for (String key : prefsMap.keySet()) {
            ClassName className = ClassName.get(GeneratorUtil.GENERATED_PACKAGE, prefsMap.get(key));

            MethodSpec method = MethodSpec.methodBuilder(key.toLowerCase())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(ClassName.get(GeneratorUtil.GENERATED_PACKAGE, prefsMap.get(key)))
                    .addParameter(GeneratorUtil.CONTEXT_CLASS, "context")
                    .addCode(GeneratorUtil.ADAPTER_PREFS_INSTANCE, prefsMap.get(key), prefsMap.get(key), className, className, prefsMap.get(key))
                    .build();
            typeBuilder.addMethod(method);
        }

        generate(filer, typeBuilder.build());
    }

    private FieldSpec getClassesField() {
        return FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), GeneratorUtil.PREFS_CLASS), "PREFS")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T<>($L)", HashMap.class, prefsMap.size())
                .build();
    }

    private void generate(Filer filer, TypeSpec typeSpec) throws IOException {
        JavaFile javaFile = JavaFile.builder(GeneratorUtil.GENERATED_PACKAGE, typeSpec).build();
        javaFile.writeTo(filer);
    }

}

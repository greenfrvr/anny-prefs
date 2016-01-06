package com.greenfrvr.annyprefs.compiler.components.inner;

import com.greenfrvr.annyprefs.compiler.DataSource;
import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.greenfrvr.annyprefs.compiler.utils.MethodsUtil;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by greenfrvr
 */
public abstract class InnerInstanceGenerator {

    DataSource data;

    abstract void constructMethod(PrefField field,  MethodSpec.Builder method);

    abstract String instanceName();

    abstract TypeName type();

    boolean isMutating() { return true; }

    public FieldSpec construct() {
        TypeSpec.Builder builder = TypeSpec.anonymousClassBuilder("").addSuperinterface(type());

        data.prefs().stream()
                .forEach(field -> {
                    MethodSpec.Builder method = MethodsUtil.builder(field.name(), isMutating() ? type() : field.fieldClass(), false);
                    constructMethod(field, method);
                    builder.addMethod(method.build());
                });

        addTransactionMethods(builder);

        return FieldSpec.builder(type(), instanceName(), Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$L", builder.build())
                .build();
    }

    private void addTransactionMethods(TypeSpec.Builder builder) {
        if (!isMutating()) return;

        builder.addMethod(MethodsUtil.syncMethod(data.name()));
        builder.addMethod(MethodsUtil.asyncMethod(data.name()));
    }

}

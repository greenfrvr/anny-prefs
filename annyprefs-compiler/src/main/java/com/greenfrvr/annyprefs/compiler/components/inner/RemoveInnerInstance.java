package com.greenfrvr.annyprefs.compiler.components.inner;

import com.greenfrvr.annyprefs.compiler.DataSource;
import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

/**
 * Created by greenfrvr
 */
public class RemoveInnerInstance extends InnerInstanceGenerator {

    RemoveInnerInstance(DataSource dataSource){
        super(dataSource);
    }

    public static RemoveInnerInstance init(DataSource data) {
        return new RemoveInnerInstance(data);
    }

    @Override
    void constructMethod(PrefField field, MethodSpec.Builder method) {
        method.addCode(field.hasResKey() ? Utils.REMOVE_VALUE_RES : Utils.REMOVE_VALUE, field.key());
    }

    @Override
    String instanceName() {
        return "remove";
    }

    @Override
    TypeName type() {
        return ClassName.get(Utils.GENERATED_PACKAGE, Utils.REMOVE.concat(data.name()));
    }
}

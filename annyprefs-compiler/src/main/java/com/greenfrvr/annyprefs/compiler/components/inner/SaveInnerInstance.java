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
public class SaveInnerInstance extends InnerInstanceGenerator {

    SaveInnerInstance(DataSource dataSource){
        super(dataSource);
    }

    public static SaveInnerInstance init(DataSource data) {
        return new SaveInnerInstance(data);
    }

    @Override
    void constructMethod(PrefField field, MethodSpec.Builder method) {
        field.putSaveStatement(method);
    }

    @Override
    String instanceName() {
        return "save";
    }

    @Override
    TypeName type() {
        return ClassName.get(Utils.GENERATED_PACKAGE, Utils.SAVE.concat(data.name()));
    }
}

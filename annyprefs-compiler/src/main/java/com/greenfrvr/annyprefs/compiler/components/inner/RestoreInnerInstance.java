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
public class RestoreInnerInstance extends InnerInstanceGenerator {

    RestoreInnerInstance(DataSource dataSource){
        super(dataSource);
    }

    public static RestoreInnerInstance init(DataSource data) {
        return new RestoreInnerInstance(data);
    }

    @Override
    void constructMethod(PrefField field, MethodSpec.Builder method) {
        field.putRestoreStatement(method);
    }

    @Override
    boolean isMutating() {
        return !super.isMutating();
    }

    @Override
    String instanceName() {
        return "restore";
    }

    @Override
    TypeName type() {
        return ClassName.get(Utils.GENERATED_PACKAGE, Utils.RESTORE.concat(data.name()));
    }

}

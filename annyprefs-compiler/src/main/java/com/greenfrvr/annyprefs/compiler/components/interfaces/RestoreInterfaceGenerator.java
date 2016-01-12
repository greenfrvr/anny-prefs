package com.greenfrvr.annyprefs.compiler.components.interfaces;

import com.greenfrvr.annyprefs.compiler.DataSource;
import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.greenfrvr.annyprefs.compiler.utils.Utils;
import com.greenfrvr.annyprefs.compiler.utils.MethodsUtil;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.function.Consumer;

/**
 * Created by greenfrvr
 */
public class RestoreInterfaceGenerator extends InterfaceGenerator {

    RestoreInterfaceGenerator(DataSource dataSource) {
        super(dataSource);
    }

    public static RestoreInterfaceGenerator init(DataSource data) {
        return new RestoreInterfaceGenerator(data);
    }

    @Override
    String className() {
        return Utils.RESTORE.concat(data.name());
    }

    @Override
    TypeName superInterface() {
        return Utils.RESTORE_CLASS;
    }

    @Override
    Consumer<PrefField> constructMethod(TypeSpec.Builder builder) {
        return field -> builder.addMethod(MethodsUtil.restoreMethodInstance(field));
    }

}

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
public class SaveInterfaceGenerator extends InterfaceGenerator {

    SaveInterfaceGenerator(DataSource dataSource) {
        super(dataSource);
    }

    public static SaveInterfaceGenerator init(DataSource data) {
        return new SaveInterfaceGenerator(data);
    }

    @Override
    String className() {
        return Utils.SAVE.concat(data.name());
    }

    @Override
    TypeName superInterface() {
        return Utils.SAVE_CLASS;
    }

    @Override
    public Consumer<PrefField> constructMethod(TypeSpec.Builder builder) {
        return field -> builder.addMethod(MethodsUtil.saveMethodInstance(data.name(), field));
    }

}

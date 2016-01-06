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
public class RemoveInterfaceGenerator extends InterfaceGenerator {

    private RemoveInterfaceGenerator() {
    }

    public static RemoveInterfaceGenerator init(DataSource data) {
        RemoveInterfaceGenerator generator = new RemoveInterfaceGenerator();
        generator.data = data;
        return generator;
    }

    @Override
    String className() {
        return Utils.REMOVE.concat(data.name());
    }

    @Override
    TypeName superInterface() {
        return Utils.REMOVE_CLASS;
    }

    @Override
    Consumer<PrefField> constructMethod(TypeSpec.Builder builder) {
        return field -> builder.addMethod(MethodsUtil.removeMethodInstance(data.name(), field));
    }

}

package com.greenfrvr.annyprefs.compiler;

import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by greenfrvr
 */
public class GeneratorUtil {

    public static final String SAVE = "Save";
    public static final String RESTORE = "Restore";
    public static final String PREFS = "Prefs";
    public static final String SAVE_METHOD = "SaveMethod";

    public static final String PACKAGE = "com.greenfrvr.annyprefs";
    public static final String GENERATED_PACKAGE = PACKAGE + ".compiled";

    public static final ClassName PREFS_CLASS = ClassName.get(PACKAGE, PREFS);
    public static final ClassName SAVE_CLASS = ClassName.get(PACKAGE, SAVE);
    public static final ClassName RESTORE_CLASS = ClassName.get(PACKAGE, RESTORE);
    public static final ClassName CONTEXT_CLASS = ClassName.get("android.content", "Context");

    public static final ClassName SAVE_METHOD_INTERFACE = ClassName.get(PACKAGE, SAVE_METHOD);

    public static final String METHOD_STRING = "String";
    public static final String METHOD_BOOLEAN = "Boolean";
    public static final String METHOD_INT = "Int";
    public static final String METHOD_FLOAT = "Float";
    public static final String METHOD_LONG = "Long";

    public static final String ADAPTER_PREFS_INSTANCE =
            "if (!PREFS.containsKey($L.KEY)) " +
            "{\n\tPREFS.put($L.KEY, new $T(context)); \n}" +
            "\nreturn ($T) PREFS.get($L.KEY);\n";

    public static String saveInterfaceName(String name) {
        return SAVE + name;
    }

    public static String restoreInterfaceName(String name) {
        return RESTORE + name;
    }

    public static String prefsInstanceName(String name) {
        return name + PREFS;
    }

    public static MethodSpec saveMethodInstance(PrefField field) {
        return MethodSpec.methodBuilder(field.name())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(SAVE_METHOD_INTERFACE)
                .addParameter(field.fieldClass(), "value")
                .build();
    }

    public static MethodSpec restoreMethodInstance(PrefField field) {
        return MethodSpec.methodBuilder(field.name())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(field.fieldClass())
                .build();
    }
}

package com.greenfrvr.annyprefs.compiler.utils;

import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.greenfrvr.annyprefs.compiler.prefs.StringSetField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by greenfrvr
 */
public class GeneratorUtil {

    public static final String PACKAGE = "com.greenfrvr.annyprefs";
    public static final String GENERATED_PACKAGE = PACKAGE;

    public static final String SAVE = "Save";
    public static final String RESTORE = "Restore";
    public static final String REMOVE = "Remove";
    public static final String PREFS = "Prefs";

    public static final ClassName PREFS_CLASS = ClassName.get(PACKAGE, PREFS);
    public static final ClassName SAVE_CLASS = ClassName.get(PACKAGE, SAVE);
    public static final ClassName RESTORE_CLASS = ClassName.get(PACKAGE, RESTORE);
    public static final ClassName REMOVE_CLASS = ClassName.get(PACKAGE, REMOVE);
    public static final ClassName CONTEXT_CLASS = ClassName.get("android.content", "Context");

    public static final String STRING = "String";
    public static final String BOOLEAN = "Boolean";
    public static final String INT = "Int";
    public static final String FLOAT = "Float";
    public static final String LONG = "Long";
    public static final String STRING_SET = "StringSet";

    public static final String ADAPTER_PREFS_INSTANCE =
            "if (!PREFS.containsKey($L.KEY)) " +
                    "{\n\tPREFS.put($L.KEY, new $T(context)); \n}" +
                    "\nreturn ($T) PREFS.get($L.KEY);\n";

    public static final String PREFS_CONSTRUCTOR = "this.$N = $N.getApplicationContext()";
    public static final String PREFS_PUT_VALUE = "editor().put$N($S, value);\nreturn this";
    public static final String PREFS_RESTORE_VALUE = "return shared().get$N($S, $T.valueOf($S))";
    public static final String PREFS_RESTORE_SET_VALUE = "return shared().get$N($S, new $T($T.asList(0)))";
    public static final String PREFS_RESTORE_SET_EMPTY_VALUE = "return shared().get$N($S, $L)";
    public static final String PREFS_REMOVE_VALUE = "editor().remove($S);\nreturn this";

    public static String prefsInstanceName(String name) {
        return name + PREFS;
    }

    public static String saveInterfaceName(String name) {
        return SAVE + name;
    }

    public static String restoreInterfaceName(String name) {
        return RESTORE + name;
    }

    public static String removeInterfaceName(String name) {
        return REMOVE + name;
    }

    public static ClassName saveInterfaceClassName(String name) {
        return className(saveInterfaceName(name));
    }

    public static ClassName removeInterfaceClassName(String name) {
        return className(removeInterfaceName(name));
    }

    public static String prefsKey(String packageName, String name) {
        return packageName + "." + name.toLowerCase() + "_" + PREFS.toLowerCase();
    }

    public static ClassName className(String name) {
        return ClassName.get(GeneratorUtil.GENERATED_PACKAGE, name);
    }

    public static String restoreSetStatement(StringSetField field) {
        Iterator<String> it = field.value().iterator();
        StringBuilder builder = new StringBuilder();

        while (it.hasNext()) {
            builder.append("\"").append(it.next()).append("\"");
            if (it.hasNext()) builder.append(", ");
        }

        return PREFS_RESTORE_SET_VALUE.replaceAll("0", builder.toString());
    }
}

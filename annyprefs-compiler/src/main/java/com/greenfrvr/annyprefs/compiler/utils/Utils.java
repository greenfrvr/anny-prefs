package com.greenfrvr.annyprefs.compiler.utils;

import com.greenfrvr.annyprefs.compiler.prefs.StringSetField;
import com.squareup.javapoet.ClassName;

import java.util.Iterator;

/**
 * Created by greenfrvr
 */
public class Utils {

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
    public static final ClassName GSON_CLASS = ClassName.get("com.google.gson","Gson");

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
    public static final String PREFS_PUT_VALUE_RES = "editor().put$N(getContext().getString($L), value);\nreturn this";
    public static final String PREFS_PUT_DATE_VALUE = "editor().put$N($S, value.getTime());\nreturn this";
    public static final String PREFS_PUT_DATE_VALUE_RES = "editor().put$N(getContext().getString($L), value.getTime());\nreturn this";
    public static final String PREFS_PUT_OBJECT_VALUE = "String json = new $T().toJson(value, $T.class);\n" +
            "editor().putString($S, json);\nreturn this";
    public static final String PREFS_PUT_OBJECT_VALUE_RES = "String json = new $T().toJson(value, $T.class);\n" +
            "editor().putString(getContext().getString($L), json);\nreturn this";

    public static final String PREFS_RESTORE_VALUE = "return shared().get$N($S, $L)";
    public static final String PREFS_RESTORE_STRING_VALUE = "return shared().get$N($S, $S)";
    public static final String PREFS_RESTORE_JSON_STRING_VALUE = "return new $T().fromJson(shared().get$N($S, $S), $T.class)";
    public static final String PREFS_RESTORE_LONG_VALUE = "return shared().get$N($S, $LL)";
    public static final String PREFS_RESTORE_FLOAT_VALUE = "return shared().get$N($S, $Lf)";
    public static final String PREFS_RESTORE_DATE_VALUE = "return new $T(shared().get$N($S, $LL))";
    public static final String PREFS_RESTORE_SET_VALUE = "return shared().get$N($S, new $T($T.asList(0)))";
    public static final String PREFS_RESTORE_SET_EMPTY_VALUE = "return shared().get$N($S, $L)";

    public static final String PREFS_RESTORE_VALUE_RES = "return shared().get$N(getContext().getString($L), $L)";
    public static final String PREFS_RESTORE_STRING_VALUE_RES = "return shared().get$N(getContext().getString($L), $S)";
    public static final String PREFS_RESTORE_JSON_STRING_VALUE_RES = "return new $T().fromJson(shared().get$N(getContext().getString($L), $S), $T.class)";
    public static final String PREFS_RESTORE_LONG_VALUE_RES = "return shared().get$N(getContext().getString($L), $LL)";
    public static final String PREFS_RESTORE_FLOAT_VALUE_RES = "return shared().get$N(getContext().getString($L), $Lf)";
    public static final String PREFS_RESTORE_DATE_VALUE_RES = "return new $T(shared().get$N(getContext().getString($L), $LL))";
    public static final String PREFS_RESTORE_SET_VALUE_RES = "return shared().get$N(getContext().getString($L), new $T($T.asList(0)))";
    public static final String PREFS_RESTORE_SET_EMPTY_VALUE_RES = "return shared().get$N(getContext().getString($L), $L)";

    public static final String PREFS_REMOVE_VALUE = "editor().remove($S);\nreturn this";
    public static final String PREFS_REMOVE_VALUE_RES = "editor().remove(getContext().getString($L));\nreturn this";

    public static ClassName saveClassName(String name) {
        return className(SAVE.concat(name));
    }

    public static ClassName removeClassName(String name) {
        return className(REMOVE.concat(name));
    }

    public static String prefsKey(String packageName, String name) {
        return (packageName + '.' + name + '_' + PREFS).toLowerCase();
    }

    public static ClassName className(String name) {
        return ClassName.get(Utils.GENERATED_PACKAGE, name);
    }

    public static String restoreSetStatement(StringSetField field, boolean hasResKey) {
        Iterator<String> it = field.value().iterator();
        StringBuilder builder = new StringBuilder();

        while (it.hasNext()) {
            builder.append("\"").append(it.next()).append("\"");
            if (it.hasNext()) builder.append(", ");
        }

        return (hasResKey ? PREFS_RESTORE_SET_VALUE_RES : PREFS_RESTORE_SET_VALUE).replaceAll("0", builder.toString());
    }
}

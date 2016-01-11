package com.greenfrvr.annyprefs.annotation;

/**
 * Created by greenfrvr
 */
public @interface ObjectPref {

    int keyRes() default -1;

    String key() default "";

    Class<?> type();

}

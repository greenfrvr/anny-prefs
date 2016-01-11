package com.greenfrvr.annyprefs.annotation;

/**
 * Created by greenfrvr
 */
public @interface ObjectPref {

    String key() default "";

    Class<?> type();

}

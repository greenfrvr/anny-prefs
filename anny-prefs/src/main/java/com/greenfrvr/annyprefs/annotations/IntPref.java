package com.greenfrvr.annyprefs.annotations;

/**
 * Created by greenfrvr
 */
public @interface IntPref {

    String key() default "";

    int value() default 0;

}

package com.greenfrvr.annyprefs.annotations;

/**
 * Created by greenfrvr
 */
public @interface BoolPref {

    String key() default "";

    boolean value() default false;
}

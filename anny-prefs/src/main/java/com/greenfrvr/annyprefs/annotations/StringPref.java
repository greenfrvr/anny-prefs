package com.greenfrvr.annyprefs.annotations;

/**
 * Created by greenfrvr
 */
public @interface StringPref {

    String key() default "";

    String value() default "";

}

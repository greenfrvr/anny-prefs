package com.greenfrvr.annyprefs.annotations;

/**
 * Created by greenfrvr
 */
public @interface LongPref {

    String key() default "";

    long value() default 0L;

}

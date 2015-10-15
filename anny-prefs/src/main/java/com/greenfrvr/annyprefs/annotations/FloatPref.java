package com.greenfrvr.annyprefs.annotations;

/**
 * Created by greenfrvr
 */
public @interface FloatPref {

    String key() default "";

    float value() default 0.0f;

}

package com.greenfrvr.annyprefs.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by greenfrvr
 */
@Target(METHOD)
@Retention(CLASS)
public @interface StringSetPref {

    String key() default "";

}

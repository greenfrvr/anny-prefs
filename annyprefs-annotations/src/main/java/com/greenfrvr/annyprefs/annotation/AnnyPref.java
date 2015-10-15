package com.greenfrvr.annyprefs.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by greenfrvr
 */
@Target(TYPE)
@Retention(CLASS)
public @interface AnnyPref {
}

package com.greenfrvr.annyprefs.annotation;

import android.support.annotation.StringRes;

/**
 * Created by greenfrvr
 */
public @interface ObjectPref {

    @StringRes int keyRes() default -1;

    String key() default "";

    Class<?> type();

}

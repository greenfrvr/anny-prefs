package com.greenfrvr.annyprefs.compiler.prefs;

import com.greenfrvr.annyprefs.annotation.BoolPref;
import com.greenfrvr.annyprefs.annotation.FloatPref;
import com.greenfrvr.annyprefs.annotation.IntPref;
import com.greenfrvr.annyprefs.annotation.LongPref;
import com.greenfrvr.annyprefs.annotation.StringPref;
import com.greenfrvr.annyprefs.annotation.StringSetPref;

import java.lang.annotation.Annotation;

/**
 * Created by greenfrvr
 */
public class PrefFieldFactory {

    public static PrefField build(Class<? extends Annotation> cls){

        if (cls.equals(StringPref.class)) {
            return new StringField();
        }

        if (cls.equals(IntPref.class)) {
            return new IntField();
        }

        if (cls.equals(FloatPref.class)) {
            return new FloatField();
        }

        if (cls.equals(LongPref.class)) {
            return new LongField();
        }

        if (cls.equals(BoolPref.class)) {
            return new BooleanField();
        }

        if (cls.equals(StringSetPref.class)) {
            return new StringSetField();
        }

        return null;
    }

}

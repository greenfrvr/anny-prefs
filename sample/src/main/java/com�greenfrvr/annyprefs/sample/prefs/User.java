package com.greenfrvr.annyprefs.sample.prefs;


import com.greenfrvr.annyprefs.annotation.AnnyPref;
import com.greenfrvr.annyprefs.annotation.BoolPref;
import com.greenfrvr.annyprefs.annotation.FloatPref;
import com.greenfrvr.annyprefs.annotation.IntPref;
import com.greenfrvr.annyprefs.annotation.LongPref;
import com.greenfrvr.annyprefs.annotation.StringPref;

/**
 * Created by greenfrvr
 */
@AnnyPref(name = "user")
public interface User {

    @BoolPref(key = "user_first_launch", value = true)
    void firstLaunch();

    @StringPref(key = "user_name", value = "name")
    void username();

    @StringPref(key = "user_surname", value = "surname")
    void surname();

    @StringPref(value = "email@gmail.com")
    void email();

    @FloatPref(key = "user_rate")
    void rate();

    @IntPref(key = "user_age")
    void age();

}

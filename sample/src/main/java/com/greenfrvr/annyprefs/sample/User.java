package com.greenfrvr.annyprefs.sample;


import com.greenfrvr.annyprefs.annotation.AnnyPref;
import com.greenfrvr.annyprefs.annotation.BoolPref;
import com.greenfrvr.annyprefs.annotation.FloatPref;
import com.greenfrvr.annyprefs.annotation.IntPref;
import com.greenfrvr.annyprefs.annotation.LongPref;
import com.greenfrvr.annyprefs.annotation.StringPref;

/**
 * Created by greenfrvr
 */
@AnnyPref
public interface User {

    @StringPref(key = "user_name", value = "greenfrvr")
    void username();

    @StringPref(key = "user_email", value = "greenfrvr@gmail.com")
    void email();

    @BoolPref(key = "user_first_launch", value = true)
    void firstLaunch();

    @FloatPref(key = "user_rate", value = 0.0f)
    void rate();

    @LongPref(key = "user_last_visit", value = 0)
    void lastVisit();

    @IntPref(key = "user_age", value = 16)
    void age();

}

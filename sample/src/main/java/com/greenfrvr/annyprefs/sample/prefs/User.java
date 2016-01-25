package com.greenfrvr.annyprefs.sample.prefs;


import com.greenfrvr.annyprefs.annotation.AnnyPref;
import com.greenfrvr.annyprefs.annotation.BoolPref;
import com.greenfrvr.annyprefs.annotation.DatePref;
import com.greenfrvr.annyprefs.annotation.FloatPref;
import com.greenfrvr.annyprefs.annotation.IntPref;
import com.greenfrvr.annyprefs.annotation.ObjectPref;
import com.greenfrvr.annyprefs.annotation.StringPref;
import com.greenfrvr.annyprefs.sample.Address;
import com.greenfrvr.annyprefs.sample.R;

import java.util.Date;

/**
 * Created by greenfrvr
 */
@AnnyPref(name = "user")
public interface User {

    @BoolPref(keyRes = R.string.first_launch, value = true)
    void firstLaunch(Boolean firstLaunch);

    @StringPref(keyRes = R.string.username, value = "name")
    void username(String username);

    @StringPref(keyRes = R.string.name , value = "surname")
    void name(String surname);

    @StringPref(value = "email@gmail.com")
    void email(String email);

    @FloatPref(keyRes = R.string.rate)
    void rate(Float rate);

    @IntPref
    void age(Integer age);

    @DatePref
    void birthday(Date date);

    @ObjectPref(key = "test_model", type = Address.class)
    void address(Address address);

}

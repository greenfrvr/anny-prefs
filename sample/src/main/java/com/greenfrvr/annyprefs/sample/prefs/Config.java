package com.greenfrvr.annyprefs.sample.prefs;

import com.greenfrvr.annyprefs.annotation.AnnyPref;
import com.greenfrvr.annyprefs.annotation.BoolPref;
import com.greenfrvr.annyprefs.annotation.DatePref;
import com.greenfrvr.annyprefs.annotation.IntPref;
import com.greenfrvr.annyprefs.annotation.LongPref;
import com.greenfrvr.annyprefs.annotation.StringPref;
import com.greenfrvr.annyprefs.annotation.StringSetPref;

/**
 * Created by greenfrvr
 */
@AnnyPref
public interface Config {

    @IntPref(key = "launches_count", value = 1)
    void launches();

    @DatePref(key = "last_visit_date")
    void lastVisit();

    @StringPref(key = "last_query", value = "query")
    void lastQuery();

    @BoolPref
    void isSubscribed();

    @StringSetPref(value = {"main", "local"})
    void channels();

}

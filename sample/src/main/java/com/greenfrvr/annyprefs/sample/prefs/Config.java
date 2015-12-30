package com.greenfrvr.annyprefs.sample.prefs;

import com.greenfrvr.annyprefs.annotation.AnnyPref;
import com.greenfrvr.annyprefs.annotation.BoolPref;
import com.greenfrvr.annyprefs.annotation.IntPref;
import com.greenfrvr.annyprefs.annotation.LongPref;
import com.greenfrvr.annyprefs.annotation.StringPref;

/**
 * Created by greenfrvr
 */
@AnnyPref
public interface Config {

    @IntPref(key = "launches_count", value = 1)
    void visits();

    @StringPref(key = "last_query", value = "query")
    void query();

    @BoolPref
    void subscribed();

    @LongPref(key = "last_visit_date")
    void lastVisit();

}

package com.greenfrvr.annyprefs.compiler;

import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.greenfrvr.annyprefs.compiler.prefs.PrefFieldFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class Anny {

    private List<PrefField> prefs;

    public Anny() {
        prefs = new ArrayList<>();
    }

    public void addElement(Element el, Class<? extends Annotation> cls) {
        PrefField field = PrefFieldFactory.build(cls);
        if (field != null) {
            field.setSource(el);
            prefs.add(field);
        }
    }


    public List<PrefField> getPrefs() {
        return prefs;
    }
}

package com.greenfrvr.annyprefs.compiler;

import com.google.common.collect.Lists;
import com.greenfrvr.annyprefs.compiler.components.util.Constructor;
import com.greenfrvr.annyprefs.compiler.components.util.Generator;
import com.greenfrvr.annyprefs.compiler.components.PrefInstanceGenerator;
import com.greenfrvr.annyprefs.compiler.components.interfaces.RemoveInterfaceGenerator;
import com.greenfrvr.annyprefs.compiler.components.interfaces.RestoreInterfaceGenerator;
import com.greenfrvr.annyprefs.compiler.prefs.PrefField;
import com.greenfrvr.annyprefs.compiler.prefs.PrefFieldFactory;
import com.greenfrvr.annyprefs.compiler.components.interfaces.SaveInterfaceGenerator;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;

/**
 * Created by greenfrvr
 */
public class Anny implements DataSource, Constructor, Generator {

    private String name;
    private String prefsName;
    private String packageName;
    private List<PrefField> prefs;

    public Anny(String name, String prefsName, String packageName) {
        this.name = name;
        this.prefsName = prefsName;
        this.packageName = packageName;
        this.prefs = Lists.newArrayList();
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

    @Override
    public Generator construct() {

        return this;
    }

    @Override
    public void generate(Filer filer) throws IOException {
        SaveInterfaceGenerator.init(this).construct().generate(filer);
        RestoreInterfaceGenerator.init(this).construct().generate(filer);
        RemoveInterfaceGenerator.init(this).construct().generate(filer);

        PrefInstanceGenerator.init(this).construct().generate(filer);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String prefsName() {
        return prefsName;
    }

    @Override
    public String packageName() {
        return packageName;
    }

    @Override
    public List<PrefField> prefs() {
        return prefs;
    }
}

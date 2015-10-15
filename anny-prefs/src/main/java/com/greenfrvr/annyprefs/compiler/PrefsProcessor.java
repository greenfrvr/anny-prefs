package com.greenfrvr.annyprefs.compiler;

import com.google.auto.service.AutoService;
import com.greenfrvr.annyprefs.annotations.BoolPref;
import com.greenfrvr.annyprefs.annotations.FloatPref;
import com.greenfrvr.annyprefs.annotations.IntPref;
import com.greenfrvr.annyprefs.annotations.LongPref;
import com.greenfrvr.annyprefs.annotations.StringPref;
import com.greenfrvr.annyprefs.annotations.StringSetPref;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by greenfrvr
 */
@AutoService(Processor.class)
public class PrefsProcessor extends AbstractProcessor{

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(StringPref.class.getCanonicalName());
        annotations.add(IntPref.class.getCanonicalName());
        annotations.add(LongPref.class.getCanonicalName());
        annotations.add(FloatPref.class.getCanonicalName());
        annotations.add(BoolPref.class.getCanonicalName());
        annotations.add(StringSetPref.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

}

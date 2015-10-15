package com.greenfrvr.annyprefs.compiler;

import com.google.auto.service.AutoService;
import com.greenfrvr.annyprefs.annotation.AnnyPref;
import com.greenfrvr.annyprefs.annotation.BoolPref;
import com.greenfrvr.annyprefs.annotation.FloatPref;
import com.greenfrvr.annyprefs.annotation.IntPref;
import com.greenfrvr.annyprefs.annotation.LongPref;
import com.greenfrvr.annyprefs.annotation.StringPref;
import com.greenfrvr.annyprefs.annotation.StringSetPref;
import com.greenfrvr.annyprefs.compiler.prefs.PrefField;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by greenfrvr
 */
@AutoService(Processor.class)
public class PrefsProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<String, Anny> map;
    private static final List<Class<? extends Annotation>> classes = Arrays.asList(
            StringPref.class, IntPref.class,
            LongPref.class, FloatPref.class,
            BoolPref.class, StringSetPref.class
    );

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        map = new LinkedHashMap<>();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(AnnyPref.class.getCanonicalName());
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
        for (Element element : roundEnv.getElementsAnnotatedWith(AnnyPref.class)) {

            if (element.getKind() != ElementKind.INTERFACE) {
                error(element, "Only classes can be annotated with @%s", AnnyPref.class.getSimpleName());
                return true;
            }
            System.out.println("Got AnnyPref annotation for [" + element.getSimpleName().toString() + "] class!");
            map.put(element.getSimpleName().toString(), new Anny());
        }

        searchAnnotations(roundEnv);
        printAnnyPrefs();

        return true;
    }

    private void searchAnnotations(RoundEnvironment roundEnv) {
        for (Class<? extends Annotation> cls : classes) {
            searchAnnotationClass(roundEnv, cls);
        }
    }

    private void searchAnnotationClass(RoundEnvironment roundEnv, Class<? extends Annotation> cls) {
        System.out.println(String.format("Scanning for %s annotation", cls.getSimpleName()));
        for (Element el : roundEnv.getElementsAnnotatedWith(cls)) {
            if (map.get(el.getEnclosingElement().getSimpleName().toString()) != null) {
                map.get(el.getEnclosingElement().getSimpleName().toString()).addElement(el, cls);
            }
        }
    }

    private void printAnnyPrefs() {
        for (Anny anny : map.values()) {
            for (PrefField pref : anny.getPrefs()) {
                System.out.println(pref.toString());
            }
        }
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

}

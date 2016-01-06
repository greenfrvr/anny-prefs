package com.greenfrvr.annyprefs.compiler;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.greenfrvr.annyprefs.annotation.AnnyPref;
import com.greenfrvr.annyprefs.annotation.BoolPref;
import com.greenfrvr.annyprefs.annotation.DatePref;
import com.greenfrvr.annyprefs.annotation.FloatPref;
import com.greenfrvr.annyprefs.annotation.IntPref;
import com.greenfrvr.annyprefs.annotation.LongPref;
import com.greenfrvr.annyprefs.annotation.StringPref;
import com.greenfrvr.annyprefs.annotation.StringSetPref;
import com.greenfrvr.annyprefs.compiler.components.AdapterGenerator;
import com.greenfrvr.annyprefs.compiler.utils.Utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
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
public class PreferencesProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<String, Anny> map;
    private AdapterGenerator adapter;

    private static final Class<? extends AnnyPref> ANNY = AnnyPref.class;
    private static final List<Class<? extends Annotation>> CLASSES = Arrays.asList(
            StringPref.class, IntPref.class, LongPref.class, FloatPref.class,
            BoolPref.class, DatePref.class, StringSetPref.class
    );

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();

        map = Maps.newHashMap();
        adapter = AdapterGenerator.init();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = Sets.newHashSet(ANNY.getCanonicalName());
        CLASSES.stream().forEach(aClass -> annotations.add(aClass.getCanonicalName()));

        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ANNY)) {

            if (element.getKind() != ElementKind.INTERFACE) {
                error(element, "Only classes can be annotated with @%s", ANNY.getSimpleName());
                return true;
            }

            String className = element.getSimpleName().toString();
            String packageName = elementUtils.getPackageOf(element).toString();
            System.out.println("Got AnnyPref annotation for [" + elementUtils.getPackageOf(element).getQualifiedName() + "." + className + "] class!" + packageName);

            map.put(className, new Anny(className, element.getAnnotation(ANNY).name(), packageName));
            adapter.add(className, className.concat(Utils.PREFS));
        }

        CLASSES.stream().forEach(aClass -> searchAnnotationClass(roundEnv, aClass));
        printAnnyPrefs();

        generatePreferences();
        generateAdapter();

        return true;
    }

    private void searchAnnotationClass(RoundEnvironment roundEnv, Class<? extends Annotation> cls) {
        System.out.println(String.format("Scanning for %s annotation", cls.getSimpleName()));
        roundEnv.getElementsAnnotatedWith(cls).stream()
                .filter(el -> map.get(key(el)) != null)
                .forEach(el -> map.get(key(el)).addElement(el, cls));
    }

    private String key(Element element) {
        return element.getEnclosingElement().getSimpleName().toString();
    }

    private void generatePreferences() {
        map.values().stream().forEach(anny -> {
            try {
                anny.construct().generate(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void generateAdapter() {
        try {
            adapter.construct().generate(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    //TODO - remove, debug purposes only
    private void printAnnyPrefs() {
        map.values().forEach(anny -> anny.getPrefs().forEach(pref -> System.out.println(pref.toString())));
    }
}

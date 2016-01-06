package com.greenfrvr.annyprefs.compiler.components.util;

import java.io.IOException;

import javax.annotation.processing.Filer;

/**
 * Created by greenfrvr
 */
public interface Generator {

    void generate(Filer filer) throws IOException;

}

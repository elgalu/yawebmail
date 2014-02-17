/*
 * @(#)ClassLoaderUtils.java 1.00 2007/06/13
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 13.06.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util;

/**
 * Stellt Hilfsroutinen im Bereich ClassLoader bereit.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class ClassLoaderUtils {

  /**
   * Liefert den Classloader des aktuellen Threas. Wenn das fehlschlaegt wird
   * versucht, den Classloader aus dem uebergebenen Objekt zurueckzuliefern.
   * 
   * @param   defaultObject  Objekt, dessen Classloader geliefert wird, wenn
   *                         der Classloader des aktuellen Threas nicht bezogen
   *                         werden kann.
   * @return  <code>ClassLoader</code>-Objekt.
   */
  public static ClassLoader getCurrentClassLoader(Object defaultObject){

    ClassLoader loader = Thread.currentThread().getContextClassLoader();

    if(loader == null){

      loader = defaultObject.getClass().getClassLoader();
    }

    return(loader);
  }

}

/*
 * @(#)MessageMapper.java 1.00 2006/11/29
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 29.11.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Holt aus den Message-Properties die Ensprechung als String
 * 
 * @author Stephan Sann
 * @version 1.0
 */

public class MessageMapper {

  /**
   * Holt den Message-String zu einem Key in einem Resourcebundle.
   * 
   * @param   bundleName  Name des Resource-Bundles
   * @param   key         Schluessel des Message-Strings
   * @param   params      Array mit Erstetzungs-Werten (z.B. "min {0} chars")
   * @param   locale      Locale, fuer die das Resource-Bundle gesucht wird
   * @return  <code>String</code>-Objekt
   */
  public static String getMessageResourceString(String bundleName, String key,
          Object[] params, Locale locale) {

    String text = null;

    ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);

    // Versuchen wir mal das Property zu holen!
    try {

      text = bundle.getString(key);
    }
    catch(MissingResourceException e) {

      text = "Property " + key + " not found !!";
    }

    // Wenn "params" != null, dann muessen wir ggf. Replacements durchfuehren
    if((params != null) && (params.length >= 1)) {

      MessageFormat mf = new MessageFormat(text, locale);
      text = mf.format(params, new StringBuffer(), null).toString();
    }

    return(text);
  }

}

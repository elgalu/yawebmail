/*
 * @(#)OutputLocalizedTextUIComponent.java 1.00 2007/02/09
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 09.02.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.cuic;

import java.io.IOException;
import java.util.Locale;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.lotk.yawebmail.util.MessageMapper;

/**
 * UI-Component zur Ausgabe eines Textes mit meiner gegebenen Locale 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class OutputLocalizedTextUIComponent extends UIOutput {

  /* (non-Javadoc)
   * @see javax.faces.component.UIComponentBase#encodeBegin(javax.faces.context.FacesContext)
   */
  public void encodeBegin(FacesContext context) throws IOException {

    // span-Tag ausgeben
    ResponseWriter writer = context.getResponseWriter();

    // String aus Resource-Datei auslesen
    String nameMessageBundle = context.getApplication().getMessageBundle();
    Locale locale = new Locale((String)getAttributes().get("language"));
    String resourceKey = (String)getAttributes().get("key");

    String partContent =
            MessageMapper.getMessageResourceString(nameMessageBundle,
            resourceKey, null, locale);

    // Part-Content ausgeben
    writer.write(partContent);
  }

}

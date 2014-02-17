/*
 * @(#)LocaleTypeConverter.java 1.00 2007/02/19
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 19.02.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util.faces;

import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Konvertiert Locale-Objekte in Strings und vice versa (zur Verwendung in
 * Dropdown-Listen).
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class LocaleTypeConverter implements Converter {

  /* (non-Javadoc)
   * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
   */
  public Object getAsObject(FacesContext context, UIComponent comp, String
          value) throws ConverterException {

    return(new Locale(value));
  }

  /* (non-Javadoc)
   * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
   */
  public String getAsString(FacesContext context, UIComponent component, Object
          object) throws ConverterException {

    if((object == null) || (! (object instanceof Locale))) {

      return(null);
    }

    return(((Locale)object).getLanguage());
  }

}

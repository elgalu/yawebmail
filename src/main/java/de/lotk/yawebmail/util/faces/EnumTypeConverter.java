/*
 * @(#)EnumTypeConverter.java 1.00 2007/08/30
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 30.08.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util.faces;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Konvertiert Enum-Objekte in Strings und vice versa (zur Verwendung in
 * Dropdown-Listen).
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class EnumTypeConverter implements Converter {

  /* (non-Javadoc)
   * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public Object getAsObject(FacesContext facescontext, UIComponent uicomponent,
          String s) throws ConverterException {

    Class enumType = uicomponent.getValueBinding("value").getType(facescontext);
    return(Enum.valueOf(enumType, s));
  }

  /* (non-Javadoc)
   * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
   */
  public String getAsString(FacesContext facescontext, UIComponent uicomponent, Object obj) throws ConverterException {

    // Uebergebenes Objekt darf nicht NULL und muss eine Enum sein.
    if((obj == null) || (! (obj instanceof Enum))) {

      return(null);
    }

    //Enum type = (Enum)obj;
    //return type.toString(); 
    return(obj.toString());
  }

}

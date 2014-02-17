/*
 * @(#)ManagedBeanUtils.java 1.00 2007/06/13
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 13.06.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util.faces;

import javax.faces.context.FacesContext;

/**
 * Stellt Hilfsroutinen im Bereich Managed Beans bereit.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class ManagedBeanUtils {

  // TODO alle Klassen auf diese Methode umstellen

  /**
   * Liefert eine in der "faces-config.xml" deklarierte Managed Bean ueber ihren
   * Namen.
   *
   * @param   fc    Aktueller Faces-Context
   * @param   name  Name der Managed Bean in der "faces-config.xml"
   * @return        Bean, die unter dem uebergebenen Namen deklariert wurde.
   * @throws        <code>Exception</code>, wenn unter dem uebergebenen Namen
   *                keine Bean gefunden wurde. 
   */
  public static Object getManagedBeanByName(FacesContext fc, String name) throws
          Exception {

    Object rueckgabe =
            fc.getApplication().getVariableResolver().resolveVariable(fc, name);

    if(rueckgabe == null) {

      throw(new Exception("Managed bean \"" + name + "\" not found!"));
    }

    return(rueckgabe);
  }

}

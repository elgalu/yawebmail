/*
 * @(#)BaseController.java 1.00 2006/02/17
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 17.02.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.controller;

import javax.faces.context.FacesContext;

import de.lotk.yawebmail.util.faces.ManagedBeanUtils;

/**
 * Basis-Controller 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class BaseController {

  /**
   * Liefert eine in der "faces-config.xml" deklarierte Managed Bean ueber ihren
   * Namen.
   * 
   * @param   name  Name der Managed Bean in der "faces-config.xml"
   * @return        Bean, die unter dem uebergebenen Namen deklariert wurde.
   * @throws        <code>Exception</code>, wenn unter dem uebergebenen Namen
   *                keine Bean gefunden wurde. 
   */
  protected Object getManagedBeanByName(String name) throws Exception {

    FacesContext fc = FacesContext.getCurrentInstance();
    return(ManagedBeanUtils.getManagedBeanByName(fc, name));
  }

}

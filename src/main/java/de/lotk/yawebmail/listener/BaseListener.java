/*
 * @(#)BaseListener.java 1.00 2006/02/21
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 21.02.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.listener;

import javax.faces.context.FacesContext;

import de.lotk.yawebmail.util.faces.ManagedBeanUtils;

/**
 * Basis-Listener 
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class BaseListener {

  protected Object getManagedBeanByName(FacesContext fc, String name) throws
          Exception {

    return(ManagedBeanUtils.getManagedBeanByName(fc, name));
  }

}

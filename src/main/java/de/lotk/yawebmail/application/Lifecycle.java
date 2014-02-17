/*
 * @(#)Lifecycle.java 1.00 2005/01/29
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 29.01.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.application;

/**
 * Definiert Methoden fuer Objekte, die einen Lifecycle haben.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public interface Lifecycle {

  //void init()
  //boolean prepareDestroy()


  /**
   * Wird aufgerufen, wenn ein Objekt zerstoert wird.
   */
  public void destroy();

}

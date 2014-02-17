/*
 * @(#)ReversibleComparator.java 1.00 2005/11/27
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 07.06.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.business;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Abstrakte Klasse fuer einen umkehrbaren Comparator
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public abstract class ReversibleComparator<T> implements Comparator<T>,
        Serializable {

  // --------------------------------------------------------- Instanz-Variablen

  /** Soll umgekehrt sortiert werden? */
  protected boolean reverse = false;


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return Returns the reverse.
   */
  public boolean isReverse() {
  
    return reverse;
  }

  /**
   * @param reverse The reverse to set.
   */
  public void setReverse(boolean reverse) {
  
    this.reverse = reverse;
  }

}

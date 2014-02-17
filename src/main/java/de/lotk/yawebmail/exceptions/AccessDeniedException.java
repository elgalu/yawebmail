/*
 * @(#)AccessDeniedException.java 1.00 2004/09/19
 *
 * Copyright (c) 2004, Stephan Sann
 *
 * 19.09.2004 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.exceptions;

/**
 * Tritt auf, wenn der Server den Zugriff verweigert.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class AccessDeniedException extends Exception {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = 4209087821626315511L;


  /**
   * 
   */
  public AccessDeniedException() {

    super();
  }

  /**
   * @param message
   */
  public AccessDeniedException(String message) {

    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public AccessDeniedException(String message, Throwable cause) {

    super(message, cause);
  }

  /**
   * @param cause
   */
  public AccessDeniedException(Throwable cause) {

    super(cause);
  }

}

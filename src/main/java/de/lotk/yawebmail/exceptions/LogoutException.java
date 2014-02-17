/*
 * @(#)LogoutException.java 1.00 2005/02/17
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 17.02.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.exceptions;

/**
 * Tritt auf, wenn es ein Problem beim Logout gibt.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class LogoutException extends Exception {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = 2121434747823576896L;


  /**
   * 
   */
  public LogoutException() {

    super();
  }

  /**
   * @param message
   */
  public LogoutException(String message) {

    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public LogoutException(String message, Throwable cause) {

    super(message, cause);
  }

  /**
   * @param cause
   */
  public LogoutException(Throwable cause) {

    super(cause);
  }

}

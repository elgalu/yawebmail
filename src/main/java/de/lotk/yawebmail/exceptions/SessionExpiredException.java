/*
 * @(#)SessionExpiredException.java 1.00 2006/05/02
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 02.05.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.exceptions;

/**
 * Tritt auf, wenn die Session abgelaufen ist.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class SessionExpiredException extends Exception {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = -668952224495318411L;


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * 
   */
  public SessionExpiredException() {

    super();
  }

  /**
   * @param message
   */
  public SessionExpiredException(String message) {

    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public SessionExpiredException(String message, Throwable cause) {

    super(message, cause);
  }

  /**
   * @param cause
   */
  public SessionExpiredException(Throwable cause) {

    super(cause);
  }

}

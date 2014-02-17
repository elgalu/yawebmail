/*
 * @(#)MessageRetrieveException.java 1.00 2005/02/06
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 06.02.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.exceptions;

/**
 * Tritt auf, wenn es ein Probleme beim Empfangen einer Message gibt.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class MessageRetrieveException extends Exception {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = -3709479124876059382L;


  /**
   * 
   */
  public MessageRetrieveException() {

    super();
  }

  /**
   * @param message
   */
  public MessageRetrieveException(String message) {

    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public MessageRetrieveException(String message, Throwable cause) {

    super(message, cause);
  }

  /**
   * @param cause
   */
  public MessageRetrieveException(Throwable cause) {

    super(cause);
  }

}

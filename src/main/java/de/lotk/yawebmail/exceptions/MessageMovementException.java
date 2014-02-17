/*
 * @(#)MessageDeletionException.java 1.00 2007/07/26
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 26.07.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.exceptions;

/**
 * Tritt auf, wenn es ein Probleme beim Verschieben einer Message gibt.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class MessageMovementException extends Exception {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = 5693305569212745310L;


  /**
   * 
   */
  public MessageMovementException() {

    super();
  }

  /**
   * @param message
   */
  public MessageMovementException(String message) {

    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public MessageMovementException(String message, Throwable cause) {

    super(message, cause);
  }

  /**
   * @param cause
   */
  public MessageMovementException(Throwable cause) {

    super(cause);
  }

}

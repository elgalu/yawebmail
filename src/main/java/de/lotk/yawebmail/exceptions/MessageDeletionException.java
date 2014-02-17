/*
 * @(#)MessageDeletionException.java 1.00 2005/02/14
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 14.02.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.exceptions;

/**
 * Tritt auf, wenn es ein Probleme beim Loeschen einer Message gibt.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class MessageDeletionException extends Exception {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = 5693305569212800310L;


  /**
   * 
   */
  public MessageDeletionException() {

    super();
  }

  /**
   * @param message
   */
  public MessageDeletionException(String message) {

    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public MessageDeletionException(String message, Throwable cause) {

    super(message, cause);
  }

  /**
   * @param cause
   */
  public MessageDeletionException(Throwable cause) {

    super(cause);
  }

}

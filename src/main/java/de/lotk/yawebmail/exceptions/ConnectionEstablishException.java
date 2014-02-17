/*
 * @(#)ConnectionEstablishException.java 1.00 2004/09/19
 *
 * Copyright (c) 2004, Stephan Sann
 *
 * 19.09.2004 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.exceptions;

/**
 * Tritt auf, wenn keine Verbindung zum Server aufgebaut werden kann.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class ConnectionEstablishException extends Exception {

  // ---------------------------------------------------------------- Konstanten

  /** serialVersionUID */
  private static final long serialVersionUID = 5537038375284009080L;

  // --------------------------------------------------------- Instanz-Variablen

  /** Speichert den Host */
  private String host = null;

  /** Speichert den Port */
  private int port = (-1);


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialisiert eine ConnectionEstablishException mit einer Message, einem
   * Host und einem Port
   * 
   * @param   message  Message fuer die Exception
   * @param   host     Host, gegen den connected werden sollte
   * @param   port     Port, gegen den connected werden sollte
   */
  public ConnectionEstablishException(String message, String host, int port) {

    super(message);
    this.host = host;
    this.port = port;
  }

  /**
   * Initialisiert eine ConnectionEstablishException mit einer Message, einem
   * ausloesenden Throwable, einem Host und einem Port.
   * 
   * @param   message  Message fuer die Exception
   * @param   cause    Ausloesender Throwable
   * @param   host     Host, gegen den connected werden sollte
   * @param   port     Port, gegen den connected werden sollte
   */
  public ConnectionEstablishException(String message, Throwable cause, String
          host, int port) {

    super(message, cause);
    this.host = host;
    this.port = port;
  }


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return Returns the host.
   */
  public String getHost() {
  
    return host;
  }

  /**
   * @return Returns the port.
   */
  public int getPort() {
  
    return port;
  }

}

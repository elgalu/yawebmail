/*
 * @(#)MailboxConnectionFactory.java 1.00 2005/02/05
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 05.02.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.business;

import de.lotk.yawebmail.bean.LoginDataBean;

/**
 * Liefert MailboxConnection-Instanzen
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class MailboxConnectionFactory {

  // --------------------------------------------------------- Klassen-Variablen

  /** Singelton-Instanz */
  private static MailboxConnectionFactory mailboxConnectionFactory =
          new MailboxConnectionFactory();


  // --------------------------------------------------------------- Konstruktor

  /**
   * Privater Singelton-Konstruktor.
   */
  private MailboxConnectionFactory() {
  }


  // ------------------------------------------- oeffentliche statische Methoden

  /**
   * Liefert eine Instanz der MailboxConnectionFactory.
   * 
   * @return       Instanz der <code>MailboxConnectionFactory</code>
   */
  public static MailboxConnectionFactory getInstance() {

    return(mailboxConnectionFactory);
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Liefert eine MailboxConnection-Instanz.
   * 
   * @param    loginData   Die Login-Daten
   * @throws   InstantiationException
   */
  public MailboxConnection createMailboxConnection(LoginDataBean loginData,
          YawebmailTrustManager trustManager) throws InstantiationException {

    return(new DefaultMailboxConnection(loginData, trustManager));
  }

}

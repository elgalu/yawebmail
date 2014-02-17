/*
 * @(#)SmthAuthenticator.java 1.00 2006/09/04
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 04.09.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.business;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Authenticator fuer SMTP-Authentication.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class SmtpAuthenticator extends Authenticator {

  // --------------------------------------------------------- Instanz-Variablen

  /** Speichert ein PasswordAuthentication-Objekt */
  private PasswordAuthentication passwordAuthentication = null;


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialisiert ein neues SmthAuthenticator-Objekt.
   * 
   * @param   smtpAuthUser  SMTP-Auth-User
   * @param   smtpAuthPass  SMTP-Auth-Passwort
   */
  public SmtpAuthenticator(String smptAuthUser, String smtpAuthPass) {

    this.passwordAuthentication =
            new PasswordAuthentication(smptAuthUser, smtpAuthPass);
  }


  // ------------------------------------------------------ geschuetzte Methoden

  /* (non-Javadoc)
   * @see javax.mail.Authenticator#getPasswordAuthentication()
   */
  protected PasswordAuthentication getPasswordAuthentication() {

    return(this.passwordAuthentication);
  }

}

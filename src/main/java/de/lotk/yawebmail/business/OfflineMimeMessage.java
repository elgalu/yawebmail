/*
 * @(#)OfflineMimeMessage.java 1.00 2006/05/11
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 11.05.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.business;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Speichert eine MimeMessage, die offline verwendet werden kann (wenn die
 * Verbindung zum Mailbox-Server schon wieder getrennt wurde).
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class OfflineMimeMessage extends MimeMessage {

  // --------------------------------------------------------- Instanz-Variablen

  /** Speichert ggf. einen X-Spam-Level */
  private String spamLevel = null;


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialiesiert ein neues OfflineMimeMessage-Objekt mit dem Inhalt einer
   * vorhandenen MimeMessage.
   * 
   * @param   mimeMessage  MimeMessage, deren Inhalt in die neue kopiert wird.
   * @throws  MessagingException
   */
  public OfflineMimeMessage(MimeMessage mimeMessage) throws MessagingException {

    super(mimeMessage);
  }

  /**
   * Initialiesiert ein leeres OfflineMimeMessage-Objekt.
   * 
   * @param   session  Session object for this message
   * @throws  MessagingException
   */
  public OfflineMimeMessage(Session session) throws MessagingException {

    super(session);
  }


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return Returns the spamLevel.
   */
  public String getSpamLevel() {
  
    return this.spamLevel;
  }

  /**
   * @param spamLevel The xSpamLevel to set.
   */
  public void setSpamLevel(String spamLevel) {
  
    this.spamLevel = spamLevel;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Setzt die Message-Nummer.
   * 
   * @param  messageNumber  Message-Nummer, die gesetzt werden soll.
   */
  public void setMessageNumber(int messageNumber) {
    
    this.msgnum = messageNumber;
  }

  /* (non-Javadoc)
   * @see javax.mail.internet.MimeMessage#getFrom()
   */
  public Address[] getFrom() throws MessagingException {

    try {

      Address[] fromArray = super.getFrom();

      if(fromArray == null) {

        fromArray = new Address[0];
      }

      return(fromArray);
    }
    catch(Exception e) {

      e.printStackTrace();
      return(new Address[0]);
    }
  }

}

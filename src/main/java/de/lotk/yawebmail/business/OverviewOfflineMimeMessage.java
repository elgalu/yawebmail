/*
 * @(#)OverviewOfflineMimeMessage.java 1.00 2006/05/11
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 11.05.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.business;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import de.lotk.yawebmail.application.Constants;

/**
 * Erweitert eine OfflineMimeMessage um Properties, die bei der Uebersicht von
 * Nutzen sind.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class OverviewOfflineMimeMessage extends OfflineMimeMessage {

  // --------------------------------------------------------- Instanz-Variablen

  /** Subject-Tooltip */
  private String subjectTooltip = Constants.LEERSTRING;


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * @param mimeMessage
   * @throws MessagingException
   */
  public OverviewOfflineMimeMessage(MimeMessage mimeMessage) throws MessagingException {
    super(mimeMessage);
  }

  /**
   * @param session
   * @throws MessagingException
   */
  public OverviewOfflineMimeMessage(Session session) throws MessagingException {
    super(session);
  }


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return the subjectTooltip
   */
  public String getSubjectTooltip() {
    return subjectTooltip;
  }

  /**
   * @param subjectTooltip the subjectTooltip to set
   */
  public void setSubjectTooltip(String subjectTooltip) {
    this.subjectTooltip = subjectTooltip;
  }

}

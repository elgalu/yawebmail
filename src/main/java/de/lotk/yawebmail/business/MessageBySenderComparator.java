/*
 * @(#)MessageBySenderComparator.java 1.00 2005/06/06
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 06.06.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.business;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Comparator fuer das Sortieren von Messages nach Sender
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class MessageBySenderComparator<T extends Message> extends
        ReversibleComparator<T> {

  // ----------------------------------------------------------------- Constants

  /** serialVersionUID */
  private static final long serialVersionUID = 5556264714168821793L;


  // --------------------------------------------------------- Klassen-Variablen

  /** Logging-Instanz */
  protected static final Log LOG =
          LogFactory.getLog(MessageBySenderComparator.class);


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialisiert ein neues MessageBySenderComparator-Objekt.
   */
  public MessageBySenderComparator() {
  }

  /**
   * Initialisiert ein neues MessageBySenderComparator-Objekt und setzt den
   * reverse-boolean (true = Messages werden umgekehrt sortiert).
   * 
   * @param   reverse  Soll umgekehrt sortiert werden?
   */
  public MessageBySenderComparator(boolean reverse) {

    this.reverse = reverse;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /* (non-Javadoc)
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(T m1, T m2) {

    int rueck = 0;

    try {

      Address[] senderEins = m1.getFrom();
      Address[] senderZwei = m2.getFrom();

      // Sind die Sender-Arrays beider Messages am Start und nicht leer?
      boolean einsDa = ((senderEins != null) && (senderEins.length >= 1));
      boolean zweiDa = ((senderZwei != null) && (senderZwei.length >= 1));
      
      if(einsDa && zweiDa) {

        // Anfuehrungsstriche fuer die Suche rausnehmen, da sonst in
        // Anfuehrungsstriche gepackte Namen nach oben sortiert werden.
        String einsCleaned = senderEins[0].toString().replaceAll("\"", "");
        String zweiCleaned = senderZwei[0].toString().replaceAll("\"", "");

        // Wir sortieren nach dem ersten Sender
        rueck = einsCleaned.compareToIgnoreCase(zweiCleaned);
      }

      // Sonst Messages mit leeren "Froms" nach oben sortieren
      else if((! einsDa) && zweiDa) {

        rueck = (-50);
      }
      else if(einsDa && (! zweiDa)) {

        rueck = 50;
      }
      else {

        rueck = 0;
      }

      // Soll umgekehrt sortiert werden?
      if(this.reverse) {

        rueck = rueck * (-1);
      }

      return(rueck);
    }
    catch(MessagingException me) {

      // Bei einer Exception geben wir 0 zurueck.
      LOG.error("[compare] Problem getting the sender.", me);
      return(0);
    }
  }

}

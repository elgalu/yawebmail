/*
 * @(#)MessageByDateComparator.java 1.00 2005/06/07
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 07.06.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.business;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Comparator fuer das Sortieren von Messages nach Datum
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class MessageByDateComparator<T extends Message> extends
        ReversibleComparator<T> {

  // ----------------------------------------------------------------- Constants

  /** serialVersionUID */
  private static final long serialVersionUID = 5556264714168711793L;


  // --------------------------------------------------------- Klassen-Variablen

  /** Logging-Instanz */
  protected static final Log LOG =
          LogFactory.getLog(MessageByDateComparator.class);


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialisiert ein neues MessageByDateComparator-Objekt.
   */
  public MessageByDateComparator() {
  }

  /**
   * Initialisiert ein neues MessageByDateComparator-Objekt und setzt den
   * reverse-boolean (true = Messages werden umgekehrt sortiert).
   * 
   * @param   reverse  Soll umgekehrt sortiert werden?
   */
  public MessageByDateComparator(boolean reverse) {

    this.reverse = reverse;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /* (non-Javadoc)
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(T m1, T m2) {

    int rueck = 0;

    try {

      Date dateEins = m1.getSentDate();
      Date dateZwei = m2.getSentDate();

      // Sind die Dates beider Messages am Start?
      boolean einsDa = (dateEins != null);
      boolean zweiDa = (dateZwei != null);

      if(einsDa && zweiDa) {

        // Einfach die Dates vergleichen
    	rueck = dateEins.compareTo(dateZwei);
      }

      // Sonst Messages mit leeren Dates nach oben sortieren
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
      LOG.error("[compare] Problem getting the sent-dates.", me);
      return(0);
    }
  }

}

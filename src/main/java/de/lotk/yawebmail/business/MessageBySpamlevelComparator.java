/*
 * @(#)MessageBySpamlevelComparator.java 1.00 2005/11/27
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 07.06.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.business;

import javax.mail.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Comparator fuer das Sortieren von Messages nach Spamlevel
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class MessageBySpamlevelComparator<T extends Message> extends
        ReversibleComparator<T> {

  // ----------------------------------------------------------------- Constants

  /** serialVersionUID */
  private static final long serialVersionUID = 5556264714168733793L;


  // --------------------------------------------------------- Klassen-Variablen

  /** Logging-Instanz */
  protected static final Log LOG =
          LogFactory.getLog(MessageBySpamlevelComparator.class);


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialisiert ein neues MessageBySpamlevelComparator-Objekt.
   */
  public MessageBySpamlevelComparator() {
  }

  /**
   * Initialisiert ein neues MessageBySpamlevelComparator-Objekt und setzt den
   * reverse-boolean (true = Messages werden umgekehrt sortiert).
   * 
   * @param   reverse  Soll umgekehrt sortiert werden?
   */
  public MessageBySpamlevelComparator(boolean reverse) {

    this.reverse = reverse;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /*
   * (non-Javadoc)
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(T m1, T m2) {

    int rueck = 0;

    String slEins = null;
    String slZwei = null;

    // Get spamlevel from both messages (works only on OfflineMimeMessages)
    // -> In case of something went wrong at the creation of the
    //    OfflineMimeMessage, object may be of type "Message"
    if(m1 instanceof OfflineMimeMessage) {

      slEins = ((OfflineMimeMessage)m1).getSpamLevel();  
    }
    if(m2 instanceof OfflineMimeMessage) {

      slZwei = ((OfflineMimeMessage)m2).getSpamLevel();  
    }

    // Sind die Spamlevel beider Messages am Start?
    boolean einsDa = (slEins != null);
    boolean zweiDa = (slZwei != null);

    if(einsDa && zweiDa) {

      // Jetzt einfach die Laenge der Strings vergleichen
      rueck = slEins.length() - slZwei.length();
    }

    // Sonst Messages mit leeren Spamleveln nach oben sortieren
    else if((!einsDa) && zweiDa) {

      rueck = (-50);
    }
    else if(einsDa && (!zweiDa)) {

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

}

/*
 * @(#)MessageBySubjectComparator.java 1.00 2005/06/07
 *
 * Copyright (c) 2005, Stephan Sann
 *
 * 07.06.2005 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.business;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.perl.Perl5Util;

/**
 * Comparator fuer das Sortieren von Messages nach Subject
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class MessageBySubjectComparator<T extends Message> extends
        ReversibleComparator<T> {

  // ----------------------------------------------------------------- Constants

  /** serialVersionUID */
  private static final long serialVersionUID = 5556264934168711793L;


  // --------------------------------------------------------- Klassen-Variablen

  /** Logging-Instanz */
  protected static final Log LOG =
          LogFactory.getLog(MessageBySubjectComparator.class);

  /** Perl5Util fuer Suchen und/oder Ersetzen */
  private Perl5Util perl5Util = new Perl5Util();


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialisiert ein neues MessageBySubjectComparator-Objekt.
   */
  public MessageBySubjectComparator() {
  }

  /**
   * Initialisiert ein neues MessageBySubjectComparator-Objekt und setzt den
   * reverse-boolean (true = Messages werden umgekehrt sortiert).
   * 
   * @param   reverse  Soll umgekehrt sortiert werden?
   */
  public MessageBySubjectComparator(boolean reverse) {

    this.reverse = reverse;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /* (non-Javadoc)
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(T m1, T m2) {

    int rueck = 0;

    try {

      String subjectEins = m1.getSubject();
      String subjectZwei = m2.getSubject();

      // Sind die Subjects beider Messages am Start?
      boolean einsDa = (subjectEins != null);
      boolean zweiDa = (subjectZwei != null);

      if(einsDa && zweiDa) {

        // Subject-Prefixe schneiden wir ab (Da wir keine Ahnung haben, was auf
        // aller Welt fuer Prefixe verwendet werden, nehmen wir alle zwei- bis
        // dreistelligen Buchstabenfolgen mit anschliessendem Doppelpunkt.).
        if(perl5Util.match("#^[a-zA-Z]{2,3}:#", subjectEins)) {

          subjectEins =
                  perl5Util.substitute("s#^[a-zA-Z]{2,3}:\\s*##", subjectEins);
        }
        if(perl5Util.match("#^[a-zA-Z]{2,3}:#", subjectZwei)) {

          subjectZwei =
                  perl5Util.substitute("s#^[a-zA-Z]{2,3}:\\s*##", subjectZwei);
        }

        // Jetzt einfach die Strings vergleichen
        rueck = subjectEins.trim().compareToIgnoreCase(subjectZwei.trim());
      }

      // Sonst Messages mit leeren Subjects nach oben sortieren
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
      LOG.error("[compare] Problem getting the subjects.", me);
      return(0);
    }
  }

}

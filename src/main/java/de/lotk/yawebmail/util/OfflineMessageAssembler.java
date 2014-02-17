/*
 * @(#)OfflineMessageAssembler.java 1.00 2006/05/15
 *
 * Copyright (c) 2006, Stephan Sann
 *
 * 15.05.2006 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.util;

import java.util.Date;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.lotk.yawebmail.application.Configuration;
import de.lotk.yawebmail.application.Constants;
import de.lotk.yawebmail.business.OfflineMimeMessage;
import de.lotk.yawebmail.business.OverviewOfflineMimeMessage;

/**
 * Erzeugt eine Display-Message aus einer javax.mail.Message.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class OfflineMessageAssembler {

  // --------------------------------------------------------- Klassen-Variablen

  /** Ein Session-Objekt, das zur Message-Erzeugung gebraucht wird. */
  private static Session SESSION =
          Session.getInstance(JavamailUtils.getProperties());


  // ---------------------------------------------------------- private Methoden

  /**
   * Ermittelt das "From"-Array und setzt default-Werte, falls es bei der
   * Ermittlung Probleme gibt.
   * 
   * @param   message  Message, dessen "From"-Array ermittelt werden soll.
   * @return  <code>Address[]</code> mit "From"-Adressen.
   */
  private static Address[] getFrom(MimeMessage message) throws Exception {

    // Beim "getFrom" gibt es oefter Probleme...
    Address[] fromArray = null;

    try {

      fromArray = message.getFrom();
    }
    catch(AddressException ae) {

      StringBuffer badAddress = new StringBuffer("\"");
      badAddress.append(ae.getRef().replace('"', '´')).append("\"");
      badAddress.append("<javax.mail.internet.AddressException@getFrom>");

      fromArray = new Address[1];
      fromArray[0] = new InternetAddress(badAddress.toString());
    }
    catch(NullPointerException npe) {

      StringBuffer badAddress = new StringBuffer("\"Sender NULL\"");
      badAddress.append("<java.lang.NullPointerException@getFrom>");

      fromArray = new Address[1];
      fromArray[0] = new InternetAddress(badAddress.toString());
    }

    return(fromArray);
  }

  /**
   * Ermittelt das Sent-Date und liefert NULL, falls es bei der Ermittlung
   * Probleme gibt.
   * 
   * @param   message  Message, dessen Sent-Date ermittelt werden soll.
   * @return  <code>Date</code>-Objekt mit Sent-Date, wenn ermittelt; sonst NULL
   */
  private static Date getSentDate(MimeMessage message) {

    try {

      return(message.getSentDate());
    }
    catch(Exception e) {

      return(null);
    }
  }

  /**
   * Ermittelt das Subject und liefert NULL, falls es bei der Ermittlung
   * Probleme gibt.
   * 
   * @param   message  Message, dessen Subject ermittelt werden soll.
   * @return  <code>String</code>-Objekt mit Subject, wenn ermittelt; sonst NULL
   */
  private static String getSubject(MimeMessage message) {

    try {

      return(message.getSubject());
    }
    catch(Exception e) {

      return(null);
    }
  }

  /**
   * Ermittelt ein gegebenes Header-Array und liefert NULL, falls es bei der
   * Ermittlung Probleme gibt.
   * 
   * @param   message  Message, dessen Header-Array ermittelt werden soll.
   * @return  <code>String[]</code>-Objekt mit Header, wenn ermittelt; sonst
   *          NULL
   */
  private static String[] getHeader(MimeMessage message, String headerName) {

    try {

      return(message.getHeader(headerName));
    }
    catch(Exception e) {

      return(null);
    }
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Baut aus einem Message-Objekt ein OfflineMimeMessage-Objekt. Die erzeugte
   * Mail hat lediglich die Properties, die wir fuer die Anzeige auf der
   * Uebersichtsseite brauchen.
   * 
   * @param     message   Message, aus der das OfflineMimeMessage-Objekt gebaut
   *                      werden soll.
   */
  public static MimeMessage assembleOverviewOfflineMimeMessage(MimeMessage
          message) {

    try {

      // Beim "getFrom" gibt es oefter Probleme...
      Address[] fromArray = getFrom(message);

      // Leere OfflineMimeMessage erzeugen und Header-Daten kopieren.
      OverviewOfflineMimeMessage oomm = new OverviewOfflineMimeMessage(SESSION);
      oomm.setMessageNumber(message.getMessageNumber());
      oomm.setSentDate(getSentDate(message));

      // Es kann vorkommen, dass wir keinen Absender feststellen konnten.
      if((fromArray != null) && (fromArray.length >= 1)) {

        oomm.setFrom(fromArray[0]);
      }

      // Subject und Spam-Level gibt es nicht immer...
      String subject = getSubject(message);
      String[] xSpamLevelHeaders = getHeader(message, "X-Spam-Level");

      if(subject != null) {

        // Ggf. kuerzen
        if(subject.length() > Configuration.getMaxLenSubjectOnMailslisting()) {

          // Komplettes Subject in Subject-Tooltip schreiben
          oomm.setSubjectTooltip(subject);

          subject = (subject.substring(0,
                  Configuration.getMaxLenSubjectOnMailslisting() - 3) + "...");
        }

        oomm.setSubject(subject, Constants.MESSAGE_CHAR_ENCODING);
      }

      if((xSpamLevelHeaders != null) && (xSpamLevelHeaders.length >= 1)) {

        oomm.setSpamLevel(xSpamLevelHeaders[0]);
      }

      return(oomm);
    }
    catch(Exception e) {

      e.printStackTrace();

      // Im Fehlerfall geben wir einfach die original Message zurueck. Das ist
      // nicht schoen, aber das beste, was wir hier tun koennen.
      return(message);
    }
  }

  /**
   * Baut aus einem Message-Objekt ein OfflineMimeMessage-Objekt.
   * 
   * @param     message   Message, aus der das OfflineMimeMessage-Objekt gebaut
   *                      werden soll.
   */
  public static MimeMessage assembleOfflineMimeMessage(MimeMessage message) {

    try {

      // Erstmal die uebergebene Message kopieren und Message-Nummer uebernehmen
      OfflineMimeMessage omm = new OfflineMimeMessage((MimeMessage)message);
      omm.setMessageNumber(message.getMessageNumber());

      return(omm);
    }
    catch(Exception e) {

      e.printStackTrace();

      // Im Fehlerfall geben wir einfach die original Message zurueck. Das ist
      // nicht schoen, aber das beste, was wir hier tun koennen.
      return(message);
    }
  }

}

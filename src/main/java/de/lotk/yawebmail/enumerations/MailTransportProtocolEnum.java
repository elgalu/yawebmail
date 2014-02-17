/*
 * @(#)MailTransportProtocolEnum.java 1.00 2007/08/31
 *
 * Copyright (c) 2007, Stephan Sann
 *
 * 31.08.2007 ssann        Vers. 1.0     created
 */


package de.lotk.yawebmail.enumerations;

/**
 * Enum fuer die moeglichen Mail-Transport-Protokolle
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public enum MailTransportProtocolEnum {

  // ---------------------------------------------- Moegliche Enum-Auspraegungen

  SMTP("smtp"), SMTP_SSL("smtps");


  // --------------------------------------------------------- Klassen-Variablen

  /** Alle Auspraegungen in einem Array */
  private static MailTransportProtocolEnum[] allValues =
          MailTransportProtocolEnum.values();


  // --------------------------------------------------------- Instanz-Variablen

  /** Protokoll-Kuerzel fuer die jeweilige Auspraegung */
  private String protocolId = null;


  // ----------------------------------------------------------- Konstruktor(en)

  /**
   * Initialisiert ein neues MailTransportProtocolEnum-Objekt.
   * 
   * @param   protocolId  Protokoll-Kuerzel
   */
  private MailTransportProtocolEnum(String protocolId) {

    this.protocolId = protocolId;
  }


  // --------------------------------------------------------- Getter und Setter

  /**
   * @return the protocolId
   */
  public String getProtocolId() {
  
    return protocolId;
  }


  // ----------------------------------------------------- oeffentliche Methoden

  /**
   * Liefert ein MailTansportProtocolEnum anhand einer Protokoll-ID.
   * 
   * @param   protocolId  Die Protokoll-ID ("smtp", "smtps", ...)
   * @return  <code>MailTansportProtocolEnum</code>-Objekt, wenn fuer diese
   *          Protokoll-ID ein Mail-Tansport-Protokoll gefunden wurde;
   *          <code>null</code>, wenn kein Mail-Tansport-Protokoll gefunden
   *          wurde. 
   */
  public static MailTransportProtocolEnum byProtocolId(String protocolId) {

    for(int ii = 0; ii < allValues.length; ii++) {

      if(allValues[ii].protocolId.equals(protocolId)) {

        return(allValues[ii]);
      }
    }

    return(null);
  }

}
